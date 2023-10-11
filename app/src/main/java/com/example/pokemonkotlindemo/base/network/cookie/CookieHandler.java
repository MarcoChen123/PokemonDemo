package com.example.pokemonkotlindemo.base.network.cookie;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieHandler implements CookieJar {

    private final CookiePersistStore cookieStore;

    public CookieHandler(Context context) {
        cookieStore = CookiePersistStore.getInstance();
        cookieStore.init(context);
    }

    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        // 同步cookie
        return cookieStore.getCookies(httpUrl);
    }

    @Override
    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> cookies) {
        // cookie持久化
        Log.i(CookiePersistStore.TAG, "saveFromResponse：" + httpUrl.url());
        cookieStore.saveCookies(httpUrl, cookies);
    }

}
