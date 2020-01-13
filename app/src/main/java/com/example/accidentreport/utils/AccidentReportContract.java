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
        public static final String ID = "id";
        public static final String REASON_ACCIDENT = "reasonAccident";
        public static final String LOCATION = "location";

        public static final String SURNAMES_A = "surnamesA";
        public static final String NAME_A = "nameA";
        public static final String PHONE_A = "phoneA";
        public static final String DNI_A = "dniA";
        public static final String REGISTRATION_A = "registrationA";


        public static final String SURNAMES_B = "nameB";
        public static final String NAME_B = "surnamesB";
        public static final String PHONE_B = "phoneB";
        public static final String DNI_B = "dniB";
        public static final String REGISTRATION_B = "registrationB";
    }
}
