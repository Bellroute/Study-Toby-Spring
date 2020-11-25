package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class NUserDao extends UserDao{
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // N 사 DB connection 생성코드
    }
}
