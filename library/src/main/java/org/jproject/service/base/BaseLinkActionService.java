package org.jproject.service.base;

import org.apache.commons.io.FilenameUtils;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EFileStatus;
import org.jproject.domain.ELinkStatus;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TFileHist;
import org.jproject.domain.TLink;
import org.jproject.domain.TLinkHist;
import org.jproject.exception.AppException;
import org.jproject.utils.TimeUtils;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseLinkActionService implements IBaseLinkActionService {

    private static final Logger logger = LoggerFactory.getLogger(BaseLinkActionService.class);

    private final TFileHist fileHist;
    private final TLink link;
    private final DaoWorker dao;
    private final ELinkStatus targetStatus;

    private final TFile file;
    private final TFileGroup fileGroup;
    private final String linkFolderPath;
    private final String linkPath;

    public BaseLinkActionService(DaoWorker dao, TFile file, TLink link, ELinkStatus targetStatus) {
        logger.debug("Link service: init");

        this.dao = dao;
        this.link = link;
        this.targetStatus = targetStatus;

        this.file = file;
        this.fileHist = file.getFileHist();
        // TODO исправить
        //this.fileGroupMember = getFileGroupMember(fileHist);
        //this.fileGroup = getFileGroup(fileGroupMember);
        //this.linkFolderPath = getLinkFolderPath(fileGroup);
        //this.linkPath = getLinkPath(fileHist, linkFolderPath);
        //this.fileGroupMember = null;
        this.fileGroup = null;
        this.linkFolderPath = null;
        this.linkPath = null;
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

        final TLink result = updateLink(this.link);
        logger.debug("Link service: complete");

        return result;
    }

    @Override
    public boolean action(String linkPath) {
        throw new RuntimeException("You need to override the run method");
    }

    private TFile getFile(TFileHist fileHist) {
        final TFile result = Optional.ofNullable(fileHist).map(TFileHist::getFile)
                .orElseThrow(() -> new RuntimeException("File not found"));
        logger.debug("Get file: fle_id = {}", result.getId());
        return result;
    }

    /*
    private TFileGroupMember getFileGroupMember(TFileHist fileHist) {
        final TFileGroupMember result = Optional.ofNullable(fileHist).map(TFileHist::getFileGroupMember)
                .orElseThrow(() -> new RuntimeException("File group member not found"));
        logger.debug("Get file group member: flgm_id = {}", result.getId());
        return result;
    }
    */

    /*
    private TFileGroup getFileGroup(TFileGroupHist fileGroupMember) {
        final TFileGroup result = Optional.ofNullable(fileGroupMember).map(TFileGroupHist::getFileGroup)
                .orElseThrow(() -> new RuntimeException("File group not found"));
        logger.debug("Get file group: fleg_id = {}", result.getId());
        return result;
    }
    */

    private String getLinkFolderPath(TFileGroup fileGroup) {
        final String result = Optional.ofNullable(fileGroup).map(TFileGroup::getLinkPath)
                .orElseThrow(() -> new RuntimeException("Link path not found"));
        logger.debug("Get link folder path: {}", result);
        return result;
    }

    private String getLinkPath(TFileHist fileHist, String linkFolderPath) {
        final String result = FilenameUtils.concat(linkFolderPath, FilenameUtils.getName(fileHist.getPath().toFile().getAbsolutePath())); // TODO имя файла нормально как-то найти
        logger.debug("Get link path: {}", result);
        return result;
    }

    private TLink updateLink(TLink link) {
        if (link != null) {
            logger.debug("Action: link updated: lnk_id = {}", link.getId());

            final TLinkHist linkHist = link.getLinkHist();

            if (linkHist != null) {
                this.dao.closeLinkHist(linkHist);
            }

            return link.setLinkHist(createLinkHist(link, this.linkPath));
        } else {
            if (!this.targetStatus.equals(ELinkStatus.DELETE)) {
                final TLink result = this.dao.getLink(this.file).orElse(createLink(this.file, this.linkPath));
                logger.debug("Action: link created: lnk_id = {}", result.getId());
                return result;
            } else {
                logger.debug("Nothing to change: link not found, target status: {}", ELinkStatus.DELETE);
                return null;
            }
        }
    }

    private TLink createLink(TFile file, String linkPath) {
        final TLink link = new TLink();
        link.setFile(file);
        this.dao.persist(link);

        final TLinkHist linkHist = createLinkHist(link, linkPath);
        return link.setLinkHist(linkHist);
    }

    private TLinkHist createLinkHist(TLink link, String linkPath) {
        final TLinkHist linkHist = new TLinkHist();

        try {
            linkHist.setLink(link);
            linkHist.setPath(linkPath);
            linkHist.setStartDate(TimeUtils.getCurrentTime());
            linkHist.setEndDate(TimeUtils.getMaxTime());
            linkHist.setLinkStatus(this.targetStatus);
            action(linkPath);
        } catch (AppException e) {
            linkHist.setLinkStatus(ELinkStatus.INVALID);
        }

        return this.dao.persist(linkHist);
    }
}