package com.example.accidentreport.start;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.accidentreport.R;
import com.example.accidentreport.domain.User;
import com.example.accidentreport.login.LoginActivity;
import com.example.accidentreport.login.RegisterActivity;
import com.example.accidentreport.report.AccidentReportActivity;
import com.example.accidentreport.report.MyReportsActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button buttonNewReport;
    Button buttonMyReports;
    Button buttonMyLastReport;
    MenuItem userId;

    private User userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RECUPERAR USUARIO LOGGEADO
        userLogged = (User) getIntent().getSerializableExtra("userLogged");


        buttonNewReport = findViewById(R.id.newReportButton);
        buttonNewReport.setOnClickListener(this);

        buttonMyReports = findViewById(R.id.myReportsButton);
        buttonMyReports.setOnClickListener(this);

        buttonMyLastReport = findViewById(R.id.myLastReportButton);
        buttonMyLastReport.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        userId = menu.findItem(R.id.userId);
        userId.setTitle(userLogged.getUsername());
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
                showAlert();
                return true;
            default:
                return false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newReportButton:
                Intent intent = new Intent(this, AccidentReportActivity.class);
                intent.putExtra("userLogged", userLogged);
                intent.putExtra("newPart", "true");
                intent.putExtra("reportId", "0");
                startActivity(intent);
                break;
            case R.id.myLastReportButton:
                Intent intentMyLastReport = new Intent(this, AccidentReportActivity.class);
                intentMyLastReport.putExtra("userLogged", userLogged);
                intentMyLastReport.putExtra("newPart", "false");
                intentMyLastReport.putExtra("reportId", "0");
                startActivity(intentMyLastReport);
                break;
            case R.id.myReportsButton:
                Intent intentMyReports = new Intent(this, MyReportsActivity.class);
                intentMyReports.putExtra("userLogged", userLogged);
                startActivity(intentMyReports);
        }
    }

    public void showAlert() {
        AlertDialog.Builder alertShutdown = new AlertDialog.Builder(this);
        alertShutdown.setMessage(getString(R.string.closed_sesion_answer));
        alertShutdown.setCancelable(true);
        final Context context = this;
        alertShutdown.setNegativeButton(
                getString(R.string.not),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alertShutdown.setPositiveButton(
                getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(context, LoginActivity.class));
                    }
                });


        AlertDialog alert = alertShutdown.create();
        alert.show();
    }

    public User getUserLogged() {
        return userLogged;
    }


}
