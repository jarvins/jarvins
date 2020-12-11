package com.jarvins.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class User {

    private int id;

    private String name;

    private boolean sex;

    private int age;

    private String phone;

    private LocalDate born;

    private char zodiac;

    private String address;

    private boolean married;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", born=" + born +
                ", zodiac=" + zodiac +
                ", address='" + address + '\'' +
                ", married=" + married +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
