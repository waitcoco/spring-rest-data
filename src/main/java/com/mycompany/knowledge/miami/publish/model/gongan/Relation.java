package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "relation")
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
