package com.blixza.store.storage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StorageController {
    @GetMapping("/db/read")
    public List<Storage> read() throws SQLException {
        var url = "jdbc:sqlite:database.db";
        var sql = "SELECT id, name, capacity FROM warehouses";
        List<Storage> list = new ArrayList<>();

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql);
        ) {
            while (rs.next()) {
                list.add(new Storage(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("capacity")
                ));
            }
        }
        return list;
    }

    @GetMapping("/db/write")
    public String write() throws SQLException {
        String url = "jdbc:sqlite:database.db";

        var names = new String[] {
                "Raw Materials",
                "Semifinished Goods",
                "Finished Goods"
        };
        var capacities = new int[] {
                3000,
                4000,
                5000
        };

        String sql = """
                INSERT INTO warehouses (
                    name,
                    capacity
                ) VALUES (?, ?)
                """;

        try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < 3; i++) {
                stmt.setString(1, names[i]);
                stmt.setDouble(2, capacities[i]);
                stmt.executeUpdate();
                return "Success";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error: " + e.getMessage();
        }

        return "";
    }
}
