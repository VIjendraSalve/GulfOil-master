package com.taraba.gulfoilapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.taraba.gulfoilapp.databinding.ActivityTermsAndConditionsBinding;
import com.taraba.gulfoilapp.model.TnCResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TermsAndConditionsActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityTermsAndConditionsBinding binding;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTermsAndConditionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        callTermAndConditionsAPI();

    }

    private void callTermAndConditionsAPI() {
        disposable = ServiceBuilder.getRetrofit()
                .create(GulfService.class)
                .getTnC()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::tncAPIResponse, this::tncAPIError);
    }

    private void tncAPIResponse(TnCResponse response) {
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            setValues(response.getData());
        }
    }

    private void tncAPIError(Throwable throwable) {
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void setValues(String tncText) {
        String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
        binding.wvHtmlView.loadData(String.format(text, tncText), "text/html", "utf-8");
//        binding.tvTermsAndConditionsValues.setText(Html.fromHtml(tncText));
    }

    private void setListeners() {
        binding.btnGoToLogin.setOnClickListener(this);
        binding.footerView.tvFooterTollFreeNumber.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent(this, LoginActivity.class);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGoToLogin:
                onBackPressed();
                break;
            case R.id.tvFooterTollFreeNumber:
                GulfOilUtils.callTollFree(this);
                break;
        }
    }
}