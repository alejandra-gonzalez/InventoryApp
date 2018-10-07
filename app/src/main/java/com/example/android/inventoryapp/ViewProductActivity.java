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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class ViewProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private TextView productNameView;
    private TextView productPriceView;
    private TextView productQuantityView;
    private TextView supplierNameView;
    private TextView supplierNumberView;

    private Button increment;
    private Button decrement;

    private long productId;
    private String productName;
    private String productPrice;
    private String productQuantity;
    private String supplierName;
    private String supplierNumber;

    private static final int EDIT_PRODUCT_LOADER = 1;
    private Uri currentProductUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        productNameView = findViewById(R.id.product_name);
        productPriceView = findViewById(R.id.product_price);
        productQuantityView = findViewById(R.id.product_quantity);
        supplierNameView = findViewById(R.id.supplier_name);
        supplierNumberView = findViewById(R.id.supplier_number);

        increment = findViewById(R.id.increment_quantity);
        decrement = findViewById(R.id.decrement_quantity);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            currentProductUri = intent.getData();
            getSupportLoaderManager().initLoader(EDIT_PRODUCT_LOADER, null, this);
        } else {
            productId = savedInstanceState.getLong(ProductEntry._ID);
            productName = savedInstanceState.getString(ProductEntry.COLUMN_PRODUCT_NAME);
            productPrice = savedInstanceState.getString(ProductEntry.COLUMN_PRODUCT_PRICE);
            productQuantity = savedInstanceState.getString(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            supplierName = savedInstanceState.getString(ProductEntry.COLUMN_SUPPLIER_NAME);
            supplierNumber = savedInstanceState.getString(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
            currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, productId);

            bindData();
        }

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productQuantity = String.valueOf(Integer.parseInt(productQuantity) + 1);
                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
                int rowsUpdated = getContentResolver().update(
                        currentProductUri,
                        values,
                        null,
                        null);
                if (rowsUpdated == 1) {
                    productQuantityView.setText(productQuantity);
                } else {
                    Toast.makeText(ViewProductActivity.this, R.string.increase_quantity_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(productQuantity) >= 1) {
                    productQuantity = String.valueOf(Integer.parseInt(productQuantity) - 1);
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
                    int rowsUpdated = getContentResolver().update(
                            currentProductUri,
                            values,
                            null,
                            null);
                    if (rowsUpdated == 1) {
                        productQuantityView.setText(productQuantity);
                    } else {
                        Toast.makeText(ViewProductActivity.this, R.string.decrease_quantity_error, Toast.LENGTH_SHORT).show();
                    }
                }

                if (Integer.parseInt(productQuantity) == 0) {
                    Toast.makeText(ViewProductActivity.this, R.string.out_of_stock_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Inflates the menu options from the res/menu/menu_view_activity.xml file
     * and adds them to the app bar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_activity, menu);
        return true;
    }

    /**
     * Performs an action based on what was selected from the app bar menu.
     * Reference for startActivity for result: https://stackoverflow.com/a/14292451, accessed Oct 6, 2018
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_product:
                Intent intent = new Intent(ViewProductActivity.this, EditorActivity.class);
                intent.putExtra(ProductEntry._ID, productId);
                intent.putExtra(ProductEntry.COLUMN_PRODUCT_NAME, productName);
                intent.putExtra(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
                intent.putExtra(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
                intent.putExtra(ProductEntry.COLUMN_SUPPLIER_NAME, supplierName);
                intent.putExtra(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierNumber);
                Log.i("VIEWPRODUCTACTIVITY", "PRODUCT ID BEING PASSED = " + productId);
                startActivityForResult(intent, 101);
                return true;
            case R.id.action_delete_product:
                //for now just delete the product
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates the loader to show product details if it was not created already.
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, currentProductUri, null, null,
                null, null);
    }

    /**
     * After the loader is finished, bind current product data to its corresponding EditText.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1){
            return;
        }

        data.moveToFirst();

        productId = data.getLong(data.getColumnIndex(ProductEntry._ID));
        productName = data.getString(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
        productPrice = data.getString(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
        productQuantity = data.getString(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        supplierName = data.getString(data.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME));
        supplierNumber = data.getString(data.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER));

        bindData();
    }

    /**
     * Reset the data bound to the EditTexts if the loader is reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        productNameView.setText("");
        productPriceView.setText("");
        productQuantityView.setText("");
        supplierNameView.setText("");
        supplierNumberView.setText("");
    }

    /**
     * Restores data when orientation changes
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        productId = savedInstanceState.getLong(ProductEntry._ID);
        productName = savedInstanceState.getString(ProductEntry.COLUMN_PRODUCT_NAME);
        productPrice = savedInstanceState.getString(ProductEntry.COLUMN_PRODUCT_PRICE);
        productQuantity = savedInstanceState.getString(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        supplierName = savedInstanceState.getString(ProductEntry.COLUMN_SUPPLIER_NAME);
        supplierNumber = savedInstanceState.getString(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

        bindData();
    }

    /**
     * Saves data in case of orientation change
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ProductEntry._ID, productId);
        outState.putString(ProductEntry.COLUMN_PRODUCT_NAME, productName);
        outState.putString(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
        outState.putString(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        outState.putString(ProductEntry.COLUMN_SUPPLIER_NAME, supplierName);
        outState.putString(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierNumber);
    }

    /**
     * Populates TextViews with product data
     */
    private void bindData(){
        productNameView.setText(productName);
        productPriceView.setText(productPrice);
        productQuantityView.setText(productQuantity);
        supplierNameView.setText(supplierName);
        supplierNumberView.setText(supplierNumber);
    }

    /**
     * Loads data from EditorActivity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                productId = data.getLongExtra(ProductEntry._ID, -1);
                productName = data.getStringExtra(ProductEntry.COLUMN_PRODUCT_NAME);
                productPrice = data.getStringExtra(ProductEntry.COLUMN_PRODUCT_PRICE);
                productQuantity = data.getStringExtra(ProductEntry.COLUMN_PRODUCT_QUANTITY);
                supplierName = data.getStringExtra(ProductEntry.COLUMN_SUPPLIER_NAME);
                supplierNumber = data.getStringExtra(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

                bindData();
            }
        }
    }
}