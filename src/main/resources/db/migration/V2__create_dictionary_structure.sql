CREATE TABLE IF NOT EXISTS dictionary_type
(
    id            BIGSERIAL PRIMARY KEY NOT NULL,
    code          VARCHAR(250)          NOT NULL unique,
    name_ru       VARCHAR(500)          NULL,
    name_kk       VARCHAR(500)          NULL,
    name_en       VARCHAR(500)          NULL,
    short_name_ru VARCHAR(500)          NULL,
    short_name_kk VARCHAR(500)          NULL,
    short_name_en VARCHAR(500)          NULL,
    is_delete     BOOLEAN               NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS config
(
    id                   BIGSERIAL PRIMARY KEY NOT NULL,
    type_code varchar(250)          NOT NULL,
    code                 VARCHAR(250)          NOT NULL,
    type                 VARCHAR(50)           NOT NULL,
    is_delete            BOOLEAN               NOT NULL DEFAULT FALSE,
    CONSTRAINT config_code_dictionary_type_id_unique UNIQUE (code, type_code),
    CONSTRAINT fk_config_type FOREIGN KEY (type_code)
        REFERENCES errors.dictionary_type (code)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS dictionary_value
(
    id                   BIGSERIAL PRIMARY KEY NOT NULL,
    parent_id            bigint                NULL
        constraint fk_dictionary_value_parent_fk
            REFERENCES dictionary_value
            on update cascade on delete cascade,
    code                 VARCHAR(250)          NOT NULL,
    dictionary_type_code VARCHAR(250)          NOT NULL,
    -- constraint fk_dictionary_value_type
    --     REFERENCES dictionary_type
    --    on update cascade on delete cascade,
    name_ru              VARCHAR(3000)         NULL,
    name_kk              VARCHAR(3000)         NULL,
    name_en              VARCHAR(3000)         NULL,
    short_name_ru        VARCHAR(3000)         NULL,
    short_name_kk        VARCHAR(3000)         NULL,
    short_name_en        VARCHAR(3000)         NULL,
    is_delete            BOOLEAN               NOT NULL DEFAULT FALSE,
    CONSTRAINT dictionary_value_code_dictionary_type_id_unique UNIQUE (code, dictionary_type_code),
    CONSTRAINT fk_dictionary_value_type FOREIGN KEY (dictionary_type_code)
        REFERENCES errors.dictionary_type (code)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS settings
(
    id        BIGSERIAL PRIMARY KEY NOT NULL,
    code      VARCHAR(250)          NOT NULL,
    type      VARCHAR(50)           NOT NULL,
    value     VARCHAR(3000)         NULL,
    is_delete BOOLEAN               NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS dictionary_settings
(
    id                  BIGSERIAL PRIMARY KEY NOT NULL,
    dictionary_value_id BIGINT                NOT NULL
        CONSTRAINT fk_dictionary_settings_value
            REFERENCES dictionary_value
            on update cascade on delete cascade,
    settings_id         BIGINT                NOT NULL
        constraint fk_dictionary_settings_settings
            REFERENCES settings
            on update cascade on delete cascade,
    CONSTRAINT dictionary_settings_dictionary_value_id_settings_id_unique UNIQUE (dictionary_value_id, settings_id)
);