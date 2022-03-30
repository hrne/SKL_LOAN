package com.st1.ifx.service.jdbc;

import java.util.Date;

import javax.sql.DataSource;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.st1.ifx.file.FileImport;
import com.st1.ifx.service.FileImportService;

@Service("fileImportService")
@Repository
@Transactional
public class JdbcFileImportService implements FileImportService {

	private JdbcTemplate jdbcTemplate;

	private JobExplorer jobExplorer;

	// public JdbcFileImportRepository(DataSource dataSource,
	// JobExplorer jobExplorer) {
	// this.jdbcTemplate = new JdbcTemplate(dataSource);
	// this.jobExplorer = jobExplorer;
	// }
	@Autowired
	public void setJobExplorer(JobExplorer jobExplorer) {
		this.jobExplorer = jobExplorer;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void createFileImport(String original, String importId) throws Exception {
		int count = jdbcTemplate.queryForObject(
				"select count(1) from FX_file_import where original= ? and import_id = ?",
				new Object[] { original, importId }, Integer.class);
		if (count > 0) {
			throw new Exception("Import-id already exists in table file_import, : " + importId);
		}
		jdbcTemplate.update("insert into FX_file_import (original,import_id,creation_date) values (?,?,?)",
				original.toLowerCase(), importId, new Date());

	}

	@Override
	public void mapImporttoJobInstance(String original, String importId, Long jobInstanceId) {
		jdbcTemplate.update("update FX_file_import set job_instance_id = ? where original = ? and import_id = ?",
				jobInstanceId, original.toLowerCase(), importId);

	}

	@Override
	public FileImport get(String original, String importId) {
		int count = jdbcTemplate.queryForObject(
				"select count(1) from FX_file_import where original=? and import_id = ?",
				new Object[] { original, importId }, Integer.class);
		if (count == 0) {
			throw new EmptyResultDataAccessException("No import with this ID: " + original + "," + importId, 1);
		}
		String status = "PENDING";
		Long instanceId = jdbcTemplate.queryForObject(
				"select job_instance_id from FX_file_import where original = ? and import_id = ?",
				new Object[] { original }, Long.class);
		JobInstance jobInstance = jobExplorer.getJobInstance(instanceId);
		if (jobInstance != null) {
			JobExecution lastJobExecution = jobExplorer.getJobExecutions(jobInstance).get(0);
			status = lastJobExecution.getStatus().toString();
		}
		return new FileImport(original, importId, status);
	}

}
