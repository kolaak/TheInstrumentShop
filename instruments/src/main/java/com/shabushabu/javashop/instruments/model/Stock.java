package com.shabushabu.javashop.instruments.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "InstrumentStocks")
public class Stock {


	/*@Id
    @GeneratedValue
    private long id;
	*/
	
	@Id
	@Column(name = "ID", nullable = false)
    private String m_id;
    
    
    @Column(name = "Quantity", nullable = false)
    private String quantity;
    
    public Stock () {
    	super();
    }
    
    public Stock(/*int id,*/ String sId, String quantity) {
        super();
        //this.id = id;
        this.m_id = sId != null ? sId: "";
        this.quantity = null != quantity ? quantity : "";
    }

    public String getID() {
        return m_id;
    }

    public void setID(String id) {
        m_id = id;
    }
    
   /* public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    */
    
    
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
   
  /*  @Override
    public String toString() {
        //return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
  */
}


