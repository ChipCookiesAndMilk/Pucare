package com.example.a01363207.pucare;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.a01363207.pucare.UserPackage.SignUp;
import com.example.a01363207.pucare.UserPackage.UserDP;
import com.example.a01363207.pucare.UserPackage.UserDatabaseController;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // for the signUp & PlantsView
    public static String EXTRA_INPUT_USER = "INPUT_USER";
    // for the logIn
    UserDatabaseController controller;
    // the the patter_lock_view
    String patternStr = "";
    PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new UserDatabaseController(this.getBaseContext());

        // pattern
        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() { }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) { }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                patternStr = PatternLockUtils.patternToString(mPatternLockView, pattern);
            }

            @Override
            public void onCleared() { }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.close();
    }

    /* activity_main: signUp = onClick */
    public void signUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }

    // validation of existence of user
    public int valEmail(String u, String p) {
        int result = -2;
        //Log.d("VALID_USER_METHOD", "Llegue___ USER: " + u + " PASS: " + p);

        // Ask DB look for user
        String[] selectionArgs = new String[]{u, p};
        // for requirement: Initialize DatabaseHelper, in this case calls the DBcontroller to do it
        String selection = UserDP.COLUMN_USERNAME + " =? AND " + UserDP.COLUMN_PASSWORD + " = ?";

        Cursor cursor = controller.selectUser(selection, selectionArgs);

        // get info from cursor

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(UserDP.COLUMN_USERNAME));
                String pass = cursor.getString(cursor.getColumnIndex(UserDP.COLUMN_PASSWORD));
                //Log.d("DATABASE_INFO", "USER_INFO____userName " + name + " Pass: " + pass);
            }
            result = 1;
        }
        cursor.close();
        return result;
    }

    /* activity_main: logIn = onClick */
    public void logIn(View view) {
        String message = "";
        EditText user = (EditText) findViewById(R.id.idUser);
        //EditText pass = (EditText) findViewById(R.id.idPass);

        String u = user.getText().toString();
        //String p = pass.getText().toString();
        //String p = patternStr;

        //Log.d("MainActivity", "User: " + u + " Pass: " + p);

        // user exists?
        int result = valEmail(u, patternStr);

        // if user exist && data is correct?
        if (result == 1) {
            // move to next activity
            Intent intent = new Intent(MainActivity.this, PlantsView.class);
            intent.putExtra(EXTRA_INPUT_USER, u);

            startActivity(intent);
            // Log.d("MainActivity", "\nFOUND: USUARIO Y CONTRASENA CORRECTOS");
            message = "WELCOME BACK";

        } else {
            // throw error, data do not match or user does not exists
            // Log.d("RECEPTION", "\nEXCEPTION: DATA DO NOT MATCH");
            message = "TRY AGAIN";
        }
        //textView.setText(message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
