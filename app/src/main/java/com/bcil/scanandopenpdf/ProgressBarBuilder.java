package com.bcil.scanandopenpdf;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Ninad Gawankar on 29-07-2017.
 */

public class ProgressBarBuilder {

    private static ProgressDialog mProgressDialog;



    public static void startProgressBarDialogAsPerPercent(Context context, String message) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(100);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public static void stopProgressBarDialog() {
        mProgressDialog.dismiss();
    }

    public static void updateProgressBarDialog(int progress) {
        mProgressDialog.setProgress(progress);
    }
}
