package com.rikim.donation.repository;

import com.rikim.donation.entity.Dividend;
import com.rikim.donation.entity.Donation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class MongoDonationRepository implements DonationRepository {
    private final MongoTemplate mongoTemplate;

    public MongoDonationRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Donation insertDonation(Donation donation) {
        return mongoTemplate.insert(donation);
    }

    @Override
    public Donation findDonation(String donationId, String roomId) {
        return mongoTemplate.findOne(Query.query(new Criteria("id").is(donationId)), Donation.class);
    }

    @Override
    public Dividend findDividend(String donationId, long doneeId) {
        Query grantedForGivenDoneeId = Query.query(new Criteria("id").is(donationId))
                .addCriteria(
                        new Criteria("dividends").elemMatch(
                                new Criteria("doneeUserId").is(doneeId)
                        )
                );
        Donation donation = mongoTemplate.findOne(grantedForGivenDoneeId, Donation.class);
        return donation.getDividends().stream()
                .filter(dividend -> dividend.getDoneeUserId() == doneeId).findFirst().get();
    }

    @Override
    public boolean updateDividendDoneeId(String donationId, long doneeId) {
        Query notGrantedDividend = Query.query(new Criteria("id").is(donationId))
                .addCriteria(
                        new Criteria("dividends").elemMatch(
                                new Criteria("doneeUserId").is(0)
                        )
                );

        Update updateDividendWithDoneeId = new Update();
        updateDividendWithDoneeId.set("dividends.$.doneeUserId", doneeId);
        return mongoTemplate.updateFirst(notGrantedDividend, updateDividendWithDoneeId, Donation.class).getModifiedCount() == 1;
    }
}