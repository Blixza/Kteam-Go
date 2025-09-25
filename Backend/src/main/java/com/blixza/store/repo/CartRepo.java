package com.blixza.store.repo;

import com.blixza.store.cart.CartStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<CartStorage, Integer> {
    void deleteByUserIdAndGameId(Integer userId, Integer gameId);
}
