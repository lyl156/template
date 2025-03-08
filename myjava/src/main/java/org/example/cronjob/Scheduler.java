package org.example.cronjob;

// scheduler -> task(from builder), trigger(from builder) -> job
public interface Scheduler {
    void addCronJob(String cronExpression, boolean withSeconds, Runnable task) throws Exception;
}