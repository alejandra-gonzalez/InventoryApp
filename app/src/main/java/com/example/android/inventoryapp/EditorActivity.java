package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class EditorActivity extends AppCompatActivity {

    private EditText getProductName;
    private EditText getProductPrice;
    private EditText getProductQuantity;
    private EditText getSupplierName;
    private EditText getSupplierNumber;


    private long productId = -1;
    private String productName;
    private String productPrice;
    private String productQuantity;
    private String supplierName;
    private String supplierNumber;

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
        Bundle productData = intent.getExtras();

        if (productData == null) {
            setTitle(R.string.activity_title_add_new_product);
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.activity_title_edit_product);
            productId = productData.getLong(ProductEntry._ID);
            productName = productData.getString(ProductEntry.COLUMN_PRODUCT_NAME);
            productPrice = productData.getString(ProductEntry.COLUMN_PRODUCT_PRICE);
            productQuantity = productData.getString(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            supplierName = productData.getString(ProductEntry.COLUMN_SUPPLIER_NAME);
            supplierNumber = productData.getString(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            getProductName.setText(productName);
            getProductPrice.setText(productPrice);
            getProductQuantity.setText(productQuantity);
            getSupplierName.setText(supplierName);
            getSupplierNumber.setText(supplierNumber);
        }
    }

    /**
     * Checks to see if the product data entered is valid before
     * adding it to the database.
     */
    public void saveProduct(View v){
        productName = getProductName.getText().toString().trim();
        String productPriceString = getProductPrice.getText().toString().trim();
        String productQuantityString = getProductQuantity.getText().toString().trim();
        supplierName = getSupplierName.getText().toString().trim();
        supplierNumber = getSupplierNumber.getText().toString().trim();
        int productQuantityInt = 0;


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
            productQuantityInt = Integer.parseInt(productQuantityString);
            if (productQuantityInt < 0) {
                Toast.makeText(this, R.string.no_product_quantity, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        int productPriceInt = Integer.parseInt(productPriceString);
        if (productPriceInt < 0) {
            Toast.makeText(this, R.string.no_product_price, Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPriceInt);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantityInt);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierNumber);

        if (productId == -1) {
            Uri insertedProduct = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

            if (insertedProduct == null) {
                Toast.makeText(this, R.string.saving_product_error, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.saving_product_success, Toast.LENGTH_SHORT).show();
                returnData();
                finish();
            }
        } else {
            Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, productId);

            int updatedRows = getContentResolver().update(currentProductUri, values, null, null);

            if (updatedRows == 0) {
                Toast.makeText(this, R.string.updating_product_error, Toast.LENGTH_SHORT).show();
            } else {
                productQuantity = String.valueOf(productQuantityInt);
                productPrice = String.valueOf(productPrice);
                Toast.makeText(this, R.string.updating_product_success, Toast.LENGTH_SHORT).show();
                returnData();
                finish();
            }
        }
    }

    /**
     * Returns data to ViewActivity via Intent.
     */
    private void returnData(){
        Intent productData = new Intent();
        productData.putExtra(ProductEntry._ID, productId);
        productData.putExtra(ProductEntry.COLUMN_PRODUCT_NAME, productName);
        productData.putExtra(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
        productData.putExtra(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        productData.putExtra(ProductEntry.COLUMN_SUPPLIER_NAME, supplierName);
        productData.putExtra(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierNumber);
        setResult(RESULT_OK, productData);
    }

    /**
     * Sends back data to ViewActivity when up button is pressed
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnData();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
