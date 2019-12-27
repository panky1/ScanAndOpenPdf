package com.bcil.scanandopenpdf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity/* implements View.OnClickListener*/ {

    public static final String PDFURL = "https://amul.barcodeindia.com/pdffiles/Barcodedata.pdf";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static String MAINURL;
    @Bind(R.id.etScan)
    TextInputEditText etScan;
    @Bind(R.id.tvLastScan)
    TextView tvLastScan;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        verifyStoragePermissions(this);
        preferenceManager = new PreferenceManager(MainActivity.this);
        initData();
    }


    private void initData() {
        etScan.requestFocus();
        etScan.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    new CommonUtils().hideKeyboardOnLeaving(MainActivity.this);
                    if (checkValidation()) {
                        if (etScan.getText().toString().trim().equals(PDFURL)) {
                            tvLastScan.setText(etScan.getText().toString().trim());
                            etScan.setText("");
                            if(preferenceManager.getPreferenceIntValues(AppConstants.STATUS)==0){
                                if(new NetworkUtils().isNetworkAvailable(MainActivity.this)){
                                    new DownLoadPdf1(MainActivity.this,PDFURL,"BarcodeScanData.pdf",preferenceManager).execute();
                                }else {
                                    Toast.makeText(MainActivity.this, "Please check internet connection!!!", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Intent intent = new Intent(MainActivity.this,ViewActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        } else {
                            tvLastScan.setText(etScan.getText().toString().trim());
                            etScan.setText("");
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter the required fields", Toast.LENGTH_SHORT).show();
                    }


                }
                return false;
            }
        });



    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(etScan)) ret = false;
        return ret;
    }

    private void verifyStoragePermissions(MainActivity mainActivity) {
        int permission = ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    mainActivity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
         if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length <= 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Cannot write pdf to external storage", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
