package org.superbiz.moviefun;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class DatabaseConfig {

    @Value("${VCAP_SERVICES}")
    private String vcapServices;

    @Bean
    public DatabaseServiceCredentials databaseServiceCredentials() {
        return new DatabaseServiceCredentials(vcapServices);
    }

    @Bean
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapterImpl = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapterImpl.setDatabase(Database.MYSQL);
        hibernateJpaVendorAdapterImpl.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        hibernateJpaVendorAdapterImpl.setGenerateDdl(true);
        return hibernateJpaVendorAdapterImpl;
    }
}
