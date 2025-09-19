package com.blixza.store;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static void create() {
        String url = "jdbc:sqlite:database.db";

        try (var conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                var meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void connect() {
        String path = "database.db";
        var url = "jdbc:sqlite:" + path;

        try (var conn = DriverManager.getConnection(url)) {
            System.out.println("Connected to the " + path);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTable() {
        var url = "jdbc:sqlite:database.db";

        var sql = """
                CREATE TABLE IF NOT EXISTS warehouses (
                    id      INTEGER PRIMARY KEY,
                    name    text NOT NULL,
                    capacity REAL
                );
                """;

        try (var conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                var stmt = conn.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void write() {
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
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String read() {
        var url = "jdbc:sqlite:database.db";
        var sql = "SELECT id, name, capacity FROM warehouses";

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql);

        ) {
            while (rs.next()) {
                System.out.printf("%-5s%-25s%-10s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("capacity")
                );
                return "" + rs.getInt("id") + rs.getString("name") + rs.getDouble("capacity");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }

        return "Meow";
    }

    public static void read2() {
        var url = "jdbc:sqlite:database.db";
        var sql = "SELECT id, name, capacity FROM warehouses WHERE capacity > ?";
        var capacity = 3600;

        try (var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(sql)
        ) {
            stmt.setDouble(1, capacity);

            var rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.printf("%-5s%-25s%-10s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("capacity")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void update() {
        var url = "jdbc:sqlite:database.db";
        var sql = """
                UPDATE      warehouses SET name = ?,
                capacity =  ?
                WHERE id =  ?
                """;

        var name = "Finished Products";
        var capacity = 500;
        var id = 3;

        try (var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, name);
            stmt.setDouble(2, capacity);
            stmt.setInt(3, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void delete() {
        var url = "jdbc:sqlite:database.db";
        var sql = "DELETE FROM warehouses WHERE id = ?";
        var id = 3;

        try (var conn = DriverManager.getConnection(url);
        var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void drop() {
        var url = "jdbc:sqlite:database.db";

        try (var stmt = DriverManager.getConnection(url).createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS warehouses");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

    }
}
