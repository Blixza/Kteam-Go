package com.blixza.store.game;

import com.blixza.store.repo.GameRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameRepo repo;

    @GetMapping
    public List<GameStorage> getGames() throws SQLException {
        return repo.findAll();
    }
}
