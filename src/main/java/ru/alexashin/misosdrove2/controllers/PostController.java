package ru.alexashin.misosdrove2.controllers;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.alexashin.misosdrove2.models.Post;
import ru.alexashin.misosdrove2.security.PersonDetails;
import ru.alexashin.misosdrove2.services.PersonDetailsService;
import ru.alexashin.misosdrove2.services.PostService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
@Controller
public class PostController {

    private final PostService postService;
    private final PersonDetailsService personDetailsService;

    public PostController(PostService postService, PersonDetailsService personDetailsService) {
        this.postService = postService;
        this.personDetailsService = personDetailsService;
    }





    @PostMapping("/newPost")
    @Modifying(clearAutomatically = true)
    public String newPost(@ModelAttribute("post") Post post, @RequestParam("files[]") MultipartFile[] files, HttpServletRequest http) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        postService.newPost(post,personDetails.getPerson(),files);
        String referer = http.getHeader("Referer");
        return "redirect:" +referer;
    }

    @PostMapping("/delPost/{postId}")
    @Modifying(clearAutomatically = true)
    public String delPost(@PathVariable int postId, HttpServletRequest http){
        postService.delPost(postService.findById(postId));
        String referer = http.getHeader("Referer");
        return "redirect:"+referer;
    }

    @PostMapping("/rePost/{id}/{postId}")
    public String rePost(HttpServletRequest http,@PathVariable int id,
                         @PathVariable int postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        postService.rePost(postService.findById(postId),personDetails.getPerson(),personDetailsService.findById(id));
        String referer = http.getHeader("Referer");
        return "redirect:" +referer;
    }
}
