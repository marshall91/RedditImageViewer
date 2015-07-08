package com.example.mackenziem.redditimageviewer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static final String LOGTAG = "MAIN";

    private ArrayAdapter<String> mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String redditUrl = "https://www.reddit.com/r/aww.json";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, redditUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(LOGTAG, jsonObject.toString());
                        addImagesToList(jsonObject);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(LOGTAG, volleyError.getMessage());
                    }
                });

        VolleySingleton.getInstance().getRequestQueue().add(jsObjRequest);

        List<String> images = new ArrayList<String>();
        mImageAdapter = new CustomImageAdapter(this, R.layout.list_item_net_image, images);
        ListView listView = (ListView) findViewById(R.id.listview_image);
        listView.setAdapter(mImageAdapter);
    }

    private void addImagesToList(JSONObject jsonObject) {
        JSONArray items = new JSONArray();
        try {
            items = jsonObject.getJSONObject("data").getJSONArray("children");
            Log.d(LOGTAG, items.toString());

        } catch (JSONException e) {
            Log.e(LOGTAG, "failed to parse json: " + e.getMessage());
        }


        for (int i = 0; i < items.length(); i++) {
            try {
                JSONObject post = items.getJSONObject(i);
                JSONObject preview = post.getJSONObject("data").getJSONObject("preview");
                JSONArray images = preview.getJSONArray("images");
                JSONObject source = images.getJSONObject(0).getJSONObject("source");

                String imgUrl = source.getString("url");
                mImageAdapter.add(imgUrl);
            }  catch (JSONException e) {
                Log.e(LOGTAG, "failed to parse imgUrl: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
