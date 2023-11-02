package ru.alexashin.misosdrove2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alexashin.misosdrove2.models.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
