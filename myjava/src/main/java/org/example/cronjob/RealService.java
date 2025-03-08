package org.example.cronjob;

import java.util.concurrent.*;

public class RealService {
    private final Scheduler scheduler;

    public RealService(Scheduler scheduler) {
        this.scheduler = scheduler;
        try {
            registerSpecificJob();
        } catch (Exception e) {
            System.err.println("unable to registerSpecificJob, err: " + e.getMessage());
        }
    }
    private void registerSpecificJob() throws Exception {
        scheduler.addCronJob("* * * *  ?", false, this::doSpecificJob);
    }

    private void doSpecificJob() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> {
            try {
                System.out.println("Executing specific job...");
                Thread.sleep(3000); // Simulate job execution
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println("After executing specific job...");

        try {
            future.get(120, TimeUnit.SECONDS); // 2 minutes timeout, 会阻塞当前线程，直到任务完成
            System.out.println("After future get ********");
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            System.err.println("Job execution failed: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
}
