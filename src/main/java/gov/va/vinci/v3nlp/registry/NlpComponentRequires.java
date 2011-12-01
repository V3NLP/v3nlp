/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.registry;


import javax.persistence.*;
import java.io.Serializable;

@Entity(name="v3nlp.nlp_component_requires")
public class NlpComponentRequires implements Serializable {

    @Id
    @Column(name="id")
    private Integer id;

    @ManyToOne
    private NlpComponent component;

    @ManyToOne
    private NlpAnnotation annotation;

    @Column
    private String name;

    @Column
    private Integer orGroup;

    @Version
    private long version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public NlpComponent getComponent() {
        return component;
    }

    public void setComponent(NlpComponent component) {
        this.component = component;
    }


    public NlpAnnotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(NlpAnnotation annotation) {
        this.annotation = annotation;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Integer getOrGroup() {
        return orGroup;
    }

    public void setOrGroup(Integer orGroup) {
        this.orGroup = orGroup;
    }

    public String toString() {
        return this.annotation.getName();
    }
}
