package ru.alexashin.misosdrove2.models;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "person")
public class Person {
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(name = "name")
        @Size(min = 2, max = 30,message = "Ник должен быть от 2 до 30 символов")
        @NotEmpty(message = "Данное поле обязательно к заполнению")
        private String username;

        @Column(name="brand")
        @Size(min = 2, max = 30,message = "Марка должена быть от 2 до 30 символов")
        @NotEmpty(message = "Данное поле обязательно к заполнению")
        private String brand;

        @Column(name = "model")
        @Size(min = 2, max = 50,message = "Модель должена быть от 2 до 50 символов")
        @NotEmpty(message = "Данное поле обязательно к заполнению")
        private String model;

        @Column(name = "yearOfRelease")
        @Max(value = 2023,message = "Год выпуска не может быть больше 2023")
        @NotNull(message = "Данное поле обязательно к заполнению")
        private int yearOfRelease;

        @Column(name="password")
        @NotEmpty(message = "Данное поле обязательно к заполнению")
        private String password;

        @Column(name="role")
        private String role;

        @Column(name = "carNumber")
        @Pattern(regexp = "[авекмнорстху]\\d{3}[авекмнорстху]{2}\\d{2,3}",message = "Вид: a001мр99/a001мр199")
        @NotEmpty(message = "Данное поле обязательно к заполнению")
        private String carNumber;

        @Column(name = "description")
        @Size(min = 2, max = 300,message = "Описание может быть от 2 до 300 символов")
        private String description;

        @Column(name = "timeOfRegister")
        @Temporal(TemporalType.DATE)
        private Date timeOfRegister;

        @OneToMany(mappedBy = "personMain",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private List<Image> images = new ArrayList<>();

        @Column(name = "previewImageId")
        private Long previewImageId;

    @ManyToMany()
    @JoinTable(
            name="personSubscriptions",
            joinColumns = { @JoinColumn(name="subscriberId")},
            inverseJoinColumns = {@JoinColumn(name = "channelId")}
    )
    List<Person> subscribers  = new ArrayList<>();

    @ManyToMany()
    @JoinTable(
            name="personSubscriptions",
            joinColumns = { @JoinColumn(name="channelId")},
            inverseJoinColumns = {@JoinColumn(name = "subscriberId")}
    )
    List<Person> subscriptions  = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    public void addImageToPerson(Image image){
        image.setPersonMain(this);
        images.add(image);
    }
    public void addMainImageToPerson(Image image){
        image.setPersonMain(this);
        images.add(0,image);
    }

    public List<Post> getPosts() {
        List<Post> posts1 = posts;
        Collections.reverse(posts1);
        return posts1;
    }
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Person> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Person> subscribers) {
        this.subscribers = subscribers;
    }

    public List<Person> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Person> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Date getTimeOfRegister() {
        return timeOfRegister;
    }

    public void setTimeOfRegister(Date timeOfRegister) {
        this.timeOfRegister = timeOfRegister;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getYearOfRelease() {
            return yearOfRelease;
        }

        public void setYearOfRelease(int yearOfRelease) {
            this.yearOfRelease = yearOfRelease;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Long getPreviewImageId() {
        return previewImageId;
    }

    public void setPreviewImageId(Long previewImageId) {
        this.previewImageId = previewImageId;
    }

    @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + username + '\'' +
                    ", brand='" + brand + '\'' +
                    ", model='" + model + '\'' +
                    ", yearOfRelease=" + yearOfRelease +
                    ", password='" + password + '\'' +
                    ", role='" + role + '\'' +
                    ", carNumber='" + carNumber + '\'' +
                    '}';
        }
}
