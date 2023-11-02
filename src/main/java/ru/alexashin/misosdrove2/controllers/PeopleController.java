package ru.alexashin.misosdrove2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.alexashin.misosdrove2.models.Person;
import ru.alexashin.misosdrove2.models.Post;
import ru.alexashin.misosdrove2.security.PersonDetails;
import ru.alexashin.misosdrove2.services.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class PeopleController {
    private final AdminService adminService;
    private final PersonDetailsService personDetailsService;
    private final PostService postService;
    private final TopicService topicService;

    @Autowired
    public PeopleController(AdminService adminService, PersonDetailsService personDetailsService, PostService postService, TopicService topicService) {
        this.adminService = adminService;
        this.personDetailsService = personDetailsService;
        this.postService = postService;
        this.topicService = topicService;
    }

    @GetMapping("/hello")
    public String sayHello(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        model.addAttribute("person",personDetails.getPerson());
        model.addAttribute("posts", postService.allPost());
        model.addAttribute("post",new Post());
        model.addAttribute("topics", topicService.findAll());
        return "social/hello";
    }
    @GetMapping("photos/{id}")
    public String photos(@PathVariable int id,Model model){
        model.addAttribute("person", personDetailsService.findById(id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        model.addAttribute("authPerson", personDetails.getPerson());
        return "social/photos";
    }

    @GetMapping("/admin")
    public String adminPage() {
        adminService.doAdminStuff();
        return "social/admin";
    }


    @GetMapping("/{id}")
    public String showPerson(@PathVariable int id,
                             Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        model.addAttribute("person",personDetailsService.findById(id));
        model.addAttribute("authPerson",personDetails.getPerson());
        model.addAttribute("checkOnCub",personDetailsService.checkOnSub(personDetailsService.findById(id).getId(),
                personDetails.getPerson().getId()));
        model.addAttribute("post",new Post());
        return "social/show";
    }
    @GetMapping("/search")
    public String search(){
        return "social/search";
    }

    @GetMapping("/{id}/subscribers")
    public String subscribers(Model model, @PathVariable int id){
        model.addAttribute("person",personDetailsService.findById(id));
        return "social/subscribers";
    }
    @GetMapping("/{id}/subscriptions")
    public String subscriptions(Model model, @PathVariable int id){
        model.addAttribute("person",personDetailsService.findById(id));
        return "social/subscriptions";
    }
    @PostMapping("/description/{id}")
    @Modifying(clearAutomatically = true,flushAutomatically = true)
    public String changeDescriptionPerson(@ModelAttribute("person") Person person,
                                          @PathVariable int id){
        personDetailsService.updateDescription(id,person.getDescription());
        return "redirect:/{id}";
    }
    @PostMapping("/search")
    public String findPeople(Model model,@RequestParam("query") String query){
        model.addAttribute("people",personDetailsService.searchByTitle(query));
        return "social/search";
    }

    @PostMapping("/subscribe/{id}")
    @Modifying(clearAutomatically = true)
    public String toSubscribe(@PathVariable int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        personDetailsService.subscribe(personDetails.getPerson().getId(),id);
        return "redirect:/{id}";
    }
    @PostMapping("/unsubscribe/{id}")
    @Modifying(clearAutomatically = true)
    public String unSubscribe(@PathVariable int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        personDetailsService.unsubscribe(personDetails.getPerson().getId(),id);
        return "redirect:/{id}";
    }
    @PostMapping("/changeImage")
    @Modifying(clearAutomatically = true)
    public String changeMainImage(@RequestParam("file")MultipartFile file, HttpServletRequest http) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        personDetailsService.changeMainImage(personDetails.getPerson(), file);
        String referer = http.getHeader("Referer");
        return "redirect:" +referer;
    }

    @PostMapping("/images/delete/{id}")
    @Modifying(clearAutomatically = true)
    public String deleteImageById(@PathVariable Long id, HttpServletRequest http){
        personDetailsService.deleteImageById(id);
        String referer = http.getHeader("Referer");
        return "redirect:"+ referer;
    }

}