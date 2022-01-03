package book.ch3.domain.daoV3;

import book.ch3.domain.daoV1.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcContext {
    private final DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy stmt) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = this.dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (c != null) {
                try{
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // try-with-resource를 사용하면 더욱 간단하게 close를 실시할 수 있다.
    public void workWithStatementStrategyV1(StatementStrategy stmt) {
        try(Connection c = this.dataSource.getConnection();
            PreparedStatement ps = stmt.makePreparedStatement(c);){
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ResultSet> workWithSelectStatementStrategy(StatementStrategy stmt) {
        List<ResultSet> rsList = new ArrayList<>();
        try (Connection c = this.dataSource.getConnection();
             PreparedStatement ps = stmt.makePreparedStatement(c);) {
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    rsList.add(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return rsList;
    }
}
