package com.taraba.gulfoilapp.ui.help.tnc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.TnCResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class NewTermsAndConditionsFragment extends Fragment {

    private Disposable disposable;
    private SharedPreferences userPref;
    private ProgressDialog progressDialog;
    private WebView wvHtmlView;
    private String strTNC;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_tnc, container, false);
        init(view);
        callTNCAPI();
        return view;
    }


    private void init(View view) {
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        wvHtmlView = view.findViewById(R.id.wvHtmlView);

    }

    public static void webViewLoadData(WebView web, String html) {
        StringBuilder buf = new StringBuilder(html.length());
        for (char c : html.toCharArray()) {
            switch (c) {
                case '#':
                    buf.append("%23");
                    break;
                case '%':
                    buf.append("%25");
                    break;
                case '\'':
                    buf.append("%27");
                    break;
                case '?':
                    buf.append("%3f");
                    break;
                default:
                    buf.append(c);
                    break;
            }
        }
        web.loadData(buf.toString(), "text/html", "utf-8");
    }


    private void callTNCAPI() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getTnC()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::tncResponse, this::tncError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void tncError(Throwable throwable) {
        progressDialog.dismiss();
        new GulfUnnatiDialog(getActivity())
                .setTitle(getString(R.string.str_error))
                .hideDialogCloseButton(true)
                .setDescription(getString(R.string.something_went_wrong))
                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                    dialog.dismiss();
                    getActivity().finish();
                })
                .show();
    }

    private void tncResponse(TnCResponse response) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            strTNC = response.getData();
            setDataToView();
        } else {
            new GulfUnnatiDialog(getActivity())
                    .setTitle(getString(R.string.str_error))
                    .hideDialogCloseButton(true)
                    .setDescription(getString(R.string.something_went_wrong))
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        getActivity().finish();
                    })
                    .show();
        }
    }

    private void setDataToView() {
        //String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
//        wvHtmlView.loadData(strTNC, "text/html", "utf-8");
        webViewLoadData(wvHtmlView, strTNC);

    }
}
