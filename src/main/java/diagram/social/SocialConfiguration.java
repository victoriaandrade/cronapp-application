package diagram.social;

import org.springframework.orm.jpa.*;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.*;
import org.springframework.transaction.*;
import org.springframework.transaction.annotation.*;
import org.springframework.core.io.*;
import org.springframework.data.repository.init.*;
import java.net.URL;
import java.io.File;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Classe que configura os beans para persistencia
 * @generated
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "diagram.social-EntityManagerFactory",
        transactionManagerRef = "diagram.social-TransactionManager"
)
class SocialConfiguration {


    @Bean(name="diagram.social-EntityManagerFactory")
    public LocalEntityManagerFactoryBean entityManagerFactory() {
        LocalEntityManagerFactoryBean factoryBean = new LocalEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("diagram.social");
        return factoryBean;
    }

    @Bean(name = "diagram.social-TransactionManager")
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

}