package test.aop.testaop.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import test.aop.testaop.db.entity.DictionaryValue;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionaryValueRepository extends JpaRepository<DictionaryValue, Long>, JpaSpecificationExecutor<DictionaryValue> {
    Optional<DictionaryValue> findByCodeAndDictionaryType_CodeAndIsDeleteFalse(String valueCode, String typeCode);


    List<DictionaryValue> findAllByDictionaryType_CodeAndIsDeleteFalse(String code);

    @Query(value = "select v.code from DictionaryValue v where v.id = :id")
    String getCodeById(@Param("id") Long id);

}