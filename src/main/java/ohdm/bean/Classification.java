package ohdm.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "classification")
public class Classification {
	private int id;
	private String classification = null;
	private String subClassificationName = null; // e.g. fine dust, temperature
	
	public Classification(String classification) {
	    this.classification = classification;
	}
	
	@Id
	@Column(name = "id")
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
	    this.id = id;
	}
	
	@Column(name = "class")
	public String getClassification() {
		return classification;
	}
	
	public void setClassification(String classification) {
	    this.classification = classification;
	}
	
	@Column(name= "subclassname")
	public String getSubClassName() {
	    return subClassificationName;
	}
	
	public void setSubClassificationName(String subClasssificationName) {
	    this.subClassificationName = subClasssificationName;
	} 
} 
