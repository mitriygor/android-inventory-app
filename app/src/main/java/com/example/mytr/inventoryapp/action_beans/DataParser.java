package com.example.mytr.inventoryapp.action_beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import com.example.mytr.inventoryapp.data_beans.Inventory;
import com.example.mytr.inventoryapp.data_beans.Supplier;

public final class DataParser {

    public static String arrToString(String[] strArray) {
        return Arrays.toString(strArray);
    }

    public static JSONArray getJsonArray(String str) throws JSONException {
        return new JSONArray(str);
    }

    public static ArrayList<Inventory> parseItems(JSONArray jsonArray) throws JSONException {
        ArrayList<Inventory> itemArrayList = new ArrayList<>();
        int lengthJsonArr = jsonArray.length();
        for (int i = 0; i < lengthJsonArr; i++) {
            Inventory item = new Inventory();
            Supplier supplier = new Supplier();

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.has("title")) {
                item.setTitle(jsonObject.getString("title"));
            }

            if (jsonObject.has("supplier")) {
                supplier.setTitle(jsonObject.getString("supplier"));
            }

            if (jsonObject.has("supplier_email")) {
                supplier.setEmail(jsonObject.getString("supplier_email"));
            }

            if (jsonObject.has("image")) {
                item.setImage(jsonObject.getString("image"));
            }

            if (jsonObject.has("quantity")) {
                item.setQuantity(Integer.parseInt(jsonObject.getString("quantity")));
            }
            if (jsonObject.has("price")) {
                item.setPrice(Double.parseDouble(jsonObject.getString("price")));
            }
            item.setSupplier(supplier);
            itemArrayList.add(item);
        }
        return itemArrayList;
    }

    public static String formatPrice(Double price) {
        return NumberFormat.getCurrencyInstance().format(price);
    }
}