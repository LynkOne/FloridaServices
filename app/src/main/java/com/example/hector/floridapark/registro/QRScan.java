package com.example.hector.floridapark.registro;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.example.hector.floridapark.R;
import com.example.hector.floridapark.model.Personas;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;

public class QRScan extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_CAMERA = 78;

    private String token = "";

    private String nombre,apellidos, email, dni;
    private int telefono;
    private String tokenanterior = "";
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        cameraView = (SurfaceView)findViewById(R.id.camera_view);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > 22) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA))
                    Toast.makeText(getApplicationContext(), "Esta aplicación necesita acceder a la cámara para funcionar", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
        Log.e("HECTORRR", "STOY AQUI");

        initQR();
        cameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraFocus(cameraSource, Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void initQR() {

        //Creamos el lector de QR
        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        //Creama la camara
        cameraSource = new CameraSource
                .Builder(getApplicationContext(), barcodeDetector)
                .setRequestedPreviewSize(1280, 720)
                .build();

        // Prepara el lector de qr
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                //Verifica si el usuario ha dado permiso para la camara
                if (ContextCompat.checkSelfPermission(getApplicationContext(),  android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Log.e("HECTORRR", "He entrado");
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException ie) {
                        Log.e("CAMERA SOURCE", ie.getMessage());
                    }
                } else{
                    Log.e("HECTORRR", "NOOO He entrado");
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        // preparo el detector de QR
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }


            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() > 0) {
                    //Barcode.CONTACT_INFO
                    // obtenemos el token
                    for (int index = 0; index < barcodes.size(); index++) {
                        int type=barcodes.valueAt(index).valueFormat;
                        switch (type){
                            case Barcode.CONTACT_INFO://Si hemos leido el QR del contacto, almacenamos todos los tados
                                nombre=barcodes.valueAt(index).contactInfo.name.first.toString();
                                apellidos=barcodes.valueAt(index).contactInfo.name.last.toString();
                                telefono=Integer.parseInt(barcodes.valueAt(index).contactInfo.phones[0].number.toString());
                                email=barcodes.valueAt(index).contactInfo.emails[0].address.toString();

                                token = "Nombre: "+nombre+" Apellidos: "+apellidos+" Teléfono: "+telefono+" Email: "+email+" ";
                                Toast.makeText(QRScan.this, "Código QR leido", Toast.LENGTH_SHORT).show();
                                break;
                            case Barcode.TEXT://Si  hemos leido el DNI, Como el códigode barras del DNI lleva la letra, se almacena como string
                                token = barcodes.valueAt(index).displayValue.toString();
                                validaDNI(token);//validamo y almacenamos dni
                                Toast.makeText(QRScan.this, "Código de barras leido", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                    try{
                        if(!nombre.isEmpty()&&!apellidos.isEmpty()&&!email.isEmpty()&&telefono!=0&&!dni.isEmpty()){
                            Log.d("hectorrr","TODO CORRECTO");
                            Personas p=new Personas(dni,nombre,apellidos,telefono,email);
                            Intent i =getIntent();
                            Bundle b = new Bundle();
                            b.putParcelable("nou usuari",p);
                            i.putExtras(b);
                            setResult(RESULT_OK,i);
                            finish();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    // verificamos que el token anterior no se igual al actual
                    // esto es util para evitar multiples llamadas empleando el mismo token
                    if (!token.equals(tokenanterior)) {

                        // guardamos el ultimo token procesado
                        tokenanterior = token;
                        Log.d("hectorrr", token+"");


                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (this) {
                                        wait(2000);
                                        // limpiamos el token
                                        tokenanterior = "";
                                        Log.d("hectorrr", "estoy en el hilo");
                                    }
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Waiting didnt work!!");
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                }
            }
        });


    }
    private static boolean cameraFocus(@NonNull CameraSource cameraSource, @NonNull String focusMode) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        Camera.Parameters params = camera.getParameters();
                        params.setFocusMode(focusMode);
                        camera.setParameters(params);
                        return true;
                    }

                    return false;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        return false;

    }
    /** call this to return control to the calling activity - {@link registro#onActivityResult} */
    public void returnToCaller(Bundle bundle) {
        // sets the result for the calling activity
        //setResult(RESULT_OK, null, bundle);
        // equivalent of 'return'
        finish();
    }

    private boolean validaDNI(String scanned_dni){
        boolean res=false;
        int num_dni;
        char letra_dni;
        try {
            //Obtenemos la parte numerica y la letra del DNI por separado
            num_dni=Integer.parseInt(scanned_dni.substring(0,9));
            letra_dni=scanned_dni.charAt(9);


            String posiblesLetras="TRWAGMYFPDXBNJZSQVHLCKE";
            int modulo= num_dni % 23;
            char letra = posiblesLetras.charAt(modulo);
            if(letra==letra_dni){
                res=true;
                Log.d("hectorrr", "DNI VALIDADO");
                this.dni=scanned_dni;
            }
        }
        catch (Exception e){
            Log.e("MIO", "algo no funciona en la comprobacion del dni!!"+e.getMessage());
            e.printStackTrace();
        }


        return res;
    }

}


