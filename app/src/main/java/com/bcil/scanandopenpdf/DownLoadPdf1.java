package com.bcil.scanandopenpdf;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadPdf1 extends AsyncTask<Void, String, Void> {
    private static final String TAG = DownLoadPdf1.class.getSimpleName();
    private  String downloadLink, downloadFileName;
    @SuppressLint("StaticFieldLeak")
    private Activity activity;
    private File outputFile = null;
    private PreferenceManager preferenceManager;
    DownLoadPdf1(Activity activity, String downloadLink, String downloadFileName, PreferenceManager preferenceManager) {
        this.activity = activity;
        this.downloadLink = downloadLink;
        this.downloadFileName = downloadFileName;
        this.preferenceManager = preferenceManager;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressBarBuilder.startProgressBarDialogAsPerPercent(activity, "Downloading file. Please wait...");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(Void... voids) {
        int len1;
        try {
            URL url = new URL(downloadLink);//Create Download URl
            HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
            c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
            c.connect();//connect the URL Connection

            int lenghtOfFile = c.getContentLength();
            //If Connection response is not OK then show Logs
            if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                        + " " + c.getResponseMessage());
            }
            outputFile = new CommonUtils().createFileInternalStorage(downloadFileName);
            FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

            InputStream is = c.getInputStream();//Get InputStream for connection

            byte[] buffer = new byte[1024];//Set buffer type
            long total = 0;
            while ((len1 = is.read(buffer)) != -1) {
                total += len1;
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                fos.write(buffer, 0, len1);//Write new file
            }
            fos.flush(); // added to flushing output
            //Close all connection after doing task
            fos.close();
            is.close();

        } catch (Exception e) {

            //Read exception if something went wrong
            e.printStackTrace();
            outputFile = null;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Download failed please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        ProgressBarBuilder.stopProgressBarDialog();
        try {
            if (outputFile != null) {
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;

                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Uri path;
                if (currentapiVersion >= 24) {
                    pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    String authority = BuildConfig.APPLICATION_ID + ".provider";
                    path = FileProvider.getUriForFile(activity, authority, outputFile);
                } else {
                    path = Uri.fromFile(outputFile);
                }
                pdfOpenintent.setDataAndType(path, "application/pdf");
                try {
                    activity.startActivity(pdfOpenintent);
                    preferenceManager.putPreferenceIntValues(AppConstants.STATUS,1);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Please install any pdf viewer app to view report in pdf", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "There is some issue while downloading file,Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "There is some issue while downloading file,Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
        super.onPostExecute(result);

    }

    protected void onProgressUpdate(String... progress) {
        ProgressBarBuilder.updateProgressBarDialog(Integer.parseInt(progress[0]));
    }
}


