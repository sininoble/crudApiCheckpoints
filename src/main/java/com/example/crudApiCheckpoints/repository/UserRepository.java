package com.example.crudApiCheckpoints.repository;

import com.example.crudApiCheckpoints.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends CrudRepository<User, Integer> {
    User getByEmailId (String email);

    @Modifying
    void updateEmailOnly(int id,String email);

    @Modifying
    int updateUser(int id, String email, String password);
}
