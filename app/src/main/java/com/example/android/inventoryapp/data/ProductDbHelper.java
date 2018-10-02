package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class ProductDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";

    // Note to self: If you change the schema, update DATABASE_VERSION and add to onUpgrade.
    private static final int DATABASE_VERSION = 2;

    private String CREATE_INVENTORY_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + "(" +
            ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ProductEntry.COLUMN_PRODUCT_NAME + " TEXT, " +
            ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER," +
            ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " +
            ProductEntry.COLUMN_SUPPLIER_NAME + " TEXT," +
            ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT" +")";

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
        sqLiteDatabase.execSQL(CREATE_INVENTORY_TABLE);
    }

    /**
     * This method updates the database when the database version number changes.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // In DB Version 1, there was no ID column and the product name was the primary key.
        // Since SQLite ALTER TABLE only allows renaming a column or a table and adding a new column,
        // there is no easy way to drop the PRIMARY KEY constraint from the product_name column of the table.
        // Reference: https://stackoverflow.com/questions/16900552/change-the-primary-key-of-a-table-in-sqlite
        // Accessed October 1, 2017

        if (oldVersion == 1) {
            // Rename old table.
            sqLiteDatabase.execSQL("ALTER TABLE " +ProductEntry.TABLE_NAME + " RENAME TO table2");
            // Create new table with desired constraints.
            sqLiteDatabase.execSQL(CREATE_INVENTORY_TABLE);
            // Transfer data from old table to new table.
            sqLiteDatabase.execSQL("INSERT INTO " + ProductEntry.TABLE_NAME + " (" + ProductEntry.COLUMN_PRODUCT_NAME
            + ", " + ProductEntry.COLUMN_PRODUCT_PRICE + ", " + ProductEntry.COLUMN_PRODUCT_QUANTITY + ", "
            + ProductEntry.COLUMN_SUPPLIER_NAME + ", " +ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER
                    + ") SELECT * FROM table2" );
            // Drop the old table.
            sqLiteDatabase.execSQL("DROP TABLE table2");
        }
    }
}
