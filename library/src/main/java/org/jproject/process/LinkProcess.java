package org.jproject.process;

import jakarta.persistence.EntityManagerFactory;
import org.jproject.dao.Dao;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TFileGroupMember;
import org.jproject.domain.TProcess;
import org.jproject.dto.parameters.DtoLinkFileParameters;
import org.jproject.parameters.process.FileLinkingProcessParameters;
import org.jproject.process.base.BaseProcessActionService;
import org.jproject.service.LinkAddService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LinkProcess extends BaseProcessActionService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(LinkProcess.class);

    public LinkProcess(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, EProcessType.LINK);
    }

    @Override
    public int action(Dao dao, TProcess process) {
        final FileLinkingProcessParameters param = getParam(process.getParam(), FileLinkingProcessParameters.class);
        final List<Path> pathList = param.getFiles().stream().map(DtoLinkFileParameters::getPath).toList();
        // TODO попробовать реализовать в SQL-запросе?
        final Map<TFile, List<TFileGroupMember>> fileGroupMap =
                        dao.getFileGroupMembers(pathList)
                        .stream()
                        .collect(Collectors.groupingBy(TFileGroupMember::getFile));

        for (Map.Entry<TFile, List<TFileGroupMember>> entry : fileGroupMap.entrySet()) {
            final TFile file = entry.getKey();
            final List<TFileGroup> fileGroupList = entry.getValue().stream().map(TFileGroupMember::getFileGroup).toList();
            (new LinkAddService(dao, file, fileGroupList)).apply();
        }

        return fileGroupMap.size();
    }

    @Override
    public void run() {
        logger.info("Add thread: process type = {}, thread name = {} ", getProcessType(), Thread.currentThread().getName());
        super.apply();
    }
}
