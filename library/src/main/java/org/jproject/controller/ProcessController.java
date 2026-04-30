package org.jproject.controller;

import jakarta.persistence.EntityManagerFactory;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EProcessStatus;
import org.jproject.dto.controllers.DtoProcessStatus;
import org.jproject.parameters.process.FileFetchingProcessParameters;
import org.jproject.service.FileFetchService;
import org.jproject.utils.DaoUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    private final EntityManagerFactory entityManagerFactory;

    public ProcessController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @PostMapping(path = "/fetch/add", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public DtoProcessStatus addFetch(@RequestBody FileFetchingProcessParameters param) {
        return DaoUtils.withTransaction(
                    () -> new DaoWorker(entityManagerFactory),
                    dao -> DtoProcessStatus.of(new FileFetchService(entityManagerFactory).add(dao, param))
                    );
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public DtoProcessStatus getStatus(@PathVariable int id) {
        return DaoUtils.withTransaction(
                () -> new DaoWorker(entityManagerFactory),
                dao -> dao.getProcess(dao.getProcessSpec(id))
                        .map(DtoProcessStatus::of)
                        .orElse(new DtoProcessStatus())
        );
    }

    @GetMapping(path = "/status/{processStatus}", produces = "application/json")
    public List<DtoProcessStatus> getStatuses(@PathVariable EProcessStatus processStatus) {
        return DaoUtils.withTransaction(
                () -> new DaoWorker(entityManagerFactory),
                dao -> dao.getProcesses(dao.getProcessSpec(processStatus))
                        .stream()
                        .map(DtoProcessStatus::of)
                        .toList()
        );
    }

}
