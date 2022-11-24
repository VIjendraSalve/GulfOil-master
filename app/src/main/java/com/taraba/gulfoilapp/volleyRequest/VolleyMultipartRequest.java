package com.taraba.gulfoilapp.volleyRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.commons.net.SocketClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {
    private final String boundary = ("apiclient-" + System.currentTimeMillis());
    private final String lineEnd = SocketClient.NETASCII_EOL;
    private Response.ErrorListener mErrorListener;
    private Map<String, String> mHeaders;
    private Response.Listener<NetworkResponse> mListener;
    private final String twoHyphens = "--";

    /* access modifiers changed from: protected */
    public Map<String, DataPart> getByteData() throws AuthFailureError {
        return null;
    }

    public VolleyMultipartRequest(int i, String str, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(i, str, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map = this.mHeaders;
        return map != null ? map : super.getHeaders();
    }

    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + this.boundary;
    }

    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                textParse(dataOutputStream, params, getParamsEncoding());
            }
            Map<String, DataPart> byteData = getByteData();
            if (byteData != null && byteData.size() > 0) {
                dataParse(dataOutputStream, byteData);
            }
            dataOutputStream.writeBytes("--" + this.boundary + "--" + SocketClient.NETASCII_EOL);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public Response<NetworkResponse> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            return Response.success(networkResponse, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError((Throwable) e));
        }
    }

    /* access modifiers changed from: protected */
    public void deliverResponse(NetworkResponse networkResponse) {
        this.mListener.onResponse(networkResponse);
    }

    public void deliverError(VolleyError volleyError) {
        this.mErrorListener.onErrorResponse(volleyError);
    }

    private void textParse(DataOutputStream dataOutputStream, Map<String, String> map, String str) throws IOException {
        try {
            for (Map.Entry next : map.entrySet()) {
                buildTextPart(dataOutputStream, (String) next.getKey(), (String) next.getValue());
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported: " + str, e);
        }
    }

    private void dataParse(DataOutputStream dataOutputStream, Map<String, DataPart> map) throws IOException {
        for (Map.Entry next : map.entrySet()) {
            buildDataPart(dataOutputStream, (DataPart) next.getValue(), (String) next.getKey());
        }
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String str, String str2) throws IOException {
        dataOutputStream.writeBytes("--" + this.boundary + SocketClient.NETASCII_EOL);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + str + "\"" + SocketClient.NETASCII_EOL);
        dataOutputStream.writeBytes(SocketClient.NETASCII_EOL);
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(SocketClient.NETASCII_EOL);
        dataOutputStream.writeBytes(sb.toString());
    }

    private void buildDataPart(DataOutputStream dataOutputStream, DataPart dataPart, String str) throws IOException {
        dataOutputStream.writeBytes("--" + this.boundary + SocketClient.NETASCII_EOL);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + str + "\"; filename=\"" + dataPart.getFileName() + "\"" + SocketClient.NETASCII_EOL);
        if (dataPart.getType() != null && !dataPart.getType().trim().isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataPart.getType() + SocketClient.NETASCII_EOL);
        }
        dataOutputStream.writeBytes(SocketClient.NETASCII_EOL);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataPart.getContent());
        int min = Math.min(byteArrayInputStream.available(), 1048576);
        byte[] bArr = new byte[min];
        int read = byteArrayInputStream.read(bArr, 0, min);
        while (read > 0) {
            dataOutputStream.write(bArr, 0, min);
            min = Math.min(byteArrayInputStream.available(), 1048576);
            read = byteArrayInputStream.read(bArr, 0, min);
        }
        dataOutputStream.writeBytes(SocketClient.NETASCII_EOL);
    }

    public class DataPart {
        private byte[] content;
        private String fileName;
        private String type;

        public DataPart() {
        }

        public DataPart(String str, byte[] bArr) {
            this.fileName = str;
            this.content = bArr;
        }

        /* access modifiers changed from: package-private */
        public String getFileName() {
            return this.fileName;
        }

        /* access modifiers changed from: package-private */
        public byte[] getContent() {
            return this.content;
        }

        /* access modifiers changed from: package-private */
        public String getType() {
            return this.type;
        }
    }
}
