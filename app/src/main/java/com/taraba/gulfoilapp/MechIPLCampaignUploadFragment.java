package com.taraba.gulfoilapp;


import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.util.ImageLoadingUtils;
import com.taraba.gulfoilapp.util.UserFunctions;
import com.taraba.gulfoilapp.volleyRequest.BitmapHelper;
import com.taraba.gulfoilapp.volleyRequest.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MechIPLCampaignUploadFragment extends Fragment implements View.OnClickListener {


    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String MECH_TRNO = "mech_trno";
    private final int CHOOSE_FILE_REQUESTCODE_FROM_CAMERA_INVOICE = 100;
    private final int CHOOSE_FILE_REQUESTCODE_FROM_GALLERY_INVOICE = 200;
    View rootView;
    Button btn_ipl_campaign_photo, btn_submit_ipl_campaign_photo;
    EditText edt_ipl_campaign_photo;
    int mech_trno = 0;
    String iplCampaignPhotoPath = "";
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String upLoadServerUri = null;
    private TextView tv_info;
    private TableRow tr_img_selector;
    private RelativeLayout rl_ipl_campaign_img_preview;
    private ImageView iv_ipl_campaign_img_preview, iv_remove_ipl_campaign_img;
    private ImageLoadingUtils utils;
    private Bitmap ipl_campaign_img_bitmap;
    private ImageView ivToolbarLogo;
    private Toolbar toolbar;

    public MechIPLCampaignUploadFragment() {
        // Required empty public constructor
    }

    public static MechIPLCampaignUploadFragment newInstance(int mech_trno) {
        MechIPLCampaignUploadFragment mechTallyInvoiceUploadFragment = new MechIPLCampaignUploadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MECH_TRNO, mech_trno);
        mechTallyInvoiceUploadFragment.setArguments(bundle);
        return mechTallyInvoiceUploadFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_mech_ipl_campaign_upload, container, false);

        initView();
        return rootView;
    }

    public void initView() {
        btn_ipl_campaign_photo = (Button) rootView.findViewById(R.id.btn_ipl_campaign_photo);
        btn_submit_ipl_campaign_photo = (Button) rootView.findViewById(R.id.btn_submit_ipl_campaign_photo);
        tv_info = rootView.findViewById(R.id.tv_info);
        tr_img_selector = rootView.findViewById(R.id.tr_img_selector);
        rl_ipl_campaign_img_preview = rootView.findViewById(R.id.rl_ipl_campaign_img_preview);
        iv_ipl_campaign_img_preview = rootView.findViewById(R.id.iv_ipl_campaign_img_preview);
        iv_remove_ipl_campaign_img = rootView.findViewById(R.id.iv_remove_ipl_campaign_img);
        iv_remove_ipl_campaign_img.setOnClickListener(this);
        btn_ipl_campaign_photo.setOnClickListener(this);
        btn_submit_ipl_campaign_photo.setOnClickListener(this);

        edt_ipl_campaign_photo = (EditText) rootView.findViewById(R.id.edt_ipl_campaign_photo);
        utils = new ImageLoadingUtils(getActivity());

        if (mech_trno == 0) {
            SharedPreferences preferences = getActivity().getSharedPreferences(
                    "userinfo", Context.MODE_PRIVATE);

            mech_trno = preferences.getInt("Mechanic_trno_to_redeem", 0);
        }

        /*final ConnectionDetector cd = new ConnectionDetector(getActivity());
        if (cd.isConnectingToInternet()) {
            new GetUploadedInvoiceStatus().execute(new String[]{String.valueOf(mech_trno)});
        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ipl_campaign_photo:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermission()) {
                        selectImage(1);
                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{READ_EXTERNAL_STORAGE,
                                        CAMERA}, PERMISSION_REQUEST_CODE);
                    }
                } else {
                    selectImage(1);
                }

                break;
            case R.id.btn_submit_ipl_campaign_photo:
                if (validateFields()) {
                    uploadDocument(iplCampaignPhotoPath);
                }
                break;
            case R.id.iv_remove_ipl_campaign_img:
                removePreviewImage();
                break;
        }
    }

    private void removePreviewImage() {
        ipl_campaign_img_bitmap = null;
        rl_ipl_campaign_img_preview.setVisibility(View.GONE);
        tr_img_selector.setVisibility(View.VISIBLE);
        tv_info.setVisibility(View.VISIBLE);
        iplCampaignPhotoPath = "";
        edt_ipl_campaign_photo.setText("");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //Invoice 1 capturing from camera
            if (requestCode == 101) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    File fileToUploadRisk;
                    //Commented by PRAVIN DHARAM ON 18092020
//                    String filePath = new BitmapHelper().compressImage(getActivity(),
//                            Environment.getExternalStorageDirectory()
//                                    + File.separator + "image.jpg");
                    String filePath = Environment.getExternalStorageDirectory()
                            + File.separator + "image.jpg";
//                    String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
//                    String filePath = Environment.getExternalStorageDirectory()
//                                    + File.separator + "image"+timestamp+".jpg";
//
//                    String filePath1 = new BitmapHelper().getFilename();
                    new ImageCompressionAsyncTask().execute(filePath);
//                    fileToUploadRisk = new File(filePath);
//                    iplCampaignPhotoPath = filePath;
//                    ipl_campaign_img_bitmap = new BitmapHelper().grabImage(filePath);
//                    edt_ipl_campaign_photo.setText(fileToUploadRisk.getName());
//                    setPreviewImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//Invoice 1 from gallery
            else if (requestCode == 201) {
              /*  Uri filePath = data.getData();
                invoicePath1 = filePath.getPath();
                File file = new File(filePath.getPath());
                edt_invoice_1.setText(file.getName());
                Log.w("path of image", file.getName() + "");*/
                Uri selectedImage = data.getData();
                try {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        new ImageCompressionAsyncTask().execute(filePath);
//                        iplCampaignPhotoPath = filePath;
//                        ipl_campaign_img_bitmap = new BitmapHelper().grabImage(filePath);
//                        edt_ipl_campaign_photo.setText(filePath.substring(filePath.lastIndexOf("/")).replace("/", ""));
//                        setPreviewImage();
                    }
                    cursor.close();
                } catch (Exception e) {
                    Uri filePath = data.getData();
                    new ImageCompressionAsyncTask().execute(filePath.getPath());
//                    iplCampaignPhotoPath = filePath.getPath();
//                    ipl_campaign_img_bitmap = new BitmapHelper().grabImage(filePath.getPath());
//                    File file = new File(filePath.getPath());
//                    edt_ipl_campaign_photo.setText(file.getName());
//                    setPreviewImage();
                }
            }
        }
    }

    private void setPreviewImage() {
        iv_ipl_campaign_img_preview.setImageBitmap(ipl_campaign_img_bitmap);
        rl_ipl_campaign_img_preview.setVisibility(View.VISIBLE);
        tr_img_selector.setVisibility(View.GONE);
        tv_info.setVisibility(View.GONE);
    }

    private void takePicture(int requestCode) {
        Uri photoURI;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "image.jpg");
        photoURI = FileProvider.getUriForFile(getActivity(),
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(cameraIntent, requestCode);
    }

    private void selectImage(final int invoice_no) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                   /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, CHOOSE_FILE_REQUESTCODE_FROM_CAMERA_INVOICE + invoice_no);*/
                    takePicture(CHOOSE_FILE_REQUESTCODE_FROM_CAMERA_INVOICE + invoice_no);
                } else if (options[item].equals("Choose from Gallery")) {
                    // Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("*/*");
                    startActivityForResult(intent, CHOOSE_FILE_REQUESTCODE_FROM_GALLERY_INVOICE + invoice_no);

                }
            }
        });
        builder.show();
    }

    void uploadDocument(final String iplCampaignPhotoPath) {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(
                    Request.Method.POST, UserFunctions.webservice_URL_upload_tally_invoice,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            //dialog.dismiss();
                            JSONObject obj = new JSONObject();
                            try {
                                obj = new JSONObject(new String(response.data));
                                if (obj.getString("status").equalsIgnoreCase("success")) {
                                    progressDialog.dismiss();
                                    showDialog(getActivity(), obj.getString("message"));
                                } else {
                                    CustomOKDialog cdd = new CustomOKDialog(getActivity(), obj.getString("error"), "Gulf Oil");
                                    cdd.show();
                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                CustomOKDialog cdd = null;
                                try {
                                    cdd = new CustomOKDialog(getActivity(), obj.getString("message"), "Gulf Oil");
                                    cdd.show();
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    String source = BuildConfig.AUTH_USERNAME + ":" + BuildConfig.AUTH_PASSWORD;

                    String ret = "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);

                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", ret);
                    //params.put(HTTP.CONTENT_TYPE, "application/json");
                    params.put("participant_login_id", "" + mech_trno);
                    return params;
                }

                @Override
                public Map<String, DataPart> getByteData() throws AuthFailureError {
                    Map<String, DataPart> params = new HashMap<>();
                    // TODO : fetch file and upload.. compress will be done at the time of image capture
                    try {
                        params.put("invoice_1", new DataPart(edt_ipl_campaign_photo.getText().toString().trim(), new BitmapHelper().fullyReadFileToBytes(new File(iplCampaignPhotoPath))));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return params;
                }
            };

            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //adding the request to volley
            Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        boolean result = true;

        if (edt_ipl_campaign_photo.getText().toString().trim().length() == 0) {
            Toast.makeText(getActivity(), "Please select photo", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;

    }

    private String getExtension(String invoicePath) {
        String strExtension = "";
        try {
            strExtension = invoicePath.substring(invoicePath.lastIndexOf("."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strExtension;
    }

    public void showDialog(Activity activity, String message) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.success_dialog);
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), CAMERA);
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean externalStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && externalStorageAccepted)
                        Log.i("LOG_TAG", "Permission Granted, Now you can access camera.");
                        //  CommonHelper.toast("Permission Granted, Now you can access camera.", context);
                    else {
                        //Log.i(LOG_TAG, "Permission Granted, Now you can access camera.");
                        Toast.makeText(getActivity(), "Permission Denied, You cannot access camera.", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;

        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private Intent getFileChooserIntent() {
        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

        return intent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = getActivity().findViewById(R.id.tool_bar);
        ivToolbarLogo = toolbar.findViewById(R.id.imgvw_logo);
        if (ivToolbarLogo != null) {
            ivToolbarLogo.setImageDrawable(getResources().getDrawable(R.drawable.logo_ipl));
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) ivToolbarLogo.getLayoutParams();
            lp.setMargins(0, 0, -120, 0);
            ivToolbarLogo.setLayoutParams(lp);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (ivToolbarLogo != null) {
            ivToolbarLogo.setImageDrawable(getResources().getDrawable(R.drawable.blnk_bg_head_4logo));
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) ivToolbarLogo.getLayoutParams();
            lp.setMargins(0, 0, -50, 0);
            ivToolbarLogo.setLayoutParams(lp);
        }
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            return compressImage(params[0]);
        }

        public String compressImage(String imageUri) {
            File file = new File(imageUri);
            long lenbmpact = file.length() / 1024;
            Log.d("length--->", "" + lenbmpact);

            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(imageUri, options);

            int actualHeight = options.outHeight;
            Log.d("length1--->", "" + actualHeight);

            int actualWidth = options.outWidth;
            Log.d("length2--->", "" + actualWidth);

            float maxHeight = 1600.0f;
            float maxWidth = 2400.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(imageUri, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            //compreesion
            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


            ExifInterface exif;
            try {
                exif = new ExifInterface(imageUri);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;
            String filename = getFilename();
            try {
                out = new FileOutputStream(filename);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filename;

        }


        public String getFilename() {
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "Images/");

            if (!file.exists()) {
                file.mkdirs();
            }
            String uriSting = (file.getAbsolutePath() + "/Query.jpg");

            Log.d("Path-->", uriSting);

            return uriSting;

        }

        //move to next activity image
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            File fileToUploadRisk = new File(result);
            iplCampaignPhotoPath = result;
            ipl_campaign_img_bitmap = new BitmapHelper().grabImage(result);
            edt_ipl_campaign_photo.setText(fileToUploadRisk.getName());
            setPreviewImage();

        }

    }
}
