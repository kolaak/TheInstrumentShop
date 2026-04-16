package com.shabushabu.javashop.shop.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.shabushabu.javashop.shop.model.Product;
import com.shabushabu.javashop.shop.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @RequestMapping()
    public List<Product> getProducts(@RequestParam(value="location", required=false)String location) {
        return productService.getProducts(null != location ? location : "California" );
    }
}
