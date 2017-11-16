package com.nanoddegree.inventoryapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nanoddegree.inventoryapp.data.ProductContract.ProductEntry;

public class ProductDbHelper extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "products.db";

  public ProductDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
            ProductEntry._ID + " INTEGER PRIMARY KEY," +
            ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL," +
            ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0," +
            ProductEntry.COLUMN_PRODUCT_PRICE + " TEXT)";

    db.execSQL(SQL_CREATE_ENTRIES);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

  }
}
