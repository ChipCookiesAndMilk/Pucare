package com.example.a01363207.pucare.UserPlantPackage;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a01363207.pucare.PlantPackage.PlantDP;
import com.example.a01363207.pucare.PlantPackage.PlantDatabaseController;
import com.example.a01363207.pucare.PlantsView;
import com.example.a01363207.pucare.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

/*
PlantsEdit Class  -> Handles the edit screen
	~ plants_edit.xml
		Show editable fields to load into the plants user DB

	~ plants_stats.xml
		Show the stats of a user plant
*/

public class PlantsEdit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static String EXTRA_INPUT_USER = "INPUT_USER";
    private static final String TAG = "PlantsEdit";
    // controllers
    UserPlantDatabaseController controller;
    PlantDatabaseController plantController;

    // global variables
    String user, date;
    String inP = "";
    String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get params
        String pk = (getIntent().getStringExtra(RecyclerViewAdapter.EXTRA_PLANT_PK)).toString();
            //Log.d(TAG, "-> Received: " + pk + " <<END");

        // load layout
        setContentView(R.layout.plants_stats);

        // initialize controllers
        controller = new UserPlantDatabaseController(this.getBaseContext());
        plantController = new PlantDatabaseController(this.getBaseContext());

        // tokenize the params
        tokenizePK(pk);

        // load info of selected plant
        loadCurrentPlantData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.close();
    }

/** prepares screen info */
    // loads the plants info to be edited
    private void loadCurrentPlantData() {
        UserPlantDP input = new UserPlantDP();

        String selection = UserPlantDP.COLUMN_USER_EMAIL + " = ? AND "
                + UserPlantDP.COLUMN_DATE_REGISTERED + " = ?";

        String[] selectionArgs = new String[]{user, date};

        Cursor cursor = controller.selectContent(selection, selectionArgs);

        while (cursor.moveToNext()) {
            String name     = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_NICKNAME));
            String health   = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_HEALTH));
            String last     = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_LAST_WATER));
            String water    = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_NEXT_WATER));
            String image    = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_IMAGE));
            String user     = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_USER_EMAIL));
            String plant    = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_PLANT_NAME));
            String date     = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_DATE_REGISTERED));

            input.setNickname(name);
            input.setHealth(health);
            input.setLastWater(last);
            input.setNextWater(water);
            input.setImage(image);
            input.setUserEmail(user);
            input.setPlantName(plant);
            input.setDateRegistered(date);

            // save the original plant type
            inP = plant;
            //Log.d(TAG, "addUserPlant. Card info. name " + name + "       ___water: " + water);
        }
        cursor.close();

        ImageView img   = (ImageView) findViewById(R.id.idPlantThumbnail);
        TextView name   = (TextView) findViewById(R.id.idName);
        TextView type   = (TextView) findViewById(R.id.tfType);
        TextView health = (TextView) findViewById(R.id.idHealth);
        TextView water  = (TextView) findViewById(R.id.idNextWater);

        new GetImageFromURL(img).execute(input.getImage());
        name.setText(input.getNickname());
        type.setText(input.getPlantName());
        health.setText(input.getHealth());
        water.setText(input.getNextWater());
    }

    // tokenize the params, primary key of the plant, user and date
    private void tokenizePK(String pk) {
        StringTokenizer st = new StringTokenizer(pk, "*");
        while (st.hasMoreTokens()) {
            user = (String) st.nextElement().toString();
            date = (String) st.nextElement().toString();
        }
        // Log.d(TAG,"tokenizePK. user: "+user+" __date: "+date);
    }

    // load the information from plant and display it
    private void prepareNextLayout(UserPlantDP input) {
        setContentView(R.layout.plants_edit);
        ImageView image = (ImageView) findViewById(R.id.idThumbnail);
        EditText nick = (EditText) findViewById(R.id.idNickName);
        Spinner spinner = (Spinner) findViewById(R.id.idSpinner);

        // set image
        new GetImageFromURL(image).execute(input.getImage());

        // set nickName
        nick.setText(input.getNickname());

        // add all the possible values to the spinner
        ArrayList<String> list;
        list = getPlantsOptions();
            // Log.d(TAG, "- - - - getPlantsOptions. list: "+list.toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_inner_layout, R.id.sData, list);
        spinner.setAdapter(adapter);
            //Log.d(TAG, "getPlantsOptions. Plants name were set");

        // set the current type of the plant
        ArrayAdapter adapterOption = (ArrayAdapter) spinner.getAdapter();
        int spinnerCurrentPosition = adapterOption.getPosition(input.getPlantName());
        spinner.setSelection(spinnerCurrentPosition);
            //Log.d(TAG, "getPlantsOptions. The type the plant already has was set");
            //Log.d(TAG, "getPlantsOptions. -------- Listener ------ ");
        spinner.setOnItemSelectedListener(this);
            //msg = "Was everything ok? ";
            //Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
            //Log.d(TAG,msg);
    }

    // gets the link of selected plant from database
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

    // get all the available plants in DB
    private ArrayList<String> getPlantsOptions() {
        ArrayList<String> list = new ArrayList<String>();
        Log.d(TAG, "getPlantsOptions. after array creation");

        Cursor cursor = plantController.selectPlantsName();
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(PlantDP.COLUMN_PLANT_NAME));
                    list.add(name);
                    //Log.d(TAG, "getPlantsOptions; name: " + name);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception. getPlantsOptions; exception: " + e.getMessage());
        }
        cursor.close();
        return list;
    }

    // returns the UserPlantDP that holds the information from selected plant in plants_view layout
    private UserPlantDP getUserPlant() {
        UserPlantDP input = new UserPlantDP();

        String selection = UserPlantDP.COLUMN_USER_EMAIL + " = ? AND " + UserPlantDP.COLUMN_DATE_REGISTERED + " = ?";
        String[] selectionArgs = {user, date};

        Cursor cursor = controller.selectContent(selection, selectionArgs);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_NICKNAME));
            String health = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_HEALTH));
            String last = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_LAST_WATER));
            String water = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_NEXT_WATER));
            String image = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_IMAGE));
            String user = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_USER_EMAIL));
            String plant = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_PLANT_NAME));
            String date = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_DATE_REGISTERED));

            input.setNickname(name);
            input.setHealth(health);
            input.setLastWater(last);
            input.setNextWater(water);
            input.setImage(image);
            input.setUserEmail(user);
            input.setPlantName(plant);
            input.setDateRegistered(date);
            /*
            Log.d(TAG, "\n## >>>>>> addUserPlant.  I N P U T info. name: " + name + " health: " + health+ " last: "+last+" water: "
                    +water+" image: "+image+ " email: "+user+" plant: "+plant+" date: "+date);
            */
        }
        cursor.close();

        return input;
    }

/** ONCLICK **/
    // responds to button.onClick. on edit Edits the info of a plant from given user
    public void edit(View view) {

        UserPlantDP input = new UserPlantDP();

        String selection = UserPlantDP.COLUMN_USER_EMAIL + " = ? AND "
                + UserPlantDP.COLUMN_DATE_REGISTERED + " = ?";

        String[] selectionArgs = new String[]{user, date};

        Cursor cursor = controller.selectContent(selection, selectionArgs);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_NICKNAME));
            String health = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_HEALTH));
            String last = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_LAST_WATER));
            String water = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_NEXT_WATER));
            String image = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_IMAGE));
            String user = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_USER_EMAIL));
            String plant = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_PLANT_NAME));
            String date = cursor.getString(cursor.getColumnIndex(UserPlantDP.COLUMN_DATE_REGISTERED));

            input.setNickname(name);
            input.setHealth(health);
            input.setLastWater(last);
            input.setNextWater(water);
            input.setImage(image);
            input.setUserEmail(user);
            input.setPlantName(plant);
            input.setDateRegistered(date);

            Log.d(TAG, "addUserPlant. Card info. name " + name + "       ___water: " + water);
        }
        cursor.close();

        prepareNextLayout(input);
        //Log.d(TAG, "addUserPlant. New plants view");
        //Log.d(TAG, "addUserPlant. parameters sent");
    }

    // gets the time to water according to the type of plant
    // request when to water a plant to the DB
    public String getTimeToWater(String plantName) {
        String water = "";

        Log.d(TAG, "In getTimeToWater method ");

        Cursor cursor = plantController.selectPlantsWater(plantName);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    water = cursor.getString(cursor.getColumnIndex(PlantDP.COLUMN_WATER));
                    Log.d(TAG, "getWater; water: " + water);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception. getWater; exception: " + e.getMessage());
        }
        cursor.close();
        return water;
    }

    // updates the plants information, from view to DB
    public void save(View view) {
        EditText nick = (EditText) findViewById(R.id.idNickName);
        Spinner spinner = (Spinner) findViewById(R.id.idSpinner);

        String sNick = nick.getText().toString();
        String sType = spinner.getSelectedItem().toString();

        if (sNick.isEmpty()) {
            String message = "Mmmm... Your plant " + nick + " must have a name";
                //Log.d(TAG, message);
        } else {

            UserPlantDP input;
            input = getUserPlant();

            // update nickName, type, next water and image
            input.setNickname(sNick);

            if(!inP.equals(sType)){
                // update new type
                input.setPlantName(sType);
                // get last date in order to get the new one
                String prevLast = input.getLastWater();
                    //String prevNext = input.getNextWater();
                String nextWater = getTimeToWater(sType);
                    // calculate and set this new date
                OperationsHandler oh = new OperationsHandler();
                input.setNextWater(oh.nextWater(prevLast,nextWater));
                // change image
                input.setImage(getPlantsImage(sType));
            }

            //format for update(String table, ContentValues values, String whereClause, String[] whereArgs)
            long inserted = controller.update(input);
                Log.d(TAG, "bSave.onClick. Data was sent. " + inserted);
        }

        msg = "Your plant was updated";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            //Log.d(TAG, msg);

        Intent intent = new Intent(this, PlantsView.class);
        intent.putExtra(EXTRA_INPUT_USER, user);

        startActivity(intent);
        Log.d(TAG, "Plant was uploaded. Display Catalogue");
    }

    // deletes a userPlant from DB
    public void delete(View view) {
        long deleted = controller.delete(user, date);
        if(deleted == 1)
            msg = "Your plant was deleted "+deleted;
        else    // may be no record was deleted or more that one was deleted :(
            msg = "Something went wrong";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, msg);
    }

/** spinner handler **/
    // manages the spinner options
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        String spValue = (String) parent.getItemAtPosition(position).toString();
        Log.d(TAG, "onItemSelected. value: " + spValue);
        // Change image icon
        // get image url

        String link = getPlantsImage(spValue);
        ImageView icon = (ImageView) findViewById(R.id.idThumbnail);

        if (link.isEmpty()) {
            Log.d(TAG, "onItemSelected. an image for " + spValue + " was not found");
        }   // execute param is the url of the image
        else new GetImageFromURL(icon).execute(link);

        msg = "onItemSelected. Hi ";
        //Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        Log.d(TAG, msg);
    }

    // do nothing in here but requested
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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
