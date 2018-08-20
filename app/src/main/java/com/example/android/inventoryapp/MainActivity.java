package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;
import com.example.android.inventoryapp.data.ProductDbHelper;

public class MainActivity extends AppCompatActivity {

    private ProductDbHelper productDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productDbHelper = new ProductDbHelper(this);

        displayProductData();
    }

    /**
     * Inserts a row of fake data.
     */
    public void insertProductData(View v){
        SQLiteDatabase db = productDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Krill Oil Capsules");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 12);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 25);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME, "BioOptimal");
        values.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, "(123) 456-7890");

        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        if (newRowId != -1) {
            displayProductData();
        }
    }

    /**
     * Displays all columns and rows in the database.
     */
    private void displayProductData(){
        SQLiteDatabase db = productDbHelper.getReadableDatabase();

        Cursor cursor = db.query(ProductEntry.TABLE_NAME, null, null,
                null, null, null, null);

        TextView displayView = findViewById(R.id.product_data);

        String columnsHeader = ProductEntry.COLUMN_PRODUCT_NAME + " | " +
                ProductEntry.COLUMN_PRODUCT_PRICE + " | " +
                ProductEntry.COLUMN_PRODUCT_QUANTITY + " | " +
                ProductEntry.COLUMN_SUPPLIER_NAME + " | " +
                ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER + "\n";

        if (!displayView.getText().toString().contains(columnsHeader)){
            displayView.append(columnsHeader);
        }

        try {
            int productNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int productPriceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int productQuantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
            int supplierNumberColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            while (cursor.moveToNext()) {
                String currentProductName = cursor.getString(productNameColumnIndex);
                int currentProductPrice = cursor.getInt(productPriceColumnIndex);
                int currentProductQuantity = cursor.getInt(productQuantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierNumber = cursor.getString(supplierNumberColumnIndex);

                displayView.append(("\n" + currentProductName + " | " + currentProductPrice + " | " +
                        currentProductQuantity + " | " + currentSupplierName + " | " + currentSupplierNumber));
            }
        } finally {
            cursor.close();
        }
    }
}
