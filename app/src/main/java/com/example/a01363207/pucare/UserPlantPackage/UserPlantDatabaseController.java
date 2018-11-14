package com.example.a01363207.pucare.UserPlantPackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.a01363207.pucare.DatabaseHelper;

public class UserPlantDatabaseController {
    // global variables
    private static String TAG = "UserPlantDatabaseController";
    DatabaseHelper helper;
    SQLiteDatabase database;

    // Constructor
    public UserPlantDatabaseController(Context context) {
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }
    // add a plant to an user
    public long insert(UserPlantDP input) {
        ContentValues values = new ContentValues();

        values.put(UserPlantDP.COLUMN_NICKNAME, input.getNickname());
        values.put(UserPlantDP.COLUMN_HEALTH, input.getHealth());
        values.put(UserPlantDP.COLUMN_LAST_WATER, input.getLastWater());
        values.put(UserPlantDP.COLUMN_NEXT_WATER, input.getNextWater());
        values.put(UserPlantDP.COLUMN_IMAGE, input.getImage());

        values.put(UserPlantDP.COLUMN_USER_EMAIL, input.getUserEmail());
        values.put(UserPlantDP.COLUMN_PLANT_NAME, input.getPlantName());
        values.put(UserPlantDP.COLUMN_DATE_REGISTERED, input.getDateRegistered());

        Log.d(TAG, "insert. <<<<---- DATA plantName: " + input.getNickname()+" Health: "+input.getHealth()+" Last: "+input.getLastWater()+" next: "+input.getNextWater()+" image: "+input.getImage()+" email: "+input.getUserEmail()+" plantType: "+input.getPlantName()+" date: "+input.getDateRegistered());

        long inserted = database.insert(UserPlantDP.TABLE_NAME, null, values);
        Log.d("UserPlantDBController", "inserted: " + inserted);
        return inserted;
    }

    // change some fields from plant
    public long update(UserPlantDP input) {

        ContentValues values = new ContentValues();
        values.put(UserPlantDP.COLUMN_NICKNAME, input.getNickname());
        values.put(UserPlantDP.COLUMN_HEALTH, input.getHealth());
        values.put(UserPlantDP.COLUMN_LAST_WATER, input.getLastWater());
        values.put(UserPlantDP.COLUMN_NEXT_WATER, input.getNextWater());
        values.put(UserPlantDP.COLUMN_IMAGE, input.getImage());

        values.put(UserPlantDP.COLUMN_USER_EMAIL, input.getUserEmail());
        values.put(UserPlantDP.COLUMN_PLANT_NAME, input.getPlantName());
        values.put(UserPlantDP.COLUMN_DATE_REGISTERED, input.getDateRegistered());

        // update(String table, ContentValues values, String whereClause, String[] whereArgs)
        String selection = UserPlantDP.COLUMN_USER_EMAIL + " = ? AND " + UserPlantDP.COLUMN_DATE_REGISTERED + " = ?";
        String[] selectionArgs = new String[]{input.getUserEmail(), input.getDateRegistered()};

        long inserted = database.update(UserPlantDP.TABLE_NAME, values, selection, selectionArgs);

        Log.d(TAG, "update. Data was updated. " + inserted);

        return inserted;
    }

    // delete a plant from user
    public long delete(String user, String date) {
        // update(String table, String whereClause, String[] whereArgs)
        String selection = UserPlantDP.COLUMN_USER_EMAIL + " = ? AND " + UserPlantDP.COLUMN_DATE_REGISTERED + " = ?";
        String[] selectionArgs = new String[]{user, date};
        long result = database.delete(UserPlantDP.TABLE_NAME, selection, selectionArgs);
        Log.d(TAG, "update. Data was removed from UserPlants. Result: " + result);
        return result;
    }

    // get information about a plant
    public Cursor selectContent(String selection, String[] selectionArgs) {

        SQLiteDatabase databaseRead = helper.getReadableDatabase();

        String columns[] = {
                UserPlantDP.COLUMN_NICKNAME,
                UserPlantDP.COLUMN_HEALTH,
                UserPlantDP.COLUMN_LAST_WATER,
                UserPlantDP.COLUMN_NEXT_WATER,
                UserPlantDP.COLUMN_IMAGE,
                UserPlantDP.COLUMN_USER_EMAIL,
                UserPlantDP.COLUMN_PLANT_NAME,
                UserPlantDP.COLUMN_DATE_REGISTERED
        };

        Cursor cursor = databaseRead.query(
                UserPlantDP.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        Log.d("UserPlantDBController","end_selectContent");
        return cursor;
    }

    // close database
    public void close() {
        database.close();
    }

}
