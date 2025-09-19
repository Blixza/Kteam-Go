package com.blixza.store.game;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {
    @GetMapping("/games/")
    public List<GameStorage> getGames() throws SQLException {
        var url = "jdbc:sqlite:database.db";
        var sql = "SELECT id, name, price, creator FROM Games";
        List<GameStorage> list = new ArrayList<>();

        try (var conn = DriverManager.getConnection(url);
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                list.add(new GameStorage(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("creator")
                ));
            }
        }
        return list;
    }
}
