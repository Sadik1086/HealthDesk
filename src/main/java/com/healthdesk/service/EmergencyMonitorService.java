package com.healthdesk.service;

import com.healthdesk.dao.EmergencyQueueDAO;
import com.healthdesk.model.EmergencyQueue;
import javafx.application.Platform;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class EmergencyMonitorService {

    private final EmergencyQueueDAO dao = new EmergencyQueueDAO();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread t = new Thread(runnable, "healthdesk-emergency-monitor");
        t.setDaemon(true); // daemon = JVM won't wait for this thread before shutting down
        return t;
    });

    private volatile boolean running = false;

    public void startMonitoring(int intervalSeconds, Consumer<List<EmergencyQueue>> onQueueUpdated) {
        if (running) return;
        running = true;
        scheduler.scheduleWithFixedDelay(() -> {
            try {
                List<EmergencyQueue> waiting = dao.findWaiting(); // background thread reads DB
                Platform.runLater(() -> onQueueUpdated.accept(waiting)); // hand off to UI thread
            } catch (Exception e) {
                System.err.println("[EmergencyMonitor] Background scan failed: " + e.getMessage());
            }
        }, 0, intervalSeconds, TimeUnit.SECONDS);
    }

    public void stop() {
        running = false;
        scheduler.shutdownNow();
    }
}
