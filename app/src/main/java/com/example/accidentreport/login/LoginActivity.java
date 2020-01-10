package com.example.accidentreport.login;

import android.content.Intent;
import android.database.Cursor;
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
import com.example.accidentreport.start.MainActivity;
import com.example.accidentreport.utils.AccidentReportContract;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginButton;
    Button registerButton;
    EditText username;
    EditText password;

    private User userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        switch (v.getId()) {
            case R.id.loginButton:
                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(this, "Introduzca usuario y password", Toast.LENGTH_LONG).show();
                } else {
                    login(user, pass);
                }
                break;
            case R.id.registerButton:
                register();
                break;

        }
    }

    public void login(String user, String pass) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] userList = {
                AccidentReportContract.TableUserColumns.USERNAME,
                AccidentReportContract.TableUserColumns.PASSWORD,
                AccidentReportContract.TableUserColumns.NAME,
                AccidentReportContract.TableUserColumns.SURNAMES,
                AccidentReportContract.TableUserColumns.DNI,
                AccidentReportContract.TableUserColumns.PHONE
        };

        String selection = AccidentReportContract.TableUserColumns.USERNAME + " = ?";
        String[] selectionArgs = {user};

        String sortOrder = AccidentReportContract.DEFAULT_SORT_USER;

        Cursor cursor = db.query(
                AccidentReportContract.TABLE_USER,
                userList,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            userLogged = new User();
            userLogged.setUsername(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableUserColumns.USERNAME)));
            userLogged.setPassword(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableUserColumns.PASSWORD)));
            userLogged.setName(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableUserColumns.NAME)));
            userLogged.setSurnames(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableUserColumns.SURNAMES)));
            userLogged.setDni(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableUserColumns.DNI)));
            userLogged.setPhone(cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableUserColumns.PHONE)));

        }
        cursor.close();
        if (userLogged != null && pass.equals(userLogged.getPassword())) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userLogged", userLogged);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Usuario o password incorrecto", Toast.LENGTH_LONG).show();
        }
    }

    public void register() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
