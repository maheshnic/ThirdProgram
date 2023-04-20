package com.mahesh.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mahesh.dto.BeerStyle;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahesh.dto.BeerDTO;
import com.mahesh.entities.Beer;
import com.mahesh.exception.NotFoundException;
import com.mahesh.mappers.BeerMapper;
import com.mahesh.repositories.BeerRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
class BeerControllerIT {

	@Autowired
	BeerController beerController;
	@Autowired
	BeerRepository beerRepository;

	@Autowired
	BeerMapper beerMapper;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	ObjectMapper objectMapper;

	MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testListBeers() {

		List<BeerDTO> dtos = beerController.listBeers(null, null, false);

		assertThat(dtos.size()).isEqualTo(2413);
	}

	@Rollback
	@Transactional
	@Test
	void testEmptyList() {
		beerRepository.deleteAll();
		List<BeerDTO> dtos = beerController.listBeers(null, null, false);

		assertThat(dtos.size()).isEqualTo(0);
	}

	@Test
	void testGetById() {
		Beer beer = beerRepository.findAll().get(0);

		BeerDTO beerDTO = beerController.getBeerById(beer.getId());

		assertThat(beerDTO).isNotNull();
	}

	@Test
	void testBeerIdNotFound() {
		assertThrows(NotFoundException.class, () -> {
			beerController.getBeerById(UUID.randomUUID());
		});
	}

	@Rollback
	@Transactional
	@Test
	void testHandlePost() {
		BeerDTO beerDTO = BeerDTO.builder().beerName("New Beer").build();

		ResponseEntity responseEntity = beerController.handlePost(beerDTO);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

		String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
		UUID savedUUID = UUID.fromString(locationUUID[4]);

		Beer beer = beerRepository.findById(savedUUID).get();

		assertThat(beer).isNotNull();
	}

	@Rollback
	@Transactional
	@Test
	void testUpdateExistingBeer() {
		Beer beer = beerRepository.findAll().get(0);
		BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);

		beerDTO.setId(null);
		beerDTO.setVersion(null);
		final String beerName = "Updated Beer";
		beerDTO.setBeerName(beerName);

		ResponseEntity responseEntity = beerController.updateById(beer.getId(), beerDTO);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

		Beer updatedBeer = beerRepository.findById(beer.getId()).get();
		assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
	}

	@Test
	void testUpdateNotFound() {
		assertThrows(NotFoundException.class, () -> {
			beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
		});
	}

	@Rollback
	@Transactional
	@Test
	void testDeleteBeerById() {
		Beer beer = beerRepository.findAll().get(0);

		ResponseEntity responseEntity = beerController.deleteBeerById(beer.getId());
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(202));

		assertThat(beerRepository.findById(beer.getId())).isEmpty();
	}

	@Test
	void testDeleteByIDNotFound() {
		assertThrows(NotFoundException.class, () -> {
			beerController.deleteBeerById(UUID.randomUUID());
		});
	}

	@Rollback
	@Transactional
	@Test
	void testPatchBeerById() {
		Beer beer = beerRepository.findAll().get(0);
		BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);

		beerDTO.setId(null);
		beerDTO.setVersion(null);
		final String beerName = "Patched Beer";
		beerDTO.setBeerName(beerName);

		ResponseEntity responseEntity = beerController.patchBeerById(beer.getId(), beerDTO);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

		Beer updatedBeer = beerRepository.findById(beer.getId()).get();
		assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
	}

	@Test
	void testPatchBeerByIdBadName() throws Exception {
		Beer beer = beerRepository.findAll().get(0);

		Map<String, Object> beerMap = new HashMap<>();
		beerMap.put("beerName",
				"New Name 01234567890123456789012345678901234567890123456789012345678901234567890123456789");

		mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId()).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(beerMap)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testListBeersByName() throws Exception {
		mockMvc.perform(get(BeerController.BEER_PATH).queryParam("beerName", "IPA")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(336)));
	}

	@Test
	void testListBeersByBeerStyle() throws Exception {
		mockMvc.perform(get(BeerController.BEER_PATH)
				.queryParam("beerStyle", BeerStyle.IPA.name()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(548)));
	}

	@Test
	void testListBeersByBeerNameAndStyleShowInventoryFalse() throws Exception {
		mockMvc.perform(get(BeerController.BEER_PATH)
						.queryParam("beerName", "IPA")
						.queryParam("beerStyle", BeerStyle.IPA.name())
						.queryParam("showInventory", "false"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(310)))
				.andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.nullValue()));
	}

	@Test
	void testListBeersByBeerNameAndStyleShowInventoryTrue() throws Exception {
		mockMvc.perform(get(BeerController.BEER_PATH)
						.queryParam("beerName", "IPA")
						.queryParam("beerStyle", BeerStyle.IPA.name())
						.queryParam("showInventory", "true"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(310)))
				.andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.notNullValue()));
	}
}