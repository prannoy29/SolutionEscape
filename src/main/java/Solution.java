import plans.RoutePlan;
import pojos.CityDetails;
import utils.converters.JsonArrayToCityDetails;

import java.util.List;
import java.util.Map;

public class Solution {
    public static void main(String args[]){
        Map<String, List<CityDetails>> map = JsonArrayToCityDetails.fetchCityDetailsFromUrl(args[0],args[1]);
        CityDetails cityDetails = JsonArrayToCityDetails.originCityDetailObject;
        RoutePlan routePlan = new RoutePlan(map,cityDetails);
        routePlan.createRoutePlan();
    }
}
