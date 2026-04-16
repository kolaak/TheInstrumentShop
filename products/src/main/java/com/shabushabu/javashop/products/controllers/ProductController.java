

package com.shabushabu.javashop.products.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shabushabu.javashop.products.services.ProductFilterService;
import com.shabushabu.javashop.products.services.ProductService;
import com.shabushabu.javashop.products.model.Product;
import java.util.List;


@RestController
public class ProductController {

    @GetMapping("/products")
    public List<Product> getProductsByLocation(@RequestParam String location) {
        // Mock response for demonstration
    	
    	if (location == null ) {
    		location = "California";
    	}
    	
    	ProductService service = new ProductService();
    	
    	ProductFilterService serviceFilter = new ProductFilterService();
    	
        return serviceFilter.filterAllProducts(location, service);
    }
    
    @RequestMapping("products/healthcheck")
    @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    public String healthCheck() {
        return "HTTP Status OK (CODE 200)\n";
    }    
}

