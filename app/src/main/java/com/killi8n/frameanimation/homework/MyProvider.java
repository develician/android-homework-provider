package com.killi8n.frameanimation.homework;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by USER on 2018-05-29.
 */

public class MyProvider extends ContentProvider {

    SQLiteDatabase sqLiteDatabase;

    static final Uri CONTENT_URI = Uri.parse("content://com.killi8n.frameanimation.homework/students");
    static final int ALLDATA = 1;
    static final int ONEDATA = 2;

    static final UriMatcher Matcher;
    static {
        Matcher = new UriMatcher(UriMatcher.NO_MATCH);
        Matcher.addURI("com.killi8n.frameanimation.homework", "students", ALLDATA);
        Matcher.addURI("com.killi8n.frameanimation.homework", "students/*", ONEDATA);
    }

    public MyProvider() {

    }


    @Override
    public boolean onCreate() {
        MyDBOpenHelper helper = new MyDBOpenHelper(getContext());
        sqLiteDatabase = helper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        String sql = "SELECT * FROM STUDENTS";
        if(Matcher.match(uri) == ONEDATA) {
            sql += " WHERE number = " + uri.getPathSegments().get(1) + ";";
        }
        return sqLiteDatabase.rawQuery(sql, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if(Matcher.match(uri) == ONEDATA) {
            return "vnd.Jenn.cursor.item/number";
        } else if(Matcher.match(uri) == ALLDATA) {
            return "vnd.Jenn.cursor.dir/students";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long row = sqLiteDatabase.insert("students", null, contentValues);
        if(row > 0) {
            Uri result_uri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(result_uri, null);
            return result_uri;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int count = 0;

        switch (Matcher.match(uri)) {
            case ALLDATA:
                count = sqLiteDatabase.delete("students", s, strings);
                break;
            case ONEDATA:
                String where;
                where = "number = " + uri.getPathSegments().get(1) + ";";
                if(TextUtils.isEmpty(s) == false) {
                    where += " AND" + s;
                }
                count = sqLiteDatabase.delete("students", where, strings);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        sqLiteDatabase.update("students", contentValues, s, strings);
        return 0;
    }
}


class MyDBOpenHelper extends SQLiteOpenHelper {

    public MyDBOpenHelper(Context context) {
        super(context, "Students2.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE students (_id INTEGER PRIMARY KEY AUTOINCREMENT, number INTEGER, name TEXT, major TEXT, grade INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}