package com.shabushabu.javashop.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.shabushabu.javashop.shop.model.Product;
import com.shabushabu.javashop.shop.model.Instrument;
import com.shabushabu.javashop.shop.repo.StockRepo;
import com.shabushabu.javashop.shop.repo.InstrumentRepo;
import com.shabushabu.javashop.shop.repo.ProductRepo;
import com.shabushabu.javashop.shop.services.dto.InstrumentDTO;
import com.shabushabu.javashop.shop.exceptions.InvalidLocaleException;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InstrumentService {

    @SuppressWarnings("unused")
    @Autowired
    private StockRepo stockRepo;

   @SuppressWarnings("unused")
@Autowired
   private ProductRepo productRepo;
    
    @Autowired
    private InstrumentRepo instrumentRepo;

    public List<Instrument> getInstruments(String location) {
        Map<Long, InstrumentDTO> instrumentsDTO = instrumentRepo.getinstrumentsByLocation(location);
        return instrumentsDTO.values().stream()
                .map(instrumentDTO -> {
                	  try {
  						return new Instrument().buildForLocale(instrumentDTO.getId(),  
  								instrumentDTO.getTitle(), instrumentDTO.getPrice(), instrumentDTO.getInstrumentType(),
  								instrumentDTO.getCondition(), instrumentDTO.getSellerType(), instrumentDTO.getPublishedDate());
  					} catch (InvalidLocaleException e) {
  						
  						e.printStackTrace();
  						return  new Instrument().buildIt(instrumentDTO.getId(),   instrumentDTO.getPrice(), instrumentDTO.getInstrumentType(),
  								instrumentDTO.getCondition(), instrumentDTO.getSellerType(), instrumentDTO.getPublishedDate());	
  					}
                   })
                  .collect(Collectors.toList());
    }

    public List<Product> productsNotFound() {
        return Collections.emptyList();
    }
}
