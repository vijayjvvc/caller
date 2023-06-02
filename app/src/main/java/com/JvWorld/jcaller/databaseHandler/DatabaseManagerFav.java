package com.JvWorld.jcaller.databaseHandler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import com.JvWorld.jcaller.someVariable.StcVariable;

import java.sql.SQLDataException;

public class DatabaseManagerFav {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private Context context;

    public DatabaseManagerFav(Context context) {
        this.context = context;

    }


    public DatabaseManagerFav open() throws SQLDataException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }


    public void close() {
        databaseHelper.close();
    }

    ///////////////////// favourities .............

    public void insert(String number_fav, int Id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_NUMBER, number_fav);
        contentValues.put(DatabaseHelper.USER_ID, Id);
        database.insert(DatabaseHelper.DATABASE_TABLE, null, contentValues);
    }

    public Cursor fetch() {
        String[] favourite = new String[]{DatabaseHelper.USER_ID, DatabaseHelper.USER_NUMBER};
        Cursor cursor = database.query(DatabaseHelper.DATABASE_TABLE, favourite, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    @SuppressLint("Range")
    public boolean fetchOne(String numb) {
        boolean num = false;
        int ab = 0;
        String[] favourite = new String[]{DatabaseHelper.USER_NUMBER, DatabaseHelper.USER_ID};
        Cursor cursor = database.query(DatabaseHelper.DATABASE_TABLE, favourite, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToLast();
            if (cursor.getPosition() != -1) {
                cursor.moveToFirst();
                do {
                    String a = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_NUMBER));
                    if (a.equals(numb)) {
                        StcVariable.FAVOURITE_ID_CONTACTS = Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                        ab++;
                    }
                } while (cursor.moveToNext());
            }
        }
        if (ab == 1) {
            num = true;
        }
        return num;
    }

    public int update(long id, String number) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_NUMBER, number);
        int a = database.update(DatabaseHelper.DATABASE_TABLE, contentValues, DatabaseHelper.USER_ID + "=" + id, null);
        return a;
    }

    public void delete(long number) {
        database.delete(DatabaseHelper.DATABASE_TABLE, DatabaseHelper.USER_ID + "=" + number, null);
    }

    ///////////////////// favourities .............

    ///////////////////// Speed Dial .............
    public void insertSpeedDial(String number_fav, int Id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_NUMBER_SPEED_DIAL, number_fav);
        contentValues.put(DatabaseHelper.USER_ID_SPEED_DIAL, Id);
        database.insert(DatabaseHelper.DATABASE_TABLE_SPEED_DIAL, null, contentValues);
    }

    @SuppressLint("Range")
    public String fetchSpeedDial(int ID) {
        String a = "";
        String[] speedDial = new String[]{DatabaseHelper.USER_NUMBER_SPEED_DIAL, DatabaseHelper.USER_ID_SPEED_DIAL};
        Cursor cursor = database.query(DatabaseHelper.DATABASE_TABLE_SPEED_DIAL, speedDial, null, null
                , null, null, null);
        if (cursor != null) {
            cursor.moveToLast();
            if (cursor.getPosition() != -1) {
                cursor.moveToFirst();
                do {
                    String aa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID_SPEED_DIAL));
                    if (Integer.parseInt(aa) == ID) {
                        a = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_NUMBER_SPEED_DIAL));
                    }
                } while (cursor.moveToNext());
            }
        }
        return a;
    }

    public void deleteSpeedDial(long number) {

        database.delete(DatabaseHelper.DATABASE_TABLE_SPEED_DIAL, DatabaseHelper.USER_ID_SPEED_DIAL + "=" + number, null);
    }
    ///////////////////// Speed Dial .............

    ///////////////////// Call Blocker .............

    public void insertBlockNumbers(String number, int _ID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_NUMBER_BLOCK, number);
        contentValues.put(DatabaseHelper.USER_ID, _ID);
        database.insert(DatabaseHelper.DATABASE_TABLE_BLOCK, null, contentValues);
    }

    public Cursor fetchBlockNumbers() {
        String[] Block = new String[]{DatabaseHelper.USER_ID, DatabaseHelper.USER_NUMBER_BLOCK};
        Cursor cursor = database.query(DatabaseHelper.DATABASE_TABLE_BLOCK, Block, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    @SuppressLint("Range")
    public boolean fetchOneBlock(String numb) {
        boolean num = false;
        int ab = 0;
        String[] favourite = new String[]{DatabaseHelper.USER_NUMBER_BLOCK, DatabaseHelper.USER_ID};
        Cursor cursor = database.query(DatabaseHelper.DATABASE_TABLE_BLOCK, favourite, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToLast();
            if (cursor.getPosition() != -1) {
                cursor.moveToFirst();
                do {
                    String a = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_NUMBER_BLOCK));
                    if (a.equals(numb)) {
                        StcVariable.BLOCK_ID_CONTACTS = Long.parseLong(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                        ab++;
                    }
                } while (cursor.moveToNext());
            }
        }
        if (ab == 1) {
            num = true;
        }
        return num;
    }

    public void deleteBlock(long number) {
        database.delete(DatabaseHelper.DATABASE_TABLE_BLOCK, DatabaseHelper.USER_ID + "=" + number, null);
    }

    ///////////////////// Call Blocker .............


}


















