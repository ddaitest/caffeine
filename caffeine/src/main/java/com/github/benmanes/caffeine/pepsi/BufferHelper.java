package com.github.benmanes.caffeine.pepsi;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BufferHelper {
    static final BufferHelper instance = new BufferHelper();

    private static final ConcurrentHashMap<String, Integer> mockProbePool = new ConcurrentHashMap<>();

    private static final AtomicInteger probeGenerator = new AtomicInteger();
    /**
     * The increment for generating probe values.
     */
    private static final int PROBE_INCREMENT = 0x9e3779b9;

    public static BufferHelper current() {
        String threadName = Thread.currentThread().toString();
        instance.localInit(threadName);
        return instance;
    }

    public static int getProbe() {
        return instance.get(Thread.currentThread().toString());
    }

    public static void setProbe(int newValue) {
        instance.add(Thread.currentThread().toString(), newValue);
    }

    private void localInit(String threadName) {
        int p = probeGenerator.addAndGet(PROBE_INCREMENT);
        int probe = (p == 0) ? 1 : p; // skip 0
        mockProbePool.put(threadName, probe);
    }

    private void add(String threadName, int value) {
        mockProbePool.put(threadName, value);
    }

    private int get(String threadName) {
        return mockProbePool.getOrDefault(threadName, 0);
    }
}
