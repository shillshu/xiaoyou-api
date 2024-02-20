package cn.sibetech.fellow.util;

/**
 * Created by sibe-pc-20170617 on 2018-04-16.
 */
public class DistanceUtil {
    private static double EARTH_RADIUS = 6378.137;// 单位千米

    /**
     * 角度弧度计算公式 rad:().
     * 360度=2π π=Math.PI
     * x度 = x*π/360 弧度
     *
     * @param degree
     * @return
     */
    private static double getRadian(double degree) {
        return degree * Math.PI / 180.0;
    }

    /**
     * 依据经纬度计算两点之间的距离 GetDistance:().
     *
     * @param lat1 1点的纬度
     * @param lng1 1点的经度
     * @param lat2 2点的纬度
     * @param lng2 2点的经度
     * @return 距离 单位 米
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = getRadian(lat1);
        double radLat2 = getRadian(lat2);
        double a = radLat1 - radLat2;// 两点纬度差
        double b = getRadian(lng1) - getRadian(lng2);// 两点的经度差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS * 1000;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    //合肥与杭州的经纬度距离计算
    public static void main(String ar[]) {
        System.out.println(getDistance(30.5187, 114.428166, 30.49984, 114.41983));
    }
}
