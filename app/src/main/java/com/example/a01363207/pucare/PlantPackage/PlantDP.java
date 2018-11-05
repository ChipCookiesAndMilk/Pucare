package com.example.a01363207.pucare.PlantPackage;

import android.provider.BaseColumns;

public class PlantDP implements BaseColumns {

    public static final String TABLE_NAME       = "Plant";

    public static final String COLUMN_IDPLANT   = "idPlant";
    public static final String COLUMN_PLANTNAME = "plantName";
    public static final String COLUMN_TYPE      = "plantType";
    public static final String COLUMN_SUNLIGHT  = "sunlight";
    public static final String COLUMN_HEIGHT    = "height";
    public static final String COLUMN_WATER     = "water";
    public static final String COLUMN_IMAGE     = "image";

    private int idPlant;
    private String plantName, plantType, sunlight, height, water, image;

/* CONSTRUCTORS */
    public PlantDP(){}

    public PlantDP(int idPlant, String plantName, String plantType, String sunlight, String height, String water, String image) {
        this.idPlant    = idPlant;
        this.plantName  = plantName;
        this.plantType  = plantType;
        this.sunlight   = sunlight;
        this.height     = height;
        this.water      = water;
        this.image      = image;
    }

/* GETTERS */
    public int
    getIdPlant() { return idPlant; }

    public String
    getPlantName() { return plantName; }

    public String
    getPlantType() { return plantType; }

    public String
    getSunlight() { return sunlight; }

    public String
    getHeight() { return height; }

    public String
    getWater() { return water; }

    public String
    getImage() { return image; }


/* SETTERS */
    public void
    setIdPlant(int idPlant) { this.idPlant = idPlant; }

    public void
    setPlantName(String plantName) { this.plantName = plantName; }

    public void
    setPlantType(String plantType) { this.plantType = plantType; }

    public void
    setSunlight(String sunlight) { this.sunlight = sunlight; }

    public void
    setHeight(String height) { this.height = height; }

    public void
    setWater(String water) { this.water = water; }

    public void
    setImage(String image) { this.image = image; }

}
