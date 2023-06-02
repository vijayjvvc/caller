package com.JvWorld.jcaller.databaseHandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "JV_WORLD.DB";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "FAVOURITES";
    public static final String DATABASE_TABLE_BLOCK = "Block_numbers_by_user";
    public static final String USER_NUMBER_BLOCK = "block_number";

    public static final String DATABASE_TABLE_SPEED_DIAL = "SPEED_DIAL";

    public static final String USER_ID = "_ID";
    public static final String USER_NUMBER = "fav_phone_number";

    public static final String USER_ID_SPEED_DIAL = "_ID";
    public static final String USER_NUMBER_SPEED_DIAL = "Speed_phone_number";


    public static final String CREATE_DB = "CREATE TABLE " + DATABASE_TABLE + " (" + USER_ID
            + " INTEGER NOT NULL, " + USER_NUMBER + " NOT NULL , PRIMARY KEY(" + USER_NUMBER + "));";

    public static final String CREATE_DB_BLOCK_NUMBER = "CREATE TABLE " + DATABASE_TABLE_BLOCK + "(" + USER_ID
            + " INTEGER NOT NULL ," + USER_NUMBER_BLOCK + " NOT NULL , PRIMARY KEY(" + USER_NUMBER_BLOCK + "));";

    public static final String CREATE_DB_SPEED_DIAL = "CREATE TABLE " + DATABASE_TABLE_SPEED_DIAL + " ("
            + USER_ID_SPEED_DIAL + " INTEGER NOT NULL, "
            + USER_NUMBER_SPEED_DIAL + " NOT NULL , PRIMARY KEY(" + USER_NUMBER_SPEED_DIAL + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
        db.execSQL(CREATE_DB_SPEED_DIAL);
        db.execSQL(CREATE_DB_BLOCK_NUMBER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SPEED_DIAL);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_BLOCK);
    }
}
