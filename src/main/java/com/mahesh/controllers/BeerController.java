package com.mahesh.controllers;

import java.util.List;
import java.util.UUID;

import com.mahesh.exception.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mahesh.dto.BeerDTO;
import com.mahesh.service.BeerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beer";

    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    public List<BeerDTO> listBeers(@RequestParam(required = false) String beerName){
        return beerService.listBeers(beerName);
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerid){

        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(beerid).orElseThrow(NotFoundException::new);
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity handlePost(@Valid @RequestBody BeerDTO beer){

        BeerDTO savedBeer = beerService.saveBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedBeer);
    }


    @PutMapping(BEER_PATH_ID)
    public  ResponseEntity updateById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beer){

        if (beerService.updateBeer(beerId, beer).isEmpty()){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteBeerById(@PathVariable("beerId") UUID id){
        if(!beerService.deleteBeer(id)){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity patchBeerById(@PathVariable("beerId") UUID id, @RequestBody BeerDTO beer){

        beerService.patchById(id, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
