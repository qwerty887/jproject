package org.jproject.service;

import org.jproject.dao.Dao;
import org.jproject.domain.TFile;
import org.jproject.service.base.BaseFileActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileDeleteService extends BaseFileActionService {

    private static final Logger logger = LoggerFactory.getLogger(FileDeleteService.class);

    private final TFile file;

    public FileDeleteService(Dao dao, TFile file) {
        super(dao, file);
        this.file = file;
    }

    @Override
    public TFile apply() {
        logger.debug("File service: start: path " + file.getPath());
        super.closeFileHist(this.file);
        logger.debug("File service: complete");
        return this.file;
    }

}
