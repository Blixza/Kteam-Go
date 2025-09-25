package com.blixza.store.repo;

import com.blixza.store.wishlist.WishlistStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepo extends JpaRepository<WishlistStorage, Integer> {
    List<WishlistStorage> findByUserId(Integer userId);
    void deleteByUserIdAndGameId(Integer userId, Integer gameId);
}
