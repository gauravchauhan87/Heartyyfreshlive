package com.heartyy.heartyyfresh.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AccountChecker {

    private static String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\"
            + "@([\\w]+\\.)+[\\w]+[\\w]$";
    public static boolean show = false;
    public static boolean convertDone = false;

    public static boolean checkEmail(String email) {
        Boolean b = email.matches(EMAIL_REGEX);
        return b;

    }

    public static boolean checkNameLength(String name) {
        boolean valid = false;
        if (name.length() >= 2) {
            valid = true;
        } else {
            valid = false;
        }
        return valid;
    }

    public static boolean checkPasswordlength(String password) {
        boolean valid = false;
        if (password.length() >= 6) {
            valid = true;
        } else {
            valid = false;
        }
        return valid;
    }

    public static boolean checkPhoneNumber(String phone) {
        boolean valid = false;
        if (phone.length() == 10) {
            valid = true;
        } else {
            valid = false;
        }
        return valid;
    }

    public static boolean checkZip(String zip) {
        boolean valid = false;
        if (zip.length() == 5) {
            valid = true;
        } else {
            valid = false;
        }
        return valid;
    }

    public static boolean checkcardNo(String cardNo) {
        boolean valid = false;
        if (cardNo.length() >= 14) {
            valid = true;
        }
        return valid;
    }

    public static boolean checkCvv(String cvv, String cardBrand) {
        boolean valid = false;
        if ((cvv.length() == 3)
                && ((cardBrand.equalsIgnoreCase("DinersClub"))
                || (cardBrand.equalsIgnoreCase("visa"))
                || (cardBrand.equalsIgnoreCase("unknown")) || (cardBrand
                .equalsIgnoreCase("MasterCard")))) {
            valid = true;
        } else if (((cvv.length() == 3) || (cvv.length() == 4))
                && (cardBrand.equalsIgnoreCase("AmericanXpress"))) {
            valid = true;

        } else {
            valid = false;
        }
        return valid;
    }

    public static String getCardBrand(String cardNo) {
        String brand;
        if (cardNo.startsWith("30") || cardNo.startsWith("36")
                || cardNo.startsWith("38") || cardNo.startsWith("39")) {
            brand = "DinersClub";
        } else if (cardNo.startsWith("60") || cardNo.startsWith("62")
                || cardNo.startsWith("64") || cardNo.startsWith("65")) {
            brand = "Discover";
        } else if (cardNo.startsWith("35")) {
            brand = "JCB";
        } else if (cardNo.startsWith("34") || cardNo.startsWith("37")) {
            brand = "AmericanXpress";
        } else if (cardNo.startsWith("4")) {
            brand = "visa";
        } else if (cardNo.startsWith("5")) {
            brand = "masterCard";
        } else {
            brand = "unknown";
        }
        return brand;
    }

    public static boolean checkYear(String expiryYear) {
        boolean valid = false;
        if (expiryYear.length() != 0) {

            int year = Integer.parseInt(expiryYear);
            SimpleDateFormat sdf = new SimpleDateFormat("yy"); // Just the year, with 2 digits
            String formattedDate = sdf.format(Calendar.getInstance().getTime());

            int currentYear = Integer.parseInt(String.valueOf(formattedDate));
            if (year >= currentYear) {
                valid = true;
            }
        }
        return valid;
    }

    public static boolean checkMonth(String expiryMonth) {
        boolean valid = false;
        int month = Integer.parseInt(expiryMonth);
        if (month > 0 && month <= 12) {
            valid = true;
        }
        return valid;
    }

    public static boolean checkExpiryDate(String month, String year) {
        boolean valid = false;
        boolean validYear = checkYear(year);
        if (validYear) {
            DateFormat sdf = new SimpleDateFormat("MM"); // Just the month, with 2 digits
            Date date = new Date();
            String formattedDate = sdf.format(date);

            int currentMonth = Integer.parseInt(String.valueOf(formattedDate));

            SimpleDateFormat sdf1 = new SimpleDateFormat("yy"); // Just the month, with 2 digits
            String formattedDate1 = sdf1.format(Calendar.getInstance().getTime());
            int currentYear = Integer.parseInt(String.valueOf(formattedDate1));
            int mon = Integer.parseInt(month);
            int expyear = Integer.parseInt(year);

            if(currentYear==expyear){
                if (mon >= currentMonth) {
                    valid = true;
                }
            }else{
                valid=true;
            }


        }
        return valid;

    }

}
