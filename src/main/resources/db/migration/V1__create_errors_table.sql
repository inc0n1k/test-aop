drop table if exists errors;

CREATE TABLE
    errors
(
    "id"          BIGSERIAL NOT NULL PRIMARY KEY,
    "module"      VARCHAR   NOT NULL,
    "date"        DATE      NOT NULL,
    "date_time"   TIMESTAMP NOT NULL,
    "method"      VARCHAR   NOT NULL,
    "request"     VARCHAR,
    "url"         VARCHAR,
    "error"       VARCHAR   NOT NULL,
    "stack_trace" VARCHAR
);