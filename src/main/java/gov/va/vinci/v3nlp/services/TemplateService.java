/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services;

import gov.va.vinci.v3nlp.model.Template;

/**
 * Templates are pre-build pipelines/applications in xml format.
 */
public interface TemplateService {


    /**
     * Get all templates currently defined in the system.
     * @return all templates currently defined in the system.
     *
     */
    public abstract Template[] getTemplates();

}
