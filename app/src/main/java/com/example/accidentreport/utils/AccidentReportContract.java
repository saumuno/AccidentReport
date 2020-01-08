package com.example.accidentreport.utils;

import android.net.Uri;
import android.provider.BaseColumns;

public class AccidentReportContract {
    public static final String DB_NAME = "accidentReport.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_USER = "user";
    public static final String TABLE_ACCIDENT_REPORT = "accident_report";
    public static final String DEFAULT_SORT_USER = TableUserColumns.USERNAME + " DESC";
    public static final String DEFAULT_SORT_ACCIDENT_REPORT = TableAccidentReportColumns.ID + " DESC";

    // Constantes del content provider
// content://com.example.jadiego.yamba.StatusProvider/status
//    public static final String AUTHORITY = "com.example.accidentreport.contentProvider.StatusProvider";
//    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_USER);
//    public static final int STATUS_ITEM = 1;
//    public static final int STATUS_DIR = 2;

    public class TableUserColumns {
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String NAME = "name";
        public static final String SURNAMES = "surnames";
        public static final String DNI = "dni";
        public static final String PHONE = "phone";
    }

    public class TableAccidentReportColumns {
        public static final String ID = BaseColumns._ID;
        public static final String USERNAME = "username";
    }
}
