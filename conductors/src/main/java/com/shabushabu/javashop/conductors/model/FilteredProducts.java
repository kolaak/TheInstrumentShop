package com.shabushabu.javashop.conductors.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shabushabu.javashop.conductors.exceptions.InvalidLocaleException;



public class FilteredProducts {
	
   private static final boolean  s_Localedisabled = true; 
	
   private final Logger s_logger = LoggerFactory.getLogger(FilteredProducts.class);
	

public boolean filterProducts( String locale) throws InvalidLocaleException {
	
	boolean result = true;
	
	if ("Oregon".equalsIgnoreCase(locale)) {
	
		if (s_Localedisabled) {
				
				// See src/main/resources/application.properties ..
				s_logger.error("Trying to filter to disabled Region: " + locale);
				
				throw new InvalidLocaleException("Trying to filter to disabled Region: Oregon");
			
		} else {	
				
				// See src/main/resources/application.properties ..
				s_logger.info("Calling Oregon Specific data.....");
				
				result = true;	 
		}
	}
	
	return result;
}

}