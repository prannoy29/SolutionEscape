package utils.calculator;

import pojos.Point;

public class DistanceCalculator {

    private static final double RADIUS = 6371.00;

    public static double calculateDistanceBetweenPoints(Point pointA, Point pointB) {
        return calculateHaversineDistance(pointA.getLat().doubleValue(), pointB.getLat().doubleValue(), pointA.getLon().doubleValue(), pointB.getLon().doubleValue());
    }

    private static double calculateHaversineDistance(double lat1, double lat2, double lon1, double lon2) {
        double dLat = getRadFromDeg(lat2 - lat1);
        double dLon = getRadFromDeg(lon2 - lon1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(getRadFromDeg(lat1)) * Math.cos(getRadFromDeg(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)) * RADIUS;
    }

    private static double getRadFromDeg(double deg) {
        return deg * (Math.PI / 180.00);
    }

}
