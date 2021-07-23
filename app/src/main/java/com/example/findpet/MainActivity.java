package com.example.findpet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    Button sortnameasc, sortagedesc;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initRecyclerView();
        initSortButtons();
        initSearchButton();

        try {
            populateAllPets(false,"","", false,"");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initSearchButton() {
        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    if (!query.isEmpty()) {
                        populateAllPets(false, "", "", true, query);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        int clearButton = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView clearButtonImg = findViewById(clearButton);
        clearButtonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    populateAllPets(false,"","",false,"");
                    searchView.setQuery("", false);
                    Log.d("Hi","Search");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initSortButtons() {
        sortnameasc = findViewById(R.id.sortbyname);
        sortagedesc = findViewById(R.id.sortbyage);

        sortnameasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    populateAllPets(true, "asc", "name",false,"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sortagedesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    populateAllPets(true, "asc", "bornAt", false,"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

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

    private void populateAllPets(boolean sort, String sortOrder, String sortParameter, boolean search, String searchname) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Uri.Builder path = Uri.parse(getString(R.string.baseurl)).buildUpon();
        if(sort){
            path = path.appendQueryParameter("sortBy",sortParameter);
            path = path.appendQueryParameter("order",sortOrder);
        }

        if (search)
        {
            path = path.appendQueryParameter("search", searchname);
        }

        Request request = new Request.Builder()
                .url(path.build().toString())
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
                        long elapsedMonths = (elapsedTime)/(24*30*60);
                        long elapsedDays = (elapsedTime)/(24*60);
                        long elapsedYears = (elapsedTime)/(24*365*60);
                        long elapsedHours = (elapsedTime)/(60);
                        //Log.d("Hi",(elapsedTime)+"");

                        if ( elapsedYears > 0)
                        {
                            if (elapsedYears == 1)
                                actualAge = elapsedYears + " Year";
                            else
                                actualAge = elapsedYears + " Years";
                        }
                        else if (elapsedMonths > 0)
                        {
                            if (elapsedMonths == 1)
                                actualAge = elapsedMonths + " Month";
                            else
                                actualAge = elapsedMonths + " Months";
                        }
                        else if (elapsedDays > 0)
                        {
                            if (elapsedDays == 1)
                                actualAge = elapsedDays + " Day";
                            else
                                actualAge = elapsedDays + " Day";
                        }
                        else
                        {
                            if (elapsedHours == 1)
                                actualAge = elapsedHours + " Hour";
                            else
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