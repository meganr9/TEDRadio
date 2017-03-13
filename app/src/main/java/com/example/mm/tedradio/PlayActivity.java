package com.example.mm.tedradio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends AppCompatActivity {
    private Episode episode;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        episode = getIntent().getExtras().getParcelable("EPISODE_EXTRA");

        ((TextView) findViewById(R.id.episodeTitle)).setText(episode.getTitle());
        ((TextView) findViewById(R.id.descriptionText)).setText(episode.getDescription());

        String date = episode.getPubDate();
        date = date.substring(0, date.lastIndexOf(" "));
        date = date.substring(0, date.lastIndexOf(" "));
        ((TextView) findViewById(R.id.dateText)).setText("Publication Date: " + date);

        ((TextView) findViewById(R.id.durationText)).setText("Duration: " + episode.getDuration() + "ms");
        Picasso.with(this).load(episode.getImgUrl()).into((ImageView) findViewById(R.id.episodePic));

    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void playButtonClicked(View view) {
            if (mediaPlayer == null) {
                String url = episode.getMp3Url();

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
                        ((ImageView) findViewById(R.id.playButton)).setImageResource(android.R.drawable.ic_media_play);
                    }
                });

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer player) {
                        player.start();

                        findViewById(R.id.progressBar_content_play).setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(R.id.playButton)).setImageResource(android.R.drawable.ic_media_pause);

                        timerTask = new TimerTask() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void run() {
                                if (PlayActivity.this.mediaPlayer.getDuration() != 0) {
                                    float num = ((float) PlayActivity.this.mediaPlayer.getCurrentPosition()) / ((float) PlayActivity.this.mediaPlayer.getDuration());
                                    ((ProgressBar) findViewById(R.id.progressBar_content_play)).setProgress((int) (100 * num));
                                }
                            }

                        };
                        timer = new Timer();
                        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
                    }

                });
            } else if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                timer.cancel();
                timer.purge();
                ((ImageView) findViewById(R.id.playButton)).setImageResource(android.R.drawable.ic_media_play);

            } else {
                mediaPlayer.start();
                try {
                    timer.scheduleAtFixedRate(timerTask, 1000, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((ImageView) findViewById(R.id.playButton)).setImageResource(android.R.drawable.ic_media_pause);
            }

    }
}
