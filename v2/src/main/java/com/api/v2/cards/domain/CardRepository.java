package com.api.v2.cards.domain;

import com.api.v2.cards.domain.exposed.Card;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CardRepository extends MongoRepository<Card, ObjectId> {
}
