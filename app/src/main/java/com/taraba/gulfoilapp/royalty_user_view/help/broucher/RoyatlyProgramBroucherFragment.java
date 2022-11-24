package com.taraba.gulfoilapp.royalty_user_view.help.broucher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import java.io.File;
import java.io.IOException;
import java.util.List;

//import com.taraba.gulfoilapp.crop.Log;

/**
 * Modified by Chaitali on 19/01/2017
 */
public class RoyatlyProgramBroucherFragment extends Fragment {
    String pdfurl = UserFunctions.program_broucher_url;
    File yourDir;
    private Button btn_download_broucher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_royalty_programbroucher, container, false);
        btn_download_broucher = (Button) view.findViewById(R.id.btn_Download_Broucher);
        final String fileName = pdfurl.substring(pdfurl.lastIndexOf('/') + 1);
        Log.e("Program Broucher: ", "PDF name: " + fileName);
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, "Gulfpdf");
        folder.mkdir();
        yourDir = new File(Environment.getExternalStorageDirectory() + "/Gulfpdf/" + fileName);
        if (yourDir.exists()) {
            yourDir.delete();
            Log.e("yourDir ", "deleted");
            //btn_download_broucher.setText("View Brochure");
        }
        if (yourDir.exists()) {
            Log.e("yourDir ", "still exists");
        }
        btn_download_broucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (yourDir.exists())
                {
                    showPdf(fileName);
                }else {*/

                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    new FetDownloadFile().execute(fileName);
                } else {
                    Toast.makeText(getActivity(),
                            "Internet Disconnected", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    public void showPdf(String pdfname) {
        File file = new File(Environment.getExternalStorageDirectory() + "/Gulfpdf/" + pdfname);
        PackageManager packageManager = getActivity().getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
    }

    class FetDownloadFile extends AsyncTask<String, Void, Void> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setTitle(getResources().getString(R.string.app_name));
            pd.setMessage("Downloading File Please wait..");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            String extStorageDirectory = Environment.getExternalStorageDirectory()
                    .toString();
            File folder = new File(extStorageDirectory, "Gulfpdf");
            folder.mkdir();
            File file = new File(folder, params[0]);
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //Downloader.DownloadFile(pdfurl+""+params[0], file);
            com.taraba.gulfoilapp.util.Downloader.DownloadFile(pdfurl, file);
            showPdf(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            pd.dismiss();
            super.onPostExecute(result);
        }


    }
}

