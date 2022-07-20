package test.aop.testaop.db.entity;


import jakarta.persistence.*;
import lombok.*;
import test.aop.testaop.dto.constant.SettingType;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "settings")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code",unique = true)
    private String code;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SettingType type;

    @Column(name = "value")
    private String value;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @ManyToOne
    @JoinTable(name = "dictionary_settings",
            joinColumns = {@JoinColumn(name = "settings_id")},
            inverseJoinColumns = {@JoinColumn(name = "dictionary_value_id")})
    private DictionaryValue dictionaryValue;


}
