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
    public UserDatabaseController(Context context){
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    public long insert (UserDP user)
    {
        ContentValues values = new ContentValues();
        values.put(UserDP.COLUMN_USERNAME, user.getUserName());
        values.put(UserDP.COLUMN_EMAIL, user.getEmail());
        values.put(UserDP.COLUMN_PASSWORD, user.getPassword());

        long inserted = database.insert(UserDP.TABLE_NAME, null, values);

        return inserted;
    }

    public Cursor selectUser(String selection, String[] selectionArgs)
    {
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
                UserDP.COLUMN_IDUSER,
                UserDP.COLUMN_USERNAME,
                UserDP.COLUMN_EMAIL,
                UserDP.COLUMN_PASSWORD
        };

        database.query(UserDP.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        String order = UserDP.COLUMN_USERNAME +" DESC";


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

    public long delete(UserDP user){

        String where = UserDP.COLUMN_IDUSER + " = ?";
        String[] whereArgs= {
                ""+user.getIdUser()
        };
        long deleted = database.delete(UserDP.TABLE_NAME ,  where, whereArgs);
        return deleted;
    }

    /*
    // Update user info
        public long update(UserDP user){

            ContentValues values = new ContentValues();

            values.put(UserDP.COLUMN_USERNAME, user.getUser());
            values.put(UserDP.COLUMN_EMAIL, user.getEmail());
            values.put(UserDP.COLUMN_PASSWORD, user.getPassword());

            String where = UserDP.COLUMN_ID + " = ?";
            String[] whereArgs= {
                    ""+user.getId()
            };

            long updated = database.update(UserDP.TABLE_NAME, values, where, whereArgs);
            return updated;
        }
    */
    public void close() {
        database.close();
    }
}
