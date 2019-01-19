package com.example.admin.movanuser.Fragments;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.admin.movanuser.Adapter.DropOffListAdapter;
import com.example.admin.movanuser.Adapter.PickUpListAdapter;
import com.example.admin.movanuser.ConfigURL;
import com.example.admin.movanuser.Drawer;
import com.example.admin.movanuser.Model.Pickupqueue;
import com.example.admin.movanuser.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.admin.movanuser.ConfigURL.handler;
import static com.example.admin.movanuser.ConfigURL.runnable;


public class Pickupndropoffqueue extends Fragment {

    ListView androidListView, androidListView2;
    ArrayList<Pickupqueue> pickupndropoffqueueArrayList, pickupndropoffqueueArrayList2;
    String journeyid;
    Button btn_dinished;
    Context mContext = getActivity();
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //do other stuff here
            String message = intent.getStringExtra("message");
            String type = intent.getStringExtra("type");
            /* finished,pickedup,accepted*/
            if (type.equals("PICKEDUP") || type.equals("ACCEPTED") || type.equals("FINISHED") || type.equals("REQUEST")) {
                pickupndropoffqueueArrayList.clear();
                pickupndropoffqueueArrayList2.clear();
                getdata();

                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
            }

        }
    };


    public Pickupndropoffqueue() {
        // Required empty public constructor
    }

    // @SuppressLint("MissingPermission")
    // @SuppressLint("MissingPermission")

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pickupndropoffqueue, container, false);


        Bundle bundle = getArguments();
        if (bundle != null) {
            journeyid = bundle.getString("Journy_id");

            Log.d("Journy_idJourny_id", "jid: " + journeyid);
        }

        SharedPreferences preferences = getActivity().getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("RIDE", "YES");
        editor.commit();

        getdata();
        btn_dinished = rootView.findViewById(R.id.btn_finished);
        androidListView = rootView.findViewById(R.id.list1);
        androidListView2 = rootView.findViewById(R.id.list2);


        btn_dinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finished();
            }
        });

        //Update Driver Location To Server


        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return false;
                    }
                }
                return false;
            }
        });

        return rootView;
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

    public void getdata() {
        AndroidNetworking.get(ConfigURL.URL_LIST_OF_CUSTOMERS)
                .addQueryParameter("journy_id", journeyid)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("resres", "" + response);
                        try {

                            JSONArray jsonArray = response.getJSONArray("Customers");

                            pickupndropoffqueueArrayList = new ArrayList<>();
                            pickupndropoffqueueArrayList2 = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                String pname = jsonObject.getString("Person_name");
                                String pmobileno = jsonObject.getString("Person_mobileNo");
                                String Image = jsonObject.getString("Image");
                                String Customer_status = jsonObject.getString("Customer_status");
                                String Pick_lat = jsonObject.getString("Pick_lat");
                                String Pick_long = jsonObject.getString("Pick_long");
                                String Drop_lat = jsonObject.getString("Drop_lat");
                                String Drop_long = jsonObject.getString("Drop_long");
                                String Fare = jsonObject.getString("Fare");
                                String Parentno = jsonObject.getString("Parent_num");


                                Log.d("saddasd", "pname: " + pname + "\npmobile: " + pmobileno + "\nStatus" + Customer_status + "Parentno: " + Parentno);

                                if (Customer_status.equals("ACTIVE")) {
                                    Pickupqueue pickupqueue = new Pickupqueue(pname, pmobileno, Customer_status, Pick_lat, Pick_long, Drop_lat, Drop_long, Fare, Parentno, journeyid);
                                    pickupndropoffqueueArrayList.add(pickupqueue);
                                }
                                if (Customer_status.equals("PICKEDUP")) {
                                    Pickupqueue pickupqueue2 = new Pickupqueue(pname, pmobileno, Customer_status, Pick_lat, Pick_long, Drop_lat, Drop_long, Fare, Parentno, journeyid);
                                    pickupndropoffqueueArrayList2.add(pickupqueue2);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        PickUpListAdapter pickUpListAdapter = new PickUpListAdapter(getActivity(), pickupndropoffqueueArrayList);
                        androidListView.setAdapter(pickUpListAdapter);


                        DropOffListAdapter dropOffListAdapter = new DropOffListAdapter(getActivity(), pickupndropoffqueueArrayList2);
                        androidListView2.setAdapter(dropOffListAdapter);


                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void finished() {
        ProgressDialogClass.showRoundProgress(getActivity(), "Please Wait While..!");
        AndroidNetworking.post(ConfigURL.URL_COMPLETE_JOURNEY)
                .addBodyParameter("journy_id", journeyid)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.dismissRoundProgress();
                        try {
                            if (!response.getBoolean("error")) {
                                handler.removeCallbacks(runnable);
                                //handler.removeCallbacks(runnable);
                              /*  String msg = "Dropped Off..!";
                                String url = "https://wa.me/" + parentno + "/?text=" + msg + "";
                                Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);*/
                                ConfigURL.DROP_OF_LAT = "";
                                ConfigURL.PICK_UP_LAT = "";
                                ConfigURL.DROP_OF_LNG = "";
                                ConfigURL.PICK_UP_LNG = "";

                                SharedPreferences preferences = getActivity().getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("RIDE", "");
                                editor.commit();

                                //getActivity().onBackPressed();
                                Intent intent = new Intent(getActivity(), Drawer.class);
                                startActivity(intent);
                                getActivity().finish();
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


}
