package com.taraba.gulfoilapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.ProfileDetailAdapter;
import com.taraba.gulfoilapp.contentproviders.UserModel;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.model.KeyValue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.taraba.gulfoilapp.util.GulfOilUtils.getStr;

/**
 * Created by android3 on 2/16/16.
 */
public class MyProfileFragment extends Fragment {
    private static final String FROM = "from";
    private static final String MECH_TRNO = "mech_trno", FILLNAME = "fullname", SHOPNAME = "shopname", MOBILE = "mobile", DIST_ID = "dist_id", PARTICIPANT_CODE = "Participant_code";
    private ImageView ivProfile;
    private List<KeyValue> keyValues;
    private RecyclerView rvProfileDetails;
    private ProfileDetailAdapter adapter;
    private String from = "";
    private TextView tvProfileHeading;
    private String mech_trno = "";
    private String fullname, shopname, mobile, dist_id, Participant_code;


    public static MyProfileFragment newInstance(String from, String mech_trno, String fullname, String shopname, String mobileNo, String distributorId, String Participant_code) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FROM, from);
        bundle.putString(MECH_TRNO, mech_trno);
        bundle.putString(FILLNAME, fullname);
        bundle.putString(SHOPNAME, shopname);
        bundle.putString(MOBILE, mobileNo);
        bundle.putString(DIST_ID, distributorId);
        bundle.putString(PARTICIPANT_CODE, Participant_code);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.from = bundle.getString(FROM);
            this.mech_trno = bundle.getString(MECH_TRNO);
            this.fullname = bundle.getString(FILLNAME);
            this.shopname = bundle.getString(SHOPNAME);
            this.mobile = bundle.getString(MOBILE);
            this.dist_id = bundle.getString(DIST_ID);
            this.Participant_code = bundle.getString(PARTICIPANT_CODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_my_profile, container, false);
        keyValues = new ArrayList<>();
        initComp(view);
        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);

        if (mech_trno.equals("")) {
            mech_trno = preferences.getString("usertrno", "");
        }

        String mob = preferences.getString("mobile", "");
        String user_type = preferences.getString("user_type", "");

        //common for retailer and FLS
        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity().getApplicationContext());
        try {
            ctdUser.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (!from.equals("SearchMember")) {

            UserModel savedNews = ctdUser.getHomeworkByID(mech_trno);
            ctdUser.close();

            if (!TextUtils.isEmpty(savedNews.getUserfname()))
                keyValues.add(new KeyValue(getStr(R.string.d_fullname, getActivity()), savedNews.getUserfname().toUpperCase() + " " + savedNews.getUserlname().toUpperCase()));


            //Load photo is exist
            String photo = savedNews.getPicture();

            Log.e("Photo url", "photo url in my profile :--------------------" + photo);

            try {
                if (photo.equals("") || photo == null) {
                } else {
                    Picasso.with(getContext())
                            .load(photo)
                            .placeholder(getContext().getResources().getDrawable(R.drawable.loading)).error(getContext().getResources().getDrawable(R.drawable.about1))
                            .resize(100, 100) // optional
                            .into(ivProfile);
                }
            } catch (NullPointerException ne) {
                Log.e("", "Error:" + ne);
            }

            if (!user_type.equals("fls")) {
                keyValues.add(new KeyValue(getStr(R.string.d_shop_name, getActivity()), preferences.getString("shopname", "")));
                keyValues.add(new KeyValue("Shop Address1", preferences.getString("shopaddress1", "").replaceAll(",$", "")));
                keyValues.add(new KeyValue("Shop Street Address", preferences.getString("shopaddress2", "")));
                keyValues.add(new KeyValue(getStr(R.string.m_shop_landmark, getActivity()), preferences.getString("shoplandmark", "")));
                keyValues.add(new KeyValue(getStr(R.string.m_shop_pincode, getActivity()), preferences.getString("shoppincode", "")));
                keyValues.add(new KeyValue(getStr(R.string.m_shop_subdistrict, getActivity()), preferences.getString("shopsubdistrict", "")));
                keyValues.add(new KeyValue("Shop City Name", preferences.getString("shopcity", "")));
                keyValues.add(new KeyValue("Shop District", preferences.getString("shopdistrict", "")));
                keyValues.add(new KeyValue("Shop State", preferences.getString("shopstate", "")));
                keyValues.add(new KeyValue(getStr(R.string.d_mobile, getActivity()), preferences.getString("mobile", "")));
                keyValues.add(new KeyValue(getStr(R.string.d_altmobile, getActivity()), preferences.getString("alternatemob", "")));
                keyValues.add(new KeyValue("Shop Landline No.", preferences.getString("shoplandline", "")));
                keyValues.add(new KeyValue("Residential Address1", preferences.getString("resadd1", "")));
                keyValues.add(new KeyValue("Residential Address2", preferences.getString("resadd2", "")));
                keyValues.add(new KeyValue("Residential Landmark", preferences.getString("reslandmark", "")));
                keyValues.add(new KeyValue("Residential Pincode", preferences.getString("respincode", "")));
                keyValues.add(new KeyValue("Residential City", preferences.getString("rescity", "")));
                keyValues.add(new KeyValue("Residential State", preferences.getString("resstate", "")));
                keyValues.add(new KeyValue("Gender", preferences.getString("gender", "")));
                keyValues.add(new KeyValue("Date of Birth", preferences.getString("dob", "")));
                keyValues.add(new KeyValue("Marriage Anniversary", preferences.getString("anniversery", "")));
                keyValues.add(new KeyValue("Spouse Name", preferences.getString("spouse", "")));
                keyValues.add(new KeyValue("No. of Children", preferences.getString("children", "")));
                keyValues.add(new KeyValue("Distributor ID", preferences.getString("distributorid", "")));
                keyValues.add(new KeyValue("Distributor Name", preferences.getString("distributorname", "")));
                keyValues.add(new KeyValue(getStr(R.string.d_emp_id, getActivity()), preferences.getString("uname", "")));
                keyValues.add(new KeyValue(getStr(R.string.d_email, getActivity()), preferences.getString("email", "")));
            } else {

                keyValues.add(new KeyValue(getStr(R.string.d_mobile, getActivity()), preferences.getString("mobile", "")));
                keyValues.add(new KeyValue(getStr(R.string.d_emp_id, getActivity()), preferences.getString("uname", "")));
                keyValues.add(new KeyValue(getStr(R.string.d_email, getActivity()), preferences.getString("email", "")));
            }

        } else {
            tvProfileHeading.setText("Retailer profile");
            keyValues.add(new KeyValue(getStr(R.string.d_retailer_id, getActivity()), Participant_code));

            keyValues.add(new KeyValue(getStr(R.string.d_retailer_name, getActivity()), fullname));
            keyValues.add(new KeyValue(getStr(R.string.d_shop_name, getActivity()), shopname));
            keyValues.add(new KeyValue(getStr(R.string.d_mobile, getActivity()), mobile));
            keyValues.add(new KeyValue(getStr(R.string.d_distributor_id, getActivity()), dist_id));
        }


        adapter.notifyDataSetChanged();


        return view;
    }

    public void initComp(View view) {

        tvProfileHeading = (TextView) view.findViewById(R.id.myprofile);
        ivProfile = (ImageView) view.findViewById(R.id.img_profile);
        rvProfileDetails = (RecyclerView) view.findViewById(R.id.rvProfileDetails);
        rvProfileDetails.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new ProfileDetailAdapter(getActivity(), keyValues);
        rvProfileDetails.setAdapter(adapter);

        rvProfileDetails.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


    }
}