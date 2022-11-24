package com.taraba.gulfoilapp.royalty_user_view.tally_invoice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.contentproviders.Database;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.util.UserFunctions;
import com.taraba.gulfoilapp.volleyRequest.BitmapHelper;
import com.taraba.gulfoilapp.volleyRequest.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RoyaltyTallyInvoiceUploadFragment extends Fragment implements View.OnClickListener {
    private static final String MECH_TRNO = "mech_trno";
    private static final int PERMISSION_REQUEST_CODE = 200;
    private final int CHOOSE_FILE_REQUESTCODE_FROM_CAMERA_INVOICE = 100;
    private final int CHOOSE_FILE_REQUESTCODE_FROM_GALLERY_INVOICE = 200;
    Button btn_invoice_1;
    Button btn_invoice_2;
    Button btn_invoice_3;
    Button btn_submit_tally_invoice;
    ProgressDialog dialog = null;
    EditText edt_invoice_1;
    EditText edt_invoice_2;
    EditText edt_invoice_3;
    private String invoicePath1 = "";
    private String invoicePath2 = "";
    private String invoicePath3 = "";
    int mech_trno = 0;
    View rootView;
    int serverResponseCode = 0;
    String upLoadServerUri = null;

    public static RoyaltyTallyInvoiceUploadFragment newInstance(int i) {
        RoyaltyTallyInvoiceUploadFragment royaltyTallyInvoiceUploadFragment = new RoyaltyTallyInvoiceUploadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MECH_TRNO, i);
        royaltyTallyInvoiceUploadFragment.setArguments(bundle);
        return royaltyTallyInvoiceUploadFragment;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.rootView = layoutInflater.inflate(R.layout.fragment_royalty_tally_invoice_upload, viewGroup, false);
        initView();
        return this.rootView;
    }

    public void initView() {
        this.btn_invoice_1 = (Button) this.rootView.findViewById(R.id.btn_invoice_1);
        this.btn_invoice_2 = (Button) this.rootView.findViewById(R.id.btn_invoice_2);
        this.btn_invoice_3 = (Button) this.rootView.findViewById(R.id.btn_invoice_3);
        this.btn_submit_tally_invoice = (Button) this.rootView.findViewById(R.id.btn_submit_tally_invoice);
        this.btn_invoice_1.setOnClickListener(this);
        this.btn_invoice_2.setOnClickListener(this);
        this.btn_invoice_3.setOnClickListener(this);
        this.btn_submit_tally_invoice.setOnClickListener(this);
        this.edt_invoice_1 = (EditText) this.rootView.findViewById(R.id.edt_invoice_1);
        this.edt_invoice_2 = (EditText) this.rootView.findViewById(R.id.edt_invoice_2);
        this.edt_invoice_3 = (EditText) this.rootView.findViewById(R.id.edt_invoice_3);
        if (this.mech_trno == 0) {
            this.mech_trno = getActivity().getSharedPreferences("userinfo", 0).getInt("Mechanic_trno_to_redeem", 0);
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btn_submit_tally_invoice) {
            switch (id) {
                case R.id.btn_invoice_1:
                    if (Build.VERSION.SDK_INT < 23) {
                        selectImage(1);
                        return;
                    } else if (checkPermission()) {
                        selectImage(1);
                        return;
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 200);
                        return;
                    }
                case R.id.btn_invoice_2:
                    if (Build.VERSION.SDK_INT < 23) {
                        selectImage(2);
                        return;
                    } else if (checkPermission()) {
                        selectImage(2);
                        return;
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 200);
                        return;
                    }
                case R.id.btn_invoice_3:
                    if (Build.VERSION.SDK_INT < 23) {
                        selectImage(3);
                        return;
                    } else if (checkPermission()) {
                        selectImage(3);
                        return;
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 200);
                        return;
                    }
                default:
                    return;
            }
        } else if (validateFields()) {
            uploadDocument(this.invoicePath1, this.invoicePath2, this.invoicePath3);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            int i3 = 0;
            if (i == 101) {
                File[] listFiles = new File(Environment.getExternalStorageDirectory().toString()).listFiles();
                int length = listFiles.length;
                while (i3 < length && !listFiles[i3].getName().equals("temp.jpg")) {
                    i3++;
                }
                try {
                    BitmapHelper bitmapHelper = new BitmapHelper();
                    FragmentActivity activity = getActivity();
                    String compressImage = bitmapHelper.compressImage(activity, Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                    File file = new File(compressImage);
                    this.invoicePath1 = compressImage;
                    this.edt_invoice_1.setText(file.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (i == 201) {
                try {
                    String[] strArr = {"_data"};
                    Cursor query = getActivity().getContentResolver().query(intent.getData(), strArr, (String) null, (String[]) null, (String) null);
                    if (query.moveToFirst()) {
                        String string = query.getString(query.getColumnIndex(strArr[0]));
                        this.invoicePath1 = string;
                        this.edt_invoice_1.setText(string.substring(string.lastIndexOf("/")).replace("/", ""));
                    }
                    query.close();
                } catch (Exception unused) {
                    Uri data = intent.getData();
                    this.invoicePath1 = data.getPath();
                    this.edt_invoice_1.setText(new File(data.getPath()).getName());
                }
            } else if (i == 102) {
                try {
                    BitmapHelper bitmapHelper2 = new BitmapHelper();
                    FragmentActivity activity2 = getActivity();
                    String compressImage2 = bitmapHelper2.compressImage(activity2, Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                    this.invoicePath2 = compressImage2;
                    this.edt_invoice_2.setText(new File(compressImage2).getName());
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else if (i == 202) {
                try {
                    String[] strArr2 = {"_data"};
                    Cursor query2 = getActivity().getContentResolver().query(intent.getData(), strArr2, (String) null, (String[]) null, (String) null);
                    if (query2.moveToFirst()) {
                        String string2 = query2.getString(query2.getColumnIndex(strArr2[0]));
                        this.invoicePath2 = string2;
                        this.edt_invoice_2.setText(string2.substring(string2.lastIndexOf("/")).replace("/", ""));
                    }
                    query2.close();
                } catch (Exception unused2) {
                    Uri data2 = intent.getData();
                    this.invoicePath2 = data2.getPath();
                    this.edt_invoice_2.setText(new File(data2.getPath()).getName());
                }
            } else if (i == 103) {
                try {
                    BitmapHelper bitmapHelper3 = new BitmapHelper();
                    FragmentActivity activity3 = getActivity();
                    String compressImage3 = bitmapHelper3.compressImage(activity3, Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                    this.invoicePath3 = compressImage3;
                    this.edt_invoice_3.setText(new File(compressImage3).getName());
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            } else if (i == 203) {
                try {
                    String[] strArr3 = {"_data"};
                    Cursor query3 = getActivity().getContentResolver().query(intent.getData(), strArr3, (String) null, (String[]) null, (String) null);
                    if (query3.moveToFirst()) {
                        String string3 = query3.getString(query3.getColumnIndex(strArr3[0]));
                        this.invoicePath3 = string3;
                        this.edt_invoice_3.setText(string3.substring(string3.lastIndexOf("/")).replace("/", ""));
                    }
                    query3.close();
                } catch (Exception unused3) {
                    Uri data3 = intent.getData();
                    this.invoicePath3 = data3.getPath();
                    this.edt_invoice_3.setText(new File(data3.getPath()).getName());
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void takePicture(int i) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", FileProvider.getUriForFile(getActivity(), "com.taraba.gulfoilapp.provider", new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg")));
        startActivityForResult(intent, i);
    }

    private void selectImage(final int i) {
        final CharSequence[] charSequenceArr = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle((CharSequence) "Add Photo!");
        builder.setItems(charSequenceArr, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (charSequenceArr[i].equals("Take Photo")) {
                    RoyaltyTallyInvoiceUploadFragment.this.takePicture(i + 100);
                } else if (charSequenceArr[i].equals("Choose from Gallery")) {
                    Intent intent = new Intent("android.intent.action.PICK");
                    intent.setType("*/*");
                    RoyaltyTallyInvoiceUploadFragment.this.startActivityForResult(intent, i + 200);
                }
            }
        });
        builder.show();
    }

    /* access modifiers changed from: package-private */
    public void uploadDocument(String str, String str2, String str3) {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final String str4 = str;
            final String str5 = str2;
            final String str6 = str3;
            VolleyMultipartRequest r3 = new VolleyMultipartRequest(1, UserFunctions.webservice_URL_upload_tally_invoice, new Response.Listener<NetworkResponse>() {
                public void onResponse(NetworkResponse networkResponse) {
                    JSONObject jSONObject = new JSONObject();
                    try {
                        JSONObject jSONObject2 = new JSONObject(new String(networkResponse.data));
                        try {
                            if (jSONObject2.getString("status").equalsIgnoreCase("success")) {
                                progressDialog.dismiss();
                                RoyaltyTallyInvoiceUploadFragment royaltyTallyInvoiceUploadFragment = RoyaltyTallyInvoiceUploadFragment.this;
                                royaltyTallyInvoiceUploadFragment.showDialog(royaltyTallyInvoiceUploadFragment.getActivity());
                                return;
                            }
                            new CustomOKDialog(RoyaltyTallyInvoiceUploadFragment.this.getActivity(), jSONObject2.getString("error"), "Gulf Oil").show();
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e = e;
                            jSONObject = jSONObject2;
                            try {
                                new CustomOKDialog(RoyaltyTallyInvoiceUploadFragment.this.getActivity(), jSONObject.getString("message"), "Gulf Oil").show();
                                e.printStackTrace();
                                progressDialog.dismiss();
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                    } catch (JSONException e3) {

                        new CustomOKDialog(RoyaltyTallyInvoiceUploadFragment.this.getActivity(), jSONObject.optString("message"), "Gulf Oil").show();
                        e3.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                    Toast.makeText(RoyaltyTallyInvoiceUploadFragment.this.getActivity(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                public Map<String, String> getHeaders() {
                    HashMap hashMap = new HashMap();
                    hashMap.put("Authorization", "Basic " + Base64.encodeToString("unnati:4us*uvuFrAS&".getBytes(), 10));
                    hashMap.put(Database.HISTRY_PARTICIPANT_ID_COL, "" + RoyaltyTallyInvoiceUploadFragment.this.mech_trno);
                    return hashMap;
                }

                /* access modifiers changed from: protected */
                public Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                    HashMap hashMap = new HashMap();
                    try {
                        hashMap.put("invoice_1", new VolleyMultipartRequest.DataPart(RoyaltyTallyInvoiceUploadFragment.this.edt_invoice_1.getText().toString().trim(), new BitmapHelper().fullyReadFileToBytes(new File(str4))));
                        String str = str5;
                        if (str != null && !str.isEmpty()) {
                            hashMap.put("invoice_2", new VolleyMultipartRequest.DataPart(RoyaltyTallyInvoiceUploadFragment.this.edt_invoice_2.getText().toString().trim(), new BitmapHelper().fullyReadFileToBytes(new File(str5))));
                        }
                        String str2 = str6;
                        if (str2 != null && !str2.isEmpty()) {
                            hashMap.put("invoice_3", new VolleyMultipartRequest.DataPart(RoyaltyTallyInvoiceUploadFragment.this.edt_invoice_3.getText().toString().trim(), new BitmapHelper().fullyReadFileToBytes(new File(str6))));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return hashMap;
                }
            };
            r3.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1.0f));
            Volley.newRequestQueue(getActivity()).add(r3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        if (this.edt_invoice_1.getText().toString().trim().length() != 0) {
            return true;
        }
        Toast.makeText(getActivity(), "Please select invoice 1", Toast.LENGTH_SHORT).show();
        return false;
    }

    private String getExtension(String str) {
        try {
            return str.substring(str.lastIndexOf("."));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void showDialog(Activity activity) {
        final Dialog dialog2 = new Dialog(activity);
        dialog2.requestWindowFeature(1);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.success_dialog);
        ((TextView) dialog2.findViewById(R.id.textViewSuccess)).setTextColor(getActivity().getResources().getColor(R.color.gold_color));
        ((TextView) dialog2.findViewById(R.id.textViewMessage)).setTextColor(getActivity().getResources().getColor(R.color.gold_color));
        Button button = (Button) dialog2.findViewById(R.id.btn_ok);
        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.gold_btn_bg));
        button.setTextColor(getResources().getColor(R.color.black));
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RoyaltyTallyInvoiceUploadFragment.this.getFragmentManager().popBackStack();
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), "android.permission.CAMERA") == 0 && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 200 && iArr.length > 0) {
            boolean z = true;
            boolean z2 = iArr[1] == 0;
            if (iArr[0] != 0) {
                z = false;
            }
            if (!z || !z2) {
                Toast.makeText(getActivity(), "Permission Denied, You cannot access camera.", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= 23 && shouldShowRequestPermissionRationale("android.permission.CAMERA")) {
                    showMessageOKCancel("You need to allow access to both the permissions", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                RoyaltyTallyInvoiceUploadFragment.this.requestPermissions(new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 200);
                            }
                        }
                    });
                    return;
                }
                return;
            }
            Log.i("LOG_TAG", "Permission Granted, Now you can access camera.");
        }
    }

    private void showMessageOKCancel(String str, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(getActivity()).setMessage(str).setPositiveButton("OK", onClickListener).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).create().show();
    }

    private Intent getFileChooserIntent() {
        String[] strArr = {"image/*", "application/pdf"};
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        if (Build.VERSION.SDK_INT >= 19) {
            intent.setType("*/*");
            intent.putExtra("android.intent.extra.MIME_TYPES", strArr);
        } else {
            String str = "";
            for (int i = 0; i < 2; i++) {
                str = str + strArr[i] + "|";
            }
            intent.setType(str.substring(0, str.length() - 1));
        }
        return intent;
    }
}
