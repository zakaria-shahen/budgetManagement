package com.myCompany.budgetManagement.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.myCompany.budgetManagement.model.Household;
import com.myCompany.budgetManagement.service.HouseholdService;

@RestController
@RequestMapping("/api/v1/households")
public class HouseholdController {

    private HouseholdService householdService;

    public HouseholdController(HouseholdService householdService) {
        this.householdService = householdService;
    }

    @GetMapping
    public ResponseEntity<List<Household>> getAllHouseholds() {
        return new ResponseEntity<>(householdService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Household> getHousehold(@PathVariable Long id) {
        return new ResponseEntity<>(householdService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postHousehold(@RequestBody Household household) {
        householdService.create(household);

        // Set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newHouseholdlUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(household.getId())
                .toUri();
        responseHeaders.setLocation(newHouseholdlUri);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHousehold(@PathVariable Long id, @RequestBody Household household) {
        householdService.update(id, household);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHousehold(@PathVariable Long id) {
        householdService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
