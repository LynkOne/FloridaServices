package com.example.hector.floridapark.peceras;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
    private ArrayList<Peceras> alPecerasJsonOcupadas;
    private RequestQueue queue;
    private String pecera_aux_onclick;

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
        alPecerasJsonOcupadas=new ArrayList<Peceras>();
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
                //Obtenemos el número de la pecera seleccionada
                pecera_aux_onclick="";
                if(tv.getTag().toString().length()==7){
                    pecera_aux_onclick=""+tv.getTag().toString().charAt(6);
                }
                if(tv.getTag().toString().length()==8){
                    pecera_aux_onclick=""+tv.getTag().toString().charAt(6)+tv.getTag().toString().charAt(7);
                }
                Log.d("hectorr","Pecera seleccionada: "+pecera_aux_onclick+" Index: "+(Integer.parseInt(pecera_aux_onclick)-1));
                //Si el array de peceras parseado de Json no esta vacio, y el númerono es nulo
                if(!alPecerasJson.isEmpty()&& pecera_aux_onclick.compareTo("")!=0){
                    final Peceras auxObjPecera = alPecerasJson.get(Integer.parseInt(pecera_aux_onclick)-1);
                    if (auxObjPecera.isOcupada()){
                        if(auxObjPecera.getDni_usuario_reserva().compareTo(usuario.getDni())==0){
                            //El dni del usuario es el mismo que el de la pecera, pude cancelar reserva
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                            alertBuilder.setTitle(getResources().getText(R.string.titulo_dialog_abandonar_pecera));
                            // Botón ok
                            alertBuilder.setPositiveButton(getResources().getText(R.string.boton_dialog_pecera_abandonar), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Abanonar pecera
                                    anularReserva(auxObjPecera,usuario);
                                    //cerrar dialog
                                    dialog.dismiss();

                                }
                            });
                            alertBuilder.setNegativeButton(getResources().getText(R.string.boton_dialog_pecera_cancelar), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //cerrar dialog
                                    dialog.dismiss();

                                }
                            });


                            alertBuilder.show();
                        }else{

                            //No es el mismo DNI, mostrar quien está dentro y a que hora aprox. sale.
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                            alertBuilder.setTitle(getResources().getText(R.string.titulo_dialog_pecera_ocupada));
                            String aux_fin_reserva="";
                            for (Peceras p:alPecerasJsonOcupadas) {
                                if(p.getId()==alPecerasJson.get(Integer.parseInt(pecera_aux_onclick)-1).getId()){
                                    aux_fin_reserva=p.getFin_de_reserva().toString();
                                    for (Peceras p2:alPecerasJson) {
                                        if(p.getId()==p2.getId()){
                                            p2.setFin_de_reserva(p.getFin_de_reserva());
                                        }
                                    }
                                }

                            }
                            //Generamos el string del dialog
                            String auxAlertString = getResources().getText(R.string.contenido_dialog_pecera_ocupada_usuario).toString()+auxObjPecera.getDni_usuario_reserva().toString()+getResources().getText(R.string.contenido_dialog_pecera_ocupada_tiempofin).toString()+auxObjPecera.getFin_de_reserva();
                            alertBuilder.setMessage(auxAlertString);
                            Log.d("hectorr","Fecha fin de reserva: "+(auxObjPecera.getHora_de_reserva().toString()+auxObjPecera.getTiempo_reservado()));

                            // Botón ok
                            alertBuilder.setPositiveButton(getResources().getText(R.string.boton_ok_dialog_pecera_ocupada), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //cerrar dialog
                                    dialog.dismiss();

                                }
                            });


                            alertBuilder.show();
                        }
                    }
                    else{
                       //La pecera esta disponible
                    }




                }
            }
        }
    }
    public void actualizarPeceras(){
        //Pintar todas
        String url=getResources().getText(R.string.HOST)+"/api/peceras/getpeceras/todas/";
        Log.d("hectorr", "estoy accediendo a la api: "+url);
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

                    ///Pintar ocupadas

                    String url2=getResources().getText(R.string.HOST)+"/api/peceras/getpeceras/ocupadas/";
                    Log.d("hectorr", "estoy accediendo a la api: "+url2);
                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                JSONArray pecerasArray=response.getJSONArray("peceras");
                                Log.d("hectorr",pecerasArray.toString());
                                alPecerasJsonOcupadas.clear();
                                for(int i=0;i<pecerasArray.length();i++){
                                    int auxIdPecera=pecerasArray.getJSONObject(i).getInt("id");
                                    String auxTiempoReservado=pecerasArray.getJSONObject(i).getString("tiempo_reservado");
                                    String auxHoraDeReserva=pecerasArray.getJSONObject(i).getString("hora_de_reserva");
                                    String auxDniUsuario=pecerasArray.getJSONObject(i).getString("dni_usuario_reserva");
                                    String auxFinReserva=pecerasArray.getJSONObject(i).getString("fin_de_reserva");
                                    Time auxTiempoReservadoParsed=Time.valueOf(auxTiempoReservado);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date auxHoraDeReservaParsed = sdf.parse(auxHoraDeReserva);
                                    Time auxTiempoFinReserva=Time.valueOf(auxFinReserva);
                                    auxPecera_json = new Peceras(auxIdPecera,auxTiempoReservadoParsed,auxHoraDeReservaParsed,auxDniUsuario,auxTiempoFinReserva);
                                    alPecerasJsonOcupadas.add(auxPecera_json);
                                    //Log.d("hectorr","Pecera: "+auxIdPecera+" Tiempo reservado: "+auxTiempoReservado+" Hora de Reserva: "+auxHoraDeReserva+" DNI usuario: "+auxDniUsuario);
                                }

                                for (Peceras pcr:alPecerasJsonOcupadas) {
                                    salas.get(pcr.getId()-1).setBackgroundColor(Color.parseColor("#B40404"));
                                    pcr.setOcupada(true);

                                    for (Peceras p2:alPecerasJson) {
                                        if(pcr.getId()==p2.getId()){
                                            p2.setFin_de_reserva(pcr.getFin_de_reserva());
                                            p2.setOcupada(true);
                                        }
                                    }

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

                        }
                    });
                    queue.add(request);


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
    public void reservarPecera(Peceras pcr, Personas per){
    }
    public void anularReserva(Peceras pcr, Personas per){
        String url=getResources().getText(R.string.HOST)+"/api/peceras/abandonarpecera/"+pcr.getId()+"/"+per.getDni()+"/";
        Log.d("hectorr", "estoy accediendo a la api: "+url);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            actualizarPeceras();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }
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
