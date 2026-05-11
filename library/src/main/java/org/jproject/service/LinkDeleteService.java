package org.jproject.service;

import org.jproject.dao.Dao;
import org.jproject.domain.TFileGroupMember;
import org.jproject.service.base.BaseLinkActionService;

public class LinkDeleteService extends BaseLinkActionService {

    public LinkDeleteService(Dao dao, TFileGroupMember flegFleh) {
        super(dao, flegFleh);
    }

}
