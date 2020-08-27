package com.example.todolist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todolist.databinding.FragmentOasiswinterBinding;

public class OasisWinterFragment extends Fragment {
    FragmentOasiswinterBinding binding;

    /* int imageResources[] = {
             R.drawable.fox_swim, R.drawable.fox, R.drawable.fox_sleep, R.drawable.fox_red, R.drawable.fox_pilot,
             R.drawable.fox_angry,
     };

     Boolean[] farr = new Boolean[imageResources.length];*/
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
    }

}