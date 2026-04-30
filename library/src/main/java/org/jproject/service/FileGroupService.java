package org.jproject.service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.NotSupportedException;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TProcess;
import org.jproject.dto.DtoGroupFileParameters;
import org.jproject.exception.NotFoundExceptionApp;
import org.jproject.exception.NotSupportExceptionApp;
import org.jproject.parameters.process.FileGroupingProcessParameters;
import org.jproject.service.base.BaseGroupActionService;
import org.jproject.service.base.BaseProcessActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        final List<List<TFileGroup>> result = files.stream()
             .map(file -> {
                     try {
                         return (new BaseGroupActionService(dao, file, fileGroupList, fileGroupDefault)).apply();
                     } catch (NotSupportedException e) {
                         e.printStackTrace();
                         throw new NotSupportExceptionApp(e.getMessage());
                     }
                 }
                 )
             .toList();

        return result.size(); // TODO учёсть вложенные коллекции
    }

    @Override
    public void run() {
        logger.info("Add thread: process type = {}, thread name = {} ", getProcessType(), Thread.currentThread().getName());
        super.apply();
    }
}

/*
    @Override
    public int action(DaoWorker dao, TProcess process) {
        final FileGroupingProcessParameters param = getParam(process.getParam(), FileGroupingProcessParameters.class);
        //final List<DtoGroupFileParameters> files = param.getFiles();

        final List<TFile> tFileList = dao.getFiles(param.getFiles());

        files.stream()
             .map(p -> {
                 final String md5 = p.getMd5();
                 final Path path = p.getPath();
                 System.out.println(">>> md5 = " + md5);
                 System.out.println(">>> path = " + path);

                 final TFile tFile = dao.getFile(path, md5).orElseThrow(() -> new NotFoundExceptionApp("File not found"));

                 // TODO ИД истории оодин и тот же
                 System.out.println(">>> file_hist = " + tFile.getFileHist());
                 System.out.println(">>> fleh_id = " + tFile.getFileHist().getId());

                     try {
                         List<TFileGroup> fileGroups = (new BaseGroupActionService(dao, tFile, fileGroupList, fileGroupDefault)).apply();
                         return fileGroups;
                     } catch (NotSupportedException e) {
                         e.printStackTrace();
                         throw new NotSupportExceptionApp(e.getMessage());
                     }
                 }
                 )
             .toList();

        return files.size();
    }
 */