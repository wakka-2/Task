package com.task.app.util.shared;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.task.app.model.User;


public class SharedPrefsUtil {

    private static final String PREF_FILE_NAME = "patient_case";


    public static void saveString(@NonNull Context context, @NonNull String key, @NonNull String value) {
        if (context != null) {
            getSharedPreference(context).edit().putString(key, value).apply();
        }
    }

    public static void saveDouble(@NonNull Context context, @NonNull String key, float value) {
        if (context != null) {
            getSharedPreference(context).edit().putFloat(key, value).apply();
        }
    }

    public static void saveInteger(@NonNull Context context, @NonNull String key, int value) {
        if (context != null) {
            getSharedPreference(context).edit().putInt(key, value).apply();
        }
    }

    public static void saveBoolean(@NonNull Context context, @NonNull String key, boolean value) {
        if (context != null) {
            getSharedPreference(context).edit().putBoolean(key, value).apply();
        }
    }

    public static int getInteger(@NonNull Context context, @NonNull String key, int defValue) {
        if (context != null) {
            return getSharedPreference(context).getInt(key, defValue);
        } else {
            return defValue;
        }
    }

    public static boolean getBoolean(@NonNull Context context, @NonNull String key, boolean defValue) {
        if (context != null) {
            return getSharedPreference(context).getBoolean(key, defValue);
        } else {
            return defValue;
        }
    }

    public static float getFloat(@NonNull Context context, @NonNull String key, float defValue) {
        if (context != null) {
            return getSharedPreference(context).getFloat(key, defValue);
        } else {
            return defValue;
        }
    }

    public static String getString(@NonNull Context context, @NonNull String key, String defValue) {
        if (context != null) {
            return getSharedPreference(context).getString(key, defValue);
        } else{
            return defValue;
        }
    }

    public static void clearPreference(@NonNull Context context, @NonNull String key) {
        if (context != null) {
            getSharedPreference(context).edit().remove(key).apply();
        }
    }

    public static void clearAll(@NonNull Context context) {
        if (context != null) {
            getSharedPreference(context).edit().clear().apply();
        }
    }

    private static SharedPreferences getSharedPreference(@NonNull Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void clearUser(Context context){
        new TinyDB(context).remove("USER");

    }

    public static void storeUser(Context context, User loginData){
        new TinyDB(context).putObject("USER",loginData);
    }
    public static User fetchUser(Context context){
        TinyDB tinyDB = new TinyDB(context);
        tinyDB.getObject("USER",User.class);
        return tinyDB.getObject("USER",User.class);
    }

}
