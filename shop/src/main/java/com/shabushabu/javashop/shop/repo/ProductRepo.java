package com.shabushabu.javashop.shop.repo;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.shabushabu.javashop.shop.services.dto.ProductDTO;

@Component
public class ProductRepo {

    @Value("${productsUri}")
    private String productsUri;

    @Value("${conductorsUri}")
    private String conductorsUri;

    @Autowired
    @Qualifier(value = "stdRestTemplate")
    private RestTemplate restTemplate;

    // Enable this when conductors is Live.
    static boolean bConductorsEnabled=true;


    public Map<String, ProductDTO> getProductDTOs(String location) {


        ResponseEntity<List<ProductDTO>> productCatalogueResponse;

        if (bConductorsEnabled && "Utah".equalsIgnoreCase(location)) {
                productCatalogueResponse =
                        restTemplate.exchange(conductorsUri + "/conductors?" + "location=" + location,
                                HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductDTO>>() {
                                });
        } else {
                productCatalogueResponse =
                        restTemplate.exchange(productsUri + "/products?" + "location=" + location,
                                HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductDTO>>() {
                                });
        }

        List<ProductDTO> productDTOs = productCatalogueResponse.getBody();

        return productDTOs.stream()
        .collect(Collectors.toMap(ProductDTO::getId, Function.identity()));

                             
    }

}
