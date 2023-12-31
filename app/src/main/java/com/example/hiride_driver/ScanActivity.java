package com.example.hiride_driver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {
    SurfaceHolder holder;
    SurfaceView cameraview;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);


        cameraview=(SurfaceView)findViewById(R.id.surfaceview);
        cameraview.setZOrderMediaOverlay(true);
        holder=cameraview.getHolder();
        barcodeDetector=new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        if (!barcodeDetector.isOperational())
        {
            Toast.makeText(this, "Sorry! couldn't setup the detector", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        cameraSource=new CameraSource.Builder(this,barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920,1024)
                .build();
        cameraview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try
                {
                    if(ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
                    {
                        cameraSource.start(cameraview.getHolder());

                    }

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                Log.d("DETECTIONS",detections.getDetectedItems().valueAt(0).toString());

                final SparseArray<Barcode> barcodes=detections.getDetectedItems();
                if(barcodes.size()>0)
                {
                    Intent intent=new Intent();
                    intent.putExtra("barcode", barcodes.valueAt(0));
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }
}
