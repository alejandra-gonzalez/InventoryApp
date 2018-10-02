package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Constructs an instance of ProductCursorAdapter
     */
    public ProductCursorAdapter (Context context, Cursor c) {
        super(context, c, 0);
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

        String productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
        String productPrice = cursor.getString(cursor.getColumnIndexOrThrow("price"));
        String productQuantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));

        name.setText(productName);
        price.setText(productPrice);
        quantity.setText(productQuantity);
    }
}
