package com.example.hector.floridapark.parking;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hector.floridapark.MainActivity;
import com.example.hector.floridapark.R;

import java.util.ArrayList;


public class parking_fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<ImageView> plazas;
    private String letra_aux_plazas="A";
    protected final static String PREFS = "preferencias";
    private SharedPreferences.Editor editor;
    private SharedPreferences preferencias;
    private View viewAuxiliar;

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
        plazas=new ArrayList<ImageView>();
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
       // String rdo_consulta="C1";
       // prova = (ImageView) v.findViewWithTag("plaza"+rdo_consulta);
        //prova.setImageResource(0);

        for (ImageView iv:plazas) {
            Log.d("hectorr","rellenando plaza "+iv.getTag().toString());
            iv.setImageResource(0);
            iv.setOnClickListener(this);
        }
        if(preferencias.getString("plaza_aparcado","").compareTo("")!=0){

            ImageView aux=v.findViewWithTag(preferencias.getString("plaza_aparcado",""));
            aux.setImageResource(R.drawable.carmine);
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        for (ImageView iv:plazas) {
            if(v.getId()==iv.getId()){
                if(preferencias.getString("plaza_aparcado","").compareTo("")!=0){
                    Log.d("hectorr","plaza donde estabas aparcado: "+preferencias.getString("plaza_aparcado",""));
                    ImageView aux=viewAuxiliar.findViewWithTag(preferencias.getString("plaza_aparcado",""));
                    aux.setImageResource(0);
                    if(v.getId()!=aux.getId()){
                        editor.putString("plaza_aparcado", iv.getTag().toString());
                        editor.commit();
                        iv.setImageResource(R.drawable.carmine);
                    }
                }
                else{
                    ImageView aux=v.findViewById(iv.getId());
                    editor.putString("plaza_aparcado", iv.getTag().toString());
                    editor.commit();
                    iv.setImageResource(R.drawable.carmine);
                }

            }

        }
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
