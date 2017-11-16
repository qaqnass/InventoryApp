package com.nanoddegree.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nanoddegree.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by qaqnass on 15/11/17.
 */

public class ProductCursorAdapter extends CursorAdapter {
   Context context;

  public ProductCursorAdapter(Context context, Cursor c) {
    super(context, c, 0);
    this.context = context;
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup product) {
    return LayoutInflater.from(context).inflate(R.layout.list_product, product, false);
  }

  @Override
  public void bindView(View view, final Context context, Cursor cursor) {

    final TextView nameTextView = (TextView) view.findViewById(R.id.product_name);
    final TextView quantityTextView = (TextView) view.findViewById(R.id.product_quantity);
    final TextView priceTextView = (TextView) view.findViewById(R.id.product_price);
//    Button salebtn = (Button) view.findViewById(R.id.product_sale);

    final int id = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_ID);
    int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
    int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
    int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);

    String productName = cursor.getString(nameColumnIndex);
    final String productQuantity = cursor.getString(quantityColumnIndex);
    String productPrice = cursor.getString(priceColumnIndex);

    nameTextView.setText(productName);
    quantityTextView.setText(productQuantity);
    priceTextView.setText(productPrice);

//    salebtn.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        System.out.println("ddddddddddddddddddddddddddddd"+id);
//        String nameString = nameTextView.getText().toString().trim();
//        String quantityString = quantityTextView.getText().toString().trim();
//        String priceString = priceTextView.getText().toString().trim();
//
//        int quantity = Integer.parseInt(quantityString);
//        int increaseByOne = quantity - 1;
//        ContentValues values = new ContentValues();
//        values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameString);
//        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, increaseByOne);
//        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, priceString);
//
//        Uri currentUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
//        int rowsAffected = context.getContentResolver().update(currentUri, values, null, null);
//      }
//    });
  }
}
