package com.mycompany.knowledge.miami.publish.engine.gongan;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CaseBase {

    private String subjectId;
    private String caseId;
    private String caseName;
    private String caseType;
    private List<BiluBase> bilus = new ArrayList<>();
}
