package com.example.myclubmembers;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.myclubmembers.data.ClubContract;


public class MemberCursorAdapter extends CursorAdapter {
    public MemberCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.member_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView firstnameTextView = (TextView) view.findViewById(R.id.memberFirstName);
        TextView lastnameTextView = (TextView) view.findViewById(R.id.memberLastName);
        TextView genderTextView = (TextView) view.findViewById(R.id.memberGender);
        TextView sportTextView = (TextView) view.findViewById(R.id.memberSport);
        TextView phoneTextView = (TextView) view.findViewById(R.id.memberPhone);

        String firstname = cursor.getString(cursor.getColumnIndexOrThrow(ClubContract.MemberEntry.KEY_FIRSTNAME));
        String lastname = cursor.getString(cursor.getColumnIndexOrThrow(ClubContract.MemberEntry.KEY_LASTNAME));
        String gender = cursor.getString(cursor.getColumnIndexOrThrow(ClubContract.MemberEntry.KEY_GENDER));
        String sport = cursor.getString(cursor.getColumnIndexOrThrow(ClubContract.MemberEntry.KEY_SPORT));
        String phone = cursor.getString(cursor.getColumnIndexOrThrow(ClubContract.MemberEntry.KEY_PHONE));

        firstnameTextView.setText(firstname);
        lastnameTextView.setText(lastname);
        genderTextView.setText(gender);
        sportTextView.setText(sport);
        phoneTextView.setText(phone);

    }

}
