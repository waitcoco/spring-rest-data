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
    private String subjectId;
    private String name;
    private String phone;
    private String gender;
    private String birthDay;
    private String identity;
    private String formerName;
    private Integer age;
    private String maritalStatus;
    private String nativePlace;
    private String ethnicGroup;
    private String bloodType;
    private String occupation;
    private String address;
    private String height;

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
