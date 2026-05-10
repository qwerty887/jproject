package org.jproject.process;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.NotSupportedException;
import org.jproject.domain.EProcessType;
import org.jproject.parameters.AppParameters;
import org.jproject.parameters.EAppParameters;
import org.jproject.service.base.BaseScheduleService;
import org.springframework.stereotype.Service;

@Service
public class ScheduleProcess extends BaseScheduleService {

    private final EntityManagerFactory entityManagerFactory;
    private final AppParameters appParameters;

    public ScheduleProcess(EntityManagerFactory entityManagerFactory, AppParameters appParameters) throws NotSupportedException {
        super(appParameters);
        this.appParameters = appParameters;
        this.entityManagerFactory = entityManagerFactory;

        add(EProcessType.FETCH);
        add(EProcessType.SCAN);
        add(EProcessType.GROUP);
        add(EProcessType.LINK);
        add(EProcessType.VACUUM);
    }

    public void add(EProcessType processType) throws NotSupportedException {
        switch (processType) {
            case FETCH -> {
                final Integer threadCount = appParameters.get(EAppParameters.PROCESS_FILE_FETCHING_COUNT, Integer.class);
                final Integer packSize = appParameters.get(EAppParameters.PROCESS_PACK_SIZE, Integer.class);
                add(new FileFetchProcess(entityManagerFactory, packSize), threadCount);
            }
            case SCAN -> {
                final Integer threadCount = appParameters.get(EAppParameters.PROCESS_FILE_SCANNING_COUNT, Integer.class);
                add(new FileScanProcess(entityManagerFactory), threadCount);
            }
            case GROUP -> {
                final Integer threadCount = appParameters.get(EAppParameters.PROCESS_FILE_GROUPING_COUNT, Integer.class);
                add(new FileGroupProcess(entityManagerFactory), threadCount);
            }
            case LINK -> {
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
