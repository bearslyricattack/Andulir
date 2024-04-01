package org.andulir.controller;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    String name;
    String password;
    User1 user1;
    Date date;
}