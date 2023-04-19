package com.mahesh.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mahesh.repositories.BeerRepository;
import com.mahesh.repositories.CustomerRepository;

@DataJpaTest
public class BootstrapDataTest {

	@Autowired
	BeerRepository beerRepository;

	@Autowired
	CustomerRepository customerRepository;

	BootstrapData bootstrapData;

	@BeforeEach
	void setUp() {
		bootstrapData = new BootstrapData(beerRepository, customerRepository);
	}

	@Test
	void Testrun() throws Exception {
		bootstrapData.run(null);

		assertThat(beerRepository.count()).isEqualTo(3);
		assertThat(customerRepository.count()).isEqualTo(3);
	}
}
