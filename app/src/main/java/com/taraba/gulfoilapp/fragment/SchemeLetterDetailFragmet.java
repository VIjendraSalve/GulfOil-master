package com.taraba.gulfoilapp.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchemeLetterDetailFragmet extends Fragment {
    private TextView tvLabelSchemeLetterDetailTitle, tvLabelSchemeLetterDetailDesc;
    private ImageView ivSchemeLetterDetailImage;
    private static final String SCHEME_ID = "id";
    private WebView webView;
    private String schemeId;

    public SchemeLetterDetailFragmet() {
        // Required empty public constructor
    }

    public static SchemeLetterDetailFragmet newInstance(String schemeId) {
        SchemeLetterDetailFragmet fragment = new SchemeLetterDetailFragmet();
        Bundle args = new Bundle();
        args.putString(SCHEME_ID, schemeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            schemeId = getArguments().getString(SCHEME_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scheme_letter_detail_fragmet, container, false);

        init(view);
        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });
        return view;
    }

    private void init(View view) {
        tvLabelSchemeLetterDetailTitle = (TextView) view.findViewById(R.id.tv_label_scheme_letter_detail_title);
        tvLabelSchemeLetterDetailDesc = (TextView) view.findViewById(R.id.tv_label_scheme_letter_detail_desc);
        ivSchemeLetterDetailImage = (ImageView) view.findViewById(R.id.iv_scheme_letter_detail_image);

        webView = (WebView) view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);


        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            new SchemeLetterDetailWS().execute(new String[]{schemeId});
        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }

    }

    class SchemeLetterDetailWS extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String[]... params) {
            JSONObject jObj = null;
            try {
                jObj = new UserFunctions().Scheme_letter_Detail_webservice("" + params[0][0]);

                Log.e("", "schemeletter Detail Response:---" + jObj);
            } catch (Exception e) {
                // TODO: handle exception
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Network Error...",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (jsonObject != null) {
                try {

                    Log.e("Json Data : ", "Json data : " + jsonObject);
                    String mStringStatus = jsonObject.getString("status");
                    if (mStringStatus.equals("success")) {
                        String schemeName = jsonObject.getJSONObject("data").getString("scheme_name");
                        String schemeDesc = jsonObject.getJSONObject("data").getString("scheme_description");

                        tvLabelSchemeLetterDetailTitle.setText(schemeName);
                        tvLabelSchemeLetterDetailDesc.setText(GulfOilUtils.fromHtml(schemeDesc));

                        /*Picasso.with(getActivity())
                                .load(schemeImage)
                                .error(R.drawable.no_photo)
                                .resize(600, 400)
                                .into(ivSchemeLetterDetailImage);*/
                        webView.loadData(schemeDesc, "text/html; charset=utf-8", "UTF-8");


                    } else {
                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jsonObject.getString("error"), "Gulf Oil");
                        cdd.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                progressDialog.dismiss();
            }
        }
    }

}
