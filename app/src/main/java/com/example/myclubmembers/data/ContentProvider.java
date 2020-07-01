package com.example.myclubmembers.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


public class ContentProvider extends android.content.ContentProvider {

    DbOpenHelper appdatabaseHandler;
    private static final int MEMBERS = 111;
    private static final int MEMBER_ID = 222;


    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); // создание uri matcher

    static {
        uriMatcher.addURI(ClubContract.AUTHORITY, ClubContract.PATH_MEMBERS, MEMBERS);
        uriMatcher.addURI(ClubContract.AUTHORITY, ClubContract.PATH_MEMBERS + "/#", MEMBER_ID);

    }


    @Override
    public boolean onCreate() {
        appdatabaseHandler = new DbOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = appdatabaseHandler.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                cursor = db.query(ClubContract.NAME_TABLE, projection, selection,
                        selectionArgs, null, null, sortOrder);  // работа со всей таблицей
                break;

            case MEMBER_ID:
                selection = ClubContract.MemberEntry.KEY_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ClubContract.NAME_TABLE, projection, selection,     // работа с отдельными строками
                        selectionArgs, null, null, sortOrder);
                break;


            default:
                Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_SHORT).show();
                throw new IllegalArgumentException("Can't query incorrect URI "
                        + uri);


        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
        // метод query - для запроса данных из таблицы по опр параметрам
    }

    @Override
    public String getType(Uri uri) {  // выдает MIME типы

        SQLiteDatabase db = appdatabaseHandler.getWritableDatabase();
        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:

                return ClubContract.MemberEntry.CONTENT_MULTIPLE_ITEMS;

            case MEMBER_ID:

                return ClubContract.MemberEntry.CONTENT_SINGLE_ITEMS;

            default:
                Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_SHORT).show();
                throw new IllegalArgumentException("Can't delete this URI "
                        + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String firstName = contentValues.getAsString(ClubContract.MemberEntry.KEY_FIRSTNAME);
        if (firstName.isEmpty()) {
            throw new RuntimeException("You have to input last name");
        }

        String lastName = contentValues.getAsString(ClubContract.MemberEntry.KEY_LASTNAME);
        if (lastName.isEmpty()) {
            throw new RuntimeException("You have to input last name");
        }

        String gender = contentValues.getAsString(ClubContract.MemberEntry.KEY_GENDER);
        if (gender == null || !(gender == ClubContract.MemberEntry.GENDER_UNKNOWN || gender ==
                ClubContract.MemberEntry.GENDER_MALE || gender == ClubContract.MemberEntry.GENDER_FEMALE)) {
            throw new IllegalArgumentException
                    ("You have to input correct gender");
        }

        String sport = contentValues.getAsString(ClubContract.MemberEntry.KEY_SPORT);
        if (sport.isEmpty()) {
            throw new IllegalArgumentException("You have to input sport");
        }

        String phone = contentValues.getAsString(ClubContract.MemberEntry.KEY_PHONE);
        if (phone.isEmpty()) {
            throw new IllegalArgumentException("You have to input phone");
        }

        SQLiteDatabase db = appdatabaseHandler.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case MEMBERS:
                long id = db.insert(ClubContract.NAME_TABLE, null, contentValues);
                if (id == -1) {
                    Log.e("Insert method", "Insertion of data in the table failed for " + uri);
                    return null;

                }

                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);

            default:
                throw new IllegalArgumentException("Insertion of data in the table failed for "
                        + uri);
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;

        SQLiteDatabase db = appdatabaseHandler.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:

                rowsDeleted = db.delete(ClubContract.NAME_TABLE, selection,
                        selectionArgs);
                break;


            case MEMBER_ID:
                selection = ClubContract.MemberEntry.KEY_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsDeleted = db.delete(ClubContract.NAME_TABLE, selection, selectionArgs);
                break;


            default:
                throw new IllegalArgumentException("Can't delete this URI "
                        + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if (contentValues.containsKey(ClubContract.MemberEntry.KEY_FIRSTNAME)) {
            String firstName = contentValues.getAsString(ClubContract.MemberEntry.KEY_FIRSTNAME);
            if (firstName.isEmpty()) {
                throw new RuntimeException("You have to input last name");
            }

        }

        if (contentValues.containsKey(ClubContract.MemberEntry.KEY_LASTNAME)) {
            String lastName = contentValues.getAsString(ClubContract.MemberEntry.KEY_LASTNAME);
            if (lastName.isEmpty()) {
                throw new RuntimeException("You have to input last name");
            }
        }

        if (contentValues.containsKey(ClubContract.MemberEntry.KEY_GENDER)) {

            String gender = contentValues.getAsString(ClubContract.MemberEntry.KEY_GENDER);
            if (gender == null || !(gender == ClubContract.MemberEntry.GENDER_UNKNOWN || gender ==
                    ClubContract.MemberEntry.GENDER_MALE || gender == ClubContract.MemberEntry.GENDER_FEMALE)) {
                throw new IllegalArgumentException
                        ("You have to input correct gender");
            }
        }

        if (contentValues.containsKey(ClubContract.MemberEntry.KEY_SPORT)) {

            String sport = contentValues.getAsString(ClubContract.MemberEntry.KEY_SPORT);
            if (sport.isEmpty()) {
                throw new IllegalArgumentException("You have to input sport");
            }

        }

        if (contentValues.containsKey(ClubContract.MemberEntry.KEY_PHONE)) {
            String phone = contentValues.getAsString(ClubContract.MemberEntry.KEY_PHONE);
            if (phone.isEmpty()) {
                throw new IllegalArgumentException("You have to input phone");
            }

        }


        SQLiteDatabase db = appdatabaseHandler.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MEMBERS:

                rowsUpdated = db.update(ClubContract.NAME_TABLE, contentValues, selection, selectionArgs);

                break;


            case MEMBER_ID:
                selection = ClubContract.MemberEntry.KEY_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsUpdated = db.update(ClubContract.NAME_TABLE, contentValues, selection, selectionArgs);

                break;

            default:
                Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_SHORT).show();
                throw new IllegalArgumentException("Can't update this URI "
                        + uri);


        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
