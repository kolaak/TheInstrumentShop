package com.shabushabu.javashop.stock.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.shabushabu.javashop.stock.exceptions.StockNotFoundException;
import com.shabushabu.javashop.stock.model.Stock;
import com.shabushabu.javashop.stock.services.InstrumentStocksService;
import com.shabushabu.javashop.stock.services.StockService;


import java.util.List;


@RestController
@RequestMapping()
public class StockResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockResource.class);

    @Autowired
    private StockService stockService;
    
    @Autowired
    private InstrumentStocksService instrumentStocksService;

    @RequestMapping("/legacy")
    public List<Stock> getStocks() {
        LOGGER.info("getStocks (All stocks)");
        return stockService.getStocks();
    }
   
    @RequestMapping("/insruments")
    public List<Stock> getInstrumentStocks() {
        LOGGER.info("getInstrumentStocks (All Instrument stocks)");
        return instrumentStocksService.getStocks();
    }
    
    @RequestMapping("/healthcheck")
    @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    public String healthCheck() {
        return "HTTP Status OK (CODE 200)\n";
    }    
    

   /* @RequestMapping("{productId}")
    public Stock getStock(@PathVariable("productId") String productId) throws StockNotFoundException {
        LOGGER.info("getStock with productId: {}", productId);
        return stockService.getStock(productId);
    }
	*/
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleStockNotFound(StockNotFoundException snfe) {
    }
}
