package com.killi8n.frameanimation.homework;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private static final String CONTENT_URI = "content://com.killi8n.frameanimation.homework/students";

    private LinearLayout detailView, updateView;
    private TextView numberTextView, numberContentTextView,
                    nameTextView, nameContentTextView,
                    majorTextView, majorContentTextView,
                    gradeTextView, gradeContentTextView;
    private Button updateButton, cancelUpdateButton, commitUpdateButton, removeButton;
    private EditText updateNumberEditText, updateNameEditText, updateMajorEditText, updateGradeEditText;
    private Cursor cursor;
    private ContentResolver contentResolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        contentResolver = getContentResolver();
        cursor = contentResolver.query(Uri.parse(CONTENT_URI), null, null, null, null);

        Intent intent = getIntent();
        final int id = intent.getIntExtra("id", -1);
        final int number = intent.getIntExtra("number", -1);
        final String name = intent.getStringExtra("name");
        final String major = intent.getStringExtra("major");
        final int grade = intent.getIntExtra("grade", -1);

        detailView = (LinearLayout) findViewById(R.id.detailView);
        updateView = (LinearLayout) findViewById(R.id.updateView);


        updateNumberEditText = (EditText) findViewById(R.id.updateNumberEditText);
        updateNameEditText = (EditText) findViewById(R.id.updateNameEditText);
        updateMajorEditText = (EditText) findViewById(R.id.updateMajorEditText);
        updateGradeEditText = (EditText) findViewById(R.id.updateGradeEditText);


        updateButton = (Button) findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailView.setVisibility(View.GONE);
                updateView.setVisibility(View.VISIBLE);
                updateNumberEditText.setText(number + "");
                updateNameEditText.setText(name);
                updateMajorEditText.setText(major + "");
                updateGradeEditText.setText(grade + "");
            }
        });

        cancelUpdateButton = (Button) findViewById(R.id.cancelUpdateButton);
        cancelUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailView.setVisibility(View.VISIBLE);
                updateView.setVisibility(View.GONE);
            }
        });

        removeButton = (Button) findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentResolver.delete(Uri.parse(CONTENT_URI + "/" + number), null, null);
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });



        commitUpdateButton = (Button) findViewById(R.id.commitUpdateButton);
        commitUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues row = new ContentValues();
                row.put("number", Integer.parseInt(updateNumberEditText.getText().toString()));
                row.put("name", updateNameEditText.getText().toString());
                row.put("major", updateMajorEditText.getText().toString());
                row.put("grade", Integer.parseInt(updateGradeEditText.getText().toString()));
                contentResolver.update(Uri.parse(CONTENT_URI), row, "_id=" + id, null);
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

        numberTextView = (TextView) findViewById(R.id.numberTextView);
        numberTextView.setText("학번: ");
        numberContentTextView = (TextView) findViewById(R.id.numberContentTextView);
        numberContentTextView.setText(number + "");
//
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText("이름: ");
        nameContentTextView = (TextView) findViewById(R.id.nameContentTextView);
        nameContentTextView.setText(name);

        majorTextView = (TextView) findViewById(R.id.majorTextView);
        majorTextView.setText("학과: ");
        majorContentTextView = (TextView) findViewById(R.id.majorContentTextView);
        majorContentTextView.setText(major);
//
        gradeTextView = (TextView) findViewById(R.id.gradeTextView);
        gradeTextView.setText("학년: ");
        gradeContentTextView = (TextView) findViewById(R.id.gradeContentTextView);
        gradeContentTextView.setText(grade + "");

    }
}
