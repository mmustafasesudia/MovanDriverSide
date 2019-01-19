package com.example.admin.movanuser.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.movanuser.Model.Pickupqueue;
import com.example.admin.movanuser.R;
import com.example.admin.movanuser.ViewlocationFragment;

import java.util.ArrayList;

public class PickUpListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<Pickupqueue> data;

    public PickUpListAdapter(Activity mainActivity, ArrayList<Pickupqueue> data) {
        // TODO Auto-generated constructor stub
        this.data = data;
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;


        FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
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

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        final Pickupqueue current = data.get(position);

        rowView = inflater.inflate(R.layout.custom_list_item_layout, null);
        holder.name = rowView.findViewById(R.id.pickupname);
        holder.phone_number = rowView.findViewById(R.id.pickupphonno);
        holder.iv_pickup = rowView.findViewById(R.id.iv_pickup);
        holder.iv_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lat = current.getPick_lat();
                String lng = current.getPick_long();
                String parentno = current.getParentno();
                String person_mobileNo = current.getPerson_mobileNo();
                String journeyid = current.getJourneyid();


                Bundle args = new Bundle();
                Fragment fragmentName = null;
                Fragment pickupndropoffqueue = new ViewlocationFragment();
                args.putString("latlat", lat);
                args.putString("lnglng", lng);
                args.putString("parentno", parentno);
                args.putString("person_mobileNo", person_mobileNo);
                args.putString("journeyid", journeyid);

                args.putString("activitystatus", "pick");
                fragmentName = pickupndropoffqueue;
                fragmentName.setArguments(args);
                replaceFragment(fragmentName);

            }
        });


        holder.name.setText("" + current.getPerson_name());
        holder.phone_number.setText("" + current.getPerson_mobileNo());

        return rowView;
    }

    public class Holder {
        TextView name, phone_number;
        ImageView iv_pickup;
    }

}