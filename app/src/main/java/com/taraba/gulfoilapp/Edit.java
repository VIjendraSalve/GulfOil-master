package com.taraba.gulfoilapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.model.RegistrationDetail;

import java.util.ArrayList;

/**
 * Created by android on 7/16/16.
 */
public class Edit extends Fragment {
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
    RadioButton rBtn_male, rBtn_female;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit, container, false);
        init(view);
        return view;
    }

    private void init(View view) {


    }
}
