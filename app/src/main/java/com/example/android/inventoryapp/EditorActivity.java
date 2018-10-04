package com.example.android.inventoryapp;

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
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private EditText getProductName;
    private EditText getProductPrice;
    private EditText getProductQuantity;
    private EditText getSupplierName;
    private EditText getSupplierNumber;

    private static final int EDIT_PRODUCT_LOADER = 1;
    private Uri currentProductUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        getProductName = findViewById(R.id.product_name);
        getProductPrice = findViewById(R.id.product_price);
        getProductQuantity = findViewById(R.id.product_quantity);
        getSupplierName = findViewById(R.id.supplier_name);
        getSupplierNumber = findViewById(R.id.supplier_phone_number);

        Intent intent = getIntent();
        currentProductUri = intent.getData();

        if (currentProductUri == null) {
            setTitle(R.string.activity_title_add_new_product);
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.activity_title_edit_product);
            getSupportLoaderManager().initLoader(EDIT_PRODUCT_LOADER, null, this);
        }
    }

    /**
     * Checks to see if the product data entered is valid before
     * adding it to the database.
     */
    public void saveProduct(View v){
        String productName = getProductName.getText().toString().trim();
        String productPriceString = getProductPrice.getText().toString().trim();
        String productQuantityString = getProductQuantity.getText().toString().trim();
        String supplierName = getSupplierName.getText().toString().trim();
        String supplierNumber = getSupplierNumber.getText().toString().trim();
        int productQuantity = 0;

        if (TextUtils.isEmpty(productName)){
            Toast.makeText(this, R.string.no_product_name, Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(productPriceString)){
            Toast.makeText(this, R.string.no_product_price, Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(supplierName)) {
            Toast.makeText(this, R.string.no_supplier_name, Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(supplierNumber)){
            Toast.makeText(this, R.string.no_supplier_number, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(productQuantityString)){
            productQuantity = Integer.parseInt(productQuantityString);
            if (productQuantity < 0) {
                Toast.makeText(this, R.string.no_product_quantity, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        int productPrice = Integer.parseInt(productPriceString);
        if (productPrice < 0) {
            Toast.makeText(this, R.string.no_product_price, Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierNumber);

        Uri insertedProduct = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        if (insertedProduct == null) {
            Toast.makeText(this, R.string.saving_product_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.saving_product_success, Toast.LENGTH_SHORT).show();
            finish();
        }
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
        getProductName.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME)));
        getProductPrice.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE)));
        getProductQuantity.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY)));
        getSupplierName.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME)));
        getSupplierNumber.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER)));
    }

    /**
     * Reset the data bound to the EditTexts if the loader is reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        getProductName.setText("");
        getProductPrice.setText("");
        getProductQuantity.setText("");
        getSupplierName.setText("");
        getSupplierNumber.setText("");
    }
}
