package org.jproject.service;

import jakarta.persistence.EntityManagerFactory;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TProcess;
import org.jproject.parameters.process.FileLinkingProcessParameters;
import org.jproject.service.base.BaseProcessActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkService extends BaseProcessActionService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(LinkService.class);

    public LinkService(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, EProcessType.FILE_LINKING);
    }

    @Override
    public int action(DaoWorker dao, TProcess process) {
        final FileLinkingProcessParameters param = getParam(process.getParam(), FileLinkingProcessParameters.class);

        return 0;
    }

    @Override
    public void run() {
        logger.info("Add thread: process type = {}, thread name = {} ", getProcessType(), Thread.currentThread().getName());
        super.apply();
    }
}
