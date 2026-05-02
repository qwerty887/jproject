package org.jproject.service.base;

public interface IBaseScheduleService {

    <T extends Runnable> void add(T process, int threadCount);
}
