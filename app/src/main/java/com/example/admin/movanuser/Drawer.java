package com.example.admin.movanuser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.admin.movanuser.Fragments.DashboardFragment;
import com.example.admin.movanuser.Fragments.Pickupndropoffqueue;
import com.example.admin.movanuser.Fragments.ProfileFragment;
import com.example.admin.movanuser.Fragments.ProgressDialogClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.admin.movanuser.ConfigURL.handler;
import static com.example.admin.movanuser.ConfigURL.runnable;

public class Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static int delay = 5000; //milliseconds
    TextView tv_nav_head_name, tv_nav_head_no;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Drawer.this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tv_nav_head_name = navigationView.getHeaderView(0).findViewById(R.id.nav_name);

        tv_nav_head_no = navigationView.getHeaderView(0).findViewById(R.id.nav_phoneno);

        tv_nav_head_name.setText(ConfigURL.getName(Drawer.this));

        tv_nav_head_no.setText(ConfigURL.getMobileNo(Drawer.this));

        if (!(ConfigURL.isRideIsActive(Drawer.this).length() > 0)) {
            handler.removeCallbacks(runnable);
        }
        getRideIfHave();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            /*if (getSupportFragmentManager().findFragmentByTag("PickDropQueue").equals("PickDropQueue")) {
                Toast.makeText(Drawer.this, "", Toast.LENGTH_LONG).show();
            }*/
            if (ConfigURL.isRideIsActive(Drawer.this).length() > 0) {
                handler.removeCallbacks(runnable);
            }
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        /*if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            getRideIfHave();

        } else if (id == R.id.nav_gallery) {

            Fragment fragmentName = null;
            Fragment profileFragment = new ProfileFragment();
            fragmentName = profileFragment;
            replaceFragment(fragmentName);

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //stopService(new Intent(DrawerMainActivity.this, MyService.class));
            startActivity(intent);
            ConfigURL.clearshareprefrence(this);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;


        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = false;
        try {

            fragmentPopped = manager.popBackStackImmediate(fragmentTag, 0);

        } catch (IllegalStateException ignored) {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
        }

    }

    public void getRideIfHave() {
        ProgressDialogClass.showRoundProgress(Drawer.this, "Please Wait While..!");
        AndroidNetworking.get(ConfigURL.URL_DRIVER_CURRENT_JOURNEY)
                .addQueryParameter("driver_num", ConfigURL.getMobileNo(Drawer.this))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.dismissRoundProgress();
                        try {
                            JSONArray jsonArray = response.getJSONArray("Rides");
                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String journey_id = jsonObject.getString("Journy_id");

                                Fragment fragmentName = null;
                                Fragment Pickupndropoffqueue = new Pickupndropoffqueue();
                                Bundle bundle = new Bundle();
                                bundle.putString("Journy_id", journey_id);
                                fragmentName = Pickupndropoffqueue;
                                fragmentName.setArguments(bundle);
                                replaceFragment(fragmentName);
                                callURL();

                            } else {
                                Fragment fragmentName = null;
                                Fragment SignInFragment = new DashboardFragment();
                                fragmentName = SignInFragment;
                                replaceFragment(fragmentName);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProgressDialogClass.dismissRoundProgress();
                    }
                });
    }

    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(Drawer.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Drawer.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(Drawer.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            updateDriverLocation(location.getLatitude(), location.getLongitude());
                        }
                    }
                })
                .addOnFailureListener(Drawer.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Drawer.this, "Failure", Toast.LENGTH_LONG).show();

                    }
                });
    }

    public void callURL() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                getLocation();
                handler.postDelayed(runnable, delay);
            }
        }, delay);

    }

    public void updateDriverLocation(final Double lat, final Double lng) {
        AndroidNetworking.post(ConfigURL.URL_UPDATE_CURRENT_LOCATION)
                .addBodyParameter("driver_mobile_num", ConfigURL.getMobileNo(Drawer.this))
                .addBodyParameter("van_num", ConfigURL.getVanNo(Drawer.this))
                .addBodyParameter("long", lat.toString())
                .addBodyParameter("lat", lng.toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConfigURL.isRideIsActive(Drawer.this).length() > 0) {
            callURL();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ConfigURL.isRideIsActive(Drawer.this).length() > 0) {
            handler.removeCallbacks(runnable);
        }
    }
}
