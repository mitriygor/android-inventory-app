package com.example.mytr.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;

import com.example.mytr.inventoryapp.action_beans.Validator;
import com.example.mytr.inventoryapp.data_beans.AlertMessage;
import com.example.mytr.inventoryapp.data_beans.Inventory;
import com.example.mytr.inventoryapp.action_beans.Alerter;
import com.example.mytr.inventoryapp.data_beans.Supplier;
import com.example.mytr.inventoryapp.model.InventoryContract.InventoryEntry;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private EditText editInventoryTitle;
    private EditText editInventorySupplier;
    private EditText editInventorySupplierEmail;
    private ImageView imagePreview;
    private EditText editInventoryQuantity;
    private EditText editInventoryPrice;

    private String imageUri = "";
    private final boolean mInventoryHasChanged = false;
    private Uri mCurrentInventoryUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Button btnSelectImage = (Button) findViewById(R.id.btn_select_image);
        editInventoryTitle = (EditText) findViewById(R.id.edit_inventory_title);
        editInventorySupplier = (EditText) findViewById(R.id.edit_inventory_supplier);
        editInventorySupplierEmail = (EditText) findViewById(R.id.edit_inventory_supplier_email);
        imagePreview = (ImageView) findViewById(R.id.image_preview);
        editInventoryQuantity = (EditText) findViewById(R.id.edit_inventory_quantity);
        editInventoryPrice = (EditText) findViewById(R.id.edit_inventory_price);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
                startActivityForResult(chooserIntent, 1);
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
                imagePreview.setImageBitmap(bitmap);
                imageUri = uri.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createInventory() {

        String title = editInventoryTitle.getText().toString().trim();
        String supplierTitle = editInventorySupplier.getText().toString().trim();
        String supplierEmail = editInventorySupplierEmail.getText().toString().trim();
        String image = imageUri;
        String quantity = editInventoryQuantity.getText().toString().trim();
        String price = editInventoryPrice.getText().toString().trim();

        if (!Validator.isTitleValid(title)) {
            AlertMessage alertMessage = new AlertMessage("Inventory Title Error", "Inventory title is not valid");
            Alerter.showAlert(EditorActivity.this, alertMessage);
        } else if (!Validator.isSupplierValid(supplierTitle)) {
            AlertMessage alertMessage = new AlertMessage("Supplier Error", "Supplier name is not valid");
            Alerter.showAlert(EditorActivity.this, alertMessage);
        } else if (!Validator.isSupplierEmailValid(supplierEmail)) {
            AlertMessage alertMessage = new AlertMessage("Supplier Email Error", "Supplier email is not valid");
            Alerter.showAlert(EditorActivity.this, alertMessage);
        } else if (!Validator.isQuantityValid(quantity)) {
            AlertMessage alertMessage = new AlertMessage("Quantity Error", "Quantity is not valid");
            Alerter.showAlert(EditorActivity.this, alertMessage);
        } else if (!Validator.isPriceValid(price)) {
            AlertMessage alertMessage = new AlertMessage("Price Error", "Priceis not valid");
            Alerter.showAlert(EditorActivity.this, alertMessage);
        } else if (!Validator.isImageValid(image)) {
            AlertMessage alertMessage = new AlertMessage("Image Error", "Image is not valid");
            Alerter.showAlert(EditorActivity.this, alertMessage);
        } else {
            Supplier supplier = new Supplier(supplierTitle, supplierEmail);
            Inventory inventory = new Inventory(title, supplier, image, Integer.parseInt(quantity), Double.parseDouble(price));
            saveInventory(inventory);
        }
    }

    private void saveInventory(Inventory inventory) {
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_INVENTORY_TITLE, inventory.getTitle());
        values.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER, inventory.getSupplier().getTitle());
        values.put(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL, inventory.getSupplier().getEmail());
        values.put(InventoryEntry.COLUMN_INVENTORY_IMAGE, inventory.getImage());
        values.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, inventory.getQuantity());
        values.put(InventoryEntry.COLUMN_INVENTORY_PRICE, inventory.getPrice());

        if (mCurrentInventoryUri == null) {
            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_inventory_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_inventory_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentInventoryUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_inventory_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_inventory_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.btn_save:
                createInventory();
                return true;
            case android.R.id.home:
                if (!mInventoryHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        if (!mInventoryHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVENTORY_TITLE,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER,
                InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL,
                InventoryEntry.COLUMN_INVENTORY_QUANTITY,
                InventoryEntry.COLUMN_INVENTORY_PRICE,
                InventoryEntry.COLUMN_INVENTORY_IMAGE};

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
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_TITLE);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER);
            int supplierEmailColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER_EMAIL);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);
            int imageColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_IMAGE);

            // Extract out the value from the Cursor for the given column index
            String title = cursor.getString(titleColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String supplierEmail = cursor.getString(supplierEmailColumnIndex);
            String image = cursor.getString(imageColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            double price = cursor.getDouble(priceColumnIndex);

            // Update the views on the screen with the values from the database
            editInventoryTitle.setText(title);
            editInventorySupplier.setText(supplier);
            editInventorySupplierEmail.setText(supplierEmail);
            editInventorySupplierEmail.setText(Integer.toString(quantityColumnIndex));
            editInventoryQuantity.setText(Integer.toString(quantity));
            editInventoryPrice.setText(Double.toString(price));
            if (image.contains("content:")) {
                try {
                    Uri uri = Uri.parse(image);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imagePreview.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                imagePreview.setImageResource(getResources().getIdentifier(image, "drawable", getPackageName()));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        editInventoryTitle.setText("");
        editInventorySupplier.setText("");
        editInventorySupplierEmail.setText("");
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
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

}
