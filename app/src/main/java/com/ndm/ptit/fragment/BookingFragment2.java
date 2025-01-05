package com.ndm.ptit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.ndm.ptit.R;
import com.ndm.ptit.activity.GuidepageActivity2;
import com.ndm.ptit.activity.MainActivity;


public class BookingFragment2 extends Fragment {

    private AppCompatButton btnHowToExam;
    private AppCompatButton btnHomepage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking2, container, false);

        setupComponent(view);
        setupEvent();

        return view;
    }



    private void setupComponent(View view)
    {
        btnHomepage = view.findViewById(R.id.btnHomepage);
        btnHowToExam = view.findViewById(R.id.btnHowToExam);
    }


    private void setupEvent()
    {
        btnHomepage.setOnClickListener(view->{
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        btnHowToExam.setOnClickListener(view->{
            Intent intent = new Intent(requireContext(), GuidepageActivity2.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}