package com.example.DEMO_INTEGRATION.config;



import com.example.DEMO_INTEGRATION.DAL.Entities.NRLM.Voters4;
import com.example.DEMO_INTEGRATION.DAL.Interfaces.IVoters4Repository;
import com.example.DEMO_INTEGRATION.DAL.Repository.Voters4Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

@Configuration
public class RepositoryConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JpaEntityInformation<Voters4, ?> Voters4info() {
        return JpaEntityInformationSupport.getEntityInformation(Voters4.class, entityManager);
    }

    @Bean
    public IVoters4Repository nrlmRepository(
            JpaEntityInformation<Voters4, ?> entityInfo) {

        return new Voters4Repository(entityInfo, entityManager);
    }



}