package com.satyam.whetherreport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.satyam.whetherreport.R;
import com.satyam.whetherreport.WeatherDataService;
import com.satyam.whetherreport.WeatherReportModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_getCityId, btn_WeatherById, btn_weatherByName;
    EditText et_dataInput;
    ListView lv_WeatherReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_getCityId = findViewById(R.id.btn_get_city_id);
        btn_WeatherById = findViewById(R.id.btn_get_wetherbyCityCode);
        btn_weatherByName = findViewById(R.id.btn_get_wehetherbyCityName);
        et_dataInput = findViewById(R.id.et_data_input);
        lv_WeatherReport = findViewById(R.id.lv_wether_report);
       final WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

        btn_getCityId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               weatherDataService.getCityId(et_dataInput.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String CityId) {
                        Toast.makeText(MainActivity.this, "returned City Id " + CityId, Toast.LENGTH_SHORT).show();
                        et_dataInput.setText(CityId);
                    }
                });


            }
        });
        btn_WeatherById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weatherDataService.getCityForecastById(et_dataInput.getText().toString(), new WeatherDataService.ForecastByIDResponse() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModel) {
                        ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,weatherReportModel);
                       lv_WeatherReport.setAdapter(arrayAdapter);
                    }
                });
            }
        });
        btn_weatherByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherDataService.getCityForecastByName(et_dataInput.getText().toString(), new WeatherDataService.GetCityForecastByNameCallback() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModel) {
                        ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,weatherReportModel);
                        lv_WeatherReport.setAdapter(arrayAdapter);
                    }
                });
            }
        });

    }
}