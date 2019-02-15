package com.example.hector.floridapark.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.hector.floridapark.R;
import com.example.hector.floridapark.biblioteca.biblioteca_fragment;
import com.example.hector.floridapark.menu.menu_fragment;
import com.example.hector.floridapark.model.Personas;
import com.example.hector.floridapark.parking.parking_fragment;

public class home extends AppCompatActivity implements menu_fragment.OnMenuInteractionListener {

    private FragmentManager fm=getSupportFragmentManager();
    private FragmentTransaction transaction=fm.beginTransaction();
    private Fragment home_fragment=new home_fragment();
    private Fragment biblioteca_fragment=new biblioteca_fragment();
    private Fragment parking_fragent=new parking_fragment();
    private Personas user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setTheme(R.style.AppThemeNAB);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        transaction.replace(R.id.fragment_app, home_fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();

        Bundle b = getIntent().getExtras();
        user=b.getParcelable(getResources().getString(R.string.OBJETO_PERSONA));

    }

    @Override
    public void onMenuInteraction(int id) {
        //ID: 1-Home, 2-Parking, 3-Biblioteca, 4-Pecera, 5-Profesor
    switch (id){
        case 1:
            if(!home_fragment.isVisible()) {
                transaction=fm.beginTransaction();
                transaction.replace(R.id.fragment_app, home_fragment);
                //transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                setTitle(R.string.app_name);
            }
            break;
        case 2:
            if(!parking_fragent.isVisible()) {
                transaction=fm.beginTransaction();
                transaction.replace(R.id.fragment_app, parking_fragent);
                //transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                setTitle("Parking");
            }

            break;
        case 3:
            if(!biblioteca_fragment.isVisible()) {
                transaction=fm.beginTransaction();
                transaction.replace(R.id.fragment_app, biblioteca_fragment);
                //transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                setTitle("Biblioteca");
            }
            break;
        case 4:

            break;
        case 5:

            break;
        default:
                break;
    }

    }
}
