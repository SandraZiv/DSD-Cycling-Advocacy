package com.cycling_advocacy.bumpy.ui.reportIssue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cycling_advocacy.bumpy.R;

public class ReportIssueFragment extends Fragment {

    private ReportIssueViewModel reportIssueViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportIssueViewModel =
                ViewModelProviders.of(this).get(ReportIssueViewModel.class);
        View root = inflater.inflate(R.layout.fragment_report_issues, container, false);
        final TextView textView = root.findViewById(R.id.text_reportIssues);
        reportIssueViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}