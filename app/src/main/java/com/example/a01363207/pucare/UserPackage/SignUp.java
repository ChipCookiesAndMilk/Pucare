package com.example.a01363207.pucare.UserPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.a01363207.pucare.PlantsView;
import com.example.a01363207.pucare.R;

import java.util.List;

public class SignUp extends AppCompatActivity {
    UserDatabaseController controller;
    // pattern
    String patternStr = "";
    PatternLockView mPatternLockView;

    public static String EXTRA_INPUT_USER = "INPUT_USER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        controller = new UserDatabaseController(this.getBaseContext());
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

    // Here users are added to database
    public void create(View view) {
        UserDP input = new UserDP();

        EditText user   = (EditText) findViewById(R.id.idUser);
        //EditText pass   = (EditText) findViewById(R.id.idPass);


        input.setUserName(user.getText().toString());
        input.setPassword(patternStr);

        // Need validations but for now I assume I'll treat kindly this program
        long inserted = controller.insert(input);
        Log.d("SignUp", "User_Insertion " + inserted);
        Toast.makeText(this,"You were successfully registered!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SignUp.this, PlantsView.class);
        intent.putExtra(EXTRA_INPUT_USER, user.getText().toString());
        startActivity(intent);
    }
}
