package org.jproject.service.base;

import org.jproject.dao.DaoWorker;
import org.jproject.domain.TFile;
import org.jproject.service.FileDeleteService;
import org.springframework.data.jpa.domain.Specification;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class BaseVacuumService implements IBaseVacuumService {

    private final DaoWorker dao;

    public BaseVacuumService(DaoWorker dao) {
        this.dao = dao;
    }

    @Override
    public int apply() {
        final List<TFile> files = dao.getFiles((Specification) null);

        int count = 0;
        for (TFile file: files) {
            final Path path = Optional.ofNullable(file.getFileHist().getPath()).orElse(null);
            if (path == null) {
                continue;
            }

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
