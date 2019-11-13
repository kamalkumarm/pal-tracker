package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(MysqlDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry find(long nonExistentTimeEntryId) {
        String query = "Select * from time_entries where id = ?";

        List<TimeEntry> timeEntry = jdbcTemplate.query(query, new Object[] {nonExistentTimeEntryId},(rs, i) ->{
            return new TimeEntry(rs.getLong("id"), rs.getLong("project_id"), rs.getLong("user_id"), rs.getDate("date").toLocalDate(), rs.getInt("hours"));
        });
        if(timeEntry != null && timeEntry.size()>0)
            return timeEntry.get(0);
        else
            return null;
    }

    @Override
    public List<TimeEntry> list() {
        String query = "Select * from time_entries";
        List<TimeEntry> timeEntry = jdbcTemplate.query(query, (rs, rowNum) ->
            new TimeEntry(
                    rs.getLong("id"),
                    rs.getLong("project_id"),
                    rs.getLong("user_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getInt("hours")
            )
        );
        return timeEntry;
    }

    @Override
    public TimeEntry update(long eq, TimeEntry any) {
        String updateQuery = "UPDATE time_entries SET " +
                "project_id = ?, " +
                "user_id = ?, " +
                "date = ?, " +
                "hours = ? " +
                "WHERE id = ?;";

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps =
                        con.prepareStatement(updateQuery);
                ps.setLong(1, any.getProjectId());
                ps.setLong(2,any.getUserId());
                ps.setDate(3, Date.valueOf(any.getDate()));
                ps.setInt(4, any.getHours());
                ps.setLong(5, eq);
                return ps;
            }
        };
        jdbcTemplate.update(psc);
        return find(eq);
    }

    @Override
    public void delete(long timeEntryId) {
        String deleteQuery = "DELETE from time_entries WHERE id = "+ timeEntryId;
        jdbcTemplate.execute(deleteQuery);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntryToCreate) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        String insertQuery = "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                "VALUES (?,?,?,?);";

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps =
                        con.prepareStatement(insertQuery, new String[] {"id"});
                //ps.setLong(1, timeEntryToCreate.getId());
                ps.setLong(1, timeEntryToCreate.getProjectId());
                ps.setLong(2,timeEntryToCreate.getUserId());
                ps.setDate(3, Date.valueOf(timeEntryToCreate.getDate()));
                ps.setInt(4, timeEntryToCreate.getHours());
                return ps;
            }
        };
        jdbcTemplate.update(psc, holder);
        return find(holder.getKey().longValue());
    }
}
