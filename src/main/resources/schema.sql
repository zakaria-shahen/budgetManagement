drop table if exists  `transaction`, user_, `role`, household;

create table household
(
    id                bigint auto_increment primary key,
    name              varchar(255)   not null,
    total_balance     decimal(19, 2) not null check(total_balance > -1),
    created_at        datetime       not null default CURRENT_TIMESTAMP,
    monthly_deposits  decimal(19, 2) check(monthly_deposits > -1),
    monthly_spendings decimal(19, 2) check(monthly_spendings > -1),
    greeting_msg      varchar(255),
    invitation_code   varchar(255)
);

create table `role`
(
    id   tinyint auto_increment primary key,
    name varchar(255) not null
);

create table user_
(
    id           bigint auto_increment primary key,
    name         varchar(255) not null,
    household_id bigint       not null,
    role_id      tinyint      not null,
    foreign key(role_id) references role(id),
    foreign key(household_id) references household(id)
);

create table `transaction`
(
    id           bigint auto_increment primary key,
    type         enum('WITHDRAW', 'DEPOSIT'),
    amount       decimal(19, 2) not null check(amount > 0),
    date         datetime       not null default CURRENT_TIMESTAMP,
    memo         varchar(255)   not null,
    household_id bigint,
    user_id      bigint,
    foreign key (household_id) references household (id),
    foreign key (user_id) references user_ (id)
);
