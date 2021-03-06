import java.io.Serializable;
import java.util.*;
import java.util.UUID;
import jason.asSyntax.*;

/**
 * Describes an item offered or requested in the market place.
 * 
 */
public class ItemDescriptor implements Serializable {

	private static final long serialVersionUID = -3595604261119043714L;

	private List<String> attributes = new ArrayList<String>();
	
	private double priceLimit;

	/**
	 * Adds an attribute to this item.
	 * 
	 * @param a
	 */
	public void addAttribute(String a) {
		attributes.add(a);
	}

	/**
	 * Checks if this item has a specific attribute.
	 * 
	 * @param a
	 * @return
	 */
	public boolean hasAttribute(String a) {
		return attributes.contains(a);
	}
	
	/**
	 * Returns a copy of the attributes of this item.
	 *
	 * @return
	 */
	public List<String> getAttributes() {
		return new ArrayList<String>(attributes);
	}

	/**
	 * Sets the maximal/minimal price for this item.
	 * 
	 * @param limit
	 */
	public void setPriceLimit(double limit) {
		this.priceLimit = limit;
	}

	/**
	 * Gets the maximal/minimal price for this item, respectively, if this item
	 * is requested/offered.
	 * 
	 * @return
	 */
	public double getPriceLimit() {
		return priceLimit;
	}
	
	/**
	 * Returns a ListTerm of StringTerms of the attributes of this item (excluding the price limit).
	 *
	 * @return
	 */
	public Term toTerm() {
		ListTerm attributeTerms = new ListTermImpl();
		for(String attribute : attributes)
			attributeTerms.add(new StringTermImpl(attribute));
		return attributeTerms;
	}
	
	/**
	 * Constructs an item from the specified ListTerm of StringTerms.
	 *
	 * @param term
	 * @return
	 */
	public static ItemDescriptor fromTerm(Term term) {
		if(!(term instanceof ListTerm))
			throw new IllegalArgumentException("The term has to be an attribute list instead of " + term.getClass().getName() + ".");
		
		ItemDescriptor id = new ItemDescriptor();
		for(Term attribute : ((ListTerm) term)) {
			if(!(attribute instanceof StringTerm))
				throw new IllegalArgumentException("Expecting the attributes to be strings.");
			id.addAttribute(((StringTerm) attribute).getString());
		}
		return id;
	}
	
	/**
	 * Two items are equal if they have the same set of attributes in the same order (regardless of the price limit).
	 *
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		
		if(!(o instanceof ItemDescriptor))
			return false;
		
		ItemDescriptor id = (ItemDescriptor) o;
		
		if(id.getAttributes().size() != attributes.size())
			return false;
		
		for(int i = 0; i < id.getAttributes().size(); ++i)
			if(!id.getAttributes().get(i).equals(attributes.get(i)))
				return false;
			
		return true;
	}
	
	/**
	 * @see equals
	 */
	@Override
	public int hashCode() {
		return toString().split("@")[0].hashCode();
	}
	
	/**
	 * Creates a human-readable string representation of this item.
	 *
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		String prefix = "";
		for (String attribute : attributes) {
			sb.append(prefix);
			sb.append(attribute);
			prefix = ", ";
		}
		sb.append("}@");
		sb.append(priceLimit);
		return sb.toString();
	}
}
