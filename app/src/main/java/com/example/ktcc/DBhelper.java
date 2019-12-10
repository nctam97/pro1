package com.example.ktcc;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {
    public DBhelper(@Nullable Context context) {
        super(context, "qlnv", null, 11);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table NhanVien(" + "id integer" +
                ",name text" +
                ",address text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists NhanVien");
        onCreate(db);
    }
    public boolean loadData(){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("id",1);
        contentValues.put("name","chi tam");
        contentValues.put("address","govap");
        int result= (int)db.insert("NhanVien",null,contentValues);
        contentValues=new ContentValues();
        contentValues.put("id",2);
        contentValues.put("name","thanh tin");
        contentValues.put("address","govap");
        result= (int)db.insert("NhanVien",null,contentValues);
        db.close();
        return true;
    }

}
