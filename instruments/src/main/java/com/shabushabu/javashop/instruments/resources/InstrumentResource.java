package com.shabushabu.javashop.instruments.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.shabushabu.javashop.instruments.exceptions.InstrumentNotFoundException;
import com.shabushabu.javashop.instruments.model.Instrument;
import com.shabushabu.javashop.instruments.model.Stock;
import com.shabushabu.javashop.instruments.services.InstrumentService;
import com.shabushabu.javashop.instruments.services.InstrumentStocksService;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@RestController
@RequestMapping()
public class InstrumentResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstrumentResource.class);

    @Autowired
    private InstrumentStocksService instrumentStocksService;
    
    @Autowired
    private InstrumentService instrumentService;
    
    @RequestMapping("/healthcheck")
    @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    public String healthCheck() {
        return "HTTP Status OK (CODE 200)\n";
    }    
    
    @RequestMapping("/instruments")
    public List<Instrument> getInstruments(@DefaultValue("California") @RequestParam("location") String location) {
    	LOGGER.info("getInstruments (All) at location: " + location);
        return instrumentService.getInstruments(location);
    }
    
    @RequestMapping("/stocks")
    public List<Stock> getInstrumentStocks() {
        LOGGER.info("getInstrument Stocks (All)");
        return instrumentStocksService.getInstrumentStocks();
    }
    
    /*
    @RequestMapping("{productId}")
    public Instrument getInstrument(@PathVariable("productId") String productId) throws InstrumentNotFoundException {
        LOGGER.info("getInstrument with productId: {}", productId);
        return instrumentService.getInstrument(productId);
    }
    */
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleInstrumentNotFound(InstrumentNotFoundException snfe) {
    }
}
