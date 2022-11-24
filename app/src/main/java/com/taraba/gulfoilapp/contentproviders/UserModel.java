package com.taraba.gulfoilapp.contentproviders;

/**
 * Created by android3 on 12/25/15.
 */
public class UserModel {
    private int id;
    private String picture;
    private String userfname;
    private String userlname;
    private String gender;
    private String nomini;
    private String nominirely;
    private String mothername;
    private String workshopname;
    private String state, state_mapped;
    private String district;

    private String landline;
    private String residential_address;
    private String residential_address2;
    private String residentialLandmark;
    private String residentialPincode;

    private String residentialCityName;
    private String residentialState;
    private String dateofanniversary;
    private String spouse;
    private String db_name;
    private String db_code;


    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getResidential_address() {
        return residential_address;
    }

    public void setResidential_address(String residential_address) {
        this.residential_address = residential_address;
    }

    public String getResidential_address2() {
        return residential_address2;
    }

    public void setResidential_address2(String residential_address2) {
        this.residential_address2 = residential_address2;
    }

    public String getResidentialLandmark() {
        return residentialLandmark;
    }

    public void setResidentialLandmark(String residentialLandmark) {
        this.residentialLandmark = residentialLandmark;
    }

    public String getResidentialPincode() {
        return residentialPincode;
    }

    public void setResidentialPincode(String residentialPincode) {
        this.residentialPincode = residentialPincode;
    }

    public String getResidentialCityName() {
        return residentialCityName;
    }

    public void setResidentialCityName(String residentialCityName) {
        this.residentialCityName = residentialCityName;
    }

    public String getResidentialState() {
        return residentialState;
    }

    public void setResidentialState(String residentialState) {
        this.residentialState = residentialState;
    }

    public String getDateofanniversary() {
        return dateofanniversary;
    }

    public void setDateofanniversary(String dateofanniversary) {
        this.dateofanniversary = dateofanniversary;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public String getDb_code() {
        return db_code;
    }

    public void setDb_code(String db_code) {
        this.db_code = db_code;
    }



    /*
    .s
.se
.s
     */


    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    private String subdistrict;
    private String pincode;

    private String taluka;
    private String city;
    private String shopadd;
    private String landmark;
    private String mobile_no;

    public String getAlternate_mobile_no() {
        return alternate_mobile_no;
    }

    public void setAlternate_mobile_no(String alternate_mobile_no) {
        this.alternate_mobile_no = alternate_mobile_no;
    }

    private String alternate_mobile_no;
    private String email;
    private String sector;
    private String specialization;
    private String dob;
    private String regdate;

    private String toatalsperconpermonth;
    private String sperpartconformmvehicpermonth;
    private String mmgenuspareconpermonth;
    private String totalvehicalpermonth;
    private String mahindravehicalpermonth;
    private String noofmechanics;
    private String noofchildren;

    public String getNoofchildren() {
        return noofchildren;
    }

    public void setNoofchildren(String noofchildren) {
        this.noofchildren = noofchildren;
    }

    public String getLuckydraw_a1() {
        return luckydraw_a1;
    }

    public void setLuckydraw_a1(String luckydraw_a1) {
        this.luckydraw_a1 = luckydraw_a1;
    }

    public String getLuckydraw_a2() {
        return luckydraw_a2;
    }

    public void setLuckydraw_a2(String luckydraw_a2) {
        this.luckydraw_a2 = luckydraw_a2;
    }

    private String luckydraw_a1;
    private String luckydraw_a2;

    private String total_point;
    private String redeem_point;
    private String balance_points;
    private String status;
    private String type;

    private String participant_code;
    private String participant_id_pk;
    private String form_fillup_date;
    private String participant_claim_history;

    public UserModel() {
    }

    public String getBalance_points() {
        return balance_points;
    }

    public void setBalance_points(String balance_points) {
        this.balance_points = balance_points;
    }

    public String getRedeem_point() {
        return redeem_point;
    }

    public void setRedeem_point(String redeem_point) {
        this.redeem_point = redeem_point;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserfname() {
        return userfname;
    }

    public void setUserfname(String userfname) {
        this.userfname = userfname;
    }

    public String getUserlname() {
        return userlname;
    }

    public void setUserlname(String userlname) {
        this.userlname = userlname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNomini() {
        return nomini;
    }

    public void setNomini(String nomini) {
        this.nomini = nomini;
    }

    public String getNominirely() {
        return nominirely;
    }

    public void setNominirely(String nominirely) {
        this.nominirely = nominirely;
    }

    public String getMothername() {
        return mothername;
    }

    public void setMothername(String mothername) {
        this.mothername = mothername;
    }

    public String getWorkshopname() {
        return workshopname;
    }

    public void setWorkshopname(String workshopname) {
        this.workshopname = workshopname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getShopadd() {
        return shopadd;
    }

    public void setShopadd(String shopadd) {
        this.shopadd = shopadd;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getToatalsperconpermonth() {
        return toatalsperconpermonth;
    }

    public void setToatalsperconpermonth(String toatalsperconpermonth) {
        this.toatalsperconpermonth = toatalsperconpermonth;
    }

    public String getSperpartconformmvehicpermonth() {
        return sperpartconformmvehicpermonth;
    }

    public void setSperpartconformmvehicpermonth(String sperpartconformmvehicpermonth) {
        this.sperpartconformmvehicpermonth = sperpartconformmvehicpermonth;
    }

    public String getMmgenuspareconpermonth() {
        return mmgenuspareconpermonth;
    }

    public void setMmgenuspareconpermonth(String mmgenuspareconpermonth) {
        this.mmgenuspareconpermonth = mmgenuspareconpermonth;
    }

    public String getTotalvehicalpermonth() {
        return totalvehicalpermonth;
    }

    public void setTotalvehicalpermonth(String totalvehicalpermonth) {
        this.totalvehicalpermonth = totalvehicalpermonth;
    }

    public String getMahindravehicalpermonth() {
        return mahindravehicalpermonth;
    }

    public void setMahindravehicalpermonth(String mahindravehicalpermonth) {
        this.mahindravehicalpermonth = mahindravehicalpermonth;
    }

    public String getNoofmechanics() {
        return noofmechanics;
    }

    public void setNoofmechanics(String noofmechanics) {
        this.noofmechanics = noofmechanics;
    }

    public String getTotal_point() {
        return total_point;
    }

    public void setTotal_point(String total_point) {
        this.total_point = total_point;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getParticipant_code() {
        return participant_code;
    }

    public void setParticipant_code(String participant_code) {
        this.participant_code = participant_code;
    }

    public String getParticipant_id_pk() {
        return participant_id_pk;
    }

    public void setParticipant_id_pk(String participant_id_pk) {
        this.participant_id_pk = participant_id_pk;
    }

    public String getForm_fillup_date() {
        return form_fillup_date;
    }

    public void setForm_fillup_date(String form_fillup_date) {
        this.form_fillup_date = form_fillup_date;
    }

    public String getParticipant_claim_history() {
        return participant_claim_history;
    }

    public void setParticipant_claim_history(String participant_claim_history) {
        this.participant_claim_history = participant_claim_history;
    }

    public String getState_mapped() {
        return state_mapped;
    }

    public void setState_mapped(String state_mapped) {
        this.state_mapped = state_mapped;
    }

    public UserModel(int id, String picture, String userfname, String userlname, String gender, String nomini, String nominirely, String mothername, String workshopname, String state, String district,
                     String pincode, String taluka, String city, String shopadd, String landmark, String mobile_no, String email, String sector, String specialization,
                     String dob, String regdate, String toatalsperconpermonth, String sperpartconformmvehicpermonth, String mmgenuspareconpermonth, String totalvehicalpermonth,
                     String mahindravehicalpermonth, String noofmechanics, String participant_code, String participant_id_pk, String form_fillup_date, String participant_claim_history) {
        this.id = id;
        this.picture = picture;

        this.userfname = userfname;
        this.userlname = userlname;
        this.gender = gender;
        this.nomini = nomini;
        this.nominirely = nominirely;
        this.mothername = mothername;
        this.workshopname = workshopname;
        this.state = state;
        this.district = district;

        this.pincode = pincode;
        this.taluka = taluka;
        this.city = city;
        this.shopadd = shopadd;
        this.landmark = landmark;
        this.mobile_no = mobile_no;
        this.email = email;
        this.sector = sector;
        this.specialization = specialization;

        this.dob = dob;
        this.regdate = regdate;
        this.toatalsperconpermonth = toatalsperconpermonth;
        this.sperpartconformmvehicpermonth = sperpartconformmvehicpermonth;
        this.mmgenuspareconpermonth = mmgenuspareconpermonth;

        this.mobile_no = totalvehicalpermonth;
        this.email = mahindravehicalpermonth;
        this.sector = noofmechanics;

        this.participant_code = participant_code;
        this.participant_id_pk = participant_id_pk;
        this.form_fillup_date = form_fillup_date;

        this.participant_claim_history = participant_claim_history;

    }


    /*public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getParticepentid() {
        return particepentid;
    }

    public void setParticepentid(String particepentid) {
        this.particepentid = particepentid;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getDistrict() {
        return district;
    }


    public String getWorkshopname() {
        return workshopname;
    }

    public void setWorkshopname(String workshopname) {
        this.workshopname = workshopname;
    }*/

}
