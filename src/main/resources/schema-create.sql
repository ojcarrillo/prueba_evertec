-- batch --
drop table if exists EVERTEC.batch_step_execution_context;
drop table if exists EVERTEC.batch_job_execution_context;
drop table if exists EVERTEC.batch_step_execution;
drop table if exists EVERTEC.batch_job_execution_params;
drop table if exists EVERTEC.batch_job_execution;
drop table if exists EVERTEC.batch_job_instance;
drop sequence if exists EVERTEC.batch_paso_execution_seq;
drop sequence if exists EVERTEC.batch_job_execution_seq;
drop sequence if exists EVERTEC.batch_job_seq;

create table EVERTEC.batch_job_instance
(
    job_instance_id bigint       not null primary key,
    version         bigint,
    job_name        varchar(100) not null,
    job_key         varchar(32)  not null,
    constraint job_inst_un unique (job_name, job_key)
);
create table EVERTEC.batch_job_execution
(
    job_execution_id           bigint        not null primary key,
    version                    bigint,
    job_instance_id            bigint        not null,
    create_time                timestamp     not null,
    start_time                 timestamp default null,
    end_time                   timestamp default null,
    status                     varchar(10),
    exit_code                  varchar(2500),
    exit_message               varchar(2500),
    last_updated               timestamp,
    job_configuration_location varchar(2500) null,
    constraint job_inst_exec_fk foreign key (job_instance_id)
        references EVERTEC.batch_job_instance (job_instance_id)
);
create table EVERTEC.batch_job_execution_params
(
    job_execution_id bigint       not null,
    type_cd          varchar(6)   not null,
    key_name         varchar(100) not null,
    string_val       text,
    date_val         timestamp default null,
    long_val         bigint,
    double_val       double precision,
    identifying      char(1)      not null,
    constraint job_exec_params_fk foreign key (job_execution_id)
        references EVERTEC.batch_job_execution (job_execution_id)
);
create table EVERTEC.batch_step_execution
(
    step_execution_id  bigint       not null primary key,
    version            bigint       not null,
    step_name          varchar(100) not null,
    job_execution_id   bigint       not null,
    start_time         timestamp    not null,
    end_time           timestamp default null,
    status             varchar(10),
    commit_count       bigint,
    read_count         bigint,
    filter_count       bigint,
    write_count        bigint,
    read_skip_count    bigint,
    write_skip_count   bigint,
    process_skip_count bigint,
    rollback_count     bigint,
    exit_code          varchar(2500),
    exit_message       varchar(2500),
    last_updated       timestamp,
    constraint job_exec_step_fk foreign key (job_execution_id)
        references EVERTEC.batch_job_execution (job_execution_id)
);
create table EVERTEC.batch_step_execution_context
(
    step_execution_id  bigint        not null primary key,
    short_context      varchar(2500) not null,
    serialized_context text,
    constraint step_exec_ctx_fk foreign key (step_execution_id)
        references EVERTEC.batch_step_execution (step_execution_id)
);
create table EVERTEC.batch_job_execution_context
(
    job_execution_id   bigint        not null primary key,
    short_context      varchar(2500) not null,
    serialized_context text,
    constraint job_exec_ctx_fk foreign key (job_execution_id)
        references EVERTEC.batch_job_execution (job_execution_id)
);
create sequence EVERTEC.batch_step_execution_seq maxvalue 9223372036854775807 no cycle;
create sequence EVERTEC.batch_job_execution_seq maxvalue 9223372036854775807 no cycle;
create sequence EVERTEC.batch_job_seq maxvalue 9223372036854775807 no cycle;

/*************************************************/
/* tabla para guardar las deudas de los clientes */
CREATE TABLE EVERTEC.DEUDACLIENTE 
( 
    id IDENTITY NOT NULL PRIMARY KEY ,  -- ⬅ `identity` = auto-incrementing long integer.
    idCliente VARCHAR NOT NULL ,
    nombreCliente VARCHAR NOT NULL ,
    correoCliente VARCHAR NOT NULL ,
    montoDeuda DECIMAL(20, 2) NOT NULL ,
    idDeuda VARCHAR NOT NULL ,
    fechaVencimiento TIMESTAMP WITH TIME ZONE NOT NULL 
) 
;