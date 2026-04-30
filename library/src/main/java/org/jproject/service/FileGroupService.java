package org.jproject.service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.NotSupportedException;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EProcessType;
import org.jproject.domain.FlegFleh;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TProcess;
import org.jproject.dto.DtoGroupFileParameters;
import org.jproject.dto.DtoLinkFileParameters;
import org.jproject.exception.NotSupportExceptionApp;
import org.jproject.parameters.process.FileGroupingProcessParameters;
import org.jproject.parameters.process.FileLinkingProcessParameters;
import org.jproject.service.base.BaseGroupActionService;
import org.jproject.service.base.BaseProcessActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileGroupService extends BaseProcessActionService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileGroupService.class);

    private final List<TFileGroup> fileGroupList;
    private final Optional<TFileGroup> fileGroupDefault;

    public FileGroupService(EntityManagerFactory entityManagerFactory, List<TFileGroup> fileGroupList, Optional<TFileGroup> fileGroupDefault) {
        super(entityManagerFactory, EProcessType.FILE_GROUPING);
        this.fileGroupList = fileGroupList;
        this.fileGroupDefault = fileGroupDefault;
    }

    @Override
    public int action(DaoWorker dao, TProcess process) {
        final FileGroupingProcessParameters param = getParam(process.getParam(), FileGroupingProcessParameters.class);
        final List<TFile> files = dao.getFiles(param.getFiles(), DtoGroupFileParameters.class);

        dao.deleteFlegFleh(files);

        final List<DtoLinkFileParameters> result = new ArrayList<>();
        for (TFile file: files) {
            try {
                final List<FlegFleh.PK> list = (new BaseGroupActionService(dao, file, fileGroupList, fileGroupDefault)).apply();
                result.addAll(list.stream().map(DtoLinkFileParameters::of).toList());
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
