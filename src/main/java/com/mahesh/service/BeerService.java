package com.mahesh.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.mahesh.dto.BeerDTO;
import com.mahesh.dto.BeerStyle;

public interface BeerService {

	Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber,
			Integer pageSize);

	Optional<BeerDTO> getBeerById(UUID id);

	BeerDTO saveBeer(BeerDTO beer);

	Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beer);

	Boolean deleteBeer(UUID id);

	Optional<BeerDTO> patchById(UUID id, BeerDTO beer);
}
