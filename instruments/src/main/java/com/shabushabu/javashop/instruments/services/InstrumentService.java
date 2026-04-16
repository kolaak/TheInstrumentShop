package com.shabushabu.javashop.instruments.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shabushabu.javashop.instruments.model.FilteredInstrument;
import com.shabushabu.javashop.instruments.model.Instrument;
import com.shabushabu.javashop.instruments.repositories.InstrumentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Service
public class InstrumentService {

	 private static Logger s_logger = LogManager.getLogger(InstrumentService.class);
	 
    private InstrumentRepository instrumentRepo;

    @Autowired
    public InstrumentService(InstrumentRepository instrumentRepo) {
        this.instrumentRepo = instrumentRepo;
    }

	public List<Instrument> getInstruments(String location) {
        
    	s_logger.info("Entering InstrumentService::getInstruments: Location= " + location);
    	
    	Object obj = null;
    	
    	FilteredInstrument fInstrument = new FilteredInstrument();
    	
    	try {
    		if (!fInstrument.filterInstruments(location)) {
    			return new ArrayList<Instrument>();
    		}
    		
    	}catch(Exception e) {
    		s_logger.error("Locale Filter Failed on " + location);
    	}
    	
    	if (location.equalsIgnoreCase("Chicago")) {
    	
    		obj = instrumentRepo.findInstruments() ;
    		
    		if ( null == obj || !( obj instanceof List<?>) ) {
    			return null;
    		} else {
    			return  StreamSupport.stream(instrumentRepo.findAll().spliterator(), false)
    					.collect(Collectors.toList());
    		}	
    	}
    	else {
    		return  StreamSupport.stream(instrumentRepo.findAll().spliterator(), false)
                    .collect(Collectors.toList());
    		
    	}
    }

}
