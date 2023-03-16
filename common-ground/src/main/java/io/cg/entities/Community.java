package io.cg.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Community {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private String name;

    @Column( nullable = false)
    private String description;

    @ManyToMany(mappedBy = "communities")
    private List<Member> members;

    @OneToMany(mappedBy = "community")
    private List<Post> posts;


}
