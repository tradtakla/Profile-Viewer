package com.appsandgamesinc.progetter;


import android.graphics.Bitmap;
import android.media.Image;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;

public class ScanningActivity extends AppCompatActivity
{



    private String username = "";
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        initVars();
    }

    private void initVars()
    {
        ImageView imageView = (ImageView) findViewById(R.id.ivSearchbtn);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final EditText editText = (EditText) findViewById(R.id.etSearchBar);
                editText.setSingleLine(true);
                username = editText.getText().toString();
                LongOperation lo = new LongOperation();
                lo.execute(username);
            }
        });

    }


    private class LongOperation extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String ret="";
            try
            {
                String url = "https://www.instagram.com/"+username+"/?hl=en";
                url = url.replaceAll(" ", "_");


                Document document = Jsoup.connect(url).get();
                Elements metaElmnts = document.select("meta");

                for (Element metaElmnt : metaElmnts)
                {
                    String property = metaElmnt.attr("property");
                    if (property.equals("og:image"))
                    {
                        ret = metaElmnt.attr("content");
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        protected void onPostExecute(String result)
        {
            formatPicUrl(result);
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
        }
    }


    public void formatPicUrl(String url)
    {
        LinearLayout scanningLayout = (LinearLayout) findViewById(R.id.scanningLayout);
        LinearLayout resultLayout = (LinearLayout) findViewById(R.id.resultLayout);
        ImageView iView = (ImageView) findViewById(R.id.ivProfilePhoto);

        String newUrl = url.replace("/s150x150","");
        scanningLayout.setVisibility(View.GONE);
        resultLayout.setVisibility(View.VISIBLE);


        Picasso.with(ScanningActivity.this).load(newUrl).into(iView);
    }


//    public String getWebsite(String website)
//    {
//        String firstString = "";
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpGet httpGet = new HttpGet(website);
//        try
//        {
//            HttpResponse response;
//            response = httpClient.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//            InputStream inputStream = entity.getContent();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "windows-1251"), 8);
//            StringBuilder stringBuilder = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null)
//            {
//                stringBuilder.append(line + "\n");
//            }
//            firstString = stringBuilder.toString();
//            inputStream.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            Toast.makeText(ScanningActivity.this, "Error!", Toast.LENGTH_SHORT).show();
//        }
//
//        return firstString;
//    }

}
