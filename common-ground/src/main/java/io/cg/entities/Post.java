package io.cg.entities;

import io.cg.enums.PostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PostType type;

    @Column( nullable = false)
    private String title;

    @Column(length = 1000)
    private String tags;


    private Boolean visible = true;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Date updatedAt;

    //    Text Post
    @Column( length = 2000)
    private String text;

    //    Image Post OR Video Post
    private String caption;

    //    Image Post
    private String image;

    //    Video Post
    private String video;

    @ManyToOne
    private Community community;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @ManyToOne
    private Member member;

    @ManyToMany
    @JoinTable(name = "post_like",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> members_liked;

}
