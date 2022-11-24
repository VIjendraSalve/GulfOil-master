package com.taraba.gulfoilapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.taraba.gulfoilapp.adapter.CircularListAdapter;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.model.RegistrationDetail;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Mansi on 4/16/16.
 * Modified by Mansi
 */
public class EditRegistrationRetailer1 extends Fragment implements View.OnClickListener {
    private EditText edt_m_spouseName, edt_m_luckydrawQuestion2, edt_m_distributorName,
            edt_m_distributorId, edt_m_luckydrawquestion1, edt_m_shopLandmark, edt_m_shopPincode,
            edt_m_shopSubdistrict, edt_m_shopdistrict, edt_dateofanniversary, edt_m_shopstreetaddress,
            edt_m_shopaddressline, edt_mobile_number_first, edt_m_firstname, edt_m_shopname,
            edt_m_mobilenumber, edt_m_shopState, edt_m_alternatemobilenumber,
            edt_m_residentialCityName, edt_m_residentialAddressState, edt_dateofbirth,
            edt_m_residentialLandmark, edt_m_residentialPincode,
            edt_m_residentialAddressline2, edt_m_shopLandlinenumber, edt_m_residentialAddressline1,
            edt_m_email, edt_m_shopTownName;
    private RadioGroup myRadioGroupGender, myRadioGroupLuckydrawanswer1, myRadioGroupLuckydrawanswer2;
    private TextView lblShopName, lblShopStreetAddress, lblShopAddressLine, lblFirstName, lblDateOfAnniversary,
            lblSpouseName, lblShopLandmark, lblResidentialAddressState, lblShopSubdistrict,
            lblLuckyDrawQuestion2, lblLuckyDrawQuestion1, lblspn_state, lblDistributorId, lblDistributorName,
            lblResidentialCityName, lblShopTownName, txtluckydrawanswer2, lblShopPincode, lblDateOfBirth,
            lblResidentialPincode, lblShopState, lblMobileNumber, txtluckydrawanswer1, txtmechanicregistration,
            txtgendet, txt_mechanic_code, lblAlternateMobileNumber, txthead, lblshopLandlinenumber,
            lblResidentialAddressline2, tv_retailerId1,
            lblResidentialLandmark, lblResidentialAddressline1, lblEmail;
    String selectedGender, selectedluckyDrawAnswer1, selectedluckyDrawAnswer2;
    public static String date, gotolist;
    RadioButton rBtn_male, rBtn_female, rBtn_answer1, rBtn_answer2, rBtn_answer3, rBtn_answer4, rBtn_answer, rBtn_answer11, rBtn_answer12, rBtn_answer13;
    Spinner spn_children;
    Button btn_update;
    private CheckBox rBtn_communication, rBtn_luckydrawconsent;
    private ArrayList<RegistrationDetail> retailerList1;
    int position;


    String participant_id;
    String retailer_code;
    String login_id_fk;
    String workshop_name;
    String workshop_address;
    String workshop_address2;
    String first_name;
    String email_id;
    String mobile_no;
    String alternate_mobile_no;
    String landline_no;
    String anniversary_date;
    String state;
    String district;
    String city;
    String sub_district;
    String dob;
    String category;
    String type;
    String spouse_name;
    String participant_gender;
    String residential_address;
    String residential_address2;
    String residential_landmark;
    String residential_pincode;
    String residential_city;
    String residential_state;
    String communication_consent;
    String lucky_draw_consent;
    String lucky_draw_a1;
    String lucky_draw_a2;
    String pincode;
    String country;
    String landmark;
    String classification;
    String db_name;
    String db_code;
    String strChildren;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit, container, false);
        init(view);
        position = RetailerRegistrationFragment.position1;
        Log.e("list position", "" + position);
        // new RetailerDetails().execute();
        // edt_m_firstname.setText("" + retailerList1.get(position).getFull_name());


        //edt_m_firstname.setText("" + RetailerRegistrationFragment.retailerList.get(position).getFull_name());
        edt_m_firstname.setText("" + MechanicalSearchFragment.circularList.get(0).getUserfname());

        edt_m_shopname.setText("" + MechanicalSearchFragment.circularList.get(0).getWorkshopname());
        edt_m_shopaddressline.setText("" + MechanicalSearchFragment.circularList.get(0).getShopadd());


        edt_m_shopstreetaddress.setText("" + MechanicalSearchFragment.circularList.get(0).getShopadd());
        edt_m_shopLandmark.setText("" + MechanicalSearchFragment.circularList.get(0).getLandmark());
        edt_m_shopPincode.setText("" + MechanicalSearchFragment.circularList.get(0).getPincode());
        edt_m_shopSubdistrict.setText("" + MechanicalSearchFragment.circularList.get(0).getSubdistrict());
        edt_m_shopTownName.setText("" + MechanicalSearchFragment.circularList.get(0).getCity());
        edt_m_shopdistrict.setText("" + MechanicalSearchFragment.circularList.get(0).getDistrict());
        edt_m_shopState.setText("" + MechanicalSearchFragment.circularList.get(0).getState());
        edt_m_mobilenumber.setText("" + MechanicalSearchFragment.circularList.get(0).getMobile_no());
        edt_m_alternatemobilenumber.setText("" + MechanicalSearchFragment.circularList.get(0).getAlternate_mobile_no());
        edt_m_shopLandlinenumber.setText("" + MechanicalSearchFragment.circularList.get(0).getLandline());
        edt_m_email.setText("" + MechanicalSearchFragment.circularList.get(0).getEmail());
        edt_m_residentialAddressline1.setText("" + MechanicalSearchFragment.circularList.get(0).getResidential_address());
        edt_m_residentialAddressline2.setText("" + MechanicalSearchFragment.circularList.get(0).getResidential_address2());
        edt_m_residentialLandmark.setText("" + MechanicalSearchFragment.circularList.get(0).getResidentialLandmark());
        edt_m_residentialPincode.setText("" + MechanicalSearchFragment.circularList.get(0).getResidentialPincode());
        edt_m_residentialCityName.setText("" + MechanicalSearchFragment.circularList.get(0).getResidentialCityName());
        edt_m_residentialAddressState.setText("" + MechanicalSearchFragment.circularList.get(0).getResidentialState());
        edt_dateofbirth.setText("" + MechanicalSearchFragment.circularList.get(0).getDob());
        edt_dateofanniversary.setText("" + MechanicalSearchFragment.circularList.get(0).getDateofanniversary());
        edt_m_spouseName.setText("" + MechanicalSearchFragment.circularList.get(0).getSpouse());
        tv_retailerId1.setText("" + MechanicalSearchFragment.circularList.get(0).getParticipant_code());
        edt_m_distributorId.setText("" + MechanicalSearchFragment.circularList.get(0).getDb_code());
        edt_m_distributorName.setText("" + MechanicalSearchFragment.circularList.get(0).getDb_name());
        if (!CircularListAdapter.hideRegButton.equals("")) {
            btn_update.setVisibility(View.GONE);
        }
       /* int radioButtonId = myRadioGroupGender.getCheckedRadioButtonId();
        String selectedValue;
        switch (radioButtonId)
        {
            case R.id.rBtn_male:
                rBtn_male.setChecked(true);
                break;
            case R.id.rBtn_female:
                rBtn_female.setChecked(true);
                break;
        }*/
        Log.e("gender reg", "" + MechanicalSearchFragment.circularList.get(0).getGender());
        if (MechanicalSearchFragment.circularList.get(0).getGender().equalsIgnoreCase("male")) {
            rBtn_male.setChecked(true);
        } else if (MechanicalSearchFragment.circularList.get(0).getGender().equalsIgnoreCase("female")) {
            rBtn_female.setChecked(true);
        } else {
            rBtn_male.setChecked(true);
        }

        if (MechanicalSearchFragment.circularList.get(0).getLuckydraw_a1().equalsIgnoreCase(" Multi G+ ")) {
            rBtn_answer1.setChecked(true);
        } else if (MechanicalSearchFragment.circularList.get(0).getLuckydraw_a1().equalsIgnoreCase("Multi G Max")) {
            rBtn_answer2.setChecked(true);
        } else if (MechanicalSearchFragment.circularList.get(0).getLuckydraw_a1().equalsIgnoreCase("Gulf MAX Supreme")) {
            rBtn_answer3.setChecked(true);
        } else if (MechanicalSearchFragment.circularList.get(0).getLuckydraw_a1().equalsIgnoreCase("Gulf Formula GX")) {
            rBtn_answer4.setChecked(true);
        } else {
            rBtn_answer1.setChecked(true);
        }


        if (MechanicalSearchFragment.circularList.get(0).getLuckydraw_a2().equalsIgnoreCase(" Gulf Supreme Duty LE ")) {
            rBtn_answer.setChecked(true);
        } else if (MechanicalSearchFragment.circularList.get(0).getLuckydraw_a2().equalsIgnoreCase("Gulf Cargo Power")) {
            rBtn_answer11.setChecked(true);
        } else if (MechanicalSearchFragment.circularList.get(0).getLuckydraw_a2().equalsIgnoreCase(" Gulf Superfleet Turbo")) {
            rBtn_answer12.setChecked(true);
        } else if (MechanicalSearchFragment.circularList.get(0).getLuckydraw_a2().equalsIgnoreCase("Gulf Superfleet LE Dura Max")) {
            rBtn_answer13.setChecked(true);
        } else {
            rBtn_answer.setChecked(true);
        }


        if (MechanicalSearchFragment.circularList.get(0).getNoofchildren().equals("0")) {
            spn_children.setSelection(0);
        } else if (MechanicalSearchFragment.circularList.get(0).getNoofchildren().equals("1")) {
            spn_children.setSelection(1);
        } else if (MechanicalSearchFragment.circularList.get(0).getNoofchildren().equals("2")) {
            spn_children.setSelection(2);
        } else if (MechanicalSearchFragment.circularList.get(0).getNoofchildren().equals("3")) {
            spn_children.setSelection(3);
        } else if (MechanicalSearchFragment.circularList.get(0).getNoofchildren().equals("4")) {
            spn_children.setSelection(4);
        } else if (MechanicalSearchFragment.circularList.get(0).getNoofchildren().equals("5")) {
            spn_children.setSelection(5);
        }

        if (MechanicalSearchFragment.circularList.get(0).getLuckydraw_a1().equalsIgnoreCase("option1")) {
            rBtn_answer1.setChecked(true);
        } else if (MechanicalSearchFragment.circularList.get(0).getLuckydraw_a1().equalsIgnoreCase("option2")) {
            rBtn_answer2.setChecked(true);
        }
        /*
        if(RetailerRegistrationFragment.retailerList.get(position).getCommunication_consent().equals("yes")){

            rBtn_communication.setChecked(true);
        }

        if(RetailerRegistrationFragment.retailerList.get(position).getLucky_draw_consent().equals("yes")){
            rBtn_luckydrawconsent.setChecked(true);
        }
      */  //spn_children.set;
        // if(RetailerRegistrationFragment.retailerList.get(position).getLucky_draw_a1().equals(""))

        edt_dateofbirth.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @SuppressWarnings("deprecation")
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP)

                    showDatePickerDOB();
                return true;

            }
        });
        edt_dateofanniversary.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @SuppressWarnings("deprecation")
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP)

                    showDatedPickerDOB();
                return true;
            }
        });
        settingStarsForLabels(view);

        //  new RetailerDetails().execute();
       /* int selectId1 = myRadioGroupGender.getCheckedRadioButtonId();
        RadioButton radioButton1 = (RadioButton) view.findViewById(selectId1);
        selectedGender = radioButton1.getText().toString();*/

   /* int selectId1 = myRadioGroupGender.getCheckedRadioButtonId();
    RadioButton radioButton1 = (RadioButton) view.findViewById(selectId1);
    selectedGender = radioButton1.getText().toString();

    int selectId3 = myRadioGroupLuckydrawanswer2.getCheckedRadioButtonId();
    RadioButton radioButton3 = (RadioButton) view.findViewById(selectId3);
    selectedluckyDrawAnswer2 = radioButton3.getText().toString();*/


        return view;
    }

    private void showDatedPickerDOB() {
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
/*
            edt_dateofanniversary.setText(mStringDay + "-" + mStringMonth + "-"
                    + year);
*/
            edt_dateofanniversary.setText(year + "-" + mStringMonth + "-"
                    + mStringDay);

           /* mEditTextDateOfBirth.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));*/
        }
    };
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
           /* edt_dateofbirth.setText(mStringDay + "-" + mStringMonth + "-"
                    + year);
*/
            edt_dateofbirth.setText(year + "-" + mStringMonth + "-"
                    + mStringDay);

           /* mEditTextDateOfBirth.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));*/
        }
    };

    private void settingStarsForLabels(View view) {
        setStarForLabel((TextView) view.findViewById(R.id.lblFirstName));
        setStarForLabel((TextView) view.findViewById(R.id.lblShopName));
        setStarForLabel((TextView) view.findViewById(R.id.lblDistrict));
        setStarForLabel((TextView) view.findViewById(R.id.lblShopAddressLine));
        setStarForLabel((TextView) view.findViewById(R.id.lblShopStreetAddress));
        setStarForLabel((TextView) view.findViewById(R.id.lblShopPincode));
        setStarForLabel((TextView) view.findViewById(R.id.lblShopTownName));
        setStarForLabel((TextView) view.findViewById(R.id.lblShopState));
        setStarForLabel((TextView) view.findViewById(R.id.lblMobileNumber));
        setStarForLabel((TextView) view.findViewById(R.id.lblResidentialAddressline1));
        setStarForLabel((TextView) view.findViewById(R.id.lblResidentialAddressline2));
        setStarForLabel((TextView) view.findViewById(R.id.lblResidentialPincode));
        setStarForLabel((TextView) view.findViewById(R.id.lblResidentialCityName));
        setStarForLabel((TextView) view.findViewById(R.id.lblResidentialAddressState));
        setStarForLabel((TextView) view.findViewById(R.id.lblDateOfBirth));
        setStarForLabel((TextView) view.findViewById(R.id.txtgendet));
        //  setStarForLabel((TextView) view.findViewById(R.id.lblspn_children));
        setStarForLabel((TextView) view.findViewById(R.id.rBtn_luckydrawconsent));
        setStarForLabel((TextView) view.findViewById(R.id.lblLuckyDrawQuestion1));
        setStarForLabel((TextView) view.findViewById(R.id.txtluckydrawanswer1));
        setStarForLabel((TextView) view.findViewById(R.id.lblLuckyDrawQuestion2));
        setStarForLabel((TextView) view.findViewById(R.id.txtluckydrawanswer2));
        setStarForLabel((TextView) view.findViewById(R.id.rBtn_communication));
        setStarForLabel((TextView) view.findViewById(R.id.lblDistributorId));
        setStarForLabel((TextView) view.findViewById(R.id.lblDistributorName));


    }

    private void setStarForLabel(TextView txt) {
        txt.setText(Html.fromHtml(txt.getText() + "<font color='#EE0000'> * </font>"));
    }

    private void init(View view) {
        spn_children = (Spinner) view.findViewById(R.id.spn_children);
        edt_m_spouseName = (EditText) view.findViewById(R.id.edt_m_spouseName);
        edt_m_luckydrawQuestion2 = (EditText) view.findViewById(R.id.edt_m_luckydrawQuestion2);
        edt_m_distributorName = (EditText) view.findViewById(R.id.edt_m_distributorName);
        edt_m_distributorId = (EditText) view.findViewById(R.id.edt_m_distributorId);
        edt_m_luckydrawquestion1 = (EditText) view.findViewById(R.id.edt_m_luckydrawquestion1);
        edt_m_shopLandmark = (EditText) view.findViewById(R.id.edt_m_shopLandmark);
        edt_m_shopPincode = (EditText) view.findViewById(R.id.edt_m_shopPincode);
        edt_m_shopSubdistrict = (EditText) view.findViewById(R.id.edt_m_shopSubdistrict);
        edt_m_shopdistrict = (EditText) view.findViewById(R.id.edt_m_shopdistrict);
        edt_dateofanniversary = (EditText) view.findViewById(R.id.edt_dateofanniversary);
        edt_m_shopstreetaddress = (EditText) view.findViewById(R.id.edt_m_shopstreetaddress);
        edt_m_shopaddressline = (EditText) view.findViewById(R.id.edt_m_shopaddressline);
        edt_mobile_number_first = (EditText) view.findViewById(R.id.edt_mobile_number_first);
        edt_m_firstname = (EditText) view.findViewById(R.id.edt_m_firstname);
        edt_m_shopname = (EditText) view.findViewById(R.id.edt_m_shopname);
        edt_m_mobilenumber = (EditText) view.findViewById(R.id.edt_m_mobilenumber);
        edt_m_shopState = (EditText) view.findViewById(R.id.edt_m_shopState);
        edt_m_alternatemobilenumber = (EditText) view.findViewById(R.id.edt_m_alternatemobilenumber);
        edt_m_residentialCityName = (EditText) view.findViewById(R.id.edt_m_residentialCityName);
        edt_m_residentialAddressState = (EditText) view.findViewById(R.id.edt_m_residentialAddressState);
        edt_dateofbirth = (EditText) view.findViewById(R.id.edt_dateofbirth);
        edt_m_residentialLandmark = (EditText) view.findViewById(R.id.edt_m_residentialLandmark);
        edt_m_residentialPincode = (EditText) view.findViewById(R.id.edt_m_residentialPincode);
        edt_m_residentialAddressline2 = (EditText) view.findViewById(R.id.edt_m_residentialAddressline2);
        edt_m_shopLandlinenumber = (EditText) view.findViewById(R.id.edt_m_shopLandlinenumber);
        edt_m_residentialAddressline1 = (EditText) view.findViewById(R.id.edt_m_residentialAddressline1);
        edt_m_email = (EditText) view.findViewById(R.id.edt_m_email);
        edt_m_shopTownName = (EditText) view.findViewById(R.id.edt_m_shopTownName);
        tv_retailerId1 = (TextView) view.findViewById(R.id.retailerId1);

        rBtn_communication = (CheckBox) view.findViewById(R.id.rBtn_communication);
        rBtn_luckydrawconsent = (CheckBox) view.findViewById(R.id.rBtn_luckydrawconsent);

        myRadioGroupGender = (RadioGroup) view.findViewById(R.id.myRadioGroupGender);
        myRadioGroupLuckydrawanswer1 = (RadioGroup) view.findViewById(R.id.myRadioGroupLuckydrawanswer1);


        btn_update = (Button) view.findViewById(R.id.btn_update);

        rBtn_male = (RadioButton) view.findViewById(R.id.rBtn_male);
        rBtn_female = (RadioButton) view.findViewById(R.id.rBtn_female);
        rBtn_answer2 = (RadioButton) view.findViewById(R.id.rBtn_answer2);
        rBtn_answer1 = (RadioButton) view.findViewById(R.id.rBtn_answer1);
        rBtn_answer3 = (RadioButton) view.findViewById(R.id.rBtn_answer3);
        rBtn_answer4 = (RadioButton) view.findViewById(R.id.rBtn_answer4);

        rBtn_answer = (RadioButton) view.findViewById(R.id.rBtn_answer);
        rBtn_answer11 = (RadioButton) view.findViewById(R.id.rBtn_answer11);
        rBtn_answer12 = (RadioButton) view.findViewById(R.id.rBtn_answer12);
        rBtn_answer13 = (RadioButton) view.findViewById(R.id.rBtn_answer13);


        btn_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String str_mob = edt_m_mobilenumber.getText().toString();
        if (v.getId() == R.id.btn_update) {


            if (edt_m_firstname.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Name of Retailer Proprietor", "Retailer Registration");
                cod.show();
                edt_m_firstname.requestFocus();
            } else if (edt_m_shopname.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Shop State", "Retailer Registration");
                cod.show();
                edt_m_shopname.requestFocus();
            } else if (edt_m_shopaddressline.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Shop Address Line 1", "Retailer Registration");
                cod.show();
                edt_m_shopaddressline.requestFocus();
            } else if (edt_m_shopstreetaddress.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Shop Street Address", "Retailer Registration");
                cod.show();
                edt_m_shopstreetaddress.requestFocus();
            } else if (edt_m_shopPincode.getText().toString().equals("") || edt_m_shopPincode.getText().toString().length() < 6) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter valid Shop Pincode", "Retailer Registration");
                cod.show();
                edt_m_shopPincode.requestFocus();
            } else if (edt_m_shopTownName.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Shop Town Name", "Retailer Registration");
                cod.show();
                edt_m_shopTownName.requestFocus();
            } else if (edt_m_shopdistrict.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Shop District", "Retailer Registration");
                cod.show();

                edt_m_shopdistrict.requestFocus();
            } else if (edt_m_shopState.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Shop State", "Retailer Registration");
                cod.show();
                edt_m_shopState.requestFocus();
            } else if (edt_m_mobilenumber.getText().toString().equals("") || str_mob.length() < 10) {
                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Invalid mobile number", "Retailer Registration");
                cod.show();

                edt_m_mobilenumber.requestFocus();
            } else if (!edt_m_email.getText().toString().equals("") && !edt_m_email.getText().toString().trim().matches(emailPattern)) {
                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Invalid email address", "Retailer Registration");
                cod.show();

                edt_m_email.requestFocus();
            } else if (edt_m_residentialAddressline1.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Residential Address Line 1", "Retailer Registration");
                cod.show();
                edt_m_residentialAddressline1.requestFocus();
            } else if (edt_m_residentialAddressline2.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Residential Address Line 2", "Retailer Registration");
                cod.show();
                edt_m_residentialAddressline2.requestFocus();
            } else if (edt_m_residentialPincode.getText().toString().equals("") || edt_m_residentialPincode.getText().toString().length() < 6) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter valid Residential Pincode", "Retailer Registration");
                cod.show();
                edt_m_residentialPincode.requestFocus();
            } else if (edt_m_residentialCityName.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Residential City Name", "Retailer Registration");
                cod.show();
                edt_m_residentialCityName.requestFocus();
            } else if (edt_m_residentialAddressState.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Residential Address State", "Retailer Registration");
                cod.show();
                edt_m_residentialAddressState.requestFocus();
            } else if (edt_dateofbirth.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Date of Birth", "Retailer Registration");
                cod.show();
                edt_dateofbirth.requestFocus();
            } else if (rBtn_communication.isChecked() == false) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Communication Consent mandatory for submission", "Retailer Registration");
                cod.show();
                edt_dateofbirth.requestFocus();
            }

           /* else if(edt_m_luckydrawquestion1.getText().toString().equals("")){

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Lucky draw Question 1", "Retailer Registration");
                cod.show();
                edt_m_luckydrawquestion1.requestFocus();
            }
            else if(edt_m_luckydrawQuestion2.getText().toString().equals("")){

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Lucky draw Question 2", "Retailer Registration");
                cod.show();
                edt_m_luckydrawQuestion2.requestFocus();
            }*/
            else if (rBtn_luckydrawconsent.isChecked() == false) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Lucky draw Consent mandatory for submission", "Retailer Registration");
                cod.show();
            } else if (edt_m_distributorId.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Distributor ID", "Retailer Registration");
                cod.show();
                edt_m_distributorId.requestFocus();
            } else if (edt_m_distributorName.getText().toString().equals("")) {

                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "Enter Distributor Name", "Retailer Registration");
                cod.show();
                edt_m_distributorName.requestFocus();
            } else {

                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    strChildren = spn_children.getSelectedItem().toString();
                    Log.d("Children", "Children: " + strChildren);
                    new UpdateRetailerDetails().execute();
                } else {
                    Toast.makeText(getActivity(),
                            "Internet Disconnected", Toast.LENGTH_LONG).show();
                }

            }
          /*  ConnectionDetector cd= new ConnectionDetector(
                    getActivity());
            if(cd.isConnectingToInternet()){
                new UpdateRetailerDetails().execute();
            }
            else {
                Toast.makeText(getActivity(),
                        "Internet Disconnected", Toast.LENGTH_LONG).show();
            }
*/
        }
    }

    private class UpdateRetailerDetails extends AsyncTask<String[], Void, JSONObject> {
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
            Log.e("result", "" + params);
            try {

                //  jObj = new UserFunctions().updateRetailer();

                SharedPreferences preferences1 = getActivity().getSharedPreferences("signupdetails",
                        getActivity().MODE_PRIVATE);
                String fls_login_id = preferences1.getString("usertrno", "");

                getData();

                jObj = new UserFunctions().updateRetailer("" + fls_login_id, "" + participant_id, "" + retailer_code,
                        "" + login_id_fk, "" + workshop_name, "" + workshop_address, "" + workshop_address2, "" + first_name,
                        "" + email_id, "" + mobile_no, "" + alternate_mobile_no, "" + landline_no, "" + anniversary_date, "" + state,
                        "" + district, "" + city, "" + sub_district, "" + dob, "" + pincode, "" + country, "" + landmark, "" + classification, "" + category, "" + type, "" + spouse_name, "" + strChildren, "" + participant_gender,
                        "" + residential_address, "" + residential_address2, "" + residential_landmark, "" + residential_pincode,
                        "" + residential_city, "" + residential_state, "" + communication_consent, "" + lucky_draw_consent,
                        "" + lucky_draw_a1, "" + lucky_draw_a2);

                Log.e("jobj", "" + jObj);
                Log.e("web_response_error", String.valueOf(jObj.getString("error")));
                Log.e("doinbackground", "doinbackground");
            } catch (Exception e) {
                // TODO: handle exception
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Network Error...", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
            } // end catch
            return jObj;

        }

        @Override
        protected void onPostExecute(final JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {

                String mStringStatus = jsonObject.getString("status");
                Log.e("web response staus", "" + mStringStatus);
                if (mStringStatus.equals("failure")) {
                    //  Log.e("web response staus",""+jsonObject.getString("status"));
                    Log.e("web response error", String.valueOf(jsonObject.getString("error")));
                    // String jsonObject1=jsonObject.getString("error");
                    // JSONObject jo=new JSONObject(jsonObject1);
                    //  jo.getString()
                    CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jsonObject.getString("error"), "Gulf Oil");
                    cdd.show();

                    progressDialog.dismiss();
                } else if (mStringStatus.equals("success")) {
                    gotolist = "ok";
                    CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jsonObject.getString("status"), "Gulf Oil");
                    cdd.show();

                    cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            try {


                                Fragment otpFragment = new Otp_fragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt("participant_login_id", Integer.parseInt(MechanicalSearchFragment.circularList.get(0).getParticipant_id_pk()));//Integer.parseInt(jsonObject.getString("participant_login_id")));
                                bundle.putString("participant_mob_no", "" + edt_m_mobilenumber.getText().toString());//jsonObject.getString("mobile_no"));
                                bundle.putString("participant_code", "" + MechanicalSearchFragment.circularList.get(0).getParticipant_code());//jsonObject.getString("retailer_code"));
                                otpFragment.setArguments(bundle);

                                SharedPreferences preferences = getActivity().getSharedPreferences("status", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("status", "");
                                editor.commit();
                                FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                                ft1.replace(R.id.container_body, otpFragment);
                                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                ft1.addToBackStack(null);
                                getFragmentManager().popBackStack();
                                ft1.commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("", "OTPP:" + e.toString());
                            }
                        }
                    });

                    Log.e("web response staus", "" + jsonObject.getString("status"));
                    progressDialog.dismiss();
                }

            } catch (Exception upe) {
                Log.e("error", "" + upe);
                progressDialog.dismiss();
            }


        }
    }


    /*
        private boolean isValidMobile(String edt_m_mobilenumber) {


            boolean check=false;
            if(!Pattern.matches("[a-zA-Z]+", text))
            {
                if(phone2.length() < 6 || phone2.length() > 13)
                {
                    check = false;
                    txtPhone.setError("Not Valid Number");
                }
                else
                {

                    check = true;
                }
            }
            else
            {
                check=false;
            }
            return check;
        }
    */
    public void getData() {
   /* participant_id=""+RetailerRegistrationFragment.retailerList.get(position).getParticipant_id_pk();
    retailer_code=""+RetailerRegistrationFragment.retailerList.get(position).getRetailer_code();
    login_id_fk=""+RetailerRegistrationFragment.retailerList.get(position).getLogin_id_fk();*/
        participant_id = "" + MechanicalSearchFragment.circularList.get(0).getParticipant_id_pk();
        retailer_code = "" + MechanicalSearchFragment.circularList.get(0).getParticipant_code();
        login_id_fk = "1";
        workshop_name = edt_m_shopname.getText().toString();
        workshop_address = edt_m_shopaddressline.getText().toString();
        workshop_address2 = "" + edt_m_shopstreetaddress.getText().toString();
        first_name = edt_m_firstname.getText().toString();
        email_id = edt_m_email.getText().toString();
        mobile_no = edt_m_mobilenumber.getText().toString();
        alternate_mobile_no = edt_m_alternatemobilenumber.getText().toString();
        landline_no = edt_m_shopLandlinenumber.getText().toString();
        anniversary_date = edt_dateofanniversary.getText().toString();
        state = edt_m_shopState.getText().toString();
        district = edt_m_shopdistrict.getText().toString();
        city = edt_m_residentialCityName.getText().toString();
        sub_district = edt_m_shopSubdistrict.getText().toString();
        dob = edt_dateofbirth.getText().toString();
    /*category=""+RetailerRegistrationFragment.retailerList.get(position).getCategory();
    type=""+RetailerRegistrationFragment.retailerList.get(position).getType();*/
        category = "category";
        type = "fls";
        spouse_name = edt_m_spouseName.getText().toString();

        participant_gender = "" + selectedGender;
        residential_address = edt_m_residentialAddressline1.getText().toString();
        residential_address2 = edt_m_residentialAddressline2.getText().toString();
        residential_landmark = edt_m_residentialLandmark.getText().toString();
        residential_pincode = edt_m_residentialPincode.getText().toString();
        residential_city = edt_m_residentialCityName.getText().toString();
        residential_state = edt_m_residentialAddressState.getText().toString();
        pincode = edt_m_shopPincode.getText().toString();
        ;
        //country=""+RetailerRegistrationFragment.retailerList.get(position).getCountry();
        landmark = edt_m_shopLandmark.getText().toString();
        ;
        //classification=""+RetailerRegistrationFragment.retailerList.get(position).getClassification();
        classification = "classification";

   /* Boolean ischecked_communication_consent=rBtn_communication.isChecked();
    if(ischecked_communication_consent)
        communication_consent="yes";
    else
        communication_consent="no";

    Boolean ischecked_lucky_draw_consent=rBtn_communication.isChecked();
    if(ischecked_lucky_draw_consent)
        lucky_draw_consent="yes";
    else
        lucky_draw_consent="no";

*/
        lucky_draw_a1 = "option1";
        lucky_draw_a2 = "option2";
   /* lucky_draw_a1=""+RetailerRegistrationFragment.retailerList.get(position).getLucky_draw_a1();
    lucky_draw_a2=""+RetailerRegistrationFragment.retailerList.get(position).getLucky_draw_a2();*/

    }
}

