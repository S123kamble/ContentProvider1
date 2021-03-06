package com.theeralabs.app.contentprovider.view;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theeralabs.app.contentprovider.R;
import com.theeralabs.app.contentprovider.adapter.GalleryAdapter;
import com.theeralabs.app.contentprovider.model.GalleryImage;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {


    private static final int EXTERNAL_PERM_REQ = 3;

    public GalleryFragment() {
        // Required empty public constructor
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mesasges, container, false);
        init(v);
        // Inflate the layout for this fragment
        return v;
    }

    private ProgressDialog mDialog;
    private RecyclerView mRecyclerview;
    private Handler updateDialog;

    private void init(View v) {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Reading Images...");
        mDialog.setCancelable(false);
        updateDialog = new Handler();
        mRecyclerview = v.findViewById(R.id.list);
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_PERM_REQ);
        } else {
            mDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getMessages();
                }
            }).start();
        }
    }

    private int count;
    private Cursor cursor;
    private ArrayList<GalleryImage> contactList;

    private void getMessages() {
        contactList = new ArrayList<>();

        //Top level
        Uri CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
        String BODY = MediaStore.Images.Media.DATA;


        ContentResolver contentResolver = getActivity().getContentResolver();
        cursor = contentResolver.query(CONTENT_URI, null, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        assert cursor != null;
        final int cursorCount = cursor.getCount();
        if (cursor != null && cursorCount > 0) {
            count = 0;
            while (cursor.moveToNext()) {
                updateDialog.post(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.setMessage("Reading Messages : " + (count++) + "/" + cursorCount);
                    }
                });

                String name = cursor.getString(cursor.getColumnIndex(NAME));
                String body = cursor.getString(cursor.getColumnIndex(BODY));

                GalleryImage message = new GalleryImage(name, body);
                contactList.add(message);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GalleryAdapter adapter = new GalleryAdapter(contactList, getActivity());
                    mRecyclerview.setAdapter(adapter);
                }
            });

            //Cancel dialog after 100ms
            updateDialog.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDialog.cancel();
                }
            }, 500);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EXTERNAL_PERM_REQ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init(v);
            } else {
                requestPermissions(new String[]{
                        Manifest.permission.READ_CONTACTS}, EXTERNAL_PERM_REQ);
            }
        }
    }

}
