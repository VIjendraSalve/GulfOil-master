package com.taraba.gulfoilapp.util;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class GulfValidator {

    public static boolean isValidEmail(String emailId) {
        return Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
    }

    public static boolean isEmpty(String value) {
        return TextUtils.isEmpty(value);
    }

    public static boolean isValidLength(String value, int length) {
        return value.length() == length;
    }

    public static boolean isValidSpinner(int position) {
        return position > 0;
    }

    public static boolean isEqual(String val1, String val2) {
        if (val1 == null || val2 == null)
            return false;
        return val1.equals(val2);
    }

    public static boolean isValidPanCardNumber(String panCardString) {
        return Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}").matcher(panCardString.toUpperCase()).matches();
    }

    public static boolean isValidAadharCardNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }

    static class VerhoeffAlgorithm {
        static int[][] d = new int[][]
                {
                        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                        {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
                        {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
                        {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
                        {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
                        {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
                        {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
                        {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
                        {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
                        {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
                };
        static int[][] p = new int[][]
                {
                        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                        {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
                        {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
                        {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
                        {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
                        {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
                        {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
                        {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
                };
        static int[] inv = {0, 4, 3, 2, 1, 5, 6, 7, 8, 9};

        public static boolean validateVerhoeff(String num) {
            int c = 0;
            int[] myArray = StringToReversedIntArray(num);
            for (int i = 0; i < myArray.length; i++) {
                c = d[c][p[(i % 8)][myArray[i]]];
            }

            return (c == 0);
        }

        private static int[] StringToReversedIntArray(String num) {
            int[] myArray = new int[num.length()];
            for (int i = 0; i < num.length(); i++) {
                myArray[i] = Integer.parseInt(num.substring(i, i + 1));
            }
            myArray = Reverse(myArray);
            return myArray;
        }

        private static int[] Reverse(int[] myArray) {
            int[] reversed = new int[myArray.length];
            for (int i = 0; i < myArray.length; i++) {
                reversed[i] = myArray[myArray.length - (i + 1)];
            }
            return reversed;
        }
    }

    public static boolean isNumber(String str) {
        return TextUtils.isDigitsOnly(str);
    }
}
