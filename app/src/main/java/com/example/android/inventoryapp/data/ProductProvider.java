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
     * Inserts a product into the database with the given content values. Returns the URI for the product inserted into the database.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    /**
     * Updates the data at the given URI using the new ContentValues. Returns the number of rows affected
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
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
