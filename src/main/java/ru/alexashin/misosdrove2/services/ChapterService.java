package ru.alexashin.misosdrove2.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.alexashin.misosdrove2.models.*;
import ru.alexashin.misosdrove2.repositories.ChapterRepository;
import ru.alexashin.misosdrove2.repositories.PeopleRepository;
import ru.alexashin.misosdrove2.repositories.PostRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
public class ChapterService {
    private final ChapterRepository chapterRepository;
    private final PostRepository postRepository;
    private final PeopleRepository peopleRepository;


    public ChapterService(ChapterRepository chapterRepository, PostRepository postRepository, PeopleRepository peopleRepository) {
        this.chapterRepository = chapterRepository;
        this.postRepository = postRepository;
        this.peopleRepository = peopleRepository;
    }

    public void addChapter(Chapter chapter, Topic topic){
        chapter.setTopic(topic);
        chapterRepository.save(chapter);
    }

    public void addPostToChapter(Chapter chapter, Post post){
        chapter.getPosts().add(post);
        chapterRepository.save(chapter);
    }
    public Chapter findChapterById(long id){
        return chapterRepository.findById(id).orElse(null);
    }

    public void newPost(Post post, Chapter chapter, MultipartFile[] files, Person person) throws IOException {
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        post.setTimeOfPost(out);
        post.setChapter(chapter);
        post.setWriter(person);
        for (MultipartFile file:
                files) {
            Image image;
            if(file.getSize()!=0){
                image = toImageEntity(file);
                post.getImages().add(image);
                image.setPost(post);
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
}
