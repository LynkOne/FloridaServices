package com.example.hector.floridapark.peceras;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hector.floridapark.R;
import com.example.hector.floridapark.model.Peceras;
import com.example.hector.floridapark.model.Personas;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class peceras_fragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PERSONAS = "personas";
    private ArrayList<TextView> salas;
    private Peceras pecera;
    private Peceras auxPecera_json;
    private Personas usuario;
    private ArrayList<Peceras> alPecerasJson;
    private RequestQueue queue;

    //private OnFragmentInteractionListener mListener;

    public peceras_fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static peceras_fragment newInstance(Personas p) {
        peceras_fragment fragment = new peceras_fragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PERSONAS, p);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = getArguments().getParcelable(ARG_PERSONAS);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_peceras_fragment, container, false);
        queue= Volley.newRequestQueue(getContext());
        salas=new ArrayList<TextView>();
        alPecerasJson=new ArrayList<Peceras>();
        for(int i=1;i<=15;i++){
            salas.add((TextView) v.findViewWithTag("pecera"+i));
            Log.d("hectorr","Findview Pecera: pecera"+i);
            salas.get(i-1).setOnClickListener(this);
        }



        actualizarPeceras();
        return v;
    }

    @Override
    public void onClick(View v) {
        for (TextView tv:salas) {
            if (v.getId() == tv.getId()) {

                String pecera_aux_onclick="";
                if(tv.getTag().toString().length()==7){
                    pecera_aux_onclick=""+tv.getTag().toString().charAt(6);
                }
                if(tv.getTag().toString().length()==8){
                    pecera_aux_onclick=""+tv.getTag().toString().charAt(6)+tv.getTag().toString().charAt(7);
                }
                Log.d("hectorr","Pecera seleccionada: "+pecera_aux_onclick);
            }
        }
    }
    public void actualizarPeceras(){
        String url=getResources().getText(R.string.HOST)+"/api/peceras/getpeceras/ocupadas/";
        Log.d("hectorr", "estoy accediendo a la api: "+url);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            try{
                JSONArray pecerasArray=response.getJSONArray("peceras");
                Log.d("hectorr",pecerasArray.toString());
                alPecerasJson.clear();
                for(int i=0;i<pecerasArray.length();i++){
                    int auxIdPecera=pecerasArray.getJSONObject(i).getInt("id");
                    String auxTiempoReservado=pecerasArray.getJSONObject(i).getString("tiempo_reservado");
                    String auxHoraDeReserva=pecerasArray.getJSONObject(i).getString("hora_de_reserva");
                    String auxDniUsuario=pecerasArray.getJSONObject(i).getString("dni_usuario_reserva");

                    Time auxTiempoReservadoParsed=Time.valueOf(auxTiempoReservado);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date auxHoraDeReservaParsed = sdf.parse(auxHoraDeReserva);

                    auxPecera_json = new Peceras(auxIdPecera,auxTiempoReservadoParsed,auxHoraDeReservaParsed,auxDniUsuario);
                    alPecerasJson.add(auxPecera_json);
                    //Log.d("hectorr","Pecera: "+auxIdPecera+" Tiempo reservado: "+auxTiempoReservado+" Hora de Reserva: "+auxHoraDeReserva+" DNI usuario: "+auxDniUsuario);
                }

                for (Peceras pcr:alPecerasJson) {
                    salas.get(pcr.getId()-1).setBackgroundColor(Color.parseColor("#B40404"));
                    Log.d("hectorr", "Cambiando background de la sala ocupada "+pcr.getId());
                }


            }catch (Exception e){
                Log.d("hectorr", "Error del parse json "+e.getMessage());
            }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hectorr","error respuesta getPeceras ocupadas API");
                String url=getResources().getText(R.string.HOST)+"/api/peceras/getpeceras/libres/";
                JsonObjectRequest request2=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray pecerasArray=response.getJSONArray("peceras");
                            Log.d("hectorr",pecerasArray.toString());
                            alPecerasJson.clear();
                            for(int i=0;i<pecerasArray.length();i++){
                                int auxIdPecera=pecerasArray.getJSONObject(i).getInt("id");
                                String auxTiempoReservado=pecerasArray.getJSONObject(i).getString("tiempo_reservado");
                                String auxHoraDeReserva=pecerasArray.getJSONObject(i).getString("hora_de_reserva");
                                String auxDniUsuario=pecerasArray.getJSONObject(i).getString("dni_usuario_reserva");

                                Time auxTiempoReservadoParsed=Time.valueOf(auxTiempoReservado);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date auxHoraDeReservaParsed = sdf.parse(auxHoraDeReserva);

                                auxPecera_json = new Peceras(auxIdPecera,auxTiempoReservadoParsed,auxHoraDeReservaParsed,auxDniUsuario);
                                alPecerasJson.add(auxPecera_json);
                                //Log.d("hectorr","Pecera: "+auxIdPecera+" Tiempo reservado: "+auxTiempoReservado+" Hora de Reserva: "+auxHoraDeReserva+" DNI usuario: "+auxDniUsuario);
                            }

                            for (Peceras pcr:alPecerasJson) {
                                salas.get(pcr.getId()-1).setBackgroundColor(Color.parseColor("#ef9a3f"));
                                Log.d("hectorr", "Cambiando background de la sala libre "+pcr.getId());
                            }


                        }catch (Exception e){
                            Log.d("hectorr", "Error del parse json "+e.getMessage());
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("hectorr","error respuesta getPeceras ocupadas API");

                    }
                });
                queue.add(request2);
            }
        });
        queue.add(request);
    }
    public void reservarPecera(Peceras pcr, Personas per){

    }
    public void anularReserva(Peceras pcr, Personas per){

    };
/*


    @Override
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */
}
