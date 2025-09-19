package com.blixza.store.cart;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CartController {
    @GetMapping("cart")
    public List<CartStorage> getCart() throws SQLException {
        var url = "jdbc:sqlite:database.db";
        var sql = "SELECT id, user_id, game_id FROM Cart";
        List<CartStorage> list = new ArrayList<>();

        try (var conn = DriverManager.getConnection(url);
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                list.add(new CartStorage(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3)
                ));
            }
        }
        return list;
    }

    @PostMapping("cart/add")
    public void addToCart(@RequestBody CartStorage request) throws SQLException {
        var url = "jdbc:sqlite:database.db";

        try (var conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);

            try (var stmt = conn.prepareStatement("INSERT INTO Cart (user_id, game_id) VALUES (?, ?)")) {
                stmt.setInt(1, request.getUserId());
                stmt.setInt(2, request.getGameId());
                stmt.executeUpdate();
            }

            conn.commit();
        }
    }

    @PostMapping("cart/remove")
    public void removeFromCart(@RequestBody CartStorage request) throws SQLException {
        var url = "jdbc:sqlite:database.db";

        try (var conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);

            try (var stmt = conn.prepareStatement("DELETE FROM Cart WHERE user_id = ? AND game_id = ?")) {
                stmt.setInt(1, request.getUserId());
                stmt.setInt(2, request.getGameId());
                stmt.executeUpdate();
            }
            conn.commit();
        }
    }
}
