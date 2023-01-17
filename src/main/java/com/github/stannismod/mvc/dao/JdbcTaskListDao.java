package com.github.stannismod.mvc.dao;

import com.github.stannismod.mvc.model.Task;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class JdbcTaskListDao extends JdbcDaoSupport implements TaskListDao {

    public JdbcTaskListDao(DataSource source) {
        super();
        setDataSource(source);
        getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS TaskLists (" +
                "name VARCHAR(100) UNIQUE PRIMARY KEY" +
        ")");
        getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS Tasks (" +
                "name VARCHAR(100)," +
                "list VARCHAR(100)," +
                "description VARCHAR(1000)," +
                "due timestamp," +
                "status INT," +
                "PRIMARY KEY(name, list)," +
                "FOREIGN KEY(list) REFERENCES TaskLists(name)" +
        ")");
    }

    @Override
    public List<String> getTaskLists() {
        String sql = "SELECT * FROM TaskLists";
        return getJdbcTemplate().query(sql, new SingleColumnRowMapper<>());
    }

    @Override
    public void addList(final String list) {
        String sql = "INSERT INTO TaskLists (name) VALUES (?)";
        getJdbcTemplate().update(sql, list);
    }

    @Override
    public void removeList(final String list) {
        String sql = "DELETE FROM Tasks WHERE list='" + list + "'";
        getJdbcTemplate().update(sql);
        sql = "DELETE FROM TaskLists WHERE name='" + list + "'";
        getJdbcTemplate().update(sql);
    }

    @Override
    public void addTask(final Task task) {
        String sql = "INSERT INTO Tasks (name, list, description, due, status) VALUES (?, ?, ?, ?, ?)";
        getJdbcTemplate().update(sql,
                task.getName(), task.getList(), task.getDescription(), task.getDue().toEpochSecond(ZoneOffset.UTC), task.getStatus().ordinal());
    }

    @Override
    public void removeTask(final String list, final String name) {
        String sql = "DELETE FROM Tasks WHERE name='" + name + "'" + " AND list='" + list + "'";
        getJdbcTemplate().update(sql);
    }

    @Override
    public Task getTaskByName(final String list, final String name) {
        String sql = "SELECT * FROM Tasks WHERE name='" + name + "'" + " AND list='" + list + "'";
        return getProductsByRequest(sql).stream().findAny().orElse(null);
    }

    @Override
    public void setTaskStatus(final String list, final String name, final Task.Status status) {
        String sql = "UPDATE Tasks SET status=? WHERE name='" + name + "'" + " AND list='" + list + "'";
        getJdbcTemplate().update(sql, status.ordinal());
    }

    @Override
    public List<Task> getTasks(final String list) {
        String sql = "SELECT * FROM Tasks WHERE list='" + list + "'";
        return getProductsByRequest(sql);
    }

    @Override
    public List<Task> getActualTasks(final String list) {
        String sql = "SELECT * FROM Tasks WHERE due > unixepoch() AND status="
                + Task.Status.ASSIGNED.ordinal() + " AND list='" + list + "'";
        return getProductsByRequest(sql);
    }

    @Override
    public List<Task> getOverdueTasks(final String list) {
        String sql = "SELECT * FROM Tasks WHERE due <= unixepoch() AND status="
                + Task.Status.ASSIGNED.ordinal() + " AND list='" + list + "'";
        return getProductsByRequest(sql);
    }

    @Override
    public List<Task> getCompletedTasks(final String list) {
        String sql = "SELECT * FROM Tasks WHERE status="
                + Task.Status.COMPLETED.ordinal() + " AND list='" + list + "'";
        return getProductsByRequest(sql);
    }

    @SuppressWarnings("all")
    private List<Task> getProductsByRequest(String sql) {
        return getJdbcTemplate().query(sql, new RowMapper<>() {

            @Override
            public Task mapRow(final ResultSet resultSet, final int i) throws SQLException {
                Task task = new Task();
                task.setName(resultSet.getString("name"));
                task.setList(resultSet.getString("list"));
                task.setDescription(resultSet.getString("description"));
                task.setDue(LocalDateTime.ofEpochSecond(resultSet.getTimestamp("due").getTime(), 0, ZoneOffset.UTC));
                task.setStatus(Task.Status.values()[resultSet.getInt("status")]);
                return task;
            }
        });
    }
}
