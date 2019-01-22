/*
 * Created by wulin on 18-4-13 上午11:05.
 * Copyright (c) 2018 Blockin. All Rights Reserved.
 * Last modified 18-4-13 上午11:05.
 */

package com.yjq.hashchart;

import java.util.ArrayList;
import java.util.Random;

/*

转换服务器返回的数据格式
tickers:
[
    [
      1521417600,//时间 单位秒
      3.745,//算力
      0.0029
     ],
*/
public class LinePointManager {

    public static ArrayList<LinePoint> getPoints(String dimension){
        ArrayList<LinePoint> fakeData = new ArrayList<>();
        for (int i=0;i<25;i++){
            LinePoint linePoint = new LinePoint();
            if(dimension.equals("1h")){
                linePoint.time = 1541343017L + i * 3600L;
            }else {
                linePoint.time = 1541343017L + i * 86400L;
            }
            linePoint.dimension = dimension;
            linePoint.pow = new Random().nextInt(100);
            linePoint.rate = new Random().nextInt(10);
            linePoint.unit = "K";
            fakeData.add(linePoint);
        }
        return fakeData;
    }

    public static class LinePoint {
        public long time;//时间 毫秒
        public float pow;//算力
        public float rate;//拒绝率
        public String dimension;
        public String unit;
    }
}
