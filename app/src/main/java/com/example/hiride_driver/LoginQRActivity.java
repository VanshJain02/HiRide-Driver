package com.example.hiride_driver;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.barcode.Barcode;

public class LoginQRActivity extends AppCompatActivity {
    Button clickbtw;
    TextView resulttxt;
    public static final int REQUEST_CODE=100;
    public static final int PERMISSION_REQUEST=200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_qr);
        clickbtw=(Button)findViewById(R.id.clickbtn);
        resulttxt=(TextView)findViewById(R.id.resulttxt);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST);
        }
        clickbtw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginQRActivity.this,ScanActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");
                resulttxt.post(new Runnable() {
                    @Override
                    public void run() {
                        resulttxt.setText(barcode.displayValue);
                        final String qrresult = (String) resulttxt.getText();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginQRActivity.this);
                        builder.setTitle("Scan Result");

                        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(LoginQRActivity.this, ChooseSDActivity.class);
                                intent.putExtra("busNo", qrresult);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setMessage(barcode.displayValue);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
                });
            } else {
                Toast.makeText(this, "QR Code is not Scanned!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
