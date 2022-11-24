package com.taraba.gulfoilapp.royalty_user_view.whats_new;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.customdialogs.CustomDialogForFragment;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.model.WhatsNew;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoyaltyWhatsNewFragment extends Fragment {
    private RecyclerView rvWhatsNew;
    private List<WhatsNew> whatsNewList;
    private RoyaltyWhatsNewAdapter whatsNewAdapter;
    private String loginID;

    public RoyaltyWhatsNewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_royalty_whats_new, container, false);
        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        loginID = preferences1.getString("usertrno", "");
//        Picasso.with(getActivity())
//                .load("http://dev.grgrewards.in/gulfoil/./uploads/1493879642Desert_thumb.jpg")
//                //.placeholder(R.drawable.placeholder)   // optional
//                //.error(R.drawable.error)      // optional
//                .resize(400,400)                        // optional
//                .into(imageView);
        init(view);
//        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GulfOilUtils.callTollFree(getActivity());
//            }
//        });
        return view;
    }

    private void init(View v) {
        whatsNewList = new ArrayList<>();
        rvWhatsNew = (RecyclerView) v.findViewById(R.id.rv_whats_new);
        rvWhatsNew.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        whatsNewAdapter = new RoyaltyWhatsNewAdapter(getActivity(), whatsNewList);
        rvWhatsNew.setAdapter(whatsNewAdapter);



        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            new WhatsNewWS().execute(new String[]{loginID, "6"});
        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }


    }


    class WhatsNewWS extends AsyncTask<String[], Void, JSONObject> implements CustomDialogForFragment.MyCallback {
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
                jObj = new UserFunctions().Whats_new_webservice(params[0][0], params[0][1]);

                Log.e("", "Whatsnew Response:---" + jObj);
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
                        JSONArray whatsNewJsonArray = jsonObject.getJSONArray("data");
                        if (whatsNewJsonArray.length() > 0) {
                            for (int i = 0; i < whatsNewJsonArray.length(); i++) {

                                whatsNewList.add(new WhatsNew(whatsNewJsonArray.getJSONObject(i)));
                            }
                            Log.e("whatsnew", "tryr" + whatsNewList);
                            whatsNewAdapter.notifyDataSetChanged();
                        } else {
                            CustomDialogForFragment customDialogForFragment = new CustomDialogForFragment(getActivity(), "" + jsonObject.get("error"), "Gulf Oil");
                            customDialogForFragment.setMyCallback(this);
                            customDialogForFragment.show();
                        }


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


        @Override
        public void onMyBackStack() {
            getActivity().onBackPressed();
        }

    }
}
