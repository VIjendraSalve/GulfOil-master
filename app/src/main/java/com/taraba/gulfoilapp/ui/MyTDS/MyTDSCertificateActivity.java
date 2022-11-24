package com.taraba.gulfoilapp.ui.MyTDS;

import static android.os.Build.VERSION_CODES.N;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.taraba.gulfoilapp.HowToUseRewardsActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.YDRDetailsRewardListAdapter;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.SubmitYDROTPResponse;
import com.taraba.gulfoilapp.model.YDRRequest;
import com.taraba.gulfoilapp.model.YDRResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.FileUtils;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MyTDSCertificateActivity extends AppCompatActivity
        implements View.OnClickListener, ViewStub.OnInflateListener{

    private ViewStub toolbar;
    private static final String TAG = "Vijendra";
    private TextView tvToolbarTitle;
    private AppCompatSpinner spnYears;
    private AppCompatSpinner spnQuantity;
    private ProgressDialog progressDialog;
    private Disposable disposable;
    ArrayList<String> yearList = new ArrayList<>();
    ArrayList<String> quaterList = new ArrayList<>();
    private MyTDSCertificateDownloadResponse myTDSCertificateDownloadResponse;
    private SharedPreferences userPref;
    private TextView tv_quatername, tv_tdsfilename, tv_tdsamount, tv_updatedon, tv_tds_deduction;
    private LinearLayout ll_tds_data, ll_tds_no_data;
    private AppCompatButton btnDownload;
    private String pdfurl="", filename="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutUserWise());
        init();
        setToolbarUserWise();
        setToolbarTitle(getString(R.string.str_my_tds_certificate));


        callYearAPI();

    }

    private void callYearAPI() {
        if (ConnectionDetector.isNetworkAvailable(MyTDSCertificateActivity.this)) {
            Log.e(TAG, "callYDRAPI: Unnati Fragment");
            progressDialog = new ProgressDialog(MyTDSCertificateActivity.this);
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getTDSData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::ydrPIResponse, this::ydrAPIError);
        } else {
            Toast.makeText(MyTDSCertificateActivity.this,
                    getString(R.string.internet_connection_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void ydrAPIError(Throwable throwable) {
        progressDialog.dismiss();
        Log.d(TAG, "ydrAPIError: "+throwable.toString());
        Toast.makeText(MyTDSCertificateActivity.this,
                R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void ydrPIResponse(MyTDSCertificate myTDSCertificate) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(myTDSCertificate.getStatus())) {
            //unnatiList = ydrResponse.getData().getUnnatiList();

            if (myTDSCertificate.getData().getPeriodOfArraylist() != null) {
                yearList = myTDSCertificate.getData().getPeriodOfArraylist();
                yearList.add(0, "Select Year");
                spnYears.setAdapter(new ArrayAdapter<String>(MyTDSCertificateActivity.this,
                        R.layout.list_item, yearList));
            }

            Log.d(TAG, "ydrPIResponse: "+myTDSCertificate.getData().getPeriodOfArraylist().get(0));

        } else {
            new GulfUnnatiDialog(MyTDSCertificateActivity.this)
                    .setTitle(getString(R.string.str_error))
                    .setDescription(myTDSCertificate.getError())
                    .setPosButtonText(getString(R.string.str_ok), null)
                    .show();
        }

    }


    private void init() {
        toolbar = findViewById(R.id.toolbar);

        tv_quatername = findViewById(R.id.tv_quatername);
        tv_tdsfilename = findViewById(R.id.tv_tdsfilename);
        tv_tdsamount = findViewById(R.id.tv_tdsamount);
        tv_updatedon = findViewById(R.id.tv_updatedon);
        ll_tds_data = findViewById(R.id.ll_tds_data);
        ll_tds_no_data = findViewById(R.id.ll_tds_no_data);
        tv_tds_deduction = findViewById(R.id.tv_tds_deduction);
        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(this);

        toolbar.setOnInflateListener(this);
        toolbar.setVisibility(View.GONE);

        quaterList.clear();
        quaterList.add("Q1");
        quaterList.add("Q2");
        quaterList.add("Q3");
        quaterList.add("Q4");

        spnYears = findViewById(R.id.spnYears);
        spnQuantity = findViewById(R.id.spnQuantity);

        quaterList.add(0, "Select Quaters");
        spnQuantity.setAdapter(new ArrayAdapter<String>(MyTDSCertificateActivity.this,
                R.layout.list_item, quaterList));

        spnQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    if(position == 1) {
                        if(!myTDSCertificateDownloadResponse.getData().getQ1().getQuarter_status().equals("failed")) {
                            ll_tds_data.setVisibility(View.VISIBLE);
                            ll_tds_no_data.setVisibility(View.GONE);
                            //Toast.makeText(MyTDSCertificateActivity.this, "" +myTDSCertificateDownloadResponse.getData().getQ1().getQuarter_status(), Toast.LENGTH_SHORT).show();

                            tv_quatername.setText(myTDSCertificateDownloadResponse.getData().getQ1().getQuarter());
                            tv_tdsfilename.setText(myTDSCertificateDownloadResponse.getData().getQ1().getTds_certificate());
                            tv_tdsamount.setText(myTDSCertificateDownloadResponse.getData().getQ1().getTds_amount());
                            tv_updatedon.setText(myTDSCertificateDownloadResponse.getData().getQ1().getUpload_date());
                            pdfurl = myTDSCertificateDownloadResponse.getData().getQ1().getTds_certificate();
                            if(pdfurl.length() > 0) {
                                filename = pdfurl.substring(pdfurl.lastIndexOf("/") + 1);
                                tv_tdsfilename.setText(filename);
                            }
                            //pdfurl = "https://www.africau.edu/images/default/sample.pdf";
                        }else {
                            //Toast.makeText(MyTDSCertificateActivity.this, "" +myTDSCertificateDownloadResponse.getData().getQ1().getQuarter_status(), Toast.LENGTH_SHORT).show();
                            ll_tds_data.setVisibility(View.GONE);
                            ll_tds_no_data.setVisibility(View.VISIBLE);

                        }
                    }else if(position == 2) {
                        if(!myTDSCertificateDownloadResponse.getData().getQ2().getQuarter_status().equals("failed")) {
                            ll_tds_data.setVisibility(View.VISIBLE);
                            ll_tds_no_data.setVisibility(View.GONE);
                        tv_quatername.setText(myTDSCertificateDownloadResponse.getData().getQ2().getQuarter());

                        tv_tdsamount.setText(myTDSCertificateDownloadResponse.getData().getQ2().getTds_amount());
                        tv_updatedon.setText(myTDSCertificateDownloadResponse.getData().getQ2().getUpload_date());
                            pdfurl = myTDSCertificateDownloadResponse.getData().getQ2().getTds_certificate();
                            if(pdfurl.length() > 0) {
                                filename = pdfurl.substring(pdfurl.lastIndexOf("/") + 1);
                                tv_tdsfilename.setText(filename);
                            }
                        }else {
                            //Toast.makeText(MyTDSCertificateActivity.this, "" +myTDSCertificateDownloadResponse.getData().getQ2().getQuarter_status(), Toast.LENGTH_SHORT).show();
                            ll_tds_data.setVisibility(View.GONE);
                            ll_tds_no_data.setVisibility(View.VISIBLE);

                        }
                    }else if(position == 3) {
                        if(!myTDSCertificateDownloadResponse.getData().getQ3().getQuarter_status().equals("failed")) {
                            ll_tds_data.setVisibility(View.VISIBLE);
                            ll_tds_no_data.setVisibility(View.GONE);
                        tv_quatername.setText(myTDSCertificateDownloadResponse.getData().getQ3().getQuarter());
                        tv_tdsfilename.setText(myTDSCertificateDownloadResponse.getData().getQ3().getTds_certificate());
                        tv_tdsamount.setText(myTDSCertificateDownloadResponse.getData().getQ3().getTds_amount());
                        tv_updatedon.setText(myTDSCertificateDownloadResponse.getData().getQ3().getUpload_date());
                            pdfurl = myTDSCertificateDownloadResponse.getData().getQ3().getTds_certificate();
                            if(pdfurl.length() > 0) {
                                filename = pdfurl.substring(pdfurl.lastIndexOf("/") + 1);
                                tv_tdsfilename.setText(filename);
                            }
                        }else {
                            //Toast.makeText(MyTDSCertificateActivity.this, "" +myTDSCertificateDownloadResponse.getData().getQ2().getQuarter_status(), Toast.LENGTH_SHORT).show();
                            ll_tds_data.setVisibility(View.GONE);
                            ll_tds_no_data.setVisibility(View.VISIBLE);

                        }
                    }else if(position == 4) {
                        if(!myTDSCertificateDownloadResponse.getData().getQ4().getQuarter_status().equals("failed")) {
                            ll_tds_data.setVisibility(View.VISIBLE);
                            ll_tds_no_data.setVisibility(View.GONE);
                        tv_quatername.setText(myTDSCertificateDownloadResponse.getData().getQ4().getQuarter());
                        tv_tdsfilename.setText(myTDSCertificateDownloadResponse.getData().getQ4().getTds_certificate());
                        tv_tdsamount.setText(myTDSCertificateDownloadResponse.getData().getQ4().getTds_amount());
                        tv_updatedon.setText(myTDSCertificateDownloadResponse.getData().getQ4().getUpload_date());
                            pdfurl = myTDSCertificateDownloadResponse.getData().getQ4().getTds_certificate();
                            if(pdfurl.length() > 0) {
                                filename = pdfurl.substring(pdfurl.lastIndexOf("/") + 1);
                                tv_tdsfilename.setText(filename);
                            }
                        }else {
                           // Toast.makeText(MyTDSCertificateActivity.this, "" +myTDSCertificateDownloadResponse.getData().getQ2().getQuarter_status(), Toast.LENGTH_SHORT).show();
                            ll_tds_data.setVisibility(View.GONE);
                            ll_tds_no_data.setVisibility(View.VISIBLE);

                        }
                    }
                }else {
                    ll_tds_no_data.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    //Toast.makeText(MyTDSCertificateActivity.this, "" + yearList.get(position).toString(), Toast.LENGTH_SHORT).show();
                    spnQuantity.setSelection(0);
                    ll_tds_no_data.setVisibility(View.GONE);
                    ll_tds_data.setVisibility(View.GONE);
                    tv_tds_deduction.setVisibility(View.VISIBLE);
                    callTDSDetailsAPI(yearList.get(position).toString());

                }else {
                    tv_tds_deduction.setVisibility(View.GONE);
                    ll_tds_no_data.setVisibility(View.GONE);
                    ll_tds_data.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void callTDSDetailsAPI(String years) {
        if (ConnectionDetector.isNetworkAvailable(MyTDSCertificateActivity.this)) {
            Log.e(TAG, "callYDRAPI: Unnati Fragment");
            progressDialog = new ProgressDialog(MyTDSCertificateActivity.this);
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            MyTDSCertificateRequest myTDSCertificateRequest = new MyTDSCertificateRequest();
            userPref = getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);
        myTDSCertificateRequest.setParticipant_login_id(userPref.getString("usertrno", ""));
            //myTDSCertificateRequest.setParticipant_login_id("39");
            myTDSCertificateRequest.setPeriod_of(years);

            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getTDSDownloadData(myTDSCertificateRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::ydrPIResponseNew, this::ydrAPIErrorNew);
        } else {
            Toast.makeText(MyTDSCertificateActivity.this,
                    getString(R.string.internet_connection_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void ydrAPIErrorNew(Throwable throwable) {
        progressDialog.dismiss();
        Log.d(TAG, "ydrAPIError: "+throwable.toString());
        Toast.makeText(MyTDSCertificateActivity.this,
                R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void ydrPIResponseNew(MyTDSCertificateDownloadResponse myTDSCertificateDownloadResponse) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(myTDSCertificateDownloadResponse.getStatus())) {
            this.myTDSCertificateDownloadResponse = myTDSCertificateDownloadResponse;
            tv_tds_deduction.setText("Total TDS Deductions "
            +myTDSCertificateDownloadResponse.getTotal_tds_amount());
            Log.d(TAG, "ydrPIResponse: "+myTDSCertificateDownloadResponse.getData().getQ1().getTds_amount());

        } else {
            new GulfUnnatiDialog(MyTDSCertificateActivity.this)
                    .setTitle(getString(R.string.str_error))
                    .setDescription(myTDSCertificateDownloadResponse.getError())
                    .setPosButtonText(getString(R.string.str_ok), null)
                    .show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.btnDownload:
                String fileName = FileUtils.getFileNameFromURL(pdfurl);
                if (!FileUtils.isFileExists(fileName))
                    downloadFile();
                else
                    showPdf(fileName);
        }
    }

    private void downloadFile() {
        if (ConnectionDetector.isNetworkAvailable(this)) {
            Log.e(TAG, "callUnnatiConnectAPI: Unnati Fragment");
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .downloadFileWithDynamicUrlSync(pdfurl)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::downloadFileResponse, this::downloadFileError);
        } else {
            Toast.makeText(this, getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadFileError(Throwable throwable) {
        progressDialog.dismiss();
        somethingWentWrongDialog();
    }

    private void somethingWentWrongDialog() {
        new GulfUnnatiDialog(this)
                .setTitle(getString(R.string.str_error))
                .hideDialogCloseButton(true)
                .setDescription(getString(R.string.something_went_wrong))
                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void downloadFileResponse(ResponseBody responseBody) {
        progressDialog.dismiss();
        try {
            final String fileName = pdfurl.substring(pdfurl.lastIndexOf('/') + 1);
            boolean writtenToDisk = FileUtils.writeResponseBodyToDisk(responseBody, fileName);
            if (writtenToDisk) {
                showPdf(fileName);
            } else {
                somethingWentWrongDialog();
            }
        } catch (Exception e) {
            somethingWentWrongDialog();
        }
    }

    public void showPdf(String pdfname) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileUtils.getLocalUri(FileUtils.BROCHURE_LOCAL_PATH + pdfname);
            intent.setDataAndType(uri, "application/pdf");
            if (Build.VERSION.SDK_INT >= N) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_my_tds_certificate_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_my_tds_certificate_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_my_tds_certificate_club;
        else
            return R.layout.fragment_my_tds_certificate_royal;
    }

    private void setToolbarUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL) {
            toolbar.setLayoutResource(R.layout.tool_bar_royal);
            toolbar.inflate();
        } else if (userType == UserType.ELITE) {
            toolbar.setLayoutResource(R.layout.tool_bar_elite);
            toolbar.inflate();
        } else if (userType == UserType.CLUB) {
            toolbar.setLayoutResource(R.layout.tool_bar_club);
            toolbar.inflate();
        } else {
            toolbar.setLayoutResource(R.layout.tool_bar_royal);
            toolbar.inflate();
        }
    }

    public void setToolbarTitle(String title) {
        tvToolbarTitle.setText(title);
    }

    @Override
    public void onInflate(ViewStub stub, View inflated) {
        inflated.findViewById(R.id.ivBack).setOnClickListener(this);
        tvToolbarTitle = inflated.findViewById(R.id.tvToolbarTitle);
    }

}