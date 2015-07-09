package com.example.mackenziem.redditimageviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;


public class CustomImageAdapter extends ArrayAdapter<String[]> {
    private List<String[]> listOfStrings;
    private LayoutInflater layoutInflater;

    public CustomImageAdapter(Context context, int resource, List<String[]> objects) {
        super(context, resource, objects);
        listOfStrings = objects;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_net_image, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.netImageView = (NetworkImageView)convertView.findViewById(R.id.list_item_net_imageview);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        String imgUrl = getItem(position)[0];
        viewHolder.netImageView = (NetworkImageView)convertView.findViewById(R.id.list_item_net_imageview);
        viewHolder.netImageView.setImageUrl(imgUrl, VolleySingleton.getInstance().getImageLoader());

        return convertView;
    }

    private static class ViewHolder {
        public NetworkImageView netImageView;
    }
}
