package com.mycompany.knowledge.miami.publish.engine.gongan;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Person {
    private String subjectId;
    private String name;
    private String gender;
    private String phone;
    private String birthDay;
    private String identity;

    // case subjectId
    private List<String> caseList = new ArrayList<>();

    // case subjectId
    private List<String> biluList = new ArrayList<>();
}
