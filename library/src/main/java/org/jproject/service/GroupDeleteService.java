package org.jproject.service;

import jakarta.transaction.NotSupportedException;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.service.base.BaseGroupActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GroupDeleteService extends BaseGroupActionService {

    private static final Logger logger = LoggerFactory.getLogger(GroupDeleteService.class);

    private final DaoWorker dao;
    private final TFile file;

    public GroupDeleteService(DaoWorker dao, TFile file) {
        super(dao, file);
        this.dao = dao;
        this.file = file;
    }

    @Override
    public List<TFileGroup> apply() throws NotSupportedException {
        logger.debug("Group service: start");
        dao.closeGroupMember(this.file);
        logger.debug("Group service: complete");
        return null;
    }

}
