package com.taraba.gulfoilapp.view;

import static android.os.Build.VERSION_CODES.N;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.FileUtils;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class FullScreenImageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FullScreenImageActivity";
    private PhotoView pv_image;
    private String imageURL, pdfurl;
    private ProgressBar progressBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private AppCompatButton btnDownload;
    private boolean isBrochure;
    private ProgressDialog progressDialog;
    private Disposable disposable;
    private boolean isLocalImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutUserWise());
        init();
        setListener();
        loadImage();
    }

    private void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadImage() {
        if (isLocalImg) {
            Picasso.with(this).load(new File(imageURL)).into(pv_image, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
        } else {
            Picasso.with(this).load(imageURL).into(pv_image, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    private void init() {
        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(this);
        pv_image = findViewById(R.id.pv_image);
        progressBar = findViewById(R.id.progress_bar);
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        imageURL = getIntent().getStringExtra("IMG_URL");
        tvTitle.setText(getIntent().getStringExtra("Title"));
        isBrochure = getIntent().getBooleanExtra("isBrochure", false);
        isLocalImg = getIntent().getBooleanExtra("isLocalImg", false);
        pdfurl = getIntent().getStringExtra("brochurePdfURL");

        if (isBrochure)
            btnDownload.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.activity_full_screen_image_royal;
        else if (userType == UserType.ELITE)
            return R.layout.activity_full_screen_image_elite;
        else if (userType == UserType.CLUB)
            return R.layout.activity_full_screen_image_club;
        else
            return R.layout.activity_full_screen_image_fls;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDownload) {
            if (TextUtils.isEmpty(pdfurl)) {
                Toast.makeText(this, "File not found...", Toast.LENGTH_SHORT).show();
                return;
            }

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
}
