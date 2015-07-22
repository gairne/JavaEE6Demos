package numberBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@ManagedBean
@SessionScoped
public class NumberEntryBean implements Serializable, Validator {
	
	private static final long serialVersionUID = 5443351151396868724L;
	@Inject
    Number userNumber = null;
    private int maximum = 10;
    private int minimum = 0;
    
    public NumberEntryBean() {
    	
    }
    
    public String getResponse() {
        return "";
    }
    
    public int getMinimum() {
    	return minimum;
    }
    
    public int getMaximum() {
    	return maximum;
    }
    
    public Integer getUserNumber() {
    	return userNumber.getNumber();
    }
    
    public void setUserNumber(Integer input) {
    	userNumber.setNumber(input);
    	System.out.println("User number is now: " + userNumber.getNumber());
    	System.out.println("ID for " + userNumber.toString() + "is: " + userNumber.getID());
    }
    
    @Resource
    UserTransaction utx;
    
    @PersistenceContext
    EntityManager em;
    /**
     * Called by numberPrompt.xhtml on form submit. On submit, it seems that the
     * setUserNumber method is already called because of the #{xyz.userNumber} in the
     * value parameter of the inputText. 
     * To be picked up by EL, this needs to take no parameters.
     * 
     * @return
     */
    public String save() {
    	//Persist details in DB
    	try {
    		utx.begin();
    		System.out.println("Transaction begin");
    		Number n = null;
    		if (userNumber.getID() != null) {
    			System.out.println("ID is already generated, pull the number out of the DB");
    			n = em.find(Number.class, userNumber.getID());
    			System.out.println("Pulled " + n.toString());
    		}
    		if (n == null) {
    			System.out.println("Persisting: " + userNumber.toString());
    			em.persist(userNumber);
    			System.out.println("Persisted " + userNumber.toString());
    		}
    		else {
    			System.out.println("Updating " + userNumber.toString());
    			em.merge(userNumber);
    			System.out.println("Persisted " + userNumber.toString());
    		}
    		sum();
    		sum(2);
    		utx.commit();
    		System.out.println("Transaction commit");
    	}
    	//SystemException, NotSupportedException
    	catch (Exception te) {
    		try {
				utx.rollback();
	    		System.out.println("Transaction rollback");
	    		te.printStackTrace();
    		}
	    	//IllegalStateException, SecurityException, SystemException
	    	catch (Exception re) {
	    		re.printStackTrace();
	    	}
    	}
    	System.out.println("Saved " + this.userNumber);
    	return "numberPrompt.xhtml?faces-redirect=true";
    }
    /*
    public void save(int userNumber) {
    	//This will not work.
    }
    */
    
    public void sum() {
    	Query q = em.createNativeQuery("select * from Number", Number.class);
    	@SuppressWarnings("unchecked")
		List<Number> numbers = q.getResultList();
    	
    	int sum = 0;
    	int i = 0;
    	for (Number n : numbers) {
    		i++;
    		sum += n.getNumber();
    	}
    	
    	System.out.println("Sum: " + sum + ", Entries: " + i);
    }
    
    public void sum(int equal) {
    	Query q = em.createNativeQuery("select * from Number where number = ?1", Number.class);
    	q.setParameter(1, equal);
    	@SuppressWarnings("unchecked")
		List<Number> numbers = q.getResultList();
    	
    	int sum = 0;
    	int i = 0;
    	for (Number n : numbers) {
    		i++;
    		sum += n.getNumber();
    	}
    	
    	System.out.println("Sum: " + sum + ", Entries: " + i);
    }

    /**
     * Validate user input.
     * TODO: Produce a user displayable message, not just a console one.
     */
	@Override
	public void validate(FacesContext ctx, UIComponent cpt, Object value) throws ValidatorException {
		if ((Integer) value % 2 != 0) {
			FacesMessage msg = new FacesMessage();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Input invalid");
			msg.setDetail("Supplied number is not even");
			ctx.addMessage("xyz", msg);
			throw new ValidatorException(msg);
		}
	}
}
