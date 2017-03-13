package com.example.mm.tedradio;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by M&M on 3/7/2017.
 */

public class EpisodeUtil {

    static public class EpisodePullParser {
        static ArrayList<Episode> parseEpisodes(InputStream in) {
            XmlPullParser parser = null;
            ArrayList<Episode> episodeList = null;
            try {
                parser = XmlPullParserFactory.newInstance().newPullParser();

                parser.setInput(in, "UTF-8");
                Episode episode = null;
                episodeList = new ArrayList<Episode>();
                int event = parser.getEventType();
                Boolean isInActualEpisode = false;
                String imageUrl = "";
                while (event != XmlPullParser.END_DOCUMENT) {

                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("item")) {
                                isInActualEpisode = true;
                                episode = new Episode();
                            }
                            if (isInActualEpisode) {
                                if (parser.getName().equals("title")) {
                                    episode.setTitle(parser.nextText().trim());
                                } else if (parser.getName().equals("description")) {
                                    episode.setDescription(parser.nextText().trim());
                                } else if (parser.getName().equals("pubDate")) {
                                    episode.setPubDate(parser.nextText().trim());
                                } else if (parser.getName().equals("itunes:image")) {
                                    episode.setImgUrl(parser.getAttributeValue(null,"href").trim());
                                } else if (parser.getName().equals("itunes:duration")) {
                                    episode.setDuration(parser.nextText().trim());
                                } else if (parser.getName().equals("enclosure")) {
                                    episode.setMp3Url(parser.getAttributeValue(null, "url").trim());
                                }
                            }

                            break;
                        case XmlPullParser.END_TAG:
                            if (isInActualEpisode && parser.getName().equals("item")) {
                                episodeList.add(episode);
//                                Log.d("demo", episode.toString());
                                isInActualEpisode = false;
                                episode = null;
                            }
                            break;
                        default:
                            break;
                    }

                    event = parser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return episodeList;
        }

    }
}
