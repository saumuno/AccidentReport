package com.example.accidentreport.report;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.accidentreport.utils.AccidentReportContract;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AccidentReportActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;


    TextView txtlocation;
    Button buttonLocation;

    Button saveButton;
    //Button updateButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_report);

        //RECUPERAR USUARIO LOGGEADO
        userLogged = (User) getIntent().getSerializableExtra("userLogged");

        txtlocation = findViewById(R.id.location);

        reason = findViewById(R.id.reason);
        id = findViewById(R.id.id);

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

        //updateButton = findViewById(R.id.updateButton);
        //updateButton.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
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
                //case R.id.updateButton:
                AccidentReport accidentReport = new AccidentReport();

                //  if (id.getText().toString().isEmpty()) {
                accidentReport.setId(UUID.randomUUID().toString());
                //}else {
                //  accidentReport.setId(id.getText().toString());
                //}
                accidentReport.setReasonAccident(reason.getText().toString());
                accidentReport.setLocation(txtlocation.getText().toString());
                //imageView.getDrawable().toString();

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
                    values.put(AccidentReportContract.TableAccidentReportColumns.REASON_ACCIDENT, accidentReport.getReasonAccident());
                    values.put(AccidentReportContract.TableAccidentReportColumns.LOCATION, accidentReport.getLocation());

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

                    //switch (v.getId()) {
                    //  case R.id.saveButton:
                    long insertRows = db.insert(AccidentReportContract.TABLE_ACCIDENT_REPORT, null, values);

                    if (insertRows == -1) {
                        Toast.makeText(this, "Error al registrar el parte", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Parte registrado correctamente", Toast.LENGTH_LONG).show();
                    }
                    // break;
                       /* case R.id.updateButton:
                            long updateRow = db.update(AccidentReportContract.TABLE_ACCIDENT_REPORT, values, "id= ?", new String[]{id.getText().toString()});

                            if (updateRow == -1) {
                                Toast.makeText(this, "Error al actualizar el parte", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "Parte actualizado correctamente", Toast.LENGTH_LONG).show();
                            }
                            break;*/
                    //}
                } else {
                    Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    public void getUbication() {
        LocationManager locationManager = (LocationManager) AccidentReportActivity.this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    txtlocation.setText((address.get(0).getAddressLine(0)));
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
        // Register the listener with the Location Manager to receive location updates
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

        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(file);
            }
        }
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

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


}
