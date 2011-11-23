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
        public List<NlpAnnotation> getNlpAnnotationList() {
            return entityManager.createQuery(
                    "select ann from gov.va.vinci.v3nlp.registry.NlpAnnotation as ann order by ann.name")
                    .getResultList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<NlpComponentCategory> getNlpComponentCategoryList() {
            @SuppressWarnings("unchecked")
            List<NlpComponentCategory> queryResults = entityManager.createQuery(
                    "select cat from gov.va.vinci.v3nlp.registry.NlpComponentCategory as cat where active = true order by cat.sortOrder")
                    .getResultList();

            List<NlpComponentCategory> results = new ArrayList<NlpComponentCategory>();

            /** Eager load all the parts, copying only active components over. **/
            for (NlpComponentCategory c : queryResults) {
                NlpComponentCategory cat1 = new NlpComponentCategory();
                cat1.setActive(c.isActive());
                cat1.setCategoryDescription(c.getCategoryDescription());
                cat1.setCategoryName(c.getCategoryName());
                cat1.setIconUrl(c.getIconUrl());
                cat1.setId(c.getId());
                cat1.setShortName(c.getShortName());
                cat1.setSortOrder(c.getSortOrder());

                for (NlpComponent comp : c.getComponents()) {
                    if (!comp.isActive()) {
                        continue;
                    }

                    /** Force initialization for sending to flex. **/
                    for (NlpComponentProvides prov : comp.getProvides()) {
                        prov.getAnnotation().getId();
                    }
                    for (NlpComponentRequires req : comp.getRequires()) {
                        req.getAnnotation().getId();
                    }

                    comp.setCategory(cat1);
                    cat1.getComponents().add(comp);
                }
                results.add(cat1);
            }

            return results;
        }

        @Transactional(readOnly = true)
        public String validatePipeline(ServicePipeLine pipeLine) {
            List<NlpAnnotation> provides = new ArrayList<NlpAnnotation>();

            String technology = null;

            for (ServicePipeLineComponent c : pipeLine.getServices()) {
                if (c.getServiceUid() == null) { // Null service id's are UI side services, ignore them.
                    continue;
                }

                NlpComponent comp = getNlpComponent(c.getServiceUid());

                if (technology == null) {
                    technology = comp.getTechnology();
                }

                if (!technology.equals(comp.getTechnology()))
                {
                    return "Cannot currently mix modules from " + comp.getTechnology() + " with " + technology + ".";
                }

                for (NlpComponentProvides p : comp.getProvides()) { // Put provides in first.
                    provides.add(p.getAnnotation());
                }

                for (NlpComponentRequires r : comp.getRequires()) { // Check requires next.

                    // Straight forward, see if it contains.
                    if (provides.contains(r.getAnnotation())) {
                        continue;
                    }

                    // Basic case doesn't work, check "orGroup"
                    if (r.getOrGroup() != null && validateRequiresInOr(comp, r, provides)) {
                        continue;
                    }

                    return "Missing Dependency: " + comp.getCategory().getCategoryName()
                            + " (" + comp.getImplementationClass() + ") requires "
                            + getRequiresString(comp, r) + ".";

                }
            }
            return "";
        }

        private String getRequiresString(NlpComponent comp, NlpComponentRequires r) {
            if (r.getOrGroup() == null) {
                return r.getAnnotation().getName();
            }

            String orClause = "";
            for (NlpComponentRequires require: comp.getRequires()) {
                if (r.getOrGroup().equals(require.getOrGroup())) {
                        orClause += require.getAnnotation().getName() + " or ";
                }
            }
            return orClause.substring(0, orClause.length() - 3);
        }

        private boolean validateRequiresInOr(NlpComponent comp, NlpComponentRequires r, List<NlpAnnotation> provides) {
            if (r.getOrGroup() == null) {
                return false;
            }

            Integer orGroup = r.getOrGroup();
            for (NlpComponentRequires orGroupItem : comp.getRequires()) {
                if (orGroup.equals(orGroupItem.getOrGroup())) {
                     // This one is an or, see if it is in the deps.
                     if (provides.contains(orGroupItem.getAnnotation())) {
                         return true;
                     }
                 }
            }
            return false;

        }

        @Override
        @Transactional(readOnly = true)
        public NlpComponent getNlpComponent(String uid) {
            Query q = entityManager.createQuery(
                    "select comp from gov.va.vinci.v3nlp.registry.NlpComponent as comp where comp.uid = ?1");
            q.setParameter(1, uid);
            NlpComponent comp = (NlpComponent) q.getSingleResult();
            return comp;
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
