package org.jproject.config;

import org.jproject.domain.EFileType;
import org.jproject.parameters.AppParameters;
import org.jproject.parameters.EAppParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Value( "${database.url}" )
    private String databaseUrl;

    @Value( "${database.username}" )
    private String databaseUsername;

    @Value( "${database.password}" )
    private String databasePassword;

    @Value( "${database.driver}" )
    private String databaseDriver;

    @Value( "${schedule.timeout:5}")
    private Integer schedulerTimeout;

    @Value( "${thread.poolSize:10}" )
    private Integer poolSize;

    @Value( "${process.fileFetching.count:1}" )
    private Integer processFileFetchingCount;

    @Value( "${process.fileScanning.count:1}" )
    private Integer processFileScanningCount;

    @Value( "${process.fileVerification.count:1}" )
    private Integer processFileVerificationCount;

    @Value( "${process.fileGrouping.count:1}" )
    private Integer processFileGroupingCount;

    @Value( "${process.fileLinking.count:1}" )
    private Integer processFileLinkingCount;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(databaseDriver)
                .url(databaseUrl)
                .username(databaseUsername)
                .password(databasePassword)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("org.jproject.domain");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(getHibernateProperties());
        return em;
    }

    public Properties getHibernateProperties() {
        try {
            Properties properties = new Properties();
            InputStream is = getClass().getClassLoader().getResourceAsStream("hibernate.properties");
            properties.load(is);
            return properties;
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't find hibernate properties");
        }
    }

    @Bean
    public AppParameters appParameters() {
        final AppParameters appParameters = new AppParameters();
        appParameters.set(EAppParameters.SCHEDULER_TIMEOUT, schedulerTimeout);
        appParameters.set(EAppParameters.POOL_SIZE, poolSize);
        appParameters.set(EAppParameters.PROCESS_FILE_FETCHING_COUNT, processFileFetchingCount);
        appParameters.set(EAppParameters.PROCESS_FILE_SCANNING_COUNT, processFileScanningCount);
        appParameters.set(EAppParameters.PROCESS_FILE_VERIFICATION_COUNT, processFileVerificationCount);
        appParameters.set(EAppParameters.PROCESS_FILE_GROUPING_COUNT, processFileGroupingCount);
        appParameters.set(EAppParameters.PROCESS_FILE_LINKING_COUNT, processFileLinkingCount);
        return appParameters;
    }
}
