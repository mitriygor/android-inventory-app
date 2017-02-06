package com.example.mytr.inventoryapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.content.ContentUris;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mytr.inventoryapp.action_beans.DataParser;
import com.example.mytr.inventoryapp.model.InventoryContract.InventoryEntry;

/**
 * {@link InventoryCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of inventory data as its data source. This adapter knows
 * how to create list items for each row of inventory data in the {@link Cursor}.
 */
class InventoryCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link InventoryCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the inventory data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current inventory can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Button minusBtn = (Button) view.findViewById(R.id.minus_btn);
        Button plusBtn = (Button) view.findViewById(R.id.plus_btn);
        RelativeLayout clickable = (RelativeLayout) view.findViewById(R.id.clickable);
        RelativeLayout clickableBottom = (RelativeLayout) view.findViewById(R.id.clickable_bottom);

        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        TextView supplierTextView = (TextView) view.findViewById(R.id.supplier);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);

        int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
        int titleColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_TITLE);
        int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);

        final Long id = cursor.getLong(idColumnIndex);
        final int q = cursor.getInt(quantityColumnIndex);
        String inventoryTitle = cursor.getString(titleColumnIndex);
        String inventorySupplier = cursor.getString(supplierColumnIndex);
        String inventoryQuantity = String.valueOf(q);
        String inventoryPrice = DataParser.formatPrice(cursor.getDouble(priceColumnIndex));

        titleTextView.setText(inventoryTitle);
        supplierTextView.setText(inventorySupplier);
        quantityTextView.setText(inventoryQuantity);
        priceTextView.setText(inventoryPrice);

        final Context mainContext = context;
        clickable.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainContext, DetailActivity.class);
                Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                intent.setData(currentInventoryUri);
                mainContext.startActivity(intent);
            }
        });

        clickableBottom.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainContext, DetailActivity.class);
                Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                intent.setData(currentInventoryUri);
                mainContext.startActivity(intent);
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(q > 0) {
                    int quanta = q - 1;
                    Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                    saveInventory(mainContext, quanta, currentInventoryUri);
                }
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    int quanta = q + 1;
                    Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                    saveInventory(mainContext, quanta, currentInventoryUri);
            }
        });
    }

    private void saveInventory(Context context, int quantity, Uri uri) {

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, quantity);

            int rowsAffected = context.getContentResolver().update(uri, values, null, null);

            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(context, "Quantity has been updated", Toast.LENGTH_SHORT).show();
            }
    }
}
