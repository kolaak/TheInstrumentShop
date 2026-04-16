package com.shabushabu.javashop.instruments.repositories;

import org.springframework.data.repository.CrudRepository;

import com.shabushabu.javashop.instruments.model.Stock;

public interface InstrumentStocksRepository extends CrudRepository<Stock, String> {

}
