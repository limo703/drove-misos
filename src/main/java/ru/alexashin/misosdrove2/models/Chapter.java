package ru.alexashin.misosdrove2.models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "chapter")
public class Chapter {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "topicId", referencedColumnName = "id")
    private Topic topic;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private List<Post> posts;

    public Chapter(){}

    public List<Post> getPosts() {
        List<Post> temp = posts;
        Collections.reverse(temp);
        return temp;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
