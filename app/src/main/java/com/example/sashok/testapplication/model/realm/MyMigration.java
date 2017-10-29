package com.example.sashok.testapplication.model.realm;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by sashok on 27.10.17.
 */

public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (schema.get("Image").hasField("Uri"))
            schema.get("Image").removeField("Uri");
        if (!schema.get("Image").hasField("UserId"))
            schema.get("Image").addField("UserId", int.class);
        if (oldVersion == 1) {

            oldVersion++;
        }
        if (oldVersion == 2) oldVersion++;
    }


}