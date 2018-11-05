package com.example.a01363207.pucare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

    private static final String URL = "http://192.168.100.7:3000/plants";
    private static final String VOLLEY_ERROR = "VOLLEY_ERROR";
    private String strUser = "";

    PlantDatabaseController plantController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Get userName */
        strUser = (getIntent().getStringExtra(MainActivity.EXTRA_INPUT_USER)).toString();
        Log.d("RECEPTION", "\n\n<----> Received:" + strUser);

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
    private void jsonIntoDatabase(JSONObject plantObj){
        try{
            Log.d("jsonIntoDatabase","IN jsonIntoDatabase, inside try");
            int idPlant = Integer.parseInt(plantObj.getString("id"));
            String plantName = plantObj.getString("name");
            String plantType = plantObj.getString("type");
            String sunlight = plantObj.getString("sunlight");
            String height = plantObj.getString("height");
            String water = plantObj.getString("water");
            String image = plantObj.getString("image");

            PlantDP plantDP = new PlantDP();

            plantDP.setIdPlant(idPlant);
            plantDP.setPlantName(plantName);
            plantDP.setPlantType(plantType);
            plantDP.setSunlight(sunlight);
            plantDP.setHeight(height);
            plantDP.setWater(water);
            plantDP.setImage(image);
            Log.d("jsonIntoDatabase","before plantController call");

            // validate if this plant exists
            long insert = plantController.insert(plantDP);
            Log.d("INSERT","Data from API  was inserted into database: "+ insert);

        }catch(Exception e){
            Log.d("Exception","jsonIntoDatabase. EXCEPTION: "+e.getMessage());
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


/* plants_view: onClick */
    public void addPlantView(View view) {
    }
}
