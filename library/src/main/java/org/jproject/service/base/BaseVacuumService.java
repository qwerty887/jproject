package org.jproject.service.base;

import org.jproject.dao.DaoWorker;
import org.jproject.domain.TFile;
import org.jproject.service.FileDeleteService;
import org.jproject.service.GroupDeleteService;
import org.springframework.data.jpa.domain.Specification;

import java.nio.file.Path;
import java.util.List;

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
            final Path path = file.getPath();

            if (!path.toFile().exists()) {
                (new FileDeleteService(dao, file)).apply();
                (new GroupDeleteService(dao, file)).apply();
                count++;
            }
        }

        // TODO удаление связок с линками
        // TODO удаление линков
        return count;
    }
}
