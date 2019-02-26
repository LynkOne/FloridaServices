package com.example.hector.floridapark.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hector.floridapark.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class home_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int pecerasLibres;
    private int plazasLibres;
    private RequestQueue queue;
    private TextView tvResumenPlazas, tvResumenPeceras;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public home_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static home_fragment newInstance(String param1, String param2) {
        home_fragment fragment = new home_fragment();
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
        View v=inflater.inflate(R.layout.fragment_home_fragment, container, false);
        queue= Volley.newRequestQueue(getContext());
        tvResumenPeceras=(TextView)v.findViewById(R.id.home_resumen_peceras);
        tvResumenPlazas=(TextView)v.findViewById(R.id.home_resumen_plazas);
        getStats();
        return v;
    }
    public void getStats(){
        String url=getResources().getText(R.string.HOST)+"/api/peceras/getpeceras/libres/";
        Log.d("hectorr", "estoy accediendo a la api: "+url);

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray pecerasArray=response.getJSONArray("peceras");
                    pecerasLibres=pecerasArray.length()+1;
                    tvResumenPeceras.setText(pecerasLibres+"/15");
                    //Log.d("hectorr",pecerasLibres+" peceras libres");
                }catch (Exception e){
                    Log.d("hectorr", "Error del parse json "+e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hectorr","error respuesta getPeceras libres API");
            }
        });
        queue.add(request);
        String url2=getResources().getText(R.string.HOST)+"/api/parking/getplazas/libres/";
        Log.d("hectorr", "estoy accediendo a la api: "+url2);

        JsonObjectRequest request2=new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray plazasArray=response.getJSONArray("plazas");
                    plazasLibres=plazasArray.length()+1;
                    tvResumenPlazas.setText(plazasLibres+"/253");
                    //Log.d("hectorr",plazasLibres+" plazas libres");
                }catch (Exception e){
                    Log.d("hectorr", "Error del parse json "+e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hectorr","error respuesta getPeceras libres API");
            }
        });
        queue.add(request2);
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
    }*/

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
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
