package com.ninja.dexuscrisis;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ninja.dexuscrisis.Adapter.LeftNavAdapter;
import com.ninja.dexuscrisis.Fragment.DevelopFragment;
import com.ninja.dexuscrisis.Fragment.HowFragment;
import com.ninja.dexuscrisis.Fragment.IdentifyFragment;
import com.ninja.dexuscrisis.Fragment.RespondFragment;
import com.ninja.dexuscrisis.Fragment.WhenFragment;
import com.ninja.dexuscrisis.Fragment.WhoFragment;
import com.ninja.dexuscrisis.Utils.Constant;

public class MainActivity extends FragmentActivity {

    private DrawerLayout drawerLayout;

    /** ListView for left side drawer. */
    private ListView drawerLeft;

    /** The left navigation list adapter. */
    private LeftNavAdapter adapterLeft;

    private boolean isMenuOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if (!weHavePermissionToCallPhone()) {
            requestCallPhonePermissionFirst();
        }

        setupDrawer();

        launchFragment(0);
    }

    void setupDrawer()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
//                GravityCompat.START);

        drawerLayout.closeDrawers();
        isMenuOpened = false;

        setupLeftNavDrawer();

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isMenuOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isMenuOpened = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    void setupLeftNavDrawer()
    {
        drawerLeft = (ListView) findViewById(R.id.left_drawer);

//        drawerLeft.removeAllViews();

        View header = getLayoutInflater().inflate(R.layout.left_nav_header, null);

        ImageView imgvBG = (ImageView)header.findViewById(R.id.imgvBG);
        imgvBG.setImageResource(R.drawable.dexus_logo);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        drawerLeft.addHeaderView(header);

        adapterLeft = new LeftNavAdapter(this); // home, my chat, location, setting, invite friend
        drawerLeft.setAdapter(adapterLeft);
        drawerLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                drawerLayout.closeDrawers();

                launchFragment(pos - 1);
                adapterLeft.setSelection(pos - 1);
            }
        });
    }

    public void toggleMenu() {
        isMenuOpened = !isMenuOpened;

        if(isMenuOpened)
            drawerLayout.openDrawer(drawerLeft);
        else
            drawerLayout.closeDrawers();
    }


    public void launchFragment(int pos) {
        Fragment f = null;
        String title = null;

        switch (pos){
            case 0:
                f = new IdentifyFragment();
                title = Constant.FRAGMENT_IDENTIFY;
                break;

            case 1:
                f = new RespondFragment();
                title = Constant.FRAGMENT_RESPOND;
                break;

            case 2:
                f = new DevelopFragment();
                title = Constant.FRAGMENT_DEVELOP;
                break;

            case 3:
                f = new WhenFragment();
                title = Constant.FRAGMENT_WHEN;
                break;

            case 4:
                f = new WhoFragment();
                title = Constant.FRAGMENT_WHO;
                break;

            case 5:
                f = new HowFragment();
                title = Constant.FRAGMENT_HOW;
                break;
        }

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, f, title).addToBackStack(title).commit();
    }

    private boolean weHavePermissionToCallPhone() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCallPhonePermissionFirst() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            Toast.makeText(this, "We need permission so you can make a call.", Toast.LENGTH_LONG).show();
            requestForResultCallPermission();
        } else {
            requestForResultCallPermission();
        }
    }

    private void requestForResultCallPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 123);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission For Call Phone Granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

}
