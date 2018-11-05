package com.example.a01363207.pucare.PlantPackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.a01363207.pucare.DatabaseHelper;

public class PlantDatabaseController {

    SQLiteDatabase database;
    DatabaseHelper helper;

    public PlantDatabaseController(Context context) {
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    // for testing purposes only
    public long insert(PlantDP plant) {
        ContentValues values = new ContentValues();
        values.put(PlantDP.COLUMN_IDPLANT, plant.getIdPlant());
        values.put(PlantDP.COLUMN_PLANTNAME, plant.getPlantName());
        values.put(PlantDP.COLUMN_TYPE, plant.getPlantType());
        values.put(PlantDP.COLUMN_SUNLIGHT, plant.getSunlight());
        values.put(PlantDP.COLUMN_HEIGHT, plant.getHeight());
        values.put(PlantDP.COLUMN_WATER, plant.getWater());
        values.put(PlantDP.COLUMN_IMAGE, plant.getImage());

        long inserted = database.insert(PlantDP.TABLE_NAME, null, values);
        return inserted;
    }

// Gets all the kind of plants
    public Cursor selectPlantsKind(String selection, String[] selectionArgs) {
        SQLiteDatabase databaseRead = helper.getReadableDatabase();

        String columns[] = {
                PlantDP.COLUMN_PLANTNAME
        };

        Cursor cursor = database.query(PlantDP.TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);

        return cursor;
    }
// Gets all the plants, and all their info
    public Cursor selectAllPlants(String selection, String[] selectionArgs){

        // for requirement: finally: Initialize DatabaseHelper
        SQLiteDatabase databaseRead = helper.getReadableDatabase();

/*
        CURSOR STRUCTURE
        Cursor cursor = sqLiteDatabase.query(
            tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);

        return query(false, table, columns, selection, selectionArgs, groupBy,
                having, orderBy, null limit );
*/
        String columns[] = {
                PlantDP.COLUMN_IDPLANT,
                PlantDP.COLUMN_PLANTNAME,
                PlantDP.COLUMN_TYPE,
                PlantDP.COLUMN_SUNLIGHT,
                PlantDP.COLUMN_HEIGHT,
                PlantDP.COLUMN_WATER,
                PlantDP.COLUMN_IMAGE,
        };

        Cursor cursor = databaseRead.query(PlantDP.TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);

        return cursor;
    }

    public void close() {
        database.close();
    }

}
