package org.jproject.process;

import jakarta.persistence.EntityManagerFactory;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EProcessStatus;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TProcess;
import org.jproject.parameters.process.base.BaseProcessParam;
import org.jproject.process.base.BaseProcessActionService;
import org.jproject.service.base.BaseVacuumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VacuumProcess extends BaseProcessActionService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(VacuumProcess.class);

    public VacuumProcess(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, EProcessType.VACUUM);
    }

    @Override
    public <T extends BaseProcessParam> TProcess add(DaoWorker dao, T param) {
        List<Integer> prcdList = dao.getProcesses(List.of(EProcessType.VACUUM), List.of(EProcessStatus.WAIT, EProcessStatus.LOCK))
                                .stream().map(TProcess::getId).toList();

        if (prcdList.size() > 0) {
            throw new RuntimeException("Process " + prcdList + " not complete yet");
        }

        return add(dao, null, param);
    }

    @Override
    public int action(DaoWorker dao, TProcess process) {
        return (new BaseVacuumService(dao)).apply();
    }

    @Override
    public void run() {
        logger.info("Add thread: process type = {}, thread name = {} ", getProcessType(), Thread.currentThread().getName());
        super.apply();
    }

}
