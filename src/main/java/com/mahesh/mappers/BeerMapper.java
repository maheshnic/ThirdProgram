package com.mahesh.mappers;

import com.mahesh.dto.BeerDTO;
import com.mahesh.entities.Beer;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);
}
