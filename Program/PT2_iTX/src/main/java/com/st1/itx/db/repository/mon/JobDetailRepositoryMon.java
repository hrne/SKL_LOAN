package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JobDetail;
import com.st1.itx.db.domain.JobDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JobDetailRepositoryMon extends JpaRepository<JobDetail, JobDetailId> {

	// ExecDate = ,AND JobCode =
	public Slice<JobDetail> findAllByExecDateIsAndJobCodeIsOrderByExecDateAscJobCodeAscStepIdAsc(int execDate_0, String jobCode_1, Pageable pageable);

	// ExecDate =
	public Slice<JobDetail> findAllByExecDateIsOrderByExecDateAscJobCodeAscStepIdAsc(int execDate_0, Pageable pageable);

	// ExecDate >=
	public Optional<JobDetail> findTopByExecDateGreaterThanEqualOrderByExecDateDesc(int execDate_0);

	// ExecDate >= ,AND ExecDate <=
	public Slice<JobDetail> findAllByExecDateGreaterThanEqualAndExecDateLessThanEqualOrderByStepStartTimeDesc(int execDate_0, int execDate_1, Pageable pageable);

	// ExecDate >= ,AND ExecDate <= ,AND Status =
	public Slice<JobDetail> findAllByExecDateGreaterThanEqualAndExecDateLessThanEqualAndStatusIsOrderByStepStartTimeDesc(int execDate_0, int execDate_1, String status_2, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JobDetail> findByJobDetailId(JobDetailId jobDetailId);

}
