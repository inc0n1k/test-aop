package test.aop.testaop.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import test.aop.testaop.db.entity.DictionaryType;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionaryTypeRepository extends JpaRepository<DictionaryType, Long>, JpaSpecificationExecutor<DictionaryType> {

    Optional<DictionaryType> findByCodeAndIsDeleteFalse(String code);

    List<DictionaryType> findAllByIsDeleteFalse();

}