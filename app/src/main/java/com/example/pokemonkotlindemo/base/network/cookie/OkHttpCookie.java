package com.example.pokemonkotlindemo.base.network.cookie;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import okhttp3.Cookie;

/**
 * 序列化读写的cookie
 */
class OkHttpCookie implements Serializable {
    private transient final Cookie oldCookie;
    private transient Cookie newCookie;

    public OkHttpCookie(Cookie cookie) {
        this.oldCookie = cookie;
    }

    public Cookie getCookie() {
        Cookie ret = oldCookie;
        if (newCookie != null) {
            ret = newCookie;
        }
        return ret;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(oldCookie.name());
        out.writeObject(oldCookie.value());
        out.writeLong(oldCookie.expiresAt());
        out.writeObject(oldCookie.domain());
        out.writeObject(oldCookie.path());
        out.writeBoolean(oldCookie.secure());
        out.writeBoolean(oldCookie.httpOnly());
        out.writeBoolean(oldCookie.hostOnly());
        out.writeBoolean(oldCookie.persistent());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        long expiresAt = in.readLong();
        String domain = (String) in.readObject();
        String path = (String) in.readObject();
        boolean secure = in.readBoolean();
        boolean httpOnly = in.readBoolean();
        boolean hostOnly = in.readBoolean();
        boolean persistent = in.readBoolean();
        Cookie.Builder builder = new Cookie.Builder()
                .name(name)
                .value(value)
                .expiresAt(expiresAt)
                .path(path);
        if (hostOnly) {
            builder.hostOnlyDomain(domain);
        } else {
            builder.domain(domain);
        }
        if (secure) {
            builder.secure();
        }
        if (httpOnly) {
            builder.httpOnly();
        }
        newCookie = builder.build();
    }

}
