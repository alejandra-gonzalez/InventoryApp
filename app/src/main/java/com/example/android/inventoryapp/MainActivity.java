package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int PRODUCT_LIST_LOADER = 0;
    private ProductCursorAdapter productCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView productList = findViewById(R.id.product_list);
        productCursorAdapter = new ProductCursorAdapter(this, null);
        productList.setAdapter(productCursorAdapter);

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, l);
                intent.setData(currentProductUri);
                Log.i("MAINACTIVITY", "URI BEING PASSED = " + l);
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(PRODUCT_LIST_LOADER, null, this);
    }

    /**
     * Inserts a row of fake data.
     */
    public void insertProductData(){
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, getString(R.string.sample_product_name));
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 12);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 25);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, getString(R.string.sample_supplier_name));
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, getString(R.string.sample_supplier_number));

        getContentResolver().insert(ProductEntry.CONTENT_URI, values);
    }

    /**
     * Creates the loader if it does not exist already.
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        String[] projection = {ProductEntry._ID, ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE, ProductEntry.COLUMN_PRODUCT_QUANTITY};
        return new CursorLoader(this, ProductEntry.CONTENT_URI, projection, null,
                null, null);
    }

    /**
     * Uses the Cursor from the CursorLoader once the loader finishes its work.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        productCursorAdapter.swapCursor(cursor);
    }

    /**
     * Resets the CursorLoader.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        productCursorAdapter.swapCursor(null);
    }

    /**
     * Inflates the menu options from the res/menu/menu_main_activity.xml file
     * and adds them to the app bar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    /**
     * Performs an action based on what was selected from the app bar menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertProductData();
                return true;
            case R.id.action_insert_new_product:
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
