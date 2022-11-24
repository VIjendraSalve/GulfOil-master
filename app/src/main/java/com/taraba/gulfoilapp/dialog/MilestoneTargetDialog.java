package com.taraba.gulfoilapp.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.model.MilestoneTargetListRequest;
import com.taraba.gulfoilapp.model.MilestoneTargetListResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MilestoneTargetDialog extends AppCompatDialog implements View.OnClickListener {
    private String title, description, posButtonText, negButtonText;
    private UserType userType;
    private OnPositiveClickListener onPositiveClickListener;
    private OnNegetiveClickListener onNegetiveClickListener;
    private AppCompatButton btnCancel, btnOk;
    private TextView tvDialogHeader, tvDescription;
    private ImageView ivDialogClose;
    private ProgressDialog progressDialog;
    private SharedPreferences userPref;
    private Disposable disposable;
    private AppCompatSpinner spnTarget;

    public MilestoneTargetDialog(Context context) {
        this(context, UserType.UNNATI);
    }

    public MilestoneTargetDialog(Context context, UserType userType) {
        super(context);
        setCancelable(false);
        this.userType = userType;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(getContentType());
        init();
        setListeners();
        showHideButtons();
        fetchMilestoneTargets();
    }

    private int getContentType() {
        int contentType = 0;
        switch (userType) {
            case ROYAL:
                contentType = R.layout.milestone_dialog_royal;
                break;
            case ELITE:
                contentType = R.layout.milestone_dialog_elite;
                break;
            case CLUB:
                contentType = R.layout.milestone_dialog_club;
                break;
            default:
                contentType = R.layout.milestone_dialog_royal;
                break;
        }
        return contentType;
    }

    private void init() {
        userPref = getContext().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        tvDialogHeader = findViewById(R.id.tvDialogHeader);
        tvDescription = findViewById(R.id.tvDescription);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);
        ivDialogClose = findViewById(R.id.ivDialogClose);
        spnTarget = findViewById(R.id.spnTarget);
    }

    private void showHideButtons() {
        if (TextUtils.isEmpty(negButtonText))
            btnCancel.setVisibility(View.GONE);
        else
            btnCancel.setText(negButtonText);

        tvDialogHeader.setText(title);
        tvDescription.setText(description);
        btnOk.setText(posButtonText);

    }

    private void setListeners() {
        ivDialogClose.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDialogClose:
                dismiss();
                break;
            case R.id.btnOk:
                if (spnTarget.getSelectedItemPosition() > 0) {
                    dismiss();
                    if (onPositiveClickListener != null)
                        onPositiveClickListener.onClick(this, spnTarget.getSelectedItem().toString());

                } else {
                    Toast.makeText(getContext(), "Please select target", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnCancel:
                if (onNegetiveClickListener != null)
                    onNegetiveClickListener.onClick(this);
                else
                    dismiss();
                break;
        }
    }

    public MilestoneTargetDialog setTitle(String title) {
        this.title = GulfOilUtils.chkNullDefaultValue(title, getContext().getString(R.string.app_name));
        return this;

    }

    public MilestoneTargetDialog setDescription(String description) {
        this.description = GulfOilUtils.chkNullDefaultValue(description, "");
        return this;
    }

    public MilestoneTargetDialog setPosButtonText(String posButtonText, OnPositiveClickListener positiveClickListener) {
        this.posButtonText = GulfOilUtils.chkNullDefaultValue(posButtonText, getContext().getString(R.string.str_ok));
        this.onPositiveClickListener = positiveClickListener;
        return this;
    }

    public MilestoneTargetDialog setNegButtonText(String negButtonText, OnNegetiveClickListener negetiveClickListener) {
        this.negButtonText = GulfOilUtils.chkNullDefaultValue(negButtonText, "");
        this.onNegetiveClickListener = negetiveClickListener;
        return this;
    }

    private void fetchMilestoneTargets() {
        if (ConnectionDetector.isNetworkAvailable(getContext())) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            MilestoneTargetListRequest request = new MilestoneTargetListRequest();
            request.setParticipant_login_id(userPref.getString("usertrno", ""));
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getMilestoneTargetList(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::milestoreTargetListResponse, this::milestoreTargetListError);
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void milestoreTargetListError(Throwable throwable) {
        progressDialog.dismiss();
        Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void milestoreTargetListResponse(MilestoneTargetListResponse response) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            tvDescription.setText(response.getTarget_title());
            List<String> targetList = response.getData();
            targetList.add(0, "Select Target (Ltrs)");
            spnTarget.setAdapter(new ArrayAdapter<>(getContext(), R.layout.list_item, targetList));
        } else {
            dismiss();
            new GulfUnnatiDialog(getContext(), new GulfOilUtils().getUserType())
                    .setTitle(getContext().getString(R.string.str_error))
                    .setDescription(response.getError())
                    .setPosButtonText(getContext().getString(R.string.str_ok), new GulfUnnatiDialog.OnPositiveClickListener() {
                        @Override
                        public void onClick(GulfUnnatiDialog dialog) {
                            dialog.dismiss();

                        }
                    })
                    .show();
        }
    }

    public interface OnPositiveClickListener {
        void onClick(MilestoneTargetDialog dialog, String selectedTarget);
    }

    public interface OnNegetiveClickListener {
        void onClick(MilestoneTargetDialog dialog);
    }


}
