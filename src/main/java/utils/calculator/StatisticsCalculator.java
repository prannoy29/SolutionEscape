package utils.calculator;

import pojos.CityDetails;
import pojos.Point;

import java.math.BigDecimal;
import java.util.List;


public class StatisticsCalculator {
    public static Point getMeanCenterPoint(List<CityDetails> cityDetails){
        double lat = 0.0;
        double lon = 0.0;
        double size = cityDetails.size();
        for(CityDetails cityDetail : cityDetails){
            lat = lat + cityDetail.getPoint().getLat().doubleValue();
            lon = lon + cityDetail.getPoint().getLon().doubleValue();
        }
        Point resultPoint = new Point();
        resultPoint.setLat(new BigDecimal(lat/size));
        resultPoint.setLon(new BigDecimal(lon/size));
        return resultPoint;
    }

    public static List<CityDetails> fetchClosestNCities(List<CityDetails> cityDetails, final Point point, int n){
        cityDetails.sort(
                (cd1,cd2) -> {
                    Double d1 = DistanceCalculator.calculateDistanceBetweenPoints(cd1.getPoint(),point);
                    Double d2 = DistanceCalculator.calculateDistanceBetweenPoints(cd2.getPoint(),point);
                    return d1.compareTo(d2);
                }
        );
        return cityDetails.subList(0,n-1);
    }

}
