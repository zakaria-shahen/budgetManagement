package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.exception.NotEnteredForeignKeyIdException;
import com.tokyo.expensetracker.exception.NotFoundException;
import com.tokyo.expensetracker.model.Household;
import com.tokyo.expensetracker.model.User;
import com.tokyo.expensetracker.repository.RoleRepository;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class HouseholdServiceTest {

    @Autowired
    HouseholdService householdService;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    
    @AfterEach
    void tearDown() {
        userService.deleteAll();
        householdService.deleteAll();
    }

    @Nested
    @DisplayName("Tests for findAll method")
    class findAll {
        @Test
        void shouldReturnAllHouseholds() {
            Household household1 = new Household();
            household1.setName("household1");
            household1.setTotalBalance(BigDecimal.valueOf(2000));
            householdService.create(household1);

            Household household2 = new Household();
            household2.setName("Household2");
            household2.setTotalBalance(BigDecimal.valueOf(2000));
            householdService.create(household2);

            Household household3 = new Household();
            household3.setName("Household3");
            household3.setTotalBalance(BigDecimal.valueOf(3000));
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
            household.setTotalBalance(BigDecimal.valueOf(1000));
            householdService.create(household);

            assertAll(
                    () -> assertInstanceOf(Household.class, householdService.findById(household.getId())),
                    () -> assertEquals("Household", householdService.findById(household.getId()).getName())
            );
        }

        @Test
        void shouldThrowNotFoundException() {
            assertThrows(NotFoundException.class, () -> householdService.findById(123456L));
        }
    }

    @Nested
    @DisplayName("Tests for findByInvitationCode method")
    class findByInvitationCode {
        @Test
        void shouldReturnHousehold() {
            Household household = new Household();
            household.setName("Household");
            household.setInvitationCode("123456");
            household.setTotalBalance(BigDecimal.valueOf(1000));
            householdService.create(household);

            assertAll(
                    () -> assertInstanceOf(Household.class, householdService.findByInvitationCode("123456")),
                    () -> assertEquals("Household", householdService.findByInvitationCode("123456").getName())
            );
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
            household.setTotalBalance(BigDecimal.valueOf(1000));
            householdService.create(household);

            assertAll(
                    () -> assertEquals(1, householdService.findAll().size()),
                    () -> assertInstanceOf(Household.class, householdService.findById(household.getId())),
                    () -> assertEquals("Household", householdService.findById(household.getId()).getName())
            );
        }

        @Test
        void shouldThrowNotEnteredForeignKeyIdExceptionIfNameNull() {
            Household household = new Household();
            household.setTotalBalance(BigDecimal.valueOf(1000));
            assertThrows(NotEnteredForeignKeyIdException.class, () -> householdService.create(household));
        }

        @Test
        void shouldThrowNotEnteredForeignKeyIdExceptionIfBalanceNull() {
            Household household = new Household();
            household.setName("Household");
            assertThrows(NotEnteredForeignKeyIdException.class, () -> householdService.create(household));
        }

        @Test
        void shouldThrowNotEnteredForeignKeyIdExceptionIfBothNull() {
            Household household = new Household();
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
            household.setTotalBalance(BigDecimal.valueOf(1000));
            householdService.create(household);

            Household household2 = new Household();
            household2.setName("Household2");
            household2.setTotalBalance(BigDecimal.valueOf(2000));
            householdService.update(household.getId(), household2);

            assertAll(
                    () -> assertEquals(1, householdService.findAll().size()),
                    () -> assertInstanceOf(Household.class, householdService.findById(household.getId())),
                    () -> assertEquals("Household2", householdService.findById(household.getId()).getName())
            );
        }

        @Test
        void shouldThrowNotFoundException() {
            Household household = new Household();
            household.setName("Household");
            household.setTotalBalance(BigDecimal.valueOf(1000));
            assertThrows(NotFoundException.class, () -> householdService.update(123456L, household));
        }

        @Test
        void shouldThrowNotEnteredForeignKeyIdExceptionIfNameNull() {
            Household household = new Household();
            household.setName("Household");
            household.setTotalBalance(BigDecimal.valueOf(1000));
            householdService.create(household);

            Household household2 = new Household();

            assertThrows(NotEnteredForeignKeyIdException.class, () -> householdService.update(household.getId(), household2));
        }
    }

    @Nested
    @DisplayName("Tests for deleteById method")
    class deleteById {
        @Test
        void shouldDeleteHousehold() {
            Household household = new Household();
            household.setName("Household");
            household.setTotalBalance(BigDecimal.valueOf(1000));
            householdService.create(household);

            householdService.deleteById(household.getId());

            assertEquals(0, householdService.findAll().size());
        }

        @Test
        void shouldThrowNotFoundException() {
            assertThrows(NotFoundException.class, () -> householdService.deleteById(123456L));
        }
        // TODO: add test for deleting household with members
    }

    @Nested
    @DisplayName("Tests for findAllMembers method")
    class findAllMembers {
        @Test
        void shouldReturnAllMembers() {
            Household household = new Household();
            household.setName("Household");
            household.setTotalBalance(BigDecimal.valueOf(1000));

            User user1 = new User();
            user1.setName("User");
            user1.setHousehold(household);
            user1.setRole(roleRepository.findByName("co-owner").get());
            User user2 = new User();
            user2.setName("User2");
            user1.setHousehold(household);
            user1.setRole(roleRepository.findByName("member").get());
            userService.saveUser(user1);
            userService.saveUser(user2);

            household.setMembers(List.of(user1, user2));
            householdService.create(household);

            householdService.findAllMembers(household.getId());

            assertEquals(3, householdService.findAllMembers(household.getId()).size());
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
            household.setTotalBalance(BigDecimal.valueOf(1000));
            householdService.create(household);

            User user = new User();
            user.setName("User");
            user.setHousehold(household);
            user.setRole(roleRepository.findByName("owner").get());
            userService.saveUser(user);

            householdService.addMember(household.getId(), user.getId());

            assertAll(
                    () -> assertEquals(1, householdService.findAllMembers(household.getId()).size()),
                    () -> assertEquals("User", householdService.findAllMembers(household.getId()).get(0).getName()),
                    () -> assertInstanceOf(User.class, householdService.findAllMembers(household.getId()).get(0))
            );

        }

        @Test
        void shouldThrowNotFoundExceptionIfHouseholdDoesNotExist() {
            User user = new User();
            user.setName("User");
            assertThrows(NotFoundException.class, () -> householdService.addMember(123456L, user.getId()));
        }

        @Test
        void shouldThrowNotFoundExceptionIfUserDoesNotExist() {
            Household household = new Household();
            household.setName("Household");
            household.setTotalBalance(BigDecimal.valueOf(1000));
            householdService.create(household);

            assertThrows(NotFoundException.class, () -> householdService.addMember(household.getId(),123456L));
        }
    }

    @Nested
    @DisplayName("Tests for deleteMember method")
    class removeMember {
        @Test
        void shouldRemoveMember() {
            Household household = new Household();
            household.setName("Household");
            household.setTotalBalance(BigDecimal.valueOf(1000));
            householdService.create(household);

            User user = new User();
            user.setName("User");
            userService.saveUser(user);
            householdService.addMember(household.getId(), user.getId());

            householdService.deleteMember(household.getId(), user.getId());

            assertEquals(0, householdService.findAllMembers(household.getId()).size());
        }

        @Test
        void shouldThrowNotFoundExceptionIfHouseholdDoesNotExist() {
            User user = new User();
            user.setName("User");
            assertThrows(NotFoundException.class, () -> householdService.deleteMember(123456L, user.getId()));
        }

        @Test
        void shouldThrowNotFoundExceptionIfUserDoesNotExist() {
            Household household = new Household();
            household.setName("Household");
            household.setTotalBalance(BigDecimal.valueOf(1000));
            householdService.create(household);

            assertThrows(NotFoundException.class, () -> householdService.deleteMember(household.getId(),123456L));
        }


    }
}