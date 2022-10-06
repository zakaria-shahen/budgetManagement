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
import com.myCompany.budgetManagement.model.User;
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
        householdService.create(household);

        // Set the location header for the newly created Household
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newHouseholdlUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(household.getId())
                .toUri();
        responseHeaders.setLocation(newHouseholdlUri);

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
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
        List<User> allMemebers = householdService.findAllMembers(householdId);
        return new ResponseEntity<>(allMemebers, HttpStatus.OK);
    }

    @PostMapping("/{householdId}/members")
    public ResponseEntity<?> addMemberToHousehold(@PathVariable Long householdId, @RequestBody Long memberId) {
        householdService.addMember(householdId, memberId);

        // Set the location header for the newly created Member
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newMemberlUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(memberId)
                .toUri();
        responseHeaders.setLocation(newMemberlUri);

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

    @DeleteMapping("/{householdId}/members/{memberId}")
    public ResponseEntity<?> removeMemberFromHousehold(@PathVariable Long householdId, @PathVariable Long memberId) {
        householdService.deleteMember(householdId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
