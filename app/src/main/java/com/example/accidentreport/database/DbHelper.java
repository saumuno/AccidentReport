package com.example.accidentreport.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.accidentreport.utils.AccidentReportContract;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    public DbHelper(Context context) {
        super(context, AccidentReportContract.DB_NAME, null, AccidentReportContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlUser = String.format("create table %s (%s text primary key, %s text, %s text, %s text, %s text, %s text)",
                AccidentReportContract.TABLE_USER,
                AccidentReportContract.TableUserColumns.USERNAME,
                AccidentReportContract.TableUserColumns.PASSWORD,
                AccidentReportContract.TableUserColumns.NAME,
                AccidentReportContract.TableUserColumns.SURNAMES,
                AccidentReportContract.TableUserColumns.DNI,
                AccidentReportContract.TableUserColumns.PHONE);

        Log.d(TAG,"onCreate con SQL: " + sqlUser);
        db.execSQL(sqlUser);

        String sqlAccidentReport = String.format("create table %s (%s text primary key, %s text," +
                        " %s text, %s text, %s BLOB, %s text, %s text, %s text, %s text, %s text, %s text," +
                        " %s text, %s text, %s text, %s text)",
              AccidentReportContract.TABLE_ACCIDENT_REPORT,
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
                AccidentReportContract.TableAccidentReportColumns.REGISTRATION_B);

        Log.d(TAG,"onCreate con SQL: " + sqlAccidentReport);
        db.execSQL(sqlAccidentReport);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + AccidentReportContract.TABLE_USER);
        db.execSQL("drop table if exists " + AccidentReportContract.TABLE_ACCIDENT_REPORT);

        Log.d(TAG,"onUpgrade");
        onCreate(db);
    }
}
