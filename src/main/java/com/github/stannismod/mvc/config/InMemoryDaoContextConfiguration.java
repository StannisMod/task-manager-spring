package com.github.stannismod.mvc.config;

import com.github.stannismod.mvc.dao.InMemoryTaskListDao;
import com.github.stannismod.mvc.dao.TaskListDao;
import org.springframework.context.annotation.Bean;

//@Configuration
public class InMemoryDaoContextConfiguration {

    @Bean
    public TaskListDao taskDao() {
        return new InMemoryTaskListDao();
    }
}
