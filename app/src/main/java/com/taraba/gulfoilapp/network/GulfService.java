package com.taraba.gulfoilapp.network;

import com.taraba.gulfoilapp.model.AppSurveyRequest;
import com.taraba.gulfoilapp.model.AppSurveyResponse;
import com.taraba.gulfoilapp.model.AppVersionRequest;
import com.taraba.gulfoilapp.model.AppVersionResponse;
import com.taraba.gulfoilapp.model.BankMasterResponse;
import com.taraba.gulfoilapp.model.DashboardDataRequest;
import com.taraba.gulfoilapp.model.DashboardDataResponse;
import com.taraba.gulfoilapp.model.FLSProfileUpdateResponse;
import com.taraba.gulfoilapp.model.GetPanDetailsResponse;
import com.taraba.gulfoilapp.model.GetRetailerProfileDetailsRequest;
import com.taraba.gulfoilapp.model.GetRetailerProfileDetailsResponse;
import com.taraba.gulfoilapp.model.HelpRequest;
import com.taraba.gulfoilapp.model.HelpResponse;
import com.taraba.gulfoilapp.model.KYCOTPRequest;
import com.taraba.gulfoilapp.model.KnowledgeCornerRequest;
import com.taraba.gulfoilapp.model.KnowledgeCornerResponse;
import com.taraba.gulfoilapp.model.LeaderBoardRequest;
import com.taraba.gulfoilapp.model.LeaderBoardResponse;
import com.taraba.gulfoilapp.model.MagicBonanzaListResponse;
import com.taraba.gulfoilapp.model.MilestoneJourneyRequest;
import com.taraba.gulfoilapp.model.MilestoneJourneyResponse;
import com.taraba.gulfoilapp.model.MilestoneTargetListRequest;
import com.taraba.gulfoilapp.model.MilestoneTargetListResponse;
import com.taraba.gulfoilapp.model.NotificationRequest;
import com.taraba.gulfoilapp.model.NotificationResponse;
import com.taraba.gulfoilapp.model.ParticipantProfileRequest;
import com.taraba.gulfoilapp.model.ParticipantProfileResponse;
import com.taraba.gulfoilapp.model.PointsCalculatorURLRequest;
import com.taraba.gulfoilapp.model.PointsCalculatorURLResponse;
import com.taraba.gulfoilapp.model.ResendOTPRetailerRequest;
import com.taraba.gulfoilapp.model.ResendOTPRetailerResponse;
import com.taraba.gulfoilapp.model.SearchRetailerRequest;
import com.taraba.gulfoilapp.model.SearchRetailerResponse;
import com.taraba.gulfoilapp.model.SubmitKYCOTPRequest;
import com.taraba.gulfoilapp.model.SubmitYDROTPRequest;
import com.taraba.gulfoilapp.model.SubmitYDROTPResponse;
import com.taraba.gulfoilapp.model.THPeriodResponse;
import com.taraba.gulfoilapp.model.THRequest;
import com.taraba.gulfoilapp.model.THResponse;
import com.taraba.gulfoilapp.model.TargetMeterReqest;
import com.taraba.gulfoilapp.model.TargetMeterResponse;
import com.taraba.gulfoilapp.model.TnCResponse;
import com.taraba.gulfoilapp.model.UnnatiConnectRequest;
import com.taraba.gulfoilapp.model.UnnatiConnectResponse;
import com.taraba.gulfoilapp.model.UpdateParticipantProfileResponse;
import com.taraba.gulfoilapp.model.UpdateRetailerProfileRequest;
import com.taraba.gulfoilapp.model.UpdateRetailerProfileResponse;
import com.taraba.gulfoilapp.model.VerifyUpdatePanOTPRequest;
import com.taraba.gulfoilapp.model.VerifyUpdateProfileOTPRequest;
import com.taraba.gulfoilapp.model.VerifyUpdateProfileOTPResponse;
import com.taraba.gulfoilapp.model.VerifyUpdateRetailerProfileOTPRequest;
import com.taraba.gulfoilapp.model.VerifyUpdateRetailerProfileOTPResponse;
import com.taraba.gulfoilapp.model.YDROTPRequest;
import com.taraba.gulfoilapp.model.YDROTPResponse;
import com.taraba.gulfoilapp.model.YDRRequest;
import com.taraba.gulfoilapp.model.YDRResponse;
import com.taraba.gulfoilapp.ui.MyTDS.MyTDSCertificate;
import com.taraba.gulfoilapp.ui.MyTDS.MyTDSCertificateDownloadResponse;
import com.taraba.gulfoilapp.ui.MyTDS.MyTDSCertificateRequest;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface GulfService {

    @POST("tnc")
    Observable<TnCResponse> getTnC();

    @POST("participant_dashboard")
    Observable<DashboardDataResponse> getDashboardData(@Body DashboardDataRequest request);


    @POST("reward_orderhistory")
    Observable<YDRResponse> getYDRData(@Body YDRRequest request);

    @POST("ev_otp")
    Observable<YDROTPResponse> sendDigitalRewardOTP(@Body YDROTPRequest request);

    //vijendra 10 Apr 2023
    @POST("non_kyc_approved")
    Observable<YDROTPResponse> sendKYCOTP(@Body KYCOTPRequest request);

    @POST("ev_details")
    Observable<SubmitYDROTPResponse> submitDigitalRewardOTP(@Body SubmitYDROTPRequest request);

    //vijendra 10 Apr 2023
    @POST("non_kyc_otp_submit")
    Observable<YDROTPResponse> submitKYCOTP(@Body SubmitKYCOTPRequest request);

    @POST("milestone_set_target")
    Observable<MilestoneTargetListResponse> getMilestoneTargetList(@Body MilestoneTargetListRequest request);

    @POST("milestone_journey")
    Observable<MilestoneJourneyResponse> submitMilestoreJourney(@Body MilestoneJourneyRequest request);

    @POST("transaction_history")
    Observable<THResponse> getTH(@Body THRequest request);

    @POST("point_period")
    Observable<THPeriodResponse> getTHPeriod();

    @POST("app_version")
    Observable<AppVersionResponse> getAppVersionDetails(@Body AppVersionRequest request);

    @POST("target_header")
    Observable<MagicBonanzaListResponse> getMagicBoanazaList();

    @POST("target_meter")
    Observable<TargetMeterResponse> getTargetMeter(@Body TargetMeterReqest reqest);

    @POST("point_calculator")
    Observable<PointsCalculatorURLResponse> getPointsCalaculatorURL(@Body PointsCalculatorURLRequest reqest);

    @POST("unnati_connect")
    Observable<UnnatiConnectResponse> getUnnatiConnect(@Body UnnatiConnectRequest reqest);

    @POST("help")
    Observable<HelpResponse> getHelp(@Body HelpRequest reqest);

    @POST("bank_detail")
    Observable<BankMasterResponse> getBankMasterData();

    @POST("participant_my_profile")
    Observable<ParticipantProfileResponse> getParticipantProfileData(@Body ParticipantProfileRequest request);



    @Multipart
    @POST("update_my_profile")
    Observable<UpdateParticipantProfileResponse> updateParticipantProfile(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part panCardImg,
            @Part MultipartBody.Part profileImg,
            @Part MultipartBody.Part cancelChequeImg
    );

    @Multipart
    @POST("verify_image")
    Observable<GetPanDetailsResponse> getPanDetails(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part panCardImg
    );

    @Multipart
    @POST("update_retailer_profile_by_fls")
    Observable<UpdateParticipantProfileResponse> updatePanRetailer(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part panCardImg
    );


    @GET
    Observable<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

    @POST("update_my_profile_otp")
    Observable<VerifyUpdateProfileOTPResponse> verifyUpdateProfileOTP(@Body VerifyUpdateProfileOTPRequest request);

    @POST("update_my_profile_otp")
    Observable<VerifyUpdateProfileOTPResponse> verifyUpdatePanOTP(@Body VerifyUpdatePanOTPRequest request);


    @POST("knowledge_corner")
    Observable<KnowledgeCornerResponse> getKnowledgeCorner(@Body KnowledgeCornerRequest request);

    @POST("notification_list")
    Observable<NotificationResponse> getNotification(@Body NotificationRequest request);

    @POST("participant_leaderboard")
    Observable<LeaderBoardResponse> getParticipantLeaderboard(@Body LeaderBoardRequest request);

    @POST("v2_verify_update_profile")
    Observable<VerifyUpdateRetailerProfileOTPResponse> verifyUpdateRetailerProfile(@Body VerifyUpdateRetailerProfileOTPRequest verifyUpdateRetailerProfileOTPRequest);

    @POST("v2_Resend_Otp_profile_update")
    Observable<ResendOTPRetailerResponse> resendOTPRetailer(@Body ResendOTPRetailerRequest resendOTPRetailerRequest);

    @POST("app_survey")
    Observable<AppSurveyResponse> getAppSurvey(@Body AppSurveyRequest appSurveyRequest);

    @POST("mgmt_profile_update")
    @Multipart
    Observable<FLSProfileUpdateResponse> fLSProfileUpdate(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part part);

    @POST("get_participant")
    Observable<SearchRetailerResponse> searchRetailer(@Body SearchRetailerRequest searchRetailerRequest);

    @POST("v2_Otp_UpdateParticipant_profile")
    Observable<UpdateRetailerProfileResponse> updateRetailerProfile(@Body UpdateRetailerProfileRequest updateRetailerProfileRequest);

    @POST("v2_get_participant_profile")
    Observable<GetRetailerProfileDetailsResponse> getRetilerProfileDetails(@Body GetRetailerProfileDetailsRequest getRetailerProfileDetailsRequest);

    @POST("tds_selection")
    Observable<MyTDSCertificate> getTDSData();

    @POST("tds_certificate_download")
    Observable<MyTDSCertificateDownloadResponse> getTDSDownloadData(@Body MyTDSCertificateRequest myTDSCertificateRequest);

}
