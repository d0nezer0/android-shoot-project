package com.example.common_module.utils;

public class BinaryUtils {
    //有效范围点的坐标
    private final static int[][] points = {
            {-800, 0, -800, 160},
            {-800, 160, -352, 384},
            {-352, 384, -368, 631},
            {-368, 631, -320, 912},
            {320, 912, 368, 631},
            {368, 631, 352, 384},
            {352, 384, 800, 160},
            {800, 160, 800, 0},
    };


    private final static int[] sPoints =
            {-320, 912, 320, 912};

    public static int byteArrayToInt(byte[] byteArray) {

        int result = 0;
        for (int i = 0; i < byteArray.length; i++) {
            result = (result << 8) | (byteArray[i] & 0xFF);
        }

        return result;
    }


    // 2个字节转带符号的整数
    public static int bytesToSignedInt(byte[] bytes) {
        int value = 0;

        for (int i = 0; i < bytes.length; i++) {
            value = value << 8;
            value = value | (bytes[i] & 0xFF);
        }

        // 进行符号位扩展
        int signBit = 1 << ((bytes.length * 8) - 1);
        if ((value & signBit) != 0) {
            value = value - (1 << (bytes.length * 8));
        }

        return value;
    }

    // 计算三角形的面积
    private static double calculateArea(double x1, double y1, double x2, double y2, double x3, double y3) {
        return 0.5 * Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
    }

    // 判断点是否在三角形内部
    private static boolean isPointInTriangle(double x, double y, double a, double b, double c, double d) {
        double areaABC = calculateArea(0, 0, a, b, c, d);
        double areaABP = calculateArea(0, 0, a, b, x, y);
        double areaACP = calculateArea(0, 0, c, d, x, y);
        double areaBCP = calculateArea(a, b, c, d, x, y);

        double totalArea = areaABP + areaACP + areaBCP;

        // 判断点是否在三角形内部
        return Math.abs(totalArea - areaABC) < 1e-3; // 使用一个很小的阈值来比较浮点数
    }

    public static boolean isPointInTriangle(double x, double y) {

        if (y <= 0)
            return true;

        for (int i = 0; i < points.length; i++) {
            int[] point = points[i];
            if (isPointInTriangle(x, y, point[0], point[1], point[2], point[3]))
                return true;
        }

        if (isPointInSector(x, y, sPoints[0], sPoints[1], sPoints[2], sPoints[3]))
            return true;
        return false;
    }

    /**
     * 扇形 计算
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    // 计算两个向量之间的夹角（弧度）
    private static double calculateAngle(double x1, double y1, double x2, double y2) {
        double dotProduct = x1 * x2 + y1 * y2;
        double magnitude1 = Math.sqrt(x1 * x1 + y1 * y1);
        double magnitude2 = Math.sqrt(x2 * x2 + y2 * y2);
        return Math.acos(dotProduct / (magnitude1 * magnitude2));
    }

    // 判断点是否在扇形内部
    public static boolean isPointInSector(double x, double y, double a, double b, double c, double d) {
        double radius = Math.sqrt(a * a + b * b); // 圆的半径
        double distanceToCenter = Math.sqrt(x * x + y * y); // 点到圆心的距离

        if (distanceToCenter > radius) {
            return false; // 点在圆外部
        }

        double angle1 = calculateAngle(a, b, x, y);
        double angle2 = calculateAngle(c, d, x, y);
        double sectorAngle = calculateAngle(a, b, c, d);

        if (angle1 + angle2 - sectorAngle<= 1e-3) {
            return true; // 点在扇形内部
        }

        return false; // 点在扇形外部
    }


}
