package com.example.myclubmembers;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.myclubmembers.data.ClubContract;

import java.util.ArrayList;

public class AddMemberActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText sportEditText;
    private EditText phoneEditText;
    private Spinner genderSpinner;
    private String gender = "Unknown";

    private ArrayAdapter spinnerAdapter;
    private ArrayList spinnerArrayList;

    Uri currentMemberUri;


    private static final int EDIT_MEMBER_LOADER = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Intent intent = getIntent();
        currentMemberUri = intent.getData();

        if (currentMemberUri == null) {
            setTitle("Add member");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit a member");
            getSupportLoaderManager().initLoader(EDIT_MEMBER_LOADER, null, this);

        }


        firstNameEditText = findViewById(R.id.firstNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        sportEditText = findViewById(R.id.sportEditText);
        genderSpinner = findViewById(R.id.genderSpinner);

        spinnerArrayList = new ArrayList(); //создание списка для спиннера
        spinnerArrayList.add("Unknown");
        spinnerArrayList.add("Male");
        spinnerArrayList.add("Female");

        spinnerAdapter = new ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item, spinnerArrayList);            //связь списка и адаптера
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        genderSpinner.setAdapter(spinnerAdapter); // установка адаптера в спиннер

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selectedGender)) {

                    if (selectedGender.equals("Male")) {
                        gender = ClubContract.MemberEntry.GENDER_MALE;
                    } else if (selectedGender.equals("Female")) {
                        gender = ClubContract.MemberEntry.GENDER_FEMALE;
                    } else {
                        gender = ClubContract.MemberEntry.GENDER_UNKNOWN;
                    }
                }
            } //выбор значений спиннера

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = ClubContract.MemberEntry.GENDER_UNKNOWN;

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_member_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        if (currentMemberUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_member);
            menuItem.setVisible(false);
        }

        return true;
    } // убрать удалить члена клуба, если запись только зодается

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_member:
                saveMember();
                return true;

            case R.id.delete_member:
                showDeleteMemberDialog();
                return true;


            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void saveMember() {

        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String sport = sportEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ClubContract.MemberEntry.KEY_FIRSTNAME, firstName);
        contentValues.put(ClubContract.MemberEntry.KEY_LASTNAME, lastName);
        contentValues.put(ClubContract.MemberEntry.KEY_SPORT, sport);
        contentValues.put(ClubContract.MemberEntry.KEY_GENDER, gender);
        contentValues.put(ClubContract.MemberEntry.KEY_PHONE, phone);

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this,
                    "Input the first name",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this,
                    "Input the last name",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(sport)) {
            Toast.makeText(this,
                    "Input the sport",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this,
                    "Input the phone",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (gender == ClubContract.MemberEntry.GENDER_UNKNOWN) {
            Toast.makeText(this,
                    "Choose the gender",
                    Toast.LENGTH_LONG).show();
            return;
        }


        if (currentMemberUri == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(ClubContract.MemberEntry.CONTENT_URI,
                    contentValues);

            if (uri == null) {
                Toast.makeText(this,
                        "Insertion of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Data saved", Toast.LENGTH_LONG).show();

            }
        } else {
            int rowsChanged = getContentResolver().update(currentMemberUri,
                    contentValues, null, null);

            if (rowsChanged == 0) {
                Toast.makeText(this,
                        "Saving of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Member updated", Toast.LENGTH_LONG).show();
            }
        }


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                ClubContract.MemberEntry._ID,
                ClubContract.MemberEntry.KEY_FIRSTNAME,
                ClubContract.MemberEntry.KEY_LASTNAME,
                ClubContract.MemberEntry.KEY_GENDER,
                ClubContract.MemberEntry.KEY_SPORT,
                ClubContract.MemberEntry.KEY_PHONE
        };
        return new CursorLoader(this,
                currentMemberUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {
            int firstNameColumnIndex = cursor.getColumnIndex(ClubContract.MemberEntry.KEY_FIRSTNAME);
            int lastNameColumnIndex = cursor.getColumnIndex(ClubContract.MemberEntry.KEY_LASTNAME);
            int genderColumnIndex = cursor.getColumnIndex(ClubContract.MemberEntry.KEY_GENDER);
            int sportColumnIndex = cursor.getColumnIndex(ClubContract.MemberEntry.KEY_SPORT);
            int phoneColumnIndex = cursor.getColumnIndex(ClubContract.MemberEntry.KEY_PHONE);

            String firstName = cursor.getString(firstNameColumnIndex);
            String lastName = cursor.getString(lastNameColumnIndex);
            String gender = cursor.getString(genderColumnIndex);
            String sport = cursor.getString(sportColumnIndex);
            String phone = cursor.getString(phoneColumnIndex);

            firstNameEditText.setText(firstName);
            lastNameEditText.setText(lastName);
            sportEditText.setText(sport);
            phoneEditText.setText(phone);

            switch (gender) {
                case ClubContract.MemberEntry.GENDER_MALE:
                    genderSpinner.setSelection(1);
                    break;
                case ClubContract.MemberEntry.GENDER_FEMALE:
                    genderSpinner.setSelection(2);
                    break;
                case ClubContract.MemberEntry.GENDER_UNKNOWN:
                    genderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void showDeleteMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete the member?");
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMember();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show(); // алерт при удалении записи
    }

    private void deleteMember() {
        if (currentMemberUri != null) {
            int rowsDeleted = getContentResolver().delete(currentMemberUri,
                    null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this,
                        "Deleting of data from the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Member is deleted",
                        Toast.LENGTH_LONG).show();
            }

            finish();
        }
    }
}