package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

public final class ProductContract {

    /**
     * Suppress the constructor, since this class should not be instantiated.
     */
    private ProductContract(){}

    /**
     * Inner class for Inventory table.
     */
    public static final class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "inventory";

        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
    }
}