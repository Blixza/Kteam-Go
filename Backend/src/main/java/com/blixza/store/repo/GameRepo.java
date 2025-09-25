package com.blixza.store.repo;

import com.blixza.store.game.GameStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepo extends JpaRepository<GameStorage, Integer> {

}
