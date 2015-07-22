package helloBean;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class HelloBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public String getHello() {
		return "Greetings chief inspector!";
	}
}
