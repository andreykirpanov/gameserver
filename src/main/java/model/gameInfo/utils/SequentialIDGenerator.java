package model.gameInfo.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Alpi
 * @since 31.10.16
 */
public class SequentialIDGenerator {
    private final AtomicLong current = new AtomicLong(0);
    public long next() {
        return current.getAndIncrement();
    }
}
