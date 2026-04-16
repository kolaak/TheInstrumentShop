package com.shabushabu.javashop.instruments.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shabushabu.javashop.instruments.model.Instrument;

public interface InstrumentRepository extends JpaRepository<Instrument, String>, FindInstrumentRepository {
}
