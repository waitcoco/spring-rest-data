package com.mycompany.knowledge.miami.publish.engine.gongan;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Bilu {
    private String subjectId;
    private String biluId;
    private String name;
    private String content;

    private List<Person> persons = new ArrayList<>();
    private Map<String, String> phones = new HashMap<>();
    private Map<String, String> bankCards = new HashMap<>();

    //key is subjectid, value is connection type;
    private Map<String, String> connections = new HashMap<>();
    private List<String> tags = new ArrayList<>();
}
