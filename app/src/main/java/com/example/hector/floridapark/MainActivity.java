package com.example.hector.floridapark;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hector.floridapark.home.home;
import com.example.hector.floridapark.model.Personas;
import com.example.hector.floridapark.registro.registro;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button entrar, registrarse;
    private EditText login_correo, login_password;
    private CheckBox recordar;
    private RequestQueue queue;
    private final int ACTIVITY_REGISTRE = 2;

    private Personas user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeNAB);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        entrar=(Button)findViewById(R.id.bt_entrar);
        registrarse=(Button) findViewById(R.id.bt_registrar);
        login_correo=(EditText)findViewById(R.id.et_login_email);
        login_password=(EditText)findViewById(R.id.et_login_password);
        recordar=(CheckBox)findViewById(R.id.cb_recordar);

        entrar.setOnClickListener(this);
        registrarse.setOnClickListener(this);

        queue= Volley.newRequestQueue(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==entrar.getId()){

            LoginUserApi(login_correo.getText().toString(),login_password.getText().toString());
                
            
            
        }
        if (v.getId()==registrarse.getId()){
            Intent i = new Intent(getApplicationContext(), registro.class);
            startActivityForResult(i, ACTIVITY_REGISTRE);
        }
    }

    private void LoginUserApi(String correo, String password){
        String url="http://floridaservices.cf/api/users/"+correo+"/"+password+"/";
        Log.d("hectorr", "estoy accediendo a la api: "+url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray loginArray=response.getJSONArray("usuarios");
                    Log.d("hectorr",loginArray.getJSONObject(0).toString());
                    Log.d("hectorr",loginArray.get(0).toString());

                    JSONObject login=loginArray.getJSONObject(0);
                    String resultado;
                    Log.d("hectorr",login.keys().next());
                    if (login.keys().next().compareTo("dni")==0) {
                        resultado = "chachi";
                        user = new Gson().fromJson(loginArray.get(0).toString(), Personas.class);
                        user.setPassword(login_password.getText().toString());
                        Log.d("hectorr",user.toString());
                    }
                    else
                    {
                        resultado="error";
                    }

                    Log.d("hectorr",resultado);
                    if(resultado.equals("chachi")){
                        
                        Log.d("hectorr","LOGIN CORRECTO");

                        Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), home.class);
                        Bundle buser=new Bundle();
                        buser.putParcelable(getResources().getString(R.string.OBJETO_PERSONA),user);
                        i.putExtras(buser);
                        startActivity(i);
                    }else{
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        Log.d("hectorr","LOGIN INCORRECTO");
                    }

                    //Toast.makeText(MainActivity.this, resultado, Toast.LENGTH_SHORT).show();
                   // Log.d("hectorr", "Resultado del login: "+login.getBoolean("login"));
                    //result[0] =login.getBoolean("login");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ACTIVITY_REGISTRE){
            if(resultCode==RESULT_OK){
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
