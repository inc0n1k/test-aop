package test.aop.testaop.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import test.aop.testaop.dto.constant.SettingType;

@Getter
@Setter
@Entity
@Table(name = "config")
public class Config {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SettingType type;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @ManyToOne
    @JoinColumn(name = "type_code", referencedColumnName = "code", nullable = false)
    private DictionaryType dictionaryType;
}