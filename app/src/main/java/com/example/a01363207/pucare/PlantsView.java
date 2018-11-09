package com.example.a01363207.pucare;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.a01363207.pucare.PlantPackage.PlantDP;
import com.example.a01363207.pucare.PlantPackage.PlantDatabaseController;
import com.example.a01363207.pucare.UserPlantPackage.RecyclerViewAdapter;
import com.example.a01363207.pucare.UserPlantPackage.UserPlantDP;
import com.example.a01363207.pucare.UserPlantPackage.UserPlantDatabaseController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*
PlantsView Class  -> Handles the next screens related with users plant's
	~ plants_catalog.xml
		Loads a catalog that shows the plants the user owns

	~ plants_add.xml
		Show editable fields to create a new plants user in DB

This class needs to get the data from an API, and save it into Plant table and then
displays the user content
*/

public class PlantsView extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String URL_PLANTS = "http://192.168.100.7:3000/plants";
    private static final String VOLLEY_ERROR = "VOLLEY_ERROR";
    private static final String TAG = "PlantsView";

    private ArrayList<String> plantsInDB;
    private String email = "";
    private String spValue = "";

    PlantDatabaseController plantController;
    UserPlantDatabaseController userPlantController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Get userName */
        email = (getIntent().getStringExtra(MainActivity.EXTRA_INPUT_USER)).toString();
        Log.d(TAG, "\n\n<----> Received:" + email);

        plantController = new PlantDatabaseController(this.getBaseContext());
        userPlantController = new UserPlantDatabaseController(this.getBaseContext());

        /* set layout */
        setContentView(R.layout.plants_view);

        /* Load data from API */
        loadApiData();

        /* Load user's plants data from DB */
        getUserPlants();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        plantController.close();
        userPlantController.close();
    }

/** plants_new **/
    public void addUserPlant(View view) {

        // get user info
        EditText n = (EditText)findViewById(R.id.idNewName);
        Spinner s = (Spinner)findViewById(R.id.idSpinner);

        String plantName = n.getText().toString();
        String plantType = s.getSelectedItem().toString();

        Log.d(TAG,"addUserPlant. plantName: "+plantName+"    ___plantType: "+plantType);

        // add plant to the user
        UserPlantDP input = new UserPlantDP();
// <<<<<<< DELETE FROM HERE >>>>>
        input.setNickname("Watson");
        input.setHealth("Healthy");
        input.setLastWater("2018-11-07 20:28:22");
        input.setNextWater("2018-11-14 20:28:22");
        input.setImage("https://image.ibb.co/gsGOsf/claret.jpg");
        input.setUserEmail(email);
        input.setPlantName(plantType);
        input.setDateRegistered("2018-11-07 20:28:22");

        long inserted = userPlantController.insert(input);
        Log.d(TAG, "addUserPlant. inserted : " + inserted);
/*

        input.setNickname("Edu");
        input.setHealth("Healthy");
        input.setLastWater("2018-11-07 21:12:10");
        input.setNextWater("2018-11-21 21:12:10");
        input.setImage("https://image.ibb.co/gsGOsf/cherry.jpg");
        input.setUserEmail(email);
        input.setPlantName("Cherry");
        input.setDateRegistered("2018-11-07 21:12:10");

        inserted = userPlantController.insert(input);
        Log.d(TAG, "addUserPlant. inserted_2: " + inserted);
*/
// <<<<<<< DELETE TO HERE >>>>>

        /*
        OperationsHandler oh = new OperationsHandler();

        input.setNickname(plantName);
        input.setHealth("Healthy");
        String lW = oh.getDate();
        input.setLastWater(lW);

        // because this is a new plant the countdown starts from now, from last water
        String wW = oh.getWater(plantType);
        String nW = oh.nextWater(lW, wW);

        input.setNextWater(nW);
        String img = oh.getImage(plantType);
        input.setImage(img);

        input.setUserEmail(email);
        input.setPlantName(plantType);
        input.setDateRegistered(lW);

        long inserted = userPlantController.insert(input);
        Log.d(TAG, "addUserPlant. inserted " + inserted);

        Toast.makeText(this,"Now, let's take a good care of "+plantName,Toast.LENGTH_LONG).show();

        // load catalogue
        */
        Log.d(TAG, "<---- addUserPlant. inserted ");
        setContentView(R.layout.plants_view);
    }

/** plants_view: **/
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
        // requestTypeMethod, URL_PLANTS, responseListener(file), errorListener
        Log.d("arrayRequest", "\tloadApiData: Request");
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                URL_PLANTS, null, new Response.Listener<JSONArray>() {
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

    /* CATALOGUE */
    private void getUserPlants(){

        List<UserPlantDP> list = new ArrayList<UserPlantDP>();
        String selection = UserPlantDP.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        //int curSize = 0;

        Cursor cursor = userPlantController.selectContent(selection,selectionArgs);

        while (cursor.moveToNext()) {
            String name     = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_NICKNAME));
            String health   = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_HEALTH));
            String last     = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_LAST_WATER));
            String water    = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_NEXT_WATER));
            String image    = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_IMAGE));
            String user     = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_USER_EMAIL));
            String plant    = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_PLANT_NAME));
            String date     = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_DATE_REGISTERED));

            UserPlantDP input = new UserPlantDP();
            input.setNickname(name);
            input.setHealth(health);
            input.setLastWater(last);
            input.setNextWater(water);
            input.setImage(image);
            input.setUserEmail(user);
            input.setPlantName(plant);
            input.setDateRegistered(date);

            list.add(input);
            //curSize++;

            /*Log.d(TAG, "\n## >>>>>> addUserPlant. List size: "+list.size()+" __Card info. name: " + name + " health: " + health+ " last: "+last+" water: "
                    +water+" image: "+image+ " email: "+user+" plant: "+plant+" date: "+date);
            */
        }
        cursor.close();
        //Log.d(TAG,"getUserPlants. User email_Origin: "+email);
        //Log.d(TAG,"getUserPlants. User email : "+input.getUserEmail());

        //Log.d(TAG,"*------- Contents of plantsList -------*");
        //for(int i = 0; i < list.size(); i++){
        //    Log.d(TAG,"<Nick>: "+list.get(i).getNickname());
        //    Log.d(TAG,"<Type>: "+list.get(i).getPlantName());
        //    Log.d(TAG,"<Water>: "+list.get(i).getNextWater());
        //}

        //Log.d(TAG, "\n## >>>>>> addUserPlant. List size: "+list.size() +" curSize: "+curSize);

        RecyclerView view = (RecyclerView) findViewById(R.id.idRecyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, list);

        view.setLayoutManager(new GridLayoutManager(this, 2));
        view.setAdapter(adapter);
    }

    /* ADD PLANT BUTTON */
    // this method loads the spinner content, and the plants_new screen
    public void addPlantUser(View view) {
        // set layout
        setContentView(R.layout.plants_new);

        // load plants options
        ArrayList<String> list;
        list = getPlantsOptions();
        //Log.d(TAG, "\ngetPlantsOptions. list: "+list.toString());

        // display spinner options
        Spinner spinner = (Spinner) findViewById(R.id.idSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_inner_layout, R.id.sData, list);
        spinner.setAdapter(adapter);
        // Log.d(TAG, "getPlantsOptions. Plants name were set");
        spinner.setOnItemSelectedListener(this);
    }

    // gets the plants name from DB
    private String getPlantsImage(String plantName) {
        String link = "";

        //Log.d(TAG, "getPlantsImage method ");

        Cursor cursor = plantController.selectPlantsImage(plantName);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    link = cursor.getString(cursor.getColumnIndex(PlantDP.COLUMN_IMAGE));
                    //Log.d(TAG, "getPlantsImage; link: " + link);
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
        ImageView icon = (ImageView)findViewById(R.id.imageView);

        if(link.isEmpty()){
            Log.d(TAG,"onItemSelected. an image for "+spValue+" was not found");
        }   // execute param is the url of the image
        else new GetImageFromURL(icon).execute(link);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback, or do nothing.
        // maybe that what the user wants
    }


    // Subclass, reloads the image when the user changes the spinner option
    private class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView icon;

        public GetImageFromURL(ImageView image) {
            this.icon = image;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream is = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            icon.setImageBitmap(result);
        }
    }
}
