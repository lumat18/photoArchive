package com.photoarchive.repositories;

import com.photoarchive.domain.Photo;
import com.photoarchive.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("select p from Photo p join p.tags t where t in ?1 group by p.photo_id having count(p.photo_id) = ?2")
    Set<Photo> findDistinctByTagsIn(Set<Tag> tags, Long size);
}
