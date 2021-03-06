package com.example.hector.floridapark.registro;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hector.floridapark.R;
import com.example.hector.floridapark.model.Personas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class registro extends AppCompatActivity implements View.OnClickListener{
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 78;
    private EditText etPassword, etDni, etNombe, etApellidos, etTelefono, etCorreo, etCodProfesor;
    private RadioGroup rgTipoUsuario;
    private RadioButton rbEstudiante, rbProfesor;
    private TextView tvTipoUsuario,tvCodProfesor;
    private Button btRegistrarse;
    private String dni;
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setTheme(R.style.AppThemeNAB);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //Comprobamos y pedimos permisos de camara para poder escanear
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > 22) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA))
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.permisos_camara), Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
        //Asignamos todos los view de los EditText, RadioButton, etc.
        queue= Volley.newRequestQueue(this);
        rgTipoUsuario=(RadioGroup)findViewById(R.id.rg_registro_tipoUsuario);
        rbEstudiante=(RadioButton)findViewById(R.id.rb_registro_Estudiante);
        rbProfesor=(RadioButton)findViewById(R.id.rb_registro_Profesor);
        rgTipoUsuario.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==rbEstudiante.getId()){
                    tvCodProfesor.setVisibility(View.INVISIBLE);
                    etCodProfesor.setVisibility(View.INVISIBLE);
                }
                if(checkedId==rbProfesor.getId()){
                    tvCodProfesor.setVisibility(View.VISIBLE);
                    etCodProfesor.setVisibility(View.VISIBLE);
                }
            }
        });
        tvTipoUsuario=(TextView)findViewById(R.id.tv_registro_tipoUsuario);
        tvCodProfesor=(TextView)findViewById(R.id.tv_registro_codProfesor);

        etCodProfesor=(EditText)findViewById(R.id.et_registro_codProfesor);
        etDni=(EditText)findViewById(R.id.et_registro_dni);
        etNombe=(EditText)findViewById(R.id.et_registro_nombre);
        etApellidos=(EditText)findViewById(R.id.et_registro_apellidos);
        etTelefono=(EditText)findViewById(R.id.et_registro_telefono);
        etPassword=(EditText)findViewById(R.id.et_registro_contrasenya);
        btRegistrarse=(Button)findViewById(R.id.bt_registro_registrarse);
        btRegistrarse.setOnClickListener(this);

        etCorreo=(EditText)findViewById(R.id.et_registro_email);
        etDni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etDni.getText().toString().length()<10){
                    StringBuilder sb = new StringBuilder();
                    while (sb.length() < 10 - etDni.getText().toString().length()) {
                        sb.append('0');
                    }
                    sb.append(etDni.getText().toString());
                    dni=sb.toString();
                    Log.d("hectorrr",dni);
                }



            }
        });
        etCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override//Comprobamos si es un correo de profesor para habilitar el registro de profesores y mostrar los campos pertinentes
            public void afterTextChanged(Editable s) {
                if (etCorreo.getText().toString().contains("@florida-uni.es")){
                    rgTipoUsuario.setVisibility(View.VISIBLE);
                    tvTipoUsuario.setVisibility(View.VISIBLE);
                }
                if (!etCorreo.getText().toString().contains("@florida-uni.es")){
                    rgTipoUsuario.setVisibility(View.INVISIBLE);
                    tvTipoUsuario.setVisibility(View.INVISIBLE);
                    tvCodProfesor.setVisibility(View.INVISIBLE);
                    etCodProfesor.setVisibility(View.INVISIBLE);
                }
            }
        });

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(getResources().getText(R.string.titulo_dialog_registro));

        alertBuilder.setMessage(getResources().getText(R.string.contenido_dialog_registro));

        // Botón guardar
        alertBuilder.setPositiveButton(getResources().getText(R.string.boton_escanear_dialog_registro), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent i=new Intent(getApplicationContext(), QRScan.class);
                startActivityForResult(i, 1);
            }
        });
        // Botón cancelar
        alertBuilder.setNegativeButton(getResources().getText(R.string.boton_manual_dialog_registro), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertBuilder.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==btRegistrarse.getId()){

            if(etDni.getText().toString().isEmpty()){
                etDni.setError(getResources().getText(R.string.error_dni));
                etDni.requestFocus();
            }
            if(etNombe.getText().toString().isEmpty()){
                etNombe.setError(getResources().getText(R.string.error_nombre));
                etNombe.requestFocus();
            }
            if(etApellidos.getText().toString().isEmpty()){
                etApellidos.setError(getResources().getText(R.string.error_apellidos));
                etApellidos.requestFocus();
            }
            if(etTelefono.getText().toString().isEmpty()){
                etTelefono.setError(getResources().getText(R.string.empty_telefono));
                etTelefono.requestFocus();
            }
            if(etCorreo.getText().toString().isEmpty()){
                etCorreo.setError(getResources().getText(R.string.empty_email));
                etCorreo.requestFocus();
            }else{
                if(!Patterns.EMAIL_ADDRESS.matcher(etCorreo.getText().toString()).matches()){

                    etCorreo.setError(getResources().getText(R.string.error_email));
                    etCorreo.requestFocus();
                }
            }

            if(etPassword.getText().toString().isEmpty()){
                etPassword.setError(getResources().getText(R.string.empty_password));
                etPassword.requestFocus();
            }
            if(etPassword.getText().toString().length()<6){
                etPassword.setError(getResources().getText(R.string.error_password));
                etPassword.requestFocus();
            }

            if (!etDni.getText().toString().isEmpty() && validaDNI(this.dni) && !etNombe.getText().toString().isEmpty() && !etApellidos.getText().toString().isEmpty() && !etTelefono.getText().toString().isEmpty() && !etCorreo.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty() && etPassword.getText().toString().length()>=6) {

                if (rgTipoUsuario.getCheckedRadioButtonId()==rbEstudiante.getId()){
                    Personas persona_registro = new Personas(etDni.getText().toString(),etNombe.getText().toString(),etApellidos.getText().toString(),Integer.parseInt(etTelefono.getText().toString()),etCorreo.getText().toString(),etPassword.getText().toString(),false, 0);
                    Log.d("hectorrr", "REGISTRANDO ESTUDIANTE");
                    RegisterUserApi(persona_registro);
                }

                if (rgTipoUsuario.getCheckedRadioButtonId()==rbProfesor.getId()){
                    Log.d("hectorrr", "REGISTRANDO PROFESOR");
                    Personas persona_registro = new Personas(this.dni,etNombe.getText().toString(),etApellidos.getText().toString(),Integer.parseInt(etTelefono.getText().toString()),etCorreo.getText().toString(),etPassword.getText().toString(),true, Integer.parseInt(etCodProfesor.getText().toString()));
                    RegisterUserApi(persona_registro);
                }
                Log.d("hectorrr", "REGISTRANDO USUARIO");
            }





        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                Personas p=b.getParcelable("nou usuari");

                etDni.setText(p.getDni());
                etNombe.setText(p.getNombre());
                etApellidos.setText(p.getApellidos());
                etCorreo.setText(p.getCorreo());
                etTelefono.setText(String.valueOf(p.getTelefono()));

            }
        }
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
            etDni.setError(getResources().getText(R.string.error_dni));
            etDni.requestFocus();
            e.printStackTrace();
        }


        return res;
    }
    private void RegisterUserApi(Personas pr){
        String url="";
        if (pr.isTipo()){
            url=getResources().getText(R.string.HOST)+"/api/RestController.php?dni="+pr.getDni()+"&nombre="+pr.getNombre()+"&apellidos="+pr.getApellidos()+"&correo="+pr.getCorreo()+"&contrasenya="+pr.getPassword()+"&tipo=1&telefono="+pr.getTelefono()+"&codProf="+pr.getCod_prof();

        }
        else{
            url=getResources().getText(R.string.HOST)+"/api/RestController.php?dni="+pr.getDni()+"&nombre="+pr.getNombre()+"&apellidos="+pr.getApellidos()+"&correo="+pr.getCorreo()+"&contrasenya="+pr.getPassword()+"&tipo=0&telefono="+pr.getTelefono()+"&codProf="+pr.getCod_prof();

        }
       //String aux=url.replace(" ","%20");
        Log.d("hectorr", "estoy accediendo a la api: "+url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray loginArray=response.getJSONArray("usuarios");
                    JSONObject login=loginArray.getJSONObject(0);
                    String resultado=login.getString("existe");
                    Log.d("hectorr",resultado);
                    if(resultado.equals("1")){


                        setResult(RESULT_OK);
                        finish();

                    }else{

                        Toast.makeText(registro.this, getResources().getText(R.string.error_registro), Toast.LENGTH_SHORT).show();
                        Log.d("hectorr","REGISTRO INCORRECTO");
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d("hectorr", "Error del parse json "+e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hectorr",error.getMessage());
            }
        });

        queue.add(request);

    }
}
