package com.example.a01363207.pucare;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.a01363207.pucare.PlantPackage.PlantDP;
import com.example.a01363207.pucare.PlantPackage.PlantDatabaseController;
import com.example.a01363207.pucare.UserPlantPackage.PlantsAdd;
import com.example.a01363207.pucare.UserPlantPackage.RecyclerViewAdapter;
import com.example.a01363207.pucare.UserPlantPackage.UserPlantDP;
import com.example.a01363207.pucare.UserPlantPackage.UserPlantDatabaseController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
PlantsView Class  -> Handles the next screens related with users plant's
	~ plants_catalog.xml
		Loads a catalog that shows the plants the user owns

This class needs to get the data from an API, and save it into Plant table and then
displays the user content
*/

public class PlantsView extends AppCompatActivity {
    private static final String URL_PLANTS = "http://10.25.249.27:3000/plants";

    public static String CHANNEL_ID = "Notification Chanel A";

    private static final String VOLLEY_ERROR = "VOLLEY_ERROR";
    private static final String TAG = "PlantsView";

    public static String EXTRA_INPUT_USER = "INPUT_USER";

    // search bar
    private AutoCompleteTextView searchText;
    // catalogue plants
    private ArrayList<String> plantsInDB;
    // user
    private String email = "";

    PlantDatabaseController plantController;
    UserPlantDatabaseController userPlantController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Get userName */
        email = (getIntent().getStringExtra(MainActivity.EXTRA_INPUT_USER)).toString();
        //Log.d(TAG, "\n\n<----> Received:" + email);

        plantController     = new PlantDatabaseController(this.getBaseContext());
        userPlantController = new UserPlantDatabaseController(this.getBaseContext());

        /* set layout */
        setContentView(R.layout.plants_view);

        /* Load data from API */
        getDBFilled();

        /* Load user's plants data from DB */
        getUserPlants();

        // Create notifications channel
        createNotificationChannel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        plantController.close();
        userPlantController.close();
    }
/** plants_view **/
    private void getDBFilled(){
        int result = isDBEmpty();
        if(result == -1)loadApiData();

        Log.d(TAG,"getDBFilled. Result: "+result);
    }

    /* API */
    private void jsonIntoDatabase(JSONObject plantObj) {
        try {
            //Log.d("jsonIntoDatabase", "IN jsonIntoDatabase, inside try");
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

            //Log.d("jsonIntoDatabase", "before plantController call");

            // validate if this plant exists
            long insert = plantController.insert(plantDP);
            Log.d("INSERT", "Data from API  was inserted into database: " + insert);

        } catch (Exception e) {
            Log.d("Exception", "jsonIntoDatabase. EXCEPTION: " + e.getMessage());
        }
    }

    private int isDBEmpty(){
        int result = -1;
        //Log.d(TAG, "isDBEmpty method ");

        Cursor cursor = plantController.selectPlantsName();
        try {
            if (cursor.getCount() > 0) {
                result = 1;
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception. isDBEmpty; exception: " + e.getMessage());
        }
        cursor.close();
        return result;
    }

    private void loadApiData(){
        Log.d("loadApiData", "\tloadApiData");

        RequestQueue queue = Volley.newRequestQueue(this);
        // requestTypeMethod, URL_PLANTS, responseListener(file), errorListener
        //Log.d("arrayRequest", "\tloadApiData: Request");
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                URL_PLANTS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    String jStr= "{\"id\":\"null\"}";

                    JSONObject plantObj = null;

                    plantObj = new JSONObject(jStr);

                    for(int i = 0; i < response.length(); i++) {
                        plantObj = response.getJSONObject(i);
                        jsonIntoDatabase(plantObj);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
        String selection = UserPlantDP.COLUMN_USER_EMAIL + " = ? ";
        String[] selectionArgs = { email };

        //Log.d(TAG,"getUserPlants. User email: "+email);
        //Log.d(TAG,"getUserPlants. selection: "+selection);

        Cursor cursor = userPlantController.selectContent(selection,selectionArgs);
        // int curSize = 0;

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
            // curSize++;

            /*Log.d(TAG, "\n## >>>>>> addUserPlant. curSize: "+curSize+"List size: "+list.size()+" __Card info. name: " + name + " health: " + health+ " last: "+last+" water: "
                    +water+" image: "+image+ " user: "+user+" plant: "+plant+" date: "+date);

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
    // this method calls PlantsAdd class to handle the plants_new screen
    public void addPlantUser(View view) {
        Intent intent = new Intent(this, PlantsAdd.class);
        intent.putExtra(EXTRA_INPUT_USER, email);
        startActivity(intent);
    }

    /** Notifications **/
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    void doNotification(String title, String text, String bigText) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1000, mBuilder.build());
    }
}
