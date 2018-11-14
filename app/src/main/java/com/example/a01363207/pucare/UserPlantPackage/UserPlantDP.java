package com.example.a01363207.pucare.UserPlantPackage;

import android.provider.BaseColumns;

public class UserPlantDP implements BaseColumns {
    public static final String TABLE_NAME = "UserPlant";

    public static final String COLUMN_NICKNAME          = "nickname";
    public static final String COLUMN_HEALTH            = "health";
    public static final String COLUMN_LAST_WATER        = "lastWater";
    public static final String COLUMN_NEXT_WATER        = "nextWater";
    public static final String COLUMN_IMAGE             = "image";

    public static final String COLUMN_USER_EMAIL        = "email";
    public static final String COLUMN_PLANT_NAME        = "plantName";
    public static final String COLUMN_DATE_REGISTERED   = "dateRegistered";

    private String userEmail, plantName;
    private String nickname, health, lastWater, nextWater, dateRegistered, image;

/* CONSTRUCTORS */
    public UserPlantDP(){}

    public UserPlantDP(String nickname, String health, String lastWater, String nextWater, String image, String idUser, String idPlant, String dateRegistered) {
        this.nickname       = nickname;
        this.health         = health;
        this.lastWater      = lastWater;
        this.nextWater      = nextWater;
        this.image          = image;

        this.userEmail      = idUser;
        this.plantName      = idPlant;
        this.dateRegistered = dateRegistered;
    }

/* GETTERS  */
    public String getUserEmail() {
        return userEmail;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHealth() {
        return health;
    }

    public String getLastWater() {
        return lastWater;
    }

    public String getNextWater() {
        return nextWater;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public String getImage() { return image; }

/* SETTERS */
    public void setUserEmail(String idUser) {
        this.userEmail = idUser;
    }

    public void setPlantName(String idPlant) {
        this.plantName = idPlant;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public void setLastWater(String lastWater) {
        this.lastWater = lastWater;
    }

    public void setNextWater(String nextWater) {
        this.nextWater = nextWater;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public void setImage(String image) {
        this.image = image;
    }
}