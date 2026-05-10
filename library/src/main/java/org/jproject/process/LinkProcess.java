package org.jproject.process;

import jakarta.persistence.EntityManagerFactory;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TFileGroupMember;
import org.jproject.domain.TProcess;
import org.jproject.dto.parameters.DtoLinkFileParameters;
import org.jproject.parameters.process.FileLinkingProcessParameters;
import org.jproject.process.base.BaseProcessActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

public class LinkProcess extends BaseProcessActionService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(LinkProcess.class);

    public LinkProcess(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, EProcessType.LINK);
    }

    @Override
    public int action(DaoWorker dao, TProcess process) {
        final FileLinkingProcessParameters param = getParam(process.getParam(), FileLinkingProcessParameters.class);
        final List<Path> pathList = param.getFiles().stream().map(DtoLinkFileParameters::getPath).toList();
        final List<TFileGroupMember> fileGroupMemberList = dao.getFileGroupMembers(pathList);


        // final List<FlegFleh> flegFlehList = dao.getFlegFleh(param.getFiles());
        return 0;
        /*
        final FileLinkingProcessParameters param = getParam(process.getParam(), FileLinkingProcessParameters.class);
        final List<FlegFleh> flegFlehList = dao.getFlegFleh(param.getFiles());

        for (FlegFleh flegFleh: flegFlehList) {
            (new LinkAddService(dao, flegFleh)).apply();
        }

        return flegFlehList.size();
        */
    }

    @Override
    public void run() {
        logger.info("Add thread: process type = {}, thread name = {} ", getProcessType(), Thread.currentThread().getName());
        super.apply();
    }
}
