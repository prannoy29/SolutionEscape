package utils.converters;

import org.json.JSONObject;
import pojos.CityDetails;
import pojos.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class JsonArrayToCityDetails {
    public static CityDetails originCityDetailObject = null;

    private static String readUrl(String urlString) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public static Map<String,List<CityDetails>> fetchCityDetailsFromUrl(String urlString,String cityName){
        JSONObject json = null;
        Map<String,List<CityDetails>> stringListMap = new HashMap<>();
        try {
            json = new JSONObject(readUrl(urlString));
            Iterator<String> keyList = json.keys();
            JSONObject originCityDetails = (JSONObject) json.get(cityName);
            originCityDetailObject = convertCityJsonToObject(originCityDetails);
            while (keyList.hasNext()){
                String cityKey = keyList.next();
                JSONObject cityDetails = (JSONObject) json.get(cityKey);
                if(!cityDetails.get("contId").equals(originCityDetailObject.getContinent())) {
                    if (stringListMap.containsKey(cityDetails.get("contId"))) {
                        List<CityDetails> cityDetailList = stringListMap.get(cityDetails.get("contId"));
                        cityDetailList.add(convertCityJsonToObject(cityDetails));
                        stringListMap.put((String) cityDetails.get("contId"), cityDetailList);
                    } else {
                        List<CityDetails> cityDetailList = new ArrayList<>();
                        cityDetailList.add(convertCityJsonToObject(cityDetails));
                        stringListMap.put((String) cityDetails.get("contId"), cityDetailList);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringListMap;
    }

    private static CityDetails convertCityJsonToObject(JSONObject cityDetailObject){
        CityDetails cityDetails = new CityDetails();
        cityDetails.setContinent((String) cityDetailObject.get("contId"));
        JSONObject pointObject = (JSONObject) cityDetailObject.get("location");
        Point point = new Point((BigDecimal)pointObject.get("lat"),(BigDecimal)pointObject.get("lon"));
        cityDetails.setPoint(point);
        cityDetails.setName((String) cityDetailObject.get("name"));
        cityDetails.setId((String) cityDetailObject.get("id"));
        return cityDetails;
    }

}
