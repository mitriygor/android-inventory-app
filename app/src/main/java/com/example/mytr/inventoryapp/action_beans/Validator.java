package com.example.mytr.inventoryapp.action_beans;

public class Validator {

    public static boolean isTitleValid(String title) {
        boolean isValid = true;
        if (title == null || title.isEmpty()) {
            isValid = false;
        }
        return isValid;
    }

    public static boolean isSupplierValid(String supplier) {
        boolean isValid = true;
        if (supplier == null || supplier.isEmpty()) {
            isValid = false;
        }
        return isValid;
    }

    public static boolean isSupplierEmailValid(String email) {
        boolean isValid = true;
        if (email == null || email.isEmpty()) {
            isValid = false;
        }
        return isValid;
    }


    public static boolean isQuantityValid(String quantity) {
        boolean isValid = true;
        if (quantity == null || quantity.isEmpty()) {
            isValid = false;
        } else {
            try {
                int intQuantity = Integer.valueOf(quantity);
                if(intQuantity < 0) {
                    isValid = false;
                }
            } catch (NumberFormatException nfe) {
                isValid = false;
            }
        }
        return isValid;
    }

    public static boolean isPriceValid(String price) {
        boolean isValid = true;
        if (price == null || price.isEmpty()) {
            isValid = false;
        }  else {
            try {
                double doublePrice = Double.valueOf(price);
                if(doublePrice < 0) {
                    isValid = false;
                }
            } catch (NumberFormatException nfe) {
                isValid = false;
            }
        }
        return isValid;
    }

    public static boolean isImageValid(String imagePath) {
        boolean isValid = true;
        if (imagePath == null || imagePath.isEmpty()) {
            isValid = false;
        }
        return isValid;
    }


}
