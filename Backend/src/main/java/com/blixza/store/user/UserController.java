package com.blixza.store.user;

import com.blixza.store.wishlist.WishlistRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @GetMapping("/users/")
    public List<UserStorage> getUsers() throws SQLException {
        var url = "jdbc:sqlite:database.db";
        var sql = "SELECT id, username, nickname FROM Users";
        List<UserStorage> list = new ArrayList<>();

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql);
        ) {
            while (rs.next()) {
                list.add(new UserStorage(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("nickname")
                ));
            }
        }
        return list;
    }

    @GetMapping("/users/me")
    public UserStorage getMe() throws SQLException {
        var url = "jdbc:sqlite:database.db";
        var sql = "SELECT id, username, nickname FROM Users WHERE id == ?";

        try (var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(sql);
            var rs = stmt.executeQuery()
        ) {
            if (rs.next()) {
                stmt.setInt(1, 1);
                return new UserStorage(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("nickname")
                );
            }
        }
        return null;
    }

    @GetMapping("/users/me/nickname")
    public String getMyNickname() throws SQLException {
        var url = "jdbc:sqlite:database.db";
        var sql = "SELECT nickname FROM Users WHERE id == ?";
        String nickname;

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, 1);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nickname");
                } else {
                    return "No user found";
                }
            }
        }
    }

    @GetMapping("users/me/username")
    public String getMyUsername() throws SQLException {
        var url = "jdbc:sqlite:database.db";
        var sql = "SELECT username FROM Users WHERE id == ?";
        String username;

        try (var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, 1);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                } else {
                    return "No user found";
                }
            }

        }
    }

    @GetMapping("users/me/wishlist")
    public List<Integer> getMyWishlist() throws SQLException {
        var url = "jdbc:sqlite:database.db";
        var sql = """
                SELECT game_id
                FROM Wishlist
                WHERE user_id = ?;
                """;

        var wishlist = new ArrayList<Integer>();

        try (var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, 1);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    wishlist.add(rs.getInt("game_id"));
                }
            }
            return wishlist;
        }
    }

    @PostMapping("users/me/wishlist/add")
    public String addToMyWishlist(@RequestBody WishlistRequest request) throws SQLException {
        var url = "jdbc:sqlite:database.db";

        try (var conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);

            try (var stmt = conn.prepareStatement(
                    "INSERT OR IGNORE INTO Wishlist (user_id, game_id) VALUES (?, ?)"
            )) {
                stmt.setInt(1, request.getUserId());
                stmt.setInt(2, request.getGameId());
                stmt.executeUpdate();
            }

            conn.commit();
            return "Success";
        }
    }

    @PostMapping("users/me/wishlist/remove")
    public String removeFromMyWishlist(@RequestBody WishlistRequest request) throws SQLException {
        var url = "jdbc:sqlite:database.db";

        try (var conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);

            try (var stmt = conn.prepareStatement(
                    "DELETE FROM Wishlist WHERE user_id = ? AND game_id = ?"
            )) {
                stmt.setInt(1, request.getUserId());
                stmt.setInt(2, request.getGameId());
                stmt.executeUpdate();
            }
            conn.commit();
        }


        return "";
    }
}
