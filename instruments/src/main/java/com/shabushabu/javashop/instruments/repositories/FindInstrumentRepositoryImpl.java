package com.shabushabu.javashop.instruments.repositories;


import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.shabushabu.javashop.instruments.model.Instrument;
import com.shabushabu.javashop.instruments.resources.InstrumentResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class FindInstrumentRepositoryImpl implements FindInstrumentRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(InstrumentResource.class);
	
	
    @PersistenceContext
    private EntityManager entityManager;

	@Override
    public Object findInstruments() {
    	LOGGER.info("findInstruments Called (All)");
    	
    	Object obj = entityManager.createNativeQuery( "SELECT * FROM instruments_for_sale, instruments_for_sale_chicago").getResultList(); 
	 
		return obj;
    }
    
    @Override
    public Instrument findInstrumentByID(String id) {
	    Instrument result = (Instrument) entityManager.createQuery("FROM instruments i WHERE i.ID = " + id.toString()).getSingleResult(); 
    	return result;
    }

    @PostConstruct
    public void postConstruct() {
        Objects.requireNonNull(entityManager);
    }
}
 