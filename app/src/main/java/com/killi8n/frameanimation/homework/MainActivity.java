package com.killi8n.frameanimation.homework;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private static final String CONTENT_URI = "content://com.killi8n.frameanimation.homework/students";

    private static final int RQCODE_INSERT = 1;
    private static final int RQCODE_UPDATE = 2;
    private static final int RQCODE_DELETE = 3;


    private Button addButton, cancelButton, insertButton;
    private LinearLayout addView, studentListView;
    private ListView listView;
    private Cursor cursor;
    private ContentResolver contentResolver;
    private EditText numberEditText, nameEditText, majorEditText, gradeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentResolver = getContentResolver();
        cursor = contentResolver.query(Uri.parse(CONTENT_URI), null, null, null, null);

        final SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{"number", "name"},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(cursorAdapter);

//        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cursor.moveToPosition(i);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                int id = cursor.getInt(0);
                int number = cursor.getInt(1);
                String name = cursor.getString(2);
                String major = cursor.getString(3);
                int grade = cursor.getInt(4);

                intent.putExtra("id", id);
                intent.putExtra("number", number);
                intent.putExtra("name", name);
                intent.putExtra("major", major);
                intent.putExtra("grade", grade);
                startActivityForResult(intent, RQCODE_UPDATE);
            }
        });

        addView = (LinearLayout) findViewById(R.id.addView);
        studentListView = (LinearLayout) findViewById(R.id.studentListView);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView.setVisibility(View.VISIBLE);
                studentListView.setVisibility(View.GONE);
            }
        });

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView.setVisibility(View.GONE);
                studentListView.setVisibility(View.VISIBLE);
            }
        });

        numberEditText = (EditText) findViewById(R.id.numberEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        majorEditText = (EditText) findViewById(R.id.majorEditText);
        gradeEditText = (EditText) findViewById(R.id.gradeEditText);

        insertButton = (Button) findViewById(R.id.insertButton);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues row = new ContentValues();
                row.put("number", Integer.parseInt(numberEditText.getText().toString()));
                row.put("name", nameEditText.getText().toString());
                row.put("major", majorEditText.getText().toString());
                row.put("grade", Integer.parseInt(gradeEditText.getText().toString()));
                contentResolver.insert(Uri.parse(CONTENT_URI), row);
                cursor.requery();
                addView.setVisibility(View.GONE);
                studentListView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case RQCODE_UPDATE:
                    cursor.requery();
            }
        }
    }
}
