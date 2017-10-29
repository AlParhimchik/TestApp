package com.example.sashok.testapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sashok.testapplication.network.model.auth.UserResponse;

/**
 * Created by sashok on 26.10.17.
 */

public class SessionManager {
    private static String fileName = "sharedpreference";
    private static String LOGIN_TOKEN = "login_token";
    private static String ACCESS_TOKEN = "access_token";
    private static String ID_TOKEN = "id_token";
    private static String IS_LOGIN = "is_login";
    private UserResponse user;

    public void createLoginSession(UserResponse user) {
        this.user = user;
    }

    public SessionManager(Context context) {

    }

    public static void login(Context context, UserResponse user) {
        SharedPreferences sharedPref = context.getSharedPreferences(fileName
                , Context.MODE_PRIVATE);
        sharedPref.edit()
                .putInt(ID_TOKEN, user.getUserId())
                .putString(LOGIN_TOKEN, user.getLogin())
                .putString(ACCESS_TOKEN, user.getToken())
                .putBoolean(IS_LOGIN, true)
                .apply();

    }

    public static void logout(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(fileName
                , Context.MODE_PRIVATE);
        sharedPref.edit().putBoolean(IS_LOGIN, false);

    }

    public static boolean isLogin(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(fileName
                , Context.MODE_PRIVATE);
        return sharedPref.getBoolean(IS_LOGIN, false);
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(fileName
                , Context.MODE_PRIVATE);
        return sharedPref.getString(ACCESS_TOKEN, "");
    }

    public static int getUserId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(fileName
                , Context.MODE_PRIVATE);
        return sharedPref.getInt(ID_TOKEN, 0);
    }

    public static String getLogin(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(fileName
                , Context.MODE_PRIVATE);
        return sharedPref.getString(LOGIN_TOKEN, "");
    }
}
