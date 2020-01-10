package com.example.accidentreport.start;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.accidentreport.R;
import com.example.accidentreport.domain.User;
import com.example.accidentreport.login.RegisterActivity;
import com.example.accidentreport.report.AccidentReportActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button buttonNewPart;
    Button buttonMyParts;

    private User userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RECUPERAR USUARIO LOGGEADO
        userLogged = (User) getIntent().getSerializableExtra("userLogged");

        buttonNewPart = findViewById(R.id.newPartButton);
        buttonNewPart.setOnClickListener(this);

        buttonMyParts = findViewById(R.id.myPartsButton);
        buttonMyParts.setOnClickListener(this);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("userLogged", userLogged);
                startActivity(intent);
                return true;
            case R.id.action_shutdown:
                Toast.makeText(this, "Has pinchado en el boton Shutdown", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newPartButton:
                Intent intent = new Intent(this, AccidentReportActivity.class);
                intent.putExtra("userLogged", userLogged);
                startActivity(intent);
                break;
            case R.id.myPartsButton:
                Toast.makeText(this, "NO ESTA IMPLEMENTADO SUBNORMAL JAJAJA", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
