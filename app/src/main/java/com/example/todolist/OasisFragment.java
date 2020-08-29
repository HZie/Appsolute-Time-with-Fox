package com.example.todolist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.todolist.databinding.FragmentOasisBinding;
import com.example.todolist.databinding.FragmentTodoBinding;

// TODO: 오아시스(놀이방)관련 코드

public class OasisFragment extends Fragment {
    ImageButton fNormal; ImageButton fSwim; ImageButton fSleep; ImageButton fBaby; ImageButton fPilot;
    ImageButton fAngry; ImageButton fLife;

    Boolean[] farr = new Boolean[7];

    FragmentOasisBinding binding;

    int imageResources[] = {
            R.drawable.fox_swim, R.drawable.fox, R.drawable.fox_sleep, R.drawable.fox_pilot, R.drawable.fox_angry,
            R.drawable.fox_baby,  R.drawable.fox_lifeguard,
    };
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentOasisBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fNormal =view.findViewById(R.id.fNormal); fSwim =view.findViewById(R.id.fSwim);
        fSleep =view.findViewById(R.id.fSleep); fBaby =view.findViewById(R.id.fBabay);
        fPilot =view.findViewById(R.id.fPilot); fAngry =view.findViewById(R.id.fAngry);
        fLife =view.findViewById(R.id.fLifeg);

        farr = ((MainActivity)MainActivity.mcontext).getFoxLog();
        for(int i=0; i<imageResources.length; i++) {
            if (farr[i] == true) {

                if (i == 0) fSwim.setVisibility(View.VISIBLE);
                if (i == 1) fNormal.setVisibility(View.VISIBLE);
                if (i == 2) fSleep.setVisibility(View.VISIBLE);
                if (i == 3) fPilot.setVisibility(View.VISIBLE);
                if (i == 4) fBaby.setVisibility(View.VISIBLE);
                if (i == 5) fAngry.setVisibility(View.VISIBLE);
                if (i == 6) fLife.setVisibility(View.VISIBLE);
            }
        }
    }

}