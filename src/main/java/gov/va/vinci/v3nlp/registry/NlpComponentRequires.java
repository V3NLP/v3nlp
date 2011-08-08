package gov.va.vinci.v3nlp.registry;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity(name="nlp_component_requires")
public class NlpComponentRequires implements Serializable {

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
