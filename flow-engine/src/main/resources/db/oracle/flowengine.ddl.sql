create sequence SEQ_FLOWENGINE
minvalue  1
maxvalue  99999999999999999999
start  with  1
increment  by  1
cache  20;


CREATE TABLE FLOW_TRACE (
  ID  INTEGER PRIMARY KEY,
  ORDER_ID  VARCHAR2(128) NOT NULL,
  TRACE_ID  VARCHAR2(64) UNIQUE NOT NULL,
  FLOW_NAME VARCHAR2(64) NOT NULL,
  VERSION INTEGER NOT NULL,
  NODE VARCHAR2(64) NOT NULL,
  RETRY_TIMES INTEGER DEFAULT 0,
  NEXT_RETRY_TIME TIMESTAMP,
  NODE_NAME VARCHAR2(64),
  RETRY_MAX INTEGER,
  RETRY_MAX_LIMIT_NODE  VARCHAR2(64),
  TARGET  VARCHAR2(64) NOT NULL,
  RETRY_FAIL_TYPE VARCHAR2(16),
  RETREAT_UNIT  INTEGER,
  RETREAT_TYPE  VARCHAR2(16),
  RETREAT_TIME_UNIT VARCHAR2(16),
  START_TIME  TIMESTAMP NOT NULL,
  UPDATE_TIME TIMESTAMP DEFAULT SYSTIMESTAMP,
  EVENT_ID  VARCHAR2(64),
  EVENT_TIME  TIMESTAMP,
  EXECUTION_TARGET  CLOB,
  ATTACHMENT  CLOB
);
CREATE INDEX IDX_STIME ON FLOW_TRACE(START_TIME);
CREATE INDEX IDX_UTIME ON FLOW_TRACE(UPDATE_TIME);
CREATE INDEX IDX_NODE ON FLOW_TRACE(NODE);


CREATE TABLE FLOW_TRACE_BOMB (
  ID  INTEGER PRIMARY KEY,
  ORDER_ID  VARCHAR2(128) NOT NULL,
  TRACE_ID  VARCHAR2(64) UNIQUE NOT NULL,
  FLOW_NAME VARCHAR2(64) NOT NULL,
  VERSION INTEGER NOT NULL,
  NODE VARCHAR2(64) NOT NULL,
  RETRY_TIMES INTEGER DEFAULT 0,
  NEXT_RETRY_TIME TIMESTAMP,
  NODE_NAME VARCHAR2(64),
  RETRY_MAX INTEGER,
  RETRY_MAX_LIMIT_NODE  VARCHAR2(64),
  TARGET  VARCHAR2(64) NOT NULL,
  RETRY_FAIL_TYPE VARCHAR2(16),
  RETREAT_UNIT  INTEGER,
  RETREAT_TYPE  VARCHAR2(16),
  RETREAT_TIME_UNIT VARCHAR2(16),
  START_TIME  TIMESTAMP NOT NULL,
  UPDATE_TIME TIMESTAMP DEFAULT SYSTIMESTAMP,
  EVENT_ID  VARCHAR2(64),
  EVENT_TIME  TIMESTAMP,
  EXECUTION_TARGET  CLOB,
  ATTACHMENT  CLOB
);
CREATE INDEX IDX_STIME_B ON FLOW_TRACE_BOMB(START_TIME);
CREATE INDEX IDX_UTIME_B ON FLOW_TRACE_BOMB(UPDATE_TIME);
CREATE INDEX IDX_NODE_B ON FLOW_TRACE_BOMB(NODE);


CREATE TABLE FLOW_TRACE_HISTORY (
   ID  INTEGER PRIMARY KEY,
  ORDER_ID  VARCHAR2(128) NOT NULL,
  TRACE_ID  VARCHAR2(64) UNIQUE NOT NULL,
  FLOW_NAME VARCHAR2(64) NOT NULL,
  VERSION INTEGER NOT NULL,
  NODE VARCHAR2(64) NOT NULL,
  RETRY_TIMES INTEGER DEFAULT 0,
  NEXT_RETRY_TIME TIMESTAMP,
  NODE_NAME VARCHAR2(64),
  RETRY_MAX INTEGER,
  RETRY_MAX_LIMIT_NODE  VARCHAR2(64),
  TARGET  VARCHAR2(64) NOT NULL,
  RETRY_FAIL_TYPE VARCHAR2(16),
  RETREAT_UNIT  INTEGER,
  RETREAT_TYPE  VARCHAR2(16),
  RETREAT_TIME_UNIT VARCHAR2(16),
  START_TIME  TIMESTAMP NOT NULL,
  UPDATE_TIME TIMESTAMP DEFAULT SYSTIMESTAMP,
  END_TIME  TIMESTAMP NOT NULL,
  ERROR VARCHAR2(256)  NOT NULL,
  EVENT_ID  VARCHAR2(64),
  EVENT_TIME  TIMESTAMP,
  EXECUTION_TARGET  CLOB,
  ATTACHMENT  CLOB
);