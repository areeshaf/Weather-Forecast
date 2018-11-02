package com.example.hp.weatherforecast;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public void weatherInfo(View view){
        DownloadContent task=new DownloadContent();
        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");

    }

    public class DownloadContent extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            String result="";
            HttpURLConnection urlConnection;
            try{
                url=new URL(strings[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(inputStream);
                int data = reader.read();
                while (data!=-1){
                    char current=(char)data;
                    result=result+current;
                    data=reader.read();
                }
                return result;
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo=jsonObject.getString("weather");
                JSONArray jsonArray=new JSONArray(weatherInfo);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonPart=jsonArray.getJSONObject(i);
                    Log.i("Main",jsonPart.getString("main"));
                    Log.i("Description",jsonPart.getString("description"));

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
