package com.example.pokemonkotlindemo.base.network.request;

import android.content.Context;


import com.example.pokemonkotlindemo.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ApiManager {
    private static ApiManager instance;

    private Context mContext;
    private final HashMap<String, String> hosts = new HashMap<>();

    public static ApiManager getInstance() {
        if (instance == null) {
            synchronized (ApiManager.class) {
                if (instance == null) {
                    instance = new ApiManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        parseHostsMeta();
    }

    /**
     * 获取主域名
     * @return
     */
    public String getMainHost() {
        return hosts.get("api");
    }

    private void parseHostsMeta() {
        InputStream is = null;
        try {
            is = mContext.getResources().openRawResource(R.raw.meta_hosts);
            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            parser.setInput(is, "utf-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                boolean isOver = false;
                if (eventType == XmlPullParser.START_TAG) {
                    String envTag = parser.getName();
                    if (envTag.equals(AppConfig.getEnv())) {
                        isOver = true;
                    }
                    if (isOver) {
                        eventType = parser.next();
                        while (true) {
                            try {
                                if (eventType == XmlPullParser.END_TAG && envTag.equals(parser.getName())) {
                                    break;
                                }
                                if (eventType == XmlPullParser.START_TAG) {
                                    String name = parser.getAttributeValue(null, "name");
                                    String host = parser.nextText();
                                    hosts.put(name, host);
                                }
                                eventType = parser.next();
                            } catch (Exception e) {
                                break;
                            }
                        }
                    }
                }
                if (isOver) {
                    break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
