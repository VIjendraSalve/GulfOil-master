package com.taraba.gulfoilapp.model;

/**
 * Created by android on 7/5/16.
 */
public class RegistrationDetails {
    /*
      "participant_id_pk": "1",//retailer_id
          "retailer_code": "GRGRC1",
          "login_id_fk": "6",
          "workshop_name": "Grass Roots",//shop_name
          "workshop_address": "Lower parel",//shop_address_line_1
          "workshop_address2": "East",//shop_street_address
          "full_name": "Nirav",//name_of_retailer_proprietor
          "email_id": "nirav.shah@grg.com",//email
          "mobile_no": "9324259919",//mob_no
          "alternate_mobile_no": "",//alternate_mob_no
          "landline_no": "",//shop_landline
          "anniversary_date": "0000-00-00",//marriege_date
          "state": "Maharshtra",//shop_state
          "district": "Mumbai",//shop_district
          "city": "Mumbai",//shop_city
          "sub_district": "Mumbai",//shop_subdistrict
          "dob": "0000-00-00",//date_of_birth
          "pincode": "400001",//shop_pincode

          "country": "",

          "landmark": "",//shop_landmark
          "classification": "Lube Oil Shop",
          "category": "DEO",
          "type": "Retail Outlet",
          "spouse_name": "",//spouse_name
          "no_of_children": "0",//no_of_children
          "participant_gender": "male",//retailer_gender
          "residential_address": "",//residential_address_line1
          "residential_address2": "",//residential_address_line2
          "residential_landmark": "",//residential_landmark
          "residential_pincode": "",//residential_pincode
          "residential_city": "",//residential_city
          "residential_state": "",//residential_state
          "communication_consent": "yes",//communication_consent
          "lucky_draw_consent": "yes",//luky_draw_consent
          "lucky_draw_a1": "",//lucky_draw_answer1
          "lucky_draw_a2": "",//lucky_draw_answer2
          "status": "pending verification"
     */
    int retailer_id;
    String name_of_retailer_proprietor;
    String shop_name;
    String shop_address_line_1;
    String shop_street_address;
    String shop_landmark;
    String shop_pincode;
    String shop_subdistrict;
    String shop_city;
    String shop_district;
    String shop_state;
    String mob_no;
    String alternate_mob_no;
    String shop_landline;
    String email;
    String residential_address_line1;
    String residential_address_line2;
    String residential_landmark;
    String residential_pincode;
    String residential_city;
    String residential_state;
    String date_of_birth;
    String marriege_date;
    String spouse_name;
    String retailer_gender;
    String no_of_children;
    String luky_draw_consent;
    String lucky_draw_question1;
    String lucky_draw_answer1;
    String lucky_draw_question2;
    String lucky_draw_answer2;
    String communication_consent;
    String distributor_id;

    public RegistrationDetails() {
    }

    public RegistrationDetails(String name_of_retailer_proprietor) {
        this.name_of_retailer_proprietor = name_of_retailer_proprietor;
    }

    public RegistrationDetails(int retailer_id, String name_of_retailer_proprietor, String shop_name, String shop_address_line_1, String shop_street_address, String shop_landmark, String shop_pincode, String shop_subdistrict, String shop_city, String shop_district, String shop_state, String mob_no, String alternate_mob_no, String shop_landline, String email, String residential_address_line1, String residential_address_line2, String residential_landmark, String residential_pincode, String residential_city, String residential_state, String date_of_birth, String marriege_date, String spouse_name, String retailer_gender, String no_of_children, String luky_draw_consent, String lucky_draw_question1, String lucky_draw_answer1, String lucky_draw_question2, String lucky_draw_answer2,
                               String communication_consent, String distributor_id, String distributor_name) {
        this.retailer_id = retailer_id;
        this.name_of_retailer_proprietor = name_of_retailer_proprietor;
        this.shop_name = shop_name;
        this.shop_address_line_1 = shop_address_line_1;
        this.shop_street_address = shop_street_address;
        this.shop_landmark = shop_landmark;
        this.shop_pincode = shop_pincode;
        this.shop_subdistrict = shop_subdistrict;
        this.shop_city = shop_city;
        this.shop_district = shop_district;
        this.shop_state = shop_state;
        this.mob_no = mob_no;
        this.alternate_mob_no = alternate_mob_no;
        this.shop_landline = shop_landline;
        this.email = email;
        this.residential_address_line1 = residential_address_line1;
        this.residential_address_line2 = residential_address_line2;
        this.residential_landmark = residential_landmark;
        this.residential_pincode = residential_pincode;
        this.residential_city = residential_city;
        this.residential_state = residential_state;
        this.date_of_birth = date_of_birth;
        this.marriege_date = marriege_date;
        this.spouse_name = spouse_name;
        this.retailer_gender = retailer_gender;
        this.no_of_children = no_of_children;
        this.luky_draw_consent = luky_draw_consent;
        this.lucky_draw_question1 = lucky_draw_question1;
        this.lucky_draw_answer1 = lucky_draw_answer1;
        this.lucky_draw_question2 = lucky_draw_question2;
        this.lucky_draw_answer2 = lucky_draw_answer2;
        this.communication_consent = communication_consent;
        this.distributor_id = distributor_id;
        this.distributor_name = distributor_name;
    }

    public int getRetailer_id() {
        return retailer_id;
    }

    public void setRetailer_id(int retailer_id) {
        this.retailer_id = retailer_id;
    }

    public String getName_of_retailer_proprietor() {
        return name_of_retailer_proprietor;
    }

    public void setName_of_retailer_proprietor(String name_of_retailer_proprietor) {
        this.name_of_retailer_proprietor = name_of_retailer_proprietor;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_address_line_1() {
        return shop_address_line_1;
    }

    public void setShop_address_line_1(String shop_address_line_1) {
        this.shop_address_line_1 = shop_address_line_1;
    }

    public String getShop_street_address() {
        return shop_street_address;
    }

    public void setShop_street_address(String shop_street_address) {
        this.shop_street_address = shop_street_address;
    }

    public String getShop_landmark() {
        return shop_landmark;
    }

    public void setShop_landmark(String shop_landmark) {
        this.shop_landmark = shop_landmark;
    }

    public String getShop_pincode() {
        return shop_pincode;
    }

    public void setShop_pincode(String shop_pincode) {
        this.shop_pincode = shop_pincode;
    }

    public String getShop_subdistrict() {
        return shop_subdistrict;
    }

    public void setShop_subdistrict(String shop_subdistrict) {
        this.shop_subdistrict = shop_subdistrict;
    }

    public String getShop_city() {
        return shop_city;
    }

    public void setShop_city(String shop_city) {
        this.shop_city = shop_city;
    }

    public String getShop_district() {
        return shop_district;
    }

    public void setShop_district(String shop_district) {
        this.shop_district = shop_district;
    }

    public String getShop_state() {
        return shop_state;
    }

    public void setShop_state(String shop_state) {
        this.shop_state = shop_state;
    }

    public String getMob_no() {
        return mob_no;
    }

    public void setMob_no(String mob_no) {
        this.mob_no = mob_no;
    }

    public String getAlternate_mob_no() {
        return alternate_mob_no;
    }

    public void setAlternate_mob_no(String alternate_mob_no) {
        this.alternate_mob_no = alternate_mob_no;
    }

    public String getShop_landline() {
        return shop_landline;
    }

    public void setShop_landline(String shop_landline) {
        this.shop_landline = shop_landline;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResidential_address_line1() {
        return residential_address_line1;
    }

    public void setResidential_address_line1(String residential_address_line1) {
        this.residential_address_line1 = residential_address_line1;
    }

    public String getResidential_address_line2() {
        return residential_address_line2;
    }

    public void setResidential_address_line2(String residential_address_line2) {
        this.residential_address_line2 = residential_address_line2;
    }

    public String getResidential_landmark() {
        return residential_landmark;
    }

    public void setResidential_landmark(String residential_landmark) {
        this.residential_landmark = residential_landmark;
    }

    public String getResidential_pincode() {
        return residential_pincode;
    }

    public void setResidential_pincode(String residential_pincode) {
        this.residential_pincode = residential_pincode;
    }

    public String getResidential_city() {
        return residential_city;
    }

    public void setResidential_city(String residential_city) {
        this.residential_city = residential_city;
    }

    public String getResidential_state() {
        return residential_state;
    }

    public void setResidential_state(String residential_state) {
        this.residential_state = residential_state;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getMarriege_date() {
        return marriege_date;
    }

    public void setMarriege_date(String marriege_date) {
        this.marriege_date = marriege_date;
    }

    public String getSpouse_name() {
        return spouse_name;
    }

    public void setSpouse_name(String spouse_name) {
        this.spouse_name = spouse_name;
    }

    public String getRetailer_gender() {
        return retailer_gender;
    }

    public void setRetailer_gender(String retailer_gender) {
        this.retailer_gender = retailer_gender;
    }

    public String getNo_of_children() {
        return no_of_children;
    }

    public void setNo_of_children(String no_of_children) {
        this.no_of_children = no_of_children;
    }

    public String getLuky_draw_consent() {
        return luky_draw_consent;
    }

    public void setLuky_draw_consent(String luky_draw_consent) {
        this.luky_draw_consent = luky_draw_consent;
    }

    public String getLucky_draw_question1() {
        return lucky_draw_question1;
    }

    public void setLucky_draw_question1(String lucky_draw_question1) {
        this.lucky_draw_question1 = lucky_draw_question1;
    }

    public String getLucky_draw_answer1() {
        return lucky_draw_answer1;
    }

    public void setLucky_draw_answer1(String lucky_draw_answer1) {
        this.lucky_draw_answer1 = lucky_draw_answer1;
    }

    public String getLucky_draw_question2() {
        return lucky_draw_question2;
    }

    public void setLucky_draw_question2(String lucky_draw_question2) {
        this.lucky_draw_question2 = lucky_draw_question2;
    }

    public String getLucky_draw_answer2() {
        return lucky_draw_answer2;
    }

    public void setLucky_draw_answer2(String lucky_draw_answer2) {
        this.lucky_draw_answer2 = lucky_draw_answer2;
    }

    public String getCommunication_consent() {
        return communication_consent;
    }

    public void setCommunication_consent(String communication_consent) {
        this.communication_consent = communication_consent;
    }

    public String getDistributor_id() {
        return distributor_id;
    }

    public void setDistributor_id(String distributor_id) {
        this.distributor_id = distributor_id;
    }

    public String getDistributor_name() {
        return distributor_name;
    }

    public void setDistributor_name(String distributor_name) {
        this.distributor_name = distributor_name;
    }

    String distributor_name;
}
