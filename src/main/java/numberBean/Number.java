package numberBean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Number implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	private Integer number;
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public Integer getNumber() {
		return number;
	}
	
	public Long getID() {
		return id;
	}
	
	public String toString() {
		return super.toString() + "{ID: " + getID() + ", number: " + getNumber() + "}";
	}
}
