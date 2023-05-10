package com.example.deerdiary;

import android.text.TextUtils;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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

    public boolean doesTitleAlreadyExist(){
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

        return false;
    }
}
