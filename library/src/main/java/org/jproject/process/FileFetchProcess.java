package org.jproject.process;

import jakarta.persistence.EntityManagerFactory;
import org.jproject.dao.Dao;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TProcess;
import org.jproject.dto.parameters.DtoFetchFileParameters;
import org.jproject.dto.parameters.DtoScanFileParameters;
import org.jproject.parameters.process.FileFetchingProcessParameters;
import org.jproject.parameters.process.FileScanningProcessParameters;
import org.jproject.exception.AppException;
import org.jproject.process.base.BaseProcessActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileFetchProcess extends BaseProcessActionService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileFetchProcess.class);
    private final Integer packSize;

    public FileFetchProcess(EntityManagerFactory entityManagerFactory, Integer packSize) {
        super(entityManagerFactory, EProcessType.FETCH);
        this.packSize = packSize;
    }

    @Override
    public int action(Dao dao, TProcess process) {
        final FileFetchingProcessParameters param = getParam(process.getParam(), FileFetchingProcessParameters.class);

        int count=0;
        for (DtoFetchFileParameters p: param.getFiles()) {
            count = count + createScanProcess(dao, process, p);
        }
        return count;
    }

    private int createScanProcess(Dao dao, TProcess process, DtoFetchFileParameters param) {
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
            e.printStackTrace();
            throw new AppException(e.getMessage());
        }
    }

    @Override
    public void run() {
        logger.info("Add thread: process type = {}, thread name = {} ", getProcessType(), Thread.currentThread().getName());
        super.apply();
    }
}
