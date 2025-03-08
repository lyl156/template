package org.example.cronjob;

import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {
        QuartzScheduler quartzScheduler = new QuartzScheduler();
        new RealService(quartzScheduler);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                QuartzScheduler.shutdown();
            } catch (Exception e) {
                System.err.println("Failed to shutdown scheduler: " + e.getMessage());
            }
        }));

        // 让主线程阻塞
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
