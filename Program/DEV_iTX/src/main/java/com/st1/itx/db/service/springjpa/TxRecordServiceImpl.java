package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxRecordId;
import com.st1.itx.db.repository.online.TxRecordRepository;
import com.st1.itx.db.repository.day.TxRecordRepositoryDay;
import com.st1.itx.db.repository.mon.TxRecordRepositoryMon;
import com.st1.itx.db.repository.hist.TxRecordRepositoryHist;
import com.st1.itx.db.service.TxRecordService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txRecordService")
@Repository
public class TxRecordServiceImpl extends ASpringJpaParm implements TxRecordService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxRecordRepository txRecordRepos;

  @Autowired
  private TxRecordRepositoryDay txRecordReposDay;

  @Autowired
  private TxRecordRepositoryMon txRecordReposMon;

  @Autowired
  private TxRecordRepositoryHist txRecordReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txRecordRepos);
    org.junit.Assert.assertNotNull(txRecordReposDay);
    org.junit.Assert.assertNotNull(txRecordReposMon);
    org.junit.Assert.assertNotNull(txRecordReposHist);
  }

  @Override
  public TxRecord findById(TxRecordId txRecordId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + txRecordId);
    Optional<TxRecord> txRecord = null;
    if (dbName.equals(ContentName.onDay))
      txRecord = txRecordReposDay.findById(txRecordId);
    else if (dbName.equals(ContentName.onMon))
      txRecord = txRecordReposMon.findById(txRecordId);
    else if (dbName.equals(ContentName.onHist))
      txRecord = txRecordReposHist.findById(txRecordId);
    else 
      txRecord = txRecordRepos.findById(txRecordId);
    TxRecord obj = txRecord.isPresent() ? txRecord.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxRecord> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Entdy", "TxNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Entdy", "TxNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txRecordReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txRecordReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txRecordReposHist.findAll(pageable);
    else 
      slice = txRecordRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxRecord> findByBrNo(int entdy_0, String brNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBrNo " + dbName + " : " + "entdy_0 : " + entdy_0 + " brNo_1 : " +  brNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = txRecordReposDay.findAllByEntdyIsAndBrNoIsOrderByCreateDateAsc(entdy_0, brNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txRecordReposMon.findAllByEntdyIsAndBrNoIsOrderByCreateDateAsc(entdy_0, brNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txRecordReposHist.findAllByEntdyIsAndBrNoIsOrderByCreateDateAsc(entdy_0, brNo_1, pageable);
    else 
      slice = txRecordRepos.findAllByEntdyIsAndBrNoIsOrderByCreateDateAsc(entdy_0, brNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxRecord> findByLC001(int entdy_0, String brNo_1, String txResult_2, int canCancel_3, int actionFg_4, int hcode_5, String tlrNo_6, String tranNo_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByLC001 " + dbName + " : " + "entdy_0 : " + entdy_0 + " brNo_1 : " +  brNo_1 + " txResult_2 : " +  txResult_2 + " canCancel_3 : " +  canCancel_3 + " actionFg_4 : " +  actionFg_4 + " hcode_5 : " +  hcode_5 + " tlrNo_6 : " +  tlrNo_6 + " tranNo_7 : " +  tranNo_7);
    if (dbName.equals(ContentName.onDay))
      slice = txRecordReposDay.findAllByEntdyIsAndBrNoIsAndTxResultIsAndCanCancelIsAndActionFgIsAndHcodeNotAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, brNo_1, txResult_2, canCancel_3, actionFg_4, hcode_5, tlrNo_6, tranNo_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txRecordReposMon.findAllByEntdyIsAndBrNoIsAndTxResultIsAndCanCancelIsAndActionFgIsAndHcodeNotAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, brNo_1, txResult_2, canCancel_3, actionFg_4, hcode_5, tlrNo_6, tranNo_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txRecordReposHist.findAllByEntdyIsAndBrNoIsAndTxResultIsAndCanCancelIsAndActionFgIsAndHcodeNotAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, brNo_1, txResult_2, canCancel_3, actionFg_4, hcode_5, tlrNo_6, tranNo_7, pageable);
    else 
      slice = txRecordRepos.findAllByEntdyIsAndBrNoIsAndTxResultIsAndCanCancelIsAndActionFgIsAndHcodeNotAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, brNo_1, txResult_2, canCancel_3, actionFg_4, hcode_5, tlrNo_6, tranNo_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxRecord> findByLC002(int entdy_0, String brNo_1, String txResult_2, int canModify_3, int actionFg_4, int hcode_5, String tlrNo_6, String tranNo_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByLC002 " + dbName + " : " + "entdy_0 : " + entdy_0 + " brNo_1 : " +  brNo_1 + " txResult_2 : " +  txResult_2 + " canModify_3 : " +  canModify_3 + " actionFg_4 : " +  actionFg_4 + " hcode_5 : " +  hcode_5 + " tlrNo_6 : " +  tlrNo_6 + " tranNo_7 : " +  tranNo_7);
    if (dbName.equals(ContentName.onDay))
      slice = txRecordReposDay.findAllByEntdyIsAndBrNoIsAndTxResultIsAndCanModifyIsAndActionFgIsAndHcodeNotAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, brNo_1, txResult_2, canModify_3, actionFg_4, hcode_5, tlrNo_6, tranNo_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txRecordReposMon.findAllByEntdyIsAndBrNoIsAndTxResultIsAndCanModifyIsAndActionFgIsAndHcodeNotAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, brNo_1, txResult_2, canModify_3, actionFg_4, hcode_5, tlrNo_6, tranNo_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txRecordReposHist.findAllByEntdyIsAndBrNoIsAndTxResultIsAndCanModifyIsAndActionFgIsAndHcodeNotAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, brNo_1, txResult_2, canModify_3, actionFg_4, hcode_5, tlrNo_6, tranNo_7, pageable);
    else 
      slice = txRecordRepos.findAllByEntdyIsAndBrNoIsAndTxResultIsAndCanModifyIsAndActionFgIsAndHcodeNotAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, brNo_1, txResult_2, canModify_3, actionFg_4, hcode_5, tlrNo_6, tranNo_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxRecord> findByLC011(int entdy_0, int entdy_1, String brNo_2, String txResult_3, int actionFg_4, String tlrNo_5, String tranNo_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByLC011 " + dbName + " : " + "entdy_0 : " + entdy_0 + " entdy_1 : " +  entdy_1 + " brNo_2 : " +  brNo_2 + " txResult_3 : " +  txResult_3 + " actionFg_4 : " +  actionFg_4 + " tlrNo_5 : " +  tlrNo_5 + " tranNo_6 : " +  tranNo_6);
    if (dbName.equals(ContentName.onDay))
      slice = txRecordReposDay.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndActionFgIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, entdy_1, brNo_2, txResult_3, actionFg_4, tlrNo_5, tranNo_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txRecordReposMon.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndActionFgIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, entdy_1, brNo_2, txResult_3, actionFg_4, tlrNo_5, tranNo_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txRecordReposHist.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndActionFgIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, entdy_1, brNo_2, txResult_3, actionFg_4, tlrNo_5, tranNo_6, pageable);
    else 
      slice = txRecordRepos.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndActionFgIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, entdy_1, brNo_2, txResult_3, actionFg_4, tlrNo_5, tranNo_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxRecord> findByLC011All(int entdy_0, int entdy_1, String brNo_2, String txResult_3, String tlrNo_4, String tranNo_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByLC011All " + dbName + " : " + "entdy_0 : " + entdy_0 + " entdy_1 : " +  entdy_1 + " brNo_2 : " +  brNo_2 + " txResult_3 : " +  txResult_3 + " tlrNo_4 : " +  tlrNo_4 + " tranNo_5 : " +  tranNo_5);
    if (dbName.equals(ContentName.onDay))
      slice = txRecordReposDay.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, entdy_1, brNo_2, txResult_3, tlrNo_4, tranNo_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txRecordReposMon.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, entdy_1, brNo_2, txResult_3, tlrNo_4, tranNo_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txRecordReposHist.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, entdy_1, brNo_2, txResult_3, tlrNo_4, tranNo_5, pageable);
    else 
      slice = txRecordRepos.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, entdy_1, brNo_2, txResult_3, tlrNo_4, tranNo_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxRecord> findByLC011Hcode(int entdy_0, int entdy_1, String brNo_2, String txResult_3, int hcode_4, String tlrNo_5, String tranNo_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByLC011Hcode " + dbName + " : " + "entdy_0 : " + entdy_0 + " entdy_1 : " +  entdy_1 + " brNo_2 : " +  brNo_2 + " txResult_3 : " +  txResult_3 + " hcode_4 : " +  hcode_4 + " tlrNo_5 : " +  tlrNo_5 + " tranNo_6 : " +  tranNo_6);
    if (dbName.equals(ContentName.onDay))
      slice = txRecordReposDay.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndHcodeIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, entdy_1, brNo_2, txResult_3, hcode_4, tlrNo_5, tranNo_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txRecordReposMon.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndHcodeIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, entdy_1, brNo_2, txResult_3, hcode_4, tlrNo_5, tranNo_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txRecordReposHist.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndHcodeIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, entdy_1, brNo_2, txResult_3, hcode_4, tlrNo_5, tranNo_6, pageable);
    else 
      slice = txRecordRepos.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndHcodeIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(entdy_0, entdy_1, brNo_2, txResult_3, hcode_4, tlrNo_5, tranNo_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxRecord> findByL3005(String mrKey_0, List<String> tranNo_1, int entdy_2, int entdy_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByL3005 " + dbName + " : " + "mrKey_0 : " + mrKey_0 + " tranNo_1 : " +  tranNo_1 + " entdy_2 : " +  entdy_2 + " entdy_3 : " +  entdy_3);
    if (dbName.equals(ContentName.onDay))
      slice = txRecordReposDay.findAllByMrKeyLikeAndTranNoInAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(mrKey_0, tranNo_1, entdy_2, entdy_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txRecordReposMon.findAllByMrKeyLikeAndTranNoInAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(mrKey_0, tranNo_1, entdy_2, entdy_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txRecordReposHist.findAllByMrKeyLikeAndTranNoInAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(mrKey_0, tranNo_1, entdy_2, entdy_3, pageable);
    else 
      slice = txRecordRepos.findAllByMrKeyLikeAndTranNoInAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(mrKey_0, tranNo_1, entdy_2, entdy_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxRecord holdById(TxRecordId txRecordId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txRecordId);
    Optional<TxRecord> txRecord = null;
    if (dbName.equals(ContentName.onDay))
      txRecord = txRecordReposDay.findByTxRecordId(txRecordId);
    else if (dbName.equals(ContentName.onMon))
      txRecord = txRecordReposMon.findByTxRecordId(txRecordId);
    else if (dbName.equals(ContentName.onHist))
      txRecord = txRecordReposHist.findByTxRecordId(txRecordId);
    else 
      txRecord = txRecordRepos.findByTxRecordId(txRecordId);
    return txRecord.isPresent() ? txRecord.get() : null;
  }

  @Override
  public TxRecord holdById(TxRecord txRecord, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txRecord.getTxRecordId());
    Optional<TxRecord> txRecordT = null;
    if (dbName.equals(ContentName.onDay))
      txRecordT = txRecordReposDay.findByTxRecordId(txRecord.getTxRecordId());
    else if (dbName.equals(ContentName.onMon))
      txRecordT = txRecordReposMon.findByTxRecordId(txRecord.getTxRecordId());
    else if (dbName.equals(ContentName.onHist))
      txRecordT = txRecordReposHist.findByTxRecordId(txRecord.getTxRecordId());
    else 
      txRecordT = txRecordRepos.findByTxRecordId(txRecord.getTxRecordId());
    return txRecordT.isPresent() ? txRecordT.get() : null;
  }

  @Override
  public TxRecord insert(TxRecord txRecord, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + txRecord.getTxRecordId());
    if (this.findById(txRecord.getTxRecordId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txRecord.setCreateEmpNo(empNot);

    if(txRecord.getLastUpdateEmpNo() == null || txRecord.getLastUpdateEmpNo().isEmpty())
      txRecord.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txRecordReposDay.saveAndFlush(txRecord);	
    else if (dbName.equals(ContentName.onMon))
      return txRecordReposMon.saveAndFlush(txRecord);
    else if (dbName.equals(ContentName.onHist))
      return txRecordReposHist.saveAndFlush(txRecord);
    else 
    return txRecordRepos.saveAndFlush(txRecord);
  }

  @Override
  public TxRecord update(TxRecord txRecord, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + txRecord.getTxRecordId());
    if (!empNot.isEmpty())
      txRecord.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txRecordReposDay.saveAndFlush(txRecord);	
    else if (dbName.equals(ContentName.onMon))
      return txRecordReposMon.saveAndFlush(txRecord);
    else if (dbName.equals(ContentName.onHist))
      return txRecordReposHist.saveAndFlush(txRecord);
    else 
    return txRecordRepos.saveAndFlush(txRecord);
  }

  @Override
  public TxRecord update2(TxRecord txRecord, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + txRecord.getTxRecordId());
    if (!empNot.isEmpty())
      txRecord.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txRecordReposDay.saveAndFlush(txRecord);	
    else if (dbName.equals(ContentName.onMon))
      txRecordReposMon.saveAndFlush(txRecord);
    else if (dbName.equals(ContentName.onHist))
        txRecordReposHist.saveAndFlush(txRecord);
    else 
      txRecordRepos.saveAndFlush(txRecord);	
    return this.findById(txRecord.getTxRecordId());
  }

  @Override
  public void delete(TxRecord txRecord, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txRecord.getTxRecordId());
    if (dbName.equals(ContentName.onDay)) {
      txRecordReposDay.delete(txRecord);	
      txRecordReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txRecordReposMon.delete(txRecord);	
      txRecordReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txRecordReposHist.delete(txRecord);
      txRecordReposHist.flush();
    }
    else {
      txRecordRepos.delete(txRecord);
      txRecordRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxRecord> txRecord, TitaVo... titaVo) throws DBException {
    if (txRecord == null || txRecord.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (TxRecord t : txRecord){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txRecord = txRecordReposDay.saveAll(txRecord);	
      txRecordReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txRecord = txRecordReposMon.saveAll(txRecord);	
      txRecordReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txRecord = txRecordReposHist.saveAll(txRecord);
      txRecordReposHist.flush();
    }
    else {
      txRecord = txRecordRepos.saveAll(txRecord);
      txRecordRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxRecord> txRecord, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (txRecord == null || txRecord.size() == 0)
      throw new DBException(6);

    for (TxRecord t : txRecord) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txRecord = txRecordReposDay.saveAll(txRecord);	
      txRecordReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txRecord = txRecordReposMon.saveAll(txRecord);	
      txRecordReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txRecord = txRecordReposHist.saveAll(txRecord);
      txRecordReposHist.flush();
    }
    else {
      txRecord = txRecordRepos.saveAll(txRecord);
      txRecordRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxRecord> txRecord, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txRecord == null || txRecord.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txRecordReposDay.deleteAll(txRecord);	
      txRecordReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txRecordReposMon.deleteAll(txRecord);	
      txRecordReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txRecordReposHist.deleteAll(txRecord);
      txRecordReposHist.flush();
    }
    else {
      txRecordRepos.deleteAll(txRecord);
      txRecordRepos.flush();
    }
  }

}
