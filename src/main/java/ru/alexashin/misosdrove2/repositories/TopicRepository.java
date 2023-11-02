package ru.alexashin.misosdrove2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alexashin.misosdrove2.models.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
}
