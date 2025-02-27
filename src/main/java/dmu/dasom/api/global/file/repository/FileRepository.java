package dmu.dasom.api.global.file.repository;

import dmu.dasom.api.global.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findAllById(Iterable<Long> ids);

}
