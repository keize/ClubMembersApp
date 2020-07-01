package com.example.myclubmembers.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ClubContract {

    public static final int DATABASE_VERSION = 1;
    public static final String NAME_TABLE = "members";


    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.myclubmembers.data";
    public static final String PATH_MEMBERS = "members";


    public static final Uri BASE_CONTENT_URI =
            Uri.parse(SCHEME + AUTHORITY);


    private ClubContract() {

    }

    public static final class MemberEntry implements BaseColumns {


        public static final String KEY_ID = BaseColumns._ID;
        public static final String KEY_FIRSTNAME = "name";
        public static final String KEY_LASTNAME = "lastname";
        public static final String KEY_GENDER = "gender";
        public static final String KEY_SPORT = "sport";
        public static final String KEY_PHONE = "phone";


        public static final String GENDER_UNKNOWN = "unknown";
        public static final String GENDER_MALE = "male";
        public static final String GENDER_FEMALE = "female";

        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS);

        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;
        public static final String CONTENT_SINGLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;
        // константы MIME типов для метода getType()

    }


}