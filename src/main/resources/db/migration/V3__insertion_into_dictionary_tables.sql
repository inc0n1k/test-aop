DO
$$
    DECLARE
        code_type VARCHAR;
        v_id      BIGINT;
        s_id      BIGINT;

    BEGIN
        --First
        insert into errors.dictionary_type (code, name_ru, name_kk, name_en, short_name_ru, short_name_kk,
                                            short_name_en)
        values ('codeType0', 'nameTypeRu0', 'nameTypeKk0', 'nameTypeEn0', 'shortNameTypeRu0', 'shortNameTypeKk0',
                'shortNameTypeEn0')
        returning code into code_type;
        insert into errors.config (type_code, code, type) values (code_type, 'codeSetting0', 'STRING');
        insert into errors.dictionary_value(code, dictionary_type_code, name_ru, name_kk, name_en,
                                            short_name_ru, short_name_kk, short_name_en)
        values ('codeValue0', 'codeType0', 'nameValueRu0', 'nameValueKk0', 'nameValueEn0', 'shortNameValueRu0',
                'shortNameValueKk0', 'shortNameValueEn0')
        returning id into v_id;
        insert into errors.settings (code, type, value)
        values ('codeSetting0', 'STRING', 'value0')
        returning id into s_id;
        insert into errors.dictionary_settings (dictionary_value_id, settings_id) values (v_id, s_id);
        --Second
        insert into errors.dictionary_type (code, name_ru, name_kk, name_en, short_name_ru, short_name_kk,
                                            short_name_en)
        values ('codeType1', 'nameTypeRu1', 'nameTypeKk1', 'nameTypeEn1', 'shortNameTypeRu1', 'shortNameTypeKk1',
                'shortNameTypeEn1')
        returning code into code_type;
        insert into errors.config (type_code, code, type) values (code_type, 'codeSetting1', 'STRING');
        insert into errors.dictionary_value(code, dictionary_type_code, name_ru, name_kk, name_en,
                                            short_name_ru, short_name_kk, short_name_en)
        values ('codeValue1', 'codeType1', 'nameValueRu1', 'nameValueKk1', 'nameValueEn1', 'shortNameValueRu1',
                'shortNameValueKk1', 'shortNameValueEn1')
        returning id into v_id;
        insert into errors.settings (code, type, value)
        values ('codeSetting1', 'STRING', 'value1')
        returning id into s_id;
        insert into errors.dictionary_settings (dictionary_value_id, settings_id) values (v_id, s_id);
        --Third
        insert into errors.dictionary_type (code, name_ru, name_kk, name_en, short_name_ru, short_name_kk,
                                            short_name_en)
        values ('codeType2', 'nameTypeRu2', 'nameTypeKk2', 'nameTypeEn2', 'shortNameTypeRu2', 'shortNameTypeKk2',
                'shortNameTypeEn2')
        returning code into code_type;
        insert into errors.config (type_code, code, type) values (code_type, 'codeSetting2', 'STRING');
        insert into errors.dictionary_value(code, dictionary_type_code, name_ru, name_kk, name_en,
                                            short_name_ru, short_name_kk, short_name_en)
        values ('codeValue2', 'codeType2', 'nameValueRu2', 'nameValueKk2', 'nameValueEn2', 'shortNameValueRu2',
                'shortNameValueKk2', 'shortNameValueEn2')
        returning id into v_id;
        insert into errors.settings (code, type, value)
        values ('codeSetting2', 'STRING', 'value2')
        returning id into s_id;
        insert into errors.dictionary_settings (dictionary_value_id, settings_id) values (v_id, s_id);
    END;
$$
language plpgsql;