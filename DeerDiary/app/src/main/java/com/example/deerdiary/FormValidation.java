package com.example.deerdiary;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;

public class FormValidation {
    TextView titleField;
    TextView contentField;
    ArrayList<DiaryEntry> diaryEntries;

    public FormValidation(TextView titleField, TextView contentField, ArrayList<DiaryEntry> diaryEntries){
        this.titleField = titleField;
        this.contentField = contentField;
        this.diaryEntries = diaryEntries;
    }
    public boolean areFieldsPopulated() {
        if (TextUtils.isEmpty(titleField.getText())){
            titleField.setError("Title cannot be empty");
            titleField.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(contentField.getText())) {
            contentField.setError("Content cannot be empty");
            contentField.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public boolean titleValidation(){
        if (diaryEntries != null) {
            for (DiaryEntry entry : diaryEntries) {

                // look for any equivalent strings in titles of the current user's diaries
                if (entry.getTitle().equals(titleField.getText().toString())) {
                    titleField.setError("Title already exists");
                    titleField.requestFocus();
                    return true;
                }
            }
        }
        if(titleField.getText().toString().length() >= 20){
            titleField.setError("Title cannot be longer than 20 characters");
            titleField.requestFocus();
            return true;
        }

        return false;
    }
}
