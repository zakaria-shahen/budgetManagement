
# Table of Contents

1.  [<span class="underline">BudgetManagement</span>](#orge773894)
    1.  [`Resources`](#org41ff392)
        1.  [Roles](#orge16540f)
        2.  [Users](#orgc180be2)
        3.  [Transactions](#org2302c6c)
        4.  [Households](#orgdccf1c3)
        5.  [Reports](#org86275b5)
    2.  [`Representations`](#org121ee84)
        1.  [Role](#org8edbe86)
        2.  [User](#orgaf0fd6f)
        3.  [Transaction](#orgb79f9fe)
        4.  [Household](#org7e85adf)
        5.  [Report](#orgcd3e990)
    3.  [`Endpoints`](#org9041661)
        1.  [Base URI: http://localhost:8080/api/v1](#org5377b31)
        2.  [Users](#org33425bb)
        3.  [Transactions](#orgff6a614)
        4.  [Households](#org9b04f28)
        5.  [Reports](#org5028c5f)

---

---


<a id="orge773894"></a>

# <span class="underline">BudgetManagement</span>


<a id="org41ff392"></a>

## `Resources`


<a id="orge16540f"></a>

### Roles


<a id="orgc180be2"></a>

### Users


<a id="org2302c6c"></a>

### Transactions


<a id="orgdccf1c3"></a>

### Households


<a id="org86275b5"></a>

### Reports


<a id="org121ee84"></a>

## `Representations`


<a id="org8edbe86"></a>

### Role

Defines User privileges in the system.

    {
        "id": 1,
        "name": "OWNER" | "CO_OWNER" | "STAFF"
    }


<a id="orgaf0fd6f"></a>

### User

Main actor in the system.

    {
        "id": 1,
        "name": "Mostafa Hares",
        "role": { "id": 1 }
    }


<a id="orgb79f9fe"></a>

### Transaction

Money operation, done by a User and recorded in a Household.

    {...}


<a id="org7e85adf"></a>

### Household

A group of Users with thier Transactions.

    {
        "id": 1,
        "invitation_code": "#12345",
        "name": "Tokyo",
        "greeting_msg": "Welcome to Tokyo household."
        "owner": { "id": 1 },
        "members": [ {"id": 1}, ... ],
        // "budgets": [ {"name": "Entertainment", "Description": "Games, Movies", "max_amount": 300}, ... ],
        "transactions": [ {...}, ... ],
        "total_balance": 00.00,
        "monthly_spendings": 00.00,
        "monthly_deposits": 00.00,
        "created_at": "03-10-2022"
    }


<a id="orgcd3e990"></a>

### Report

&#x2026;

    {...}


<a id="org9041661"></a>

## `Endpoints`


<a id="org5377b31"></a>

### Base URI: <http://localhost:8080/api/v1>


<a id="org33425bb"></a>

### Users

1.  getAllUsers:   [GET ] /users

2.  getUserByName: [GET ] /users/{name}

3.  createUser:    [POST] /users

4.  updateUser:    [PUT ] /users


<a id="orgff6a614"></a>

### Transactions

1.  CRUD


<a id="org9b04f28"></a>

### Households

1.  households

    1.  getAllHouseholds: [GET   ] /households

    2.  getHouseholdById: [GET   ] /households/{id}

    3.  createHousehold:  [POST  ] /households

    4.  updateHousehold:  [PUT   ] /households/{id}

    5.  deleteHousehold:  [DELETE] /households/{id}

2.  memebers

    1.  getAllMembersUnderHousehold: [GET   ] /households/{code}/members

    2.  addMemberUnderHousehold:     [POST  ] /households/{code}/members

    3.  deleteMemberUnderHousehold:  [DELETE] /households/{code}/members/{id}

3.  transactions

    1.  getAllTransactionsUnderHousehold: [GET ] /households/{code}/transactions

    2.  getTransactionUnderHousehold:     [GET ] /households/{code}/transactions/{id}

    3.  createTransactionUnderHousehold:  [POST] /households/{code}/transactions


<a id="org5028c5f"></a>

### Reports

1.  CRUD

