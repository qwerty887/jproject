package org.jproject.service;

import org.jproject.dao.DaoWorker;
import org.jproject.domain.ELinkStatus;
import org.jproject.domain.TFile;
import org.jproject.domain.TLink;
import org.jproject.service.base.BaseLinkActionService;

public class LinkAddService extends BaseLinkActionService {

    public LinkAddService(DaoWorker dao, TFile file, TLink link) {
        super(dao, file, link, ELinkStatus.VALID);
    }

    @Override
    public boolean action(String linkPath) {
        // TODO определить логику добавления линка
        return false;
    }
}
