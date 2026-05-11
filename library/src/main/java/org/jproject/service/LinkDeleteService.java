package org.jproject.service;

import org.jproject.dao.Dao;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TLink;
import org.jproject.service.base.BaseLinkActionService;

import java.util.List;

public class LinkDeleteService extends BaseLinkActionService {

    public LinkDeleteService(Dao dao, TLink link, List<TFileGroup> fileGroupList) {
        super(dao, link, fileGroupList);
    }

}
