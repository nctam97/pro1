package com.example.ktcc;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.net.URL;
import java.util.HashMap;

public class MyContentProvider extends ContentProvider {
    static  final String AUTHOR="com.example.ktcc";
    static  final String PATH="qlnv";
    static  final String URL ="content://"+AUTHOR+"/"+PATH;
    static  final Uri    CONTENTURL=Uri.parse(URL);
    static  final String TABLENAME="NhanVien";
    private SQLiteDatabase db;
    private static HashMap<String,String>NHANVIEN_PROJECTION_MAP;
    public  static  final int ALLITEM=1;
    public  static  final int ONEITEM=2;
    static  final UriMatcher URI_MATCHER;
    static {
        URI_MATCHER=new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHOR,PATH,ALLITEM);
        URI_MATCHER.addURI(AUTHOR,PATH+"#",ONEITEM);
    }
    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (URI_MATCHER.match(uri)){
            case ALLITEM:
                count = db.delete(TABLENAME,selection,selectionArgs);
                break;
            case ONEITEM:
                String id = uri.getPathSegments().get(1);
                count = db.delete(TABLENAME,"id" + " = " + id + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""),selectionArgs);
                break;
            default: throw new UnsupportedOperationException("Unknown URL" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long number_row = db.insert(TABLENAME,"",values);
        if(number_row>0){
            Uri uri1 = ContentUris.withAppendedId(CONTENTURL, number_row);
            getContext().getContentResolver().notifyChange(uri1,null);
            return uri1;
        }
        throw new UnsupportedOperationException("Failed to add a record into" + uri);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Context context = getContext();
        DBhelper dbhelper = new DBhelper(context);
        db = dbhelper.getWritableDatabase();
        if(db==null)
            return false;
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteQueryBuilder sqlite_querybuilder=new SQLiteQueryBuilder();
        sqlite_querybuilder.setTables(TABLENAME);
        switch (URI_MATCHER.match(uri)){
            case ALLITEM:
                sqlite_querybuilder.setProjectionMap(NHANVIEN_PROJECTION_MAP);
                break;
            case ONEITEM:
                sqlite_querybuilder.appendWhere("id" + "=" + uri.getPathSegments().get(1));
                break;
        }
        if(sortOrder == null || sortOrder == ""){
            sortOrder = "name";
        }
        Cursor cursor = sqlite_querybuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return  cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (URI_MATCHER.match(uri)){
            case ALLITEM:
                count = db.update(TABLENAME,values,selection,selectionArgs);
                break;
            case ONEITEM:

                count = db.update(TABLENAME,values,"id" + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""),selectionArgs);
                break;
            default: throw new UnsupportedOperationException("Unknown URL" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
