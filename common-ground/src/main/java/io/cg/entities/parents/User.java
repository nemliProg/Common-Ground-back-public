package io.cg.entities.parents;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;


    @Column( unique = true , nullable = false)
    private String username;

    @Column( unique = true, nullable = false)
    private String email;

    @Column( nullable = false)
    @JsonIgnore
    private String password;

}
