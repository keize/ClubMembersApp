package com.example.myclubmembers.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbOpenHelper extends SQLiteOpenHelper {

    public DbOpenHelper(Context context) {
        super(context, ClubContract.NAME_TABLE, null, ClubContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MEMBERS_TABLE = "CREATE TABLE " + ClubContract.NAME_TABLE + "("
                + ClubContract.MemberEntry.KEY_ID + " INTEGER PRIMARY KEY,"
                + ClubContract.MemberEntry.KEY_FIRSTNAME + " TEXT,"
                + ClubContract.MemberEntry.KEY_LASTNAME + " TEXT,"
                + ClubContract.MemberEntry.KEY_GENDER + " INTEGER NOT NULL,"
                + ClubContract.MemberEntry.KEY_SPORT + " TEXT,"
                + ClubContract.MemberEntry.KEY_PHONE + " TEXT" + ")";

        db.execSQL(CREATE_MEMBERS_TABLE);
        // создание базы данных с готовыми полями
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ClubContract.NAME_TABLE);
        onCreate(db);
        //обновление базы данных (старая удаляется и замещается новой)
    }
}
