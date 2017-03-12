package com.reversecoder.javamail.androidstudio.k9.mailstore.migrations;


import android.database.sqlite.SQLiteDatabase;


class MigrationTo32 {
    public static void updateDeletedColumnFromFlags(SQLiteDatabase db) {
        db.execSQL("UPDATE messages SET deleted = 1 WHERE flags LIKE '%DELETED%'");
    }
}
