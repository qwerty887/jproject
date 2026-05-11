package org.jproject.service;

import org.jproject.dao.Dao;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TLink;
import org.jproject.service.base.BaseLinkActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LinkAddService extends BaseLinkActionService {

    private static final Logger logger = LoggerFactory.getLogger(LinkAddService.class);

    private final TFile file;
    private final List<TFileGroup> fileGroupList;

    public LinkAddService(Dao dao, TFile file, List<TFileGroup> fileGroupList) {
        super(dao, file, fileGroupList);
        this.file = file;
        this.fileGroupList = fileGroupList;
    }

    @Override
    public TLink apply() {
        logger.debug("Link service: start");

        logger.debug("Link service: complete");
        return null;
    }

}
