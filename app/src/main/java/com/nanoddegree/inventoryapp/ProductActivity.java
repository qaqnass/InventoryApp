package com.nanoddegree.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nanoddegree.inventoryapp.data.ProductContract.ProductEntry;

public class ProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  private final int PRODUCT_LOADER = 0;
  ProductCursorAdapter mCursorAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product);

    FloatingActionButton addProduct = (FloatingActionButton) findViewById(R.id.add_product);

    addProduct.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
        startActivity(intent);
      }
    });

    ListView productListView = (ListView) findViewById(R.id.list);

    View emptyView = findViewById(R.id.empty_view);
    productListView.setEmptyView(emptyView);

    mCursorAdapter = new ProductCursorAdapter(this, null);
    productListView.setAdapter(mCursorAdapter);

    productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(ProductActivity.this, DetailProductActivity.class);
        Uri currentUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
        intent.setData(currentUri);
        startActivity(intent);
      }
    });

    getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
  }


  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
    String[] projection = {
        ProductEntry.COLUMN_PRODUCT_ID,
        ProductEntry.COLUMN_PRODUCT_NAME,
        ProductEntry.COLUMN_PRODUCT_QUANTITY,
        ProductEntry.COLUMN_PRODUCT_PRICE};

    return new CursorLoader(this,
        ProductEntry.CONTENT_URI,
        projection,
        null,
        null,
        null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mCursorAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    mCursorAdapter.swapCursor(null);
  }
}
