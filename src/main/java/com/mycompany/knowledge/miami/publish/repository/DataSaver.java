package com.mycompany.knowledge.miami.publish.repository;

import com.mycompany.knowledge.miami.publish.model.gongan.IdentityRelation;
import com.mycompany.knowledge.miami.publish.model.gongan.Person;
import com.mycompany.knowledge.miami.publish.model.gongan.PhoneRelation;
import com.mycompany.knowledge.miami.publish.model.gongan.Relation;
import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
@Repository
public class DataSaver {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    private JdbcTemplate jdbcTemplate;
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
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveRelation(List<Relation> list){
        StringBuilder values = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            values = values.append("("+'"'+list.get(i).getSubjectId()+'"'+","+'"'+list.get(i).getPersonSubjectId()+'"'+","+
                    '"'+list.get(i).getBiluSubjectId()+'"'+","+'"'+list.get(i).getCaseSubjectId()+'"'+","+'"'+list.get(i).getRole()+'"'+"),");
            if(i%999 == 0){
                if(values.length()!=0){
                    jdbcTemplate.update("insert ignore into relation (subject_id,person_subject_id,bilu_subject_id,case_subject_id,role) values"+values.substring(0,values.length()-1));
                    values.setLength(0);
                }

            }
        }
        if(values.length()!=0){
            jdbcTemplate.update("insert ignore into relation (subject_id,person_subject_id,bilu_subject_id,case_subject_id,role) values"+values.substring(0,values.length()-1));
        }
    }

    public void savePerson(List<Person> personList){
        List<Person> list = removeDuplicatedId(personList);
        StringBuilder values = new StringBuilder();
        for(int i =0; i < list.size(); i++){
            values = values.append("("+'"'+list.get(i).getSubjectId()+'"'+","+'"'+list.get(i).getName()+'"'+","+
                    '"'+list.get(i).getPhone()+'"'+","+'"'+list.get(i).getGender()+'"'+","+'"'+list.get(i).getBirthDay()+
                    '"'+","+'"'+list.get(i).getIdentity()+'"'+","+'"'+list.get(i).getFormerName()+'"'+","+list.get(i).getAge()+
                    ","+'"'+list.get(i).getMaritalStatus()+'"'+","+'"'+list.get(i).getNativePlace()+'"'+","+'"'
                    +list.get(i).getEthnicGroup()+'"'+","+'"'+list.get(i).getBloodType()+'"'+","+ '"'+list.get(i).getOccupation()+'"'
                    +","+'"'+list.get(i).getAddress()+'"'+","+'"'+list.get(i).getHeight()+'"'+"),");
            if(i%999 == 0){
                if(values.length()!=0){
                    jdbcTemplate.update("insert ignore into person(subject_id,name,phone,gender,birth_day,identity,former_name,age,marital_status,native_place,ethnic_group,blood_type,occupation,address,height) values"+values.substring(0,values.length()-1));
                    values.setLength(0);
                }
            }
        }
        if(values.length()!=0){
            jdbcTemplate.update("insert ignore into person(subject_id,name,phone,gender,birth_day,identity,former_name,age,marital_status,native_place,ethnic_group,blood_type,occupation,address,height) values"+values.substring(0,values.length()-1));
        }
    }

    public void saveIdentityRelation(List<IdentityRelation> list){
        StringBuilder values = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            values = values.append("("+'"'+list.get(i).getSubjectId()+'"'+","+'"'+list.get(i).getIdentity()+'"'+","+
                    '"'+list.get(i).getBiluSubjectId()+'"'+","+'"'+list.get(i).getCaseSubjectId()+'"'+"),");
            if(i%999 == 0){
                if(values.length()!=0){
                    jdbcTemplate.update("insert ignore into identity_relation(subject_id,identity,bilu_subject_id,case_subject_id) values"+values.substring(0,values.length()-1));
                    values.setLength(0);
                }
            }
        }
        if(values.length()!=0){
            jdbcTemplate.update("insert ignore into identity_relation(subject_id,identity,bilu_subject_id,case_subject_id) values"+values.substring(0,values.length()-1));
        }
    }

    public void savePhoneRelation(List<PhoneRelation> list){
        StringBuilder values = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            values = values.append("("+'"'+list.get(i).getSubjectId()+'"'+","+'"'+list.get(i).getPhoneNumber()+'"'+","+
                    '"'+list.get(i).getBiluSubjectId()+'"'+","+'"'+list.get(i).getCaseSubjectId()+'"'+"),");
            if(i%999 == 0){
                if(values.length()!=0){
                    jdbcTemplate.update("insert ignore into phone_relation (subject_id,phone_number,bilu_subject_id,case_subject_id) values"+values.substring(0,values.length()-1));
                    values.setLength(0);
                }
            }
        }
        if(values.length()!=0){
            jdbcTemplate.update("insert ignore into phone_relation (subject_id,phone_number,bilu_subject_id,case_subject_id) values"+values.substring(0,values.length()-1));
        }
    }

    public List<Person> removeDuplicatedId(List<Person> personList){
        Set<Person> personSet = new TreeSet<Person>((o1, o2)->o1.getSubjectId().compareTo(o2.getSubjectId()));
        personSet.addAll(personList);
        return new ArrayList<>(personSet);
    }

}
