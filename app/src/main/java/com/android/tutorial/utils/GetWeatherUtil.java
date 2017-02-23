package com.android.tutorial.utils;

import com.noveogroup.android.log.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mannan on 2/22/2017.
 */

public class GetWeatherUtil {

    // Get you own API Key here: http://www.wunderground.com/weather/api
    static final String API_KEY = "13a68a1203a20f55";

    public GetWeatherUtil() {
    }

    public String getWeatherData(String postalCode) {

        String url = "http://api.wunderground.com/api/" + API_KEY
                + "/conditions/q/" + postalCode + ".xml";

        InputStream is = null;
        String weatherData = null;

        // Get the XML stream from the web
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("getWeather", "Error in http connection " + e.toString());
        }

        // Let's convert stream to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            weatherData = sb.toString();
        } catch (Exception e) {
            Log.e("getWeather", "Error converting result " + e.toString());
        }
        return weatherData;
    }

    public String getTemperature(String weatherData) {

        String temperature = null;
        DecimalFormat df = new DecimalFormat("#");

        Pattern p = Pattern.compile("<temp_f>(.*?)</temp_f>");
        Matcher m = p.matcher(weatherData);
        if (m.find()) {
            temperature = m.toMatchResult().group(1);
        }
        try {
            Double current_temp_dbl = Double.parseDouble(temperature);
            temperature = df.format(current_temp_dbl) + "\u00B0" + "F";
        } catch (Exception e) {
        }

        return temperature;
    }

    public String getConditions(String weatherData) {

        String conditions = null;

        Pattern p = Pattern.compile("<weather>(.*?)</weather>");
        Matcher m = p.matcher(weatherData);
        if (m.find()) {
            conditions = m.toMatchResult().group(1);
        }
        return conditions;
    }

    public String getWindDirection(String weatherData) {

        String windDirection = null;

        Pattern p = Pattern.compile("<wind_dir>(.*?)</wind_dir>");
        Matcher m = p.matcher(weatherData);
        if (m.find()) {
            windDirection = m.toMatchResult().group(1);
        }
        return windDirection;
    }

    public String getWindSpeed(String weatherData) {

        String windSpeed = null;

        Pattern p = Pattern.compile("<wind_mph>(.*?)</wind_mph>");
        Matcher m = p.matcher(weatherData);
        if (m.find()) {
            windSpeed = m.toMatchResult().group(1);
        }
        return windSpeed;
    }

    /////////////////////////////////////////////////////////////
    // Add methods as needed for addition data retrieval from XML
    /////////////////////////////////////////////////////////////

}
