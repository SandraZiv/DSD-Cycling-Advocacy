package com.cycling_advocacy.bumpy.ui.reportIssue;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReportIssueViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReportIssueViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is report issues fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}