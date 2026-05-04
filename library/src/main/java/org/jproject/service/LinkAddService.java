package org.jproject.service;

import org.jproject.dao.DaoWorker;
import org.jproject.domain.FlegFleh;
import org.jproject.domain.TLink;
import org.jproject.service.base.BaseLinkActionService;

public class LinkAddService extends BaseLinkActionService {

    public LinkAddService(DaoWorker dao, FlegFleh flegFleh) {
        super(dao, flegFleh);
    }

    @Override
    public boolean action(TLink link) {
        // TODO определить логику добавления линка
        return false;
    }
}
