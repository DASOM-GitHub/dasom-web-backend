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

    @Query("SELECT f FROM FileEntity f WHERE f.fileType = :fileType AND f.targetId IN :targetIds AND f.id IN " +
        "(SELECT MIN(f2.id) FROM FileEntity f2 WHERE f2.fileType = :fileType AND f2.targetId = f.targetId)")
    List<FileEntity> findFirstFilesByTypeAndTargetIds(@Param("fileType") FileType fileType, @Param("targetIds") List<Long> targetIds);

}
