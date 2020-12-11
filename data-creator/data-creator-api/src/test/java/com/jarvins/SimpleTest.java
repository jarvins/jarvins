package com.jarvins;

import com.jarvins.service.DataCreatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class SimpleTest {

    @Autowired
    DataCreatorService service;

    @Test
    public void test() {
        service.createUser(1);
    }

}
