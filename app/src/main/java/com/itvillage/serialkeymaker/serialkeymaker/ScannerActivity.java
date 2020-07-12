package com.itvillage.serialkeymaker.serialkeymaker;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {
    CodeScannerView codeScannerView;
    CodeScanner codeScanner;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        codeScannerView = findViewById(R.id.codeScannerView);

        openCamaraForScanner();
    }

    private void openCamaraForScanner() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            openQRCodeScneer();
        } else {
            requestCameraPermission();
        }

    }

    private void requestCameraPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Camera access is required to Scan The Barcode.",
                    Toast.LENGTH_LONG).show();
            // Request the permission
            ActivityCompat.requestPermissions(ScannerActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        } else {
            Toast.makeText(this,
                    "<b>Camera could not be opened.</b>\\nThis occurs when the camera is not available (for example it is already in use) or if the system has denied access (for example when camera access has been disabled).", Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    private void openQRCodeScneer() {
        codeScanner= new CodeScanner(this,codeScannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] results =result.getText().split(",");
                        dialog = new Dialog(ScannerActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.custom_dialog);

                        TextView packageName = (TextView) dialog.findViewById(R.id.packageName);
                        packageName.setText(results[4]);
                        TextView name = (TextView) dialog.findViewById(R.id.name);
                        name.setText("User Name: "+results[1]);
                        TextView companyName = (TextView) dialog.findViewById(R.id.company_name);
                        companyName.setText("Shop Name: "+results[6]);
                        TextView macAddress = (TextView) dialog.findViewById(R.id.mac_address);
                        macAddress.setText("Mac Address: "+results[2]);
                        TextView phoneNo = (TextView) dialog.findViewById(R.id.phoneNo);
                        phoneNo.setText("Phone No: "+results[0]);
                        TextView email = (TextView) dialog.findViewById(R.id.email);
                        email.setText("Email: "+results[5]);

                        Button send = (Button) dialog.findViewById(R.id.send);
                        Button re_scan = (Button) dialog.findViewById(R.id.re_scan);
                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendMail();
                            }
                        });
                        re_scan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                codeScanner.startPreview();
                                dialog.dismiss();
                            }
                        });


                        dialog.show();

                    }
                });
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
                dialog.dismiss();
            }
        });
    }

    private void sendMail() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }
}
