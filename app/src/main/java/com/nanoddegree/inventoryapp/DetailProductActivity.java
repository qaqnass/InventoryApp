package com.nanoddegree.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nanoddegree.inventoryapp.data.ProductContract.ProductEntry;


public class DetailProductActivity extends AppCompatActivity implements
    LoaderManager.LoaderCallbacks<Cursor> {

  private EditText mNameEditText;
  private EditText mQuantityEditText;
  private EditText mPriceEditText;
  private Button increase;
  private Button decrease;
  private Button deleteProduct;
  private Button updateProduct;
  private Button orderProduct;

  int tempQuantity = 0;

  private static final int EXISTING_PRODUCT_LOADER = 0;
  private Uri mCurrentProductUri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_detail);

    Intent intent = getIntent();
    mCurrentProductUri = intent.getData();

    if (mCurrentProductUri != null) {
      getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
    }

    mNameEditText = (EditText) findViewById(R.id.edit_product_name_input);
    mQuantityEditText = (EditText) findViewById(R.id.edit_product_quantity_input);
    mPriceEditText = (EditText) findViewById(R.id.edit_product_price_input);

    increase = (Button) findViewById(R.id.increase);
    decrease = (Button) findViewById(R.id.decrease);
    deleteProduct = (Button) findViewById(R.id.delete_product);
    updateProduct = (Button) findViewById(R.id.update_product_btn);
    orderProduct = (Button) findViewById(R.id.order_product);


    increase.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        tempQuantity = Integer.parseInt(mQuantityEditText.getText().toString()) + 1;
        mQuantityEditText.setText(tempQuantity + "");
        updateQuantityHandler();
      }
    });

    decrease.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        int quantityNumber = Integer.parseInt(mQuantityEditText.getText().toString());
        if (quantityNumber > 0) {
          tempQuantity = quantityNumber - 1;
          mQuantityEditText.setText(tempQuantity + "");
          updateQuantityHandler();
        }
      }
    });

    deleteProduct.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deleteProductHandler();
      }
    });

    updateProduct.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        updateQuantityHandler();
      }
    });

    orderProduct.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:00000000"));
        startActivity(intent);
      }
    });

  }

  private void deleteProductHandler() {
    if (mCurrentProductUri != null) {
      int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
      if (rowsDeleted == 0) {
        Toast.makeText(this, getString(R.string.delete_product_failed),
            Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(this, getString(R.string.delete_product_successful),
            Toast.LENGTH_SHORT).show();
      }
    }

    finish();
  }

  private void updateQuantityHandler() {
    String nameString = mNameEditText.getText().toString().trim();
    String quantityString = mQuantityEditText.getText().toString().trim();
    String priceString = mPriceEditText.getText().toString().trim();
    int quantity = Integer.parseInt(quantityString);

    ContentValues values = new ContentValues();
    values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameString);
    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
    values.put(ProductEntry.COLUMN_PRODUCT_PRICE, priceString);

    int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);

    if (rowsAffected < 0) {
      Toast.makeText(this, getString(R.string.update_product_failed),
          Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(this, getString(R.string.update_product_successful),
          Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
    String[] projection = {
        ProductEntry.COLUMN_PRODUCT_ID,
        ProductEntry.COLUMN_PRODUCT_NAME,
        ProductEntry.COLUMN_PRODUCT_QUANTITY,
        ProductEntry.COLUMN_PRODUCT_PRICE};

    return new CursorLoader(this,
        mCurrentProductUri,
        projection,
        null,
        null,
        null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    if (cursor == null || cursor.getCount() < 1) {
      return;
    }

    if (cursor.moveToFirst()) {
      int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
      int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
      int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);

      String name = cursor.getString(nameColumnIndex);
      String quantity = cursor.getString(quantityColumnIndex);
      String price = cursor.getString(priceColumnIndex);

      mNameEditText.setText(name);
      mQuantityEditText.setText(quantity);
      mPriceEditText.setText(price);
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    mNameEditText.setText("");
    mQuantityEditText.setText("");
    mPriceEditText.setText("");
  }
}
