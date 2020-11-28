package plans;

import pojos.CityDetails;
import pojos.Point;
import utils.calculator.DistanceCalculator;
import utils.calculator.StatisticsCalculator;

import java.util.*;

public class RoutePlan {
    private final Map<String, List<CityDetails>> continentCityMap;
    private final CityDetails originCityDetails;
    private Map<String, Point> continentCenter = new HashMap<>();


    public RoutePlan(Map<String, List<CityDetails>> continentCityMap, CityDetails originCityDetails) {
        this.continentCityMap = continentCityMap;
        this.originCityDetails = originCityDetails;
    }

    public void createRoutePlan() {
        double shortestDistanceBatch1 = Double.MAX_VALUE;
        double shortestDistanceBatch2 = Double.MAX_VALUE;
        CityDetails batch1midCity = null;
        CityDetails[] batch1endCity = new CityDetails[2];
        CityDetails[] batch2City = new CityDetails[2];
        List<String> closestContinent = findClosestContinents();
        List<CityDetails> continent1 = continentCityMap.get(closestContinent.get(0));
        List<CityDetails> continent2 = continentCityMap.get(closestContinent.get(1));
        List<List<CityDetails>> otherList = new ArrayList<>();
        for (String key : continentCityMap.keySet()) {
            if (!key.equals(closestContinent.get(0)) && !key.equals(closestContinent.get(1))) {
                otherList.add(continentCityMap.get(key));
            }
        }




        List<CityDetails> continent3 = StatisticsCalculator.fetchClosestNCities(otherList.get(0),continentCenter.get(otherList.get(0).get(0).getContinent()), 300);
        List<CityDetails> continent4 = StatisticsCalculator.fetchClosestNCities(otherList.get(1),continentCenter.get(otherList.get(1).get(0).getContinent()), 300);
        List<CityDetails> continent5 = StatisticsCalculator.fetchClosestNCities(otherList.get(2),continentCenter.get(otherList.get(2).get(0).getContinent()), 300);;
        continent1 = StatisticsCalculator.fetchClosestNCities(continent1, continentCenter.get(closestContinent.get(0)), 300);
        continent2 = StatisticsCalculator.fetchClosestNCities(continent2, continentCenter.get(closestContinent.get(1)), 300);

        for (CityDetails cityDetailsContinent3 : continent3) {
            for (CityDetails cityDetailsContinent4 : continent4) {
                for (CityDetails cityDetailsContinent5 : continent5) {
                    CityDetails midCity;
                    CityDetails[] endCity = new CityDetails[2];
                    double largest;
                    double ab = DistanceCalculator.calculateDistanceBetweenPoints(cityDetailsContinent3.getPoint(), cityDetailsContinent4.getPoint());
                    double bc = DistanceCalculator.calculateDistanceBetweenPoints(cityDetailsContinent4.getPoint(), cityDetailsContinent5.getPoint());
                    double ac = DistanceCalculator.calculateDistanceBetweenPoints(cityDetailsContinent3.getPoint(), cityDetailsContinent5.getPoint());
                    if(ac >= bc && ac >=ab){
                        largest =ac ;
                        midCity = cityDetailsContinent4;
                        endCity[0]= cityDetailsContinent3;
                        endCity[1]= cityDetailsContinent5;
                    }else if(bc > ac){
                        largest = bc;
                        midCity = cityDetailsContinent3;
                        endCity[0]= cityDetailsContinent4;
                        endCity[1]= cityDetailsContinent5;
                    }else {
                        largest= ab;
                        midCity = cityDetailsContinent5;
                        endCity[0]= cityDetailsContinent3;
                        endCity[1]= cityDetailsContinent4;
                    }
                    if(shortestDistanceBatch1 > ab+bc+ac-largest)
                    shortestDistanceBatch1 = ab+bc+ac-largest;
                    batch1midCity = midCity;
                    batch1endCity[0]= endCity[0];
                    batch1endCity[1] = endCity[1];
                }
            }
        }


        for (CityDetails cityDetailsContinent1 : continent1) {
            for (CityDetails cityDetailsContinent2 : continent2) {
                CityDetails[] endCity = new CityDetails[2];
                double smallest;
                double ac = DistanceCalculator.calculateDistanceBetweenPoints(cityDetailsContinent1.getPoint(), batch1endCity[0].getPoint());
                double bd = DistanceCalculator.calculateDistanceBetweenPoints(cityDetailsContinent2.getPoint(),batch1endCity[1].getPoint());
                double ad = DistanceCalculator.calculateDistanceBetweenPoints(cityDetailsContinent1.getPoint(), batch1endCity[1].getPoint());
                double bc = DistanceCalculator.calculateDistanceBetweenPoints(cityDetailsContinent2.getPoint(),batch1endCity[0].getPoint());
                if(ac+bd < ad+bc){
                    endCity[0] = cityDetailsContinent1;
                    endCity[1] = cityDetailsContinent2;
                    smallest = ac +bd;
                }else {
                    endCity[0] = cityDetailsContinent2;
                    endCity[1] = cityDetailsContinent1;
                    smallest = ad +bc;
                }
                if(shortestDistanceBatch2 > smallest){
                    shortestDistanceBatch2 = smallest;
                    batch2City[0]=endCity[0];
                    batch2City[1]=endCity[1];
                }
            }
        }
        double originBatch = DistanceCalculator.calculateDistanceBetweenPoints(batch2City[0].getPoint(),originCityDetails.getPoint()) +
                DistanceCalculator.calculateDistanceBetweenPoints(batch2City[1].getPoint(),originCityDetails.getPoint());
        System.out.println(String.format("Total distance %s km",(int)(shortestDistanceBatch1+shortestDistanceBatch2+originBatch)));
        System.out.println(String.format(
                "Shortest Path is %s(%s,%s) -> %s(%s,%s) -> %s(%s,%s) -> %s(%s,%s) -> %s(%s,%s) -> %s(%s,%s) -> %s(%s,%s)"
                ,originCityDetails.getId(),originCityDetails.getName(),originCityDetails.getContinent(),
                batch2City[0].getId(),batch2City[0].getName(),batch2City[0].getContinent(),
                batch1endCity[0].getId(),batch1endCity[0].getName(),batch1endCity[0].getContinent(),
                batch1midCity.getId(),batch1midCity.getName(),batch1midCity.getContinent(),
                batch1endCity[1].getId(),batch1endCity[1].getName(),batch1endCity[1].getContinent(),
                batch2City[1].getId(),batch2City[1].getName(),batch2City[1].getContinent(),
                originCityDetails.getId(),originCityDetails.getName(),originCityDetails.getContinent()));
    }

    private List<String> findClosestContinents() {
        for (String name : continentCityMap.keySet()) {
            continentCenter.put(name, StatisticsCalculator.getMeanCenterPoint(continentCityMap.get(name)));
        }
        double distance1 = Double.MAX_VALUE;
        double distance2 = Double.MAX_VALUE;
        String dist1Cont = "";
        String dist2Cont = "";
        for (String name : continentCenter.keySet()) {
            double currr = DistanceCalculator.calculateDistanceBetweenPoints(continentCenter.get(name), originCityDetails.getPoint());
            if (distance1 > currr) {
                distance2 = distance1;
                distance1 = currr;
                dist2Cont = dist1Cont;
                dist1Cont = name;
            } else if (distance2 > currr && currr != distance1) {
                distance2 = currr;
                dist2Cont = name;
            }
        }
        return Arrays.asList(dist1Cont, dist2Cont);
    }

    private static double getMin(double[] inputArray){
        double minValue = inputArray[0];
        for(int i=1;i<inputArray.length;i++){
            if(inputArray[i] < minValue){
                minValue = inputArray[i];
            }
        }
        return minValue;
    }

}
