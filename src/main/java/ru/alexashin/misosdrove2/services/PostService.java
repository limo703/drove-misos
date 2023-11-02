package ru.alexashin.misosdrove2.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.alexashin.misosdrove2.models.Image;
import ru.alexashin.misosdrove2.models.Person;
import ru.alexashin.misosdrove2.models.Post;
import ru.alexashin.misosdrove2.repositories.ImageRepository;
import ru.alexashin.misosdrove2.repositories.PostRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    public PostService(PostRepository postRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public void newPost(Post post, Person person, MultipartFile[] files) throws IOException {
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        post.setTimeOfPost(out);
        post.setWriter(person);
        for (MultipartFile file:
             files) {
            Image image;
            if(file.getSize()!=0){
                image = toImageEntity(file);
                post.getImages().add(image);
                image.setPost(post);
                person.addImageToPerson(image);
                image.setDateOfCreated(out);
            }
        }
        postRepository.save(post);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    @Transactional
    public void delPost(Post post){
        postRepository.delete(post);
    }

    public List<Post> allPost(){
        List<Post> posts = postRepository.findAll();
        Collections.reverse(posts);
        return posts;
    }

    public Post findById(int id){
        return postRepository.findById(id).orElse(null);
    }

    @Transactional
    public void rePost(Post post1,Person person,Person repPerson){
        Post post = new Post();
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        post.setTimeOfPost(out);
        post.setWriter(person);
        String temp ="репост от "+repPerson.getUsername()+": ";
        temp += post1.getText();
        post.setText(temp);
        for (Image img:
             post1.getImages()) {
            Image newImg = new Image();
            newImg.setPost(post);
            newImg.setPersonMain(person);
            newImg.setSize(img.getSize());
            newImg.setBytes(img.getBytes());
            newImg.setDateOfCreated(img.getDateOfCreated());
            newImg.setContentType(img.getContentType());
            newImg.setOriginalFileName(img.getOriginalFileName());
            imageRepository.save(newImg);
        }
        postRepository.save(post);
    }
    public List<Post> searchByTitle(String query) {

        List<Post> temp =postRepository.findPostsByTextContaining(query);
        Collections.reverse(temp);
        return temp;
    }
}
