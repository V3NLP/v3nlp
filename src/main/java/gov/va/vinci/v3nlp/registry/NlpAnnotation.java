package gov.va.vinci.v3nlp.registry;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name="v3nlp.nlp_annotation")
public class NlpAnnotation {
    @Id
    @Column(name="id")
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy="annotation", cascade= CascadeType.ALL,
            fetch= FetchType.EAGER)
    private List<NlpAnnotationAttribute> attributes = new ArrayList<NlpAnnotationAttribute>();

    @Version
    private long version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }


    public List<NlpAnnotationAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<NlpAnnotationAttribute> attributes) {
        this.attributes = attributes;
    }
}
