package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "relation",indexes = {@Index(columnList = "biluSubjectId"),
        @Index(columnList = "personSubjectId"),
        @Index(columnList = "caseSubjectId")})
@RequiredArgsConstructor
@EqualsAndHashCode
public class Relation {
    @Id
    private Integer subjectId;
    private String personSubjectId;
    private String biluSubjectId;
    private String caseSubjectId;
    private String role;
}
