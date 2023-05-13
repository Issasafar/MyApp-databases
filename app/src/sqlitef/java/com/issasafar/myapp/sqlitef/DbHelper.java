package com.issasafar.myapp.sqlitef;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="users.db";
    private static final String TABLE_NAME = "users_table";
    private static final String USERS_COLUMN_NAME = "name";
    private static final String USERS_COLUMN_PASSWORD = "password";
    private static final String USERS_COLUMN_EMAIL = "email";
    private static final String sqlCreate = "CREATE TABLE " + TABLE_NAME + " (NAME TEXT,EMAIL TEXT,PASSWORD TEXT)";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }
    public boolean insertData(String name,String email,String password) {
        if (!checkExistence(email)) {
            Log.d("myapp::mysqlite", "email not exists");
            SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_NAME,name);
        contentValues.put(USERS_COLUMN_EMAIL, email);
        contentValues.put(USERS_COLUMN_PASSWORD, password);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        return result != -1;
        } else {
            Log.d("myapp::mysqlite", "email  exists");
            return false;
        }

    }

    public boolean checkExistence(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE email=?", new String[]{email});
        boolean p=result.moveToNext();
        result.close();
        return p;
    }

    public  User getUserByEmail(String email) {
        String userName;
        String userEmail;
        String userPassword;
        SQLiteDatabase sdb = this.getReadableDatabase();
        Cursor cursor = null;
        String[] args = new String[]{email};

        User user = null;
        try {
            cursor = sdb.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE EMAIL=?", args);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            }

        } finally {
            assert cursor != null;
            cursor.close();
        }
        return user;
    }
    public void getAllEmails() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result=null;
        try {
             result=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
            while (result.moveToNext()) {
                Log.d("myapp::mysqlite::get", result.getString(1));
            }
        }finally {
            assert result != null;
            result.close();
        }

    }
}
