
package com.shabushabu.javashop.conductors.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shabushabu.javashop.conductors.services.ProductFilterService;
import com.shabushabu.javashop.conductors.services.ConductorsService;
import com.shabushabu.javashop.conductors.exceptions.InvalidLocaleException;
import com.shabushabu.javashop.conductors.model.FilteredProducts;
import com.shabushabu.javashop.conductors.model.Product;
import java.util.List;

@RestController
public class ConductorsController {

    @GetMapping("/conductors")
    public List<Product> getProductsByLocation(@RequestParam String location) throws InvalidLocaleException {
        // Mock response for demonstration
    	
    	location = "Oregon";
    	
    	ConductorsService service = new ConductorsService();
    	
    	ProductFilterService serviceFilter = new ProductFilterService();
        try {
            FilteredProducts products = new FilteredProducts();
            products.filterProducts(location);
        }catch(Exception e) {

        }
        return serviceFilter.filterAllProducts(location, service);
    }
    
    @RequestMapping("conductors/healthcheck")
    @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    public String healthCheck() {
        return "HTTP Status OK (CODE 200)\n";
    }    
}
