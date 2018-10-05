package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    // For use in sellProduct method
    private Context activityContext;

    /**
     * Constructs an instance of ProductCursorAdapter
     */
    public ProductCursorAdapter (Context context, Cursor c) {
        super(context, c, 0);
        activityContext = context;
    }

    /**
     * Creates a new view with no data bound to it yet
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * Binds a product's data to the view created by newView method
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = view.findViewById(R.id.product_name);
        TextView price = view.findViewById(R.id.product_price);
        TextView quantity = view.findViewById(R.id.product_quantity);
        Button sellProductButton = view.findViewById(R.id.sell_button);

        String productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
        String productPrice = cursor.getString(cursor.getColumnIndexOrThrow("price"));
        final String productQuantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));
        final int productId = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry._ID));

        name.setText(productName);
        price.setText(productPrice);
        quantity.setText(productQuantity);

        sellProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sellProduct(productId, Integer.parseInt(productQuantity));
            }
        });
    }

    /**
     * Decreases the quantity of an item.
     */
    private void sellProduct(int productId, int productQuantity) {
        if (productQuantity >= 1) {
            productQuantity--;
            Uri productUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, productId);
            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
            int rowsUpdated = activityContext.getContentResolver().update(
                    productUri,
                    values,
                    null,
                    null);
            if (rowsUpdated == 1) {
                Toast.makeText(activityContext, R.string.sold_successfully, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activityContext, R.string.sold_unsuccessfully, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activityContext, R.string.out_of_stock_error, Toast.LENGTH_SHORT).show();
        }
    }
}
