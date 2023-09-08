package com.oceans7.dib.global.util;

public class CoordinateUtil {
    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double METERS_IN_KILOMETER = 1000.0;
    private static final double ROUNDING_PRECISION = 10.0;

    // 좌표 간 거리 계산
    public static double calculateDistance(double lon1, double lat1, double lon2, double lat2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return round(EARTH_RADIUS_KM * c);
    }

    // m -> km 변환
    public static double convertMetersToKilometers(double meters) {
        return (ValidatorUtil.isNotEmpty(meters)) ? round(meters / METERS_IN_KILOMETER) : 0.0;
    }

    // 반올림
    private static double round(double distance) {
        return Math.round(distance * ROUNDING_PRECISION) / ROUNDING_PRECISION;
    }

    // LCC DFS 좌표변환 ( 위경도->좌표, mapX:경도, mapY:위도 )
    public static LatXLngY convertGRID_GPS(double mapX, double mapY)
    {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( 위경도->좌표, mapX:경도, mapY:위도 )
        //
        double DEGRAD = Math.PI/ 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI* 0.25 + slat2 * 0.5) / Math.tan(Math.PI* 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI* 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI* 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        rs.lat = mapY;
        rs.lng = mapX;
        double ra = Math.tan(Math.PI* 0.25 + (mapY) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = mapX * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
        rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

        return rs;
    }

    public static class LatXLngY
    {
        public double lat;
        public double lng;

        public double x;
        public double y;

    }
}
