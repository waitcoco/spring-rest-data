package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter
@Table(name = "case_repo")
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Case {
    @NonNull
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String name;
    @OneToMany(mappedBy = "aCase", cascade = CascadeType.ALL)
    private List<Bilu> bilus;
    @Override
    public String toString() {
        String biluNames = String.join(",", bilus.stream().map(bilu->bilu.getName()).collect(Collectors.toList()));
        return String.format("id: %s, name: %s, bilu names: %s\n", id, name, biluNames);
    }
}
