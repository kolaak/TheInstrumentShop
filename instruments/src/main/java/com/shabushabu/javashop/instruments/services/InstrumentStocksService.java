package com.shabushabu.javashop.instruments.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shabushabu.javashop.instruments.model.Stock;
import com.shabushabu.javashop.instruments.repositories.InstrumentStocksRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class InstrumentStocksService {

    private InstrumentStocksRepository instrumentStocksRepo;

    @Autowired
    public InstrumentStocksService(InstrumentStocksRepository instrumentStocksRepo) {
        this.instrumentStocksRepo = instrumentStocksRepo;
    }

    public List<Stock> getInstrumentStocks() {
        return StreamSupport.stream(instrumentStocksRepo.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
