package org.jproject.service;

import jakarta.persistence.EntityManagerFactory;
import org.jproject.domain.TFile;
import org.jproject.utils.DaoUtils;
import org.jproject.dao.DaoWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

// @Service
public class GoService {

    private static final Logger logger = LoggerFactory.getLogger(GoService.class);

    private final EntityManagerFactory entityManagerFactory;

    public GoService(EntityManagerFactory entityManagerFactory) throws IOException {
        this.entityManagerFactory = entityManagerFactory;

        List<TFile> files = DaoUtils.withTransaction(
                () -> new DaoWorker(entityManagerFactory),
                dao -> {
                    return dao.getFiles2(
                            List.of(Path.of(
                                    "C:/Users/Рустам/Downloads/gtptabs.com/var/www/gtptabs/data/www/gtptabs.com/storage/tabs/18/rammstein/keine_lust_3.gp5"
                                    )),
                            List.of("4a00fe2695728efb9390ae1902cc7bfb")
                    );
                }
        );


        files.stream().forEach(c -> {
            System.out.println("ID = " + c.getId());
            System.out.println("MD5 = " + c.getMd5());
        });

    }
}
