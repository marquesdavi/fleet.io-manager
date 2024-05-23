package com.api.manager.fleet.conf;

import org.hibernate.Session;

public class AutoClosableSession implements AutoCloseable{
    private final Session session;

    public AutoClosableSession(Session session) {
        this.session = session;
    }

    public Session delegate() {
        return session;
    }

    @Override
    public void close() {
        session.close();
    }
}
