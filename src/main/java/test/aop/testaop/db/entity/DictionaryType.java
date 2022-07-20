package test.aop.testaop.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "dictionary_type")
public class DictionaryType implements Serializable {

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

    @Column(name = "is_delete")
    private Boolean isDelete;

    @OneToMany(mappedBy = "dictionaryType")
    private List<DictionaryValue> values;

    @OneToMany(mappedBy = "dictionaryType")
    private List<Config> configs;

}