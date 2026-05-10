package org.jproject.service.base;

import org.jproject.dao.DaoWorker;
import org.jproject.domain.TFileGroupMember;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TFileHist;
import org.jproject.domain.TLink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseLinkActionService implements IBaseLinkActionService {

    private static final Logger logger = LoggerFactory.getLogger(BaseLinkActionService.class);

    private final DaoWorker dao;

    private final TFileGroup fileGroup;
    private final TFileHist fileHist;

    public BaseLinkActionService(DaoWorker dao, TFileGroupMember flegFleh) {
        logger.debug("Link service: init");

        this.dao = dao;
        this.fileGroup = null;
        this.fileHist = null;
    }

    @Override
    public TLink apply() {
        logger.debug("Link service: start");

        if (this.fileHist == null) {
            logger.debug("Nothing to change: file hist is null");
            return null;
        }

        logger.debug("Link service: complete");

        return null;
    }

    @Override
    public boolean action(TLink link) {
        throw new RuntimeException("You need to override the run method");
    }

}