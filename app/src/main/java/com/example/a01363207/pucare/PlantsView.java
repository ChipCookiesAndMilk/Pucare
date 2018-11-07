package com.example.a01363207.pucare;

import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.a01363207.pucare.PlantPackage.PlantDP;
import com.example.a01363207.pucare.PlantPackage.PlantDatabaseController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

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
public class PlantsView extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "PlantsView";
    private static final String URL = "http://192.168.100.7:3000/plants";
    private static final String VOLLEY_ERROR = "VOLLEY_ERROR";
    private ArrayList<String> plantsInDB;
    private String email = "";
    private String spValue = "";

    PlantDatabaseController plantController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Get userName */
        email = (getIntent().getStringExtra(MainActivity.EXTRA_INPUT_USER)).toString();
        Log.d("RECEPTION", "\n\n<----> Received:" + email);

        plantController = new PlantDatabaseController(this.getBaseContext());

        /* Load data from API */
        loadApiData();

        /* Load user's plants data from DB */
        //getUserPerfil(iUser);

        /* set layout */
        setContentView(R.layout.plants_view);

        //controllerShared = new SharedDatabaseController(this.getBaseContext());
        //operations = new SharedOperationsHandler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        plantController.close();
        //controllerShared.close();
    }

/* API */
    // gets the plants name from DB
    private ArrayList<String> getPlantsOptions() {
        ArrayList<String> list = new ArrayList<String>();
        Log.d(TAG, "getPlantsOptions. after array creation");

        Cursor cursor = plantController.selectPlantsName();
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(PlantDP.COLUMN_PLANT_NAME));
                    list.add(name);
                    Log.d(TAG, "getPlantsOptions; name: " + name);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception. getPlantsOptions; exception: " + e.getMessage());
        }

        cursor.close();
        return list;
    }

    private void jsonIntoDatabase(JSONObject plantObj) {
        try {
            Log.d("jsonIntoDatabase", "IN jsonIntoDatabase, inside try");
            String plantName = plantObj.getString("name");
            String plantType    = plantObj.getString("type");
            String sunlight     = plantObj.getString("sunlight");
            String height       = plantObj.getString("height");
            String water        = plantObj.getString("water");
            String image        = plantObj.getString("image");

            PlantDP plantDP = new PlantDP();

            plantDP.setPlantName(plantName);
            plantDP.setPlantType(plantType);
            plantDP.setSunlight(sunlight);
            plantDP.setHeight(height);
            plantDP.setWater(water);
            plantDP.setImage(image);

            Log.d("jsonIntoDatabase", "before plantController call");

            // validate if this plant exists
            long insert = plantController.insert(plantDP);
            Log.d("INSERT", "Data from API  was inserted into database: " + insert);

        } catch (Exception e) {
            Log.d("Exception", "jsonIntoDatabase. EXCEPTION: " + e.getMessage());
        }
    }

    private void loadApiData(){
        Log.d("loadApiData", "\tloadApiData");

        RequestQueue queue = Volley.newRequestQueue(this);
        // requestTypeMethod, URL, responseListener(file), errorListener
        Log.d("arrayRequest", "\tloadApiData: Request");
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                String jStr= "{\"id\":\"null\"}";

                JSONObject plantObj = null;
                try {
                    plantObj = new JSONObject(jStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i < response.length(); i++){
                    try {
                            plantObj = response.getJSONObject(i);
                            jsonIntoDatabase(plantObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("RESPONSE","loadApiData: Response: "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(VOLLEY_ERROR,"onErrorResponse: An error ocurred: "+error.getMessage());
            }
        });
        // add into queue
        Log.d("queue", "\tloadApiData: queue");
        queue.add(arrayRequest);
        Log.d("end_loadApiData", "\tend_loadApiData");
    }

/* plants_new */



/* plants_view: */
    /* CATALOGUE */



    /* ADD PLANT BUTTON */
    // this method loads the spinner content, and the plants_new screen
    public void addPlantUser(View view) {
        setContentView(R.layout.plants_new);
        ArrayList<String> list;
        list = getPlantsOptions();
        Log.d(TAG, "\ngetPlantsOptions. list: "+list.toString());
        Spinner spinner = (Spinner) findViewById(R.id.idSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_inner_layout, R.id.sData, list);
        spinner.setAdapter(adapter);
        Log.d(TAG, "getPlantsOptions. Plants name were set");
        spinner.setOnItemSelectedListener(this);
    }

    // gets the plants name from DB
    private String getPlantsImage(String plantName) {
        String link = "";

        Log.d(TAG, "getPlantsImage method ");

        Cursor cursor = plantController.selectPlantsImage(plantName);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    link = cursor.getString(cursor.getColumnIndex(PlantDP.COLUMN_IMAGE));
                    Log.d(TAG, "getPlantsImage; link: " + link);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception. getPlantsImage; exception: " + e.getMessage());
        }

        cursor.close();
        return link;
    }


    // Required by the implement, gets the value selected by user
    // parent   : AdapterView where the selection happened
    // view     : View within the AdapterView that was clicked
    // pos      : The position of the view in the adapter
    // id       : The row id of the item that is selected
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        spValue = (String) parent.getItemAtPosition(pos).toString();
        Log.d(TAG,"onItemSelected. value: "+spValue);
        // Change image icon
        // get image url

        String link = getPlantsImage(spValue);

        if(link.isEmpty()){
            Log.d(TAG,"onItemSelected. an image for "+spValue+" was not found");
        }
        else {
            ImageView icon = (ImageView)findViewById(R.id.imageView);
            //icon.setImageURI("google.com");
            Log.d(TAG,"onItemSelected. This is the link for the image to go to: "+link);
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback, or do nothing.
        // maybe that what the user wants
    }
}
