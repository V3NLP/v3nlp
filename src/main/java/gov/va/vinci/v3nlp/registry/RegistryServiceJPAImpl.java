package gov.va.vinci.v3nlp.registry;

import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.model.ServicePipeLineComponent;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


public class RegistryServiceJPAImpl implements RegistryService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public  List<NlpComponentCategory> getNlpComponentCategoryList() {
        List<NlpComponentCategory> queryResults = entityManager.createQuery(
                "select cat from gov.va.vinci.v3nlp.registry.NlpComponentCategory as cat where active = true")
                .getResultList();

        List<NlpComponentCategory> results = new ArrayList<NlpComponentCategory>();

        /** Eager load all the parts, copying only active components over. **/
        for (NlpComponentCategory c: queryResults) {
            NlpComponentCategory cat1 = new NlpComponentCategory();
            cat1.setActive(c.isActive());
            cat1.setCategoryDescription(c.getCategoryDescription());
            cat1.setCategoryName(c.getCategoryName());
            cat1.setIconUrl(c.getIconUrl());
            cat1.setId(c.getId());
            cat1.setShortName(c.getShortName());
            cat1.setSortOrder(c.getSortOrder());

            for (NlpComponent comp: c.getComponents()) {
                if (!comp.isActive()) {
                    continue;
                }

                for (NlpComponentProvides prov: comp.getProvides()) {
                     prov.getName();
                }
                for (NlpComponentRequires req: comp.getRequires()) {
                     req.getName();
                }
                comp.setCategory(cat1);
                cat1.getComponents().add(comp);
            }
            results.add(cat1);
        }

        return results;
    }

    @Transactional
    public String validatePipeline(ServicePipeLine pipeLine) {
         List<String> provides = new ArrayList<String>();
         for (ServicePipeLineComponent c: pipeLine.getServices()) {
             if (c.getServiceUid() == null) { // Null service id's are UI side services, ignore them.
                 continue;
             }
             Query q = entityManager.createQuery(
                "select comp from gov.va.vinci.v3nlp.registry.NlpComponent as comp where comp.uid = ?1");
             q.setParameter(1, c.getServiceUid());
             NlpComponent comp = (NlpComponent)q.getSingleResult();
             for (NlpComponentProvides p: comp.getProvides())  { // Put provides in first.
                  provides.add(p.getName());
             }
              for (NlpComponentRequires r: comp.getRequires())  { // Check requires next.
                  if (!provides.contains(r.getName())) {
                    return "Missing Dependency: " + comp.getCategory().getCategoryName()
                                + " (" + comp.getImplementationClass() +") requires "
                                + r.getName() + ".";
                  }
              }
         }
         return "";
    }

   @Override
    public NlpComponent getNlpComponent(String uid) {
        return null;  // TODO Implement
    }

    @Override
    public void init() {
        // No-op on this implementation.
    }

    @Override
    public void refresh() {
        // No-op on this implementation.
    }
}
