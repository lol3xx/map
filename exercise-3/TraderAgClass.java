import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.*;
import java.util.*;
import java.util.Map.*;
import java.io.*;
import java.util.logging.*;

public class TraderAgClass extends Agent {
	
	private Logger logger = Logger.getLogger("exercise-3.mas2j." + TraderAgClass.class.getName());
	
	@Override
	public void initAg() {
		super.initAg();
		
		String agentName = ts.getUserAgArch().getAgName();
		
		// read the properties file
		Properties properties = new Properties();
		Reader reader;
		try {
			reader = new FileReader("resource/" + agentName + ".properties");
			properties.load(reader);
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		// construct offers and requests
		List<ItemDescriptor> offers = parseItems(properties.getProperty("items.offered"));
		List<ItemDescriptor> requests = parseItems(properties.getProperty("items.requested"));
		
		// add initial sell beliefs from offers
		for(ItemDescriptor offer : offers) {
			Literal sellBelief = ASSyntax.createLiteral("sellBelief");
			sellBelief.addTerms(offer.toTerm());
			try {
				addBel(sellBelief);
			} catch(RevisionFailedException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		
		// add initial buy beliefs from requests
		for(ItemDescriptor request : requests) {
			Literal buyBelief = ASSyntax.createLiteral("buyBelief");
			buyBelief.addTerms(request.toTerm());
			try {
				addBel(buyBelief);
			} catch(RevisionFailedException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		
		logger.info("Agent '" + agentName + "' offers " + offers + " and requests " + requests + ".");
	}
	
	private List<ItemDescriptor> parseItems(String itemsString) {
		List<ItemDescriptor> items = new ArrayList<ItemDescriptor>();
		if (itemsString != null) {
			// items are separated by vertical bars
			String[] splitItems = itemsString.split("\\|");
			for (String itemString : splitItems) {
				ItemDescriptor item = new ItemDescriptor();

				// attributes are separated by semicolons
				String[] attributes = itemString.split(";");
				
				// first one has to be the price limit
				try {
					item.setPriceLimit(Double.parseDouble(attributes[0]));
				} catch(NumberFormatException e) {
					throw new IllegalArgumentException("The first attribute has to be the price limit.");
				}
				
				// add the rest of the attributes
				for (int i = 1; i < attributes.length; ++i)
					item.addAttribute(attributes[i]);
				items.add(item);
			}
		}
		return items;
	}

}
