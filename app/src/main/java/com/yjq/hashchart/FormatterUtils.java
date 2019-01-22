/*
 * Created by wulin on 18-4-13 上午11:47.
 * Copyright (c) 2018 Blockin. All Rights Reserved.
 * Last modified 18-4-13 上午11:47.
 */

package com.yjq.hashchart;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatterUtils {

    public static final String DATE_FORMAT_1 = "yyyy/MM/dd HH:mm";
    public static final String DATE_FORMAT_2 = "HH:mm";
    public static final String DATE_FORMAT_3 = "MM/dd";
    public static final String DATE_FORMAT_4 = "yyyyMMdd";
    public static final String DATE_FORMAT_5 = "yyyy-MM-dd";
    public static final String DATE_FORMAT_6 = "MM-dd";
    public static final String DATE_FORMAT_7 = "yyyyMMdd HHmmss";

    public static final DecimalFormat df_0 = new DecimalFormat("0");
    public static final DecimalFormat df_2 = new DecimalFormat("0.00");
    public static final DecimalFormat df_3 = new DecimalFormat("0.000");
    public static final DecimalFormat df_6 = new DecimalFormat("0.000000");
    public static final DecimalFormat df_8 = new DecimalFormat("0.00000000");

    public static String df0(double num) {
        return df_0.format(num);
    }

    public static String df8(double num) {
        return df_8.format(num);
    }

    public static String df2(double num) {
        df_2.setRoundingMode(RoundingMode.HALF_UP);
        return df_2.format(num);
    }

    public static String df6(double num) {
        return df_6.format(num);
    }

    public static String df3(double num) {
        return df_3.format(num);
    }

    public static String second2min(String number) {
        try {
            double s = Double.parseDouble(number) / 60;
            return df2(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String transferDateFormat(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_4);
        try {
            Date date = sdf.parse(dateString);
            SimpleDateFormat sdfNew = new SimpleDateFormat(DATE_FORMAT_5);
            return sdfNew.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }


    }

    public static String formatDateNO1(long seconds) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_1);
        Date dt = new Date(seconds * 1000);

        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    public static String formatDateNO2(long seconds) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_2);
        Date dt = new Date(seconds * 1000);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    public static String formatDateNO3(long seconds) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_3);
        Date dt = new Date(seconds * 1000);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    public static String formatDateNO4(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_4);
        return sdf.format(date);
    }

    public static String formatDateNO5(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_5);
        return sdf.format(date);
    }

    public static String formatDateNO6(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_6);
        return sdf.format(date);
    }

    public static String formatDateNO7(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_7);
        return sdf.format(date);
    }

    public static String formatPhone(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return phone;
    }

    public static String formatEmail(String email) {
        if (!TextUtils.isEmpty(email)) {
            return email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
        }
        return email;
    }

    public static String formatBigDouble(double value) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(30);
        return nf.format(value);
    }

    public static String formatBigDouble(double value, int digit) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(digit);
        return nf.format(value);
    }

    public static String formatDiffculty(double value) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);
        nf.setMaximumFractionDigits(0);
        return nf.format(value);
    }


    public static BigNum formatBigNum(double value) {
        BigNum b = new BigNum();
        b.source = value;

        if (value < Math.pow(10, 3)) {
            b.num = value;
        } else if (value < Math.pow(10, 6)) {
            b.num = value / Math.pow(10, 3);
            b.unit = "K";
        } else if (value < Math.pow(10, 9)) {
            b.num = value / Math.pow(10, 6);
            b.unit = "M";
        } else if (value < Math.pow(10, 12)) {
            b.num = value / Math.pow(10, 9);
            b.unit = "G";
        } else if (value < Math.pow(10, 15)) {
            b.num = value / Math.pow(10, 12);
            b.unit = "T";
        } else if (value < Math.pow(10, 18)) {
            b.num = value / Math.pow(10, 15);
            b.unit = "P";
        } else {
            b.num = value / Math.pow(10, 18);
            b.unit = "E";
        }

        return b;
    }

    public static String multiply(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String divider(BigInteger b1, BigInteger b2) {
        if (b1 == null || b2 == null) {
            return "";
        }
        BigDecimal bb1 = new BigDecimal(b1);
        BigDecimal bb2 = new BigDecimal(b2);
        return bb1.divide(bb2).setScale(6, BigDecimal.ROUND_HALF_UP).toString();
    }


    public static class BigNum {
        public double source;
        public double num;
        public String unit = "";

        public String df2() {
            return FormatterUtils.df2(num);
        }
    }

}
