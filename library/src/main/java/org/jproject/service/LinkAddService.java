package org.jproject.service;

import org.jproject.dao.Dao;
import org.jproject.domain.TFileGroupMember;
import org.jproject.service.base.BaseLinkActionService;

public class LinkAddService extends BaseLinkActionService {

    public LinkAddService(Dao dao, TFileGroupMember flegFleh) {
        super(dao, flegFleh);
    }

}
