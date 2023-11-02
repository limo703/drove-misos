package ru.alexashin.misosdrove2.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "posts")
@Entity
public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "timeOfPost")
    private Date timeOfPost;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name="personId", referencedColumnName = "id")
    private Person writer;

    @ManyToOne
    @JoinColumn(name = "chapterId", referencedColumnName = "id")
    private Chapter chapter;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    public Post(){};

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Post(int personId, Date timeOfPost, String text, Person writer) {
        this.timeOfPost = timeOfPost;
        this.text = text;
        this.writer = writer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getTimeOfPost() {
        return timeOfPost;
    }

    public void setTimeOfPost(Date timeOfPost) {
        this.timeOfPost = timeOfPost;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Person getWriter() {
        return writer;
    }

    public void setWriter(Person writer) {
        this.writer = writer;
    }
}
