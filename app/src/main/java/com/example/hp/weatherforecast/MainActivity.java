package com.example.hp.weatherforecast;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView weatherTextView;
    public void weatherInfo(View view){
        try {
            DownloadContent task = new DownloadContent();
            String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=b6907d289e10d714a6e88b30761fae22");
            InputMethodManager methodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Weather Information Not Available!!", Toast.LENGTH_SHORT).show();
        }
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
                Log.i("Result",result);
                return result;
            }catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Weather Information Not Available!!", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo=jsonObject.getString("weather");
                Log.i("Weather info",weatherInfo);
                JSONArray jsonArray=new JSONArray(weatherInfo);
                String message="";
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonPart=jsonArray.getJSONObject(i);
                    String main=jsonPart.getString("main");
                    String description=jsonPart.getString("description");
                   Log.i("Main",jsonPart.getString("main"));
                    Log.i("Description",jsonPart.getString("description"));
                    if(!main.equals("") && !description.equals(""))
                    message=message+main+" : "+description+"\r\n";

                }
                if(!message.equals("")){

                    weatherTextView.setText(message);
                }
            }catch (Exception e){

                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Weather Information Not Available!!", Toast.LENGTH_SHORT).show();

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.editText);
        weatherTextView=findViewById(R.id.weatherTextView);
    }
}
