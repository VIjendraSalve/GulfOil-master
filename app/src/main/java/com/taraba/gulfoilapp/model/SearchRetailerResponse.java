package com.taraba.gulfoilapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class SearchRetailerResponse implements Parcelable {
    public static final Creator<SearchRetailerResponse> CREATOR = new Creator<SearchRetailerResponse>() {
        public SearchRetailerResponse createFromParcel(Parcel parcel) {
            return new SearchRetailerResponse(parcel);
        }

        public SearchRetailerResponse[] newArray(int i) {
            return new SearchRetailerResponse[i];
        }
    };
    private String error;
    private List<ParticpantData> particpant_data;
    private String status;

    public int describeContents() {
        return 0;
    }

    protected SearchRetailerResponse(Parcel parcel) {
        this.status = parcel.readString();
        this.error = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.status);
        parcel.writeString(this.error);
    }

    public List<ParticpantData> getParticpant_data() {
        return this.particpant_data;
    }

    public void setParticpant_data(List<ParticpantData> list) {
        this.particpant_data = list;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String str) {
        this.error = str;
    }

    public class ParticpantData {
        private String active_status;
        private String alternate_mobile_no;
        private String anniversary_date;
        private String bank_account_details;
        private String city;
        private String db_code;
        private String db_name;
        private String district;
        private String dob;
        private int earned_points;
        private String email_id;
        private String first_name;
        private String landline_no;
        private String landmark;
        private String last_name;
        private String login_id_pk;
        private String lucky_draw_a1;
        private String lucky_draw_a2;
        private String mobile_no;
        private String no_of_children;
        //private String pan_detail;
        private String participant_gender;
        private String participant_id_pk;
        private String pincode;
        private int points;
        private String record_date;
        private int redeemed_points;
        private String residential_address;
        private String residential_address2;
        private String residential_city;
        private String residential_landmark;
        private String residential_pincode;
        private String residential_state;
        private String retailer_code;
        private String spouse_name;
        private String state;
        private String status;
        private String sub_district;
        private String username;
        private String workshop_address;
        private String workshop_name;
        private PanDetail pan_detail;

        public PanDetail getPan_detail() {
            return pan_detail;
        }

        public void setPan_detail(PanDetail pan_detail) {
            this.pan_detail = pan_detail;
        }

        public class PanDetail{
            private String pan;
            private String pan_name;
            private String pan_status;
            private String view_pan;
            private boolean is_readonly;
            private ArrayList<String> pan_reason = new ArrayList<>();

            public String getPan() {
                return pan;
            }

            public void setPan(String pan) {
                this.pan = pan;
            }

            public String getView_pan() {
                return view_pan;
            }

            public void setView_pan(String view_pan) {
                this.view_pan = view_pan;
            }

            public boolean isIs_readonly() {
                return is_readonly;
            }

            public void setIs_readonly(boolean is_readonly) {
                this.is_readonly = is_readonly;
            }

            public String getPan_name() {
                return pan_name;
            }

            public void setPan_name(String pan_name) {
                this.pan_name = pan_name;
            }

            public String getPan_status() {
                return pan_status;
            }

            public void setPan_status(String pan_status) {
                this.pan_status = pan_status;
            }

            public ArrayList<String> getPan_reason() {
                return pan_reason;
            }

            public void setPan_reason(ArrayList<String> pan_reason) {
                this.pan_reason = pan_reason;
            }
        }

        public ParticpantData() {
        }

        public String getLogin_id_pk() {
            return this.login_id_pk;
        }

        public void setLogin_id_pk(String str) {
            this.login_id_pk = str;
        }

        public String getRetailer_code() {
            return this.retailer_code;
        }

        public void setRetailer_code(String str) {
            this.retailer_code = str;
        }

        public String getUsername() {
            return this.username;
        }

        public void setUsername(String str) {
            this.username = str;
        }

        public String getMobile_no() {
            return this.mobile_no;
        }

        public void setMobile_no(String str) {
            this.mobile_no = str;
        }

        public String getFirst_name() {
            return this.first_name;
        }

        public void setFirst_name(String str) {
            this.first_name = str;
        }

        public String getLast_name() {
            return this.last_name;
        }

        public void setLast_name(String str) {
            this.last_name = str;
        }

        public String getState() {
            return this.state;
        }

        public void setState(String str) {
            this.state = str;
        }

        public String getDistrict() {
            return this.district;
        }

        public void setDistrict(String str) {
            this.district = str;
        }

        public String getEmail_id() {
            return this.email_id;
        }

        public void setEmail_id(String str) {
            this.email_id = str;
        }

        public String getParticipant_id_pk() {
            return this.participant_id_pk;
        }

        public void setParticipant_id_pk(String str) {
            this.participant_id_pk = str;
        }

        public String getWorkshop_name() {
            return this.workshop_name;
        }

        public void setWorkshop_name(String str) {
            this.workshop_name = str;
        }

        public String getWorkshop_address() {
            return this.workshop_address;
        }

        public void setWorkshop_address(String str) {
            this.workshop_address = str;
        }

        public String getPincode() {
            return this.pincode;
        }

        public void setPincode(String str) {
            this.pincode = str;
        }

        public String getLandmark() {
            return this.landmark;
        }

        public void setLandmark(String str) {
            this.landmark = str;
        }

        public String getCity() {
            return this.city;
        }

        public void setCity(String str) {
            this.city = str;
        }

        public String getDob() {
            return this.dob;
        }

        public void setDob(String str) {
            this.dob = str;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String str) {
            this.status = str;
        }

        public String getRecord_date() {
            return this.record_date;
        }

        public void setRecord_date(String str) {
            this.record_date = str;
        }

        public int getPoints() {
            return this.points;
        }

        public void setPoints(int i) {
            this.points = i;
        }

        public String getParticipant_gender() {
            return this.participant_gender;
        }

        public void setParticipant_gender(String str) {
            this.participant_gender = str;
        }

        public String getResidential_address() {
            return this.residential_address;
        }

        public void setResidential_address(String str) {
            this.residential_address = str;
        }

        public String getResidential_address2() {
            return this.residential_address2;
        }

        public void setResidential_address2(String str) {
            this.residential_address2 = str;
        }

        public String getResidential_pincode() {
            return this.residential_pincode;
        }

        public void setResidential_pincode(String str) {
            this.residential_pincode = str;
        }

        public String getResidential_city() {
            return this.residential_city;
        }

        public void setResidential_city(String str) {
            this.residential_city = str;
        }

        public String getResidential_state() {
            return this.residential_state;
        }

        public void setResidential_state(String str) {
            this.residential_state = str;
        }

        public String getAlternate_mobile_no() {
            return this.alternate_mobile_no;
        }

        public void setAlternate_mobile_no(String str) {
            this.alternate_mobile_no = str;
        }

        public String getResidential_landmark() {
            return this.residential_landmark;
        }

        public void setResidential_landmark(String str) {
            this.residential_landmark = str;
        }

        public String getSpouse_name() {
            return this.spouse_name;
        }

        public void setSpouse_name(String str) {
            this.spouse_name = str;
        }

        public String getNo_of_children() {
            return this.no_of_children;
        }

        public void setNo_of_children(String str) {
            this.no_of_children = str;
        }

        public String getLandline_no() {
            return this.landline_no;
        }

        public void setLandline_no(String str) {
            this.landline_no = str;
        }

        public String getAnniversary_date() {
            return this.anniversary_date;
        }

        public void setAnniversary_date(String str) {
            this.anniversary_date = str;
        }

        public String getSub_district() {
            return this.sub_district;
        }

        public void setSub_district(String str) {
            this.sub_district = str;
        }

        public String getLucky_draw_a1() {
            return this.lucky_draw_a1;
        }

        public void setLucky_draw_a1(String str) {
            this.lucky_draw_a1 = str;
        }

        public String getLucky_draw_a2() {
            return this.lucky_draw_a2;
        }

        public void setLucky_draw_a2(String str) {
            this.lucky_draw_a2 = str;
        }

        public String getActive_status() {
            return this.active_status;
        }

        public void setActive_status(String str) {
            this.active_status = str;
        }

        public String getBank_account_details() {
            return this.bank_account_details;
        }

        public void setBank_account_details(String str) {
            this.bank_account_details = str;
        }

       /* public String getPan_detail() {
            return this.pan_detail;
        }

        public void setPan_detail(String str) {
            this.pan_detail = str;
        }*/

        public String getDb_name() {
            return this.db_name;
        }

        public void setDb_name(String str) {
            this.db_name = str;
        }

        public String getDb_code() {
            return this.db_code;
        }

        public void setDb_code(String str) {
            this.db_code = str;
        }

        public int getEarned_points() {
            return this.earned_points;
        }

        public void setEarned_points(int i) {
            this.earned_points = i;
        }

        public int getRedeemed_points() {
            return this.redeemed_points;
        }

        public void setRedeemed_points(int i) {
            this.redeemed_points = i;
        }
    }
}
