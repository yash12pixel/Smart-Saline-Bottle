package com.example.smartsallinebottle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Dash extends AppCompatActivity {
    SwipeRefreshLayout refreshLayout;
    public RequestQueue mQueue;
    private FirebaseAuth mAuth;
    private FirebaseUser cur_user;
    Calendar cal=Calendar.getInstance();
    private ProgressBar pgsBar;
    int bottle=539;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        mAuth = FirebaseAuth.getInstance();
        cur_user = mAuth.getCurrentUser();
        WebView webView = (WebView) findViewById(R.id.grp);
        WebView webView1 = (WebView) findViewById(R.id.grp1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://thingspeak.com/channels/1205152/charts/1?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15");
        webView1.loadUrl("https://thingspeak.com/channels/1205152/widgets/251391");
        BottleData();

        refreshLayout=findViewById(R.id.swipelayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BottleData();
                refreshLayout.setRefreshing(false);
            }
        });
    }
    public void BottleData(){
        pgsBar = (ProgressBar) findViewById(R.id.pgr);
        txt=findViewById(R.id.txt);

        mQueue = Volley.newRequestQueue(getApplicationContext());
        String lightApi = "https://api.thingspeak.com/channels/1205152/fields/1.json?api_key=GY6B24TC2QEWQ0WP&results=1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, lightApi, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("feeds");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        String c_date = employee.getString("created_at");
                        String new_dt = c_date.substring(0, 10);
                        String[] parts = new_dt.split("-");
                        String get_mon = parts[parts.length - 2];
                        String get_day = parts[parts.length - 1];
                        int month = cal.get(Calendar.MONTH) + 1;
                        int day = cal.get(Calendar.DATE);

                        int btl = Integer.parseInt(employee.getString("field1"));
                        int per = btl * 100 / bottle;
                        System.out.println("percentage " + per);
                        pgsBar.setProgress(per);
                        txt.setText("" + per + "% saline left in the bottle");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
mQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.donateinfo) {
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}
