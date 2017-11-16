package com.nanoddegree.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nanoddegree.inventoryapp.data.ProductContract.ProductEntry;

public class AddProductActivity extends AppCompatActivity {

  private EditText productName;
  private EditText productQuantity;
  private EditText productPrice;
  private Button addProduct;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_product);

    productName = (EditText) findViewById(R.id.product_name_input);
    productQuantity = (EditText) findViewById(R.id.product_quantity_input);
    productPrice = (EditText) findViewById(R.id.product_price_input);
    addProduct = (Button) findViewById(R.id.add);

    addProduct.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addProduct();
      }
    });

  }

  private void addProduct() {
    String nameString = productName.getText().toString().trim();
    String quantityString = productQuantity.getText().toString().trim();
    String priceString = productPrice.getText().toString().trim();
    int quantity = Integer.parseInt(quantityString);

    ContentValues values = new ContentValues();
    values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameString);
    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
    values.put(ProductEntry.COLUMN_PRODUCT_PRICE, priceString);


    Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

    if (newUri == null) {
      Toast.makeText(this, getString(R.string.insert_product_failed),
          Toast.LENGTH_SHORT).show();
    } else {
      Intent intent = new Intent(this, ProductActivity.class);
      startActivity(intent);
      Toast.makeText(this, getString(R.string.insert_product_successful),
          Toast.LENGTH_SHORT).show();
    }
  }
}
