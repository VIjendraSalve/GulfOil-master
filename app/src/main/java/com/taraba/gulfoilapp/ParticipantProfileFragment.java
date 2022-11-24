package com.taraba.gulfoilapp;

import static android.view.View.GONE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.dialog.ImageSelectionOptionDialog;
import com.taraba.gulfoilapp.model.BankMasterResponse;
import com.taraba.gulfoilapp.model.ParticipantProfileRequest;
import com.taraba.gulfoilapp.model.ParticipantProfileResponse;
import com.taraba.gulfoilapp.model.UpdateParticipantProfileResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.RetrofitUtils;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.FileUtils;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.GulfValidator;
import com.taraba.gulfoilapp.util.ViewUtils;
import com.taraba.gulfoilapp.util.camera.CameraHelper;
import com.taraba.gulfoilapp.view.FullScreenImageActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ParticipantProfileFragment extends Fragment
        implements View.OnClickListener, CameraHelper.AsyncResponse,
        AdapterView.OnItemSelectedListener {

    private static final int PROFILE_IMG_REQUEST_CODE = 101;
    private static final int PAN_CARD_IMG_REQUEST_CODE = 102;
    private static final int CANCELLED_CHEQUE_CARD_IMG_REQUEST_CODE = 103;
    private View view;
    private ImageView ivProfileImage, ivUpdateProfileImage,
            ivBasicDetailsDropDown, ivBankDetailsDropDown, ivPanDetailsDropDown;
    private TextView
            tvStatus, tvViewPanCardCopy, tvViewCancelChequeCopy, tvErrorBankName,
            tvErrorBankType, tvPanRevalidate, tvBankRevalidate;
    private LinearLayout llBasicDetailsForm, llBankDetailsForm, ll_pan_data, ll_pan_details;
    private TextInputEditText edtUIN,
            edtRetailerCode,
            edtShopName,
            edtFullName,
            edtEmail,
            edtMobile,
            edtPanCardNoCode,
            edtPanImage,
            edtAccountHolderName,
            edtAccountNumber,
            edtIFSCCode,
            edtBranchName,
            edtCancelledChequeImage,
            edtOtherBankName,
            edtPanCardName;
    private TextInputLayout tilUIN,
            tilRetailerCode,
            tilShopName,
            tilFullName,
            tilEmailName,
            tilMobile,
            tilPanCardNo,
            tilPanImage,
            tilAccountHolderName,
            tilAccountNumber,
            tilIFSCCode,
            tilBranchName,
            tilCancelledChequeImage,
            tilPanCardName;
    private AppCompatButton btnCancel, btnSubmit;
    private RelativeLayout rl_other_bank_name;


    private AppCompatButton btnChoosePanCard, btnChooseCancelCheque;
    private Spinner spnBankName, spnBankType;

    private boolean isTest;
    private CameraHelper cameraHelper;
    private String profileImgLocalPath, panCardImgLocalPath, cancelledChequeImgLocalPath;
    private ProgressDialog progressDialog;
    private Disposable disposable;
    private SharedPreferences userPref;
    private ParticipantProfileResponse participantProfileResponse;
    private BankMasterResponse bankMasterData;
    private String strPanReason = "", strBankReason = "";
    private TextView tvPanRevalidateReason, tvBankRevalidateReason;

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    private void setListeners() {
        ivUpdateProfileImage.setOnClickListener(this);
        ivProfileImage.setOnClickListener(this);
        ivBasicDetailsDropDown.setOnClickListener(this);
        tvViewPanCardCopy.setOnClickListener(this);
        btnChoosePanCard.setOnClickListener(this);

        ivBankDetailsDropDown.setOnClickListener(this);
        btnChooseCancelCheque.setOnClickListener(this);
        tvViewCancelChequeCopy.setOnClickListener(this);
        tvPanRevalidateReason.setOnClickListener(this);
        tvBankRevalidateReason.setOnClickListener(this);
        ivPanDetailsDropDown.setOnClickListener(this);

        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(getLayoutUserWise(), container, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_update_profile));
        }
        init(view);
        setListeners();
        getParticipantProfileDataAPI();

        return view;
    }

    private void init(View view) {
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        cameraHelper = new CameraHelper(getActivity(), this);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);

        ivUpdateProfileImage = view.findViewById(R.id.ivUpdateProfileImage);
        ivBasicDetailsDropDown = view.findViewById(R.id.ivBasicDetailsDropDown);
        tvStatus = view.findViewById(R.id.tvStatus);
        llBasicDetailsForm = view.findViewById(R.id.llBasicDetailsForm);
        tilUIN = view.findViewById(R.id.tilUIN);
        edtUIN = view.findViewById(R.id.edtUIN);
        tilRetailerCode = view.findViewById(R.id.tilRetailerCode);
        edtRetailerCode = view.findViewById(R.id.edtRetailerCode);
        tilShopName = view.findViewById(R.id.tilShopName);
        edtShopName = view.findViewById(R.id.edtShopName);
        tilFullName = view.findViewById(R.id.tilFullName);
        edtFullName = view.findViewById(R.id.edtFullName);
        tilEmailName = view.findViewById(R.id.tilEmailName);
        edtEmail = view.findViewById(R.id.edtEmail);
        tilMobile = view.findViewById(R.id.tilMobile);
        edtMobile = view.findViewById(R.id.edtMobile);
        tilPanCardName = view.findViewById(R.id.tilPanCardName);
        tilPanCardNo = view.findViewById(R.id.tilPanCardNo);
        edtPanCardNoCode = view.findViewById(R.id.edtPanCardNoCode);
        edtPanCardName = view.findViewById(R.id.edtPanCardName);
        tilPanImage = view.findViewById(R.id.tilPanImage);
        edtPanImage = view.findViewById(R.id.edtPanImage);
        btnChoosePanCard = view.findViewById(R.id.btnChoosePanCard);
        tvViewPanCardCopy = view.findViewById(R.id.tvViewPanCardCopy);
        tvPanRevalidate = view.findViewById(R.id.tvPanRevalidate);
        tvBankRevalidate = view.findViewById(R.id.tvBankRevalidate);
        tvPanRevalidateReason = view.findViewById(R.id.tvPanRevalidateReason);
        tvBankRevalidateReason = view.findViewById(R.id.tvBankRevalidateReason);
        ivPanDetailsDropDown = view.findViewById(R.id.ivPanDetailsDropDown);
        ll_pan_details = view.findViewById(R.id.ll_pan_details);


        rl_other_bank_name = view.findViewById(R.id.rl_other_bank_name);
        edtOtherBankName = view.findViewById(R.id.edtOtherBankName);


        spnBankName = view.findViewById(R.id.spnBankName);
        spnBankName.setOnItemSelectedListener(this);
        spnBankType = view.findViewById(R.id.spnBankType);
        tvErrorBankName = view.findViewById(R.id.tvErrorBankName);
        tvErrorBankType = view.findViewById(R.id.tvErrorBankType);

        ivBankDetailsDropDown = view.findViewById(R.id.ivBankDetailsDropDown);
        ivBankDetailsDropDown.animate().rotationX(180).setDuration(800).start();

        llBankDetailsForm = view.findViewById(R.id.llBankDetailsForm);
        ll_pan_data = view.findViewById(R.id.ll_pan_data);
        ll_pan_details = view.findViewById(R.id.ll_pan_details);
        tilAccountHolderName = view.findViewById(R.id.tilAccountHolderName);
        edtAccountHolderName = view.findViewById(R.id.edtAccountHolderName);
        tilAccountNumber = view.findViewById(R.id.tilAccountNumber);
        edtAccountNumber = view.findViewById(R.id.edtAccountNumber);
        tilIFSCCode = view.findViewById(R.id.tilIFSCCode);
        edtIFSCCode = view.findViewById(R.id.edtIFSCCode);
        tilBranchName = view.findViewById(R.id.tilBranchName);
        edtBranchName = view.findViewById(R.id.edtBranchName);
        tilCancelledChequeImage = view.findViewById(R.id.tilCancelledChequeImage);
        edtCancelledChequeImage = view.findViewById(R.id.edtCancelledChequeImage);
        btnChooseCancelCheque = view.findViewById(R.id.btnChooseCancelCheque);
        tvViewCancelChequeCopy = view.findViewById(R.id.tvViewCancelChequeCopy);

        btnCancel = view.findViewById(R.id.btnCancel);
        btnSubmit = view.findViewById(R.id.btnSubmit);

    }


    private void showFullScreenImage(String profileImgLocalPath, String s, boolean isLocalImg) {
        Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
        intent.putExtra("IMG_URL", profileImgLocalPath);
        intent.putExtra(
                "Title", s);
        intent.putExtra("isLocalImg", isLocalImg);
        startActivity(intent);
    }

    private void ImgSelectionDialog(int requestCode, ImageView imageView) {
        new ImageSelectionOptionDialog(getActivity(), new GulfOilUtils().getUserType())
                .setTitle("Select Image")
                .setOnCameraClickListener(dialog -> {
                    dialog.dismiss();
                    cameraHelper.selectImage(imageView, requestCode, CameraHelper.CAMERA_INTENT);
                }).setOnGalleryClickListener(dialog -> {
                    dialog.dismiss();
                    cameraHelper.selectImage(imageView, requestCode, CameraHelper.GALLERY_INTENT);
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cameraHelper.myActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void processFinish(String output, int img_no) {
        switch (img_no) {
            case PROFILE_IMG_REQUEST_CODE:
                profileImgLocalPath = output;
                break;
            case PAN_CARD_IMG_REQUEST_CODE:
                panCardImgLocalPath = output;
                edtPanImage.setText(FileUtils.getFileNameFromURL(output));
                break;
            case CANCELLED_CHEQUE_CARD_IMG_REQUEST_CODE:
                cancelledChequeImgLocalPath = output;
                edtCancelledChequeImage.setText(FileUtils.getFileNameFromURL(output));
                break;
        }
    }


    private void getParticipantProfileDataAPI() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            ParticipantProfileRequest request = new ParticipantProfileRequest();
            request.setParticipant_login_id(userPref.getString("usertrno", ""));
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getParticipantProfileData(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::participantProfileResponse, this::participantProfileError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void participantProfileError(Throwable throwable) {
        progressDialog.dismiss();
        Log.d("Testtt", "participantProfileError: " + throwable.toString());
        Log.d("Testtt", "participantProfileError: " + throwable.getMessage().toString());
        Log.d("Testtt", "participantProfileError: " + throwable.getLocalizedMessage().toString());

        /*new GulfUnnatiDialog(getActivity())
                .setTitle(getString(R.string.str_error))
                .hideDialogCloseButton(true)
                .setDescription(getString(R.string.something_went_wrong))
                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                    dialog.dismiss();
                    Navigation.findNavController(getView()).popBackStack();
                })
                .show();*/
    }

    private void participantProfileResponse(ParticipantProfileResponse response) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            participantProfileResponse = response;

            Log.d("BankReason", "participantProfileResponse: " +
                    response.getData().getBank_details().getBank_reason().size());

            getBankMasterDataAPI();
            setProfileData();
        } else {
            new GulfUnnatiDialog(getActivity())
                    .setTitle(getString(R.string.str_error))
                    .hideDialogCloseButton(true)
                    .setDescription(response.getError())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        Navigation.findNavController(getView()).popBackStack();
                    })
                    .show();
        }

    }

    private void setProfileData() {
        if (participantProfileResponse != null) {
            ParticipantProfileResponse.Data data = participantProfileResponse.getData();
            Glide.with(this).load(data.getView_profile()).placeholder(R.drawable.ic_default_user_avatar).into(ivProfileImage);
            tvStatus.setText(GulfOilUtils.getHtmlText(data.getProfile_status()));
            edtUIN.setText(data.getUin());
            edtRetailerCode.setText(data.getRetailer_code());
            edtShopName.setText(data.getShop_name());
            edtFullName.setText(data.getFull_name());
            edtEmail.setText(data.getEmail_id());
            edtMobile.setText(data.getMobile_no());
            edtPanCardNoCode.setText(data.getPan_detail().getPan());
            edtPanCardName.setText(data.getPan_detail().getPan_name());
            edtPanImage.setText(FileUtils.getFileNameFromURL(data.getPan_detail().getView_pan()));

            if (data.getPan_detail().isIs_readonly()) {
                edtPanCardNoCode.setEnabled(false);
                edtPanCardName.setEnabled(false);
                edtPanImage.setEnabled(false);
                btnChoosePanCard.setEnabled(false);

                tilPanCardNo.setBoxBackgroundColor(getActivity().getResources().getColor(R.color.menu_disable_grey));
                tilPanImage.setBoxBackgroundColor(getActivity().getResources().getColor(R.color.menu_disable_grey));
                tilPanCardName.setBoxBackgroundColor(getActivity().getResources().getColor(R.color.menu_disable_grey));
            }else {
                tilPanCardNo.setBoxBackgroundColor(getActivity().getResources().getColor(R.color.white));
                tilPanImage.setBoxBackgroundColor(getActivity().getResources().getColor(R.color.white));
                tilPanCardName.setBoxBackgroundColor(getActivity().getResources().getColor(R.color.white));
            }

            strBankReason = "";
            if (data.getBank_details().getBank_reason().size() > 0) {
                for (int i = 0; i < data.getBank_details().getBank_reason().size(); i++) {
                    strBankReason = strBankReason + " " + (i + 1) + ") " + data.getBank_details().getBank_reason().get(i) + "\n ";
                    Log.d("Values", "setProfileData 2: " + data.getBank_details().getBank_reason().get(i));
                }
            }

            Log.d("Values", "setProfileData: " + data.getBank_details().getAccount_holder_name());
            edtAccountHolderName.setText(data.getBank_details().getAccount_holder_name());
            edtAccountNumber.setText(data.getBank_details().getAccount_number());
            edtIFSCCode.setText(data.getBank_details().getIfsc_code());
            edtBranchName.setText(data.getBank_details().getBranch_name());
            edtCancelledChequeImage.setText(FileUtils.getFileNameFromURL(data.getBank_details().getView_cheque_image()));

            if (data.getBank_details().isIs_readonly()) {
                spnBankName.setEnabled(false);
                spnBankType.setEnabled(false);
                edtAccountHolderName.setEnabled(false);
                edtAccountNumber.setEnabled(false);
                edtIFSCCode.setEnabled(false);
                edtBranchName.setEnabled(false);
                edtCancelledChequeImage.setEnabled(false);
                btnChooseCancelCheque.setEnabled(false);
            }

            if (!data.getPan_detail().getPan_status().equals("")) {
                if(data.getPan_detail().getPan_status().toUpperCase().equals("KYC APPROVED")) {
                    tvPanRevalidate.setText(data.getPan_detail().getPan_status());
                    tvPanRevalidate.setTextColor(getActivity().getResources().getColor(R.color.milestone_journey_success));
                }else {
                    tvPanRevalidate.setText(data.getPan_detail().getPan_status());
                    tvPanRevalidate.setTextColor(getActivity().getResources().getColor(R.color.error));
                }
            } else {
                tvPanRevalidate.setText("");
            }

            if (!data.getBank_details().getBank_status().equals("")) {
                if(data.getBank_details().getBank_status().toUpperCase().equals("APPROVED")) {
                    tvBankRevalidate.setText(data.getBank_details().getBank_status());
                    tvBankRevalidate.setTextColor(getActivity().getResources().getColor(R.color.milestone_journey_success));
                }else {
                    tvBankRevalidate.setText(data.getBank_details().getBank_status());
                    tvBankRevalidate.setTextColor(getActivity().getResources().getColor(R.color.error));
                }
            } else {
                tvBankRevalidate.setText("");
            }


            // tvBankRevalidate.setText(data.getBank_details().getBank_status());

            if (data.getPan_detail().getPan_reason().size() > 0) {
                tvPanRevalidateReason.setVisibility(View.VISIBLE);
            } else {
                tvPanRevalidateReason.setVisibility(GONE);
            }

            if (data.getBank_details().getBank_reason().size() > 0) {
                tvBankRevalidateReason.setVisibility(View.VISIBLE);
            } else {
                tvBankRevalidateReason.setVisibility(GONE);
            }

            strPanReason = "";
            if(data.getPan_detail().getPan_reason().size() > 0) {
                for (int i = 0; i <= data.getPan_detail().getPan_reason().size(); i++) {
                    strPanReason = strPanReason + " " + (i + 1) + ") " + data.getPan_detail().getPan_reason().get(i) + "\n ";
                    //Log.d("Values", "setProfileData 1: " + data.getPan_detail().getPan_reason().get(i));
                }
            }


        }
    }

    private void setData(String info, String reasonTitle) {

        UserType userType = new GulfOilUtils().getUserType();


        if (userType == UserType.ROYAL) {
            new GulfUnnatiDialog(getActivity(), UserType.ROYAL)
                    .setTitle(reasonTitle)
                    .hideDialogCloseButton(false)
                    .setDescription(info)
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                    })
                    .show();
        } else if (userType == UserType.ELITE) {
            new GulfUnnatiDialog(getActivity(), UserType.SIMPLE)
                    .setTitle(reasonTitle)
                    .hideDialogCloseButton(false)
                    .setDescription(info)
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                    })
                    .show();
        } else if (userType == UserType.CLUB) {
            new GulfUnnatiDialog(getActivity(), UserType.SIMPLE)
                    .setTitle(reasonTitle)
                    .hideDialogCloseButton(false)
                    .setDescription(info)
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                    })
                    .show();
        } else {
            new GulfUnnatiDialog(getActivity(), UserType.SIMPLE)
                    .setTitle(reasonTitle)
                    .hideDialogCloseButton(false)
                    .setDescription(info)
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                    })
                    .show();
        }

    }

    private void getBankMasterDataAPI() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getBankMasterData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::bankMasterDataResponse, this::bankMasterDataError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void bankMasterDataError(Throwable throwable) {
        progressDialog.dismiss();
        /*new GulfUnnatiDialog(getActivity())
                .setTitle(getString(R.string.str_error))
                .hideDialogCloseButton(true)
                .setDescription(getString(R.string.something_went_wrong))
                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                    dialog.dismiss();
                    Navigation.findNavController(getView()).popBackStack();
                })
                .show();*/
    }

    private void bankMasterDataResponse(BankMasterResponse response) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            bankMasterData = response;
            setBankSpinners();
        } else {
            new GulfUnnatiDialog(getActivity())
                    .setTitle(getString(R.string.str_error))
                    .hideDialogCloseButton(true)
                    .setDescription(response.getError())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        Navigation.findNavController(getView()).popBackStack();
                    })
                    .show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivUpdateProfileImage:
                ImgSelectionDialog(PROFILE_IMG_REQUEST_CODE, ivProfileImage);
                break;
            case R.id.ivBasicDetailsDropDown:
                if (llBasicDetailsForm.getVisibility() == View.VISIBLE) {
                    ivBasicDetailsDropDown.animate().rotationX(180).setDuration(800).start();
                    llBasicDetailsForm.setVisibility(GONE);
                } else {
                    llBasicDetailsForm.setVisibility(View.VISIBLE);
                    ivBasicDetailsDropDown.animate().rotationX(0).setDuration(800).start();
                }
                break;
            case R.id.ivProfileImage:
                if (!TextUtils.isEmpty(profileImgLocalPath)) {
                    showFullScreenImage(profileImgLocalPath, "Profile Image", true);
                } else {
                    if (!TextUtils.isEmpty(participantProfileResponse.getData().getView_profile())) {
                        showFullScreenImage(participantProfileResponse.getData().getView_profile(), "Profile Image", false);
                    }
                }
                break;
            case R.id.tvViewPanCardCopy:
                if (!TextUtils.isEmpty(panCardImgLocalPath)) {
                    showFullScreenImage(panCardImgLocalPath, "Pan Card Image", true);
                } else {
                    if (!TextUtils.isEmpty(participantProfileResponse.getData().getPan_detail().getView_pan())) {
                        showFullScreenImage(participantProfileResponse.getData().getPan_detail().getView_pan(), "Pan Card Image", false);
                    }
                }
                break;
            case R.id.btnChoosePanCard:
                ImgSelectionDialog(PAN_CARD_IMG_REQUEST_CODE, null);
                break;
            case R.id.ivBankDetailsDropDown:
                if (llBankDetailsForm.getVisibility() == View.VISIBLE) {
                    ivBankDetailsDropDown.animate().rotationX(180).setDuration(800).start();
                    llBankDetailsForm.setVisibility(GONE);
                } else {
                    llBankDetailsForm.setVisibility(View.VISIBLE);
                    ivBankDetailsDropDown.animate().rotationX(0).setDuration(800).start();
                }
                break;
            case R.id.btnChooseCancelCheque:
                ImgSelectionDialog(CANCELLED_CHEQUE_CARD_IMG_REQUEST_CODE, null);
                break;
            case R.id.tvViewCancelChequeCopy:
                if (!TextUtils.isEmpty(cancelledChequeImgLocalPath)) {
                    showFullScreenImage(cancelledChequeImgLocalPath, "Cancelled Cheque Image", true);
                } else {
                    if (!TextUtils.isEmpty(participantProfileResponse.getData().getBank_details().getView_cheque_image())) {
                        showFullScreenImage(participantProfileResponse.getData().getBank_details().getView_cheque_image(), "Cancelled Cheque Image", false);
                    }
                }
                break;
            case R.id.btnCancel:
                Navigation.findNavController(v).popBackStack();
                break;
            case R.id.btnSubmit:
                if (validateFields()) {
                    callUpdateProfile();
                }
                break;
            case R.id.tvPanRevalidateReason:
                setData(strPanReason, "Pan Validate Reason");
                break;

            case R.id.tvBankRevalidateReason:
                setData(strBankReason, "Bank Validate Reason");
                break;


            case R.id.ivPanDetailsDropDown:
                if (ll_pan_data.getVisibility() == View.VISIBLE) {
                    ll_pan_data.setVisibility(View.GONE);
                    ivPanDetailsDropDown.animate().rotationX(180).setDuration(800).start();
                } else {
                    ll_pan_data.setVisibility(View.VISIBLE);
                    ivPanDetailsDropDown.animate().rotationX(180).setDuration(800).start();
                }

                break;
        }
    }

    private void setBankSpinners() {
        List<String> bankList = null;
        if (bankMasterData.getData().getBank_list() != null) {
            bankList = bankMasterData.getData().getBank_list();
            bankList.add(0, "Select Bank Name");
            spnBankName.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item, bankList));
        }
        List<String> accountTypeList = null;
        if (bankMasterData.getData().getAccount_type() != null) {
            accountTypeList = bankMasterData.getData().getAccount_type();
            accountTypeList.add(0, "Select Account Type");
            spnBankType.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item, accountTypeList));
        }

        if (bankList != null && bankList.size() > 0 && !TextUtils.isEmpty(participantProfileResponse.getData().getBank_details().getBank_name())) {
            for (int i = 0; i < bankList.size(); i++) {
                if (bankList.get(i).equals(participantProfileResponse.getData().getBank_details().getBank_name())) {
                    spnBankName.setSelection(i);
                    break;
                }
            }
        }

        if (accountTypeList != null && accountTypeList.size() > 0 && !TextUtils.isEmpty(participantProfileResponse.getData().getBank_details().getAccount_type())) {
            for (int i = 0; i < accountTypeList.size(); i++) {
                if (accountTypeList.get(i).equals(participantProfileResponse.getData().getBank_details().getAccount_type())) {
                    spnBankType.setSelection(i);
                    break;
                }
            }
        }

    }

    private boolean validateFields() {
        boolean result = true;

        tilEmailName.setError(null);
        tilPanCardNo.setError(null);
        tilPanImage.setError(null);
        tvErrorBankName.setVisibility(GONE);
        tvErrorBankType.setVisibility(GONE);
        tilAccountHolderName.setError(null);
        tilAccountNumber.setError(null);
        tilIFSCCode.setError(null);
        tilBranchName.setError(null);
        tilCancelledChequeImage.setError(null);

        if (!GulfValidator.isEmpty(ViewUtils.getText(edtEmail)) && !GulfValidator.isValidEmail(ViewUtils.getText(edtEmail))) {
            result = false;
            tilEmailName.requestFocus();
            tilEmailName.setError("Enter valid email id");
        }
//         else if (GulfValidator.isEmpty(ViewUtils.getText(edtPanCardNoCode))) {
//            result = false;
//            tilPanCardNo.requestFocus();
//            tilPanCardNo.setError("Enter pan card no");
//        } else if (!GulfValidator.isValidPanCardNumber(ViewUtils.getText(edtPanCardNoCode))) {
//            result = false;
//            tilPanCardNo.requestFocus();
//            tilPanCardNo.setError("Enter valid pan card no");
//        } else if (GulfValidator.isEmpty(ViewUtils.getText(edtPanImage))) {
//            result = false;
//            tilPanImage.requestFocus();
//            tilPanImage.setError("Select pan card image");
//        } else if (!GulfValidator.isValidSpinner(spnBankName.getSelectedItemPosition())) {
//            result = false;
//            spnBankName.requestFocus();
//            tvErrorBankName.setVisibility(View.VISIBLE);
//            tvErrorBankName.setError("Select bank name");
//        } else if (!GulfValidator.isValidSpinner(spnBankType.getSelectedItemPosition())) {
//            result = false;
//            spnBankType.requestFocus();
//            tvErrorBankType.setVisibility(View.VISIBLE);
//            tvErrorBankType.setText("Select bank type");
//        } else if (GulfValidator.isEmpty(ViewUtils.getText(edtAccountHolderName))) {
//            result = false;
//            tilAccountHolderName.requestFocus();
//            tilAccountHolderName.setError("Enter account holder name");
//        } else if (GulfValidator.isEmpty(ViewUtils.getText(edtAccountNumber))) {
//            result = false;
//            tilAccountNumber.requestFocus();
//            tilAccountNumber.setError("Enter account number");
//        } else if (GulfValidator.isEmpty(ViewUtils.getText(edtIFSCCode))) {
//            result = false;
//            tilIFSCCode.requestFocus();
//            tilIFSCCode.setError("Enter IFSC code");
//        } else if (GulfValidator.isEmpty(ViewUtils.getText(edtBranchName))) {
//            result = false;
//            tilBranchName.requestFocus();
//            tilBranchName.setError("Enter branch name");
//        } else if (GulfValidator.isEmpty(ViewUtils.getText(edtCancelledChequeImage))) {
//            result = false;
//            tilCancelledChequeImage.requestFocus();
//            tilCancelledChequeImage.setError("Select cancelled cheque image");
//        }

        return result;
    }

    private void callUpdateProfile() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            Map<String, RequestBody> partMap = new HashMap<>();
            partMap.put("participant_login_id", RetrofitUtils.createPartFromString(userPref.getString("usertrno", "")));
            partMap.put("uid", RetrofitUtils.createPartFromString(ViewUtils.getText(edtUIN)));
            partMap.put("mobile_no", RetrofitUtils.createPartFromString(ViewUtils.getText(edtMobile)));
            partMap.put("retailer_code", RetrofitUtils.createPartFromString(ViewUtils.getText(edtRetailerCode)));
            partMap.put("full_name", RetrofitUtils.createPartFromString(ViewUtils.getText(edtFullName)));
            partMap.put("email_id", RetrofitUtils.createPartFromString(ViewUtils.getText(edtEmail)));
            partMap.put("pan_name", RetrofitUtils.createPartFromString(ViewUtils.getText(edtPanCardName)));

            MultipartBody.Part panCardImg = null;
            if (!participantProfileResponse.getData().getPan_detail().isIs_readonly()) {
                partMap.put("pan", RetrofitUtils.createPartFromString(ViewUtils.getText(edtPanCardNoCode)));
                panCardImg = TextUtils.isEmpty(panCardImgLocalPath) ? null : RetrofitUtils.prepareFilePart("pan_image", panCardImgLocalPath);
            }

            MultipartBody.Part cancelChequeImg = null;
            if (!participantProfileResponse.getData().getBank_details().isIs_readonly()) {
                String bankName = "";
                if (spnBankName.getSelectedItemPosition() > 0) {
                    if (ViewUtils.getText(spnBankName).equals("Other")) {
                        bankName = ViewUtils.getText(edtOtherBankName);
                    } else {
                        bankName = ViewUtils.getText(spnBankName);

                    }
                }

                String bankType = "";
                if (spnBankType.getSelectedItemPosition() > 0) {
                    bankType = ViewUtils.getText(spnBankType);
                }

                partMap.put("bank_name", RetrofitUtils.createPartFromString(bankName));
                partMap.put("account_type", RetrofitUtils.createPartFromString(bankType));
                partMap.put("account_holder_name", RetrofitUtils.createPartFromString(ViewUtils.getText(edtAccountHolderName)));
                partMap.put("account_number", RetrofitUtils.createPartFromString(ViewUtils.getText(edtAccountNumber)));
                partMap.put("ifsc_code", RetrofitUtils.createPartFromString(ViewUtils.getText(edtIFSCCode)));
                partMap.put("branch_name", RetrofitUtils.createPartFromString(ViewUtils.getText(edtBranchName)));
                cancelChequeImg = TextUtils.isEmpty(cancelledChequeImgLocalPath) ? null : RetrofitUtils.prepareFilePart("cheque_image", cancelledChequeImgLocalPath);
            }

            MultipartBody.Part profileImg = TextUtils.isEmpty(profileImgLocalPath) ? null : RetrofitUtils.prepareFilePart("profile_image", profileImgLocalPath);


            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .updateParticipantProfile(partMap, panCardImg, profileImg, cancelChequeImg)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateParticipantProfileResponse, this::updateParticipantProfileError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateParticipantProfileResponse(UpdateParticipantProfileResponse updateParticipantProfileResponse) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(updateParticipantProfileResponse.getStatus())) {
            new GulfUnnatiDialog(getActivity())
                    .setTitle(getString(R.string.str_success))
                    .hideDialogCloseButton(true)
                    .setDescription(updateParticipantProfileResponse.getMessage())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        Navigation.findNavController(getView()).navigate(R.id.updateProfileOTPFragment);
                    })
                    .show();

        } else {
            new GulfUnnatiDialog(getActivity())
                    .setTitle(getString(R.string.str_error))
                    .hideDialogCloseButton(true)
                    .setDescription(updateParticipantProfileResponse.getError())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                    })
                    .show();
        }
    }

    private void updateParticipantProfileError(Throwable throwable) {
        progressDialog.dismiss();
        new GulfUnnatiDialog(getActivity())
                .setTitle(getString(R.string.str_error))
                .hideDialogCloseButton(true)
                .setDescription(getString(R.string.something_went_wrong))
                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                    dialog.dismiss();
                })
                .show();
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_participant_profile_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_participant_profile_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_participant_profile_club;
        else
            return R.layout.fragment_participant_profile_club;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (ViewUtils.getText(spnBankName).equals("Other")) {
            Toast.makeText(getActivity(), "Other", Toast.LENGTH_SHORT).show();
            rl_other_bank_name.setVisibility(View.VISIBLE);
        } else {
            rl_other_bank_name.setVisibility(GONE);
            edtOtherBankName.setText("");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}