package io.cg.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.cg.entities.parents.User;
import io.cg.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Member extends User {

    @OneToMany(mappedBy = "member")
    private List<Post> Posts;

    @ManyToMany
    @JoinTable(name = "member_community",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id"))
    private List<Community> communities;

    @OneToMany(mappedBy = "member")
    private List<Comment> comments;

    @ManyToMany(mappedBy = "members_liked")
    private List<Post> likedPosts;

    @ManyToMany(mappedBy = "commentLikes")
    private List<Comment> likedComments;

    @Enumerated(EnumType.ORDINAL)
    @JsonIgnore
    private Role role;

}
