package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        getProductName = findViewById(R.id.product_name);
        getProductPrice = findViewById(R.id.product_price);
        getProductQuantity = findViewById(R.id.product_quantity);
        getSupplierName = findViewById(R.id.supplier_name);
        getSupplierNumber = findViewById(R.id.supplier_phone_number);
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
}
