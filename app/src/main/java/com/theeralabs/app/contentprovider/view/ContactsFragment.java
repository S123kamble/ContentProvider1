package com.theeralabs.app.contentprovider.view;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.theeralabs.app.contentprovider.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {


    private static final int CONTACT_PERM_REQ = 1;

    public ContactsFragment() {
        // Required empty public constructor
    }


    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_contacts, container, false);
        init(v);
        // Inflate the layout for this fragment
        return v;
    }

    private ProgressDialog mDialog;
    private ListView mListView;
    private Handler updateDialog;

    private void init(View v) {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Reading contacts...");
        mDialog.setCancelable(false);
        updateDialog = new Handler();
        mListView = v.findViewById(R.id.list);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    android.Manifest.permission.READ_CONTACTS}, CONTACT_PERM_REQ);
        } else {
            mDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getContacts();
                }
            }).start();
        }
    }

    private int count;
    private Cursor cursor;
    private ArrayList<String> contactList;

    private void getContacts() {
        contactList = new ArrayList<>();

        //Top level
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        //Contents phone
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        //Contents email
        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String EMAIL = ContactsContract.CommonDataKinds.Email.DATA;

        ContentResolver contentResolver = getActivity().getContentResolver();
        cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
        assert cursor != null;
        final int cursorCount = cursor.getCount();
        if (cursor != null && cursorCount > 0) {
            count = 0;
            while (cursor.moveToNext()) {
                updateDialog.post(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.setMessage("Reading contacts : " + (count++) + "/" + cursorCount);
                    }
                });

                String contactID = cursor.getString(cursor.getColumnIndex(_ID));
                String contactName = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                int contactNumberCount = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                StringBuilder output = new StringBuilder("First Name: " + contactName);
                //Get all phone numbers
                if (contactNumberCount > 0) {
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
                            Phone_CONTACT_ID + "=?", new String[]{contactID}, null);
                    while (phoneCursor != null && phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        if (!phoneNumber.isEmpty())
                            output.append("\nPhone Number: ").append(phoneNumber);
                    }
                    assert phoneCursor != null;
                    phoneCursor.close();
                }

                //Get all email address associated with contact
                Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null,
                        EmailCONTACT_ID + "=?", new String[]{contactID}, null);
                while (emailCursor != null && emailCursor.moveToNext()) {
                    String emailAddress = emailCursor.getString(emailCursor.getColumnIndex(EMAIL));
                    if (!emailAddress.isEmpty())
                        output.append("\nEmail: ").append(emailAddress);
                }
                assert emailCursor != null;
                emailCursor.close();

                contactList.add(output.toString());
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.text1, contactList);
                    mListView.setAdapter(adapter);
                }
            });

            //Cancel dialog after 100ms
            updateDialog.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDialog.cancel();
                }
            }, 100);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CONTACT_PERM_REQ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init(v);
            } else {
                requestPermissions(new String[]{
                        Manifest.permission.READ_CONTACTS}, CONTACT_PERM_REQ);
            }
        }
    }
}
