package ru.alexashin.misosdrove2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.alexashin.misosdrove2.models.Image;
import ru.alexashin.misosdrove2.models.Person;
import ru.alexashin.misosdrove2.repositories.ImageRepository;
import ru.alexashin.misosdrove2.repositories.PeopleRepository;
import ru.alexashin.misosdrove2.security.PersonDetails;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService {


    private final PeopleRepository peopleRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository, ImageRepository imageRepository) {
        this.peopleRepository = peopleRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByCarNumber(s);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetails(person.get());
    }

    @Transactional
    public void updateDescription(int id, String newDescription){
        Optional<Person> optionalPerson = peopleRepository.findById(id);
        if(optionalPerson.isPresent()){
            Person person = optionalPerson.get();
            person.setDescription(newDescription);
            peopleRepository.save(person);
        }
    }

    public Person findById(int id){
        Optional<Person> optionalPerson = peopleRepository.findById(id);
        return optionalPerson.orElse(null);
    }


    public List<Person> searchByTitle(String query) {
        return peopleRepository.findPeopleByUsernameStartingWith(query);
    }

    @Transactional
    public void subscribe(int idOfSub,int idOfCreat){
        Person sub = peopleRepository.findById(idOfSub).orElse(null);
        Person creat = peopleRepository.findById(idOfCreat).orElse(null);
        sub.getSubscriptions().add(creat);
        peopleRepository.save(sub);
    }

    @Transactional
    public void unsubscribe(int idOfSub,int idOfCreat){
        Person sub = peopleRepository.findById(idOfSub).orElse(null);
        Person creat = peopleRepository.findById(idOfCreat).orElse(null);
        sub.getSubscriptions().remove(creat);
        peopleRepository.save(sub);

    }

    public boolean checkOnSub(int idOfSub,int idOfCreat) {
        Person sub = peopleRepository.findById(idOfSub).orElse(null);
        Person creat = peopleRepository.findById(idOfCreat).orElse(null);
        return creat.getSubscriptions().contains(sub);
    }
    @Transactional
    public void changeMainImage(Person person, MultipartFile file) throws IOException {
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        if(person.getImages().size()!=0){
        if(person.getImages().get(0).isPreviewImage()) {
            Image img = person.getImages().get(0);
            img.setPreviewImage(false);
            imageRepository.save(img);
        }
        }
        Image image;
        if(file.getSize() != 0){
            image = toImageEntity(file);
            image.setPreviewImage(true);
            person.setPreviewImageId(image.getId());
            image.setDateOfCreated(out);
            person.addMainImageToPerson(image);
        }
        peopleRepository.save(person);
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
    public void deleteImageById(Long id){
        Image img = imageRepository.findById(id).orElse(null);
        assert img != null;
        img.setPersonMain(null);
        imageRepository.save(img);
        imageRepository.deleteById(id);
    }
}