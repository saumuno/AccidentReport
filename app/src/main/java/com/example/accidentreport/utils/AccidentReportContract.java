package com.example.accidentreport.utils;

import android.provider.BaseColumns;

public class AccidentReportContract {
    public static final String DB_NAME = "accidentReport.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_USER = "user";
    public static final String TABLE_ACCIDENT_REPORT = "accident_report";
    public static final String DEFAULT_SORT_USER = TableUserColumns.USERNAME + " DESC";
    public static final String DEFAULT_SORT_ACCIDENT_REPORT = TableAccidentReportColumns.ID + " DESC";

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
        public static final String USERNAME_PART = "usernamePart";

        public static final String REASON_ACCIDENT = "reasonAccident";
        public static final String LOCATION = "location";
        public static final String IMAGE = "image";

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
