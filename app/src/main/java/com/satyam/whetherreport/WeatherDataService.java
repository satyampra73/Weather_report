package com.satyam.whetherreport;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {
    Context context;
    String CityID = "";

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String CityId);
    }

    public static final String Quary_For_CityID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String Quary_For_City_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";

    public void getCityId(String CityName, VolleyResponseListener volleyResponseListener) {
        String url = Quary_For_CityID + CityName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                CityID = "";
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    CityID = jsonObject.getString("woeid");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Toast.makeText(context, "returned City Id " + CityID, Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(CityID);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(context, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("something went wrong");
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface ForecastByIDResponse {
        void onError(String message);

        void onResponse(List<WeatherReportModel> weatherReportModel);
    }

    public void getCityForecastById(String CityID, final ForecastByIDResponse forecastByIDResponse) {
        List<WeatherReportModel> weatherReportModels = new ArrayList<>();
        String url = Quary_For_City_WEATHER_BY_ID + CityID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");


                    for (int i = 0; i < consolidated_weather_list.length(); i++) {
                        WeatherReportModel one_day_weather = new WeatherReportModel();
                        JSONObject first_day_fromApi = (JSONObject) consolidated_weather_list.get(i);
                        one_day_weather.setId(first_day_fromApi.getInt("id"));
                        one_day_weather.setWeather_state_name(first_day_fromApi.getString("weather_state_name"));
                        one_day_weather.setWeather_state_abbr(first_day_fromApi.getString("weather_state_abbr"));
                        one_day_weather.setWind_direction_compass(first_day_fromApi.getString("wind_direction_compass"));
                        one_day_weather.setCreated(first_day_fromApi.getString("created"));
                        one_day_weather.setApplicable_date(first_day_fromApi.getString("applicable_date"));
                        one_day_weather.setMin_temp(first_day_fromApi.getString("min_temp"));
                        one_day_weather.setMax_temp(first_day_fromApi.getString("max_temp"));
                        one_day_weather.setThe_temp(first_day_fromApi.getString("the_temp"));
                        one_day_weather.setWind_speed(first_day_fromApi.getString("wind_speed"));
                        one_day_weather.setWind_direction(first_day_fromApi.getString("wind_direction"));
                        one_day_weather.setAir_pressure(first_day_fromApi.getString("air_pressure"));
                        one_day_weather.setHumidity(first_day_fromApi.getString("humidity"));
                        one_day_weather.setVisibility(first_day_fromApi.getString("visibility"));
                        one_day_weather.setPredictability(first_day_fromApi.getString("predictability"));
                        weatherReportModels.add(one_day_weather);
                    }
                    forecastByIDResponse.onResponse(weatherReportModels);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public interface GetCityForecastByNameCallback {
        void onError(String message);

        void onResponse(List<WeatherReportModel> weatherReportModel);
    }

    public void getCityForecastByName(String CityName, GetCityForecastByNameCallback getCityForecastByNameCallback) {
        getCityId(CityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String CityId) {
                getCityForecastById(CityId, new ForecastByIDResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModel) {
                        getCityForecastByNameCallback.onResponse(weatherReportModel);
                    }
                });

            }
        });

    }
}


//        return CityID;



