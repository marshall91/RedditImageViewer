package com.example.mackenziem.redditimageviewer;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private static final String LOGTAG = "MAIN_FRAG";

    private ArrayAdapter<String[]> mImageAdapter;

    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

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

        List<String[]> images = new ArrayList<String[]>();
        mImageAdapter = new CustomImageAdapter(getActivity(), R.layout.list_item_net_image, images);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_image);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] image = mImageAdapter.getItem(position);
                CommentsFragment fragment = new CommentsFragment();
                fragment.setCommentUrl(image[1]);
                fragment.setImgUrl(image[0]);
                Log.d(LOGTAG, "Click on image: " + image[1]);
                if(getActivity() == null) {
                    Log.d(LOGTAG, "No activity found");
                } else {
                    getActivity().getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(
                                    R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                                    R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                            .replace(R.id.activity_main_container, fragment).addToBackStack(null).commit();
                }
            }
        });

        listView.setAdapter(mImageAdapter);

        return rootView;
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

                String permaLink = post.getJSONObject("data").getString("permalink");
                String commentsUrl = "http://i.reddit.com" + permaLink;

                String[] obj = {imgUrl, commentsUrl};
                mImageAdapter.add(obj);

            }  catch (JSONException e) {
                Log.e(LOGTAG, "failed to parse imgUrl: " + e.getMessage());
            }
        }
    }
}
