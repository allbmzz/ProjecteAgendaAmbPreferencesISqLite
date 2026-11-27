package com.example.projecteloggerambsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "agenda.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CONTACTES = "contactes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOM = "nom";
    public static final String COLUMN_COGNOM = "cognom";
    public static final String COLUMN_ADRECA = "adreca";
    public static final String COLUMN_TELEFON = "telefon";
    public static final String COLUMN_MAIL = "mail";
    public static final String COLUMN_DATA_NAIXEMENT = "data_naixement";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACTES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOM + " TEXT, "
                + COLUMN_COGNOM + " TEXT, "
                + COLUMN_ADRECA + " TEXT, "
                + COLUMN_TELEFON + " INTEGER, "
                + COLUMN_MAIL + " TEXT, "
                + COLUMN_DATA_NAIXEMENT + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTES);
        onCreate(db);
    }

    public long inserirContacte(Contacte contacte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, contacte.getNom());
        values.put(COLUMN_COGNOM, contacte.getCognom());
        values.put(COLUMN_ADRECA, contacte.getAdreca());
        values.put(COLUMN_TELEFON, contacte.getTelefon());
        values.put(COLUMN_MAIL, contacte.getMail());
        if (contacte.getDataNaixement() != null) {
            values.put(COLUMN_DATA_NAIXEMENT, dateFormat.format(contacte.getDataNaixement()));
        } else {
            values.put(COLUMN_DATA_NAIXEMENT, "");
        }

        long id = db.insert(TABLE_CONTACTES, null, values);
        db.close();
        return id;
    }

    public int actualitzarContacte(Contacte contacte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, contacte.getNom());
        values.put(COLUMN_COGNOM, contacte.getCognom());
        values.put(COLUMN_ADRECA, contacte.getAdreca());
        values.put(COLUMN_TELEFON, contacte.getTelefon());
        values.put(COLUMN_MAIL, contacte.getMail());
        if (contacte.getDataNaixement() != null) {
            values.put(COLUMN_DATA_NAIXEMENT, dateFormat.format(contacte.getDataNaixement()));
        } else {
            values.put(COLUMN_DATA_NAIXEMENT, "");
        }

        int rows = db.update(TABLE_CONTACTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(contacte.getId())});
        db.close();
        return rows;
    }

    public void esborrarContacte(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Contacte> obtenirTotsElsContactes() {
        List<Contacte> contactesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOM));
                String cognom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COGNOM));
                String adreca = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADRECA));
                Integer telefon = cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_TELEFON)) ? null : cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TELEFON));
                String mail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MAIL));
                String dataStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA_NAIXEMENT));

                Date dataNaixement = null;
                if (dataStr != null && !dataStr.isEmpty()) {
                    try {
                        dataNaixement = dateFormat.parse(dataStr);
                    } catch (ParseException e) {
                        Log.e("DatabaseOpenHelper", "Error parsing date", e);
                    }
                }

                Contacte contacte = new Contacte(id, nom, cognom, adreca, telefon, mail, dataNaixement);
                contactesList.add(contacte);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactesList;
    }
}
