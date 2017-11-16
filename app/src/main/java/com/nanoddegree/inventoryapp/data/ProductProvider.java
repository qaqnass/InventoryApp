package com.nanoddegree.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.nanoddegree.inventoryapp.data.ProductContract.*;

public class ProductProvider extends ContentProvider {

  public static final String LOG_TAG = ProductProvider.class.getSimpleName();

  private static final int PRODUCTS = 100;

  private static final int PRODUCT_ID = 101;

  private ProductDbHelper mDbHelper;

  private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCT, PRODUCTS);
    sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCT + "/#", PRODUCT_ID);
  }

  @Override
  public boolean onCreate() {
    mDbHelper = new ProductDbHelper(getContext());
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                      String sortOrder) {

    SQLiteDatabase database = mDbHelper.getReadableDatabase();

    Cursor cursor = null;

    // Figure out if the URI matcher can match the URI to a specific code
    int match = sUriMatcher.match(uri);
    switch (match) {
      case PRODUCTS:
        cursor = database.query(ProductEntry.TABLE_NAME, projection, null, null,
            null, null, sortOrder);
        break;
      case PRODUCT_ID:
        selection = ProductEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

        cursor = database.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs,
            null, null, sortOrder);
        break;
      default:
        throw new IllegalArgumentException("Cannot query unknown URI " + uri);
    }

    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }

  /**
   * Insert new data into the provider with the given ContentValues.
   */
  public Uri insert(Uri uri, ContentValues contentValues) {
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case PRODUCTS:
        return insertProduct(uri, contentValues);
      default:
        throw new IllegalArgumentException("Insertion is not supported for " + uri);
    }
  }

  private Uri insertProduct(Uri uri, ContentValues values) {

    String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
    if (name == null) {
      throw new IllegalArgumentException("product requires a name");
    }

    Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
    if (quantity != null && quantity < 0) {
      throw new IllegalArgumentException("Product requires valid quantity");
    }

    String price = values.getAsString(ProductEntry.COLUMN_PRODUCT_PRICE);
    if (name == null) {
      throw new IllegalArgumentException("product requires a price");
    }

    // Get writeable database
    SQLiteDatabase database = mDbHelper.getWritableDatabase();

    long id = database.insert(ProductEntry.TABLE_NAME, null, values);

    if (id == -1) {
      Log.e(LOG_TAG, "Failed to insert row for " + uri);
      return null;
    }

    getContext().getContentResolver().notifyChange(uri, null);
    // Return the new URI with the ID (of the newly inserted row) appended at the end
    return ContentUris.withAppendedId(uri, id);
  }

  @Override
  public int update(Uri uri, ContentValues contentValues, String selection,
                    String[] selectionArgs) {
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case PRODUCTS:
        return updateProduct(uri, contentValues, selection, selectionArgs);
      case PRODUCT_ID:
        selection = ProductEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        return updateProduct(uri, contentValues, selection, selectionArgs);
      default:
        throw new IllegalArgumentException("Update is not supported for " + uri);
    }
  }

  private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    if (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) {
      String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
      if (name == null) {
        throw new IllegalArgumentException("Product requires a name");
      }
    }

    if (values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY)) {
      Integer quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
      if (quantity != null && quantity < 0) {
        throw new IllegalArgumentException("Product requires valid quantity");
      }
    }

    if (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) {
      String price = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
      if (price == null) {
        throw new IllegalArgumentException("Product requires a price");
      }
    }

    if (values.size() == 0) {
      return 0;
    }


    SQLiteDatabase database = mDbHelper.getWritableDatabase();
    int rowsUpdated = database.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);

    if (rowsUpdated != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }

    return rowsUpdated;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    // Get writeable database
    SQLiteDatabase database = mDbHelper.getWritableDatabase();

    final int match = sUriMatcher.match(uri);
    switch (match) {
      case PRODUCTS:
        return database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
      case PRODUCT_ID:
        selection = ProductEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        int rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted != 0) {
          getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
      default:
        throw new IllegalArgumentException("Deletion is not supported for " + uri);
    }
  }

  @Override
  public String getType(Uri uri) {
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case PRODUCTS:
        return ProductEntry.CONTENT_LIST_TYPE;
      case PRODUCT_ID:
        return ProductEntry.CONTENT_ITEM_TYPE;
      default:
        throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
    }
  }
}