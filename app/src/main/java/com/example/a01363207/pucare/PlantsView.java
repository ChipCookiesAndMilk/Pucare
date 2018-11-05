package com.example.a01363207.pucare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.a01363207.pucare.PlantPackage.PlantDatabaseController;

/*
PlantsView Class  -> Handles every screen related with users plant's
	~ plants_catalog.xml
		Loads a catalog that shows the plants the user owns
	~ plants_edit.xml
		Show editable fields to load into the plants user DB
	~ plants_add.xml
		Show editable fields to create a new plants user in DB
	~ plants_stats.xml
		Show the stats of a user plant
This class needs to get the data from an API, and save it into Plant table and then
display the user content
*/
public class PlantsView extends AppCompatActivity {

    PlantDatabaseController plantController;

    private String iUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Get userName */
        iUser = (getIntent().getStringExtra(MainActivity.EXTRA_INPUT_USER)).toString();
        Log.d("RECEPTION", "\n\n<----> Received:" + iUser);

        /* Load data from API */
        //loadApiData();

        /* Load user's plants data from DB */
        //getUserPerfil(iUser);

        /* set layout */
        setContentView(R.layout.plants_view);

        plantController = new PlantDatabaseController(this.getBaseContext());
        //controllerShared = new SharedDatabaseController(this.getBaseContext());
        //operations = new SharedOperationsHandler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //controllerShared.close();
        //controllerPlant.close();
    }

/* API */
    private void loadApiData(){

    }


/* plants_view: onClick */
    public void addPlantView(View view) {
    }
}
