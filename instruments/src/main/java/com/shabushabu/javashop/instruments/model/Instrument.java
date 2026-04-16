package com.shabushabu.javashop.instruments.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

//import org.springframework.data.annotation.Id;
import javax.persistence.Id;

@Entity
@Table(name = "instruments_for_sale")
public class Instrument {
	

	@Id
    @GeneratedValue
    private long id;
	
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "sub_title", nullable = false)
    private String sub_title;
    
    @Column(name = "price", nullable = false)
    private String price;
    
    @Column(name = "instrument_type", nullable = false)
    private String instrument_type;
    
    @Column(name = "condition", nullable = false)
    private String condition;
    
    @Column(name = "post_url", nullable = false)
    private String post_url;
    
    @Column(name = "seller_type", nullable = false)
    private String seller_type;
    
    @Column(name = "location", nullable = false)
    private String location;
    
    @Column(name = "published_date", nullable = false)
    private String published_date;
    
    public Instrument( String title, String sub_title, String price, String instument_type, String condition, 
    		String seller_type, String location,   String post_url, String published_date ) {
    	
    	    super();
    	    	this.title = title;
    	    	this.sub_title = sub_title;
    	    	this.price = price;
    	    	this.condition = condition;
    	    	this.seller_type = seller_type;
    	    	this.location =location;
    	    	this.post_url = post_url;
    	    	this.published_date = published_date;
    }

    
    public Instrument() {
        super();
   }
   
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws Exception {
    	
    	
        this.title = title;
    }
    
    public String getSubTitle() {
        return sub_title;
    }

    public void setSubTitle(String sub_title) {
        this.sub_title = sub_title;
    }
    
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
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    
   
    
    public String getPublishedDate() {
        return published_date;
    }

    public void setPublishedDate(String published_date) {
        this.published_date = published_date;
    }
    
    public String getPostUrl() {
        return post_url;
    }

    public void setPostURL(String post_URL) {
        this.post_url = post_URL;
    }
    
    public String getSellerType() {
        return seller_type;
    }

    public void setSellerType(String seller_type) {
        this.seller_type = seller_type;
    }

   

    @Override
    public String toString() {
        return "I-Sale [id=" + id + ", Title=" + title + ", Price=" + price + 
        	   ", SubTitle=" + sub_title + ", InstrumentType="
               + instrument_type + ", Condition=" + condition + ", PublishedDate=" + published_date + 
               ", PostUrl=" + post_url + ", Seller=" + seller_type + ", Location=" + location +  "]";
    }
  
}


