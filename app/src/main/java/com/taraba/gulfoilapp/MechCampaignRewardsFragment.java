package com.taraba.gulfoilapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.messaging.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taraba.gulfoilapp.adapter.MechCampaignRewardsAdapter;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.model.CampaignRewards;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MechCampaignRewardsFragment extends Fragment {
    private static final String FROM_MECh_SEARCH = "from_mech_search";
    private static final String MECH_TRNO = "mech_trno";
    boolean fromMechSearch = true;
    ListView lst_orderHistory;
    int mech_trno = 0;
    String order_request_no = "";
    int participant_login_id = 0;

    public static MechCampaignRewardsFragment newInstance(int i, boolean z) {
        MechCampaignRewardsFragment mechCampaignRewardsFragment = new MechCampaignRewardsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MECH_TRNO, i);
        bundle.putBoolean(FROM_MECh_SEARCH, z);
        mechCampaignRewardsFragment.setArguments(bundle);
        return mechCampaignRewardsFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mech_trno = arguments.getInt(MECH_TRNO, 0);
            this.fromMechSearch = arguments.getBoolean(FROM_MECh_SEARCH, false);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_mech_campaign_rewards, viewGroup, false);
        if (this.mech_trno == 0) {
            this.mech_trno = getActivity().getSharedPreferences("userinfo", 0).getInt("Mechanic_trno_to_redeem", 0);
        }
        Log.e("", "Mechanic trno to RedeemMainFragment : " + this.mech_trno);
        this.lst_orderHistory = (ListView) inflate.findViewById(R.id.lst_order_history);
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            new GetCampaignRewards().execute(new Void[0]);
        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", 1).show();
        }
        return inflate;
    }

    public void alertDialog2(String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(str);
        builder.setMessage(str2).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    class GetCampaignRewards extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private ProgressDialog progressDialog;

        GetCampaignRewards() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            Log.e("insert :", "in pre execute of get order history");
            ProgressDialog progressDialog2 = new ProgressDialog(MechCampaignRewardsFragment.this.getActivity());
            this.progressDialog = progressDialog2;
            progressDialog2.setMessage("Please  wait!!!");
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        public JSONObject doInBackground(Void... voidArr) {
            Log.e("insert :", "in do in background of get order history");
            JSONObject jSONObject = null;
            try {
                UserFunctions userFunctions = new UserFunctions();
                jSONObject = userFunctions.getCampaignRewards("" + MechCampaignRewardsFragment.this.mech_trno);
                Log.e("", "order history json:" + jSONObject);
                return jSONObject;
            } catch (Exception unused) {
                MechCampaignRewardsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MechCampaignRewardsFragment.this.getActivity(), "Network Error...", Toast.LENGTH_LONG).show();
                    }
                });
                return jSONObject;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(JSONObject jSONObject) {
            super.onPostExecute(jSONObject);
            Log.e("insert :", "in post execute of proceed order" + jSONObject);
            this.progressDialog.dismiss();
            if (jSONObject != null) {
                try {
                    if (jSONObject.getString("status").equals("success")) {
                        JSONArray jSONArray = jSONObject.getJSONArray(Constants.ScionAnalytics.MessageType.DATA_MESSAGE);
                        MechCampaignRewardsFragment.this.lst_orderHistory.setAdapter(new MechCampaignRewardsAdapter(MechCampaignRewardsFragment.this.getActivity(), (List) new Gson().fromJson(jSONArray.toString(), new TypeToken<List<CampaignRewards>>() {
                        }.getType()), MechCampaignRewardsFragment.this.getActivity().getSupportFragmentManager(), MechCampaignRewardsFragment.this.fromMechSearch));
                        return;
                    }
                    new CustomOKDialog(MechCampaignRewardsFragment.this.getActivity(), jSONObject.getString("error"), "Gulf Oil").show();
                    this.progressDialog.dismiss();
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }
}
