package org.jproject.service.base;

import org.jproject.parameters.AppParameters;
import org.jproject.parameters.EAppParameters;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.concurrent.TimeUnit;

public class BaseScheduleService implements IBaseScheduleService {

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final PeriodicTrigger trigger;

    public BaseScheduleService(AppParameters appParameters) {
        final Integer poolSize = appParameters.get(EAppParameters.POOL_SIZE, Integer.class);
        final Integer period = appParameters.get(EAppParameters.SCHEDULER_TIMEOUT, Integer.class);

        this.threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        this.trigger = new PeriodicTrigger(period, TimeUnit.SECONDS);
        this.threadPoolTaskScheduler.setPoolSize(poolSize);
        this.threadPoolTaskScheduler.setThreadNamePrefix("thread-");
        this.threadPoolTaskScheduler.initialize();
    }

    @Override
    public <T extends Runnable> void add(T process, int threadCount) {
        for (int i = 0; i < threadCount; i++) {
            threadPoolTaskScheduler.schedule(process, trigger);
        }
    }

    @Override
    public <T extends Runnable> void execute(T process) {
        threadPoolTaskScheduler.execute(process);
    }
}
