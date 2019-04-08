package com.theeralabs.app.contentprovider;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.theeralabs.app.contentprovider.view.BrowserHistoryFragment;
import com.theeralabs.app.contentprovider.view.ContactsFragment;
import com.theeralabs.app.contentprovider.view.GalleryFragment;
import com.theeralabs.app.contentprovider.view.MesasgesFragment;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.nav_contacts:
                    transaction.replace(R.id.main_frameLayout, new ContactsFragment()).commit();
                    return true;
                case R.id.nav_messages:
                    transaction.replace(R.id.main_frameLayout, new MesasgesFragment()).commit();
                    return true;
                case R.id.nav_gallery:
                    transaction.replace(R.id.main_frameLayout, new GalleryFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().performIdentifierAction(R.id.nav_messages, 0);
    }

}
