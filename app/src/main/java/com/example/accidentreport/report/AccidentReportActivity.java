package com.example.accidentreport.report;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.accidentreport.R;
import com.example.accidentreport.database.DbHelper;
import com.example.accidentreport.domain.AccidentReport;
import com.example.accidentreport.domain.User;
import com.example.accidentreport.start.MainActivity;
import com.example.accidentreport.utils.AccidentReportContract;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AccidentReportActivity extends AppCompatActivity implements View.OnClickListener {

    AccidentReport accidentReport;

    TextView txtlocation;
    Button buttonLocation;

    Button saveButton;

    TextView id;
    EditText reason;

    EditText name_carA;
    EditText surnames_carA;
    EditText phone_carA;
    EditText dni_carA;
    EditText registration_carA;

    EditText name_carB;
    EditText surnames_carB;
    EditText phone_carB;
    EditText dni_carB;
    EditText registration_carB;


    private Button takePictureButton;
    private ImageView imageView;
    private Uri file;

    private User userLogged;
    private AccidentReport accidentReportDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_report);

        userLogged = (User) getIntent().getSerializableExtra("userLogged");
        String isNewPart = getIntent().getStringExtra("newPart");
        String reportId = getIntent().getStringExtra("reportId");

        txtlocation = findViewById(R.id.location);
        reason = findViewById(R.id.reason);
        id = findViewById(R.id.id_accident_reason);

        name_carA = findViewById(R.id.name_carA);
        surnames_carA = findViewById(R.id.surnames_carA);
        phone_carA = findViewById(R.id.phone_carA);
        dni_carA = findViewById(R.id.dni_carA);
        registration_carA = findViewById(R.id.registration_carA);

        name_carB = findViewById(R.id.name_carB);
        surnames_carB = findViewById(R.id.surnames_carB);
        phone_carB = findViewById(R.id.phone_carB);
        dni_carB = findViewById(R.id.dni_carB);
        registration_carB = findViewById(R.id.registration_carB);


        buttonLocation = findViewById(R.id.locationButton);
        buttonLocation.setOnClickListener(this);

        takePictureButton = findViewById(R.id.button_image);
        imageView = findViewById(R.id.imageview);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        if (userLogged != null) {
            if ("true".equalsIgnoreCase(isNewPart)) {
                accidentReport = new AccidentReport();
                loadUserLoggerdData();
            } else {
                if(reportId.equals("0")){
                    accidentReportDB = myLastReport(userLogged.getUsername());
                }else{
                    accidentReportDB = getReport(reportId);
                }
                if (accidentReportDB != null) {
                    loadAccidentReportData();
                    disabledEditMode();
                } else {
                    accidentReport = new AccidentReport();
                    loadUserLoggerdData();
                }
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locationButton:
                int permissionCheck = ContextCompat.checkSelfPermission(AccidentReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                } else {
                    getUbication();
                }
                break;
            case R.id.saveButton:

                accidentReport.setReasonAccident(reason.getText().toString());
                accidentReport.setLocation(txtlocation.getText().toString());

                accidentReport.setNameA(name_carA.getText().toString());
                accidentReport.setSurnamesA(surnames_carA.getText().toString());
                accidentReport.setDniA(dni_carA.getText().toString());
                accidentReport.setPhoneA(phone_carA.getText().toString());
                accidentReport.setRegistrationA(registration_carA.getText().toString());

                accidentReport.setNameB(name_carB.getText().toString());
                accidentReport.setSurnamesB(surnames_carB.getText().toString());
                accidentReport.setDniB(dni_carB.getText().toString());
                accidentReport.setPhoneB(phone_carB.getText().toString());
                accidentReport.setRegistrationB(registration_carB.getText().toString());

                if (validateAccidentReport(accidentReport)) {
                    DbHelper dbHelper = new DbHelper(this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();


                    values.put(AccidentReportContract.TableAccidentReportColumns.ID, accidentReport.getId());
                    values.put(AccidentReportContract.TableAccidentReportColumns.USERNAME_PART, userLogged.getUsername());
                    values.put(AccidentReportContract.TableAccidentReportColumns.REASON_ACCIDENT, accidentReport.getReasonAccident());
                    values.put(AccidentReportContract.TableAccidentReportColumns.LOCATION, accidentReport.getLocation());

                    //SAVE PHOTO
                    byte[] data = getBitmapAsByteArray(accidentReport.getImage());
                    values.put(AccidentReportContract.TableAccidentReportColumns.IMAGE, data);

                    values.put(AccidentReportContract.TableAccidentReportColumns.NAME_A, accidentReport.getNameA());
                    values.put(AccidentReportContract.TableAccidentReportColumns.SURNAMES_A, accidentReport.getSurnamesA());
                    values.put(AccidentReportContract.TableAccidentReportColumns.PHONE_A, accidentReport.getPhoneA());
                    values.put(AccidentReportContract.TableAccidentReportColumns.DNI_A, accidentReport.getDniA());
                    values.put(AccidentReportContract.TableAccidentReportColumns.REGISTRATION_A, accidentReport.getRegistrationA());

                    values.put(AccidentReportContract.TableAccidentReportColumns.NAME_B, accidentReport.getNameB());
                    values.put(AccidentReportContract.TableAccidentReportColumns.SURNAMES_B, accidentReport.getSurnamesB());
                    values.put(AccidentReportContract.TableAccidentReportColumns.PHONE_B, accidentReport.getPhoneB());
                    values.put(AccidentReportContract.TableAccidentReportColumns.DNI_B, accidentReport.getDniB());
                    values.put(AccidentReportContract.TableAccidentReportColumns.REGISTRATION_B, accidentReport.getRegistrationB());


                    long insertRows = db.insert(AccidentReportContract.TABLE_ACCIDENT_REPORT, null, values);

                    if (insertRows == -1) {
                        Toast.makeText(this, getString(R.string.report_save_error), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, getString(R.string.report_save_correct), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("userLogged", userLogged);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, getString(R.string.fill_in_all_fields), Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public void getUbication() {
        LocationManager locationManager = (LocationManager) AccidentReportActivity.this.getSystemService(Context.LOCATION_SERVICE);


        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    txtlocation.setText((address.get(0).getAddressLine(0)));
                    txtlocation.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        int permissionCheck = ContextCompat.checkSelfPermission(AccidentReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);// para el emulador
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);// para el movil
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
                accidentReport.setImage(bitmap);
            }
        }
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Camera");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    private Boolean validateAccidentReport(AccidentReport accidentReport) {
        return !accidentReport.getReasonAccident().isEmpty() &&
                !accidentReport.getLocation().isEmpty() &&
                accidentReport.getImage() != null &&
                !accidentReport.getNameA().isEmpty() &&
                !accidentReport.getSurnamesA().isEmpty() &&
                !accidentReport.getDniA().isEmpty() &&
                !accidentReport.getPhoneA().isEmpty() &&
                !accidentReport.getRegistrationA().isEmpty() &&
                !accidentReport.getNameB().isEmpty() &&
                !accidentReport.getSurnamesB().isEmpty() &&
                !accidentReport.getDniB().isEmpty() &&
                !accidentReport.getPhoneB().isEmpty() &&
                !accidentReport.getRegistrationB().isEmpty();
    }

    private void loadUserLoggerdData() {
        accidentReport.setId(String.valueOf(new Date().getTime()));
        id.setText(accidentReport.getId());
        name_carA.setText(userLogged.getName());
        surnames_carA.setText(userLogged.getSurnames());
        phone_carA.setText(userLogged.getPhone());
        dni_carA.setText(userLogged.getDni());
    }

    private void loadAccidentReportData() {
        id.setText(accidentReportDB.getId());
        reason.setText(accidentReportDB.getReasonAccident());
        txtlocation.setText(accidentReportDB.getLocation());
        imageView.setImageBitmap(accidentReportDB.getImage());

        name_carA.setText(accidentReportDB.getNameA());
        surnames_carA.setText(accidentReportDB.getSurnamesA());
        dni_carA.setText(accidentReportDB.getDniA());
        phone_carA.setText(accidentReportDB.getPhoneA());
        registration_carA.setText(accidentReportDB.getRegistrationA());

        name_carB.setText(accidentReportDB.getNameB());
        surnames_carB.setText(accidentReportDB.getSurnamesB());
        dni_carB.setText(accidentReportDB.getDniB());
        phone_carB.setText(accidentReportDB.getPhoneB());
        registration_carB.setText(accidentReportDB.getRegistrationB());
    }

    public AccidentReport myLastReport(String username) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] accidentReportList = {
                AccidentReportContract.TableAccidentReportColumns.ID,
                AccidentReportContract.TableAccidentReportColumns.USERNAME_PART,
                AccidentReportContract.TableAccidentReportColumns.REASON_ACCIDENT,
                AccidentReportContract.TableAccidentReportColumns.LOCATION,
                AccidentReportContract.TableAccidentReportColumns.IMAGE,
                AccidentReportContract.TableAccidentReportColumns.SURNAMES_A,
                AccidentReportContract.TableAccidentReportColumns.NAME_A,
                AccidentReportContract.TableAccidentReportColumns.PHONE_A,
                AccidentReportContract.TableAccidentReportColumns.DNI_A,
                AccidentReportContract.TableAccidentReportColumns.REGISTRATION_A,
                AccidentReportContract.TableAccidentReportColumns.SURNAMES_B,
                AccidentReportContract.TableAccidentReportColumns.NAME_B,
                AccidentReportContract.TableAccidentReportColumns.PHONE_B,
                AccidentReportContract.TableAccidentReportColumns.DNI_B,
                AccidentReportContract.TableAccidentReportColumns.REGISTRATION_B
        };

        String selection = AccidentReportContract.TableAccidentReportColumns.USERNAME_PART + " = ?";
        String[] selectionArgs = {username};

        String sortOrder = AccidentReportContract.DEFAULT_SORT_ACCIDENT_REPORT;

        Cursor cursor = db.query(
                AccidentReportContract.TABLE_ACCIDENT_REPORT,
                accidentReportList,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            accidentReport = new AccidentReport();
            accidentReport.setId(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.ID)));
            accidentReport.setUsernamePart(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.USERNAME_PART)));
            accidentReport.setReasonAccident(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.REASON_ACCIDENT)));
            accidentReport.setLocation(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.LOCATION)));

            //GET IMAGE
            byte[] imgByte = cursor.getBlob(cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.IMAGE));
            accidentReport.setImage(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));

            accidentReport.setSurnamesA(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.SURNAMES_A)));
            accidentReport.setNameA(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.NAME_A)));
            accidentReport.setPhoneA(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.PHONE_A)));
            accidentReport.setDniA(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.DNI_A)));
            accidentReport.setRegistrationA(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.REGISTRATION_A)));
            accidentReport.setSurnamesB(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.SURNAMES_B)));
            accidentReport.setNameB(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.NAME_B)));
            accidentReport.setPhoneB(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.PHONE_B)));
            accidentReport.setDniB(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.DNI_B)));
            accidentReport.setRegistrationB(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.REGISTRATION_B)));
        }
        cursor.close();

        if (accidentReport != null) {
            return accidentReport;
        } else {
            Toast.makeText(this, getString(R.string.not_reports), Toast.LENGTH_LONG).show();
            return null;
        }

    }

    public AccidentReport getReport(String id) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] accidentReportList = {
                AccidentReportContract.TableAccidentReportColumns.ID,
                AccidentReportContract.TableAccidentReportColumns.USERNAME_PART,
                AccidentReportContract.TableAccidentReportColumns.REASON_ACCIDENT,
                AccidentReportContract.TableAccidentReportColumns.LOCATION,
                AccidentReportContract.TableAccidentReportColumns.IMAGE,
                AccidentReportContract.TableAccidentReportColumns.SURNAMES_A,
                AccidentReportContract.TableAccidentReportColumns.NAME_A,
                AccidentReportContract.TableAccidentReportColumns.PHONE_A,
                AccidentReportContract.TableAccidentReportColumns.DNI_A,
                AccidentReportContract.TableAccidentReportColumns.REGISTRATION_A,
                AccidentReportContract.TableAccidentReportColumns.SURNAMES_B,
                AccidentReportContract.TableAccidentReportColumns.NAME_B,
                AccidentReportContract.TableAccidentReportColumns.PHONE_B,
                AccidentReportContract.TableAccidentReportColumns.DNI_B,
                AccidentReportContract.TableAccidentReportColumns.REGISTRATION_B
        };

        String selection = AccidentReportContract.TableAccidentReportColumns.ID + " = ?";
        String[] selectionArgs = {id};

        String sortOrder = AccidentReportContract.DEFAULT_SORT_ACCIDENT_REPORT;

        Cursor cursor = db.query(
                AccidentReportContract.TABLE_ACCIDENT_REPORT,
                accidentReportList,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            accidentReport = new AccidentReport();
            accidentReport.setId(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.ID)));
            accidentReport.setUsernamePart(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.USERNAME_PART)));
            accidentReport.setReasonAccident(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.REASON_ACCIDENT)));
            accidentReport.setLocation(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.LOCATION)));

            //GET IMAGE
            byte[] imgByte = cursor.getBlob(cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.IMAGE));
            accidentReport.setImage(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));

            accidentReport.setSurnamesA(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.SURNAMES_A)));
            accidentReport.setNameA(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.NAME_A)));
            accidentReport.setPhoneA(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.PHONE_A)));
            accidentReport.setDniA(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.DNI_A)));
            accidentReport.setRegistrationA(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.REGISTRATION_A)));
            accidentReport.setSurnamesB(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.SURNAMES_B)));
            accidentReport.setNameB(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.NAME_B)));
            accidentReport.setPhoneB(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.PHONE_B)));
            accidentReport.setDniB(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.DNI_B)));
            accidentReport.setRegistrationB(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableAccidentReportColumns.REGISTRATION_B)));
        }
        cursor.close();

        if (accidentReport != null) {
            return accidentReport;
        } else {
            Toast.makeText(this, getString(R.string.not_reports), Toast.LENGTH_LONG).show();
            return null;
        }

    }

    private void disabledEditMode() {
        saveButton.setVisibility(View.GONE);
        buttonLocation.setVisibility(View.GONE);
        takePictureButton.setVisibility(View.GONE);
        reason.setEnabled(Boolean.FALSE);
        name_carA.setEnabled(Boolean.FALSE);
        surnames_carA.setEnabled(Boolean.FALSE);
        phone_carA.setEnabled(Boolean.FALSE);
        dni_carA.setEnabled(Boolean.FALSE);
        registration_carA.setEnabled(Boolean.FALSE);
        name_carB.setEnabled(Boolean.FALSE);
        surnames_carB.setEnabled(Boolean.FALSE);
        phone_carB.setEnabled(Boolean.FALSE);
        dni_carB.setEnabled(Boolean.FALSE);
        registration_carB.setEnabled(Boolean.FALSE);
        imageView.setVisibility(View.VISIBLE);
        txtlocation.setVisibility(View.VISIBLE);
    }


}
