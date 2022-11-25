package com.github.stannismod.mvc.config;

import com.github.stannismod.mvc.dao.InMemoryTaskDao;
import com.github.stannismod.mvc.dao.TaskDao;
import org.springframework.context.annotation.Bean;

//@Configuration
public class InMemoryDaoContextConfiguration {

    @Bean
    public TaskDao taskDao() {
        return new InMemoryTaskDao();
    }
}
