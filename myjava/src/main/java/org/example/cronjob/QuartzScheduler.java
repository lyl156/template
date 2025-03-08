package org.example.cronjob;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;



public class QuartzScheduler implements Scheduler {
    private static final Map<Runnable, JobKey> jobMap = new ConcurrentHashMap<>();
    private static final org.quartz.Scheduler scheduler;

    static {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to initialize scheduler", e);
        }
    }

    @Override
    public void addCronJob(String cronExpression, boolean withSeconds, Runnable task) throws Exception {
        String fullCron = withSeconds ? cronExpression : "0 " + cronExpression;

        JobDetail jobDetail = JobBuilder.newJob(TaskJob.class)
                .withIdentity(task.toString(), "cron-jobs")
                .build();

        jobDetail.getJobDataMap().put("task", task);

        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(fullCron))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        jobMap.put(task, jobDetail.getKey());
    }

    public static void shutdown() throws SchedulerException {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}
