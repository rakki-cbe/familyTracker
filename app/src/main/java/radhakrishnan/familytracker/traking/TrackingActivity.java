package radhakrishnan.familytracker.traking;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import radhakrishnan.familytracker.R;
import radhakrishnan.familytracker.UserInfo.LoginActivity;
import radhakrishnan.familytracker.base.BaseActivity;
import radhakrishnan.familytracker.listing.ListTrackUsers;
import radhakrishnan.familytracker.traking.model.Location;
import radhakrishnan.familytracker.utill.utils;

public class TrackingActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, TrackingView {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 5215;
    TrackingPresanter presanter = new TrackingPresanter(this);
    EditText ed_Code;
    MapFragment map;
    AlertDialog dialog;

    public static Intent getIntent(Context context) {
        return new Intent(context, TrackingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCodeAlert();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        map = MapFragment.getInstance();
        setUpPermission();

        /**
         * Its used to demonstrate leakanery lib for memory leaks
         */
      /*  radhakrishnan.familytracker.Application app= (radhakrishnan.familytracker.Application) getApplication();
        app.setSimple(this);*/
    }

    private void setUpPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog dialog = new AlertDialog.Builder(this).setCancelable(true)
                        .setMessage("Alow our app to track your location" +
                                " you can cancel anytime by clicking the notification")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(TrackingActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);

                            }
                        }).create();
                dialog.show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else {
            startLocationTrack();

        }
    }

    private void startLocationTrack() {

        startService(LocationListener.getStartForgroundServiceIntent(this));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, map).commit();
    }

    public void updateUsersLocation(Location location) {

        map.setOtherUser(location);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Add) {
            getCodeAlert();
        } else if (id == R.id.nav_list) {
            startActivity(ListTrackUsers.getIntent(this));
        } else if (id == R.id.nav_getMyTrackCode) {
            presanter.getMyCode();


        } else if (id == R.id.nav_logout) {
            utils.logOut(this);
            startActivity(LoginActivity.getLoginIntent(this));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                    startLocationTrack();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void showMyTrackCode(final String code) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_code, null);
        TextView tv = (TextView) dialogView.findViewById(R.id.ac_code);
        dialogView.findViewById(R.id.ac_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv.setText(code);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.copyText(code, getBaseContext());
                showToast(getStringFromInt(R.string.CodeCopied));

            }
        });
        dialogBuilder.setView(dialogView);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = dialogBuilder.create();
        dialog.show();

    }


    @Override
    public void errorCreatingCode(int error_Code) {

    }

    public void getCodeAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_enter_code, null);
        ed_Code = (EditText) dialogView.findViewById(R.id.ed_code);
        dialogView.findViewById(R.id.trakHim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presanter.startTrackMyFriend(ed_Code.getText().toString());
                dialog.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = dialogBuilder.create();
        dialog.show();

    }


}
