package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.*;

import javax.persistence.*;

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
    @Column(columnDefinition = "NVARCHAR(255)")
    private String subjectId;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String personSubjectId;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String biluSubjectId;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String caseSubjectId;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String role;
}
