package com.example.hector.floridapark.menu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.hector.floridapark.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link menu_fragment.OnMenuInteractionListener} interface
 * to handle interaction events.
 * Use the {@link menu_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class menu_fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton home, parking, biblioteca,pecera, profesor;
    private OnMenuInteractionListener mListener;

    public menu_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment menu_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static menu_fragment newInstance(String param1, String param2) {
        menu_fragment fragment = new menu_fragment();
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
        View v=inflater.inflate(R.layout.fragment_menu_fragment, container, false);

        home=(ImageButton)v.findViewById(R.id.home_button);
        parking=(ImageButton)v.findViewById(R.id.parking_button);
        biblioteca=(ImageButton)v.findViewById(R.id.library_button);
        pecera=(ImageButton)v.findViewById(R.id.fishbowl_button);
        profesor=(ImageButton)v.findViewById(R.id.teacher_button);

        home.setOnClickListener(this);
        parking.setOnClickListener(this);
        biblioteca.setOnClickListener(this);
        pecera.setOnClickListener(this);
        profesor.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==home.getId()){
            home.setImageResource(R.drawable.icon_home_hover);
            parking.setImageResource(R.drawable.icon_parking);
            biblioteca.setImageResource(R.drawable.icon_books);
            pecera.setImageResource(R.drawable.icon_fishbowl);
            profesor.setImageResource(R.drawable.icon_teacher);

            mListener.onMenuInteraction(1);
        }
        if(v.getId()==parking.getId()){
            home.setImageResource(R.drawable.icon_home);
            parking.setImageResource(R.drawable.icon_parking_hover);
            biblioteca.setImageResource(R.drawable.icon_books);
            pecera.setImageResource(R.drawable.icon_fishbowl);
            profesor.setImageResource(R.drawable.icon_teacher);

            mListener.onMenuInteraction(2);
        }
        if(v.getId()==biblioteca.getId()){
            home.setImageResource(R.drawable.icon_home);
            parking.setImageResource(R.drawable.icon_parking);
            biblioteca.setImageResource(R.drawable.icon_books_hover);
            pecera.setImageResource(R.drawable.icon_fishbowl);
            profesor.setImageResource(R.drawable.icon_teacher);

            mListener.onMenuInteraction(3);
        }
        if(v.getId()==pecera.getId()){
            home.setImageResource(R.drawable.icon_home);
            parking.setImageResource(R.drawable.icon_parking);
            biblioteca.setImageResource(R.drawable.icon_books);
            pecera.setImageResource(R.drawable.icon_fishbowl_hover);
            profesor.setImageResource(R.drawable.icon_teacher);

            mListener.onMenuInteraction(4);
        }
        if(v.getId()==profesor.getId()){
            home.setImageResource(R.drawable.icon_home);
            parking.setImageResource(R.drawable.icon_parking);
            biblioteca.setImageResource(R.drawable.icon_books);
            pecera.setImageResource(R.drawable.icon_fishbowl);
            profesor.setImageResource(R.drawable.icon_teacher_hover);

            mListener.onMenuInteraction(5);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMenuInteractionListener) {
            mListener = (OnMenuInteractionListener) context;
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

    public interface OnMenuInteractionListener {
        // TODO: Update argument type and name
        void onMenuInteraction(int id);
    }
}
