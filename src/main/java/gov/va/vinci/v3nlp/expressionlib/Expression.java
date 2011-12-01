/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.expressionlib;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


@Entity(name="expressions")
public class Expression {
    @Id
    @Column(name="id")
	Integer id;
    @Id
    @Column(name="expression")
	String expression;
    @Id
    @Column(name="title")
	String title;
    @Id
    @Column(name="description")
	String description;
    @Id
    @Column(name="created_by")
	String createdBy;
    @Id
    @Column(name="matches", nullable = true)
	String matches;
    @Id
    @Column(name="non_matches", nullable = true)
	String nonMatches;
	@Id
    @Column(name="citation", nullable = true)
    String citation;
	@Id
    @Column(name="created_date")
    Date createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getMatches() {
        return matches;
    }

    public void setMatches(String matches) {
        this.matches = matches;
    }

    public String getNonMatches() {
        return nonMatches;
    }

    public void setNonMatches(String nonMatches) {
        this.nonMatches = nonMatches;
    }

    public String getCitation() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
