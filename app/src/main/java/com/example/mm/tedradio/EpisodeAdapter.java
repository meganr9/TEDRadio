package com.example.mm.tedradio;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by M&M on 3/7/2017.
 */

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {
    private static Context context;
    private static Boolean isGrid;
    private ArrayList<Episode> mData;


    EpisodeAdapter(Context context, ArrayList<Episode> mData) {
        this.mData = mData;
        EpisodeAdapter.context = context;
        isGrid = false;
    }

    EpisodeAdapter(Context context, ArrayList<Episode> episodes, boolean isGrid) {
        this(context, episodes);
        EpisodeAdapter.isGrid = isGrid;
    }

    private Context getContext() {
        return context;
    }

    public EpisodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = isGrid ? inflater.inflate(R.layout.grid_item_layout, parent, false)
                : inflater.inflate(R.layout.row_item_layout, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Episode episode = mData.get(position);
        TextView textView = holder.mTextView;
        TextView dateText = holder.dateTextView;
        ImageView imageView = holder.imageView;
        ImageView playBtn = holder.play;
        LinearLayout layout = holder.layout;

        textView.setText(episode.getTitle());
        if (dateText != null) {
            String date = episode.getPubDate();
            date = date.substring(0, date.lastIndexOf(" "));
            date = date.substring(0, date.lastIndexOf(" "));
            dateText.setText("Posted: " + date);
        }
        if (episode.getImgUrl() != null) {
            Picasso.with(context).load(episode.getImgUrl()).into(imageView);
        }

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra("EPISODE_EXTRA", episode);
                context.startActivity(intent);
            }
        });


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PlayAudio) context).playAudio(episode.getMp3Url());

            }
        });


    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    interface PlayAudio {
        void playAudio(String url);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        TextView dateTextView;
        ImageView imageView;
        ImageView play;
        LinearLayout layout;

        ViewHolder(View v) {
            super(v);
            if (isGrid) {
                mTextView = (TextView) v.findViewById(R.id.grid_title_textView);
                imageView = (ImageView) v.findViewById(R.id.grid_imageView);
                play = (ImageView) v.findViewById(R.id.play_gridbutton);
                layout = (LinearLayout) v.findViewById(R.id.gridLayout);

            } else {
                mTextView = (TextView) v.findViewById(R.id.episodeText);
                dateTextView = (TextView) v.findViewById(R.id.date);
                imageView = (ImageView) v.findViewById(R.id.episodeImage);
                play = (ImageView) v.findViewById(R.id.playButton);
                layout = (LinearLayout) v.findViewById(R.id.rowLayout);

            }

        }
    }
}
