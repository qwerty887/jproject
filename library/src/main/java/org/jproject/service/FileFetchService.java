package org.jproject.service;

import jakarta.persistence.EntityManagerFactory;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TProcess;
import org.jproject.dto.parameters.DtoScanFileParameters;
import org.jproject.parameters.process.FileFetchingProcessParameters;
import org.jproject.parameters.process.FileScanningProcessParameters;
import org.jproject.exception.AppException;
import org.jproject.service.base.BaseProcessActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileFetchService extends BaseProcessActionService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileFetchService.class);
    private final Integer packSize = 1000; // TODO вынести в параметры

    public FileFetchService(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, EProcessType.FILE_FETCHING);
    }

    @Override
    public int action(DaoWorker dao, TProcess process) {
        final FileFetchingProcessParameters param = getParam(process.getParam(), FileFetchingProcessParameters.class);
        try {
            // TODO правила группировки:
            // TODO файлы с одинаковым размеров в одну пачку, чтобы снизить количество возникающих ошибок unique constraint
            // TODO постараться сформировать пачки с одинаковым суммарным размером файлов
            final List<DtoScanFileParameters> params = Files
                    .walk(param.getPath())
                    .filter(Files::isRegularFile)
                    .filter(path -> !path.toAbsolutePath().equals(param.getExcludePath()))
                    .map(Path::toAbsolutePath)
                    .map(DtoScanFileParameters::of)
                    .toList();

            Integer tempSize = 0;
            final List<DtoScanFileParameters> tempList = new ArrayList<>();
            for (DtoScanFileParameters dtoScanFileParameter : params) {
                logger.debug("Fetch file {}", dtoScanFileParameter.getPath());
                tempSize++;
                tempList.add(dtoScanFileParameter);
                if (tempSize >= packSize) {
                    tempSize = 0;
                    add(dao, process, new FileScanningProcessParameters(tempList));
                    tempList.clear();
                }
            }
            add(dao, process, new FileScanningProcessParameters(tempList));

            return params.size();
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }

    @Override
    public void run() {
        logger.info("Add thread: process type = {}, thread name = {} ", getProcessType(), Thread.currentThread().getName());
        super.apply();
    }
}
