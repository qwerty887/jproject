package org.jproject.service.base;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EFileStatus;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileHist;
import org.jproject.domain.TFileHist_;
import org.jproject.domain.TFile_;
import org.jproject.service.FileDeleteService;
import org.springframework.data.jpa.domain.Specification;

import java.nio.file.Path;
import java.util.List;

public class BaseVacuumService implements IBaseVacuumService {

    private final DaoWorker dao;

    public BaseVacuumService(DaoWorker dao) {
        this.dao = dao;
    }


    private Specification<TFile> getFileSpec() {
        return (root, query, cb) -> {
            final Join<TFile, TFileHist> joinHist = dao.getOrCreateJoin(root, TFile_.fileHist, JoinType.LEFT);
            return joinHist.get(TFileHist_.fileStatus).in(EFileStatus.ADDED);
        };
    }

    @Override
    public int apply() {
        // TODO искать файлы только в активном статусе
        final List<TFile> files = dao.getFiles((Specification) null);

        int count = 0;
        for (TFile file: files) {
            final Path path = file.getPath();

            if (!path.toFile().exists()) {
                final FileDeleteService fileDeleteService = new FileDeleteService(dao, file);
                fileDeleteService.apply();
                count++;
            }
        }

        // TODO удаление связок с группами
        // TODO удаление связок с линками
        // TODO удаление линков
        return count;
    }
}
