package ru.alexashin.misosdrove2.controllers;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.alexashin.misosdrove2.models.Chapter;
import ru.alexashin.misosdrove2.models.Post;
import ru.alexashin.misosdrove2.repositories.TopicRepository;
import ru.alexashin.misosdrove2.security.PersonDetails;
import ru.alexashin.misosdrove2.services.ChapterService;
import ru.alexashin.misosdrove2.services.TopicService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class TopicController {

    private final TopicService topicService;
    private final ChapterService chapterService;

    public TopicController(TopicService topicService, ChapterService chapterService) {
        this.topicService = topicService;
        this.chapterService = chapterService;
    }
    @GetMapping("/topic/{id}")
    public String toTopic(@PathVariable("id") long id, Model model){
        model.addAttribute("topic", topicService.findById(id));
        model.addAttribute("newChapter",new Chapter() );
        return "topics/topic";
    }

    @GetMapping("/chapter/{idOfChapter}")
    public String toChapter(@PathVariable("idOfChapter") long idOfChapter
            ,Model model){
        model.addAttribute("newPost", new Post());
        model.addAttribute("chapter", chapterService.findChapterById(idOfChapter));
        return "topics/chapter";
    }

    @PostMapping("/newChapter/{idOfTopic}")
    @Modifying(clearAutomatically = true)
    public String newChapter(@ModelAttribute("chapter")Chapter chapter, @PathVariable("idOfTopic") long id
            , HttpServletRequest http){
        chapterService.addChapter(chapter,topicService.findById(id));
        String referer = http.getHeader("Referer");
        return "redirect:" +referer;
    }
    @PostMapping("/newPostInChapter/{idOfChapter}")
    @Modifying(clearAutomatically = true)
    public String newPost(@ModelAttribute("post") Post post, @PathVariable("idOfChapter") long id,
                          @RequestParam("files[]") MultipartFile[] files, HttpServletRequest http) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        chapterService.newPost(post,chapterService.findChapterById(id),files,personDetails.getPerson());
        String referer = http.getHeader("Referer");
        return "redirect:" +referer;
    }
}
