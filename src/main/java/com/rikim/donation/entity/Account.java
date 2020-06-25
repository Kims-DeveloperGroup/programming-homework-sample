package com.rikim.donation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@AllArgsConstructor
@Data
public class Account {
    @Id
    @Indexed(unique = true)
    @NonNull
    Long userId;
    long balance = 0;
}
