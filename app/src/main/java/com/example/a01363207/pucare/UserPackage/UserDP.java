package com.example.a01363207.pucare.UserPackage;

import android.provider.BaseColumns;

public class UserDP implements BaseColumns {

    public static final String TABLE_NAME       = "User";

    public static final String COLUMN_USERNAME  = "nameUser";
    public static final String COLUMN_PASSWORD  = "password";

    private String userName;
    private String password;

    /* CONSTRUCTORS */
    public UserDP(){}

    public UserDP(String userName, String password) {
        this.userName   = userName;
        this.password   = password;
    }

    /* GETTERS */
    public String
    getUserName() { return userName; }

    public String
    getPassword() { return password; }

    /* SETTERS */
    public void
    setUserName(String userName) { this.userName = userName; }

    public void
    setPassword(String password) { this.password = password; }
}
