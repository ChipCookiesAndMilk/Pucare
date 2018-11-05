package com.example.a01363207.pucare.UserPlantPackage;

import android.provider.BaseColumns;

public class UserPlantDP implements BaseColumns {
    public static final String TABLE_NAME = "UserPlant";

    public static final String COLUMN_NICKNAME          = "nickname";
    public static final String COLUMN_HEALTH            = "health";
    public static final String COLUMN_LAST_WATER        = "lastWater";
    public static final String COLUMN_NEXT_WATER        = "nextWater";

    public static final String COLUMN_ID_USER           = "idUser";
    public static final String COLUMN_ID_PLANT          = "idPlant";
    public static final String COLUMN_DATE_REGISTERED   = "dateRegistered";

    private int idUser, idPlant;
    private String nickname, health, lastWater, nextWater, dateRegistered;

/* CONSTRUCTORS */
    public UserPlantDP(){}

    public UserPlantDP(String nickname, String health, String lastWater, String nextWater, int idUser, int idPlant, String dateRegistered) {
        this.nickname       = nickname;
        this.health         = health;
        this.lastWater      = lastWater;
        this.nextWater      = nextWater;

        this.idUser         = idUser;
        this.idPlant        = idPlant;
        this.dateRegistered = dateRegistered;
    }

/* GETTERS  */

    public int getIdUser() {
        return idUser;
    }

    public int getIdPlant() {
        return idPlant;
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

/* SETTERS */

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setIdPlant(int idPlant) {
        this.idPlant = idPlant;
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
}
