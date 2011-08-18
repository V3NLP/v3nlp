package gov.va.vinci.v3nlp.services.uima;


public class UimaUtilities {

    /**
    public static Corpus convertUimaCasToCommonModel(CAS aCas) {

            System.out.println("In PrintCasINFO");
            Iterator<String> it = aCas.getIndexRepository().getLabels();
            System.out.println("\n\nLabels:");
            while (it.hasNext()) {
                System.out.println("\t--> String:" + it.next());
            }

            Iterator indexes = aCas.getIndexRepository().getIndex("AnnotationIndex").iterator();
            System.out.println("Type: " + aCas.getIndexRepository().getIndex("AnnotationIndex").getType().getName());
            System.out.println("\n\nAnnotationIndexes:");
            while (indexes.hasNext()) {
                FeatureStructure o = (FeatureStructure)indexes.next();
                System.out.println("Class->" + o.getClass());
                System.out.println("Component Type->" + o.getType().getName());
                System.out.println(o);
            }


            return null;
    }
     **/
}
