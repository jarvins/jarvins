package com.jarvins.builder.constructor;

import com.jarvins.builder.*;
import com.jarvins.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class UserConstructor {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static User create() {
        int age = RANDOM.nextInt(18, 65);
        LocalDate born = DayBuilder.build(age);
        boolean sex = RANDOM.nextBoolean();
        return User.builder()
                .name(NameBuilder.build(sex))
                .sex(sex)
                .age(age)
                .phone(PhoneBuilder.build())
                .born(born)
                .zodiac(ZodiacBuilder.build(born.getYear()))
                .address(AddressBuilder.build().getAddress())
                .married(RANDOM.nextBoolean())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }

    public static List<User> batchCreate(int size){
        List<User> users = new LinkedList<>();
        int i = 0;
        while (i++ < size){
            users.add(create());
        }
        return users;
    }
}
