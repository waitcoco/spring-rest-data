package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "person",indexes = {@Index(columnList = "name"),
        @Index(columnList = "phone"),
        @Index(columnList = "identity")})
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Person {
    @Id
    @NonNull
    @Column(columnDefinition = "NVARCHAR2(255)")
    private String subjectId;
    @Column(columnDefinition = "NVARCHAR2(255)")
    private String name;
    @Column(columnDefinition = "NVARCHAR2(255)")
    private String phone;
    @Column(columnDefinition = "NVARCHAR2(255)")
    private String gender;
    @Column(columnDefinition = "NVARCHAR2(255)")
    private String birthDay;
    @Column(columnDefinition = "NVARCHAR2(255)")
    private String identity;
//    @ManyToMany
//    @JoinColumn(name = "bilu_subject_id")
//    private List<Bilu> bilus;
//    @ManyToMany
//    @JoinColumn(name = "case_repo_subject_id")
//    private List<Case> cases;
//    @Override
//    public String toString() {
//        String biluNames = String.join(",", bilus.stream().map(bilu->bilu.getName()).collect(Collectors.toList()));
//        return String.format("subjectId: %s, name: %s, bilu names: %s\n", subjectId, name, biluNames);
//    }
}
