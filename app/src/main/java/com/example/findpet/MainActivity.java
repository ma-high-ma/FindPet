package com.example.findpet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClass> petList;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initRecyclerView();

        try {
            populateAllPets();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initRecyclerView() {

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(petList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void initData() {

        petList = new ArrayList<>();


    }

    private void populateAllPets() throws IOException {

        OkHttpClient client = new OkHttpClient();

        String url = "https://60d075407de0b20017108b89.mockapi.io/api/v1/animals";
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)  {

                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    petList.clear();

                    //Date current_time = Calendar.getInstance().getTime();
                    String actualAge;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String time = jsonArray.getJSONObject(i).getString("bornAt");
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                        Date actual_time = format.parse(time);

                        long timestamp = actual_time.getTime();

                        int elapsedTime = (int)(System.currentTimeMillis()/60000 - timestamp/60000);
                        Log.d("Hi",elapsedTime+"");
                        long elapsedMonths = (elapsedTime)/(24*30*60);

                        long elapsedDays = (elapsedTime)/(24*60);
                        long elapsedYears = (elapsedTime)/(24*365*60);
                        long elapsedHours = (elapsedTime)/(60);
                        Log.d("Hi",(elapsedTime)+"");

                        if ( elapsedYears > 0)
                        {
                            actualAge = elapsedYears + " Years";
                        }
                        else if (elapsedMonths > 0)
                        {
                            actualAge = elapsedMonths + " Months";
                        }
                        else if (elapsedDays > 0)
                        {
                            actualAge = elapsedDays + " Days";
                        }
                        else
                        {
                            actualAge = elapsedHours + " Hours";
                        }

                        petList.add(new ModelClass(jsonArray.getJSONObject(i).getString("name"), actualAge+""));

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException | IOException | ParseException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("Juhi", "Error");
            }
        });
    }


}