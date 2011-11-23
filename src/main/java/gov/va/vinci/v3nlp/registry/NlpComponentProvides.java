package gov.va.vinci.v3nlp.registry;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="v3nlp.nlp_component_provides")
public class NlpComponentProvides implements Serializable {

    @Id
    @Column(name="id")
    private Integer id;

    @ManyToOne
    private NlpComponent component;

    @ManyToOne
    private NlpAnnotation annotation;

    @Column
    private String name;

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

    public String toString() {
        return this.annotation.getName();
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
