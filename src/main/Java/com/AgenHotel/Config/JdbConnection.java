package com.AgenHotel.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbConnection {

    private static final String URL = "jdbc:mysql://mysql:3306/agenhotel?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "agenhotel_user";
    private static final String PASSWORD = "agenhotel123";

    private static JdbConnection instance;
    private Connection connection;

    private JdbConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL NÃO ENCONTRADO NO PROJETO", e);
        }
    }

    public static synchronized JdbConnection getInstance() {
        if (instance == null) {
            instance = new JdbConnection();
        }
        return instance;
    }

    private Connection getConnect() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return this.connection;
    }

    public ResultSet executar(String sql, Object... parametros) throws SQLException {
        Connection conn = getConnect();
        PreparedStatement ps = conn.prepareStatement(sql);

        for (int i = 0; i < parametros.length; i++) {
            ps.setObject(i + 1, parametros[i]);
        }

        return ps.executeQuery();
    }

    public int executarUpdate(String sql, Object... parametros) throws SQLException {
        Connection conn = getConnect();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }
            return ps.executeUpdate();
        }
    }
}


