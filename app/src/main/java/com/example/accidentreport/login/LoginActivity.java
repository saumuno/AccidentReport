package com.example.accidentreport.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.accidentreport.R;
import com.example.accidentreport.database.DbHelper;
import com.example.accidentreport.utils.AccidentReportContract;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button loginButton;
    Button registerButton;
    EditText username;
    EditText password;

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
        switch (v.getId()){
            case R.id.loginButton:
                login(user,pass);
                break;
            case R.id.registerButton:
                register();
                break;

        }
    }

    public void login(String user, String pass){
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] userList = {
                AccidentReportContract.TableUserColumns.USERNAME,
                AccidentReportContract.TableUserColumns.PASSWORD
        };

        String selection = AccidentReportContract.TableUserColumns.USERNAME + " = ?";
        String[] selectionArgs = { user };

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

        String passDb = "";
        while(cursor.moveToNext()) {
            passDb = cursor.getString(
                    cursor.getColumnIndexOrThrow(AccidentReportContract.TableUserColumns.PASSWORD));

        }
        cursor.close();
        if(passDb.equals(pass)){
            Toast.makeText(this,"login CORRECTO: user: "+ user +" pass: "+ pass,Toast.LENGTH_LONG ).show();
        }else{
            Toast.makeText(this,"Usuario o password incorrecto",Toast.LENGTH_LONG ).show();
        }
    }

    public void register(){
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
