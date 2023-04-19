package com.mahesh.service;

import com.mahesh.dto.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> listBeers(String beerName);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beer);

    Boolean deleteBeer(UUID id);

    Optional<BeerDTO> patchById(UUID id, BeerDTO beer);
}
