package org.jproject.process;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.NotSupportedException;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TFileGroup;
import org.jproject.parameters.AppParameters;
import org.jproject.parameters.EAppParameters;
import org.jproject.service.base.BaseScheduleService;
import org.jproject.utils.DaoUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleProcess extends BaseScheduleService {

    private final EntityManagerFactory entityManagerFactory;
    private final AppParameters appParameters;

    public ScheduleProcess(EntityManagerFactory entityManagerFactory, AppParameters appParameters) throws NotSupportedException {
        super(appParameters);
        this.appParameters = appParameters;
        this.entityManagerFactory = entityManagerFactory;

        add(EProcessType.FILE_FETCHING);
        add(EProcessType.FILE_SCANNING);
        add(EProcessType.FILE_GROUPING);
        add(EProcessType.FILE_LINKING);
        add(EProcessType.VACUUM);
    }

    public void add(EProcessType processType) throws NotSupportedException {
        switch (processType) {
            case FILE_FETCHING -> {
                final Integer threadCount = appParameters.get(EAppParameters.PROCESS_FILE_FETCHING_COUNT, Integer.class);
                final Integer packSize = appParameters.get(EAppParameters.PROCESS_PACK_SIZE, Integer.class);
                add(new FileFetchProcess(entityManagerFactory, packSize), threadCount);
            }
            case FILE_SCANNING -> {
                final Integer threadCount = appParameters.get(EAppParameters.PROCESS_FILE_SCANNING_COUNT, Integer.class);
                add(new FileScanProcess(entityManagerFactory), threadCount);
            }
            case FILE_GROUPING -> {
                // TODO Придумать механизм обновления кэша
                final List<TFileGroup> fileGroupList = DaoUtils.withTransaction(
                                    () -> new DaoWorker(entityManagerFactory),
                                    DaoWorker::getFileGroups
                                    );

                final Optional<TFileGroup> fileGroupDefault = DaoUtils.withTransaction(
                                    () -> new DaoWorker(entityManagerFactory),
                                    DaoWorker::getFileGroupDefault
                                    );

                final Integer threadCount = appParameters.get(EAppParameters.PROCESS_FILE_GROUPING_COUNT, Integer.class);
                add(new FileGroupProcess(entityManagerFactory, fileGroupList, fileGroupDefault), threadCount);
            }
            case FILE_LINKING -> {
                final Integer threadCount = appParameters.get(EAppParameters.PROCESS_FILE_LINKING_COUNT, Integer.class);
                add(new LinkProcess(entityManagerFactory), threadCount);
            }
            case VACUUM -> {
                add(new VacuumProcess(entityManagerFactory), 1); // процесс vacuum выполняется только один
            }
            default -> throw new NotSupportedException("Process type " + processType + " not support");
        }
    }
}
