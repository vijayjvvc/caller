package com.JvWorld.jcaller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.JvWorld.jcaller.fragment.ContactFragment;
import com.JvWorld.jcaller.fragment.DialFragment;
import com.JvWorld.jcaller.fragment.FavFragment;
import com.JvWorld.jcaller.fragment.RecentFragment;
import com.JvWorld.jcaller.fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    // Request code. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_CODE = 999;

    String[] appPermissions = {
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.WRITE_CONTACTS
    };
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        CheckAndRequestPermission();
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new RecentFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.nav_dial);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_fav:
                        fragment = new FavFragment();
                        break;
                    case R.id.nav_recents:
                        fragment = new RecentFragment();
                        break;
                    case R.id.nav_contacts:
                        fragment = new ContactFragment();
                        break;
                    case R.id.nav_dial:
                        fragment = new DialFragment();
                        break;
                    case R.id.nav_settings:
                        fragment = new SettingFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container,fragment).commit();

                return false;
            }
        });
    }

    public boolean CheckAndRequestPermission() {
        //checking which permissions are granted
        List<String> listPermissionNeeded = new ArrayList<>();
        for (String item: appPermissions){
            if(ContextCompat.checkSelfPermission(this, item)!= PackageManager.PERMISSION_GRANTED)
                listPermissionNeeded.add(item);
        }

        //Ask for non-granted permissions
        if (!listPermissionNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),
                    PERMISSIONS_REQUEST_CODE);
            return false;
        }
        //App has all permissions. Proceed ahead
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if(grantResults[i]==PackageManager.PERMISSION_DENIED){
                    flag = 1;
                    break;
                }
            }
            if (flag==0){
                CheckAndRequestPermission();
            }

        }
    }
}