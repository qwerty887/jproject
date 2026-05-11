package org.jproject.service.base;

import org.jproject.dao.Dao;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TLink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BaseLinkActionService implements IBaseLinkActionService {

    private static final Logger logger = LoggerFactory.getLogger(BaseLinkActionService.class);

    private final Dao dao;
    private final List<TFileGroup> fileGroupList;
    private final TFile file;
    private final TLink link;

    public BaseLinkActionService(Dao dao, TFile file, List<TFileGroup> fileGroupList) {
        logger.debug("Link service: init");

        this.dao = dao;
        this.fileGroupList = fileGroupList;
        this.file = file;
        this.link = null;
    }

    public BaseLinkActionService(Dao dao, TLink link, List<TFileGroup> fileGroupList) {
        logger.debug("Link service: init");

        this.dao = dao;
        this.fileGroupList = fileGroupList;
        this.file = null;
        this.link = link;
    }

    @Override
    public TLink apply() {
        logger.debug("Link service: start");

        logger.debug("Link service: complete");

        return null;
    }

}