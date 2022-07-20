package test.aop.testaop.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.aop.testaop.dto.constant.DisplayType;
import test.aop.testaop.dto.dictionary.*;
import test.aop.testaop.exception.TestAopException;
import test.aop.testaop.service.dictionary.DictionaryService;
import test.aop.testaop.utils.ApiResponse;
import test.aop.testaop.utils.BaseController;

@RestController
public class DictionaryController extends BaseController {

    private final DictionaryService dictionaryService;

    public DictionaryController(
            DictionaryService dictionaryService
    ) {
        this.dictionaryService = dictionaryService;
    }

    /**
     * Типы справочников
     */
    //tested
    @PostMapping("/dictionary/type/add")
    public ResponseEntity<ApiResponse> addType(
            @Valid @RequestBody AddDictionaryTypeDto dto
    ) throws TestAopException {
        dictionaryService.addType(dto);
        return ok();
    }

    //tested
    @PostMapping("/dictionary/type/update")
    public ResponseEntity<ApiResponse> updateType(
            @Valid @RequestBody UpdateDictionaryTypeDto dto
    ) throws TestAopException {
        dictionaryService.updateType(dto);
        return ok();
    }

    //tested
    @DeleteMapping("/dictionary/type/delete")
    public ResponseEntity<ApiResponse> deleteType(
            @RequestParam String code
    ) throws TestAopException {
        dictionaryService.deleteType(code);
        return ok();
    }

    //tested
    @GetMapping("/dictionary/type/all")
    public ResponseEntity<ApiResponse> allTypes() {
        return ok(dictionaryService.findAllTypes());
    }

    //tested
    @PostMapping("/dictionary/type/all-page")
    public ResponseEntity<ApiResponse> allTypePage(
            @Valid @RequestBody DictionaryTypePageRequestDto dto
    ) {
        return ok(dictionaryService.findAllTypePageDto(dto));
    }

    //tested
    @GetMapping("/dictionary/type/code")
    public ResponseEntity<ApiResponse> findTypeByCode(
            @RequestParam String code,
            @RequestParam(required = false, defaultValue = "SHORT") DisplayType displayType
    ) throws TestAopException {
        return ok(
                switch (displayType) {
                    case SHORT -> dictionaryService.findShortDictionaryTypeDto(code);
                    case FULL -> dictionaryService.findFullDictionaryTypeDto(code);
                }
        );
    }

    /**
     * Значения справочников
     */
    @PostMapping("/dictionary/value/add")
    public ResponseEntity<ApiResponse> addValue(
            @Valid @RequestBody AddDictionaryValueDto in
    ) throws TestAopException {
        dictionaryService.addValue(in);
        return ok();
    }

    @PostMapping("/dictionary/value/update")
    public ResponseEntity<ApiResponse> updateValue(
            @Valid @RequestBody UpdateDictionaryValueDto in
    ) throws TestAopException {
        dictionaryService.updateValue(in);
        return ok();
    }

    @DeleteMapping("/dictionary/value/delete")
    public ResponseEntity<ApiResponse> deleteValue(
            @RequestParam String typeCode,
            @RequestParam String valueCode
    ) throws TestAopException {
        dictionaryService.deleteValue(typeCode, valueCode);
        return ok();
    }

    //tested
    @GetMapping("/dictionary/value/all")
    public ResponseEntity<ApiResponse> allValues(
            @RequestParam String typeCode,
            @RequestParam(required = false, defaultValue = "SHORT") DisplayType displayType
    ) {
        return ok(
                switch (displayType) {
                    case FULL -> dictionaryService.findAllFullValueDto(typeCode);
                    case SHORT -> dictionaryService.findAllShortValueDto(typeCode);
                }
        );
    }

    //tested
    @PostMapping("/dictionary/value/all-page")
    public ResponseEntity<ApiResponse> allValuePage(
            @Valid @RequestBody DictionaryValuePageRequestDto dto
    ) {
        return ok(
                switch (dto.getDisplayType()) {
                    case FULL -> dictionaryService.findAllFullValuePageDto(dto);
                    case SHORT -> dictionaryService.findAllShortValuePageDto(dto);
                }
        );
    }

    //tested
    @GetMapping("/dictionary/value/codes")
    public ResponseEntity<ApiResponse> findValueByCodes(
            @RequestParam String valueCode,
            @RequestParam String typeCode,
            @RequestParam(required = false, defaultValue = "SHORT") DisplayType displayType
    ) throws TestAopException {
        return ok(
                switch (displayType) {
                    case FULL -> dictionaryService.findFullDictionaryValueDtoByCodes(valueCode, typeCode);
                    case SHORT -> dictionaryService.findShortDictionaryValueDtoByCodes(valueCode, typeCode);
                }
        );
    }

    //tested
    @GetMapping("/dictionary/value/tree")
    public ResponseEntity<ApiResponse> getFullTreeValue(
            @RequestParam String code,
            @RequestParam(required = false, defaultValue = "SHORT") DisplayType displayType
    ) {
        return ok(
                switch (displayType) {
                    case FULL -> dictionaryService.getFullTreeValueDtoList(code);
                    case SHORT -> dictionaryService.getShortTreeValueDto(code);
                }
        );
    }

    /**
     * Настройки типов справочников
     */
    @PostMapping("/dictionary/config/add")
    public ResponseEntity<ApiResponse> addConfig(
            @Valid @RequestBody AddConfigDto dto
    ) throws TestAopException {
        dictionaryService.addConfig(dto);
        return ok();
    }

    @DeleteMapping("/dictionary/config/delete")
    public ResponseEntity<ApiResponse> deleteConfig(
            @RequestParam String typeCode,
            @RequestParam String configCode
    ) throws TestAopException {
        dictionaryService.deleteConfig(typeCode, configCode);
        return ok();
    }

    @GetMapping("/dictionary/config/code")
    public ResponseEntity<ApiResponse> findConfigByCode(
            @RequestParam String code
    ) {
        return ok(dictionaryService.findConfigByCode(code));
    }

    /**
     * Настройки значений справочников
     */
    @PostMapping("/dictionary/setting/update")
    public ResponseEntity<ApiResponse> addSetting(
            @Valid @RequestBody UpdateSettingsDto dto
    ) throws TestAopException {
        dictionaryService.updateSettings(dto);
        return ok();
    }

//    @DeleteMapping("/dictionary/setting/delete")
//    public ResponseEntity<ApiResponse> deleteSetting(
//            @RequestParam String typeCode,
//            @RequestParam String valueCode,
//            @RequestParam String code
//    ) throws TestAopException {
//        dictionaryService.deleteSetting(typeCode, valueCode, code);
//        return ok();
//    }

    @GetMapping("/dictionary/setting/all")
    public ResponseEntity<ApiResponse> getAllSettings(
            @RequestParam String typeCode,
            @RequestParam String valueCode
    ) throws TestAopException {
        return ok(dictionaryService.getAllSettings(typeCode, valueCode));
    }

    //******************************************************************************************************************

    @PostMapping("/dictionary/value/request/all")
    public ResponseEntity<ApiResponse> getValuesByRequest(
            @Valid @RequestBody AllValuesRequestDto request
    ) {
        return ok(
                switch (request.getRequestType()) {
                    case IN -> switch (request.getDisplayType()) {
                        case FULL -> dictionaryService.getAllFullValuesByInRequest(request);
                        case SHORT -> dictionaryService.getAllShortValuesByInRequest(request);
                    };
                    case EQUALS -> switch (request.getDisplayType()) {
                        case FULL -> dictionaryService.getAllFullValuesByEqualsRequest(request);
                        case SHORT -> dictionaryService.getAllShortValuesByEqualsRequest(request);
                    };
                }
        );
    }

    @PostMapping("/dictionary/value/request/all-page")
    public ResponseEntity<ApiResponse> getValuesPageByRequest(
            @Valid @RequestBody AllValuesPageRequestDto request
    ) {
        return ok(
                switch (request.getRequestType()) {
                    case IN -> switch (request.getDisplayType()) {
                        case FULL -> dictionaryService.getAllFullValuesByInPageRequest(request);
                        case SHORT -> dictionaryService.getAllShortValuesByInPageRequest(request);
                    };
                    case EQUALS -> switch (request.getDisplayType()) {
                        case FULL -> dictionaryService.getAllFullValuesByEqualsPageRequest(request);
                        case SHORT -> dictionaryService.getAllShortValuesByEqualsPageRequest(request);
                    };
                }
        );
    }

    //******************************************************************************************************************

}