package com.taraba.gulfoilapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.adapter.SpinnerDataAdapter;
import com.taraba.gulfoilapp.contentproviders.Database;
import com.taraba.gulfoilapp.contentproviders.StateTableDatasource;
import com.taraba.gulfoilapp.contentproviders.UserModel;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.crop.Crop;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by android3 on 12/21/15.
 * Modified by Mansi
 */
public class EditRegistration extends Fragment {
    String dist = null;
    LinearLayout linMobNoReglayout, lin_mech_code;
    TextView txt_mechanic_code, txt_first_name, txt_middle_name, txthead, txtdist;
    ScrollView scrMainlaout;
    ImageView RegphotoIV, RegphotoIV1;
    Spinner mSpinnerState, mSpinnerDistrict;
    EditText mEditTextFirstName, mEditTextLastName, mEditTextNomineName, mEditTextNomineRelation, mEditTextMotherName, mEditTextWorkShopName,
            mEditTextPincode, mEditTextTaluka, mEditTextVillage, mEditTextshopAdress, mEditTextLandmark, mEditTextOwnerMobileNumber, mEditTextEmail, mEditTextDateOfBirth,
            mEditTextregFillDate, mEditTextregtoatalSperConsumptioperMonth, mEditTextsperpartconsuptionPErMonth, mEditTextmmgenuinesparespartsconsuptionPerMoth,
            mEditTexttotalVehicalsperMonth, mEditTextmahindraVehicalsperMonth, mEditTextnoofMechanics, mEditTextOwnerMobileNumberFirst;
    Button btnSubmit, btnSubmitMobi, btnUpdateProfile, btn_update_photo;
    private RadioGroup radioGroupGender, radioGroupSector;//,radioGroupSpecialization;
    private RadioButton rBtnMale, rBtnFemale, rBtnautomotive, rBtntractor;
    CheckBox rBtnengine, rBtnhydraulics, rBtntransmission, rBtnDentingPanting, rBtnmultiple, rBtnreborer;

    String strRegPhoto = "", strGender, strSector, strSpecialization,
            strState, strDistrict,
            strFirstName, strLastName, strNomineName, strNomineRelation, strMotherName, strWorkShopName, strPincode, strTaluka, strVillage, strshopAdress, strLandmark, strOwnerMobileNumber, strEmail, strDateOfBirth,
            strregFillDate, strregtoatalSperConsumptioperMonth, strsperpartconsuptionPErMonth, strmmgenuinesparespartsconsuptionPerMoth, strtotalVehicalsperMonth, strmahindraVehicalsperMonth, strnoofMechanics,
            strOwnerMobileNumberFirst,
            strUserTrno;

    String finalPathOfImageUrl;
    static Boolean isImageUpdate = false;

    ArrayList<String> states;
    ArrayList<String> district;

    ArrayList<ArrayList<String>> statesfinal, districtfinal;

    private Calendar cal;
    private int day;
    private int month;
    private int year;

    TextView txtmechanicregistration;
    String mStringStatus;

    Boolean isMgmtVerified = false, editable = false, updateProfile = false, isImageAvailable = false;
    String strParticipnt_login_id;

    Intent resultIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View view = inflater.inflate(R.layout.mechanic_registration, container, false);

        View view = inflater.inflate(R.layout.mechanic_registration, container, false);
        initComp(view);

        txt_first_name.setText(Html.fromHtml(getString(R.string.m_first_name)));
        txt_middle_name.setText(Html.fromHtml(getString(R.string.m_last_name)));
        SharedPreferences preferences = AppConfig.getInstance().getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);

        mStringStatus = preferences.getString("Mechanic_status", "");

        int mech_trno = preferences.getInt("Mechanic_trno_to_Profile", 0);

        Log.e("", "Mechanic trno to profile : " + mech_trno);

        isMgmtVerified = preferences.getBoolean("isMgmtVerified", false);
        strParticipnt_login_id = preferences.getString("participant_id_pk", "");

        isImageAvailable = preferences.getBoolean("isImageAvailable", false);
        Log.e("", "isMgmtVerified status-----" + isMgmtVerified);

        if (isMgmtVerified)
            editable = true;
        else
            editable = false;


        if (mStringStatus.equals("profile")) {
            linMobNoReglayout.setVisibility(View.GONE);
            scrMainlaout.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
            if (!editable && isImageAvailable) {
                btn_update_photo.setVisibility(View.GONE);
                btnUpdateProfile.setVisibility(View.GONE);
                txthead.setVisibility(View.GONE);
            } else {
                btn_update_photo.setVisibility(View.VISIBLE);
                btnUpdateProfile.setVisibility(View.VISIBLE);
                txthead.setVisibility(View.VISIBLE);
            }

            txtmechanicregistration.setText("Profile");

            RegphotoIV.setVisibility(View.GONE);

            RegphotoIV1.setVisibility(View.VISIBLE);
            RegphotoIV1.setImageResource(R.drawable.my_profile_);
            final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
            try {
                ctdUser.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            UserModel savedNews = ctdUser.getHomeworkByID(String.valueOf(mech_trno));
            ctdUser.close();

            String photo = savedNews.getPicture();

            Log.e("", "photo url:--------------------" + photo);

            if (photo.equals("") || photo == null) {
            } else {
                Picasso.with(getActivity())
                        .load(photo)
                        .skipMemoryCache()
                        .placeholder(getContext().getResources().getDrawable(R.drawable.loading)).error(getContext().getResources().getDrawable(R.drawable.about1))
                        .into(RegphotoIV1);
            }

            lin_mech_code.setVisibility(View.VISIBLE);
            txt_mechanic_code.setText("Membership ID : " + savedNews.getParticipant_code());
            //------------------------------------------
            if (savedNews.getUserfname() == null) {

            } else {
                mEditTextFirstName.setText(savedNews.getUserfname().toUpperCase());
            }
            if (savedNews.getUserlname() == null) {

            } else {
                mEditTextLastName.setText(savedNews.getUserlname().toUpperCase());
            }
            if (savedNews.getNomini() == null) {

            } else {
                mEditTextNomineName.setText(savedNews.getNomini().toUpperCase());
            }
            if (savedNews.getNominirely() == null) {

            } else {
                mEditTextNomineRelation.setText(savedNews.getNominirely().toUpperCase());
            }
            if (savedNews.getMothername() == null) {

            } else {
                mEditTextMotherName.setText(savedNews.getMothername().toUpperCase());
            }
            if (savedNews.getWorkshopname() == null) {

            } else {
                mEditTextWorkShopName.setText(savedNews.getWorkshopname().toUpperCase());
            }
            if (savedNews.getPincode() == null) {

            } else {
                mEditTextPincode.setText(savedNews.getPincode().toUpperCase());
            }
            if (savedNews.getTaluka() == null) {

            } else {
                mEditTextTaluka.setText(savedNews.getTaluka().toUpperCase());
            }
            if (savedNews.getCity() == null) {

            } else {
                mEditTextVillage.setText(savedNews.getCity().toUpperCase());
            }
            if (savedNews.getShopadd() == null) {

            } else {
                mEditTextshopAdress.setText(savedNews.getShopadd().toUpperCase());
            }
            if (savedNews.getLandmark() == null) {

            } else {
                mEditTextLandmark.setText(savedNews.getLandmark().toUpperCase());
            }
            if (savedNews.getMobile_no() == null) {

            } else {
                mEditTextOwnerMobileNumber.setText(savedNews.getMobile_no().toUpperCase());
            }

            // mEditTextOwnerMobileNumber.setKeyListener(null);
            if (savedNews.getEmail() == null) {

            } else {
                mEditTextEmail.setText(savedNews.getEmail());
            }
            // <---  yyyy-mm-dd
            if (savedNews.getDob() == null) {

            } else {
                String finaldob = SplitDateFormat(savedNews.getDob());
                mEditTextDateOfBirth.setText(convertDateFormat(finaldob));
            }

            if (savedNews.getRegdate() == null) {

            } else {
                String finalRegdate = SplitDateFormat(savedNews.getRegdate());

                mEditTextregFillDate.setText(convertDateFormat(finalRegdate));
            }

            if (savedNews.getToatalsperconpermonth() == null) {

            } else {
                mEditTextregtoatalSperConsumptioperMonth.setText(savedNews.getToatalsperconpermonth().toUpperCase());
            }
            if (savedNews.getSperpartconformmvehicpermonth() == null) {

            } else {
                mEditTextsperpartconsuptionPErMonth.setText(savedNews.getSperpartconformmvehicpermonth().toUpperCase());
            }
            if (savedNews.getMmgenuspareconpermonth() == null) {

            } else {
                mEditTextmmgenuinesparespartsconsuptionPerMoth.setText(savedNews.getMmgenuspareconpermonth().toUpperCase());
            }
            if (savedNews.getTotalvehicalpermonth() == null) {

            } else {
                mEditTexttotalVehicalsperMonth.setText(savedNews.getTotalvehicalpermonth().toUpperCase());
            }
            if (savedNews.getMahindravehicalpermonth() == null) {

            } else {
                mEditTextmahindraVehicalsperMonth.setText(savedNews.getMahindravehicalpermonth().toUpperCase());
            }
            if (savedNews.getNoofmechanics() == null) {

            } else {
                mEditTextnoofMechanics.setText(savedNews.getNoofmechanics().toUpperCase());
            }

            //RecyclerView.LayoutManager a=new LinearLayoutManager(LinearLayout.HORIZONTAL,WR//)
            //-------------------Readonly-----------------------------------
            dist = savedNews.getDistrict();

            Log.e("DIST_GET :", "DIST_GET : " + dist + " : ");
            mEditTextFirstName.setEnabled(editable);
            mEditTextLastName.setEnabled(editable);
            mEditTextNomineName.setEnabled(editable);
            mEditTextNomineRelation.setEnabled(editable);
            mEditTextMotherName.setEnabled(editable);
            mEditTextWorkShopName.setEnabled(editable);
            mEditTextPincode.setEnabled(editable);
            mEditTextTaluka.setEnabled(editable);
            mEditTextVillage.setEnabled(editable);
            mEditTextshopAdress.setEnabled(editable);
            mEditTextLandmark.setEnabled(editable);
            // mEditTextOwnerMobileNumber.setEnabled(editable);
            mEditTextEmail.setEnabled(editable);
            mEditTextDateOfBirth.setEnabled(editable);
            mEditTextregFillDate.setEnabled(editable);
            mEditTextregtoatalSperConsumptioperMonth.setEnabled(editable);
            mEditTextsperpartconsuptionPErMonth.setEnabled(editable);
            mEditTextmmgenuinesparespartsconsuptionPerMoth.setEnabled(editable);
            mEditTexttotalVehicalsperMonth.setEnabled(editable);
            mEditTextmahindraVehicalsperMonth.setEnabled(editable);
            mEditTextnoofMechanics.setEnabled(editable);
            btnUpdateProfile.setEnabled(editable);
            mSpinnerState.setEnabled(false);
            mSpinnerDistrict.setEnabled(false);
            radioGroupSector.setEnabled(false);
            for (int i = 0; i < radioGroupSector.getChildCount(); i++) {
                radioGroupSector.getChildAt(i).setEnabled(false);
            }
            if (editable) {
                txthead.setVisibility(View.VISIBLE);
                txthead.setText("You can edit your profile details below");
                btnUpdateProfile.setVisibility(View.VISIBLE);
                btnUpdateProfile.setEnabled(true);
                btn_update_photo.setVisibility(View.GONE);
                mEditTextOwnerMobileNumber.setEnabled(true);
                RegphotoIV1.setEnabled(true);
                //  Toast.makeText(getActivity(),"editable------",Toast.LENGTH_SHORT).show();
            } else {
                mEditTextOwnerMobileNumber.setEnabled(false);
                // Toast.makeText(getActivity(),"editable false------",Toast.LENGTH_SHORT).show();
                if (isImageAvailable) // editable=false and isImageAvailable=true
                {
                    txthead.setVisibility(View.GONE);
                    btnUpdateProfile.setVisibility(View.GONE);
                    btn_update_photo.setVisibility(View.GONE);
                    RegphotoIV1.setEnabled(false);
                } else// editable=false and isImageAvailable=false
                {
                    txthead.setVisibility(View.VISIBLE);
                    txthead.setText("Please upload your Profile Photo");
                    btn_update_photo.setVisibility(View.VISIBLE);
                    btnUpdateProfile.setVisibility(View.GONE);
                    btnUpdateProfile.setEnabled(true);
                    RegphotoIV1.setEnabled(true);
                }
            }

            //--------------end Readonly----------------------

            SplitStringSpecialization(savedNews.getSpecialization());

            if (savedNews.getGender().equalsIgnoreCase("Male")) {
                rBtnMale.setChecked(true);
            } else if (savedNews.getGender().equalsIgnoreCase("Female")) {
                rBtnFemale.setChecked(true);
            }

            if (savedNews.getSector().equalsIgnoreCase("as")) {
                rBtnautomotive.setChecked(true);
            } else if (savedNews.getSector().equalsIgnoreCase("fes")) {
                rBtntractor.setChecked(true);
            }
        } else if (mStringStatus.equals("main")) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            mEditTextregFillDate.setText(sdf.format(new Date()));
            mEditTextregFillDate.setEnabled(false);
            btnUpdateProfile.setVisibility(View.GONE);
            btn_update_photo.setVisibility(View.GONE);
            txthead.setVisibility(View.GONE);
            linMobNoReglayout.setVisibility(View.VISIBLE);
            scrMainlaout.setVisibility(View.GONE);
            settingStarsForLabels(view);
        } else {

            linMobNoReglayout.setVisibility(View.GONE);
            scrMainlaout.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
            btnUpdateProfile.setVisibility(View.GONE);
            btn_update_photo.setVisibility(View.GONE);
            txthead.setVisibility(View.GONE);

        }

        final StateTableDatasource ctd = new StateTableDatasource(getActivity());
        ctd.open();

        SharedPreferences preferences1 = AppConfig.getInstance().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        String mStringType = preferences1.getString("user_type", "");
        Log.e("type in main:", "User Type : " + mStringType);
        states = new ArrayList<String>();

        //states = mDatabaseHelper.getDataFromTableForSpinner("tbl_distributor_data", new String[]{"distinct(State)"}, "LOWER(Zone)='"+mSpinnerZone.getSelectedItem().toString().toLowerCase()+"'", "State asc");
        states = ctd.getDataFromTableForSpinnerNew(Database.USER_TABLE, new String[]{"distinct(" + Database.USER_TABLE_STATE_MAPPED + ")"}, "type='" + mStringType + "'", "state asc");

        Log.e("", "Mechanic states---------------------------" + states.get(0).toString());

        statesfinal = convertState(states.get(0).toString());

        //  Log.e("Satate Data :", "statefinal string : " + districtfinal + " : ");
        //states.add(0, "--Select State--");
        Log.e("Satate Data :", "State : " + states);
        Log.e("Satate Data :", "State string : " + statesfinal);
        // ArrayAdapter<String> stateTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.email_layout, R.id.text_name, statesfinal);

        mSpinnerState.setAdapter(new SpinnerDataAdapter(getActivity(),
                statesfinal, "spsearchBy"));

        mSpinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
               /* if (mSpinnerState.getSelectedItem().toString().startsWith("--")) {

                } else {
                    ctd.open();
                    district = ctd.getDataFromTableForSpinner(Database.USER_TABLE, new String[]{"distinct(district)"}, " LOWER(state)='" + mSpinnerState.getSelectedItem().toString().toLowerCase() + "'", "district asc");

                }
                //}

                ArrayAdapter<String> districtTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.email_layout, R.id.text_name, district);
                mSpinnerDistrict.setAdapter(districtTypeAdapter);*/

                //  districtfinal.add("Select District");


                districtfinal = convertDistrict(states.get(0).toString());
/*
                ArrayList<String> Cat = new ArrayList<String>();
                Cat.add("Select District");
                ArrayList<ArrayList<String>> ModelModel = new ArrayList<>();
                ModelModel.add(0,Cat);
                districtfinal.add(0,ModelModel);*/

                Log.e("Satate Data :", "districtfinal string : " + districtfinal + " : ");
                // ArrayAdapter<String> stateTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.email_layout, R.id.text_name, statesfinal);
                // mSpinnerDistrict.setSelection(-1);

                mSpinnerDistrict.setAdapter(new SpinnerDataAdapter(getActivity(),
                        districtfinal, "spsearchBy"));
                Log.e("DIST_SET :", "DIST_SET : " + dist + " : ");

                if (dist != null) {
                    txtdist.setText("" + dist.toUpperCase());
                    txtdist.setVisibility(View.VISIBLE);
                    mSpinnerDistrict.setVisibility(View.GONE);
                } else {
                    mSpinnerDistrict.setVisibility(View.VISIBLE);
                }

                //  txtdist.setText(""+dist.toUpperCase());
                // txtdist.setVisibility(View.VISIBLE);

                /*Log.e(" Data :", "State spsearchBy: " + statesfinal.get(mSpinnerState.getSelectedItemPosition()).get(0));
                Log.e(" Data :", "District Spinner:" + districtfinal.get(mSpinnerDistrict.getSelectedItemPosition()).get(0));

                strState = statesfinal.get(mSpinnerState.getSelectedItemPosition()).get(0);
                strDistrict = districtfinal.get(mSpinnerDistrict.getSelectedItemPosition()).get(0);*/

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        // mSpinnerDistrict.setPrompt("Select district");

        mSpinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                ArrayList<String> district = new ArrayList<String>();

                String strselectedstate = districtfinal.get(mSpinnerDistrict.getSelectedItemPosition()).get(0);

                Log.e(" Data :", "State spsearchBy: " + statesfinal.get(mSpinnerState.getSelectedItemPosition()).get(0));
                Log.e(" Data :", "District Spinner:" + districtfinal.get(mSpinnerDistrict.getSelectedItemPosition()).get(0));

                strState = statesfinal.get(mSpinnerState.getSelectedItemPosition()).get(0).toLowerCase();
                strDistrict = districtfinal.get(mSpinnerDistrict.getSelectedItemPosition()).get(0).toLowerCase();

                Log.e("district value", "district value" + strDistrict);
                /*ArrayAdapter<String> districtTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.email_layout, R.id.text_name, district);
                mSpinnerDistrict.setAdapter(districtTypeAdapter);*/
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        ctd.close();


        RegphotoIV.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // hideKeyboard();
                //Toast.makeText(getActivity(),"Image Click",Toast.LENGTH_LONG).show();
                if (mStringStatus.equals("profile")) {

                } else {
                    selectProfileImage();
                }
            }
        });

        RegphotoIV1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                selectProfileImage();
            }
        });

        mEditTextDateOfBirth.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @SuppressWarnings("deprecation")
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP)
                    showDatePickerDOB();
                return true;

            }
        });
        mEditTextregFillDate.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @SuppressWarnings("deprecation")
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP)
                    showDatePickerRegDate();
                return true;

            }
        });

        mEditTextFirstName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextFirstName);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mEditTextLastName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextLastName);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextNomineName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextNomineName);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextNomineRelation.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextNomineRelation);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextMotherName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextMotherName);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextWorkShopName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextWorkShopName);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextPincode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextPincode);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextTaluka.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextTaluka);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextVillage.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextVillage);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextshopAdress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextshopAdress);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextLandmark.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextLandmark);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextOwnerMobileNumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextOwnerMobileNumber);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextEmail);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextDateOfBirth.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextDateOfBirth);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextregFillDate.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextregFillDate);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextregtoatalSperConsumptioperMonth.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextregtoatalSperConsumptioperMonth);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextsperpartconsuptionPErMonth.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextsperpartconsuptionPErMonth);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextmmgenuinesparespartsconsuptionPerMoth.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextmmgenuinesparespartsconsuptionPerMoth);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mEditTexttotalVehicalsperMonth.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTexttotalVehicalsperMonth);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextmahindraVehicalsperMonth.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextmahindraVehicalsperMonth);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        mEditTextnoofMechanics.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextnoofMechanics);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        btnSubmitMobi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strOwnerMobileNumberFirst = mEditTextOwnerMobileNumberFirst.getText().toString();

                Log.e("", "----------------------------Mechanic registration phone number------------------------------\n"
                        + strOwnerMobileNumberFirst + "\n\n\n\n\n");

                if (strOwnerMobileNumberFirst.equals("") || strOwnerMobileNumberFirst == null) {
                    //alertDialog3("Gulf Oil","Please enter mobile number");
                    com.taraba.gulfoilapp.customdialogs.CustomOKDialog cd = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Please Enter Mobile Number", "Mechanic Registration");
                    cd.show();
                } else {
                    if (strOwnerMobileNumberFirst.length() < 10) {
                        // alertDialog3("Gulf Oil", "Invalid mobile number");
                        com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Invalid mobile number", "Mechanic Registration");
                        cod.show();
                    } else {
                        setEmptyText();

                        if (NetworkUtils.isNetworkAvailable(getActivity())) {
                            if (strOwnerMobileNumberFirst.equals("")) {
                                mEditTextOwnerMobileNumberFirst.setError("Enter " + getResources().getString(R.string.m_ownerMobNo));
                                mEditTextOwnerMobileNumberFirst.requestFocus();
                            } else {
                                SharedPreferences preferences1 = AppConfig.getInstance().getSharedPreferences(
                                        "signupdetails", Context.MODE_PRIVATE);
                                String loginId = preferences1.getString("usertrno", "");
                                String user_type = preferences1.getString("user_type", "");
                                new LogedIn().execute(new String[]{
                                        mEditTextOwnerMobileNumberFirst.getText().toString(), "Search By Mobile Number", user_type, loginId});
                            }
                        } else {
                            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedIdForGender = radioGroupGender.getCheckedRadioButtonId();
                int selectedIdForSector = radioGroupSector.getCheckedRadioButtonId();

                lin_mech_code.setBackgroundColor(Color.WHITE);

                //int selectedIdForSpecialization = radioGroupSpecialization.getCheckedRadioButtonId();
                // find which radioButton is checked by id
                if (selectedIdForGender == rBtnMale.getId()) {
                    strGender = rBtnMale.getText().toString().toLowerCase();
                } else {
                    strGender = rBtnFemale.getText().toString().toLowerCase();
                }

               /* if (selectedIdForSector == rBtnautomotive.getId()) {
                    //strSector = rBtnautomotive.getText().toString().toLowerCase();
                    strSector = "as";
                } else {
                    // strSector = rBtntractor.getText().toString().toLowerCase();
                    strSector = "fes";
                }*/

                StringBuffer strSpecialization1 = new StringBuffer();
                strSector = "";

                if (rBtnautomotive.isChecked()) {
                    strSector = "as";
                }
                if (rBtntractor.isChecked()) {
                    strSector = "fes";
                }


                if (rBtnengine.isChecked()) {
                    strSpecialization1.append("Engine");
                }
                if (rBtnhydraulics.isChecked()) {
                    strSpecialization1.append("|Hydraulics");
                }
                if (rBtntransmission.isChecked()) {
                    strSpecialization1.append("|Transmission");
                }
                if (rBtnDentingPanting.isChecked()) {
                    strSpecialization1.append("|Denting_Painting");
                }
                if (rBtnmultiple.isChecked()) {
                    strSpecialization1.append("|Multiple");
                }
                if (rBtnreborer.isChecked()) {
                    strSpecialization1.append("|Reborer");
                }
                strSpecialization = strSpecialization1.toString();
                strFirstName = mEditTextFirstName.getText().toString();
                strLastName = mEditTextLastName.getText().toString();
                strNomineName = mEditTextNomineName.getText().toString();
                strNomineRelation = mEditTextNomineRelation.getText().toString();
                strMotherName = mEditTextMotherName.getText().toString();
                strWorkShopName = mEditTextWorkShopName.getText().toString();
                strPincode = mEditTextPincode.getText().toString();
                strTaluka = mEditTextTaluka.getText().toString();
                strVillage = mEditTextVillage.getText().toString();
                strshopAdress = mEditTextshopAdress.getText().toString();
                strLandmark = mEditTextLandmark.getText().toString();
                strOwnerMobileNumber = mEditTextOwnerMobileNumber.getText().toString();
                strEmail = mEditTextEmail.getText().toString();
                strDateOfBirth = mEditTextDateOfBirth.getText().toString();
                // strDateOfBirth = convertDateFormat(mEditTextDateOfBirth.getText().toString());
                strregFillDate = mEditTextregFillDate.getText().toString();
                // strregFillDate = convertDateFormat(mEditTextregFillDate.getText().toString());
                strregtoatalSperConsumptioperMonth = mEditTextregtoatalSperConsumptioperMonth.getText().toString();
                strsperpartconsuptionPErMonth = mEditTextsperpartconsuptionPErMonth.getText().toString();
                strmmgenuinesparespartsconsuptionPerMoth = mEditTextmmgenuinesparespartsconsuptionPerMoth.getText().toString();
                strtotalVehicalsperMonth = mEditTexttotalVehicalsperMonth.getText().toString();
                strmahindraVehicalsperMonth = mEditTextmahindraVehicalsperMonth.getText().toString();
                strnoofMechanics = mEditTextnoofMechanics.getText().toString();


                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    if (strFirstName.equals("")) {
                        mEditTextFirstName.setError("Enter " + getResources().getString(R.string.m_first_name));
                        mEditTextFirstName.requestFocus();
                    } else if (strLastName.equals("")) {
                        mEditTextLastName.setError("Enter " + getResources().getString(R.string.m_last_name));
                        mEditTextLastName.requestFocus();
                    } else if (strGender.equals("") || strGender == null) {
                        Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_LONG).show();
                    } else if (strNomineName.equals("")) {
                        mEditTextNomineName.setError("Enter " + getResources().getString(R.string.m_nominie_name));
                        mEditTextNomineName.requestFocus();
                    } else if (strNomineRelation.equals("")) {
                        mEditTextNomineRelation.setError("Enter " + getResources().getString(R.string.m_nominie_relation));
                        mEditTextNomineRelation.requestFocus();
                    } else if (strMotherName.equals("")) {
                        mEditTextMotherName.setError("Enter " + getResources().getString(R.string.m_mother_name));
                        mEditTextMotherName.requestFocus();
                    } else if (strWorkShopName.equals("")) {
                        mEditTextWorkShopName.setError("Enter " + getResources().getString(R.string.m_workshopname));
                        mEditTextWorkShopName.requestFocus();
                    } else if (strState.startsWith("--")) {
                        Toast.makeText(getActivity(), "Please Select State", Toast.LENGTH_LONG).show();
                    } else if (strDistrict.startsWith("select district")) {
                        Toast.makeText(getActivity(), "Please Select District", Toast.LENGTH_LONG).show();
                    } else if (strPincode.equals("")) {
                        mEditTextPincode.setError("Enter " + getResources().getString(R.string.m_pincode));
                        mEditTextPincode.requestFocus();
                    } else /*else if( strTaluka.equals("") )
                    {
                        mEditTextTaluka.setError("Enter " + getResources().getString(R.string.m_taluka));
                        mEditTextTaluka.requestFocus();
                    }*/  if (strVillage.equals("")) {
                        mEditTextVillage.setError("Enter " + getResources().getString(R.string.m_village_city_town));
                        mEditTextVillage.requestFocus();
                    } else if (strshopAdress.equals("")) {
                        mEditTextshopAdress.setError("Enter " + getResources().getString(R.string.m_shop_add));
                        mEditTextshopAdress.requestFocus();
                    }/*else if(strLandmark.equals("") ) {
                        mEditTextLandmark.setError("Enter "+ getResources().getString(R.string.m_landmark));
                        mEditTextLandmark.requestFocus();
                    }*/
                    if (strOwnerMobileNumber.equals("")) {
                        mEditTextOwnerMobileNumber.setError("Enter " + getResources().getString(R.string.m_ownerMobNo));
                        mEditTextOwnerMobileNumber.requestFocus();
                    }/*else if( strEmail.equals("") ) {
                        mEditTextEmail.setError("Enter "+ getResources().getString(R.string.m_emailAsddress));
                        mEditTextEmail.requestFocus();
                    }*/ else if (strSector.equals("") || strSector == null) {
                        Toast.makeText(getActivity(), "Please Select Sector", Toast.LENGTH_LONG).show();
                    } else if (strSpecialization.equals("") || strSpecialization == null) {
                        Toast.makeText(getActivity(), "Please Select Specialization", Toast.LENGTH_LONG).show();
                    } else if (strDateOfBirth.equals("")) {
                        mEditTextDateOfBirth.setError("Enter " + getResources().getString(R.string.m_dateofBirth));
                        mEditTextDateOfBirth.requestFocus();
                    } else if (strregFillDate.equals("")) {
                        mEditTextregFillDate.setError("Enter " + getResources().getString(R.string.m_registrationfillDate));
                        mEditTextregFillDate.requestFocus();
                    } else if (strregtoatalSperConsumptioperMonth.equals("")) {
                        mEditTextregtoatalSperConsumptioperMonth.setError("Enter " + getResources().getString(R.string.m_toatalSperConsumptioperMonth));
                        mEditTextregtoatalSperConsumptioperMonth.requestFocus();
                    } else if (strsperpartconsuptionPErMonth.equals("")) {
                        mEditTextsperpartconsuptionPErMonth.setError("Enter " + getResources().getString(R.string.m_sperpartconsuptionPErMonth));
                        mEditTextsperpartconsuptionPErMonth.requestFocus();
                    } else if (strmmgenuinesparespartsconsuptionPerMoth.equals("")) {
                        mEditTextmmgenuinesparespartsconsuptionPerMoth.setError("Enter " + getResources().getString(R.string.m_mmgenuinesparespartsconsuptionPerMoth));
                        mEditTextmmgenuinesparespartsconsuptionPerMoth.requestFocus();
                    } else if (strtotalVehicalsperMonth.equals("")) {
                        mEditTexttotalVehicalsperMonth.setError("Enter " + getResources().getString(R.string.m_totalVehicalsperMonth));
                        mEditTexttotalVehicalsperMonth.requestFocus();
                    } else if (strmahindraVehicalsperMonth.equals("")) {
                        mEditTextmahindraVehicalsperMonth.setError("Enter " + getResources().getString(R.string.m_mahindraVehicalsperMonth));
                        mEditTextmahindraVehicalsperMonth.requestFocus();
                    } else if (strnoofMechanics.equals("")) {
                        mEditTextnoofMechanics.setError("Enter " + getResources().getString(R.string.m_noofMechanics));
                        mEditTextnoofMechanics.requestFocus();
                    }/*else  if(!isValidEmail(strEmail)){
                        mEditTextEmail.setError("Invalid "+getResources().getString(R.string.m_emailAsddress)+" Format"); *//*"Invalid Text" or something like getString(R.string.Invalid)*//*
                        mEditTextEmail.requestFocus();
                    }*/ else if (!isImageUpdate) {
                        Toast.makeText(getActivity(), "Please Select Photo", Toast.LENGTH_LONG).show();
                        RegphotoIV.requestFocus();
                    } else {
                        Log.e("Image in befor pass: ", "in Befor pass :" + finalPathOfImageUrl + "strState :" + strState + " strDistrict :" + strDistrict);
                        SharedPreferences preferences = AppConfig.getInstance().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);

                        String mStringusertrno = preferences.getString("usertrno", "");
                        String mStringuser_type = preferences.getString("user_type", "");
                        String mStringmgmt_user_id = preferences.getString("mgmt_user_id", "");


                      /*  Bitmap bitmap=BitmapFactory.decodeResource(getResources(), finalPathOfImageUrl);
//R.drawable.images is image for my ImageView
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                        byte[] imageByteArray = stream.toByteArray();
                        System.out.println("byte array:"+ imageByteArray);

*/
                        // Toast.makeText(getActivity(),"encoded string on submit is:"+strRegPhoto,Toast.LENGTH_SHORT).show();
                        JSONObject mStringuser_data = new JSONObject();
                        try {

                            mStringuser_data.put("login_user_type", mStringuser_type);
                            mStringuser_data.put("mgmt_user_id", mStringmgmt_user_id);
                            mStringuser_data.put("login_id", mStringusertrno);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        strDateOfBirth = convertDateFormat(mEditTextDateOfBirth.getText().toString());
                        // strregFillDate = mEditTextregFillDate.getText().toString();
                        strregFillDate = convertDateFormat(mEditTextregFillDate.getText().toString());
                        Log.e("SECTOR ", "SECTOR : " + strSector);
                        new UploadData().execute(new String[]{strRegPhoto, strFirstName, strLastName, strNomineName, strNomineRelation, strMotherName, strWorkShopName,
                                strPincode, strTaluka, strVillage, strshopAdress, strLandmark, strOwnerMobileNumber, strEmail, strDateOfBirth, strregFillDate,
                                strregtoatalSperConsumptioperMonth, strsperpartconsuptionPErMonth, strmmgenuinesparespartsconsuptionPerMoth, strtotalVehicalsperMonth,
                                strmahindraVehicalsperMonth, strnoofMechanics, strGender, strSector, strSpecialization.toString(), strState, strDistrict, finalPathOfImageUrl, strUserTrno, mStringuser_data.toString()
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                }
                Log.e("strSpecialization ", "strSpecialization : " + strSpecialization);
            }

        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProfile();
            }
        });

        btn_update_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProfile();
            }
        });

        return view;
    }

    public void updateProfile() {
        updateProfile = true;
        // Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

        if (NetworkUtils.isNetworkAvailable(getActivity())) {

            StringBuffer strSpecialization1 = new StringBuffer();
            if (rBtnengine.isChecked()) {
                strSpecialization1.append("Engine");
            }
            if (rBtnhydraulics.isChecked()) {
                strSpecialization1.append("|Hydraulics");
            }
            if (rBtntransmission.isChecked()) {
                strSpecialization1.append("|Transmission");
            }
            if (rBtnDentingPanting.isChecked()) {
                strSpecialization1.append("|Denting_Painting");
            }
            if (rBtnmultiple.isChecked()) {
                strSpecialization1.append("|Multiple");
            }
            if (rBtnreborer.isChecked()) {
                strSpecialization1.append("|Reborer");
            }
            String strSpecialization123 = strSpecialization1.toString();

            int selectedIdForGender = radioGroupGender.getCheckedRadioButtonId();
            String strGender123 = "";
            if (selectedIdForGender == rBtnMale.getId()) {
                strGender123 = rBtnMale.getText().toString().toLowerCase();
            } else {
                strGender123 = rBtnFemale.getText().toString().toLowerCase();
            }

            String dob = convertDateFormat(mEditTextDateOfBirth.getText().toString());
            // strregFillDate = mEditTextregFillDate.getText().toString();
            String reg_dob = convertDateFormat(mEditTextregFillDate.getText().toString());

            new UpdateProfile().execute(new String[]{strParticipnt_login_id, mEditTextFirstName.getText().toString(),
                    mEditTextLastName.getText().toString(), mEditTextOwnerMobileNumber.getText().toString(),
                    mEditTextTaluka.getText().toString(), mEditTextshopAdress.getText().toString(),
                    mEditTextnoofMechanics.getText().toString(), reg_dob, mEditTextsperpartconsuptionPErMonth.getText().toString(),
                    strGender123, mEditTextVillage.getText().toString(), strDistrict, mEditTextMotherName.getText().toString(),
                    mEditTextmahindraVehicalsperMonth.getText().toString(), mEditTextmmgenuinesparespartsconsuptionPerMoth.getText().toString(),
                    mEditTextNomineName.getText().toString(), mEditTextPincode.getText().toString(), strSpecialization123,
                    mEditTextLandmark.getText().toString(), mEditTexttotalVehicalsperMonth.getText().toString(), dob,
                    mEditTextWorkShopName.getText().toString(), mEditTextNomineRelation.getText().toString(),
                    mEditTextregtoatalSperConsumptioperMonth.getText().toString(),
                    mEditTextEmail.getText().toString(), strRegPhoto});

        } else {
            Toast.makeText(getActivity(),
                    "Internet Disconnected", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            //editText.setError("Invalid");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String emailInput) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailInput);
        return matcher.matches();
    }

    private void showDatePickerDOB() {
        SelectDateFragment date = new SelectDateFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate1);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String mStringMonth = "" + (monthOfYear + 1);
            if ((monthOfYear + 1) < 10) {
                mStringMonth = "0" + (monthOfYear + 1);
            }

            String mStringDay = "" + dayOfMonth;
            if (dayOfMonth < 10) {
                mStringDay = "0" + dayOfMonth;
            }
            mEditTextDateOfBirth.setText(mStringDay + "-" + mStringMonth + "-"
                    + year);

           /* mEditTextDateOfBirth.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));*/
        }
    };

    public void setEmptyText() {
        mEditTextFirstName.setText("");
        mEditTextLastName.setText("");
        mEditTextNomineName.setText("");
        mEditTextNomineRelation.setText("");
        mEditTextMotherName.setText("");
        mEditTextWorkShopName.setText("");
        mEditTextPincode.setText("");
        mEditTextTaluka.setText("");
        mEditTextVillage.setText("");
        mEditTextshopAdress.setText("");
        mEditTextLandmark.setText("");
        mEditTextOwnerMobileNumber.setText("");
        mEditTextOwnerMobileNumber.setText("");
        mEditTextEmail.setText("");
        mEditTextDateOfBirth.setText("");
        mEditTextregFillDate.setText("");
        mEditTextregtoatalSperConsumptioperMonth.setText("");
        mEditTextsperpartconsuptionPErMonth.setText("");
        mEditTextmmgenuinesparespartsconsuptionPerMoth.setText("");
        mEditTexttotalVehicalsperMonth.setText("");
        mEditTextmahindraVehicalsperMonth.setText("");
        mEditTextnoofMechanics.setText("");
    }

    public void settingStarsForLabels(View view) {
        setStarForLabel((TextView) view.findViewById(R.id.lblFirstName));
        setStarForLabel((TextView) view.findViewById(R.id.lblLastName));
        setStarForLabel((TextView) view.findViewById(R.id.txtgendet));
        setStarForLabel((TextView) view.findViewById(R.id.lblNominiName));
        setStarForLabel((TextView) view.findViewById(R.id.lblNominiRelation));
        setStarForLabel((TextView) view.findViewById(R.id.lblMotherName));
        setStarForLabel((TextView) view.findViewById(R.id.lblWorkshopName));
        setStarForLabel((TextView) view.findViewById(R.id.lblspn_state));
        setStarForLabel((TextView) view.findViewById(R.id.lblspn_district));
        setStarForLabel((TextView) view.findViewById(R.id.lblPincode));
        //  setStarForLabel((TextView) view.findViewById(R.id.lblTaluka));
        setStarForLabel((TextView) view.findViewById(R.id.lblVillageCityTown));
        setStarForLabel((TextView) view.findViewById(R.id.lblShopAddress));
        setStarForLabel((TextView) view.findViewById(R.id.lblOwnerMobileNumber));
       /* setStarForLabel((TextView) view.findViewById(R.id.lblEmailAddress));*/
        setStarForLabel((TextView) view.findViewById(R.id.txtsector));
        setStarForLabel((TextView) view.findViewById(R.id.txtspecialization));
        setStarForLabel((TextView) view.findViewById(R.id.lblDateOfBirth));
        setStarForLabel((TextView) view.findViewById(R.id.lblRegistrationFillDate));
        setStarForLabel((TextView) view.findViewById(R.id.lbltoatalSperConsumptioperMonth));
        setStarForLabel((TextView) view.findViewById(R.id.lblsperpartconsuptionPErMonth));
        setStarForLabel((TextView) view.findViewById(R.id.lblmmgenuinesparespartsconsuptionPerMoth));
        setStarForLabel((TextView) view.findViewById(R.id.lbltotalVehicalsperMonth));
        setStarForLabel((TextView) view.findViewById(R.id.lblmahindraVehicalsperMonth));
        setStarForLabel((TextView) view.findViewById(R.id.lblnoofMechanics));
    }

    public void setStarForLabel(TextView txt) {
        txt.setText(Html.fromHtml(txt.getText() + "<font color='#EE0000'> * </font>"));
    }

    private void showDatePickerRegDate() {
        SelectDateFragment date = new SelectDateFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate2);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate2 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String mStringMonth = "" + (monthOfYear + 1);
            if ((monthOfYear + 1) < 10) {
                mStringMonth = "0" + (monthOfYear + 1);
            }

            String mStringDay = "" + dayOfMonth;
            if (dayOfMonth < 10) {
                mStringDay = "0" + dayOfMonth;
            }
            mEditTextregFillDate.setText(mStringDay + "-" + mStringMonth + "-"
                    + year);

           /* mEditTextregFillDate.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));*/
        }
    };

    class UpdateProfile extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String[]... params) {
            JSONObject jObj = null;
            try {
               /* Log.e("Image in befor pass: ", "in Befor pass :" + params[0][26]);
                Log.e("in befor pass: ", "UserData pass :" + params[0][29]);*/
                Log.e("in befor pass: ", "UserData pass path:" + params[0][0]);
                jObj = new UserFunctions().upload_profile(Integer.parseInt(params[0][0]), params[0][1], params[0][2], params[0][3], params[0][4], params[0][5], params[0][6], params[0][7],
                        params[0][8], params[0][9], params[0][10], params[0][11], params[0][12], params[0][13], params[0][14], params[0][15], params[0][16], params[0][17],
                        params[0][18], params[0][19], params[0][20], params[0][21], params[0][22], params[0][23], params[0][24], params[0][25]);
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("", "Exception*********" + e);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getActivity(), "Network Error...",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            // showToast("Calling set up views");
            progressDialog.dismiss();
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {
                       /* alertDialogNew(
                                getResources().getString(R.string.app_name),
                                getErrorMessage(jObj.getString("error")));*/

                        if (jObj.has("error")) {
                            //Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                            CustomOKDialog cd = new CustomOKDialog(getActivity(), "" + jObj.getString("error"), getResources().getString(R.string.app_name), "message");
                            cd.show();
                        } else {
                            CustomOKDialog cd = new CustomOKDialog(getActivity(), "User is already been verified, OTP verification is pending", getResources().getString(R.string.app_name), "error");
                            cd.show();
                        }


                    } else if (mStringStatus.equals("success")) {

                        if (jObj.has("message")) {
                            //Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                            CustomOKDialog cd = new CustomOKDialog(getActivity(), "" + jObj.getString("message"), getResources().getString(R.string.app_name), "message");
                            cd.show();
                        } else {
                            CustomOKDialog cd = new CustomOKDialog(getActivity(), "User is already been verified, OTP verification is pending", getResources().getString(R.string.app_name), "error");
                            cd.show();
                        }
                    }

                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

    public void initComp(View view) {
        txtmechanicregistration = (TextView) view.findViewById(R.id.txtmechanicregistration);
        txt_first_name = (TextView) view.findViewById(R.id.lblFirstName);
        txt_middle_name = (TextView) view.findViewById(R.id.lblLastName);
        linMobNoReglayout = (LinearLayout) view.findViewById(R.id.lin_sc_Logincontainer);
        scrMainlaout = (ScrollView) view.findViewById(R.id.sc_loginform_container);
        txthead = (TextView) view.findViewById(R.id.txthead);

        RegphotoIV = (ImageView) view.findViewById(R.id.IvRegProfile);
        RegphotoIV1 = (ImageView) view.findViewById(R.id.IvRegProfile1);
        mSpinnerState = (Spinner) view.findViewById(R.id.spn_state);
        mSpinnerDistrict = (Spinner) view.findViewById(R.id.spn_district);

        txtdist = (TextView) view.findViewById(R.id.text_district);

        mEditTextFirstName = (EditText) view.findViewById(R.id.edt_m_firstname);
        mEditTextLastName = (EditText) view.findViewById(R.id.edt_m_lastname);
        mEditTextNomineName = (EditText) view.findViewById(R.id.edt_nominieName);
        mEditTextNomineRelation = (EditText) view.findViewById(R.id.edt_nominieRelation);
        mEditTextMotherName = (EditText) view.findViewById(R.id.edt_mothername);
        mEditTextWorkShopName = (EditText) view.findViewById(R.id.edt_workshopname);
        mEditTextPincode = (EditText) view.findViewById(R.id.edt_pincode);
        mEditTextTaluka = (EditText) view.findViewById(R.id.edt_taluka);
        mEditTextVillage = (EditText) view.findViewById(R.id.edt_village);
        mEditTextshopAdress = (EditText) view.findViewById(R.id.edt_shopAdress);
        mEditTextLandmark = (EditText) view.findViewById(R.id.edt_landmark);
        mEditTextOwnerMobileNumber = (EditText) view.findViewById(R.id.edt_owner_mobile_number);
        mEditTextEmail = (EditText) view.findViewById(R.id.edt_email);
        mEditTextDateOfBirth = (EditText) view.findViewById(R.id.edt_dateofbirth);
        mEditTextregFillDate = (EditText) view.findViewById(R.id.edt_regFillDate);
        mEditTextregtoatalSperConsumptioperMonth = (EditText) view.findViewById(R.id.edt_toatalSperConsumptioperMonth);
        mEditTextsperpartconsuptionPErMonth = (EditText) view.findViewById(R.id.edt_sperpartconsuptionPErMonth);
        mEditTextmmgenuinesparespartsconsuptionPerMoth = (EditText) view.findViewById(R.id.edt_mmgenuinesparespartsconsuptionPerMoth);
        mEditTexttotalVehicalsperMonth = (EditText) view.findViewById(R.id.edt_totalVehicalsperMonth);
        mEditTextmahindraVehicalsperMonth = (EditText) view.findViewById(R.id.edt_mahindraVehicalsperMonth);
        mEditTextnoofMechanics = (EditText) view.findViewById(R.id.edt_noofMechanics);

        mEditTextOwnerMobileNumberFirst = (EditText) view.findViewById(R.id.edt_mobile_number_first);

        radioGroupGender = (RadioGroup) view.findViewById(R.id.myRadioGroupGender);
        radioGroupSector = (RadioGroup) view.findViewById(R.id.myRadioGroupSector);
        //radioGroupSpecialization  = (RadioGroup)view.findViewById(R.id.myRadioGroupSpecialization);

        rBtnMale = (RadioButton) view.findViewById(R.id.rBtn_male);
        rBtnFemale = (RadioButton) view.findViewById(R.id.rBtn_female);

        rBtnautomotive = (RadioButton) view.findViewById(R.id.rBtn_automotive);
        rBtntractor = (RadioButton) view.findViewById(R.id.rBtn_tractor);

        rBtnengine = (CheckBox) view.findViewById(R.id.rBtn_engine);
        rBtnhydraulics = (CheckBox) view.findViewById(R.id.rBtn_hydraulics);
        rBtntransmission = (CheckBox) view.findViewById(R.id.rBtn_transmission);
        rBtnDentingPanting = (CheckBox) view.findViewById(R.id.rBtn_DentingPanting);
        rBtnmultiple = (CheckBox) view.findViewById(R.id.rBtn_multiple);
        rBtnreborer = (CheckBox) view.findViewById(R.id.rBtn_reborer);

        btnSubmit = (Button) view.findViewById(R.id.btn_submit_mech_registration);
        btnSubmitMobi = (Button) view.findViewById(R.id.btn_submit_mob_registration);
        btnUpdateProfile = (Button) view.findViewById(R.id.btn_update_profile);

        btn_update_photo = (Button) view.findViewById(R.id.btn_update_photo);

        lin_mech_code = (LinearLayout) view.findViewById(R.id.lin_mech_code);
        txt_mechanic_code = (TextView) view.findViewById(R.id.txt_mechanic_code);
    }

    private void selectProfileImage() {
        // TODO Auto-generated method stub

        final CharSequence[] options = {"Photo From Camera",
                "Choose From Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());
        builder.setTitle("    Add Photo");
        // builder.setIcon(R.drawable.logoimage);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Photo From Camera")) {

                    //---------------------- Code for select image without crop---------------------
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    .getPath());
                    startActivityForResult(intent, 1);

                    //------------------------------------------------------------------------------
/*
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    MechanicRegistrationFragment titleFragmentById = (MechanicRegistrationFragment) fragmentManager.findFragmentByTag("Registration");

                    RegphotoIV.setImageDrawable(null);
                    Crop.pickImage(getContext(), titleFragmentById, "Camera");*/
                }
                if (options[item].equals("Choose From Gallery")) {
                    //---------------------- Code for select image without crop---------------------
                    /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image*//*");
                    startActivityForResult(photoPickerIntent, 2);*/
                    //------------------------------------------------------------------------------

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    MechanicRegistrationFragment titleFragmentById = (MechanicRegistrationFragment) fragmentManager.findFragmentByTag("Registration");

                    RegphotoIV.setImageDrawable(null);
                    Crop.pickImage(getActivity(), titleFragmentById, "Gallery");

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String encodedString = new String(Base64.encodeBase64(byteArray));

        return encodedString;
    }

    private void beginCrop(Uri source, String request) {

        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        MechanicRegistrationFragment titleFragmentById = (MechanicRegistrationFragment) fragmentManager.findFragmentByTag("Registration");

        if (request.equals("Camera"))
            Crop.of(source, destination).asSquare().start(getContext(), titleFragmentById, Crop.REQUEST_CROP_CAMERA);
        else if (request.equals("Gallery"))
            Crop.of(source, destination).asSquare().start(getContext(), titleFragmentById, Crop.REQUEST_CROP);
    }

    private void handleCrop(int resultCode, int requestCode, Intent result) {

        try {
            if (result != null) {
                if (resultCode == Activity.RESULT_OK) {
                    RegphotoIV.setImageURI(Crop.getOutput(result));
                    RegphotoIV1.setImageURI(Crop.getOutput(result));

                    Bitmap bitmap;
                    if (mStringStatus.equals("profile")) {
                        bitmap = ((BitmapDrawable) RegphotoIV1.getDrawable()).getBitmap();
                    } else {
                        bitmap = ((BitmapDrawable) RegphotoIV.getDrawable()).getBitmap();
                    }
                    isImageUpdate = true;
                    strRegPhoto = bitmapToBase64(bitmap);

                } else if (resultCode == Crop.RESULT_ERROR) {
                    Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getActivity(), "Error Capturing Image", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        resultIntent = data;

        if (requestCode == Crop.REQUEST_PICK_CAMERA && resultCode == getActivity().RESULT_OK) {
            if (data == null) {
                Toast.makeText(getActivity(), "Error Capturing Image", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences preferences = AppConfig.getInstance().getSharedPreferences("crop_image_url", Context.MODE_PRIVATE);
                String uriString = preferences.getString("image_uri", "");

                Uri uri = null;
                uri = Uri.parse(uriString);
                beginCrop(data.getData(), "Camera");
            }
        } else if (requestCode == Crop.REQUEST_CROP_CAMERA) {
            handleCrop(resultCode, requestCode, data);
        }

        if (requestCode == Crop.REQUEST_PICK && resultCode == getActivity().RESULT_OK) {
            beginCrop(data.getData(), "Gallery");
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, requestCode, data);
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {

                if (data != null) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    RegphotoIV.setImageBitmap(photo);
                    RegphotoIV1.setImageBitmap(photo);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getActivity(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    finalPathOfImageUrl = getRealPathFromURI(tempUri);

                    String extStorageDirectory = Environment
                            .getExternalStorageDirectory().toString();
                    File myDir = new File(extStorageDirectory, "GulfOil_images");
                    myDir.mkdir();
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname = "Image-" + n + ".jpg";
                    File file = new File(myDir, fname);
                    if (file.exists())
                        file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                        finalPathOfImageUrl = file.getAbsolutePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    isImageUpdate = true;
                    Bitmap bm = BitmapFactory.decodeFile(finalPathOfImageUrl);
                    strRegPhoto = bitmapToBase64(bm);

                    //-------------------- Camera Image path--------------------
                    Log.e("tag", "finalPathOfImageUrl-----" + finalPathOfImageUrl);
                    Log.e("tag", "tempUri-----" + tempUri);
                    beginCrop(tempUri, "Camera");
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    class LogedIn extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String[]... params) {
            JSONObject jObj = null;
            try {

                jObj = new UserFunctions().CheckParticipantAvailability(""
                        + params[0][0]);

            } catch (Exception e) {
                // TODO: handle exception
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Network Error...",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);

            progressDialog.dismiss();
            if (jObj != null) {
                try {
                    Log.e("Json Data : ", "Json data : " + jObj);

                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {
/*
                        alertDialog2(
                                getResources().getString(R.string.app_name),
                                "Number Not Registered, You want register it ?");sadvcsdbvsdv*/

                        com.taraba.gulfoilapp.customdialogs.CustomOKDialog cd = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), jObj.getString("error"), "Mechanic Registration");
                        cd.show();

                    } else if (mStringStatus.equals("success")) {

                        String availability = "" + jObj.getString("available");

                        Log.e("", "availability------------------" + availability);

                        if (availability.equals("false")) {

                            CustomYesNoDialog cd = new CustomYesNoDialog(getActivity());
                            cd.show();


                        } else if (availability.equals("true")) {
                            com.taraba.gulfoilapp.customdialogs.CustomOKDialog cd = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), jObj.getString("message"), "Mechanic Registration");
                            cd.show();
                        }
                    }
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

    class UploadData extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String[]... params) {
            JSONObject jObj = null;
            try {
                Log.e("Image in befor pass: ", "in Befor pass :" + params[0][26]);
                Log.e("in befor pass: ", "UserData pass :" + params[0][29]);
                Log.e("in befor pass: ", "UserData pass path:" + params[0][0]);
                jObj = new UserFunctions().Mech_Reg_Data(params[0][0], params[0][1], params[0][2], params[0][3], params[0][4], params[0][5], params[0][6], params[0][7],
                        params[0][8], params[0][9], params[0][10], params[0][11], params[0][12], params[0][13], params[0][14], params[0][15], params[0][16], params[0][17],
                        params[0][18], params[0][19], params[0][20], params[0][21], params[0][22], params[0][23], params[0][24], params[0][25], params[0][26], params[0][27], params[0][28], params[0][29]);
            } catch (Exception e) {
                // TODO: handle exception
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Network Error...",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            // showToast("Calling set up views");
            progressDialog.dismiss();
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {
                        alertDialogNew(
                                getResources().getString(R.string.app_name),
                                getErrorMessage(jObj.getString("error")));
                    } else if (mStringStatus.equals("success")) {
                        linMobNoReglayout.setVisibility(View.GONE);
                        scrMainlaout.setVisibility(View.VISIBLE);

                        String pcode = jObj.getString("participant_code");
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                       /* alertDialog1(
                                "Participant Code : " + jObj.getString("participant_code"),
                                "Mechanic Registration Successfully", Integer.parseInt(jObj.getString("participant_login_id")),
                                jObj.getString("mobile_no"));*/

                        Log.e("", "Participant code:" + pcode);
                       /* CustomRegistrationSuccessDialog cdd=new CustomRegistrationSuccessDialog(getActivity(), "Participant Code : " + jObj.getString("participant_code"),
                                " Participant code:"+pcode+". Mechanic Registration Successfully", Integer.parseInt(jObj.getString("participant_login_id")),
                                jObj.getString("mobile_no"));
                        cdd.show();*/

                        Fragment otpFragment = new Otp_fragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("participant_login_id", Integer.parseInt(jObj.getString("participant_login_id")));
                        bundle.putString("participant_mob_no", jObj.getString("mobile_no"));
                        bundle.putString("participant_code", "" + pcode);
                        otpFragment.setArguments(bundle);

                        SharedPreferences preferences = AppConfig.getInstance().getSharedPreferences("status", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("status", "");
                        editor.commit();
                        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                        ft1.replace(R.id.container_body, otpFragment);
                        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft1.addToBackStack(null);
                        getFragmentManager().popBackStack();
                        ft1.commit();

                    }

                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

    public String getErrorMessage(String jobj_str) {
        String strErrorMsg = "";
        try {
            if (jobj_str.startsWith("{")) {
                JSONObject jobj = new JSONObject(jobj_str);

                Map<String, String> map = new HashMap<String, String>();
                Iterator iter = jobj.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    String value = jobj.getString(key);
                    strErrorMsg = strErrorMsg + " " + key + " : " + value + "\n";
                    map.put(key, value);
                    Log.d("Key Value", "key: " + key + " Value: " + value);
                }
            } else {
                strErrorMsg = jobj_str;
            }
        } catch (Exception e) {

        }
        return strErrorMsg;
    }

    public void alertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        mEditTextOwnerMobileNumberFirst.setText("");
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void alertDialogNew(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Stay On Page", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Register New", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        int count = getFragmentManager().getBackStackEntryCount();
                        Log.e("count ", "Fragment Count : " + count);

                        if (getFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicRegistrationFragment) {
                            getFragmentManager().popBackStack();
                        }

                        int count_tt = getFragmentManager().getBackStackEntryCount();
                        Log.e("count ", "Fragment count_tt : " + count_tt);

                        Fragment mechanicRegistrationFragment = new MechanicRegistrationFragment();

                        FragmentTransaction ftmech = getFragmentManager().beginTransaction();
                        ftmech.replace(R.id.container_body, mechanicRegistrationFragment);
                        ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftmech.commit();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    class CustomRegistrationSuccessDialog extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button ok;
        int strid;
        String msg, title, strMobNo;
        TextView txtMsg, txtHeading;

        public CustomRegistrationSuccessDialog(Activity a, String title, String message, final int strid, final String strMobNo) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.msg = message;
            this.title = title;
            this.strid = strid;
            this.strMobNo = strMobNo;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog_login_invalid);
            ok = (Button) findViewById(R.id.btn_Ok);
            txtMsg = (TextView) findViewById(R.id.txtMsg);
            txtHeading = (TextView) findViewById(R.id.txtHeading);
            txtMsg.setText("" + msg);
            txtHeading.setText("" + title);
            ok.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_Ok:
                    dismiss();
                    Fragment otpFragment = new Otp_fragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("participant_login_id", strid);
                    bundle.putString("participant_mob_no", strMobNo);
                    bundle.putString("status", "" + mStringStatus);
                    otpFragment.setArguments(bundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container_body, otpFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    getFragmentManager().popBackStack();
                    ft.commit();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    public void alertDialog2(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        linMobNoReglayout.setVisibility(View.GONE);
                        scrMainlaout.setVisibility(View.VISIBLE);
                        mEditTextOwnerMobileNumber.setText(strOwnerMobileNumberFirst);
                        mEditTextOwnerMobileNumber.setKeyListener(null);
                        date_time();
                        btnSubmit.setVisibility(View.VISIBLE);
                    }
                }).setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                mEditTextOwnerMobileNumberFirst.setText("");
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    class CustomYesNoDialog extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        TextView txtMsg;

        public CustomYesNoDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_logout_dialog);
            txtMsg = (TextView) findViewById(R.id.txt_msg);
            txtMsg.setText("Number Not Registered, You want to register it ?");
            yes = (Button) findViewById(R.id.btn_Yes);
            no = (Button) findViewById(R.id.btn_No);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_Yes:
                    dismiss();
                    linMobNoReglayout.setVisibility(View.GONE);
                    scrMainlaout.setVisibility(View.VISIBLE);
                    mEditTextOwnerMobileNumber.setText(strOwnerMobileNumberFirst);
                    //  mEditTextOwnerMobileNumber.setKeyListener(null);
                    date_time();
                    btnSubmit.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn_No:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    public void alertDialog3(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void getSpinnerdata(String spinvalue, ArrayList<ArrayList<String>> arry,
                               Spinner spinname) {
        Log.e("In function", "arry lenth : " + arry.size() + ",spinerval : "
                + spinvalue.toLowerCase());
        for (int i = 0; i < arry.size(); i++) {

            Log.e("In for", "" + arry.get(i).get(0).toLowerCase());
            if (spinvalue.toLowerCase().equals(arry.get(i).get(0).toString().toLowerCase())) {
                Log.e("In if", "" + i + " : " + spinvalue + " : " + arry.get(i).get(0).toString().toLowerCase() + " : " + spinname.getId());
                spinname.setSelection(i);
                break;
            }
        }
    }

    public ArrayList<ArrayList<String>> convertState(String string) {
        ArrayList<ArrayList<String>> ModelModel = new ArrayList<>();
        try {

            JSONObject jobjcat = new JSONObject(string);
            JSONObject jobstate = jobjcat.getJSONObject("state");
            //  JSONArray mJsonArray = new JSONArray(string);
            Log.e("Whole JSON ARRAy", "Model data :" + jobstate);
            Log.e("Whole ", "Whole JSon Size : " + jobstate.length());
            Iterator<String> iterator = jobstate.keys();
            while (iterator.hasNext()) {
                ArrayList<String> Cat = new ArrayList<String>();
                String key = iterator.next();
                Log.i("TAG", "key:" + key + "--Value::" + jobstate.optString(key));

                Cat.add(jobstate.optString(key));

                ModelModel.add(Cat);
            }

            /*for (int i = 0; i < jobstate.length(); i++) {
                ArrayList<String> Cat=new ArrayList<String>();
              //  String strstate = jobjcat.getString("state");

                Log.e("JSON json object", "model : "+jobstate);
                Cat.add(jobstate.getString("maharashtra"));
                //= CategoryModel + jobjcat.getString("Categories") + "-";
                //JSONArray mJsonModel = jobjcat.getJSONArray("model");
                //String S1 =jobjcat.getJSONArray("ProductModel").toString();
                //Cat.add(mJsonModel.toString());
                  *//*      for (int j = 0; j < mJsonProduct.length(); j++) {
                     JSONObject jobjmodel = mJsonProduct.getJSONObject(j);
                        //CategoryModel = CategoryModel + jobjmodel.getString("Product") + "-";
                        Cat.add(jobjmodel.getString("Product"));
                        JSONArray mJsonModel = jobjmodel.getJSONArray("Models");
                  for(int k=0; k< mJsonModel.length(); k++)
                  {
                     Log.e("JSON json object", "Product : "+jobjmodel);
                     CategoryModel = CategoryModel + mJsonModel.getJSONObject(k).getString("model") + ",";
                  }
                  CategoryModel = CategoryModel.substring(0, CategoryModel.lastIndexOf(",")) + ",\n";
                  }*//*

                ModelModel.add(Cat);
            }*/

            //CategoryModel = CategoryModel.substring(0, CategoryModel.lastIndexOf(","));
            Log.e("Category Data : ", "Model Data Cnverted : " + ModelModel);
        } catch (Exception e) {

        }
        return ModelModel;
    }

    public ArrayList<ArrayList<String>> convertDistrict(String string) {
        ArrayList<ArrayList<String>> ModelModel = new ArrayList<>();

        ArrayList<String> Cat1 = new ArrayList<String>();
        Cat1.add("Select District");
        ModelModel.add(Cat1);

        // ModelModel.add
        try {
            JSONObject jobjcat = new JSONObject(string);
            JSONObject jobdistrict = jobjcat.getJSONObject("district");
            //  JSONArray mJsonArray = new JSONArray(string);
            Log.e("Whole JSON ARRAy", "Model data :" + jobdistrict);
            Log.e("Whole ", "Whole JSon Size : " + jobdistrict.length());
            Iterator<String> iterator = jobdistrict.keys();
            while (iterator.hasNext()) {
                ArrayList<String> Cat = new ArrayList<String>();
                String key = iterator.next();
                Log.i("TAG", "key:" + key + "--Value::" + jobdistrict.optString(key));

                Cat.add(jobdistrict.optString(key));

                ModelModel.add(Cat);
            }

            /*for (int i = 0; i < jobdistrict.length(); i++) {
                ArrayList<String> Cat=new ArrayList<String>();
                //  String strstate = jobjcat.getString("state");

                Log.e("JSON json object", "model : "+jobdistrict);
                Cat.add(jobdistrict.getString("akola"));
                //= CategoryModel + jobjcat.getString("Categories") + "-";
                //JSONArray mJsonModel = jobjcat.getJSONArray("model");
                //String S1 =jobjcat.getJSONArray("ProductModel").toString();
                //Cat.add(mJsonModel.toString());
                  *//*      for (int j = 0; j < mJsonProduct.length(); j++) {
                     JSONObject jobjmodel = mJsonProduct.getJSONObject(j);
                        //CategoryModel = CategoryModel + jobjmodel.getString("Product") + "-";
                        Cat.add(jobjmodel.getString("Product"));
                        JSONArray mJsonModel = jobjmodel.getJSONArray("Models");
                  for(int k=0; k< mJsonModel.length(); k++)
                  {
                     Log.e("JSON json object", "Product : "+jobjmodel);
                     CategoryModel = CategoryModel + mJsonModel.getJSONObject(k).getString("model") + ",";
                  }
                  CategoryModel = CategoryModel.substring(0, CategoryModel.lastIndexOf(",")) + ",\n";
                  }*//*

                ModelModel.add(Cat);
            }*/

            //CategoryModel = CategoryModel.substring(0, CategoryModel.lastIndexOf(","));
            Log.e("Category Data : ", "Model Data Cnverted : " + ModelModel);
        } catch (Exception e) {

        }
        return ModelModel;
    }

    public String convertDateFormat(String datetoconvert) {
        String date_ = "";
        if (datetoconvert.contains("-")) {
            String[] s = datetoconvert.split("-");
            date_ = s[2] + "-" + s[1] + "-" + s[0];
        } else {
            date_ = "";
        }
        return date_;
    }

    public String SplitDateFormat(String datetosplit) {
        StringTokenizer tk = new StringTokenizer(datetosplit);
        String finaldob = tk.nextToken();  // <---  yyyy-mm-dd
        return finaldob;
    }

    public String SplitStringSpecialization(String datetosplit) {

        String[] splits = datetosplit.split("\\|");  // two \\ is required because "\"     itself require escaping

        String strEngine = null;
        String strHydraulics = null;
        String strTransmission = null;
        String strDenting_Painting = null;
        String strMultiple = null;
        String strReborer = null;

        //CheckBox[] cb = new CheckBox[6];
        Log.e("String Length", "Str length : " + splits.length);
        for (String asset : splits) {
            Log.e("Split String :", "Split string : " + asset);
            if (asset.equals("Engine")) {
                strEngine = asset;
            }
            if (asset.equals("Hydraulics")) {
                strHydraulics = asset;
            }
            if (asset.equals("Transmission")) {
                strTransmission = asset;
            }
            if (asset.equals("Denting_Painting")) {
                strDenting_Painting = asset;
            }
            if (asset.equals("Multiple")) {
                strMultiple = asset;
            }
            if (asset.equals("Reborer")) {
                strReborer = asset;
            }
        }
        if (strEngine == null) {

        } else {

            if (strEngine.equals("Engine")) {
                rBtnengine.setChecked(true); //to check

            }
        }
        if (strHydraulics == null) {

        } else {
            if (strHydraulics.equals("Hydraulics")) {
                rBtnhydraulics.setChecked(true); //to check
            }
        }
        if (strTransmission == null) {

        } else {
            if (strTransmission.equals("Transmission")) {
                rBtntransmission.setChecked(true); //to check
            }
        }

        if (strDenting_Painting == null) {

        } else {
            if (strDenting_Painting.equals("Denting_Painting")) {
                rBtnDentingPanting.setChecked(true); //to check
            }
        }
        if (strMultiple == null) {

        } else {
            if (strMultiple.equals("Multiple")) {
                rBtnmultiple.setChecked(true); //to check
            }
        }
        if (strReborer == null) {

        } else {
            if (strReborer.equals("Reborer")) {
                rBtnreborer.setChecked(true); //to check
            }
        }
        return null;
    }

    public void date_time() {
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        String mStringMonth = "" + (month + 1);
        if ((month + 1) < 10) {
            mStringMonth = "0" + (month + 1);
        }

        String mStringDay = "" + day;
        if (day < 10) {
            mStringDay = "0" + day;
        }
        mEditTextregFillDate.setText(mStringDay + "-" + mStringMonth + "-" + year);
    }


    public class CustomOKDialog extends Dialog implements
            android.view.View.OnClickListener {

        public Context c;
        public Dialog d;
        public Button ok;
        String msg, heading;
        TextView txtMsg, txtHeading;
        String type;
        FragmentManager fm;

        public CustomOKDialog(Context a, String msg, String heading, String type) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.msg = msg;
            this.heading = heading;
            this.type = type;
            this.fm = fm;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog_login_invalid);
            ok = (Button) findViewById(R.id.btn_Ok);
            txtMsg = (TextView) findViewById(R.id.txtMsg);
            txtHeading = (TextView) findViewById(R.id.txtHeading);
            txtMsg.setText("" + msg);
            txtHeading.setText("" + heading);
            ok.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_Ok:
                    // getActivity().getSupportFragmentManager().beginTransaction().remove(get).commit();
                    getActivity().getSupportFragmentManager().popBackStack();

                    break;
                default:
                    break;
            }
            dismiss();
        }
    }
}