package com.example.admin.movanuser.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.movanuser.ConfigURL;
import com.example.admin.movanuser.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView tv_name, tv_phoneno, tv_email;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        tv_name = rootView.findViewById(R.id.tv__name);
        tv_phoneno = rootView.findViewById(R.id.tv__phone_no);
        tv_email = rootView.findViewById(R.id.tv_email_id);

        tv_name.setText(ConfigURL.getName(getActivity()));
        tv_email.setText(ConfigURL.getEmail(getActivity()));
        tv_phoneno.setText(ConfigURL.getMobileNo(getActivity()));

        return rootView;
    }

}
