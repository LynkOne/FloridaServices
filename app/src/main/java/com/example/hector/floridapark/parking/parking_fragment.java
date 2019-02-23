package com.example.hector.floridapark.parking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hector.floridapark.MainActivity;
import com.example.hector.floridapark.R;
import com.example.hector.floridapark.home.home;
import com.example.hector.floridapark.model.Personas;
import com.example.hector.floridapark.model.Plazas;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class parking_fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<ImageView> plazas;
    private Plazas auxPlaza;
    private ArrayList<Plazas> alPlazas;
    private String letra_aux_plazas="A";
    protected final static String PREFS = "preferencias";
    private SharedPreferences.Editor editor;
    private SharedPreferences preferencias;
    private View viewAuxiliar;
    private RequestQueue queue;
    private Timer myTimer;

    ImageView prova;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

   // private OnFragmentInteractionListener mListener;

    public parking_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment parking_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static parking_fragment newInstance(String param1, String param2) {
        parking_fragment fragment = new parking_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_parking_fragment, container, false);
        viewAuxiliar = v;
        preferencias = v.getContext().getSharedPreferences(parking_fragment.PREFS, Activity.MODE_PRIVATE);
        editor = preferencias.edit();
        queue= Volley.newRequestQueue(getContext());
        plazas=new ArrayList<ImageView>();
        alPlazas=new ArrayList<Plazas>();
        letra_aux_plazas="A";
        for(int i=1;i<=39;i++){
            Log.d("hectorr","asignando plaza por tag "+letra_aux_plazas+i);
            plazas.add((ImageView) v.findViewWithTag("plaza"+letra_aux_plazas+i));

            if (i == 39) {
                switch (letra_aux_plazas){
                    case "A":
                        letra_aux_plazas="B";
                        i=0;
                        break;
                    case "B":
                        letra_aux_plazas="C";
                        i=0;
                        break;
                    case "C":
                        letra_aux_plazas="D";
                        i=0;
                        break;
                    case "D":
                        letra_aux_plazas="E";
                        i=0;
                        break;
                }
            }
            if(letra_aux_plazas=="E" && i==21){
                letra_aux_plazas="F";
                i=0;
            }
            if(letra_aux_plazas=="F" && i==32){
                letra_aux_plazas="G";
                i=0;
            }
            if(letra_aux_plazas=="G" && i==25){
                letra_aux_plazas="H";
                i=0;
            }
            if(letra_aux_plazas=="H" && i==19){
                i=0;
                break;
            }
        }

        //Vaciamos todas las plazas y generamos el OnClickListener de cada una de ellas
        for (ImageView iv:plazas) {
            Log.d("hectorr","vaciando plaza "+iv.getTag().toString()+" y creando listener");

            iv.setOnClickListener(this);
        }
        //Rellenamos las plazas
        rellenarPlazas();
        //Comprobamos si hay preferencias guardadas y pintamos la plaza donde hemos aparcado en caso de haber preferencias
        if(preferencias.getString("plaza_aparcado","").compareTo("")!=0){

            ImageView aux=v.findViewWithTag(preferencias.getString("plaza_aparcado",""));
            aux.setImageResource(R.drawable.carmine);
        }

        return v;
    }



    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.

            //Do something to the UI thread here

        }
    };
    @Override
    public void onClick(View v) {
        for (ImageView iv:plazas) {
            if(v.getId()==iv.getId()){
                //Comprobamos si habiamos guardado la plaza donde hemos aparcado
                if(preferencias.getString("plaza_aparcado","").compareTo("")!=0){
                    Log.d("hectorr","plaza donde estabas aparcado: "+preferencias.getString("plaza_aparcado",""));
                    //Creamos un imageView auxiliar con la plaza donde estabamos aparcados anteriormente
                    ImageView aux=viewAuxiliar.findViewWithTag(preferencias.getString("plaza_aparcado",""));
                    //Le quitamos el coche rojo
                    aux.setImageResource(0);
                    if(v.getId()!=aux.getId()){
                        //Si la plaza seleccionada es diferente de la anterior, actualizamos la plaza nueva con el coche rojo y la guardamos en preferencias
                        editor.putString("plaza_aparcado", iv.getTag().toString());
                        editor.commit();
                        iv.setImageResource(R.drawable.carmine);
                    }
                }
                //Si no teniamos una plaza guardada, simplemente la guardamos
                else{
                    ImageView aux=v.findViewById(iv.getId());
                    editor.putString("plaza_aparcado", iv.getTag().toString());
                    editor.commit();
                    iv.setImageResource(R.drawable.carmine);
                }

            }

        }
    }

    public void rellenarPlazas(){
        String url=getResources().getText(R.string.HOST)+"/api/parking/getplazas/todas/";
        Log.d("hectorr", "estoy accediendo a la api: "+url);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray plazasArray=response.getJSONArray("plazas");
                    Log.d("hectorr",plazasArray.toString());
                    for(int i=0;i<plazasArray.length();i++){
                        JSONObject plaza=plazasArray.getJSONObject(i);
                        Log.d("hectorr",plaza.getString("plaza"));
                        auxPlaza = new Gson().fromJson(plazasArray.getJSONObject(i).toString(), Plazas.class);
                        alPlazas.add(auxPlaza);
                    }
                    for (Plazas p:alPlazas) {
                        String strplazaaux="plaza"+p.getPlaza();
                        ImageView auximplaza=getView().findViewWithTag(strplazaaux);
                        if(p.isOcupado()){
                            auximplaza.setImageResource(R.drawable.car);
                        }
                        else{
                            auximplaza.setImageResource(0);
                        }
                    }



                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d("hectorr", "Error del parse json "+e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hectorr","error respuesta getPlazas API");
            }
        });

        queue.add(request);
    }


/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

}
