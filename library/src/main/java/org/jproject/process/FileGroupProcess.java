package org.jproject.process;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.NotSupportedException;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TProcess;
import org.jproject.dto.parameters.DtoGroupFileParameters;
import org.jproject.dto.parameters.DtoLinkFileParameters;
import org.jproject.exception.NotSupportExceptionApp;
import org.jproject.parameters.process.FileGroupingProcessParameters;
import org.jproject.parameters.process.FileLinkingProcessParameters;
import org.jproject.service.GroupAddService;
import org.jproject.process.base.BaseProcessActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FileGroupProcess extends BaseProcessActionService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileGroupProcess.class);

    public FileGroupProcess(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, EProcessType.GROUP);
    }

    @Override
    public int action(DaoWorker dao, TProcess process) {
        final FileGroupingProcessParameters param = getParam(process.getParam(), FileGroupingProcessParameters.class);
        final List<TFile> files = dao.getFiles(param.getFiles(), DtoGroupFileParameters.class);
        final List<TFileGroup> fileGroupList = dao.getFileGroups(null);

        final List<DtoLinkFileParameters> result = new ArrayList<>();
        for (TFile file: files) {
            try {
                (new GroupAddService(dao, file, fileGroupList)).apply();
                result.add(DtoLinkFileParameters.of(file));
            } catch (NotSupportedException e) {
                e.printStackTrace();
                throw new NotSupportExceptionApp(e.getMessage());
            }
        }

        add(dao, process, new FileLinkingProcessParameters(result));
        return result.size();
    }

    @Override
    public void run() {
        logger.info("Add thread: process type = {}, thread name = {} ", getProcessType(), Thread.currentThread().getName());
        super.apply();
    }
}
