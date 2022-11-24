package com.taraba.gulfoilapp.volleyRequest;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapHelper {
    static final String FOLDER_PATH = "GulfOil";

    public Uri getImageUri(Context context, Bitmap bitmap) {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", (String) null));
    }

    public String getRealPathFromURI(Context context, Uri uri) {
        Cursor query = context.getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        query.moveToFirst();
        return query.getString(query.getColumnIndex("_data"));
    }

    public Bitmap decodeSampledBitmapFromFile(String str, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int i3 = 1;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        int i4 = options.outHeight;
        int i5 = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        if (i4 > i2) {
            i3 = Math.round(((float) i4) / ((float) i2));
        }
        if (i5 / i3 > i) {
            i3 = Math.round(((float) i5) / ((float) i));
        }
        options.inSampleSize = i3;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(str, options);
    }

    public Bitmap scaleImage(Context context, Uri uri) throws IOException {
        int i;
        int i2;
        Bitmap bitmap;
        InputStream openInputStream = context.getContentResolver().openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(openInputStream, (Rect) null, options);
        openInputStream.close();
        int orientation = getOrientation(context, uri);
        if (orientation == 90 || orientation == 270) {
            i = options.outHeight;
            i2 = options.outWidth;
        } else {
            i = options.outWidth;
            i2 = options.outHeight;
        }
        InputStream openInputStream2 = context.getContentResolver().openInputStream(uri);
        if (i > 128 || i2 > 128) {
            float max = Math.max(((float) i) / 128.0f, ((float) i2) / 128.0f);
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = (int) max;
            bitmap = BitmapFactory.decodeStream(openInputStream2, (Rect) null, options2);
        } else {
            bitmap = BitmapFactory.decodeStream(openInputStream2);
        }
        Bitmap bitmap2 = bitmap;
        openInputStream2.close();
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate((float) orientation);
            bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
        }
        String type = context.getContentResolver().getType(uri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public int getOrientation(Context context, Uri uri) {
        Cursor query = context.getContentResolver().query(uri, new String[]{"orientation"}, (String) null, (String[]) null, (String) null);
        if (query.getCount() != 1) {
            return -1;
        }
        query.moveToFirst();
        return query.getInt(0);
    }

    public Bitmap rotateImageIfRequired(Context context, Bitmap bitmap, Uri uri) {
        int rotation = getRotation(context, uri);
        if (rotation == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotation);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return createBitmap;
    }

    public int getRotation(Context context, Uri uri) {
        int i = 0;
        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"orientation", "date_added"}, (String) null, (String[]) null, "date_added desc");
        if (!(query == null || query.getCount() == 0 || !query.moveToNext())) {
            i = query.getInt(0);
        }
        query.close();
        return i;
    }

    public String getRealPathFromUri(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
            int columnIndexOrThrow = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            return cursor.getString(columnIndexOrThrow);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Bitmap grabImage(String str) {
        try {
            File file = new File(str);
            int attributeInt = new ExifInterface(file.getPath()).getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1);
            int i = 0;
            if (attributeInt == 6) {
                i = 90;
            } else if (attributeInt == 3) {
                i = 180;
            } else if (attributeInt == 8) {
                i = 270;
            }
            Matrix matrix = new Matrix();
            matrix.postRotate((float) i);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = 4;
            if (i == 0) {
                return BitmapFactory.decodeFile(str, options);
            }
            Bitmap decodeStream = BitmapFactory.decodeStream(new FileInputStream(file), (Rect) null, options);
            return Bitmap.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), matrix, true);
        } catch (IOException e) {
            e.getMessage();
            return null;
        }
    }

    public byte[] fullyReadFileToBytes(File file) throws IOException {
        int length = (int) file.length();
        byte[] bArr = new byte[length];
        byte[] bArr2 = new byte[length];
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            int read = fileInputStream.read(bArr, 0, length);
            if (read < length) {
                int i = length - read;
                while (i > 0) {
                    int read2 = fileInputStream.read(bArr2, 0, i);
                    System.arraycopy(bArr2, 0, bArr, length - i, read2);
                    i -= read2;
                }
            }
            fileInputStream.close();
            return bArr;
        } catch (IOException e) {
            throw e;
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
    }

    public String compressImage(Activity activity, String str) {
        Bitmap bitmap;
        Bitmap bitmap2;
        Bitmap bitmap3;
        String realPathFromURI = getRealPathFromURI(activity, str);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap decodeFile = BitmapFactory.decodeFile(realPathFromURI, options);
        int i = options.outHeight;
        int i2 = options.outWidth;
        float f = (float) i;
        float f2 = (float) i2;
        float f3 = (float) (i2 / i);
        float f4 = f2 / f;
        if (f > f || f2 > f2) {
            if (f3 < f4) {
                i2 = (int) ((f / f) * f2);
                i = (int) f;
            } else {
                i = f3 > f4 ? (int) ((f2 / f2) * f) : (int) f;
                i2 = (int) f2;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, i2, i);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16384];
        try {
            decodeFile = BitmapFactory.decodeFile(realPathFromURI, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        try {
            bitmap = Bitmap.createBitmap(i2, i, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
            bitmap = null;
        }
        Bitmap bitmap4 = bitmap;
        float f5 = (float) i2;
        float f6 = f5 / ((float) options.outWidth);
        float f7 = (float) i;
        float f8 = f7 / ((float) options.outHeight);
        float f9 = f5 / 2.0f;
        float f10 = f7 / 2.0f;
        Matrix matrix = new Matrix();
        matrix.setScale(f6, f8, f9, f10);
        Canvas canvas = new Canvas(bitmap4);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(decodeFile, f9 - ((float) (decodeFile.getWidth() / 2)), f10 - ((float) (decodeFile.getHeight() / 2)), new Paint(2));
        try {
            int attributeInt = new ExifInterface(realPathFromURI).getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + attributeInt);
            Matrix matrix2 = new Matrix();
            if (attributeInt == 6) {
                matrix2.postRotate(90.0f);
                Log.d("EXIF", "Exif: " + attributeInt);
            } else if (attributeInt == 3) {
                matrix2.postRotate(180.0f);
                Log.d("EXIF", "Exif: " + attributeInt);
            } else if (attributeInt == 8) {
                matrix2.postRotate(270.0f);
                Log.d("EXIF", "Exif: " + attributeInt);
            }
            bitmap3 = bitmap4;
            try {
                bitmap2 = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix2, true);
            } catch (Exception e3) {
                e3.printStackTrace();
                bitmap2 = bitmap3;
                String filename = getFilename();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 20, new FileOutputStream(filename));
                return filename;
            }
        } catch (IOException e4) {
            bitmap3 = bitmap4;
            e4.printStackTrace();
            bitmap2 = bitmap3;
            String filename2 = getFilename();
            try {
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 20, new FileOutputStream(filename2));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return filename2;
        }
        String filename22 = getFilename();
        try {
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 20, new FileOutputStream(filename22));
        } catch (FileNotFoundException e5) {
            e5.printStackTrace();
        }
        return filename22;
    }

    public String getRealPathFromURI(Activity activity, String str) {
        Uri parse = Uri.parse(str);
        Cursor query = activity.getContentResolver().query(parse, (String[]) null, (String) null, (String[]) null, (String) null);
        if (query == null) {
            return parse.getPath();
        }
        query.moveToFirst();
        return query.getString(query.getColumnIndex("_data"));
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3;
        int i4 = options.outHeight;
        int i5 = options.outWidth;
        if (i4 > i2 || i5 > i) {
            i3 = Math.round(((float) i4) / ((float) i2));
            int round = Math.round(((float) i5) / ((float) i));
            if (i3 >= round) {
                i3 = round;
            }
        } else {
            i3 = 1;
        }
        while (((float) (i5 * i4)) / ((float) (i3 * i3)) > ((float) (i * i2 * 2))) {
            i3++;
        }
        return i3;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), FOLDER_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
    }
}
