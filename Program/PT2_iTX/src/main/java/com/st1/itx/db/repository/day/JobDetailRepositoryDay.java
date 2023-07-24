package com.st1.itx.db.repository.day;


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
public interface JobDetailRepositoryDay extends JpaRepository<JobDetail, JobDetailId> {

  // ExecDate = ,AND JobCode = 
  public Slice<JobDetail> findAllByExecDateIsAndJobCodeIsOrderByExecDateAscJobCodeAscStepIdAsc(int execDate_0, String jobCode_1, Pageable pageable);

  // ExecDate = 
  public Slice<JobDetail> findAllByExecDateIsOrderByExecDateAscJobCodeAscStepIdAsc(int execDate_0, Pageable pageable);

  // ExecDate >=
  public Optional<JobDetail> findTopByExecDateGreaterThanEqualOrderByExecDateDesc(int execDate_0);

  // JobCode = ,AND ExecDate >= ,AND ExecDate <= 
  public Slice<JobDetail> findAllByJobCodeIsAndExecDateGreaterThanEqualAndExecDateLessThanEqualOrderByStepStartTimeDesc(String jobCode_0, int execDate_1, int execDate_2, Pageable pageable);

  // JobCode = ,AND ExecDate >= ,AND ExecDate <= ,AND Status =
  public Slice<JobDetail> findAllByJobCodeIsAndExecDateGreaterThanEqualAndExecDateLessThanEqualAndStatusIsOrderByStepStartTimeDesc(String jobCode_0, int execDate_1, int execDate_2, String status_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JobDetail> findByJobDetailId(JobDetailId jobDetailId);

}

