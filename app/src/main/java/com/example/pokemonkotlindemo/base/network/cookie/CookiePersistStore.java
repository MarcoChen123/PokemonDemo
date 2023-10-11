package com.example.pokemonkotlindemo.base.network.cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * cookie持久化助手
 */
public class CookiePersistStore {

    static final String TAG = CookiePersistStore.class.getSimpleName();
    private static final String SP_COOKIE_PREFS = "cookie_prefs";
    private static CookiePersistStore INSTANCE;

    // SharedPreferences持久化cookie
    private SharedPreferences cookiePrefs;
    // 内存缓存的cookie，其key为host，value为该host下的cookie的键值对
    private ConcurrentHashMap<String, ConcurrentHashMap<String, Cookie>> cookiesMap;

    private CookiePersistStore() {

    }

    public static synchronized CookiePersistStore getInstance() {
        if (INSTANCE == null) {
            synchronized (CookiePersistStore.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CookiePersistStore();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Context context) {
        cookiesMap = new ConcurrentHashMap<>();
        cookiePrefs = context.getSharedPreferences(SP_COOKIE_PREFS, Context.MODE_PRIVATE);

        // 将本地持久化的cookie读到内存中
        Map<String, ?> prefs = cookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : prefs.entrySet()) {
            String host = entry.getKey();
            String[] cookieIds = TextUtils.split((String) entry.getValue(), ",");
            for (String id : cookieIds) {
                String encodedCookie = cookiePrefs.getString(id, null);
                if (encodedCookie != null) {
                    Cookie decodedCookie = decode(encodedCookie);
                    if (decodedCookie != null) {
                        addToMap(host, id, decodedCookie);
                    }
                }
            }
        }
    }

    /**
     * 保存cookie
     * @param url
     * @param cookie
     */
    public synchronized void save(HttpUrl url, Cookie cookie) {
        try {
            String host = url.host();
            String id = getCookieID(cookie);

            // 存到内存
            addToMap(host, id, cookie);

            // 存到本地
            ConcurrentHashMap<String, Cookie> temp = getFromMap(host);
            if (temp != null) {
                SharedPreferences.Editor editor = cookiePrefs.edit();
                editor.putString(host, TextUtils.join(",", temp.keySet()));
                editor.putString(id, encode(new OkHttpCookie(cookie)));
                editor.apply();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 保存cookie
     * @param url
     * @param cookies
     */
    public synchronized void saveCookies(HttpUrl url, List<Cookie> cookies) {
        try {
            String host = url.host();
            SharedPreferences.Editor editor = cookiePrefs.edit();

            for (Cookie cookie : cookies) {
                String id = getCookieID(cookie);
                // 会话结束会过期，持久化
                addToMap(host, id, cookie);
                editor.putString(id, encode(new OkHttpCookie(cookie)));
            }

            ConcurrentHashMap<String, Cookie> temp = getFromMap(host);
            if (temp != null) {
                editor.putString(host, TextUtils.join(",", temp.keySet()));
            }
            editor.apply();
        } catch (Exception e) {
        }
    }

    /**
     * 获取cookie
     * @param url
     * @return
     */
    public List<Cookie> getCookies(HttpUrl url) {
        return getCookies(url.host());
    }

    /**
     * 获取cookie
     * @param host
     * @return
     */
    public List<Cookie> getCookies(String host) {
        List<Cookie> ret = new ArrayList<>();
        ConcurrentHashMap<String, Cookie> temp = getFromMap(host);
        if (temp != null) {
            ret.addAll(temp.values());
        }
        return ret;
    }

    /**
     * 获取cookie
     * @param host
     * @param name
     * @return
     */
    public Cookie getCookie(String host, String name) {
        ConcurrentHashMap<String, Cookie> temp = getFromMap(host);
        if (temp != null) {
            for (String id : temp.keySet()) {
                String[] idArray = TextUtils.split(id, "@");
                if (name.equals(idArray[0])) {
                    return temp.get(id);
                }
            }
        }
        return null;
    }

    /**
     * 删除cookie
     * @param url
     */
    public synchronized void removeCookies(HttpUrl url) {
        try {
            String host = url.host();
            for (String key : cookiesMap.keySet()) {
                if (host.contains(key)) {
                    cookiesMap.remove(key);
                    SharedPreferences.Editor editor = cookiePrefs.edit();
                    editor.remove(key);
                    editor.apply();
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 删除所有cookie
     */
    public synchronized void removeAll() {
        try {
            SharedPreferences.Editor editor = cookiePrefs.edit();
            editor.clear();
            editor.apply();
            cookiesMap.clear();
        } catch (Exception e) {
        }
    }

    // 将cookie添加到内存中
    private void addToMap(String host, String id, Cookie cookie) {
        try {
            if (!cookiesMap.containsKey(host)) {
                cookiesMap.put(host, new ConcurrentHashMap<>());
            }
            cookiesMap.get(host).put(id, cookie);
            Log.i(TAG, "add：" + id);
        } catch (Exception e) {
        }
    }

    private ConcurrentHashMap<String, Cookie> getFromMap(String host) {
        try {
            for (String key : cookiesMap.keySet()) {
                if (host.contains(key)) {
                    return cookiesMap.get(key);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    // 编码
    private String encode(OkHttpCookie cookie) {
        if (cookie == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(cookie);
        } catch (Exception e) {
            return null;
        }
        return bytesToHexString(os.toByteArray());
    }

    // 解码
    private Cookie decode(String cookieString) {
        byte[] bytes = hexStringToBytes(cookieString);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            cookie = ((OkHttpCookie)ois.readObject()).getCookie();
        } catch (Exception e) {
        }
        return cookie;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    private byte[] hexStringToBytes(String hexString) {
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) +
                    Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    private String getCookieID(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }
}
