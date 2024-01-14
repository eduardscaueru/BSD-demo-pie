package com.example.demo.repository;

import com.example.demo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UserModel, Long> {
    public UserModel findByUserId(Long userId);

    @Query("SELECT u FROM UserModel u WHERE u.userName = ?1")
    public UserModel findUserByStatusAndName(String userName);
}
