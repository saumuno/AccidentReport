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
import com.example.accidentreport.domain.User;
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

        register_username = findViewById(R.id.register_username);
        register_password = findViewById(R.id.register_password);
        register_name = findViewById(R.id.register_name);
        register_surnames = findViewById(R.id.register_surnames);
        register_dni = findViewById(R.id.register_dni);
        register_phone = findViewById(R.id.register_phone);

        register_registerButton = findViewById(R.id.register_registerButton);
        register_registerButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String username = register_username.getText().toString();
        String pass = register_password.getText().toString();
        String name = register_name.getText().toString();
        String surnames = register_surnames.getText().toString();
        String dni = register_dni.getText().toString();
        String phone = register_phone.getText().toString();

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(pass);
        newUser.setName(name);
        newUser.setSurnames(surnames);
        newUser.setDni(dni);
        newUser.setPhone(phone);

        if (validateRegister(newUser)) {
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(AccidentReportContract.TableUserColumns.USERNAME, username);
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
        } else {
            Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateRegister(User newUser) {
        return !newUser.getUsername().isEmpty() &&
                !newUser.getPassword().isEmpty() &&
                !newUser.getName().isEmpty() &&
                !newUser.getSurnames().isEmpty() &&
                !newUser.getDni().isEmpty() &&
                !newUser.getPhone().isEmpty();
    }

}
