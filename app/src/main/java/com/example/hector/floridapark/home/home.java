package com.example.hector.floridapark.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.hector.floridapark.R;
import com.example.hector.floridapark.biblioteca.biblioteca_fragment;
import com.example.hector.floridapark.floridaoberta.floridaoberta_fragment;
import com.example.hector.floridapark.floridaoberta.reserva_aulas_fragment;
import com.example.hector.floridapark.menu.menu_fragment;
import com.example.hector.floridapark.model.Personas;
import com.example.hector.floridapark.parking.parking_fragment;
import com.example.hector.floridapark.peceras.peceras_fragment;

public class home extends AppCompatActivity implements menu_fragment.OnMenuInteractionListener {

    private FragmentManager fm=getSupportFragmentManager();
    private FragmentTransaction transaction=fm.beginTransaction();
    private Fragment home_fragment=new home_fragment();
    private Fragment biblioteca_fragment=new biblioteca_fragment();
    private Fragment parking_fragent=new parking_fragment();
    private Fragment peceras_fragment=new peceras_fragment();
    private Fragment fragment_menu;
    private Fragment fragment_reserva_aulas;
    private Fragment floridaoberta_fragment;
    private Personas user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setTheme(R.style.AppThemeNAB);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        floridaoberta_fragment=new floridaoberta_fragment();
        Bundle b = getIntent().getExtras();
        user=b.getParcelable(getResources().getString(R.string.OBJETO_PERSONA));
        getSupportActionBar().setHomeButtonEnabled(true);
        fragment_menu = menu_fragment.newInstance(user);
        fragment_reserva_aulas=new reserva_aulas_fragment();

        transaction.replace(R.id.fragment_app, home_fragment);
        transaction.replace(R.id.fragment_menu,fragment_menu);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.commit();




    }



    @Override
    public void onMenuInteraction(int id) {
        //ID: 1-Home, 2-Parking, 3-Biblioteca, 4-Pecera, 5-Pavellon, 6-Profesor/Alumno
    switch (id){
        case 1: //boton home
            if(!home_fragment.isVisible()) {
                transaction=fm.beginTransaction();
                transaction.replace(R.id.fragment_app, home_fragment);
                //transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                setTitle(R.string.app_name);
            }
            break;
        case 2: //boton parking
            if(!parking_fragent.isVisible()) {
                transaction=fm.beginTransaction();
                transaction.replace(R.id.fragment_app, parking_fragent);
                //transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                setTitle(R.string.parking);
            }

            break;
        case 3://boton biblioteca
            if(!biblioteca_fragment.isVisible()) {
                transaction=fm.beginTransaction();
                transaction.replace(R.id.fragment_app, biblioteca_fragment);
                //transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                setTitle(R.string.bilbioteca);
            }
            break;
        case 4://boton peceras
            if(!peceras_fragment.isVisible()) {
                transaction=fm.beginTransaction();
                transaction.replace(R.id.fragment_app, peceras_fragment);
                //transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                setTitle(R.string.peceras);
            }
            break;
        case 5: //boton pavellon

            break;
        case 6:  //boton alumno/profesor
            if(user.isTipo()){
                //profesor
                if(!fragment_reserva_aulas.isVisible()) {
                    transaction=fm.beginTransaction();
                    transaction.replace(R.id.fragment_app, fragment_reserva_aulas);
                    //transaction.addToBackStack(null);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();
                    setTitle(R.string.reserva_aulas);
                }

            }
            else{
                //alumno
                if(!floridaoberta_fragment.isVisible()) {
                    transaction=fm.beginTransaction();
                    transaction.replace(R.id.fragment_app, floridaoberta_fragment);
                    //transaction.addToBackStack(null);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();
                    setTitle(R.string.florida_oberta);
                }

            }

            break;
        default:
                break;
    }

    }
}
