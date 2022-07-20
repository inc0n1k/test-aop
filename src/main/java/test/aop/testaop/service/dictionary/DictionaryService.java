package test.aop.testaop.service.dictionary;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.aop.testaop.db.entity.Config;
import test.aop.testaop.db.entity.DictionaryType;
import test.aop.testaop.db.entity.DictionaryValue;
import test.aop.testaop.db.entity.Setting;
import test.aop.testaop.db.repository.ConfigRepository;
import test.aop.testaop.db.repository.DictionaryTypeRepository;
import test.aop.testaop.db.repository.DictionaryValueRepository;
import test.aop.testaop.db.repository.SettingRepository;
import test.aop.testaop.dto.dictionary.*;
import test.aop.testaop.exception.TestAopException;
import test.aop.testaop.utils.PageDto;
import test.aop.testaop.utils.mess.MessageUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class DictionaryService {

    private final MessageUtils messageUtils;
    private final DictionaryTypeRepository dictionaryTypeRepository;
    private final DictionaryValueRepository dictionaryValueRepository;
    private final SettingRepository settingRepository;
    private final ConfigRepository configRepository;

    public DictionaryService(
            MessageUtils messageUtils,
            DictionaryTypeRepository dictionaryTypeRepository,
            DictionaryValueRepository dictionaryValueRepository,
            SettingRepository settingRepository,
            ConfigRepository configRepository
    ) {
        this.messageUtils = messageUtils;
        this.dictionaryTypeRepository = dictionaryTypeRepository;
        this.dictionaryValueRepository = dictionaryValueRepository;
        this.settingRepository = settingRepository;
        this.configRepository = configRepository;
    }

    private void save(DictionaryType in) {
        dictionaryTypeRepository.save(in);
        log.info("DictionaryType with code: " + in.getCode() + " is saved");
    }

    private DictionaryValue save(DictionaryValue in) {
        dictionaryValueRepository.save(in);
        log.info("DictionaryValue with typeCode: " + in.getDictionaryType().getCode() + " and valueCode: " + in.getCode() + " is saved");
        return in;
    }

    private void save(Setting in) {
        settingRepository.save(in);
        log.info("Setting with typeCode: " + in.getDictionaryValue().getDictionaryType().getCode() +
                " and valueCode: " + in.getDictionaryValue().getCode() + " and code: " + in.getCode() + " is saved");
    }

    private void save(Config in) {
        configRepository.save(in);
        log.info("Config with typeCode: " + in.getDictionaryType().getCode() +
                " and code: " + in.getCode() + " is saved");
    }

    @Transactional
    public void addValue(AddDictionaryValueDto in) throws TestAopException {
        DictionaryType type = getDictionaryType(in.getDictionaryTypeCode());
        DictionaryValue parentValue = null;
        if (nonNull(in.getParentCode()) && !in.getParentCode().isBlank())
            parentValue = getDictionaryValueByCodes(in.getParentCode(), in.getDictionaryTypeCode());
        DictionaryValue value = dictionaryValueRepository.findByCodeAndDictionaryType_CodeAndIsDeleteFalse(in.getCode(), in.getDictionaryTypeCode()).orElse(null);
        if (nonNull(value)) {
            String error = "DictionaryValue with typeCode: " + in.getDictionaryTypeCode() + " and valueCode: " + in.getCode() + " is present";
            log.error(error);
            throw new TestAopException(error);
        }
        DictionaryValue newV = save(convert(in, type, parentValue));
        List<Config> list = getConfigsByCode(in.getDictionaryTypeCode());
        for (var el : list) {
            save(Setting.builder().dictionaryValue(newV).type(el.getType()).code(el.getCode()).value(null).isDelete(false).build());
        }
    }

    public void updateValue(UpdateDictionaryValueDto in) throws TestAopException {
        DictionaryValue value = getDictionaryValueByCodes(in.getCode(), in.getDictionaryTypeCode());
        DictionaryValue parentValue = null;
        if (nonNull(in.getParentCode()) && !in.getParentCode().isBlank())
            parentValue = getDictionaryValueByCodes(in.getParentCode(), in.getDictionaryTypeCode());

        updateValue(value, in, getDictionaryType(in.getDictionaryTypeCode()), parentValue);
        save(value);
    }

    private DictionaryValue getDictionaryValueByCodes(String valueCode, String typeCode) throws TestAopException {
        DictionaryValue result = dictionaryValueRepository.findByCodeAndDictionaryType_CodeAndIsDeleteFalse(valueCode, typeCode).orElse(null);
        if (isNull(result)) {
            String error = "DictionaryValue with valueCode: \"" + valueCode + "\" and typeCode: " + typeCode + " not found";
            log.error(error);
            throw new TestAopException(error);
        }
        log.info("DictionaryValue with valueCode: \"" + valueCode + "\" and typeCode: " + typeCode + " is found");
        return result;
    }

    public ShortDictionaryValueDto findShortDictionaryValueDtoByCodes(String valueCode, String typeCode) throws TestAopException {
        return convertToShort(getDictionaryValueByCodes(valueCode, typeCode));
    }

    public FullDictionaryValueDto findFullDictionaryValueDtoByCodes(String valueCode, String typeCode) throws TestAopException {
        return convertToFull(getDictionaryValueByCodes(valueCode, typeCode));
    }

    public List<ShortDictionaryValueDto> findAllShortValueDto(String typeCode) {
        List<DictionaryValue> list = dictionaryValueRepository.findAllByDictionaryType_CodeAndIsDeleteFalse(typeCode);
        return list.stream().map(this::convertToShort).collect(Collectors.toList());
    }

    public List<FullDictionaryValueDto> findAllFullValueDto(String typeCode) {
        List<DictionaryValue> list = dictionaryValueRepository.findAllByDictionaryType_CodeAndIsDeleteFalse(typeCode);
        return list.stream().map(this::convertToFull).collect(Collectors.toList());
    }

    public PageDto<ShortDictionaryValueDto> findAllShortValuePageDto(DictionaryValuePageRequestDto dto) {
        Page<DictionaryValue> page = dictionaryValueRepository.findAll(
                getSpecificationForPage(dto.getTypeCode()),
                PageRequest.of(dto.getPage(), dto.getSize(), dto.getDirection(), dto.getSort()));
        List<ShortDictionaryValueDto> list = page.stream().map(this::convertToShort).toList();
        return new PageDto<>(list, page);
    }

    public PageDto<FullDictionaryValueDto> findAllFullValuePageDto(DictionaryValuePageRequestDto dto) {
        Page<DictionaryValue> page = dictionaryValueRepository.findAll(
                getSpecificationForPage(dto.getTypeCode()),
                PageRequest.of(dto.getPage(), dto.getSize(), dto.getDirection(), dto.getSort()));
        List<FullDictionaryValueDto> list = page.stream().map(this::convertToFull).toList();
        return new PageDto<>(list, page);
    }

    private Specification<DictionaryValue> getSpecificationForPage(String code) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.isFalse(root.get("isDelete")),
                criteriaBuilder.equal(root.get("dictionaryType").get("code"), code)
        );
    }

    public List<FullTreeValueDto> getFullTreeValueDtoList(String code) {
        List<DictionaryValue> list = dictionaryValueRepository.findAll(
                (root, query, criteriaBuilder) -> {
                    Predicate pr0 = criteriaBuilder.equal(root.get("dictionaryType").get("code"), code);
                    Predicate pr1 = criteriaBuilder.isNull(root.get("parentId"));
                    Predicate pr2 = criteriaBuilder.isFalse(root.get("isDelete"));
                    return criteriaBuilder.and(pr0, pr1, pr2);
                });
        return toFullTreeValueDto(list);
    }

    public List<ShortTreeValueDto> getShortTreeValueDto(String code) {
        List<DictionaryValue> list = dictionaryValueRepository.findAll(
                (root, query, criteriaBuilder) -> {
                    Predicate pr0 = criteriaBuilder.equal(root.get("dictionaryType").get("code"), code);
                    Predicate pr1 = criteriaBuilder.isNull(root.get("parentId"));
                    Predicate pr2 = criteriaBuilder.isFalse(root.get("isDelete"));
                    return criteriaBuilder.and(pr0, pr1, pr2);
                });
        return toShortTreeValueDto(list);
    }

    private List<FullTreeValueDto> toFullTreeValueDto(List<DictionaryValue> in) {
        List<FullTreeValueDto> out = new ArrayList<>();
        for (var el : in) {
            FullTreeValueDto element = toFullTreeValueDto(el);
            List<DictionaryValue> childList = dictionaryValueRepository.findAll(
                    (root, query, criteriaBuilder) -> {
                        Predicate pr0 = criteriaBuilder.equal(root.get("dictionaryType").get("code"), el.getDictionaryType().getCode());
                        Predicate pr1 = criteriaBuilder.equal(root.get("parentId"), el.getId());
                        Predicate pr2 = criteriaBuilder.isFalse(root.get("isDelete"));
                        return criteriaBuilder.and(pr0, pr1, pr2);
                    });
            if (!childList.isEmpty())
                element.setChild(toFullTreeValueDto(childList));
            out.add(element);
        }
        return out;
    }

    private List<ShortTreeValueDto> toShortTreeValueDto(List<DictionaryValue> in) {
        List<ShortTreeValueDto> out = new ArrayList<>();
        for (var el : in) {
            ShortTreeValueDto element = toShortTreeValueDto(el);
            List<DictionaryValue> childList = dictionaryValueRepository.findAll(
                    (root, query, criteriaBuilder) -> {
                        Predicate pr0 = criteriaBuilder.equal(root.get("dictionaryType").get("code"), el.getDictionaryType().getCode());
                        Predicate pr1 = criteriaBuilder.equal(root.get("parentId"), el.getId());
                        Predicate pr2 = criteriaBuilder.isFalse(root.get("isDelete"));
                        return criteriaBuilder.and(pr0, pr1, pr2);
                    });
            if (!childList.isEmpty())
                element.setChild(toShortTreeValueDto(childList));
            out.add(element);
        }
        return out;
    }

    private FullTreeValueDto toFullTreeValueDto(DictionaryValue in) {
        return FullTreeValueDto.builder()
                .code(in.getCode())
                .parentCode(getCodeById(in.getParentId()))
                .nameRu(in.getNameRu())
                .nameKk(in.getNameKk())
                .nameEn(in.getNameEn())
                .shortNameRu(in.getNameRu())
                .shortNameKk(in.getNameKk())
                .shortNameEn(in.getNameEn())
                .settings(in.getSettings().stream().filter(e -> !e.getIsDelete()).map(this::convert).collect(Collectors.toMap(SettingDto::getCode, e -> e, (s, s2) -> s2)))
                .build();
    }

    private ShortTreeValueDto toShortTreeValueDto(DictionaryValue in) {
        return ShortTreeValueDto.builder()
                .code(in.getCode())
                .parentCode(getCodeById(in.getParentId()))
                .nameRu(in.getNameRu())
                .nameKk(in.getNameKk())
                .nameEn(in.getNameEn())
                .shortNameRu(in.getNameRu())
                .shortNameKk(in.getNameKk())
                .shortNameEn(in.getNameEn())
                .build();
    }

    private ShortDictionaryValueDto convertToShort(DictionaryValue in) {
        return ShortDictionaryValueDto.builder()
                .code(in.getCode())
                .parentCode(getCodeById(in.getParentId()))
                .dictionaryTypeCode(in.getDictionaryType().getCode())
                .nameRu(in.getNameRu())
                .nameKk(in.getNameKk())
                .nameEn(in.getNameEn())
                .shortNameRu(in.getShortNameRu())
                .shortNameKk(in.getShortNameKk())
                .shortNameEn(in.getShortNameEn())
                .build();
    }

    private FullDictionaryValueDto convertToFull(DictionaryValue in) {
        return FullDictionaryValueDto.builder()
                .code(in.getCode())
                .parentCode(getCodeById(in.getParentId()))
                .dictionaryTypeCode(in.getDictionaryType().getCode())
                .nameRu(in.getNameRu())
                .nameKk(in.getNameKk())
                .nameEn(in.getNameEn())
                .shortNameRu(in.getShortNameRu())
                .shortNameKk(in.getShortNameKk())
                .shortNameEn(in.getShortNameEn())
                .settings(in.getSettings().stream().filter(e -> !e.getIsDelete()).map(this::convert).collect(Collectors.toMap(SettingDto::getCode, e -> e, (s, s2) -> s2)))
                .build();
    }

    private String getCodeById(Long id) {
        if (nonNull(id))
            return dictionaryValueRepository.getCodeById(id);
        return null;
    }

    private SettingDto convert(Setting in) {
        return SettingDto.builder()
                .code(in.getCode())
                .aClass(in.getType().getAClass())
                .value(in.getValue())
                .build();
    }

    private void updateValue(DictionaryValue out, UpdateDictionaryValueDto in, DictionaryType type, DictionaryValue parentValue) {
        out.setCode(in.getCode());
        out.setParentId(isNull(parentValue) ? null : parentValue.getId());
        out.setNameRu(in.getNameRu());
        out.setNameKk(in.getNameKk());
        out.setNameEn(in.getNameEn());
        out.setShortNameRu(in.getShortNameRu());
        out.setShortNameKk(in.getShortNameKk());
        out.setShortNameEn(in.getShortNameEn());
        out.setDictionaryType(type);
    }

    private DictionaryValue convert(AddDictionaryValueDto in, DictionaryType type, DictionaryValue parentValue) {
        DictionaryValue out = new DictionaryValue();
        out.setCode(in.getCode());
        out.setDictionaryType(type);
        out.setNameRu(in.getNameRu());
        out.setNameKk(in.getNameKk());
        out.setNameEn(in.getNameEn());
        out.setShortNameRu(in.getShortNameRu());
        out.setShortNameKk(in.getShortNameKk());
        out.setShortNameEn(in.getShortNameEn());
        out.setParentId(isNull(parentValue) ? null : parentValue.getId());
        out.setIsDelete(false);
        return out;
    }

    public void addType(AddDictionaryTypeDto in) throws TestAopException {
        if (isPresent(in.getCode())) {
            String str = "DictionaryType with code: " + in.getCode() + " is present";
            log.error(str);
            throw new TestAopException(str);
        }
        save(convert(in));
    }

    public void updateType(UpdateDictionaryTypeDto in) throws TestAopException {
        DictionaryType type = getDictionaryType(in.getCode());
        if (nonNull(in.getNewTypeCode()) && !in.getNewTypeCode().isBlank() && isPresent(in.getNewTypeCode())) {
            String str = "DictionaryType with code: " + in.getNewTypeCode() + " is present";
            log.error(str);
            throw new TestAopException(str);
        }
        updateType(type, in);
        save(type);
    }

    private boolean isPresent(String code) {
        boolean isPresent = true;
        try {
            getDictionaryType(code);
        } catch (Exception e) {
            isPresent = false;
        }
        return isPresent;
    }

    public void deleteType(String code) throws TestAopException {
        String mask = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        DictionaryType type = getDictionaryType(code);
        type.getConfigs().stream().filter(el -> !el.getIsDelete()).forEach(e -> {
            e.setIsDelete(true);
            e.setCode(e.getCode() + mask);
        });
        type.getValues().stream().filter(el -> !el.getIsDelete()).forEach(e -> {
                    e.setIsDelete(true);
                    e.setCode(e.getCode() + "_" + mask);
                    e.getSettings().forEach(el -> el.setIsDelete(true));
                }
        );
        type.setIsDelete(true);
        type.setCode(type.getCode() + "_" + mask);
        save(type);
        log.info("DictionaryType with code: " + code + " is deleted");
    }

    public void deleteValue(String typeCode, String valueCode) throws TestAopException {
        String mask = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        DictionaryValue value = getDictionaryValueByCodes(valueCode, typeCode);
        value.getSettings().forEach(e -> e.setIsDelete(true));
        value.setIsDelete(true);
        value.setCode(value.getCode() + "_" + mask);
        save(value);
        log.info("DictionaryValue with typeCode: " + typeCode + " and valueCode: " + valueCode + " is deleted");
    }

    private void deleteSetting(String typeCode, String valueCode, String code) throws TestAopException {
        Setting setting = getSettingByCodes(typeCode, valueCode, code);
        setting.setIsDelete(true);
        save(setting);
        log.info("Setting with typeCode: \"" + typeCode + "\" and valueCode: \"" + valueCode + "\" and code: \"" +
                code + "\" is deleted");
    }

    public List<SettingDto> getAllSettings(String typeCode, String valueCode) throws TestAopException {
        DictionaryValue value = getDictionaryValueByCodes(valueCode, typeCode);
        return value.getSettings().stream().filter(e -> !e.getIsDelete()).map(this::convert).toList();
    }

    public List<ShortDictionaryTypeDto> findAllTypes() {
        List<DictionaryType> list = dictionaryTypeRepository.findAllByIsDeleteFalse();
        return list.stream().map(this::convertToShort).toList();
    }

    public PageDto<ShortDictionaryTypeDto> findAllTypePageDto(DictionaryTypePageRequestDto request) {
        Page<DictionaryType> page = dictionaryTypeRepository.findAll(
                (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDelete")),
                PageRequest.of(request.getPage(), request.getSize(), request.getDirection(), request.getSort())
        );
        List<ShortDictionaryTypeDto> list = page.stream().map(this::convertToShort).toList();
        return new PageDto<>(list, page);
    }

    private DictionaryType getDictionaryType(String code) throws TestAopException {
        DictionaryType result = dictionaryTypeRepository.findByCodeAndIsDeleteFalse(code).orElse(null);
        if (isNull(result)) {
            String error = "DictionaryType with code: \"" + code + "\" not found";
            log.error(error);
            throw new TestAopException(error);
        }
        log.info("DictionaryType with code: \"" + code + "\" is found");
        return result;
    }

    public ShortDictionaryTypeDto findShortDictionaryTypeDto(String code) throws TestAopException {
        return convertToShort(getDictionaryType(code));
    }

    public FullDictionaryTypeDto findFullDictionaryTypeDto(String code) throws TestAopException {
        return convertToFull(getDictionaryType(code));
    }

    private ShortDictionaryTypeDto convertToShort(DictionaryType in) {
        return ShortDictionaryTypeDto.builder()
                .code(in.getCode())
                .nameRu(in.getNameRu())
                .nameKk(in.getNameKk())
                .nameEn(in.getNameEn())
                .shortNameRu(in.getShortNameRu())
                .shortNameKk(in.getShortNameKk())
                .shortNameEn(in.getShortNameEn())
                .build();
    }

    private FullDictionaryTypeDto convertToFull(DictionaryType in) {
        return FullDictionaryTypeDto.builder()
                .code(in.getCode())
                .nameRu(in.getNameRu())
                .nameKk(in.getNameKk())
                .nameEn(in.getNameEn())
                .shortNameRu(in.getShortNameRu())
                .shortNameKk(in.getShortNameKk())
                .shortNameEn(in.getShortNameEn())
                .values(in.getValues().stream().filter(e -> !e.getIsDelete()).map(this::convertToShort).toList())
                .build();
    }

    private DictionaryType convert(AddDictionaryTypeDto in) {
        DictionaryType out = new DictionaryType();
        out.setCode(in.getCode());
        out.setNameRu(in.getNameRu());
        out.setNameKk(in.getNameKk());
        out.setNameEn(in.getNameEn());
        out.setShortNameRu(in.getShortNameRu());
        out.setShortNameKk(in.getShortNameKk());
        out.setShortNameEn(in.getShortNameEn());
        out.setIsDelete(false);
        return out;
    }

    private void updateType(DictionaryType out, UpdateDictionaryTypeDto in) {
        if (nonNull(in.getNewTypeCode()) && !in.getNewTypeCode().isBlank())
            out.setCode(in.getNewTypeCode());
        out.setNameRu(in.getNameRu());
        out.setNameKk(in.getNameKk());
        out.setNameEn(in.getNameEn());
        out.setShortNameRu(in.getShortNameRu());
        out.setShortNameKk(in.getShortNameKk());
        out.setShortNameEn(in.getShortNameEn());
    }

    @Transactional
    public void updateSettings(UpdateSettingsDto in) throws TestAopException {
        getDictionaryValueByCodes(in.getValueCode(), in.getTypeCode());
        Map<String, Config> configMap = getConfigsByCode(in.getTypeCode()).stream().collect(Collectors.toMap(Config::getCode, e -> e));
        Config temp;
        Setting setting;
        for (var el : in.getSettings().entrySet()) {
            temp = configMap.get(el.getKey());
            if (isNull(temp))
                throw new TestAopException("Value with code: " + in.getTypeCode() + " does not contain config with code: " + el.getKey());
            if (nonNull(el.getValue()) && !el.getValue().matches(temp.getType().getPattern()))
                throw new TestAopException("Seting with code: " + el.getKey() + " does not match pattern: " + temp.getType().getExample());
            setting = getSettingByCodes(in.getTypeCode(), in.getValueCode(), el.getKey());
            setting.setValue(el.getValue());
            save(setting);
        }
    }

    public Setting getSettingByCodes(String typeCode, String valueCode, String code) throws TestAopException {
        DictionaryValue value = getDictionaryValueByCodes(valueCode, typeCode);
        Setting setting = value.getSettings().stream().filter(e -> e.getCode().equals(code) && !e.getIsDelete()).findFirst().orElse(null);
        if (isNull(setting)) {
            String str = "Setting with typeCode: \"" + typeCode + "\" and valueCode: \"" + valueCode + "\" and code: \"" +
                    code + "\" not found";
            log.error(str);
            throw new TestAopException(str);
        }
        return setting;
    }


//    public boolean contain(DictionaryValue value, String code) {
//        return value.getSettings().stream().anyMatch(e -> e.getCode().equals(code));
//    }

//    private Setting convert(UpdateSettingsDto in, DictionaryValue value) {
//        Setting out = new Setting();
//        out.setCode(in.getCode());
//        out.setValue(in.getValue());
//        out.setType(in.getType());
//        out.setDictionaryValue(value);
//        out.setIsDelete(false);
//        return out;
//    }

    private List<Specification<DictionaryValue>> getBaseSpecification(AllValuesRequestDto request) {
        var list = new ArrayList<Specification<DictionaryValue>>();

        list.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dictionaryType").get("code"), request.getTypeCode()));
        list.add((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDelete")));

        if (nonNull(request.getSearchByCode()) && !request.getSearchByCode().isBlank())
            list.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.upper(root.get("code")),
                            "%" + request.getSearchByCode().toUpperCase() + "%"));

        if (nonNull(request.getSearchByName()) && !request.getSearchByName().isBlank())
            list.add((root, query, criteriaBuilder) -> {
                String str = "%" + request.getSearchByName().toUpperCase() + "%";
                Predicate[] pr = new Predicate[3];
                pr[0] = criteriaBuilder.like(criteriaBuilder.upper(root.get("nameRu")), str);
                pr[1] = criteriaBuilder.like(criteriaBuilder.upper(root.get("nameKk")), str);
                pr[2] = criteriaBuilder.like(criteriaBuilder.upper(root.get("nameEn")), str);
                return criteriaBuilder.or(pr);
            });
        return list;
    }

    private void getSpecificationForEquals(List<Specification<DictionaryValue>> list, AllValuesRequestDto request) {
        for (var el : request.getParameters().entrySet()) {
            if (isNull(el.getValue()))
                list.add((root, query, criteriaBuilder) -> {
                    query.distinct(true);
                    Join<DictionaryValue, Setting> join = root.join("settings", JoinType.LEFT);
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(join.get("code"), el.getKey()),
                            criteriaBuilder.isNull(join.get("value")),
                            criteriaBuilder.isFalse(root.get("isDelete"))
                    );
                });
            else if (!String.valueOf(el.getValue()).isBlank())
                list.add((root, query, criteriaBuilder) -> {
                    query.distinct(true);
                    Join<DictionaryValue, Setting> join = root.join("settings", JoinType.LEFT);
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(join.get("code"), el.getKey()),
                            criteriaBuilder.equal(join.get("value"), el.getValue()),
                            criteriaBuilder.isFalse(root.get("isDelete"))
                    );
                });
        }
    }

    @SuppressWarnings("unchecked")
    private void getSpecificationForIn(List<Specification<DictionaryValue>> list, AllValuesRequestDto request) {
        for (var el : request.getParameters().entrySet()) {
            if (isNull(el.getValue()))
                list.add((root, query, criteriaBuilder) -> {
                    query.distinct(true);
                    Join<DictionaryValue, Setting> join = root.join("settings", JoinType.LEFT);
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(join.get("code"), el.getKey()),
                            criteriaBuilder.isNull(join.get("value")),
                            criteriaBuilder.isFalse(root.get("isDelete"))
                    );
                });
            else if (!String.valueOf(el.getValue()).isBlank())
                list.add((root, query, criteriaBuilder) -> {
                    query.distinct(true);
                    Join<DictionaryValue, Setting> join = root.join("settings", JoinType.LEFT);
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(join.get("code"), el.getKey()),
                            join.get("value").in((List<String>) el.getValue()),
                            criteriaBuilder.isFalse(root.get("isDelete"))
                    );
                });
        }
    }

    public List<ShortDictionaryValueDto> getAllShortValuesByEqualsRequest(AllValuesRequestDto request) {
        /*var spList = getBaseSpecification(request);
        getSpecificationForEquals(spList, request);

        var specification = spList.stream().reduce(Specification::and).
                orElse((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dictionaryType").get("code"), request.getTypeCode()));
        var list = dictionaryValueRepository.findAll(specification);
        return list.stream().map(this::convertToShort).toList();*/
        return dictionaryValueRepository.findAll(getSpecificationValueByEquals(request)).stream().map(this::convertToShort).toList();
    }

    public PageDto<ShortDictionaryValueDto> getAllShortValuesByEqualsPageRequest(AllValuesPageRequestDto request) {
        Page<DictionaryValue> page = dictionaryValueRepository.findAll(getSpecificationValueByEquals(request),
                PageRequest.of(request.getPage(), request.getSize(), request.getDirection(), request.getSort()));
        return new PageDto<>(page.stream().map(this::convertToShort).toList(), page);
    }

    public List<FullDictionaryValueDto> getAllFullValuesByEqualsRequest(AllValuesRequestDto request) {
       /* var spList = getBaseSpecification(request);
        getSpecificationForEquals(spList, request);

        var specification = spList.stream().reduce(Specification::and).
                orElse((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dictionaryType").get("code"), request.getTypeCode()));
        var list = dictionaryValueRepository.findAll(specification);
        return list.stream().map(this::convertToFull).toList();*/
        return dictionaryValueRepository.findAll(getSpecificationValueByEquals(request)).stream().map(this::convertToFull).toList();
    }

    public PageDto<FullDictionaryValueDto> getAllFullValuesByEqualsPageRequest(AllValuesPageRequestDto request) {
        Page<DictionaryValue> page = dictionaryValueRepository.findAll(getSpecificationValueByEquals(request),
                PageRequest.of(request.getPage(), request.getSize(), request.getDirection(), request.getSort()));
        return new PageDto<>(page.stream().map(this::convertToFull).toList(), page);
    }

    private Specification<DictionaryValue> getSpecificationValueByEquals(AllValuesRequestDto request) {
        var spList = getBaseSpecification(request);
        getSpecificationForEquals(spList, request);
        return spList.stream().reduce(Specification::and).
                orElse((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dictionaryType").get("code"), request.getTypeCode()));
    }

    public List<ShortDictionaryValueDto> getAllShortValuesByInRequest(AllValuesRequestDto request) {
        /*var spList = getBaseSpecification(request);
        getSpecificationForIn(spList, request);

        var specification = spList.stream().reduce(Specification::and).
                orElse((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dictionaryType").get("code"), request.getTypeCode()));
        var list = dictionaryValueRepository.findAll(specification);
        return list.stream().map(this::convertToShort).toList();*/
        return dictionaryValueRepository.findAll(getSpecificationValueByIn(request)).stream().map(this::convertToShort).toList();
    }

    public PageDto<ShortDictionaryValueDto> getAllShortValuesByInPageRequest(AllValuesPageRequestDto request) {
        Page<DictionaryValue> page = dictionaryValueRepository.findAll(getSpecificationValueByIn(request),
                PageRequest.of(request.getPage(), request.getSize(), request.getDirection(), request.getSort()));
        return new PageDto<>(page.stream().map(this::convertToShort).toList(), page);
    }

    public List<FullDictionaryValueDto> getAllFullValuesByInRequest(AllValuesRequestDto request) {
        /*var spList = getBaseSpecification(request);
        getSpecificationForIn(spList, request);

        var specification = spList.stream().reduce(Specification::and).
                orElse((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dictionaryType").get("code"), request.getTypeCode()));
        var list = dictionaryValueRepository.findAll(specification);
        return list.stream().map(this::convertToFull).toList();*/
        return dictionaryValueRepository.findAll(getSpecificationValueByIn(request)).stream().map(this::convertToFull).toList();
    }

    public PageDto<FullDictionaryValueDto> getAllFullValuesByInPageRequest(AllValuesPageRequestDto request) {
        Page<DictionaryValue> page = dictionaryValueRepository.findAll(getSpecificationValueByIn(request),
                PageRequest.of(request.getPage(), request.getSize(), request.getDirection(), request.getSort()));
        return new PageDto<>(page.stream().map(this::convertToFull).toList(), page);
    }

    private Specification<DictionaryValue> getSpecificationValueByIn(AllValuesRequestDto request) {
        var spList = getBaseSpecification(request);
        getSpecificationForIn(spList, request);
        return spList.stream().reduce(Specification::and).
                orElse((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dictionaryType").get("code"), request.getTypeCode()));
    }

    @Transactional
    public void addConfig(AddConfigDto in) throws TestAopException {
        Config config = configRepository.findOne(
                (root, query, criteriaBuilder) -> {
                    Predicate[] arr = new Predicate[3];
                    arr[0] = criteriaBuilder.equal(root.get("dictionaryType").get("code"), in.getTypeCode());
                    arr[1] = criteriaBuilder.equal(root.get("code"), in.getCode());
                    arr[2] = criteriaBuilder.isFalse(root.get("isDelete"));
                    return criteriaBuilder.and(arr);
                }).orElse(null);
        if (nonNull(config)) {
            String str = "Config with typeCode: " + in.getTypeCode() + " and code: " + in.getCode() + " is present";
            log.error(str);
            throw new TestAopException(str);
        }
        DictionaryType type = getDictionaryType(in.getTypeCode());
        save(convert(in, type));
        type.getValues().forEach(e ->
                save(Setting.builder().code(in.getCode()).type(in.getType()).dictionaryValue(e).value(null).isDelete(false).build())
        );
    }

    public void deleteConfig(String typeCode, String configCode) throws TestAopException {
        String mask = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        Config config = findConfigByCodes(typeCode, configCode);
        config.setCode(config.getCode() + "_" + mask);
        config.setIsDelete(true);
        save(config);
        List<DictionaryValue> list = dictionaryValueRepository.findAllByDictionaryType_CodeAndIsDeleteFalse(typeCode);
        for (var el : list) {
            deleteSetting(typeCode, el.getCode(), configCode);
        }
    }


    private Config findConfigByCodes(String typeCode, String code) throws TestAopException {
        Config config = configRepository.findOne(
                (root, query, criteriaBuilder) -> {
                    Predicate[] arr = new Predicate[3];
                    arr[0] = criteriaBuilder.equal(root.get("dictionaryType").get("code"), typeCode);
                    arr[1] = criteriaBuilder.equal(root.get("code"), code);
                    arr[2] = criteriaBuilder.isFalse(root.get("isDelete"));
                    return criteriaBuilder.and(arr);
                }).orElse(null);
        if (isNull(config)) {
            String str = "Config with typeCode: " + typeCode + " and code: " + code + " not found";
            log.error(str);
            throw new TestAopException(str);
        }
        log.info("Config with typeCode: " + typeCode + " and code: " + code + " is found");
        return config;
    }

    private Config convert(AddConfigDto in, DictionaryType type) {
        Config out = new Config();
        out.setCode(in.getCode());
        out.setType(in.getType());
        out.setDictionaryType(type);
        out.setIsDelete(false);
        return out;
    }

    public List<ConfigDto> findConfigByCode(String code) {
        return getConfigsByCode(code).stream().map(this::convert).toList();
    }

    public List<Config> getConfigsByCode(String code) {
        return configRepository.findAll((root, query, criteriaBuilder) -> {
            Predicate[] arr = new Predicate[2];
            arr[0] = criteriaBuilder.equal(root.get("dictionaryType").get("code"), code);
            arr[1] = criteriaBuilder.isFalse(root.get("isDelete"));
            return criteriaBuilder.and(arr);
        });
    }

    private ConfigDto convert(Config in) {
        return ConfigDto.builder()
                .code(in.getCode())
                .type(in.getType())
                .example(in.getType().getExample())
                .build();
    }

}