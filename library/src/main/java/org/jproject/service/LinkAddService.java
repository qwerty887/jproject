package org.jproject.service;

import org.jproject.dao.DaoWorker;
import org.jproject.domain.TFileGroupMember;
import org.jproject.domain.TLink;
import org.jproject.service.base.BaseLinkActionService;

public class LinkAddService extends BaseLinkActionService {

    public LinkAddService(DaoWorker dao, TFileGroupMember flegFleh) {
        super(dao, flegFleh);
    }

}
