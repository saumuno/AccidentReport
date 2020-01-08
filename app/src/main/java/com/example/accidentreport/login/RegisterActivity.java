package com.example.accidentreport.login;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.accidentreport.R;
import com.example.accidentreport.database.DbHelper;
import com.example.accidentreport.utils.AccidentReportContract;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button register_registerButton;
    EditText register_username;
    EditText register_password;
    EditText register_name;
    EditText register_surnames;
    EditText register_dni;
    EditText register_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_registerButton = findViewById(R.id.register_registerButton);

        register_username = findViewById(R.id.register_username);
        register_password = findViewById(R.id.register_password);
        register_name = findViewById(R.id.register_name);
        register_surnames = findViewById(R.id.register_surnames);
        register_dni = findViewById(R.id.register_dni);
        register_phone = findViewById(R.id.register_phone);

        register_registerButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String user = register_username.getText().toString();
        String pass = register_password.getText().toString();
        String name = register_name.getText().toString();
        String surnames = register_surnames.getText().toString();
        String dni = register_dni.getText().toString();
        String phone = register_phone.getText().toString();

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AccidentReportContract.TableUserColumns.USERNAME, user);
        values.put(AccidentReportContract.TableUserColumns.PASSWORD, pass);
        values.put(AccidentReportContract.TableUserColumns.NAME, name);
        values.put(AccidentReportContract.TableUserColumns.SURNAMES, surnames);
        values.put(AccidentReportContract.TableUserColumns.DNI, dni);
        values.put(AccidentReportContract.TableUserColumns.PHONE, phone);


        long newRowId = db.insert(AccidentReportContract.TABLE_USER, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error al registrar el usuario: Username existente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_LONG).show();
        }
    }
}
