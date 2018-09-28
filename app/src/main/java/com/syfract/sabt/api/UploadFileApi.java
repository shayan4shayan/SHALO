package com.syfract.sabt.api;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.base.api.BaseApiConnection;
import com.syfract.sabt.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UploadFileApi extends BaseApiConnection {

    private final String upload = "new";
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private final Uri file;
    private byte[] multipartBody;

    private Response.Listener<NetworkResponse> responseListener = new Response.Listener<NetworkResponse>() {
        @Override
        public void onResponse(NetworkResponse networkResponse) {
            String string = new String(networkResponse.data);
            mApiInterface.onResponse(string);
            Log.d("UploadFileApi", "onResponse: " + string);
        }
    };

    public UploadFileApi(Context context, Uri file) {
        super(context);
        this.file = file;

    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void execute() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            buildPart(dos, file);
            Log.d("UploadFileApi", "execute: " + getFileName(getContext(), file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        multipartBody = bos.toByteArray();
        Log.d(getClass().getSimpleName(), "execute: " + new String(multipartBody));
        Log.d("url", getUrl());
        VolleyMultipartRequest req = new VolleyMultipartRequest(getUrl(), getDefaultHeader(), responseListener, this) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> map = new HashMap<>();
                String fileName = "file";
                DataPart part = new DataPart(getFileName(getContext(), file), multipartBody);
                map.put(fileName, part);
                return map;
            }

            @Override
            protected Map<String, String> getParams() {
                return UploadFileApi.this.getPostParams();
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(120000, 2, 0f));
        getRequestQueue().add(req);
    }

    public String getUrl() {
        return Uri.parse(baseUrl).buildUpon().appendPath(upload).build().toString() + "/";
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mApiInterface.onError(getContext().getString(R.string.error_upload_file));
        if (error != null) {
            if (error.networkResponse != null)
                Log.d(getClass().getSimpleName(), "onErrorResponse: " + error.networkResponse.statusCode);
            else {
                Log.d(getClass().getSimpleName(), error.getCause().getMessage());
            }
        } else {
            Log.d(getClass().getSimpleName(), "onErrorResponse: error response is null");
        }
    }

    private void buildPart(DataOutputStream dataOutputStream, Uri fileData) throws IOException {
        InputStream in = getContext().getContentResolver().openInputStream(fileData);
        if (in == null) {
            throw new FileNotFoundException();
        }

        int bytesAvailable = in.available();
        Log.d("UploadFile", "buildPart: " + bytesAvailable);

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = in.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = in.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = in.read(buffer, 0, bufferSize);
        }
    }


    //    private class MultipartRequest extends Request<NetworkResponse> {
//        private final Response.Listener<NetworkResponse> mListener;
//        private final Response.ErrorListener mErrorListener;
//        private final Map<String, String> mHeaders;
//        private final String mMimeType;
//        private final byte[] mMultipartBody;
//
//        private MultipartRequest(String url, Map<String, String> headers, String mimeType, byte[] multipartBody, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
//            super(Method.POST, url, errorListener);
//            this.mListener = listener;
//            this.mErrorListener = errorListener;
//            this.mHeaders = headers;
//            this.mMimeType = mimeType;
//            this.mMultipartBody = multipartBody;
//        }
//
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            return (mHeaders != null) ? mHeaders : super.getHeaders();
//        }
//
//        @Override
//        public String getBodyContentType() {
//            return mMimeType;
//        }
//
//        @Override
//        public byte[] getBody() throws AuthFailureError {
//            return mMultipartBody;
//        }
//
//        @Override
//        protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
//            try {
//                return Response.success(
//                        response,
//                        HttpHeaderParser.parseCacheHeaders(response));
//            } catch (Exception e) {
//                return Response.error(new ParseError(e));
//            }
//        }
//
//        @Override
//        protected void deliverResponse(NetworkResponse response) {
//            mListener.onResponse(response);
//        }
//
//        @Override
//        public void deliverError(VolleyError error) {
//            mErrorListener.onErrorResponse(error);
//        }
//    }
    public class VolleyMultipartRequest extends Request<NetworkResponse> {
        private final String twoHyphens = "--";
        private final String lineEnd = "\r\n";
        private final String boundary = "apiclient-" + System.currentTimeMillis();

        private Response.Listener<NetworkResponse> mListener;
        private Response.ErrorListener mErrorListener;
        private Map<String, String> mHeaders;

        /**
         * Default constructor with predefined header and post method.
         *
         * @param url           request destination
         * @param headers       predefined custom header
         * @param listener      on success achieved 200 code from request
         * @param errorListener on error http or library timeout
         */
        public VolleyMultipartRequest(String url, Map<String, String> headers,
                                      Response.Listener<NetworkResponse> listener,
                                      Response.ErrorListener errorListener) {
            super(Method.POST, url, errorListener);
            this.mListener = listener;
            this.mErrorListener = errorListener;
            this.mHeaders = headers;
        }

        /**
         * Constructor with option method and default header configuration.
         *
         * @param method        method for now accept POST and GET only
         * @param url           request destination
         * @param listener      on success event handler
         * @param errorListener on error event handler
         */
        public VolleyMultipartRequest(int method, String url,
                                      Response.Listener<NetworkResponse> listener,
                                      Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.mListener = listener;
            this.mErrorListener = errorListener;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return (mHeaders != null) ? mHeaders : super.getHeaders();
        }

        @Override
        public String getBodyContentType() {
            return "multipart/form-data;boundary=" + boundary;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);

            try {
                // populate text payload
                Map<String, String> params = getParams();
                if (params != null && params.size() > 0) {
                    textParse(dos, params, getParamsEncoding());
                }

                // populate data byte payload
                Map<String, DataPart> data = getByteData();
                if (data != null && data.size() > 0) {
                    dataParse(dos, data);
                }

                // close multipart form data after text and file data
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                Log.d("UploadFileApi", "body" + new String(bos.toByteArray()));
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Custom method handle data payload.
         *
         * @return Map data part label with data byte
         * @throws AuthFailureError
         */
        protected Map<String, DataPart> getByteData() throws AuthFailureError {
            return null;
        }

        @Override
        protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
            try {
                return Response.success(
                        response,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (Exception e) {
                return Response.error(new ParseError(e));
            }
        }

        @Override
        protected void deliverResponse(NetworkResponse response) {
            mListener.onResponse(response);
        }

        @Override
        public void deliverError(VolleyError error) {
            mErrorListener.onErrorResponse(error);
        }

        /**
         * Parse string map into data output stream by key and value.
         *
         * @param dataOutputStream data output stream handle string parsing
         * @param params           string inputs collection
         * @param encoding         encode the inputs, default UTF-8
         * @throws IOException
         */
        private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
                }
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException("Encoding not supported: " + encoding, uee);
            }
        }

        /**
         * Parse data into data output stream.
         *
         * @param dataOutputStream data output stream handle file attachment
         * @param data             loop through data
         * @throws IOException
         */
        private void dataParse(DataOutputStream dataOutputStream, Map<String, DataPart> data) throws IOException {
            for (Map.Entry<String, DataPart> entry : data.entrySet()) {
                buildDataPart(dataOutputStream, entry.getValue(), entry.getKey());
            }
        }

        /**
         * Write string data into header and data output stream.
         *
         * @param dataOutputStream data output stream handle string parsing
         * @param parameterName    name of input
         * @param parameterValue   value of input
         * @throws IOException
         */
        private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
            //dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(parameterValue + lineEnd);
        }

        /**
         * Write data file into header and data output stream.
         *
         * @param dataOutputStream data output stream handle data parsing
         * @param dataFile         data byte as DataPart from collection
         * @param inputName        name of data input
         * @throws IOException
         */
        private void buildDataPart(DataOutputStream dataOutputStream, DataPart dataFile, String inputName) throws IOException {
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                    inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + lineEnd);
            if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty()) {
                dataOutputStream.writeBytes("Content-Type: " + dataFile.getType() + lineEnd);
            }
            dataOutputStream.writeBytes(lineEnd);

            ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024 * 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dataOutputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dataOutputStream.writeBytes(lineEnd);
        }

        /**
         * Simple data container use for passing byte file
         */
        public class DataPart {
            private String fileName;
            private byte[] content;
            private String type;

            /**
             * Default data part
             */
            public DataPart() {
            }

            /**
             * Constructor with data.
             *
             * @param name label of data
             * @param data byte data
             */
            public DataPart(String name, byte[] data) {
                fileName = name;
                content = data;
            }

            /**
             * Constructor with mime data type.
             *
             * @param name     label of data
             * @param data     byte data
             * @param mimeType mime data like "image/jpeg"
             */
            public DataPart(String name, byte[] data, String mimeType) {
                fileName = name;
                content = data;
                type = mimeType;
            }

            /**
             * Getter file name.
             *
             * @return file name
             */
            public String getFileName() {
                return fileName;
            }

            /**
             * Setter file name.
             *
             * @param fileName string file name
             */
            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            /**
             * Getter content.
             *
             * @return byte file data
             */
            public byte[] getContent() {
                return content;
            }

            /**
             * Setter content.
             *
             * @param content byte file data
             */
            public void setContent(byte[] content) {
                this.content = content;
            }

            /**
             * Getter mime type.
             *
             * @return mime type
             */
            public String getType() {
                return type;
            }

            /**
             * Setter mime type.
             *
             * @param type mime type
             */
            public void setType(String type) {
                this.type = type;
            }
        }
    }
}