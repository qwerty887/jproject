package org.jproject.service;

import jakarta.transaction.NotSupportedException;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TFileGroupMember;
import org.jproject.service.base.BaseGroupActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GroupAddService extends BaseGroupActionService {

    private static final Logger logger = LoggerFactory.getLogger(GroupAddService.class);

    private final DaoWorker dao;
    private final TFile file;

    public GroupAddService(DaoWorker dao, TFile file, List<TFileGroup> tFileGroups) {
        super(dao, file, tFileGroups);
        this.dao = dao;
        this.file = file;
    }

    @Override
    public List<TFileGroup> apply() {
        try {
            logger.debug("Group service: start");
            final List<TFileGroup> matches = super.getMatches();

            for (TFileGroup fileGroup : matches) {
                for (TFileGroupMember fileGroupMember : fileGroup.getFileGroupMembers()) {
                    if (fileGroupMember.getFile().equals(this.file)) {
                        this.dao.closeGroupMember(fileGroupMember);
                    }
                }
                super.createFileGroupMember(fileGroup, this.file);
            }

            logger.debug("Group service: complete");
            return matches;
        } catch (NotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
