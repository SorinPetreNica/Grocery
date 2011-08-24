package com.amazing;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAdapter 
{
    public static final String KEY_ROWID = "_id";
    //public static final String KEY_ISBN = "isbn";
    //public static final String KEY_TITLE = "title";
    //public static final String KEY_PUBLISHER = "publisher";    
    private static final String TAG = "DBAdapter";
    public static final String KEY_NAME = "name";
    public static final String KEY_AVG = "avg";
    public static final String KEY_ARR = "arrival_rate";
    public static final String KEY_RESTOCK = "restock_days";
    public static final String KEY_LAST = "last_date";
    public static final String KEY_FIRST = "first_date";
    public static final String KEY_NR = "nr";
    public static final String KEY_DATE = "date";
    public static final String KEY_ITEMID = "itemid";
    
    private static final String DATABASE_NAME = "grocery";
    private static final String DATABASE_TABLE1 = "items";
    private static final String DATABASE_TABLE2 = "dates";
    private static final int DATABASE_VERSION = 1;

    private static final String DATATABLE1_CREATE =
        "create table items (_id integer primary key autoincrement, "
        + "name text not null, avg float not null, arrival_rate float not null, restock_days integer not null, first_date text not null, " 
        + "last_date text not null);";
    
    private static final String DATATABLE2_CREATE =
        "create table dates (_id integer primary key autoincrement, itemid integer not null, nr integer not null, "
        + "date text not null);";
        
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATATABLE1_CREATE);
            db.execSQL(DATATABLE2_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }    
    
    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a title into the database---
    public long insertItem(String name, float avg, float arr, float restock, String first, String last) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_AVG, avg);
        initialValues.put(KEY_ARR, arr);
        initialValues.put(KEY_RESTOCK, restock);
        initialValues.put(KEY_FIRST, first.toString());
        initialValues.put(KEY_LAST, last.toString());
        return db.insert(DATABASE_TABLE1, null, initialValues);
    }
    
    public long insertDate(int itemId, int nr,  String date) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ITEMID, itemId);
        initialValues.put(KEY_NR, nr);
        initialValues.put(KEY_DATE, date.toString());
        return db.insert(DATABASE_TABLE2, null, initialValues);
    }

    //---deletes a particular title---
    public boolean deleteItem(long rowId) 
    {
        return db.delete(DATABASE_TABLE1, KEY_ROWID + 
        		"=" + rowId, null) > 0;
    }

    //---retrieves all the titles---
    public Cursor getAllItems() 
    {
        return db.query(DATABASE_TABLE1, new String[] {
        		KEY_ROWID, 
        		KEY_NAME,
        		KEY_AVG,
        		KEY_ARR,
        		KEY_RESTOCK,
                KEY_LAST,
                KEY_FIRST}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    public Cursor getAllDates(long rowId) 
    {
        return db.query(DATABASE_TABLE2, new String[] {
        		KEY_ROWID, 
        		KEY_ITEMID,
        		KEY_NR,
                KEY_DATE}, 
                KEY_ITEMID + "=" + rowId, 
                null, 
                null, 
                null, 
                null);
    }

    //---retrieves a particular title---
    public Cursor getItem(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE1, new String[] {
                		KEY_ROWID, 
                		KEY_NAME,
                		KEY_AVG,
                		KEY_ARR,
                		KEY_RESTOCK,
                        KEY_LAST,
                        KEY_FIRST
                		}, 
                		KEY_ROWID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor getDate(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE2, new String[] {
                		KEY_ROWID, 
                		KEY_ITEMID,
                		KEY_NR,
                        KEY_DATE
                		}, 
                		KEY_ROWID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a title---
    public boolean updateItem(long rowId, float avg, float arr, float restock, String last) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_AVG, avg);
        args.put(KEY_ARR, arr);
        args.put(KEY_RESTOCK, restock);
        args.put(KEY_LAST, last.toString());
        return db.update(DATABASE_TABLE1, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
}
