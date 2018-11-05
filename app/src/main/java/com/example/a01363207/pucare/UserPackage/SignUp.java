package com.example.a01363207.pucare.UserPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a01363207.pucare.MainActivity;
import com.example.a01363207.pucare.PlantsView;
import com.example.a01363207.pucare.R;
import com.example.a01363207.pucare.UserPackage.UserDP;
import com.example.a01363207.pucare.UserPackage.UserDatabaseController;

public class SignUp extends AppCompatActivity {
    UserDatabaseController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        controller = new UserDatabaseController(this.getBaseContext());
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
        EditText email  = (EditText) findViewById(R.id.idEmail);
        EditText pass   = (EditText) findViewById(R.id.idPass);

        input.setUserName(user.getText().toString());
        input.setEmail(email.getText().toString());
        input.setPassword(pass.getText().toString());

        long inserted = controller.insert(input);
        Log.d("DATABASE", "User_Insertion " + inserted);
        Toast.makeText(this,"You were successfully registered!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SignUp.this, PlantsView.class);
        startActivity(intent);
    }
}
