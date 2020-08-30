package com.example.todolist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.todolist.databinding.FragmentOasiswinterBinding;

public class OasisWinterFragment extends Fragment {
    ImageButton fRed; ImageButton fGuitar; ImageButton fOwl; ImageButton fFlower; ImageButton fCold;
    ImageButton fPotato; ImageButton fWhite; ImageButton fXmas;

    Boolean[] farr = new Boolean[7];

    FragmentOasiswinterBinding binding;

    int imageResources[] = {
            R.drawable.fox_swim, R.drawable.fox, R.drawable.fox_sleep, R.drawable.fox_pilot, R.drawable.fox_angry,
            R.drawable.fox_baby,  R.drawable.fox_lifeguard, R.drawable.fox_red, R.drawable.fox_guitar, R.drawable.fox_owl,
            R.drawable.fox_flower, R.drawable.fox_cold, R.drawable.fox_spotato, R.drawable.fox_white, R.drawable.fox_xmas
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentOasiswinterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fRed =view.findViewById(R.id.fRed); fGuitar =view.findViewById(R.id.fGuitar);
        fOwl =view.findViewById(R.id.fOwl); fFlower =view.findViewById(R.id.fFlower);
        fCold =view.findViewById(R.id.fCold); fPotato =view.findViewById(R.id.fSpotato);
        fWhite =view.findViewById(R.id.fWhite); fXmas =view.findViewById(R.id.fXmas);

        farr = ((MainActivity)MainActivity.mcontext).getFoxLog();
        for(int i=0; i<imageResources.length; i++) {
            if (farr[i] == true) {

                if (i == 7) fRed.setVisibility(View.VISIBLE);
                if (i == 8) fGuitar.setVisibility(View.VISIBLE);
                if (i == 9) fOwl.setVisibility(View.VISIBLE);
                if (i == 10) fFlower.setVisibility(View.VISIBLE);
                if (i == 11) fCold.setVisibility(View.VISIBLE);
                if (i == 12) fPotato.setVisibility(View.VISIBLE);
                if (i == 13) fWhite.setVisibility(View.VISIBLE);
                if (i == 14) fXmas.setVisibility(View.VISIBLE);
            }
        }
    }

}