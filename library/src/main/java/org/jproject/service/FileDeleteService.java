package org.jproject.service;

import org.jproject.dao.DaoWorker;
import org.jproject.domain.EFileStatus;
import org.jproject.domain.TFile;
import org.jproject.service.base.BaseFileActionService;

import java.nio.file.Path;
import java.util.List;

public class FileDeleteService extends BaseFileActionService {

    private final DaoWorker dao;

    public FileDeleteService(DaoWorker dao, TFile file) {
        super(dao, file, EFileStatus.DELETE);
        this.dao = dao;
    }

    @Override
    public void action(TFile tFile) {
        dao.deleteFlegFleh(List.of(tFile));
        // TODO удалить связку с линком
    }
}
