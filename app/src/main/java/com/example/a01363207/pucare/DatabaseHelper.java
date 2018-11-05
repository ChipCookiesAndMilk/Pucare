package com.example.a01363207.pucare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.a01363207.pucare.PlantPackage.PlantDP;
import com.example.a01363207.pucare.UserPackage.UserDP;
import com.example.a01363207.pucare.UserPlantPackage.UserPlantDP;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PucareDB";
    private static final int DATABASE_VERSION = 1;

    // TABLES NAMES
    private static final String TABLE_USER          = UserDP.TABLE_NAME;
    private static final String TABLE_PLANT         = PlantDP.TABLE_NAME;
    private static final String TABLE_USER_PLANT    = UserPlantDP.TABLE_NAME;

    // DELETE TABLES
    private static final String DESTROY_USER        = "DROP TABLE IF EXISTS " + TABLE_USER ;
    private static final String DESTROY_PLANT       = "DROP TABLE IF EXISTS " + TABLE_PLANT;
    private static final String DESTROY_USER_PLANT  = "DROP TABLE IF EXISTS " + TABLE_USER_PLANT;

    // CREATE TABLES
    // User
    private static final String SQL_CREATE_USER = "CREATE TABLE " + TABLE_USER + "(" +
            UserDP.COLUMN_IDUSER    +  " INTEGER PRIMARY KEY, " +
            UserDP.COLUMN_USERNAME  +  " TEXT," +
            UserDP.COLUMN_EMAIL     +  " TEXT," +
            UserDP.COLUMN_PASSWORD  +  " TEXT)";

    // Plant
    private static final String SQL_CREATE_PLANT = "CREATE TABLE " + TABLE_PLANT  + "(" +
            PlantDP.COLUMN_IDPLANT  +  " INTEGER PRIMARY KEY, " +
            PlantDP.COLUMN_PLANTNAME+  " TEXT," +
            PlantDP.COLUMN_TYPE     +  " TEXT," +
            PlantDP.COLUMN_SUNLIGHT +  " TEXT," +
            PlantDP.COLUMN_HEIGHT   +  " TEXT," +
            PlantDP.COLUMN_WATER    +  " TEXT," +
            PlantDP.COLUMN_IMAGE    +  " TEXT)" ;

    // UserPlant
    private static final String SQL_CREATE_USER_PLANT = "CREATE TABLE " + TABLE_USER_PLANT + "(" +
            UserPlantDP.COLUMN_NICKNAME         +  " TEXT," +
            UserPlantDP.COLUMN_HEALTH           +  " TEXT," +
            UserPlantDP.COLUMN_LAST_WATER       +  " TEXT," +
            UserPlantDP.COLUMN_NEXT_WATER       +  " TEXT," +

            UserPlantDP.COLUMN_ID_USER          + " INTEGER," +
            UserPlantDP.COLUMN_ID_PLANT         + " INTEGER," +
            UserPlantDP.COLUMN_DATE_REGISTERED  +  " TEXT," +

            "FOREIGN KEY ("+UserPlantDP.COLUMN_ID_USER+") REFERENCES "+UserDP.TABLE_NAME+"("+UserDP.COLUMN_IDUSER+")," +
            "FOREIGN KEY ("+UserPlantDP.COLUMN_ID_PLANT+") REFERENCES "+PlantDP.TABLE_NAME+"("+PlantDP.COLUMN_IDPLANT+"),"+
            "PRIMARY KEY ("+UserPlantDP.COLUMN_ID_USER+","+UserPlantDP.COLUMN_ID_PLANT+","+UserPlantDP.COLUMN_DATE_REGISTERED+"))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PLANT);
        db.execSQL(SQL_CREATE_USER);
        db.execSQL(SQL_CREATE_USER_PLANT);
        Log.d("SUCCESS","onCreate: Tables were created with success");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DESTROY_PLANT);
        db.execSQL(DESTROY_USER);
        db.execSQL(DESTROY_USER_PLANT);
        onCreate(db);
    }
}
