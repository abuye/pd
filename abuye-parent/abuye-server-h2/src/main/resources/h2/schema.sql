/*********************
 * datasource
 *********************/
drop table if exists aby_datasource;
drop sequence if exists seq_aby_datasource;
create table aby_datasource (
    id bigint,
    name varchar(255),
    notes varchar(255),
    db_type varchar(255) default 'h2',
    conn_type varchar(255) default 'jdbc',
    jndi varchar(255),
    jdbc_url varchar(1023),
    db_user varchar(255),
    db_pass varchar(255),
    db_driver varchar(255),
    dialect_name varchar(255),
    db_catalog varchar(255),
    db_schema varchar(255),
    deleted_flag varchar(1) default '0',
    primary key (id)
);
create sequence seq_aby_datasource;

/*********************
 * table
 *********************/
drop table if exists aby_table;
drop sequence if exists seq_aby_table;
create table aby_table (
    id bigint,
    datasource_id bigint,
    name varchar(255),
    is_view varchar(1) default '0',
    sql_script varchar(2047),
    notes varchar(1023),
    primary key (id)
);
create sequence seq_aby_table;

/*********************
 * tableColumn
 *********************/
drop table if exists aby_table_column;
drop sequence if exists seq_aby_table_column;
create table aby_table_column (
    id bigint,
    table_id bigint,
    name varchar(255),
    data_type varchar(255) default 'string',
    column_name varchar(255),
    is_required varchar(1) default '0',
    is_unique varchar(1) default '0',
    is_date varchar(1) default '0',
    is_dict varchar(1) default '0',
    save_pattern varchar(255),
    dict_type varchar(255),
    notes varchar(1023),
    deleted_flag varchar(1) default '0',
    primary key (id)
);
create sequence seq_aby_table_column;

/*********************
 * tableParam
 *********************/
drop table if exists aby_table_param;
drop sequence if exists seq_aby_table_param;
create table aby_table_param (
    id bigint,
    table_id bigint,
    name varchar(255),
    data_type varchar(255) default 'string',
    param_name varchar(255),
    is_date varchar(1) default '0',
    is_dict varchar(1) default '0',
    save_pattern varchar(255),
    dict_type varchar(255),
    notes varchar(1023),
    deleted_flag varchar(1) default '0',
    primary key (id)
);
create sequence seq_aby_table_param;
