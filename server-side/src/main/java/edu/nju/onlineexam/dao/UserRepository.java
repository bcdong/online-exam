package edu.nju.onlineexam.dao;

import edu.nju.onlineexam.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

    UserEntity findByNumber(String studentNumber);

    UserEntity findByUsernameOrNumber(String username, String number);
}
