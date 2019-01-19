package com.example.admin.movanuser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.admin.movanuser.Fragments.DashboardFragment;
import com.example.admin.movanuser.Fragments.ProgressDialogClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;


public class ViewlocationFragment extends Fragment implements View.OnClickListener {

    private static final String FINE_LOCATION =
            Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION =
            Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    // TODO: Rename and change types of parameters
    String lat, lng, status, parentno, custumer_mobile_no, journey_id;
    Button btn_pick, btn_drop, btn_finished;
    //MapView mMapView;
    View rootView;
    GoogleMap mGoogleMap;
    Marker mCurrLocationMarker, mDestLocationMarker;
    private Boolean mLocationPermissionsGranted = false;

    public ViewlocationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_viewlocation, container, false);

        btn_drop = rootView.findViewById(R.id.btn_drop);
        btn_pick = rootView.findViewById(R.id.btn_pick);
        btn_finished = rootView.findViewById(R.id.btn_finishedd);

        btn_drop.setOnClickListener(this);
        btn_pick.setOnClickListener(this);
        btn_finished.setOnClickListener(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            lat = bundle.getString("latlat");
            lng = bundle.getString("lnglng");
            parentno = bundle.getString("parentno");
            custumer_mobile_no = bundle.getString("person_mobileNo");
            journey_id = bundle.getString("journeyid");
            status = bundle.getString("activitystatus");
            Log.d("latlnglatlng", "" + lat + "\nlng: " + lng + "\nstatus: " + status + "\nparentno: " + parentno);
        }

        if (status.equals("pick")) {
            btn_pick.setVisibility(View.VISIBLE);
        }

        if (status.equals("drop")) {
            btn_drop.setVisibility(View.VISIBLE);
            //btn_finished.setVisibility(View.VISIBLE);
        }


        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_pick:
                pickedUp();
                break;
            case R.id.btn_drop:
                dropUp();
                break;
            case R.id.btn_finishedd:
                finished();
                break;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map3);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;


                mSetUpMap();

            }
        });
    }


    public void setOriginMarkerOfCustomer(Double mLat, Double mLng) {
        /*mLat = 24.902577553876107;
        mLng = 67.05668918788434;*/
        Log.v("Hello    " + mLat, "" + mLng);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.add_marker);

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        LatLng latLng = new LatLng(mLat, mLng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Origin");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        mCurrLocationMarker.showInfoWindow();

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

    }

    public void setDestMarkerOfCustomer(Double mLat, Double mLng) {
        /*mLat = 24.902577553876107;
        mLng = 67.05668918788434;*/
        Log.v("Hello    " + mLat, "" + mLng);
        // BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.add_marker);

        if (mDestLocationMarker != null) {
            mDestLocationMarker.remove();
        }
        LatLng latLng = new LatLng(mLat, mLng);
        /*mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Destination")).showInfoWindow();*/
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Destination");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mDestLocationMarker = mGoogleMap.addMarker(markerOptions);
        mDestLocationMarker.showInfoWindow();

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

    }


    private void mSetUpMap() {
        // your method code
        MapsInitializer.initialize(getContext());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);


        if (!lat.equals("") || !lng.equals("")) {
            //Toast.makeText(getActivity(), "" + ConfigURL.PICK_UP_LAT, Toast.LENGTH_SHORT).show();
            setOriginMarkerOfCustomer(Double.parseDouble(lat), Double.parseDouble(lng));
        }
        if (!lat.equals("") || !lng.equals("")) {
            //Toast.makeText(getActivity(), "" + ConfigURL.PICK_UP_LAT, Toast.LENGTH_SHORT).show();
            setDestMarkerOfCustomer(Double.parseDouble(lat), Double.parseDouble(lng));

        }
    }

    private void getLocationPermission() {
        Log.d("", "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        Log.d("", "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {

            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                    // initMap();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }

    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map3);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;


                mSetUpMap();
            }
        });
    }


    public void pickedUp() {
        ProgressDialogClass.showRoundProgress(getActivity(), "Please Wait While..!");
        AndroidNetworking.post(ConfigURL.URL_PICKUP)
                .addBodyParameter("journy_id", journey_id)
                .addBodyParameter("customer_num", custumer_mobile_no)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.dismissRoundProgress();
                        try {
                            if (!response.getBoolean("error")) {


                               /* Fragment fragmentName = null;
                                Fragment Pickupndropoffqueu = new Pickupndropoffqueue();
                                fragmentName = Pickupndropoffqueu;
                                replaceFragment(fragmentName);*/
/*
                                Intent intent1 = new Intent(getActivity(),Drawer.class);
                                startActivity(intent1);*/

                                getActivity().onBackPressed();

                                String msg = "Picked Up..!";
                                String url = "https://wa.me/" + parentno + "/?text=" + msg + "";
                                Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getActivity(), "Request Failed", Toast.LENGTH_SHORT).show();
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


    public void dropUp() {
        ProgressDialogClass.showRoundProgress(getActivity(), "Please Wait While..!");
        AndroidNetworking.post(ConfigURL.URL_DROP_OFF)
                .addBodyParameter("journy_id", journey_id)
                .addBodyParameter("customer_num", custumer_mobile_no)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.dismissRoundProgress();
                        try {
                            if (!response.getBoolean("error")) {

                               /* Bundle bundle = new Bundle();
                                bundle.putString("msg", "Dropped Off...");
                                bundle.putString("parentNumber", parentno);
                                Fragment fragmentName = null;
                                Fragment Pickupndropoffqueue = new Pickupndropoffqueue();
                                fragmentName = Pickupndropoffqueue;
                                fragmentName.setArguments(bundle);
                                replaceFragment(fragmentName);*/

                                getActivity().onBackPressed();


                                String msg = "Dropped Off..!";
                                String url = "https://wa.me/" + parentno + "/?text=" + msg + "";
                                Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivityForResult(intent, 0);


                            } else {
                                Toast.makeText(getActivity(), "Request Failed", Toast.LENGTH_SHORT).show();
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


    public void finished() {
        ProgressDialogClass.showRoundProgress(getActivity(), "Please Wait While..!");
        AndroidNetworking.post(ConfigURL.URL_COMPLETE_JOURNEY)
                .addBodyParameter("journy_id", journey_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.dismissRoundProgress();
                        try {
                            if (!response.getBoolean("error")) {

                              /*  String msg = "Dropped Off..!";
                                String url = "https://wa.me/" + parentno + "/?text=" + msg + "";
                                Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);*/

                                Fragment fragmentName = null;
                                Fragment DashboardFragment = new DashboardFragment();
                                fragmentName = DashboardFragment;
                                replaceFragment(fragmentName);
                            } else {
                                Toast.makeText(getActivity(), "Request Failed", Toast.LENGTH_SHORT).show();
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


    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getActivity().getSupportFragmentManager();
        boolean fragmentPopped = false;
        try {

            fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        } catch (IllegalStateException ignored) {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }
        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commitAllowingStateLoss();
        }
    }

}
