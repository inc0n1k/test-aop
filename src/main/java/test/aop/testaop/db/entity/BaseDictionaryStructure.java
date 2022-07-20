package test.aop.testaop.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseDictionaryStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_kk")
    private String nameKk;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "short_name_ru")
    private String shortNameRu;

    @Column(name = "short_name_kk")
    private String shortNameKk;

    @Column(name = "short_name_en")
    private String shortNameEn;

}