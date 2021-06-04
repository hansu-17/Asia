package com.miskaa.asia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miskaa.asia.Util.ConnectionManager;
import com.miskaa.asia.database.CountriesDatabase;
import com.miskaa.asia.database.Country;
import com.miskaa.asia.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    static List<Country> countryList = new ArrayList<>();
    /*ArrayList<String> borders = new ArrayList<>();
    ArrayList<String> languages = new ArrayList<>();*/

    private RequestQueue requestQueue;
    CountriesRecyclerAdapter adapter;

    CountriesDatabase db;
    String url = "https://restcountries.eu/rest/v2/region/asia";

    String strborder;
    String strlang;

    Dialog progressDialog;
    TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new Dialog(MainActivity.this, R.style.CustomProgressDialog);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        dialogText = progressDialog.findViewById(R.id.dialogText);
        dialogText.setText("Hang on...");

        adapter = new CountriesRecyclerAdapter(countryList, this);
        binding.countriesRecycler.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.countriesRecycler.setLayoutManager(manager);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        db = CountriesDatabase.getDatabase(this.getApplicationContext());

        if (new ConnectionManager().checkConnecctivity(this)) {

            countryList.clear();
            db = CountriesDatabase.getDatabase(this.getApplicationContext());
            countryList = db.getCountryDao().getAllCrew();
            adapter = new CountriesRecyclerAdapter(countryList, this);
            binding.countriesRecycler.setAdapter(adapter);

            if (countryList.size() == 0){
                progressDialog.show();
                requestQueue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    public void onResponse(String str) {
                        try {

                            db.getCountryDao().deleteAll();

                            JSONArray jsonArray = new JSONArray(str);
                            Log.e("output...obj..", "\n" + jsonArray);

                            for (int i = 0; i < jsonArray.length(); i++) {

                            /*borders.clear();
                            languages.clear();*/

                                strborder = "";
                                strlang = "";

                                //Declaring a json object corresponding to every pdf object in our json Array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String capital = jsonObject.getString("capital");
                                String flag = jsonObject.getString("flag");
                                String region = jsonObject.getString("region");
                                String subregion = jsonObject.getString("subregion");
                                String population = jsonObject.getString("population");

                                JSONArray border = jsonObject.getJSONArray("borders");
                                for (int j=0; j<border.length(); j++){
                                    /*borders.add(border.getString(j));*/
                                    if (j + 1 == border.length()){
                                        strborder = strborder + border.getString(j);
                                    }else {
                                        strborder = strborder + border.getString(j) + ", ";
                                    }
                                }

                                JSONArray lang = jsonObject.getJSONArray("languages");
                                for(int j=0; j<lang.length(); j++){
                                    JSONObject langObject = lang.getJSONObject(j);
                                    /*languages.add(langObject.getString("name"));*/
                                    if (j + 1 == lang.length()){
                                        strlang = strlang + langObject.getString("name");
                                    }else {
                                        strlang = strlang + langObject.getString("name") + ", ";
                                    }
                                }

                                System.out.println("borders string: " + strborder);
                                System.out.println("languages string: " + strlang);

                                Country model = new Country(name, capital, flag, region, subregion, population, strborder, strlang);

                                db.getCountryDao().insertCountry(model);
                            }

                            loadCountryList();

                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Check Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }) {
                    protected Map getParams() {
                        return new HashMap();
                    }
                });
            }else {
                adapter.notifyDataSetChanged();
            }

        } else {
            countryList.clear();
            db = CountriesDatabase.getDatabase(this.getApplicationContext());
            countryList = db.getCountryDao().getAllCrew();
            adapter = new CountriesRecyclerAdapter(countryList, this);
            binding.countriesRecycler.setAdapter(adapter);

            System.out.println("Size: " + countryList.size());
            if (countryList.size() == 0){
                noInternet();
            }else {
                adapter.notifyDataSetChanged();
            }
        }

        binding.imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new ConnectionManager().checkConnecctivity(MainActivity.this)) {
                    progressDialog.show();
                    requestQueue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        public void onResponse(String str) {
                            try {
                                db.getCountryDao().deleteAll();
                                /*crewList1.clear();*/
                                JSONArray jsonArray = new JSONArray(str);
                                Log.e("output...obj..", "\n" + jsonArray);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    strborder = "";
                                    strlang = "";

                                    //Declaring a json object corresponding to every pdf object in our json Array
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString("name");
                                    String capital = jsonObject.getString("capital");
                                    String flag = jsonObject.getString("flag");
                                    String region = jsonObject.getString("region");
                                    String subregion = jsonObject.getString("subregion");
                                    String population = jsonObject.getString("population");

                                    JSONArray border = jsonObject.getJSONArray("borders");
                                    for (int j=0; j<border.length(); j++){
                                        /*borders.add(border.getString(j));*/
                                        if (j + 1 == border.length()){
                                            strborder = strborder + border.getString(j);
                                        }else {
                                            strborder = strborder + border.getString(j) + ", ";
                                        }
                                    }

                                    JSONArray lang = jsonObject.getJSONArray("languages");
                                    for(int j=0; j<lang.length(); j++){
                                        JSONObject langObject = lang.getJSONObject(j);
                                        /*languages.add(langObject.getString("name"));*/
                                        if (j + 1 == lang.length()){
                                            strlang = strlang + langObject.getString("name");
                                        }else {
                                            strlang = strlang + langObject.getString("name") + ", ";
                                        }
                                    }

                                    System.out.println("borders string: " + strborder);
                                    System.out.println("languages string: " + strlang);

                                    Country model = new Country(name, capital, flag, region, subregion, population, strborder, strlang);

                                    db.getCountryDao().insertCountry(model);
                                }

                                loadCountryList();
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this,
                                        "Refreshed Successfully...",
                                        Toast.LENGTH_SHORT)
                                        .show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Check Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        protected Map getParams() {
                            return new HashMap();
                        }
                    });
                } else {
                    noInternet();
                }
            }
        });

        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryList.clear();
                db.getCountryDao().deleteAll();
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,
                        "Database cleared Successfully...",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void noInternet(){
        new AlertDialog.Builder(MainActivity.this, R.style.CustomAlertDialog)
                .setTitle("Offline")
                .setIcon(R.drawable.ic_no_internet)
                .setMessage("Your network is unavailable.\nCheck your data or wifi connection.")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (new ConnectionManager().checkConnecctivity(MainActivity.this)) {
                            progressDialog.show();
                            requestQueue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                public void onResponse(String str) {
                                    try {
                                        db.getCountryDao().deleteAll();

                                        JSONArray jsonArray = new JSONArray(str);
                                        Log.e("output...obj..", "\n" + jsonArray);

                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            strborder = "";
                                            strlang = "";

                                            //Declaring a json object corresponding to every pdf object in our json Array
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String name = jsonObject.getString("name");
                                            String capital = jsonObject.getString("capital");
                                            String flag = jsonObject.getString("flag");
                                            String region = jsonObject.getString("region");
                                            String subregion = jsonObject.getString("subregion");
                                            String population = jsonObject.getString("population");

                                            JSONArray border = jsonObject.getJSONArray("borders");
                                            for (int j=0; j<border.length(); j++){
                                                /*borders.add(border.getString(j));*/
                                                if (j + 1 == border.length()){
                                                    strborder = strborder + border.getString(j);
                                                }else {
                                                    strborder = strborder + border.getString(j) + ", ";
                                                }
                                            }

                                            JSONArray lang = jsonObject.getJSONArray("languages");
                                            for(int j=0; j<lang.length(); j++){
                                                JSONObject langObject = lang.getJSONObject(j);
                                                /*languages.add(langObject.getString("name"));*/
                                                if (j + 1 == lang.length()){
                                                    strlang = strlang + langObject.getString("name");
                                                }else {
                                                    strlang = strlang + langObject.getString("name") + ", ";
                                                }
                                            }

                                            System.out.println("borders string: " + strborder);
                                            System.out.println("languages string: " + strlang);

                                            Country model = new Country(name, capital, flag, region, subregion, population, strborder, strlang);

                                            db.getCountryDao().insertCountry(model);
                                        }

                                        loadCountryList();
                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this,
                                                "Retry Successful...",
                                                Toast.LENGTH_SHORT)
                                                .show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Check Internet Connection", Toast.LENGTH_LONG).show();
                                }
                            }) {
                                protected Map getParams() {
                                    return new HashMap();
                                }
                            });

                        } else {
                            noInternet();
                        }
                    }
                }).setNegativeButton("Exit",
                (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .show();
    }

    private void loadCountryList(){
        countryList.clear();
        db = CountriesDatabase.getDatabase(this.getApplicationContext());
        countryList = db.getCountryDao().getAllCrew();
        adapter = new CountriesRecyclerAdapter(countryList, this);
        binding.countriesRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}