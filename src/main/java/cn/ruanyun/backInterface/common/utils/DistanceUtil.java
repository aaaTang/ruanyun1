package cn.ruanyun.backInterface.common.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class DistanceUtil {

    /**
     * 赤道半径
     */
    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * Description : 通过经纬度获取距离(单位：米)
     * Group :
     *
     * @param originLon      出发点经度
     * @param originLat      出发点纬度
     * @param destinationLon 目的地经度
     * @param destinationLat 目的地纬度
     * @return double
     * @author honghh
     * @date 2019/2/15 0015 9:14
     */
    public static double getDistance(String originLon, String originLat, String destinationLon, String destinationLat) {
        if (ToolUtil.isEmpty(originLon)) {
            log.error("出发点 经度不可以为空！");
            return 0;
        }
        if (ToolUtil.isEmpty(originLat)) {
            log.error("出发点 纬度不可以为空！");
            return 0;
        }
        if (ToolUtil.isEmpty(destinationLon)) {
            log.error("目的地 经度不可以为空！");
            return 0;
        }
        if (ToolUtil.isEmpty(destinationLat)) {
            log.error("目的地 纬度不可以为空！");
            return 0;
        }

        double radLat1 = rad(Double.parseDouble(originLat));
        double radLat2 = rad(Double.parseDouble(destinationLat));
        double a = radLat1 - radLat2;
        double b = rad(Double.parseDouble(originLon)) - rad(Double.parseDouble(destinationLon));
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        // 保留两位小数
        s = Math.round(s * 100d) / 100d;
        s = s * 1000;
        return s;
    }


    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(getDistance("40.2795256688","93.5156250000","40.2784124966","93.5371685028"));
    }

}
