package org.jproject.service;

import org.jproject.dao.Dao;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TLink;
import org.jproject.service.base.BaseLinkActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LinkDeleteService extends BaseLinkActionService {

    private static final Logger logger = LoggerFactory.getLogger(LinkDeleteService.class);

    public LinkDeleteService(Dao dao, TLink link, List<TFileGroup> fileGroupList) {
        super(dao, link, fileGroupList);
    }

}
