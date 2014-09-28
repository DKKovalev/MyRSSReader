package com.example.MyRSSReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.widget.AdapterView.OnItemClickListener;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {

    ListView viewNews;
    ArrayList<RSSItem> newsItems;

    String url;

    LinearLayout linearLayout;


    public static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        viewNews = (ListView) findViewById(R.id.news_list);

        newsItems = new ArrayList<RSSItem>();

        linearLayout = (LinearLayout)findViewById(R.id.my_layout);

        linearLayout.setBackgroundColor((int)Math.random());



        viewNews.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RSSItem item = newsItems.get(position);
                // start browser with the url
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.url));
                startActivity(browserIntent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        GetAndParseRSSDataTask getAndParseRSSDataTask;

        switch (item.getItemId()) {
            case R.id.action_bbc:
                url = "http://feeds.bbci.co.uk/news/rss.xml";

                newsItems.clear();

                getAndParseRSSDataTask = new GetAndParseRSSDataTask(newsItems);
                getAndParseRSSDataTask.execute(url);

                return true;
            case R.id.action_kotaku:
                url = "http://feeds.gawker.com/kotaku/full";

                newsItems.clear();

                getAndParseRSSDataTask = new GetAndParseRSSDataTask(newsItems);
                getAndParseRSSDataTask.execute(url);

                return true;
            case R.id.action_pcworld:
                url = "http://www.pcworld.com/index.rss";

                newsItems.clear();

                getAndParseRSSDataTask = new GetAndParseRSSDataTask(newsItems);
                getAndParseRSSDataTask.execute(url);

                return true;
            case R.id.action_sg:
                url = "http://stopgame.ru/rss/rss_vreview.xml";

                newsItems.clear();

                getAndParseRSSDataTask = new GetAndParseRSSDataTask(newsItems);
                getAndParseRSSDataTask.execute(url);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class GetAndParseRSSDataTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        ArrayList<RSSItem> items = null;

        @Override
        protected void onPreExecute() {
            this.progressDialog.setMessage("Loading");
            this.progressDialog.show();
        }

        public GetAndParseRSSDataTask(ArrayList<RSSItem> items) {
            this.items = items;
        }

        private String getNodeValue(Element entry, String tag) {
            Element tagElement = (Element) entry.getElementsByTagName(tag).item(0);
            return tagElement.getFirstChild().getNodeValue();
        }

        private String downloadAndParseXML(String url) {
            try {
                InputStream inputStream = new URL(url).openStream();

                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document dom = documentBuilder.parse(inputStream);
                Element documentElement = dom.getDocumentElement();

                NodeList nodes = documentElement.getElementsByTagName("item");

                for (int i = 0; i < nodes.getLength(); i++) {
                    Element entry = (Element) nodes.item(i);

                    String title = getNodeValue(entry, "title");
                    String description = getNodeValue(entry, "description");
                    String link = getNodeValue(entry, "link");

                    items.add(new RSSItem(title, description, link));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected String doInBackground(String... urls) {
            if (urls.length <= 0)
                return null;
            return downloadAndParseXML(urls[0]);
        }

        protected void onPostExecute(String result) {

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            updateList(items);
        }


    }

    public void updateList(ArrayList<RSSItem> items) {
        RSSAdapter rssAdapter = new RSSAdapter(this, R.layout.news_item, items);
        viewNews.setAdapter(rssAdapter);
    }

}