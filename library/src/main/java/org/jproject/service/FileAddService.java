package org.jproject.service;

import org.jproject.dao.Dao;
import org.jproject.domain.TFile;
import org.jproject.service.base.BaseFileActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class FileAddService extends BaseFileActionService {

    private static final Logger logger = LoggerFactory.getLogger(FileAddService.class);

    private final Path path;

    public FileAddService(Dao dao, Path path) {
        super(dao, path);
        this.path = path;
    }

    @Override
    public TFile apply() {
        logger.debug("File service: start: path " + this.path);
        final TFile result = super.updateFile(this.path);
        logger.debug("File service: complete");
        return result;
    }

}
