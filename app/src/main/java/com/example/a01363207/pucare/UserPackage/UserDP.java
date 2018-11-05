package com.example.a01363207.pucare.UserPackage;

import android.provider.BaseColumns;

public class UserDP implements BaseColumns {

    public static final String TABLE_NAME       = "User";

    public static final String COLUMN_IDUSER   = "idUser";
    public static final String COLUMN_USERNAME  = "nameUser";
    public static final String COLUMN_EMAIL     = "email";
    public static final String COLUMN_PASSWORD  = "password";

    private int idUser;
    private String userName;
    private String email;
    private String password;

    /* CONSTRUCTORS */
    public UserDP(){}

    public UserDP(int idUser, String userName, String email, String password) {
        this.idUser     = idUser;
        this.userName   = userName;
        this.email      = email;
        this.password   = password;
    }

    /* GETTERS */
    public int
    getIdUser() { return idUser; }

    public String
    getUserName() { return userName; }

    public String
    getEmail() { return email; }

    public String
    getPassword() { return password; }


    /* SETTERS */
    public void
    setIdUser(int idUser) { this.idUser = idUser; }

    public void
    setUserName(String userName) { this.userName = userName; }

    public void
    setEmail(String email) { this.email = email; }

    public void
    setPassword(String password) { this.password = password; }
}
