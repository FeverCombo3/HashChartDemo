/*
 * Created by wulin on 18-4-19 上午9:43.
 * Copyright (c) 2018 Blockin. All Rights Reserved.
 * Last modified 18-4-19 上午9:43.
 */

package com.yjq.hashchart;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;

public class CoinUtils {

    public static String rateUnit(String coin, String unitPrefix) {
        if ("zec".equalsIgnoreCase(coin)) {
            return unitPrefix + "Sol/s";
        } else {
            return unitPrefix + "H/s";
        }
    }

    public static String subsidyCoinType(String mainCoinType){
        if ("ltc".equalsIgnoreCase(mainCoinType)){
            return "DOGE";
        }else if ("btc".equalsIgnoreCase(mainCoinType)){
            return "NMC";
        }
        return "";
    }


    public static boolean isHavePaymentId(String coin_type) {
        if ("xmc".equals(coin_type) || "xmr".equals(coin_type) || "etn".equals(coin_type)) {
            return true;
        } else {
            return false;
        }
    }

    public static HashMap<String,String> sBlockTime = new HashMap<>();

    static {
        sBlockTime.put("btc","600");
        sBlockTime.put("bch","600");
        sBlockTime.put("ltc","150");
        sBlockTime.put("eth","15");
        sBlockTime.put("etc","15");
        sBlockTime.put("zec","150");
        sBlockTime.put("xmr","120");
        sBlockTime.put("dash","150");
        sBlockTime.put("sc","600");
        sBlockTime.put("xmc","120");
        sBlockTime.put("etn","60");
        sBlockTime.put("dcr","300");
        sBlockTime.put("xzc","600");
        sBlockTime.put("btm","150");
        sBlockTime.put("aeon","240");
        sBlockTime.put("nmc","600");
        sBlockTime.put("doge","60");
        sBlockTime.put("via","24");
        sBlockTime.put("game","90");
    }


}
