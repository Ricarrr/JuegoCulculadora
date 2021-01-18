package ea.ricardo.cal4kids;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ConsultasBD extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "hist.bd";
    public final static int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "rank";
    public static final String NOMBRE = "name";
    public static final String MODO = "mode";
    public static final String PUNTOS = "punt";

    public ConsultasBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                NOMBRE + " TEXT NOT NULL, " +
                MODO + " TEXT NOT NULL ," +
                PUNTOS + " TEXT NOT NULL," +
                "PRIMARY KEY ("+NOMBRE+","+ MODO+"))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void crear(String jugador, int puntos, String modo){
        try{
            SQLiteDatabase dbW = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NOMBRE, jugador);
            values.put(MODO, modo);
            values.put(PUNTOS, puntos);
            if(dbW.insert( TABLE_NAME,null, values) == -1)
                return;
            dbW.close();
        }catch (Exception e){

        }
    }

    public void añade(String jugador, int puntos, String modo){
        SQLiteDatabase dbr = getReadableDatabase();
        Cursor c = dbr.rawQuery("SELECT * FROM "+TABLE_NAME + " WHERE " + NOMBRE + "='"+jugador+"' AND " + MODO + "='"+modo+"'",null);
        if(c.moveToFirst()){
            if(c.getInt(2)<puntos){
                SQLiteDatabase dbW = getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(NOMBRE, jugador);
                values.put(MODO, modo);
                values.put(PUNTOS, puntos);
                dbW.execSQL("UPDATE " + TABLE_NAME + " SET "+ PUNTOS + "="+puntos+" WHERE "+ NOMBRE +"='"+jugador+"' AND " + MODO + "='"+modo+"'");
                dbW.close();
            }
        }else{
            SQLiteDatabase dbW = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NOMBRE, jugador);
            values.put(MODO, modo);
            values.put(PUNTOS, puntos);
            dbW.insert( TABLE_NAME,null, values);
            dbW.close();
        }
        dbr.close();
    }

    public ArrayList<Puntuacion> recogeInfo(){
        ArrayList<Puntuacion> punt = new ArrayList<Puntuacion>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        if(c.moveToFirst()) {
            do {
                String nombre = c.getString(0);
                int puntuacion = c.getInt(2);
                punt.add(new Puntuacion(nombre, puntuacion, c.getString(1)));
            } while (c.moveToNext());
        }
        return punt;
    }

    public ArrayList<Puntuacion> rankFacil(){
        ArrayList<Puntuacion> punt = new ArrayList<Puntuacion>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE " + MODO +"='FÁCIL' ORDER BY " + PUNTOS + " ASC",null);
        if(c.moveToFirst()) {
            do {
                String nombre = c.getString(0);
                int puntuacion = c.getInt(2);
                punt.add(new Puntuacion(nombre, puntuacion, c.getString(1)));
            } while (c.moveToNext());
        }
        return punt;
    }

    public ArrayList<Puntuacion> rankNormal(){
        ArrayList<Puntuacion> punt = new ArrayList<Puntuacion>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE " + MODO +"='NORMAL' ORDER BY " + PUNTOS + " ASC",null);
        if(c.moveToFirst()) {
            do {
                String nombre = c.getString(0);
                int puntuacion = c.getInt(2);
                punt.add(new Puntuacion(nombre, puntuacion, c.getString(1)));
            } while (c.moveToNext());
        }
        return punt;
    }

    public ArrayList<Puntuacion> rankExtremo(){
        ArrayList<Puntuacion> punt = new ArrayList<Puntuacion>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE " + MODO +"='EXTREMO' ORDER BY " + PUNTOS + " ASC",null);
        if(c.moveToFirst()) {
            do {
                String nombre = c.getString(0);
                int puntuacion = c.getInt(2);
                punt.add(new Puntuacion(nombre, puntuacion, c.getString(1)));
            } while (c.moveToNext());
        }
        return punt;
    }
}
