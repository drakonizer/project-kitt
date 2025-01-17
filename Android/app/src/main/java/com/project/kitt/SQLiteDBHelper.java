package com.project.kitt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

public class SQLiteDBHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FoodDetails";

    public static final String TABLE_NAME = "food";
    public static final String KEY_FOOD_ID = "food_id";
    public static final String KEY_FOOD_NAME = "food_name";
    public static final String KEY_FOOD_DATE = "food_date";

    // Create Table Query
    private static String SQL_CREATE_FOOD =
            "CREATE TABLE food (" + KEY_FOOD_ID + " INTEGER PRIMARY KEY, " + KEY_FOOD_NAME + " TEXT, " + KEY_FOOD_DATE
            + " TEXT );";

    public static final String SQL_DELETE_FOOD =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public SQLiteDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FOOD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the table while upgrading the database
        // such as adding new column or changing existing constraint
        db.execSQL(SQL_DELETE_FOOD);
        this.onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        this.onUpgrade(db, oldVersion, newVersion);
    }

    public long addFood(FoodDetail food)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues food_detail = new ContentValues();
        food_detail.put(KEY_FOOD_NAME, food.getFoodName());
        food_detail.put(KEY_FOOD_DATE,food.getFoodDate());

        long newRowId = db.insert(TABLE_NAME, null, food_detail);
        db.close();
        return newRowId;
    }

//    public FoodDetail getFood(int food_id)
//    {
//        FoodDetail foodDetail = new FoodDetail();
//        SQLiteDatabase db = this.getReadableDatabase();
//        // specify the columns to be fetched
//        String[] columns = {KEY_FOOD_ID, KEY_FOOD_NAME, KEY_FOOD_DATE};
//        // select condition
//        String selection = KEY_FOOD_ID + " = ?";
//        // arguments for selection
//        String[] selectionArgs = {String.valueOf(food_id)};
//
//        Cursor cursor = db.query(TABLE_NAME, columns, selection,
//                selectionArgs, null, null, null);
//        if(null != cursor)
//    }

    public FoodDetail[] getAllFood()
    {
        int length = getCount();
        int i = length - 1;
        FoodDetail[] foodDetailsArray = new FoodDetail[length];

        String selectQuery = "SELECT * FROM " + TABLE_NAME
                + " ORDER BY " + KEY_FOOD_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        // if TABLE has rows
        if(cursor.moveToFirst())
        {
            // loop through the table rows
            do
                {
                    FoodDetail foodDetail = new FoodDetail();
                    foodDetail.setFoodID(cursor.getInt(0));
                    foodDetail.setFoodName(cursor.getString(1));
                    foodDetail.setFoodDate(cursor.getString(3));

                    foodDetailsArray[i] = foodDetail;
                    i--;
                } while(cursor.moveToNext());
        }

        db.close();
        return foodDetailsArray;
    }

    public int getCount()
    {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr = db.rawQuery("select * from " + TABLE_NAME + ";", null );
        StringBuilder sr = new StringBuilder();
        while(cr.moveToNext())
        {
            count++;
        }
        db.close();
        return count;
    }
}
