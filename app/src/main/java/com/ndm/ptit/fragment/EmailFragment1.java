package com.ndm.ptit.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ndm.ptit.R;
import com.ndm.ptit.helper.Dialog_cus;


/**
 * @author Phong-Kaster
 * @since 03-12-2022
 */
public class EmailFragment1 extends Fragment {

    private EditText txtTitle;
    private EditText txtDescription;
    private EditText txtContent;

    private AppCompatButton btnNext;
    private Dialog_cus dialogCus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email1, container, false);

        setupComponent(view);
        setupEvent();

        return view;
    }

    /**
     * @since 03-12-2022
     */
    private void setupComponent(View view)
    {
        txtTitle = view.findViewById(R.id.txtTitle);
        txtContent = view.findViewById(R.id.txtContent);
        txtDescription = view.findViewById(R.id.txtDescription);
        btnNext = view.findViewById(R.id.btnNext);
        dialogCus = new Dialog_cus(requireContext());
    }

    /**
     * @since 03-12-2022
     */
    private void setupEvent()
    {
        btnNext.setOnClickListener(view->{
            String title = txtTitle.getText().toString();
            String content = txtContent.getText().toString();
            String description = txtDescription.getText().toString();


            dialogCus.announce();
            dialogCus.btnOK.setOnClickListener(view1-> dialogCus.close());
            if(TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || TextUtils.isEmpty(description))
            {
                dialogCus.show(R.string.attention, requireContext().getString(R.string.you_are_missing_mandatory_field), R.drawable.ic_close);
                return;
            }

            Fragment fragment = new EmailFragment2();

            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("description", description);
            bundle.putString("content", content);
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragment, "EmailFragment2")
                    .addToBackStack(null)
                    .commit();
        });
    }
}