package ru.finance2.simplecrmandroid;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ru.finance2.simplecrmandroid.models.Entity;
import ru.finance2.simplecrmandroid.models.OneWayEntity;
import ru.finance2.simplecrmandroid.models.TwoWaysEntity;


public class MainDbHelper extends SQLiteOpenHelper {

    private Context context;


    // DATABASE INFORMATION
    static final String DB_NAME = "MAIN.DB";
    static final int DB_VERSION = 1;

    private MainDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
    private static MainDbHelper instance;
    public static synchronized MainDbHelper getInstance(Context context)
    {
        if (instance == null)
            instance = new MainDbHelper(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Entity.CREATE_TABLE_QUERY);
        db.execSQL(OneWayEntity.CREATE_TABLE_QUERY);
        db.execSQL(TwoWaysEntity.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Entity.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OneWayEntity.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TwoWaysEntity.TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

}