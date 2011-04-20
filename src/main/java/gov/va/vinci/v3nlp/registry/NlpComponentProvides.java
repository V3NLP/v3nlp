package gov.va.vinci.v3nlp.registry;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="nlp_component_provides")
public class NlpComponentProvides implements Serializable {

    @Id
    @Column(name="id")
    private Integer id;

    @ManyToOne
    private NlpComponent component;

    @Column
    private String name;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
