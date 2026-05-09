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
import org.jproject.parameters.AppParameters;
import org.jproject.parameters.EAppParameters;
import org.jproject.parameters.process.FileGroupingProcessParameters;
import org.jproject.parameters.process.FileLinkingProcessParameters;
import org.jproject.service.base.BaseGroupActionService;
import org.jproject.process.base.BaseProcessActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileGroupProcess extends BaseProcessActionService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileGroupProcess.class);

    private final AppParameters appParameters;

    public FileGroupProcess(EntityManagerFactory entityManagerFactory, AppParameters appParameters) {
        super(entityManagerFactory, EProcessType.GROUP);
        this.appParameters = appParameters;
    }

    @Override
    public int action(DaoWorker dao, TProcess process) {
        final FileGroupingProcessParameters param = getParam(process.getParam(), FileGroupingProcessParameters.class);
        final List<TFile> files = dao.getFiles(param.getFiles(), DtoGroupFileParameters.class);
        final List<TFileGroup> fileGroupList = dao.getFileGroups(null); // TODO указать условие фильтрации по файлам
        final Integer fileGroupDefaultId = appParameters.get(EAppParameters.FILE_GROUP_DEFAULT_ID, Integer.class);
        final Optional<TFileGroup> fileGroupDefault = fileGroupList.stream().filter(c -> c.getId().equals(fileGroupDefaultId)).findAny();

        final List<DtoLinkFileParameters> result = new ArrayList<>();
        for (TFile file: files) {
            try {
                final List<TFileGroup> list = (new BaseGroupActionService(dao, file, fileGroupList, fileGroupDefault)).apply();
                result.add(DtoLinkFileParameters.of(file, list));
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
