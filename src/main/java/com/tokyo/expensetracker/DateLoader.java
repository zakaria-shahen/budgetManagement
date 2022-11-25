package com.tokyo.expensetracker;

import com.tokyo.expensetracker.model.Household;
import com.tokyo.expensetracker.model.Role;
import com.tokyo.expensetracker.model.User;
import com.tokyo.expensetracker.service.HouseholdService;
import com.tokyo.expensetracker.service.RoleServices;
import com.tokyo.expensetracker.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DateLoader implements ApplicationRunner {

    private final UserService userService;
    private final RoleServices roleServices;
    private final HouseholdService householdService;

    public DateLoader(UserService userService, RoleServices roleServices, HouseholdService householdService) {
        this.userService = userService;
        this.roleServices = roleServices;
        this.householdService = householdService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Household household = Household.builder()
                .totalBalance(BigDecimal.valueOf(100))
                .name("household one").build();

        List<Role> roles =  List.of(
                new Role("OWNER"),
                new Role("CO-OWNER"),
                new Role("STAFF")
        );

        household = householdService.create(household);
        roles = roleServices.saveAll(roles);

        userService.saveUser(
                User.builder()
                        .role(roles.get(0))
                        .password("pass")
                        .name("omar")
                        .household(household).build()
        );


    }
}
