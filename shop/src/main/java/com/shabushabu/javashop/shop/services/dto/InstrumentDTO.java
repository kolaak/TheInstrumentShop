package com.shabushabu.javashop.shop.services.dto;

    
public class InstrumentDTO {

    	
    	 public static final InstrumentDTO DEFAULT_INSTRUMENT_DTO = new InstrumentDTO(9999, "defaultTitle",
    	    		"defaultSubTitle" , "00.00",  "NO INSTRUMENT TYPE", "NO CONDITION", "NO SELLER", "NO LOCATION" , "NO_DATE", "NO URL");
        private long id;
    	
        
 
        private String title;
        
   
        private String sub_title;
        
        
        private String price;
        
        
        private String instrument_type;
        
      
        private String condition;
        
       
        private String post_url;
        
      
        private String seller_type;
        
       
        private String location;
        
        private String published_date;
        
        public InstrumentDTO( long id, String title, String sub_title, String price, String instument_type, String condition, 
        		String seller_type, String location,  String published_date, String post_url  ) {
        	
        	    super();
        	    this.id = id;
        	 	this.title = title;
        	    this.sub_title = sub_title;
        	 	this.price = price;
        	 	this.condition = condition;
        	 	this.seller_type = seller_type;
        	 	this.location =location;
        	    this.post_url = post_url;
        	 	this.published_date = published_date;
            }

        
        public InstrumentDTO() {
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

        public void setTitle(String title) {
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

