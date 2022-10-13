package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.exception.NotEnteredForeignKeyIdException;
import com.tokyo.expensetracker.exception.NotFoundException;
import com.tokyo.expensetracker.model.Household;
import com.tokyo.expensetracker.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HouseholdServiceTest {

    @Autowired
    HouseholdService householdService;

@Autowired
    UserService userService;



    @AfterEach
    void tearDown() {
        householdService.deleteAll();
    }

    @Nested
    @DisplayName("Tests for findAll method")
    class findAll {
        @Test
        void shouldReturnAllHouseholds() {
            Household household1 = new Household();
            household1.setName("Household1");
            householdService.create(household1);

            Household household2 = new Household();
            household2.setName("Household2");
            householdService.create(household2);

            Household household3 = new Household();
            household3.setName("Household3");
            householdService.create(household3);

            assertEquals(3, householdService.findAll().size());
        }

        @Test
        void shouldReturnEmptyList() {
            assertEquals(0, householdService.findAll().size());
        }
    }

    @Nested
    @DisplayName("Tests for findById method")
    class findById {
        @Test
        void shouldReturnHousehold() {
            Household household = new Household();
            household.setName("Household");
            householdService.create(household);

            assertEquals(household, householdService.findById(household.getId()));
        }

        @Test
        void shouldThrowNotFoundException() {
            assertThrows(NotFoundException.class, () -> householdService.findById(1L));
        }
    }

    @Nested
    @DisplayName("Tests for findByInvitationCode method")
    class findByInvitationCode {
        @Test
        void shouldReturnHousehold() {
            Household household = new Household();
            household.setName("Household");
            householdService.create(household);

            assertEquals(household, householdService.findByInvitationCode("Household"));
        }

        @Test
        void shouldThrowNotFoundException() {
            assertThrows(NotFoundException.class, () -> householdService.findByInvitationCode("123456"));
        }
    }

    @Nested
    @DisplayName("Tests for create method")
    class create {
        @Test
        void shouldCreateHousehold() {
            Household household = new Household();
            household.setName("Household");
            householdService.create(household);

            assertEquals(1, householdService.findAll().size());
            assertEquals(household, householdService.findById(household.getId()));
        }

        @Test
        void shouldThrowNotEnteredForeignKeyIdException() {
            Household household = new Household();
            household.setName("Household");
            household.setTotalBalance(BigDecimal.valueOf(1000.0));
            assertThrows(NotEnteredForeignKeyIdException.class, () -> householdService.create(household));
        }
    }

    @Nested
    @DisplayName("Tests for update method")
    class update {
        @Test
        void shouldUpdateHousehold() {
            Household household = new Household();
            household.setName("Household");
            householdService.create(household);

            household.setName("Household2");
            householdService.update(household.getId(), household);

            assertEquals(1, householdService.findAll().size());
            assertEquals(household, householdService.findById(household.getId()));
        }

        @Test
        void shouldThrowNotFoundException() {
            Household household = new Household();
            household.setName("Household");
            assertThrows(NotFoundException.class, () -> householdService.update(123456L, household));
        }
    }

    @Nested
    @DisplayName("Tests for deleteById method")
    class deleteById {
        @Test
        void shouldDeleteHousehold() {
            Household household = new Household();
            household.setName("Household");
            householdService.create(household);

            householdService.deleteById(household.getId());

            assertEquals(0, householdService.findAll().size());
        }

        @Test
        void shouldThrowNotFoundException() {
            assertThrows(NotFoundException.class, () -> householdService.deleteById(123456L));
        }
    }

    @Nested
    @DisplayName("Tests for deleteAll method")
    class deleteAll {
        @Test
        void shouldDeleteAllHouseholds() {
            Household household1 = new Household();
            household1.setName("Household1");
            householdService.create(household1);

            Household household2 = new Household();
            household2.setName("Household2");
            householdService.create(household2);

            Household household3 = new Household();
            household3.setName("Household3");
            householdService.create(household3);

            householdService.deleteAll();

            assertEquals(0, householdService.findAll().size());
        }
    }

    @Nested
    @DisplayName("Tests for findAllMembers method")
    class findAllMembers {
        @Test
        void shouldReturnAllMembers() {
            Household household = new Household();
            household.setName("Household");
            householdService.create(household);

            householdService.findAllMembers(household.getId());

            assertEquals(0, householdService.findAllMembers(household.getId()).size());
        }

        @Test
        void shouldThrowNotFoundException() {
            assertThrows(NotFoundException.class, () -> householdService.findAllMembers(123456L));
        }
    }

    @Nested
    @DisplayName("Tests for addMember method")
    class addMember {
        @Test
        void shouldAddMember() {
            Household household = new Household();
            household.setName("Household");
            householdService.create(household);

            User user = new User();
            user.setName("User");
            userService.saveUser(user);

            householdService.addMember(household.getId(), user.getId());

            assertEquals(1, householdService.findAllMembers(household.getId()).size());
        }

        @Test
        void shouldThrowNotFoundException() {
            User user = new User();
            user.setName("User");
            assertThrows(NotFoundException.class, () -> householdService.addMember(123456L, user.getId()));
        }
    }

    @Nested
    @DisplayName("Tests for deleteMember method")
    class removeMember {
        @Test
        void shouldRemoveMember() {
            Household household = new Household();
            household.setName("Household");
            householdService.create(household);

            User user = new User();
            user.setName("User");
            userService.saveUser(user);

            householdService.addMember(household.getId(), user.getId());
            householdService.deleteMember(household.getId(), user.getId());

            assertEquals(0, householdService.findAllMembers(household.getId()).size());
        }

        @Test
        void shouldThrowNotFoundException() {
            User user = new User();
            user.setName("User");
            assertThrows(NotFoundException.class, () -> householdService.deleteMember(123456L, user.getId()));
        }
    }
}