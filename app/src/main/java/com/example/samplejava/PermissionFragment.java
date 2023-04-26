package com.example.samplejava;

import kotlin.Unit;
import android.util.Log;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.pngme.sdk.library.PngmeSdk;
import android.content.pm.PackageManager;

import com.example.samplejava.databinding.FragmentFirstBinding;

public class PermissionFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();

                if (PngmeSdk.INSTANCE.isPermissionGranted(mainActivity)) {
                    PngmeSdk.INSTANCE.resetPermissionFlow(mainActivity);
                }
                
                PngmeSdk.INSTANCE.go(
                        mainActivity,
                        "PNGME_SDK_TOKEN",
                        "FIRST_NAME",
                        "LAST_NAME",
                        "EMAIL",
                        "PHONE_NUMBER",
                        "", // external id
                        "ACME BANK",
                        false, // hide Pngme Dialog
                        PermissionFragment.this::onComplete);
            }
        });
    }

    private final Unit onComplete() {
        return Unit.INSTANCE;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}