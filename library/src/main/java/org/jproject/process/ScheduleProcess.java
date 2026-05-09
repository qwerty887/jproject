package org.jproject.process;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.NotSupportedException;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TFileGroupMember;
import org.jproject.domain.TFileGroupRule;
import org.jproject.domain.TFileGroup_;
import org.jproject.parameters.AppParameters;
import org.jproject.parameters.EAppParameters;
import org.jproject.service.base.BaseScheduleService;
import org.jproject.utils.DaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleProcess extends BaseScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleProcess.class);

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
                // TODO Придумать механизм обновления кэша
                final List<TFileGroup> fileGroupList = DaoUtils.withTransaction(
                                    () -> new DaoWorker(entityManagerFactory),
                                    dao -> dao.getFileGroups(null)
                                    );

                logger.debug("load fileGroup: {}", fileGroupList.stream().collect(Collectors.toMap(TFileGroup::getId, c -> c.getFileGroupRules().stream().map(TFileGroupRule::getId).toList())));
                logger.debug("load fileGroupMembers: {}", fileGroupList.stream().collect(Collectors.toMap(TFileGroup::getId, c -> c.getFileGroupMembers().stream().map(TFileGroupMember::getId).toList())));

                final Integer fileGroupDefaultId = appParameters.get(EAppParameters.FILE_GROUP_DEFAULT_ID, Integer.class);
                final Optional<TFileGroup> fileGroupDefault = fileGroupList.stream().filter(c -> c.getId().equals(fileGroupDefaultId)).findAny();

                final Integer threadCount = appParameters.get(EAppParameters.PROCESS_FILE_GROUPING_COUNT, Integer.class);
                add(new FileGroupProcess(entityManagerFactory, fileGroupList, fileGroupDefault), threadCount);
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
