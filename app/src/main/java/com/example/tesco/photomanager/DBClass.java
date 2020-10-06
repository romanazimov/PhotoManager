package com.example.tesco.photomanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DBClass extends SQLiteOpenHelper {


    public DBClass(Context context) {

        super(context, "photo manager", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table photostable (id integer primary key,path text,person_tag text ,location_tag text,folder_id text,caption text,captureTime)");
        db.execSQL("create table foldertable (f_id integer primary key,f_name text)");
    }

    public long AddFolder(String folderName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        values.put("f_name", folderName);
        long a = db.insert("foldertable", null, values);
        Log.i("TAG", "AddFolder: " + folderName + a);
        return a;
    }

    public int CountFolders() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM foldertable";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        return icount;
    }

    public int CountImagess(String t_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM photostable Where name=" + t_name;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        return icount;
    }

    public int MoveImage(String imageId, String folderName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select f_id from foldertable where f_name = ?", new String[]{folderName}); // where       images of which folder
        cursor.moveToFirst();
        String id = null;
        if (!cursor.isAfterLast()) {
            id = cursor.getString(cursor.getColumnIndex("f_id"));
        }
        cursor.close();
        if (id == null) {
            return -1;
        }
        Log.i("TAG", id + " Folder ID");
        ContentValues values;
        values = new ContentValues();
        values.put("folder_id", id);

        return db.update("photostable", values, "id" + " = ? ", new String[]{imageId});

    }

    public long CopyImage(ImageInfo imageInfo, String folderName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select f_id from foldertable where f_name = ?", new String[]{folderName}); // where       images of which folder
        cursor.moveToFirst();
        String id = null;
        if (!cursor.isAfterLast()) {
            id = cursor.getString(cursor.getColumnIndex("f_id"));
        }
        cursor.close();
        if (id == null) {
            return -1;
        }
        Log.i("TAG", id + " Folder ID");
        ContentValues values;
        values = new ContentValues();
        values.put("path", imageInfo.path);
        values.put("folder_id", id);
        values.put("captureTime", imageInfo.captureTime);
        values.put("caption", imageInfo.caption);
        return db.insert("photostable", null, values);

    }

    public List<FolderInfo> getAllFolders() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<FolderInfo> imageInfoArrayList = new ArrayList<>();
        FolderInfo folderInfo;
        Cursor cursor = db.rawQuery("select * from foldertable", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            folderInfo = new FolderInfo();
            folderInfo.id = cursor.getString(cursor.getColumnIndex("f_id"));
            folderInfo.name = cursor.getString(cursor.getColumnIndex("f_name"));
            imageInfoArrayList.add(folderInfo);

            cursor.moveToNext();
        }
        cursor.close();
        return imageInfoArrayList;
    }

    public List<String> getAllFoldersNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> imageInfoArrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from foldertable", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            imageInfoArrayList.add(cursor.getString(cursor.getColumnIndex("f_name")));

            cursor.moveToNext();
        }
        cursor.close();
        return imageInfoArrayList;
    }

    public long AddImage(ImageInfo imageInfo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        values.put("path", imageInfo.path);
        values.put("folder_id", imageInfo.folderId);
        values.put("captureTime", imageInfo.captureTime);
        values.put("caption", imageInfo.caption);
        return db.insert("photostable", null, values);
    }

    public List<ImageInfo> getAllImages(String folderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ImageInfo> imageInfoArrayList = new ArrayList<>();
        ImageInfo imageInfo;
        Cursor cursor = db.rawQuery("select * from photostable where folder_id = ?", new String[]{folderId}); // where       images of which folder
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            imageInfo = new ImageInfo();
            imageInfo.id = cursor.getString(cursor.getColumnIndex("id"));
            imageInfo.path = cursor.getString(cursor.getColumnIndex("path"));
            imageInfo.tagPerson = cursor.getString(cursor.getColumnIndex("person_tag"));
            imageInfo.tagPlace = cursor.getString(cursor.getColumnIndex("location_tag"));
            imageInfo.caption = cursor.getString(cursor.getColumnIndex("caption"));
            imageInfo.captureTime = cursor.getString(cursor.getColumnIndex("captureTime"));
            imageInfo.folderId = cursor.getString(cursor.getColumnIndex("folder_id"));
            Log.i("TAG", imageInfo.toString());
            imageInfoArrayList.add(imageInfo);

            cursor.moveToNext();
        }
        cursor.close();
        return imageInfoArrayList;
    }

    public void AddPersonTag(int imageID, String personTag) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        //values.put("id",imageID);
        values.put("person_tag", personTag);
        Log.i("TAG", "AddPersonTag: " + personTag);

        db.update("photostable", values, "id" + " = ? ", new String[]{Integer.toString(imageID)});
    }

    public int AddCaption(String imageID, String caption) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        values.put("caption", caption);

        return db.update("photostable", values, "id" + " = ? ", new String[]{imageID});
    }

    public int AddLocationTag(String imageID, String locationTag) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        values.put("location_tag", locationTag);

        return db.update("photostable", values, "id" + " = ? ", new String[]{imageID});
    }

    public int RenameFolder(String folderId, String folderName) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        //values.put("f_id",folderId);
        values.put("f_name", folderName);

        return db.update("foldertable", values, "f_id" + " = ? ", new String[]{folderId});
    }

    public int DeleteFolder(String folderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("foldertable", "f_id" + " = ? ", new String[]{folderId});
    }

    public int DeleteImage(String imageId) {
        int a;
        SQLiteDatabase db = this.getWritableDatabase();
        a = db.delete("photostable", "id" + " = ? ", new String[]{imageId});
        return a;
    }

    public int DeletePersonTag(int imageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        values.put("person_tag", "");
        return db.update("photostable", values, "id" + " = ? ", new String[]{Integer.toString(imageId)});
    }

    public int DeletePlaceTag(int imageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        values.put("location_tag", "");
        return db.update("photostable", values, "id" + " = ? ", new String[]{Integer.toString(imageId)});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS privatephotos");
        onCreate(db);
    }


}
