package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class ProductDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";

    // Note to self: If you change the schema, update DATABASE_VERSION.
    private static final int DATABASE_VERSION = 1;

    /**
     * Creates a new instance of ProductDbHelper.
     */
    public ProductDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates the database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_INVENTORY_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + "(" +
                ProductEntry.COLUMN_PRODUCT_NAME + " TEXT PRIMARY KEY, " +
                ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER," +
                ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " +
                ProductEntry.COLUMN_SUPPLIER_NAME + " TEXT," +
                ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT" +")";
        sqLiteDatabase.execSQL(CREATE_INVENTORY_TABLE);
    }

    /**
     * This method updates the database when the database version number changes.
     * NOTE: I don't agree with dropping and re-creating the table, but I'm not sure what else to put here.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String DROP_INVENTORY_TABLE = "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_INVENTORY_TABLE);
        onCreate(sqLiteDatabase);
    }
}
