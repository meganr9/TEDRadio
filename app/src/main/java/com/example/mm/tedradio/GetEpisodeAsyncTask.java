package com.example.mm.tedradio;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by M&M on 3/7/2017.
 */

public class GetEpisodeAsyncTask extends AsyncTask<String, Void, ArrayList<Episode>> {
    IData activity;
    RecyclerView recyclerView;

    public GetEpisodeAsyncTask(IData activity, RecyclerView mRecyclerView) {

        this.activity = activity;
        this.recyclerView = mRecyclerView;
    }


    @Override
    protected ArrayList<Episode> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            Log.d("demo", "statusCode: " + statusCode + ", supposed to be: " + HttpURLConnection.HTTP_OK);
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();
                return EpisodeUtil.EpisodePullParser.parseEpisodes(in);
            }
        } catch (MalformedURLException e) {
            Log.d("demo", "doInBackground: " + e.getMessage());
            e.printStackTrace();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.d("demo", "doInBackground: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("demo", "doInBackground: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(ArrayList<Episode> episodes) {
        super.onPostExecute(episodes);
        activity.EpisodeResults(episodes);

    }

    public interface IData {
        public void EpisodeResults(ArrayList<Episode> results);
    }
}

