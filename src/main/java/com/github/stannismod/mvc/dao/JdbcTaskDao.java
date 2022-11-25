package com.github.stannismod.mvc.dao;

import com.github.stannismod.mvc.model.Task;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class JdbcTaskDao extends JdbcDaoSupport implements TaskDao {

    public JdbcTaskDao(DataSource source) {
        super();
        setDataSource(source);
        getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS Tasks (" +
                "name VARCHAR(100) PRIMARY KEY," +
                "description VARCHAR(1000)," +
                "due timestamp," +
                "status INT)");
    }

    @Override
    public void addTask(final Task task) {
        String sql = "INSERT INTO Tasks (name, description, due, status) VALUES (?, ?, ?, ?)";
        getJdbcTemplate().update(sql,
                task.getName(), task.getDescription(), task.getDue().getTime(), task.getStatus().ordinal());
    }

    @Override
    public void removeTask(final String name) {
        String sql = "DELETE FROM Tasks WHERE name='" + name + "'";
        getJdbcTemplate().update(sql);
    }

    @Override
    public Task getTaskByName(final String name) {
        String sql = "SELECT * FROM Tasks WHERE name='" + name + "'";
        return getProductsByRequest(sql).stream().findAny().orElse(null);
    }

    @Override
    public void setTaskStatus(final String name, final Task.Status status) {
        String sql = "UPDATE Tasks SET status=" + status.ordinal() + " WHERE name='" + name + "'";
        getJdbcTemplate().update(sql);
    }

    @Override
    public List<Task> getTasks() {
        String sql = "SELECT * FROM Tasks";
        return getProductsByRequest(sql);
    }

    @Override
    public List<Task> getActualTasks() {
        String sql = "SELECT * FROM Tasks WHERE due > date('now') AND status=" + Task.Status.ASSIGNED.ordinal();
        return getProductsByRequest(sql);
    }

    @Override
    public List<Task> getOverdueTasks() {
        String sql = "SELECT * FROM Tasks WHERE due <= date('now') AND status=" + Task.Status.ASSIGNED.ordinal();
        return getProductsByRequest(sql);
    }

    @Override
    public List<Task> getCompletedTasks() {
        String sql = "SELECT * FROM Tasks WHERE status=" + Task.Status.COMPLETED.ordinal();
        return getProductsByRequest(sql);
    }

    @SuppressWarnings("all")
    private List<Task> getProductsByRequest(String sql) {
        return getJdbcTemplate().query(sql, new RowMapper<>() {

            @Override
            public Task mapRow(final ResultSet resultSet, final int i) throws SQLException {
                Task task = new Task();
                task.setName(resultSet.getString("name"));
                task.setDescription(resultSet.getString("description"));
                task.setDue(new Date(resultSet.getTimestamp("due").getTime()));
                task.setStatus(Task.Status.values()[resultSet.getInt("status")]);
                return task;
            }
        });
    }
}
