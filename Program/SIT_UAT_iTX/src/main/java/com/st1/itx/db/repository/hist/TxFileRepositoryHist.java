package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxFile;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxFileRepositoryHist extends JpaRepository<TxFile, Long> {

  // FileDate = ,AND BrNo =
  public Slice<TxFile> findAllByFileDateIsAndBrNoIsOrderByFileNoAsc(int fileDate_0, String brNo_1, Pageable pageable);

  // FileDate >= ,AND FileDate <= ,AND BrNo = ,AND CreateEmpNo % ,AND FileCode % ,AND FileItem %
  public Slice<TxFile> findAllByFileDateGreaterThanEqualAndFileDateLessThanEqualAndBrNoIsAndCreateEmpNoLikeAndFileCodeLikeAndFileItemLikeOrderByCreateDateDesc(int fileDate_0, int fileDate_1, String brNo_2, String createEmpNo_3, String fileCode_4, String fileItem_5, Pageable pageable);

  // FileDate >= ,AND FileDate <= ,AND BrNo = ,AND CreateEmpNo % ,AND FileCode % ,AND FileItem %,AND CreateDate >=,AND CreateDate <  
  public Slice<TxFile> findAllByFileDateGreaterThanEqualAndFileDateLessThanEqualAndBrNoIsAndCreateEmpNoLikeAndFileCodeLikeAndFileItemLikeAndCreateDateGreaterThanEqualAndCreateDateLessThanOrderByCreateDateDesc(int fileDate_0, int fileDate_1, String brNo_2, String createEmpNo_3, String fileCode_4, String fileItem_5, java.sql.Timestamp createDate_6, java.sql.Timestamp createDate_7, Pageable pageable);

  // BatchNo =
  public Optional<TxFile> findTopByBatchNoIsOrderByCreateDateDesc(String batchNo_0);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxFile> findByFileNo(Long fileNo);

}

