package gov.va.vinci.v3nlp.services;

import gov.va.vinci.v3nlp.model.BatchJobStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Transactional
public abstract class BaseServicePipeLineProcessor implements ServicePipeLineProcessor {
    protected EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = false)
    protected void updatePipeLineStatus(BatchJobStatus jobStatus, String status, String pathToResults) {
        jobStatus.setStatus(status);
        jobStatus.setResultPath(pathToResults);
        entityManager.merge(jobStatus);
    }
}
