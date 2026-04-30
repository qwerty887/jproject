package org.jproject.service;

import org.jproject.dao.DaoWorker;
import org.jproject.domain.EFileStatus;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileHist;
import org.jproject.exception.AppException;
import org.jproject.service.base.BaseFileActionService;

import java.util.Optional;

public class FileUpdateService extends BaseFileActionService {

    public FileUpdateService(DaoWorker dao, TFile tfile) {
        super(dao, tfile, EFileStatus.UPDATE);
    }

    @Override
    public void action(TFile tFile) {
        // проверка существования файла
        final boolean isExist = Optional.ofNullable(tFile.getFileHist())
                .map(TFileHist::getPath)
                .map(path -> path.toFile().exists())
                .orElse(false);

        if (!isExist) {
            throw new AppException("file not found");
        }
    }
}
