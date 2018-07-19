package com.mycompany.knowledge.miami.publish.repository;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Data
@Repository
public class DataSaver {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();

    public DataSaver(
            @Value("${jdbc.user}") String user,
            @Value("${jdbc.password}") String password,
            @Value("${jdbc.driverClass}") String driverClass,
            @Value("${jdbc.jdbcUrl}") String jdbcUrl
    ){
        this.dataSource.setDriverClassName(driverClass);
        this.dataSource.setUrl(jdbcUrl);
        this.dataSource.setUsername(user);
        this.dataSource.setPassword(password);
    }

    public <T> void saveData(List<T> list, String tableName){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        int[] updatedCountArray=jdbcTemplate.batchUpdate("INSERT INTO "+tableName+" (name) VALUES (?);", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1,list.get(i).toString());
            }
            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

}
