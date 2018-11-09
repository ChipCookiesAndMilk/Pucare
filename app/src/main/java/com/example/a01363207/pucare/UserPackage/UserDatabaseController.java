package com.example.a01363207.pucare.UserPackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.a01363207.pucare.DatabaseHelper;

public class UserDatabaseController {
    DatabaseHelper helper;
    SQLiteDatabase database;

    // Constructor
    public UserDatabaseController(Context context) {
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    // Create an user
    public long insert(UserDP user) {
        ContentValues values = new ContentValues();
        values.put(UserDP.COLUMN_USERNAME, user.getUserName());
        values.put(UserDP.COLUMN_EMAIL, user.getEmail());
        values.put(UserDP.COLUMN_PASSWORD, user.getPassword());

        long inserted = database.insert(UserDP.TABLE_NAME, null, values);

        return inserted;
    }

    // Get info from user
    public Cursor selectUser(String selection, String[] selectionArgs) {
        // for requirement: finally: Initialize DatabaseHelper
        SQLiteDatabase database = helper.getReadableDatabase();
        /*
            CURSOR STRUCTURE
            Cursor cursor = sqLiteDatabase.query(
                tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);

            return query(false, table, columns, selection, selectionArgs, groupBy,
                    having, orderBy, null limit );
        */
        String columns[] = {
                UserDP.COLUMN_EMAIL,
                UserDP.COLUMN_USERNAME,
                UserDP.COLUMN_PASSWORD
        };
        String order = UserDP.COLUMN_USERNAME + " DESC";

        Cursor cursor = database.query(
                UserDP.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                order
        );
        return cursor;
    }

    // delete an user
    public long delete(UserDP user) {
        String where = UserDP.COLUMN_EMAIL + " = ?";
        String[] whereArgs = { user.getEmail() };

        long deleted = database.delete(UserDP.TABLE_NAME, where, whereArgs);
        return deleted;
    }

    // close database
    public void close() {
        database.close();
    }
}
