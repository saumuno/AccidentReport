package com.example.accidentreport.login;

import android.content.ContentValues;
import android.content.Intent;
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
    Button register_updateButton;
    EditText register_username;
    EditText register_password;
    EditText register_name;
    EditText register_surnames;
    EditText register_dni;
    EditText register_phone;

    private User userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userLogged = (User) getIntent().getSerializableExtra("userLogged");

        register_username = findViewById(R.id.register_username);
        register_password = findViewById(R.id.register_password);
        register_name = findViewById(R.id.register_name);
        register_surnames = findViewById(R.id.register_surnames);
        register_dni = findViewById(R.id.register_dni);
        register_phone = findViewById(R.id.register_phone);

        register_registerButton = findViewById(R.id.register_registerButton);
        register_registerButton.setOnClickListener(this);

        register_updateButton = findViewById(R.id.register_updateButton);
        register_updateButton.setOnClickListener(this);


        if (userLogged != null) {
            register_username.setText(userLogged.getUsername());
            register_username.setEnabled(false);
            register_password.setText(userLogged.getPassword());
            register_name.setText(userLogged.getName());
            register_surnames.setText(userLogged.getSurnames());
            register_dni.setText(userLogged.getDni());
            register_phone.setText(userLogged.getPhone());
            register_registerButton.setVisibility(View.GONE);
            register_updateButton.setVisibility(View.VISIBLE);
        }

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
            switch (v.getId()) {
                case R.id.register_registerButton:
                    long insertRows = db.insert(AccidentReportContract.TABLE_USER, null, values);

                    if (insertRows == -1) {
                        Toast.makeText(this, getString(R.string.user_register_error), Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        Toast.makeText(this, getString(R.string.user_register_correct), Toast.LENGTH_LONG).show();

                    }
                    break;
                case R.id.register_updateButton:
                    long updateRow = db.update(AccidentReportContract.TABLE_USER, values, "username= ?", new String[]{username});

                    if (updateRow == -1) {
                        Toast.makeText(this,  getString(R.string.user_update_error), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this,  getString(R.string.user_update_correct), Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        } else {
            Toast.makeText(this, getString(R.string.fill_in_all_fields), Toast.LENGTH_LONG).show();
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
