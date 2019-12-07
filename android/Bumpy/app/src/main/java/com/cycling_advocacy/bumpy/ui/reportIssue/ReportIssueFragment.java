package com.cycling_advocacy.bumpy.ui.reportIssue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cycling_advocacy.bumpy.R;

public class ReportIssueFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ReportIssueViewModel reportIssueViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_report_issues, container, false);
        Spinner issue_spinner = root.findViewById(R.id.input_issue);


        reportIssueViewModel = ViewModelProviders.of(this).get(ReportIssueViewModel.class);

        reportIssueViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        ArrayAdapter arrayAdapter= ArrayAdapter.createFromResource(getContext(), R.array.type_of_issue,R.layout.color_spinner_layout);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        issue_spinner.setAdapter(arrayAdapter);
        issue_spinner.setOnItemSelectedListener(this);
        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}