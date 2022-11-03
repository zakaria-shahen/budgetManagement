package com.tokyo.expensetracker.controller;

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

import com.tokyo.expensetracker.model.Household;
import com.tokyo.expensetracker.model.User;
import com.tokyo.expensetracker.service.HouseholdService;

@RestController
@RequestMapping("/api/v1/households")
public class HouseholdController {

    private HouseholdService householdService;

    public HouseholdController(HouseholdService householdService) {
        this.householdService = householdService;
    }

    @GetMapping
    public ResponseEntity<List<Household>> getAllHouseholds() {
        List<Household> allHouseholds = householdService.findAll();
        return new ResponseEntity<>(allHouseholds, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Household> getHousehold(@PathVariable Long id) {
        Household household = householdService.findById(id);
        return new ResponseEntity<>(household, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postHousehold(@RequestBody Household household) {

        return new ResponseEntity<>(householdService.create(household), HttpStatus.CREATED);
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

    // -----------------------------------------------------------------------------

    @GetMapping("/{householdId}/members")
    public ResponseEntity<List<User>> getAllMembersOfHousehold(@PathVariable Long householdId) {
        List<User> allMembers = householdService.findAllMembers(householdId);
        return new ResponseEntity<>(allMembers, HttpStatus.OK);
    }

    // TODO: Admin Level
    @DeleteMapping("/{householdId}/members/{memberId}")
    public ResponseEntity<?> removeMemberFromHousehold(@PathVariable Long householdId, @PathVariable Long memberId) {
        householdService.deleteMember(householdId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
