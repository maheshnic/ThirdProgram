package com.mahesh.service;

import com.mahesh.dto.BeerDTO;
import com.mahesh.dto.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beer);

    Boolean deleteBeer(UUID id);

    Optional<BeerDTO> patchById(UUID id, BeerDTO beer);
}
