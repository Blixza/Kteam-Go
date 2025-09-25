package com.blixza.store.repo;

import com.blixza.store.user.UserStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserStorage, Integer> {

}
