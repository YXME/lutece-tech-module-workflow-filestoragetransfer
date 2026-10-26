-- liquibase formatted sql
-- changeset workflow-filestoragetransfer:create_db_workflow-filestoragetransfer.sql
-- preconditions onFail:MARK_RAN onError:WARN

--
-- Structure for table workflow_task_file_transfer_config
--

DROP TABLE IF EXISTS workflow_task_file_transfer_config;
-- a tab, not a space, follows context: the HSQL translation of the test build rewrites 'text ' into 'VARCHAR '
CREATE TABLE workflow_task_file_transfer_config (
  id_task int NOT NULL,
  resource_type varchar(50) NOT NULL,
  resource_code varchar(50) NOT NULL,
  entry_code varchar(50) NOT NULL,
  target_fileserviceprovider_name varchar(50) NOT NULL,
  context	varchar(50) NOT NULL,
  PRIMARY KEY (id_task)
);


