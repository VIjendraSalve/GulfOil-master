package com.taraba.gulfoilapp.royalty_user_view.point_calculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.PointsCalculatorURLRequest;
import com.taraba.gulfoilapp.model.PointsCalculatorURLResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.ProgressDialogHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PointCalculatorWebViewFragment extends Fragment {
    private WebView wvHtmlView;
    private ProgressBar progressBarHtmlView;
    private Disposable disposable;

    public PointCalculatorWebViewFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_royalty_web_view_point_calculator,
                container, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_point_calcuator));
        }
        init(view);
        callPointCalculatorURLAPI();
        return view;
    }

    private void init(View view) {
        wvHtmlView = view.findViewById(R.id.wvHtmlView);
        progressBarHtmlView = view.findViewById(R.id.progressBarHtmlView);

    }

    private void displayWebview(String html_url) {
        wvHtmlView.loadUrl(html_url);
        wvHtmlView.setWebViewClient(new WebViewClient());
     //   wvHtmlView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
      //  wvHtmlView.getSettings().setAppCachePath(getActivity().getCacheDir().getAbsolutePath());
       wvHtmlView.getSettings().setAllowFileAccess(true);
       // wvHtmlView.getSettings().setAppCacheEnabled(true);
        wvHtmlView.getSettings().setJavaScriptEnabled(true);
        wvHtmlView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        wvHtmlView.getSettings().setBuiltInZoomControls(true);
        wvHtmlView.getSettings().setSupportZoom(true);
        wvHtmlView.getSettings().setDisplayZoomControls(false);
        wvHtmlView.setWebViewClient(new MyWebViewClient());


        wvHtmlView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && progressBarHtmlView.getVisibility() == ProgressBar.GONE) {
                    progressBarHtmlView.setVisibility(ProgressBar.VISIBLE);
                }
                progressBarHtmlView.setProgress(progress);
                if (progress == 100) {
                    progressBarHtmlView.setVisibility(View.GONE);
                }
            }
        });
        progressBarHtmlView.setVisibility(View.GONE);
    }

    private void resetWebview(WebView webView) {
        //Clearing the WebView
        try {
            webView.stopLoading();
        } catch (Exception e) {
        }
        try {
            webView.clearView();
        } catch (Exception e) {
        }
        if (webView.canGoBack()) {
            webView.goBack();
        }
        webView.loadUrl("about:blank");
            /*new PKBDialog(getActivity(), PKBDialog.WARNING_TYPE)
                    .setContentText("Web page not available. Try again later.")
                    .setConfirmClickListener(new PKBDialog.OnPKBDialogClickListner() {
                        @Override
                        public void onClick(PKBDialog customDialog) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }).show();*/
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            resetWebview(view);
            //super.onReceivedError(view, request, error);
        }


    }


    private void callPointCalculatorURLAPI() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            ProgressDialogHelper.showProgressDialog(getActivity(), "Please wait");
            PointsCalculatorURLRequest reqest = new PointsCalculatorURLRequest();
            SharedPreferences preferences1 = getActivity().getSharedPreferences(
                    "signupdetails", getActivity().MODE_PRIVATE);
            String loginId = preferences1.getString("usertrno", "");
            reqest.setLogin_id(loginId);
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getPointsCalaculatorURL(reqest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::pointCalculatorURLResponse, this::pointCalculatorURLError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void pointCalculatorURLResponse(PointsCalculatorURLResponse response) {
        ProgressDialogHelper.hideProgressDailog();
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            if (!TextUtils.isEmpty(response.getUrl())) {
                displayWebview(response.getUrl());
            } else {
                new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                        .setTitle(getString(R.string.str_error))
                        .hideDialogCloseButton(true)
                        .setDescription(getString(R.string.str_data_not_found))
                        .setPosButtonText(getString(R.string.str_ok), dialog -> {
                            dialog.dismiss();
                            Navigation.findNavController(getView()).popBackStack();
                        })
                        .show();
            }
        } else {
            new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                    .setTitle(getString(R.string.str_error))
                    .setDescription(response.getError())
                    .hideDialogCloseButton(true)
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        Navigation.findNavController(getView()).popBackStack();
                    })
                    .show();
        }
    }

    private void pointCalculatorURLError(Throwable throwable) {
        ProgressDialogHelper.hideProgressDailog();
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                .setTitle(getString(R.string.str_error))
                .hideDialogCloseButton(true)
                .setDescription(getString(R.string.something_went_wrong))
                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                    dialog.dismiss();
                    Navigation.findNavController(getView()).popBackStack();
                })
                .show();
    }


}
