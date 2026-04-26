package org.jproject.service;

import org.jproject.dao.DaoWorker;
import org.jproject.domain.EFileStatus;
import org.jproject.domain.TFile;
import org.jproject.service.base.BaseFileActionService;

import java.nio.file.Path;

public class FileAddService extends BaseFileActionService {

    public FileAddService(DaoWorker dao, Path path) {
        super(dao, path, EFileStatus.VALID);
    }

    @Override
    public void preAction(TFile tFile) {
        // удаляем связку с группами, соответствующей предыдущему историческому состоянию файла
        // TODO добавить удаление линка
        super.getDao().deleteFlegFleh(tFile);
    }

    @Override
    public void action(TFile tFile) {
    }
}
