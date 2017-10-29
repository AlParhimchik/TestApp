package com.example.sashok.testapplication;

import android.app.Application;

import com.example.sashok.testapplication.model.realm.MyMigration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sashok on 26.10.17.
 */

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        instance = this;
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(3)
                .migration(new MyMigration())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }

    public static App getInstance() {
        return instance;
    }
}
