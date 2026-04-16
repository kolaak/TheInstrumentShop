package com.shabushabu.javashop.shop.repo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.shabushabu.javashop.shop.services.dto.InstrumentDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class InstrumentRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstrumentRepo.class);

    @Value("${instrumentsUri}")
    private String instrumentsUri;

    @Autowired
    @Qualifier(value = "stdRestTemplate")
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "instrumentsNotFound") 
    public Map<Long, InstrumentDTO> getinstrumentDTOs() {
        LOGGER.info("getInstrument DTOS");
        ResponseEntity<List<InstrumentDTO>> instrumentsResponse =
                restTemplate.exchange(instrumentsUri + "/instruments",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<InstrumentDTO>>() {
                        });
        List<InstrumentDTO> instrumentDTOs = instrumentsResponse.getBody();

        return instrumentDTOs.stream()
                .collect(Collectors.toMap(InstrumentDTO::getId, Function.identity()));
    }
    
    
    public Map<Long, InstrumentDTO> getinstrumentsByLocation(String location) {
        LOGGER.info("getInstrument  by location ");
        ResponseEntity<List<InstrumentDTO>> instrumentsResponse =
                restTemplate.exchange(instrumentsUri + "/instruments?" + "location=" + location,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<InstrumentDTO>>() {
                        });
        List<InstrumentDTO> instrumentDTOs = instrumentsResponse.getBody();

        return instrumentDTOs.stream()
                .collect(Collectors.toMap(InstrumentDTO::getId, Function.identity()));
    }

    public Map<Long, InstrumentDTO> instrumentsNotFound() {
        LOGGER.info("Instruments Empty NOT FOUND  *** FALLBACK ***");
        return Collections.emptyMap();
    }
}
