package org.jproject.service;

import jakarta.persistence.EntityManagerFactory;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TFile;
import org.jproject.domain.TProcess;
import org.jproject.dto.DtoGroupFileParameters;
import org.jproject.dto.DtoScanFileParameters;
import org.jproject.parameters.process.FileGroupingProcessParameters;
import org.jproject.parameters.process.FileScanningProcessParameters;
import org.jproject.service.base.BaseProcessActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileScanService extends BaseProcessActionService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileScanService.class);

    public FileScanService(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, EProcessType.FILE_SCANNING);
    }

    @Override
    public int action(DaoWorker dao, TProcess process) {
        final FileScanningProcessParameters param = getParam(process.getParam(), FileScanningProcessParameters.class);

        final Map<Path, TFile> fileMap = dao
                .getFiles(param.getFiles(), DtoScanFileParameters.class)
                .stream()
                .filter(f -> f.getFileHist() != null)
                .collect(Collectors.toMap(f -> f.getFileHist().getPath(), c -> c));

        final List<DtoGroupFileParameters> result = param.getFiles()
                .stream()
                .map(f -> {
                            TFile tFile = fileMap.get(f.getPath());
                            if (tFile == null) {
                                tFile = new FileAddService(dao, f.getPath()).apply();
                            }
                            return tFile;
                        }
                )
                .map(DtoGroupFileParameters::of)
                .toList();

        add(dao, process, new FileGroupingProcessParameters(result));
        return result.size();
    }

    @Override
    public void run() {
        logger.info("Add thread: process type = {}, thread name = {} ", getProcessType(), Thread.currentThread().getName());
        super.apply();
    }
}
