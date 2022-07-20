package test.aop.testaop.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "dictionary_value")
public class DictionaryValue extends BaseDictionaryStructure {

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @ManyToOne
    @JoinColumn(name = "dictionary_type_code", referencedColumnName = "code", nullable = false)
    private DictionaryType dictionaryType;

    @OneToMany
    @JoinTable(name = "dictionary_settings",
            joinColumns = {@JoinColumn(name = "dictionary_value_id")},
            inverseJoinColumns = {@JoinColumn(name = "settings_id")})
    private List<Setting> settings;

}