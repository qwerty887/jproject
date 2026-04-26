package org.jproject.service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.NotSupportedException;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.service.base.BaseGroupActionService;
import org.jproject.utils.DaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Optional;

public class FileVisitor extends SimpleFileVisitor<Path> {

    private static final Logger logger = LoggerFactory.getLogger(FileVisitor.class);

    private final EntityManagerFactory entityManagerFactory;
    private final List<TFileGroup> fileGroups;
    private final Optional<TFileGroup> fileGroupDefault;

    public FileVisitor(EntityManagerFactory entityManagerFactory, List<TFileGroup> fileGroups, Optional<TFileGroup> fileGroupDefault) {
        super();
        this.entityManagerFactory = entityManagerFactory;
        this.fileGroups = fileGroups;
        this.fileGroupDefault = fileGroupDefault;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
        // TODO BasicFileAttributes attr прокинуть внутрь класса?
        logger.info("File processing: {}", path);
        processing(path);
        return FileVisitResult.CONTINUE;
    }

    void processing(Path path) {
        DaoUtils.withTransaction(
                () -> new DaoWorker(entityManagerFactory),
                dao -> {
                    try {
                        final TFile tFile = (new FileAddService(dao, path)).apply();
                        if (tFile.getFileHist().getError() == null) {
                            final List<TFileGroup> fileGroupList = (new BaseGroupActionService(dao, tFile, this.fileGroups, this.fileGroupDefault)).apply();
                            // TODO вызвать формирование линков
                        }
                        return tFile;
                    } catch (NotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

}
