package gov.va.vinci.v3nlp.registry;


import javax.persistence.*;

@Entity(name="v3nlp.nlp_annotation_attribute")
public class NlpAnnotationAttribute {
    @Id
    @Column(name="id")
    private Integer id;

    @Column
    private String name;

    @Column
    private String value;

    @ManyToOne
    private NlpAnnotation annotation;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
}
