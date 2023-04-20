package com.mahesh.service;

import com.mahesh.dto.BeerDTO;
import com.mahesh.dto.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl(){
        this.beerMap = new HashMap<>();

        BeerDTO beer1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("1235611")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("123561")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(176)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory){
        return new ArrayList<>(beerMap.values());
    }
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        log.debug("Get Beer by Id - Service. Id: " + id.toString());

        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveBeer(BeerDTO beer) {

        BeerDTO savedBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName(beer.getBeerName())
                .version(beer.getVersion())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .price(beer.getPrice())
                .upc(beer.getUpc())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beer) {

        BeerDTO existedBeer = beerMap.get(beerId);
        existedBeer.setVersion(beer.getVersion());
        existedBeer.setBeerName(beer.getBeerName());
        existedBeer.setBeerStyle(beer.getBeerStyle());
        existedBeer.setPrice(beer.getPrice());
        existedBeer.setUpc(beer.getUpc());
        existedBeer.setQuantityOnHand(beer.getQuantityOnHand());
        existedBeer.setUpdateDate(LocalDateTime.now());

        beerMap.put(beerId, existedBeer);
        return Optional.of(existedBeer);
    }

    @Override
    public Boolean deleteBeer(UUID id) {
        beerMap.remove(id);

        return true;
    }

    @Override
    public Optional<BeerDTO> patchById(UUID id, BeerDTO beer) {

        BeerDTO existingBeer = beerMap.get(id);

        if(StringUtils.hasText(beer.getBeerName()))
        {
            existingBeer.setBeerName(beer.getBeerName());
        }

        if (beer.getBeerStyle() != null){
            existingBeer.setBeerStyle(beer.getBeerStyle());
        }

        if (beer.getPrice() != null){
            existingBeer.setPrice(beer.getPrice());
        }

        if (beer.getUpc() != null){
            existingBeer.setUpc(beer.getUpc());
        }

        if (beer.getVersion() != null){
            existingBeer.setVersion(beer.getVersion());
        }

        if (beer.getQuantityOnHand() != null){
            existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        }

        return Optional.of(existingBeer);
    }


}
