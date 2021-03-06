/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.registry;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity(name="v3nlp.nlp_component")
public class NlpComponent implements Serializable {

    @Id
    @Column(name="id")
    private Integer id;

    @Column
    private String uid;

    @ManyToOne
    private NlpComponentCategory category;

    @Column(name="component_name")
    private String componentName;

    @Column(name="implementation_class")
    private String implementationClass;

    @Column(name="icon_url")
    private String iconUrl;

    @Column(name="configuration_form")
    private String configurationForm;

    @Column(name="default_configuration")
    private String defaultConfiguration;

    @OneToMany(mappedBy="component", cascade=CascadeType.ALL)
    private List<NlpComponentRequires> requires;

    @OneToMany(mappedBy="component", cascade=CascadeType.ALL)
    private List<NlpComponentProvides> provides;

    @Column(name="active")
    private
    Boolean active;

    @Column(name="technology")
    private String technology;

    @Column(name="description")
    private String description;

    @Column(name="assumptions")
    private String assumptions;

    @Column(name="pedigree")
    private String pedigree;

    @Version
    private long version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public NlpComponentCategory getCategory() {
        return category;
    }

    public void setCategory(NlpComponentCategory category) {
        this.category = category;
    }

    public String getImplementationClass() {
        return implementationClass;
    }

    public void setImplementationClass(String implementationClass) {
        this.implementationClass = implementationClass;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getConfigurationForm() {
        return configurationForm;
    }

    public void setConfigurationForm(String configurationForm) {
        this.configurationForm = configurationForm;
    }

    public List<NlpComponentRequires> getRequires() {
        return requires;
    }

    public void setRequires(List<NlpComponentRequires> requires) {
        this.requires = requires;
    }

    public List<NlpComponentProvides> getProvides() {
        return provides;
    }

    public void setProvides(List<NlpComponentProvides> provides) {
        this.provides = provides;
    }

    public String getRequiresString() {
        return listToString(this.requires);

    }

    public String getProvidesString() {
         return listToString(this.provides);
    }

    public Boolean isActive() {
        return getActive();
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDefaultConfiguration() {
        return defaultConfiguration;
    }

    public void setDefaultConfiguration(String defaultConfiguration) {
        this.defaultConfiguration = defaultConfiguration;
    }

    public Boolean getActive() {
        return active;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssumptions() {
        return assumptions;
    }

    public void setAssumptions(String assumptions) {
        this.assumptions = assumptions;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getPedigree() {
        return pedigree;
    }

    public void setPedigree(String pedigree) {
        this.pedigree = pedigree;
    }


    private String listToString(List<?> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }

        StringBuffer b = new StringBuffer();

        for (int i=0; i< list.size(); i++) {
            b.append(list.get(i).toString());
            if (i < list.size() - 1) {
                b.append(", ") ;
            }
        }
        return b.toString();
    }


}
