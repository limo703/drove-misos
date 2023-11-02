package ru.alexashin.misosdrove2.services;

import org.springframework.stereotype.Service;
import ru.alexashin.misosdrove2.models.Topic;
import ru.alexashin.misosdrove2.repositories.TopicRepository;

import java.util.List;

@Service
public class TopicService {
    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }
    public List<Topic> findAll(){
        return topicRepository.findAll();
    }

    public Topic findById(long id){
        return topicRepository.findById(id).orElse(null);
    }
}
