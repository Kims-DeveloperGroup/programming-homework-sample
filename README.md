# Money donation application

## Runbook
- prerequisite (java 11, maven, docker installation)

### Run `make` in the project base path
: `make` runs the application(spring-boot port:8080) and its db (mongodb port: 27017)

To run independently 
- `make runApplication` runs and builds only the application
- `make runDB` runs only database


### Tests
Test cases runs in Controller and Service layers. <br/>
Repository layer is mocked. <br/>
Run `mvn test`

## Architecture
- Three layered - Controller, Service, Repository

#### Controller: DonationController
##### APIs
- GET: /donations/{donationId} <br/>
: get a donation information <br/>
: Response: `Donation` json
<br/><br/>
- POST: /donations   `body= {amount: number, dividendCount:number}` <br/>
: create a donation <br/>
: Response: generated donationId
<br/><br/>
- PUT: /donations/{donationId} <br/>
: Update one of the dividends' doneeId in the Donation <br/>
and deposit the amount of the dividend into a request user account. <br/>
: Response: the dividend's amount to be deposited.


#### Services
##### 1) DonationService
DonationService is in charge of managing donations
<br/>
##### 2) AccountService
AccountService is in charge of managing user accounts <br/>
######NOTE: AccountService generates an account when request users do not owns accounts with a gift $10000
<br/>
<br/>

#### Repositories
##### 1) DonationRepository
DonationRepository stores, updates and queries `Donation`
<br/>
<br/>
##### 2) AccountRepository
AccountRepository stores, updates and queries `Account`


#### Entities
Donation
```  private String id;  // donationId or token
     private long userId; // userId who generated the donation
     private String roomId; // roomId in which the donation is valid
     private long amount; // Total amount of all dividiends
     private List<Dividend> dividends; // the amount is distributed randomly to the dividens
     private Instant created;  // created date of the Donation
```

Dividend
```
    long amount; //distributed amount from Donation
    long doneeUserId;  // userId who occupies the dividend, otherwise zero(0)
```

Account
```
    Long userId; //userid who owns the account
    long balance = 0; // the account balance
```
