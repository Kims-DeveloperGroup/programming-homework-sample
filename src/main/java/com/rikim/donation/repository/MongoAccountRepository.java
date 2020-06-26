package com.rikim.donation.repository;

import com.rikim.donation.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class MongoAccountRepository implements AccountRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Account find(long userId) {
        return mongoTemplate
                .findOne(Query.query(new Criteria("userId").is(userId)), Account.class);
    }

    @Override
    public void insert(Account account) {
        mongoTemplate.insert(account);
    }

    @Override
    public void updateBalance(long userId, long amount) {
        Query query = Query.query(new Criteria("userId").is(userId));
        Update update = new Update();
        update.inc("balance", amount);
        mongoTemplate.updateFirst(query, update, Account.class);
    }
}