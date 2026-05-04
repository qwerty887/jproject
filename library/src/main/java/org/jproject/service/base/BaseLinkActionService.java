package org.jproject.service.base;

import org.apache.commons.io.FilenameUtils;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EFileStatus;
import org.jproject.domain.FlegFleh;
import org.jproject.domain.FlehLnk;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TFileHist;
import org.jproject.domain.TLink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class BaseLinkActionService implements IBaseLinkActionService {

    private static final Logger logger = LoggerFactory.getLogger(BaseLinkActionService.class);

    private final DaoWorker dao;

    private final TFileGroup fileGroup;
    private final TFileHist fileHist;

    public BaseLinkActionService(DaoWorker dao, FlegFleh flegFleh) {
        logger.debug("Link service: init");

        this.dao = dao;
        this.fileGroup = flegFleh.getFileGroup();
        this.fileHist = flegFleh.getFileHist();
    }

    @Override
    public TLink apply() {
        logger.debug("Link service: start");

        if (this.fileHist == null) {
            logger.debug("Nothing to change: file hist is null");
            return null;
        }

        if (this.fileHist.getFileStatus().equals(EFileStatus.INVALID)) {
            logger.debug("Nothing to change: file status: {}", EFileStatus.INVALID);
            return null;
        }

        final TLink result = createLink();
        logger.debug("Link service: complete");

        return result;
    }

    @Override
    public boolean action(TLink link) {
        throw new RuntimeException("You need to override the run method");
    }

    private TLink createLink() {
        final String linkFolder = fileGroup.getLinkPath();
        final String fileName = FilenameUtils.getName(fileHist.getPath().toAbsolutePath().toString());
        final Path linkPath = Path.of(FilenameUtils.concat(linkFolder, fileName));

        final TLink link = new TLink();
        link.setPath(linkPath);
        this.dao.persist(link);

        final FlehLnk.PK pk = new FlehLnk.PK();
        pk.setFlehFlehId(fileHist.getId());
        pk.setLnkLnkId(link.getId());

        final FlehLnk flehLnk = new FlehLnk();
        flehLnk.setId(pk);
        dao.persist(flehLnk);

        action(link);

        return link;
    }
}