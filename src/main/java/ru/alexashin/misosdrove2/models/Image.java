package ru.alexashin.misosdrove2.models;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Image")
public class Image {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name="originalFileName")
    private String originalFileName;

    @Column(name="size")
    private Long size;

    @Column(name = "contentType")
    private String contentType;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    @Column(name = "image", columnDefinition = "longblob")
    private byte[] bytes;

    @Column(name="dateOfCreated")
    private Date dateOfCreated;

    @Column(name = "isPreviewImage")
    private boolean isPreviewImage;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="personId", referencedColumnName = "id")
    private Person personMain;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="postId", referencedColumnName = "id")
    private Post post;

    public Image(){}

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Date getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(Date dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }

    public boolean isPreviewImage() {
        return isPreviewImage;
    }

    public void setPreviewImage(boolean previewImage) {
        isPreviewImage = previewImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Person getPersonMain() {
        return personMain;
    }

    public void setPersonMain(Person personMain) {
        this.personMain = personMain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
