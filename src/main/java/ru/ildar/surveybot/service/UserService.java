package ru.ildar.surveybot.service;

import ru.ildar.surveybot.entity.UserEntity;
import java.util.List;
 
public interface UserService {
    UserEntity createUser(String name, String email);
    UserEntity getUserByEmail(String email);
    List<UserEntity> getAllUsers();
} 