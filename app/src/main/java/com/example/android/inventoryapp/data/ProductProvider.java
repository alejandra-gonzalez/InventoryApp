package com.example.android.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class ProductProvider extends ContentProvider {

    /**
     * Tag for the log messages
     **/
    public static final String LOG_TAG = ProductProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the inventory table
     */
    private static final int INVENTORY = 200;

    /**
     * URI matcher code for the content URI for a single product in the inventory table
     */
    private static final int PRODUCT_ID = 201;
    /**
     * UriMatcher object to match a content URI to a corresponding code
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static ProductDbHelper productDbHelper;

    static {
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_INVENTORY, INVENTORY);
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_INVENTORY + "/#", PRODUCT_ID);
    }

    /**
     * Initialize the database helper object
     */
    @Override
    public boolean onCreate() {
        productDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI using the given projection, selection, selection arguments, and sort order
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = productDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Delete the data at the given URI. Returns the number of rows affected
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    /**
     * Public method to insert a product into the database with the given content values.
     * Returns the URI for the product inserted into the database.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Checks that all required data not null before
     * inserting a product into the database with the given content values.
     * Returns the URI for the product inserted into the database.
     */
    private Uri insertProduct(Uri uri, ContentValues values) {
        String product_name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (product_name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }

        int product_price = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
        if (product_price < 0) {
            throw new IllegalArgumentException("Product requires a valid price.");
        }

        int product_quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        if (product_quantity < 0) {
            throw new IllegalArgumentException("Product requires a valid quantity.");
        }

        String supplier_name = values.getAsString(ProductEntry.COLUMN_SUPPLIER_NAME);
        if (supplier_name == null) {
            throw new IllegalArgumentException("Product requires a supplier name");
        }

        String supplier_number = values.getAsString(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        if (supplier_number == null) {
            throw new IllegalArgumentException("Product requires a supplier phone number");
        }

        SQLiteDatabase database = productDbHelper.getWritableDatabase();
        long id = database.insert(ProductEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Public method to update the data at the given URI using the new ContentValues.
     * Returns the number of rows affected
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String
            selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Checks if there is anything to update, and if so, checks that all given data is valid and not null before
     * updating a specific product using the given content values.
     * Returns the number of rows affected
     */
    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) {
            String product_name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
            if (product_name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE)){
            int product_price = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
            if (product_price < 0) {
                throw new IllegalArgumentException("Product requires a valid price");
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY)) {
            int product_quantity = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            if (product_quantity < 0) {
                throw new IllegalArgumentException("Product requires a valid quantity");
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_SUPPLIER_NAME)) {
            String supplier_name = values.getAsString(ProductEntry.COLUMN_SUPPLIER_NAME);
            if (supplier_name == null) {
                throw new IllegalArgumentException("Product requires a supplier name");
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER)) {
            String supplier_number = values.getAsString(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
            if (supplier_number == null) {
                throw new IllegalArgumentException("Product requires a supplier phone number");
            }
        }

        SQLiteDatabase database = productDbHelper.getWritableDatabase();
        int updatedRows = database.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);
        if (updatedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRows;
    }

    /**
     * Returns the MIME type of data for the content URI
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
