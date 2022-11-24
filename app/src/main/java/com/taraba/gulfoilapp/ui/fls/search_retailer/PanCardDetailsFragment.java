package com.taraba.gulfoilapp.ui.fls.search_retailer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.taraba.gulfoilapp.FlsDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.dialog.ImageSelectionOptionDialog;
import com.taraba.gulfoilapp.model.ParticipantProfileResponse;
import com.taraba.gulfoilapp.model.SearchRetailerResponse;
import com.taraba.gulfoilapp.model.UpdateParticipantProfileResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.RetrofitUtils;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.FileUtils;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.ViewUtils;
import com.taraba.gulfoilapp.util.camera.CameraHelper;
import com.taraba.gulfoilapp.view.FullScreenImageActivity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PanCardDetailsFragment#} factory method to
 * create an instance of this fragment.
 */
public class PanCardDetailsFragment extends Fragment implements View.OnClickListener, CameraHelper.AsyncResponse {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PROFILE_IMG_REQUEST_CODE = 101;
    private static final int PAN_CARD_IMG_REQUEST_CODE = 102;
    private static final int CANCELLED_CHEQUE_CARD_IMG_REQUEST_CODE = 103;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String Pan="", Pan_name="", Pan_status="", View_pan="", Pan_reason="", participant_id="";
    private boolean Is_readonly;
    private SearchRetailerResponse response;
    private CameraHelper cameraHelper;
    private TextView tvPanRevalidate, tvViewPanCardCopy;
    private TextInputEditText
            edtPanCardNoCode,
            edtPanImage,
            edtPanCardName;
    private TextInputLayout
            tilPanCardNo,
            tilPanImage;
    private String panCardImgLocalPath;
    private ProgressDialog progressDialog;
    private TextView tvPanRevalidateReason;
    private String strPanReason = "";
    private ParticipantProfileResponse participantProfileResponse;
    private AppCompatButton btnChoosePanCard;
    private AppCompatButton btnCancel, btnSubmit;
    private Disposable disposable;
    private SharedPreferences userPref;
    private View view;

    /*public PanCardDetailsFragment() {
        // Required empty public constructor
    }*/


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PanCardDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
   /* public static PanCardDetailsFragment newInstance(String param1, String param2) {
        PanCardDetailsFragment fragment = new PanCardDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    public /* synthetic */ void lambda$init$0$updateVerifyOTPPanFragment(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("flsid", this.participant_id);
        Navigation.findNavController(getView()).navigate((int) R.id.updatePanVerifyOTPFragment, bundle);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.Pan = com.taraba.gulfoilapp.ui.fls.search_retailer.NewSearchRetailerFragmentArgs.fromBundle(getArguments()).getPan();
        this.Pan_name = com.taraba.gulfoilapp.ui.fls.search_retailer.NewSearchRetailerFragmentArgs.fromBundle(getArguments()).getPanName();
        this.Pan_reason = com.taraba.gulfoilapp.ui.fls.search_retailer.NewSearchRetailerFragmentArgs.fromBundle(getArguments()).getPanReason();
        this.Pan_status = com.taraba.gulfoilapp.ui.fls.search_retailer.NewSearchRetailerFragmentArgs.fromBundle(getArguments()).getPanStatus();
        this.View_pan = com.taraba.gulfoilapp.ui.fls.search_retailer.NewSearchRetailerFragmentArgs.fromBundle(getArguments()).getViewPan();
        this.participant_id = com.taraba.gulfoilapp.ui.fls.search_retailer.NewSearchRetailerFragmentArgs.fromBundle(getArguments()).getParticipantId();
        this.Is_readonly = com.taraba.gulfoilapp.ui.fls.search_retailer.NewSearchRetailerFragmentArgs.fromBundle(getArguments()).getIsReadonly();
        Log.e("Pan", "Search By: " + this.Pan);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pan_card_details, container, false);

        if (getActivity() instanceof FlsDashboardActivity) {
            ((FlsDashboardActivity) getActivity()).hideShowView(true);
            ((FlsDashboardActivity) getActivity()).setToolbarTitle("Pan Details");
        }

        init(view);
        setListeners();
        return view;
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
                //profileImgLocalPath = output;
                break;
            case PAN_CARD_IMG_REQUEST_CODE:
                panCardImgLocalPath = output;
                edtPanImage.setText(FileUtils.getFileNameFromURL(output));
                break;
            case CANCELLED_CHEQUE_CARD_IMG_REQUEST_CODE:
                //cancelledChequeImgLocalPath = output;
                //edtCancelledChequeImage.setText(FileUtils.getFileNameFromURL(output));
                break;
        }
    }

    private void init(View view) {

        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        cameraHelper = new CameraHelper(getActivity(), this);
        tilPanCardNo = view.findViewById(R.id.tilPanCardNo);
        edtPanCardNoCode = view.findViewById(R.id.edtPanCardNoCode);
        edtPanCardName = view.findViewById(R.id.edtPanCardName);
        tilPanImage = view.findViewById(R.id.tilPanImage);
        edtPanImage = view.findViewById(R.id.edtPanImage);
        btnChoosePanCard = view.findViewById(R.id.btnChoosePanCard);
        tvPanRevalidate = view.findViewById(R.id.tvPanRevalidate);
        tvPanRevalidateReason = view.findViewById(R.id.tvPanRevalidateReason);
        tvViewPanCardCopy = view.findViewById(R.id.tvViewPanCardCopy);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        if(Pan_status.toUpperCase().equals("KYC APPROVED")){
            tvPanRevalidate.setText(Pan_status);
            tvPanRevalidate.setTextColor(getActivity().getResources().getColor(R.color.milestone_journey_success));
        }else {
            tvPanRevalidate.setTextColor(getActivity().getResources().getColor(R.color.error));
            tvPanRevalidate.setText(Pan_status);
        }

        if(Pan_reason.length() > 1){
            tvPanRevalidateReason.setVisibility(View.VISIBLE);
        }else {
            tvPanRevalidateReason.setVisibility(View.GONE);
        }

        Log.d("PanReason", "init: "+strPanReason);

        edtPanCardNoCode.setText(Pan);
        edtPanImage.setText(View_pan);
        edtPanCardName.setText(Pan_name);
       // panCardImgLocalPath = View_pan;

    }

    private void setListeners() {

        tvPanRevalidateReason.setOnClickListener(this);
        btnChoosePanCard.setOnClickListener(this);
        tvViewPanCardCopy.setOnClickListener(this);

        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }


    private void showFullScreenImage(String profileImgLocalPath, String s, boolean isLocalImg) {
        Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
        intent.putExtra("IMG_URL", profileImgLocalPath);
        intent.putExtra(
                "Title", s);
        intent.putExtra("isLocalImg", isLocalImg);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvViewPanCardCopy:

                Log.d("View_pan", "onClick: " + panCardImgLocalPath);
                Log.d("View_pan", "onClick: " + View_pan);
                if (!TextUtils.isEmpty(panCardImgLocalPath)) {
                    showFullScreenImage(panCardImgLocalPath, "Pan Card Image", true);
                } else {
                    if (!TextUtils.isEmpty(View_pan)) {
                        showFullScreenImage(View_pan, "Pan Card Image", false);
                    }
                }
                break;
            case R.id.btnChoosePanCard:
                ImgSelectionDialog(PAN_CARD_IMG_REQUEST_CODE, null);
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
                setData(Pan_reason, "Pan Validate Reason");
                break;
        }
    }

    private boolean validateFields() {
        boolean result = true;
        tilPanCardNo.setError(null);
        tilPanImage.setError(null);
        return result;
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

    private void callUpdateProfile() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Map<String, RequestBody> partMap = new HashMap<>();

            Log.d("inPutParameter", "usertrno: "+userPref.getString("usertrno", ""));
            Log.d("inPutParameter", "participant_id: "+participant_id);
            Log.d("inPutParameter", "panCardImgLocalPath: "+panCardImgLocalPath);
            Log.d("inPutParameter", "edtPanCardNoCode: "+ViewUtils.getText(edtPanCardNoCode));
            Log.d("inPutParameter", "edtPanCardName: "+ViewUtils.getText(edtPanCardName));


            partMap.put("fls_login_id", RetrofitUtils.createPartFromString(userPref.getString("usertrno", "")));
            partMap.put("participant_login_id", RetrofitUtils.createPartFromString(participant_id));
            partMap.put("pan", RetrofitUtils.createPartFromString(ViewUtils.getText(edtPanCardNoCode)));
            partMap.put("pan_name", RetrofitUtils.createPartFromString(ViewUtils.getText(edtPanCardName)));


            MultipartBody.Part panCardImg = null;

               // partMap.put("pan", RetrofitUtils.createPartFromString(ViewUtils.getText(edtPanCardNoCode)));
                panCardImg = TextUtils.isEmpty(panCardImgLocalPath) ? null
                        : RetrofitUtils.prepareFilePart("pan_image", panCardImgLocalPath);

            Log.d("inPutParameter", "callUpdateProfile: "+partMap.toString());

            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .updatePanRetailer(partMap, panCardImg)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateParticipantProfileResponse, this::updateParticipantProfileError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateParticipantProfileResponse(UpdateParticipantProfileResponse updateParticipantProfileResponse) {
        progressDialog.dismiss();
        Log.d("Response", "updateParticipantProfileResponse: "+updateParticipantProfileResponse.getStatus());
        Log.d("Response", "updateParticipantProfileResponse: "+updateParticipantProfileResponse.getMessage());
        Log.d("Response", "updateParticipantProfileResponse: "+updateParticipantProfileResponse.getError());
        if (ServiceBuilder.isSuccess(updateParticipantProfileResponse.getStatus())) {
            new GulfUnnatiDialog(getActivity())
                    .setTitle(getString(R.string.str_success))
                    .hideDialogCloseButton(true)
                    .setDescription(updateParticipantProfileResponse.getMessage())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        //Navigation.findNavController(getView()).navigate(R.id.updatePanVerifyOTPFragment);
                        PanCardDetailsFragment.this.lambda$init$0$updateVerifyOTPPanFragment(view);
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
        Log.d("ErrorMsg", "updateParticipantProfileError: "+throwable.getMessage());
        Log.d("ErrorMsg", "updateParticipantProfileError: "+throwable.getStackTrace());
        Log.d("ErrorMsg", "updateParticipantProfileError: "+throwable.getCause());
        new GulfUnnatiDialog(getActivity())
                .setTitle(getString(R.string.str_error))
                .hideDialogCloseButton(true)
                .setDescription(getString(R.string.something_went_wrong))
                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                    dialog.dismiss();
                })
                .show();
    }


}