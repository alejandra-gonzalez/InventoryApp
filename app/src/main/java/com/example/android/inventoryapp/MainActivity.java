package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;
import com.example.android.inventoryapp.data.ProductDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int PRODUCT_LIST_LOADER = 0;
    private ProductCursorAdapter productCursorAdapter;

    private ProductDbHelper productDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView productList = findViewById(R.id.product_list);
        productCursorAdapter = new ProductCursorAdapter(this, null);
        productList.setAdapter(productCursorAdapter);

        productDbHelper = new ProductDbHelper(this);

        getSupportLoaderManager().initLoader(PRODUCT_LIST_LOADER, null, this);
    }

    /**
     * Inserts a row of fake data.
     */
    public void insertProductData(View v){
        SQLiteDatabase db = productDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, getString(R.string.sample_product_name));
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 12);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 25);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, getString(R.string.sample_supplier_name));
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, getString(R.string.sample_supplier_number));

        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(getApplicationContext(),getString(R.string.display_error_message),
                    Toast.LENGTH_SHORT).show();
        }

        productCursorAdapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        String[] projection = {ProductEntry._ID, ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE, ProductEntry.COLUMN_PRODUCT_QUANTITY};
        return new CursorLoader(this, ProductEntry.CONTENT_URI, projection, null,
                null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        productCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        productCursorAdapter.swapCursor(null);
    }
}
