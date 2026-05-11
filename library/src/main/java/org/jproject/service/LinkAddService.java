package org.jproject.service;

import org.jproject.dao.Dao;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.service.base.BaseLinkActionService;

import java.util.List;

public class LinkAddService extends BaseLinkActionService {

    public LinkAddService(Dao dao, TFile file, List<TFileGroup> fileGroupList) {
        super(dao, file, fileGroupList);
    }

}
