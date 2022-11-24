package com.taraba.gulfoilapp.util.camera;

/**
 * Created by HD on 9/27/2016.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CameraHelper extends AppCompatActivity {

    public static final int REQUEST_CAMERA = 10;
    public static final int REQUEST_GALERY = 11;
    public static final int CAMERA_INTENT = 101;
    public static final int GALLERY_INTENT = 102;
    public static final int BOTH_INTENT = 103;
    public AsyncResponse delegate = null;
    private Activity activity;
    private ImageView imageView;
    private String userChoosenTask;
    private int serverResponseCode = 0;
    // private String upLoadServerUri = MyConfig.JSON_URL_PROFILE +"upload_file.php";
    private ImageLoadingUtils utils;
    private Uri imageUri;
    private String imagePath = "NONE";
    private int img_no = 0;
    private boolean requiredCompression;


    public CameraHelper(AppCompatActivity acivity) {
        this.activity = acivity;
        this.utils = new ImageLoadingUtils(acivity);
        this.delegate = (AsyncResponse) acivity;
    }

    public CameraHelper(Activity acivity, Fragment fragment) {
        this.activity = acivity;
        this.utils = new ImageLoadingUtils(acivity);
        this.delegate = (AsyncResponse) fragment;
    }


    public void selectImage(ImageView imageView, int img_no, int intentType) {

        selectImage(imageView, img_no, intentType, true);

    }

    public void selectImage(ImageView imageView, int img_no, int intentType, boolean requiredCompression) {
        this.imageView = imageView;
        this.img_no = img_no;
        this.requiredCompression = requiredCompression;
        switch (intentType) {
            case CAMERA_INTENT:
                cameraIntent();
                break;
            case GALLERY_INTENT:
                galleryIntent();
                break;
            case BOTH_INTENT:
                SelectionDialogCameraOrGallery();
                break;

        }

    }

    private void SelectionDialogCameraOrGallery() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {

            if (items[item].equals("Take Photo")) {
                cameraIntent();

            } else if (items[item].equals("Choose from Library")) {
                galleryIntent();

            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
//        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alert.show();
    }


    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALERY);
    }

    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "PgPedia");
        imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, REQUEST_CAMERA);
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, REQUEST_CAMERA);*/
    }


    public void myActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("myActivityResult", "myActivityResult: "+requestCode);
        Log.d("myActivityResult", "myActivityResult: "+resultCode);
        Log.d("myActivityResult", "myActivityResult: "+data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALERY)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
       /* Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        Log.d("location_cam","loc"+destination);

        FileOutputStream fo = null;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        try {
            new ImageCompressionAsyncTask(true).execute(imageUri.toString());

            Log.d("pathcapture-->", data + "    " + imageUri);
            //storage/emulated/0/Pictures/1467705900443.jpg

        } catch (NullPointerException e) {

            e.printStackTrace();
        }

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                Log.d("location_gal", "loc" + data.getDataString());
                new ImageCompressionAsyncTask(true).execute(data.getDataString());

                //bm = MediaStore.Images.Media.getBitmap(activity.getApplicationContext().getContentResolver(), data.getData());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        //imageView.setImageBitmap(bm);
        //bm=null;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public interface AsyncResponse {
        void processFinish(String output, int img_no);
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
        private boolean fromGallery;

        public ImageCompressionAsyncTask(boolean fromGallery) {
            this.fromGallery = fromGallery;
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = compressImage(params[0]);
            return filePath;
        }

        public String compressImage(String imageUri) {

            String filePath = getRealPathFromURI(imageUri);
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = requiredCompression ? 2560.0f : actualHeight;
            float maxWidth = requiredCompression ? 2560.0f : actualWidth;
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
                bmp = BitmapFactory.decodeFile(filePath, options);
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

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

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
                // MyApplication.getInstance().trackException(e);
                e.printStackTrace();
            }
            FileOutputStream out = null;
            String filename = getFilename();
            try {
                out = new FileOutputStream(filename);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, requiredCompression ? 10 : 80, out);


            } catch (FileNotFoundException e) {
                //MyApplication.getInstance().trackException(e);
                e.printStackTrace();
            }

            return filename;

        }


        public String getFilename() {
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "Pictures");
            if (!file.exists()) {
                file.mkdirs();
            }
            String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
            return uriSting;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (fromGallery) {
                if (imageView != null) {
                    Picasso.with(activity)
                            .load(result)
                            .into(imageView);

                    Bitmap bitmap = utils.decodeBitmapFromPath(result);
                    imageView.setImageBitmap(bitmap);
                }

                delegate.processFinish(result, img_no);
            } else {

            }
        }

    }


}
