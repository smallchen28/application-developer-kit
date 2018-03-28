/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */

package com.yiji.adk.plan.task.module;

import com.yiji.adk.plan.task.PlanTaskScheduler;
import com.yiji.adk.plan.task.statement.ExecutorStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author hasulee
 * @email ligen@yiji.com
 * @since 1.0.0 2014-1-6 T
 * @version 1.0.0
 * @see
 */
public class SchedulerRepository {

	private final String tablePrefix;

	private final JdbcTemplate jdbcTemplate;

	private final OracleSequenceMaxValueIncrementer incrementer;

	public static final String UPDATE_EXP = "UPDATE #tableNamePre#PLAN_TASK SET EXEC_STATUS = ?, EXEC_COUNT = ?, EXEC_START_TIME = ?, EXEC_LAST_TIME = ?, EXEC_NEXT_TIME = ?, EXEC_CONTEXT = ?, EXEC_MEMO = ?, CURRENT_STAT_NAME = ? WHERE TASK_IDENTITY = ?";

	public static final String UPDATE_STAT_EXP = "UPDATE #tableNamePre#TASK_STATEMENT SET  EXEC_STATUS = ? , EXEC_INFO = ?, START_TIME = ?, END_TIME = ?, EXEC_COUNT = ? WHERE TASK_IDENTITY = ? and TASK_STAT_NAME=?";

	public static final String INSERT_EXP = "INSERT INTO #tableNamePre#PLAN_TASK(TASK_IDENTITY,"
			+ "TASK_NAME, TASK_EXPIRATION, REPEAT_FLAG,"
			+ "TASK_EXP, EXEC_STATUS, EXEC_COUNT,"
			+ "EXEC_MAX_COUNT,EXEC_START_TIME,EXEC_LAST_TIME,"
			+ "EXEC_NEXT_TIME, EXEC_CONTEXT, EXEC_MEMO,"
			+ "CURRENT_STAT_NAME, CRT_JOBD, task_type,RAW_ADD_TIME"
			+ ")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";

	public static final String STATEMENT_INSERT_EXP = "INSERT INTO #tableNamePre#TASK_STATEMENT(TASK_IDENTITY, TASK_STAT_NAME, STAT_CLASS_TYPE,"
			+ "EXEC_PRIORITY, EXEC_INFO, CRT_JOBD, START_TIME , END_TIME, EXEC_COUNT"
			+ ")VALUES(" + "?,?,?,?,?,?,?,?,?)";

	public static final String CURRENT_TIME_EXP = " select sysdate from dual";

	public static final String LOAD_ACTIVE_EXP = "select * from #tableNamePre#PLAN_TASK  where task_identity = (select min(task_identity) from #tableNamePre#PLAN_TASK where task_name=? and (exec_status='PROCESSING' or exec_status='ERROR' or exec_status = 'INIT')) for update nowait";

	public static final String LOAD_ACTIVE_STATEMENT_EXP = "SELECT TASK_IDENTITY , TASK_STAT_NAME, STAT_CLASS_TYPE, EXEC_PRIORITY, EXEC_STATUS,EXEC_INFO,CRT_JOBD,START_TIME,END_TIME,EXEC_COUNT FROM #tableNamePre#task_statement WHERE TASK_IDENTITY = ?";

	public static final String LOAD_LAST_CREATED_EXP = "select * from #tableNamePre#PLAN_TASK  where task_identity = (select max(task_identity) from #tableNamePre#PLAN_TASK where task_name=?) for update nowait";

	public SchedulerRepository(JdbcTemplate jdbcTemplate, OracleSequenceMaxValueIncrementer incrementer,String tablePrefix) {

		this.jdbcTemplate = jdbcTemplate;

		this.incrementer = incrementer;

		this.tablePrefix = tablePrefix;
	}

	public int restore(PlanTask planTask, final PlanTaskStatement statement) {

		return jdbcTemplate.update(UPDATE_STAT_EXP.replaceAll("#\\S*#", tablePrefix),
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {

						ps.setString(1, statement.getExecStatus());

						ps.setString(2, statement.getExecInfo());

						ps.setTimestamp(3, statement.getStartTIme());

						ps.setTimestamp(4, statement.getEndTime());

						ps.setInt(5, statement.getExecCount());

						ps.setLong(6, statement.getPlanTaskIdentity());

						ps.setString(7, statement.getTaskStateName());
					}
				});
	}

	public int store(PlanTaskScheduler scheduler) {

		final PlanTask planTask = scheduler.getPlanTaskPrototype();

		Set<PlanTaskStatement> statements = planTask.getStatements();

		planTask.setIdentity(incrementer.nextLongValue());
		int modifyCount = jdbcTemplate.update(INSERT_EXP.replaceAll("#\\S*#", tablePrefix),
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {

						ps.setLong(1, planTask.getIdentity());
						ps.setString(2, planTask.getTaskName());
						ps.setLong(3, planTask.getTaskExpiration());
						ps.setBoolean(4, planTask.isRepeatFlag());

						ps.setString(5, planTask.getTaskExp());
						ps.setString(6, ExecutorStatus.INIT.toString());
						ps.setInt(7, 0);

						ps.setInt(8, planTask.getExecMaxCount());
						ps.setTimestamp(9, (Timestamp) null);
						ps.setTimestamp(10, (Timestamp) null);

						ps.setTimestamp(11, (Timestamp) null);
						ps.setString(12, planTask.getExeContext());
						ps.setString(13, planTask.getExecMemo());

						ps.setString(14, planTask.getCurrentStatName());
						ps.setString(15, planTask.getCrtJobd());
						ps.setString(16, planTask.getTaskType().toString());

					}
				});

		jdbcTemplate.batchUpdate(STATEMENT_INSERT_EXP.replaceAll("#\\S*#", tablePrefix), statements,
				statements.size(), new ParameterizedPreparedStatementSetter<PlanTaskStatement>() {

					@Override
					public void setValues(PreparedStatement ps, PlanTaskStatement statement) throws SQLException {

						ps.setLong(1, planTask.getIdentity());
						ps.setString(2, statement.getTaskStateName());
						ps.setString(3, statement.getStateClassType());
						ps.setInt(4, statement.getExecPriority());
						ps.setString(5, statement.getExecInfo());
						ps.setString(6, statement.getCrtJobd());
						ps.setTimestamp(7, null);
						ps.setTimestamp(8, null);
						ps.setInt(9, statement.getExecCount());

					}
				});

		return modifyCount;
	}

	public PlanTask active(String taskName) {

		PlanTask planTask = jdbcTemplate.query(LOAD_ACTIVE_EXP.replaceAll("#\\S*#", tablePrefix),
				new Object[] { taskName }, new ResultSetExtractor<PlanTask>() {

					@Override
					public PlanTask extractData(ResultSet rs) throws SQLException, DataAccessException {

						PlanTask planTask = null;

						if (rs.next()) {
							planTask = new PlanTask();
							planTask.setIdentity(rs.getLong(1));
							planTask.setTaskName(rs.getString(2));
							planTask.setTaskType(PlanTask.TaskType.valueOf(rs.getString(3)));
							planTask.setTaskExpiration(rs.getLong(4));
							planTask.setRepeatFlag(rs.getBoolean(5));
							planTask.setTaskExp(rs.getString(6));
							planTask.setExecStatus(ExecutorStatus.valueOf(rs.getString(7)));
							planTask.setExeCount(rs.getInt(8));
							planTask.setExecMaxCount(rs.getInt(9));
							planTask.setExecStartTime(rs.getTimestamp(10));
							planTask.setExecLastTime(rs.getTimestamp(11));
							planTask.setExecNextTime(rs.getTimestamp(12));
							planTask.setExeContext(rs.getString(13));
							planTask.setExecMemo(rs.getString(14));
							planTask.setCurrentStatName(rs.getString(15));
							planTask.setCrtJobd(rs.getString(16));
							planTask.setRawAddTime(rs.getTimestamp(17));
						}

						return planTask;
					}
				});

		if (planTask != null && planTask.getIdentity() != 0) {
			try {
				TreeSet<PlanTaskStatement> statements = new TreeSet<>(jdbcTemplate.query(
						LOAD_ACTIVE_STATEMENT_EXP.replaceAll("#\\S*#", tablePrefix),
						new Object[] { planTask.getIdentity() }, new RowMapper<PlanTaskStatement>() {

							@Override
							public PlanTaskStatement mapRow(ResultSet rs, int rowNum) throws SQLException {

								PlanTaskStatement statement = new PlanTaskStatement();
								statement.setPlanTaskIdentity(rs.getLong(1));
								statement.setTaskStateName(rs.getString(2));
								statement.setStateClassType(rs.getString(3));
								statement.setExecPriority(rs.getInt(4));
								statement.setExecStatus(rs.getString(5));
								statement.setExecInfo(rs.getString(6));
								statement.setCrtJobd(rs.getString(7));
								statement.setStartTIme(rs.getTimestamp(8));
								statement.setEndTime(rs.getTimestamp(9));
								statement.setExecCount(rs.getInt(10));
								return statement;
							}
						})
				);
				planTask.setStatements(statements);
			} catch (IncorrectResultSizeDataAccessException e) {
				//nothing ...
			}
		}

		return planTask;
	}

	public PlanTask loadLastCerate(String taskName) {

		PlanTask planTask = jdbcTemplate.query(LOAD_LAST_CREATED_EXP.replaceAll("#\\S*#", tablePrefix),
				new Object[] { taskName }, new ResultSetExtractor<PlanTask>() {

					@Override
					public PlanTask extractData(ResultSet rs) throws SQLException, DataAccessException {

						PlanTask planTask = null;

						if (rs.next()) {
							planTask = new PlanTask();
							planTask.setIdentity(rs.getLong(1));
							planTask.setTaskName(rs.getString(2));
							planTask.setTaskType(PlanTask.TaskType.valueOf(rs.getString(3)));
							planTask.setTaskExpiration(rs.getLong(4));
							planTask.setRepeatFlag(rs.getBoolean(5));
							planTask.setTaskExp(rs.getString(6));
							planTask.setExecStatus(ExecutorStatus.valueOf(rs.getString(7)));
							planTask.setExeCount(rs.getInt(8));
							planTask.setExecMaxCount(rs.getInt(9));
							planTask.setExecStartTime(rs.getTimestamp(10));
							planTask.setExecLastTime(rs.getTimestamp(11));
							planTask.setExecNextTime(rs.getTimestamp(12));
							planTask.setExeContext(rs.getString(13));
							planTask.setExecMemo(rs.getString(14));
							planTask.setCurrentStatName(rs.getString(15));
							planTask.setCrtJobd(rs.getString(16));
							planTask.setRawAddTime(rs.getTimestamp(17));
						}

						return planTask;
					}
				});
		return planTask;
	}

	public Timestamp currentTimestamp() {

		return jdbcTemplate.queryForObject(CURRENT_TIME_EXP, Timestamp.class);
	}

	public int restore(final PlanTask planTask) {

		int modifyCount = jdbcTemplate.update(UPDATE_EXP.replaceAll("#\\S*#", tablePrefix),
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {

						ps.setString(1, planTask.getExecStatus().toString());

						ps.setInt(2, planTask.getExeCount());

						ps.setTimestamp(3, planTask.getExecStartTime());

						ps.setTimestamp(4, planTask.getExecLastTime());

						ps.setTimestamp(5, planTask.getExecNextTime());

						ps.setString(6, planTask.getExeContext());

						ps.setString(7, planTask.getExecMemo());

						ps.setString(8, planTask.getCurrentStatName());

						ps.setLong(9, planTask.getIdentity());
					}
				});

		return modifyCount;
	}

}
