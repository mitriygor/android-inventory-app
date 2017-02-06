package com.example.mytr.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.widget.TextView;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import com.example.mytr.inventoryapp.action_beans.DataParser;
import com.example.mytr.inventoryapp.model.InventoryContract.InventoryEntry;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private int id;
    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentInventoryUri;
    private String email = "";
    private ImageView imageView;
    private TextView titleTextView;
    private TextView supplierTextView;
    private TextView quantityTextView;
    private TextView priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Button btnDelete = (Button) findViewById(R.id.btn_delete);
        Button minusBtn = (Button) findViewById(R.id.minus_btn);
        Button plusBtn = (Button) findViewById(R.id.plus_btn);
        Button btnOrderMore = (Button) findViewById(R.id.btn_order_more);

        imageView = (ImageView) findViewById(R.id.inventory_image);
        titleTextView = (TextView) findViewById(R.id.inventory_title);
        supplierTextView = (TextView) findViewById(R.id.inventory_supplier);
        quantityTextView = (TextView) findViewById(R.id.quantity);
        priceTextView = (TextView) findViewById(R.id.inventory_price);

        Intent intent = getIntent();
        mCurrentInventoryUri = intent.getData();


        getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });

//        btnRemoveOne.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int q = Integer.valueOf(quantityTextView.getText().toString());
//                if(q > 0) {
//                    int quanta = q - 1;
//                    Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
//                    saveInventory(DetailActivity.this, quanta, currentInventoryUri);
//                }
//            }
//        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q = Integer.valueOf(quantityTextView.getText().toString());
                if(q > 0) {
                    int quanta = q - 1;
                    Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                    saveInventory(DetailActivity.this, quanta, currentInventoryUri);
                }
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q = Integer.valueOf(quantityTextView.getText().toString());
                int quanta = q + 1;
                Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                saveInventory(DetailActivity.this, quanta, currentInventoryUri);
            }
        });

        btnOrderMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Order more inventories");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                Intent mailer = Intent.createChooser(intent, "Send mail");
                startActivity(mailer);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imagePreview = (ImageView) findViewById(R.id.image_preview);
                imagePreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVENTORY_TITLE,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL,
                InventoryEntry.COLUMN_INVENTORY_IMAGE,
                InventoryEntry.COLUMN_INVENTORY_QUANTITY,
                InventoryEntry.COLUMN_INVENTORY_PRICE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentInventoryUri,
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_TITLE);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER);
            int supplierEmailColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL);
            int imageColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_IMAGE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);

            id = cursor.getInt(idColumnIndex);
            email = cursor.getString(supplierEmailColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String image = cursor.getString(imageColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            double price = cursor.getDouble(priceColumnIndex);
            if (image.contains("content:")) {
                try {
                    Uri uri = Uri.parse(image);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                imageView.setImageResource(getResources().getIdentifier(image, "drawable", getPackageName()));
            }
            titleTextView.setText(title);
            supplierTextView.setText(supplier);
            quantityTextView.setText(String.valueOf(quantity));
            priceTextView.setText(DataParser.formatPrice(price));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void showDeleteConfirmationDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteInventory();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteInventory() {
        if (mCurrentInventoryUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentInventoryUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_inventory_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_inventory_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
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
