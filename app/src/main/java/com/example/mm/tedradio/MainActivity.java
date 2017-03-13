package com.example.mm.tedradio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements GetEpisodeAsyncTask.IData, EpisodeAdapter.PlayAudio {
    ArrayList<Episode> episodes;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new GetEpisodeAsyncTask(this, mRecyclerView).execute("https://www.npr.org/rss/podcast.php?id=510298");
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.switch_views) {

            mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new EpisodeAdapter(this, episodes, true);
            mRecyclerView.setAdapter(mAdapter);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            mAdapter.notifyDataSetChanged();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void EpisodeResults(ArrayList<Episode> results) {
        episodes = results;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EpisodeAdapter(this, results);
        mRecyclerView.setAdapter(mAdapter);
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        mAdapter.notifyDataSetChanged();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void playAudio(String url) {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.audio_progressbar);
                progressBar.setProgress(0);
                progressBar.setVisibility(View.INVISIBLE);
                findViewById(R.id.pauseImage).setVisibility(View.INVISIBLE);
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();

                ((ImageView) findViewById(R.id.pauseImage)).setImageResource(android.R.drawable.ic_media_pause);
                findViewById(R.id.audio_progressbar).setVisibility(View.VISIBLE);
                findViewById(R.id.pauseImage).setVisibility(View.VISIBLE);

                timerTask = new TimerTask() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        if (MainActivity.this.mediaPlayer.getDuration() != 0) {
                            float num = ((float) MainActivity.this.mediaPlayer.getCurrentPosition()) / ((float) MainActivity.this.mediaPlayer.getDuration());
                            ((ProgressBar) findViewById(R.id.audio_progressbar)).setProgress((int) (100 * num));
                        }
                    }

                };
                timer = new Timer();
                timer.scheduleAtFixedRate(timerTask, 1000, 1000);
            }

        });

    }

    public void pauseButtonClicked(View view) {
        ImageView pauseButton = (ImageView) findViewById(R.id.pauseImage);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            timer.cancel();
            timer.purge();
            pauseButton.setImageResource(android.R.drawable.ic_media_play);
        } else {
            mediaPlayer.start();
            try {
                timer.scheduleAtFixedRate(timerTask, 1000, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            pauseButton.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

}
