package com.api.manager.fleet.conf;

import com.api.manager.fleet.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class AutoClosableSession implements AutoCloseable{
    private final Session session;
    private static final Logger logger = LoggerFactory.getLogger(CustomerRepository.class);

    public Session delegate() {
        logger.info("Session opened!");
        return session;
    }

    @Override
    public void close() {
        session.close();
        logger.info("Session closed!");
    }
}
