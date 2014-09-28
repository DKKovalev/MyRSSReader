package com.example.MyRSSReader;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by PsichO on 22.09.2014.
 */
public class RSSAdapter extends ArrayAdapter<RSSItem> {

    Context context;
    ArrayList<RSSItem> newsItems;

    LayoutInflater inflater;
    RSSItem newsItem;

    Typeface customFont;

    public RSSAdapter(Context context, int textViewResourceId, ArrayList<RSSItem> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.newsItems = objects;
    }

    public View getView(int pos, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {

            //customFont = Typeface.createFromAsset(MainActivity.context.getAssets(), "fonts/font.ttf");
            //customFont = Typeface.createFromAsset(MainActivity.context.getAssets(), "fonts/rockwell.ttf");

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.news_item, null);
            viewHolder = new ViewHolder();
            viewHolder.descriptionText = (TextView) convertView.findViewById(R.id.item_description);
            viewHolder.titleText = (TextView) convertView.findViewById(R.id.item_title);

            viewHolder.titleText.setTypeface(customFont);
            viewHolder.descriptionText.setTypeface(customFont);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        newsItem = newsItems.get(pos);
        if (newsItem != null) {
            viewHolder.titleText.setText(newsItem.title);
            viewHolder.descriptionText.setText(newsItem.description);
        }

        return convertView;
    }

    class ViewHolder {
        TextView titleText;
        TextView descriptionText;
    }
}