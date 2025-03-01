package dmu.dasom.api.global.file.repository;

import dmu.dasom.api.global.file.entity.FileEntity;
import dmu.dasom.api.global.file.enums.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByFileTypeAndTargetId(FileType fileType, Long targetId);

    @Query("SELECT f FROM FileEntity f WHERE f.fileType = :fileType AND f.targetId = :targetId")
    Optional<FileEntity> findFirstByFileTypeAndTargetIds(@Param("fileType") FileType fileType,
                                             @Param("targetId") Long targetId);

}
