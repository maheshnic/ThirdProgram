package com.mahesh.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.mahesh.repositories.BeerRepository;
import com.mahesh.repositories.CustomerRepository;
import com.mahesh.service.BeerCsvService;
import com.mahesh.service.BeerCsvServiceImpl;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
public class BootstrapDataTest {

	@Autowired
	BeerRepository beerRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	BeerCsvService beerCsvService;

	BootstrapData bootstrapData;

	@BeforeEach
	void setUp() {
		bootstrapData = new BootstrapData(beerRepository, customerRepository, beerCsvService);
	}

	@Test
	void Testrun() throws Exception {
		bootstrapData.run(null);

		assertThat(beerRepository.count()).isEqualTo(2413);
		assertThat(customerRepository.count()).isEqualTo(3);
	}
}
