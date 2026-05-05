package org.jproject.service;

import org.jproject.dao.DaoWorker;
import org.jproject.domain.FileGroupHist;
import org.jproject.domain.TLink;
import org.jproject.service.base.BaseLinkActionService;

public class LinkAddService extends BaseLinkActionService {

    public LinkAddService(DaoWorker dao, FileGroupHist flegFleh) {
        super(dao, flegFleh);
    }

    @Override
    public boolean action(TLink link) {
        // TODO определить логику добавления линка
        return false;
    }
}
