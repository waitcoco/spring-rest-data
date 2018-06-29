package com.mycompany.knowledge.miami.publish.model.gongan;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "phoneRelation")
@RequiredArgsConstructor
@EqualsAndHashCode
public class PhoneRelation {
    @Id
    @NotNull
    private String subjectId;
    private String phoneNumber;
    private String biluSubjectId;
    private String caseSubjectId;
}
