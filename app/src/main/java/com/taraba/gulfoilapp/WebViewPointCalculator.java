package com.taraba.gulfoilapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.util.GulfOilUtils;

public class WebViewPointCalculator extends Fragment {
    private WebView wvHtmlView;
    private ProgressBar progressBarHtmlView;

    public WebViewPointCalculator() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view_point_calculator, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        wvHtmlView = (WebView) view.findViewById(R.id.wvHtmlView);
        progressBarHtmlView = (ProgressBar) view.findViewById(R.id.progressBarHtmlView);

        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });


        displayWebview(Constants.POINT_CALCULATOR_URL);

    }

    private void displayWebview(String html_url) {
        wvHtmlView.loadUrl(html_url);
        wvHtmlView.setWebViewClient(new WebViewClient());
       // wvHtmlView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
       // wvHtmlView.getSettings().setAppCachePath(getActivity().getCacheDir().getAbsolutePath());
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


}
