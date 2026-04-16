
package com.shabushabu.javashop.conductors.model;


import com.shabushabu.javashop.conductors.exceptions.InvalidLocaleException;

public class Instrument {

	
    public long id;
    //public String quantity;
    public String title;
    //private String m_sub_title;
    public String price;
    public String instrument_type;
    public String condition;
    // private String m_post_URL;
    public String seller_type;
    public String location;
    //public String published_date;
    
    private static final String IS_ENGLISH_REGEX = "^[ \\w \\d \\s \\. \\& \\+ \\- \\, \\! \\@ \\# \\$ \\% \\^ \\* \\( \\) \\; \\\\ \\/ \\| \\< \\> \\\" \\' \\? \\= \\: \\[ \\] ]*$";

	private static boolean isEnglish(String text) {
			if (text == null) {
				return false;
			}
			return text.matches(IS_ENGLISH_REGEX);
		}

    public Instrument() {
    }

    public Instrument(long id, String title, /*String sub_title,*/ String price, String instrument_type, String condition, 
		String seller_type, String location /* String published_date String quantity */ ) throws InvalidLocaleException {
	 	this.id= id;

	 	this.title = title;
	    //sub_title = sub_title;
	 	this.price = price;
	 	this.condition = condition;
	 	this.instrument_type = instrument_type;
	 	this.seller_type = seller_type;
	   // postURL = post_URL;
	 	this.location = location;
	 	//this.quantity = quantity;
    }
    

    public Instrument buildIt(
    	long id, /*String sub_title,*/ String price, String instrument_type, String condition, 
		String seller_type, String location /* String published_date String quantity */ ) {
	 	this.id= id;
	    //sub_title = sub_title;
	 	this.price = price;
	 	this.condition = condition;
	 	this.instrument_type = instrument_type;
	 	this.seller_type = seller_type;
	   // postURL = post_URL;
	 	this.location = location;
	 	//this.quantity = quantity;
	 	return this;
    }
    
    public Instrument buildForLocale(
        	long id, String title, /*String sub_title,*/ String price, String instrument_type, String condition, 
    		String seller_type, String location /* String published_date String quantity */ ) throws InvalidLocaleException {
    	 	this.id= id;
    	 	
    	    if (!isEnglish(title)) {
    	  // 	throw new InvalidLocaleException("Non English Characters found in Instrument Data");
    	    } else {
    	    	
    	    	//System.out.println("Characters OK ");
    	    }

    	 	this.title = title;
    	    //sub_title = sub_title;
    	 	this.price = price;
    	 	this.condition = condition;
    	 	this.instrument_type = instrument_type;
    	 	this.seller_type = seller_type;
    	   // postURL = post_URL;
    	 	this.location = location;
    	 	//this.quantity = quantity;
    	 	
    	 	return this;
        }
    
    public long getID() {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }
    /*
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    */
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
  /*  public String getSubTitle() {
        return m_sub_title;
    }

    public void setSubTitle(String sub_title) {
        m_sub_title = sub_title;
    }
   */
    
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
    public String getInstrumentType() {
        return instrument_type;
    }

    public void setInstrumentType(String type) {
        this.instrument_type = type;
    }
    
    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
   
    /*public String getPublishedDate() {
        return this.published_date;
    }

    public void setPublishedDate(String published_date) {
        this.published_date = published_date;
    }*/
  /*  
    public String getPostUrl() {
        return m_post_URL;
    }

    public void setPostURL(String post_URL) {
        m_post_URL = post_URL;
    }
   */ 
    public String getSellerType() {
        return seller_type;
    }

    public void setSellerType(String seller_type) {
        this.seller_type = seller_type;
    }
}
   

  /*  @Override
    public String toString() {
        //return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
  */