package gov.va.vinci.v3nlp.registry;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name="v3nlp.nlp_component_category")
public class NlpComponentCategory  implements Serializable
{
    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="short_name")
    private String shortName;

    @Column(name="category_name")
    private String categoryName;

    @Column(name="description")
    private String categoryDescription;

    @Column(name="icon_url")
    private String iconUrl;

    @Column(name="sort_order")
    private
    Integer sortOrder;

    @Column(name="active")
    private
    Boolean active;

    @OneToMany(mappedBy="category", cascade= CascadeType.ALL,
            fetch= FetchType.EAGER)
    private List<NlpComponent> components = new ArrayList<NlpComponent>();

    @Version
    private long version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String name) {
        this.categoryName = name;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<NlpComponent> getComponents() {
        return components;
    }

    public void setComponents(List<NlpComponent> components) {
        this.components = components;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}