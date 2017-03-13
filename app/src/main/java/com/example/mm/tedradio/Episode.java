package com.example.mm.tedradio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by M&M on 3/7/2017.
 */

public class Episode implements Parcelable{
    private String title, description, pubDate, imgUrl, duration, mp3Url;


    public Episode(){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", duration='" + duration + '\'' +
                ", mp3Url='" + mp3Url + '\'' +
                '}';
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(description);
        out.writeString(pubDate);
        out.writeString(imgUrl);
        out.writeString(duration);
        out.writeString(mp3Url);
    }

    public static final Parcelable.Creator<Episode> CREATOR
            = new Parcelable.Creator<Episode>() {
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    private Episode(Parcel in) {
        title = in.readString();
        description = in.readString();
        pubDate = in.readString();
        imgUrl = in.readString();
        duration = in.readString();
        mp3Url = in.readString();

    }

}
