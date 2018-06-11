package com.mycompany.knowledge.miami.publish.model.gongan;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "person")
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Person {
    @Id
    @NonNull
    private String id;
    private String name;
    @ManyToMany
    @JoinColumn(name = "bilu_id")
    private List<Bilu> bilus;
    @Override
    public String toString() {
        String biluNames = String.join(",", bilus.stream().map(bilu->bilu.getName()).collect(Collectors.toList()));
        return String.format("id: %s, name: %s, bilu names: %s\n", id, name, biluNames);
    }
}
