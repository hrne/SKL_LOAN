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
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.domain.TxDataLogId;
import com.st1.itx.db.repository.online.TxDataLogRepository;
import com.st1.itx.db.repository.day.TxDataLogRepositoryDay;
import com.st1.itx.db.repository.mon.TxDataLogRepositoryMon;
import com.st1.itx.db.repository.hist.TxDataLogRepositoryHist;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txDataLogService")
@Repository
public class TxDataLogServiceImpl extends ASpringJpaParm implements TxDataLogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxDataLogRepository txDataLogRepos;

	@Autowired
	private TxDataLogRepositoryDay txDataLogReposDay;

	@Autowired
	private TxDataLogRepositoryMon txDataLogReposMon;

	@Autowired
	private TxDataLogRepositoryHist txDataLogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txDataLogRepos);
		org.junit.Assert.assertNotNull(txDataLogReposDay);
		org.junit.Assert.assertNotNull(txDataLogReposMon);
		org.junit.Assert.assertNotNull(txDataLogReposHist);
	}

	@Override
	public TxDataLog findById(TxDataLogId txDataLogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + txDataLogId);
		Optional<TxDataLog> txDataLog = null;
		if (dbName.equals(ContentName.onDay))
			txDataLog = txDataLogReposDay.findById(txDataLogId);
		else if (dbName.equals(ContentName.onMon))
			txDataLog = txDataLogReposMon.findById(txDataLogId);
		else if (dbName.equals(ContentName.onHist))
			txDataLog = txDataLogReposHist.findById(txDataLogId);
		else
			txDataLog = txDataLogRepos.findById(txDataLogId);
		TxDataLog obj = txDataLog.isPresent() ? txDataLog.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxDataLog> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxDataLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "TxDate", "TxSeq", "TxSno"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "TxDate", "TxSeq", "TxSno"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txDataLogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txDataLogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txDataLogReposHist.findAll(pageable);
		else
			slice = txDataLogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxDataLog> findByCustNo0(int txDate_0, int txDate_1, String tranNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxDataLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByCustNo0 " + dbName + " : " + "txDate_0 : " + txDate_0 + " txDate_1 : " + txDate_1 + " tranNo_2 : " + tranNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = txDataLogReposDay.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txDataLogReposMon.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txDataLogReposHist.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, pageable);
		else
			slice = txDataLogRepos.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxDataLog> findByCustNo1(int txDate_0, int txDate_1, String tranNo_2, int custNo_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxDataLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByCustNo1 " + dbName + " : " + "txDate_0 : " + txDate_0 + " txDate_1 : " + txDate_1 + " tranNo_2 : " + tranNo_2 + " custNo_3 : " + custNo_3);
		if (dbName.equals(ContentName.onDay))
			slice = txDataLogReposDay.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, custNo_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txDataLogReposMon.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, custNo_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txDataLogReposHist.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, custNo_3, pageable);
		else
			slice = txDataLogRepos.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, custNo_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxDataLog> findByCustNo2(int txDate_0, int txDate_1, String tranNo_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxDataLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByCustNo2 " + dbName + " : " + "txDate_0 : " + txDate_0 + " txDate_1 : " + txDate_1 + " tranNo_2 : " + tranNo_2 + " custNo_3 : " + custNo_3 + " facmNo_4 : " + facmNo_4);
		if (dbName.equals(ContentName.onDay))
			slice = txDataLogReposDay.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsAndFacmNoIsOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, custNo_3, facmNo_4,
					pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txDataLogReposMon.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsAndFacmNoIsOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, custNo_3, facmNo_4,
					pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txDataLogReposHist.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsAndFacmNoIsOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, custNo_3, facmNo_4,
					pageable);
		else
			slice = txDataLogRepos.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsAndFacmNoIsOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, custNo_3, facmNo_4,
					pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxDataLog> findByCustNo3(int txDate_0, int txDate_1, String tranNo_2, int custNo_3, int facmNo_4, int bormNo_5, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxDataLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByCustNo3 " + dbName + " : " + "txDate_0 : " + txDate_0 + " txDate_1 : " + txDate_1 + " tranNo_2 : " + tranNo_2 + " custNo_3 : " + custNo_3 + " facmNo_4 : " + facmNo_4
				+ " bormNo_5 : " + bormNo_5);
		if (dbName.equals(ContentName.onDay))
			slice = txDataLogReposDay.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsAndFacmNoIsAndBormNoIsOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, custNo_3,
					facmNo_4, bormNo_5, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txDataLogReposMon.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsAndFacmNoIsAndBormNoIsOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, custNo_3,
					facmNo_4, bormNo_5, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txDataLogReposHist.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsAndFacmNoIsAndBormNoIsOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, custNo_3,
					facmNo_4, bormNo_5, pageable);
		else
			slice = txDataLogRepos.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsAndFacmNoIsAndBormNoIsOrderByCreateDateAsc(txDate_0, txDate_1, tranNo_2, custNo_3,
					facmNo_4, bormNo_5, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxDataLog> findTxSeq(int txDate_0, int txDate_1, String txSeq_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxDataLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findTxSeq " + dbName + " : " + "txDate_0 : " + txDate_0 + " txDate_1 : " + txDate_1 + " txSeq_2 : " + txSeq_2);
		if (dbName.equals(ContentName.onDay))
			slice = txDataLogReposDay.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTxSeqLikeOrderByCustNoAsc(txDate_0, txDate_1, txSeq_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txDataLogReposMon.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTxSeqLikeOrderByCustNoAsc(txDate_0, txDate_1, txSeq_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txDataLogReposHist.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTxSeqLikeOrderByCustNoAsc(txDate_0, txDate_1, txSeq_2, pageable);
		else
			slice = txDataLogRepos.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTxSeqLikeOrderByCustNoAsc(txDate_0, txDate_1, txSeq_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxDataLog> findByCustNo4(int txDate_0, int txDate_1, List<String> tranNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxDataLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByCustNo4 " + dbName + " : " + "txDate_0 : " + txDate_0 + " txDate_1 : " + txDate_1 + " tranNo_2 : " + tranNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = txDataLogReposDay.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txDataLogReposMon.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txDataLogReposHist.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, pageable);
		else
			slice = txDataLogRepos.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxDataLog> findByCustNo5(int txDate_0, int txDate_1, List<String> tranNo_2, int custNo_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxDataLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByCustNo5 " + dbName + " : " + "txDate_0 : " + txDate_0 + " txDate_1 : " + txDate_1 + " tranNo_2 : " + tranNo_2 + " custNo_3 : " + custNo_3);
		if (dbName.equals(ContentName.onDay))
			slice = txDataLogReposDay.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, custNo_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txDataLogReposMon.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, custNo_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txDataLogReposHist.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, custNo_3, pageable);
		else
			slice = txDataLogRepos.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, custNo_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxDataLog> findByCustNo6(int txDate_0, int txDate_1, List<String> tranNo_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxDataLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByCustNo6 " + dbName + " : " + "txDate_0 : " + txDate_0 + " txDate_1 : " + txDate_1 + " tranNo_2 : " + tranNo_2 + " custNo_3 : " + custNo_3 + " facmNo_4 : " + facmNo_4);
		if (dbName.equals(ContentName.onDay))
			slice = txDataLogReposDay.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsAndFacmNoIsOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, custNo_3, facmNo_4,
					pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txDataLogReposMon.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsAndFacmNoIsOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, custNo_3, facmNo_4,
					pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txDataLogReposHist.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsAndFacmNoIsOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, custNo_3, facmNo_4,
					pageable);
		else
			slice = txDataLogRepos.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsAndFacmNoIsOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, custNo_3, facmNo_4,
					pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxDataLog> findByCustNo7(int txDate_0, int txDate_1, List<String> tranNo_2, int custNo_3, int facmNo_4, int bormNo_5, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxDataLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByCustNo7 " + dbName + " : " + "txDate_0 : " + txDate_0 + " txDate_1 : " + txDate_1 + " tranNo_2 : " + tranNo_2 + " custNo_3 : " + custNo_3 + " facmNo_4 : " + facmNo_4
				+ " bormNo_5 : " + bormNo_5);
		if (dbName.equals(ContentName.onDay))
			slice = txDataLogReposDay.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsAndFacmNoIsAndBormNoIsOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, custNo_3,
					facmNo_4, bormNo_5, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txDataLogReposMon.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsAndFacmNoIsAndBormNoIsOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, custNo_3,
					facmNo_4, bormNo_5, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txDataLogReposHist.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsAndFacmNoIsAndBormNoIsOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, custNo_3,
					facmNo_4, bormNo_5, pageable);
		else
			slice = txDataLogRepos.findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsAndFacmNoIsAndBormNoIsOrderByCreateDateDesc(txDate_0, txDate_1, tranNo_2, custNo_3,
					facmNo_4, bormNo_5, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxDataLog> findByTranNo(String tranNo_0, String mrKey_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxDataLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByTranNo " + dbName + " : " + "tranNo_0 : " + tranNo_0 + " mrKey_1 : " + mrKey_1);
		if (dbName.equals(ContentName.onDay))
			slice = txDataLogReposDay.findAllByTranNoIsAndMrKeyIsOrderByCreateDateDesc(tranNo_0, mrKey_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txDataLogReposMon.findAllByTranNoIsAndMrKeyIsOrderByCreateDateDesc(tranNo_0, mrKey_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txDataLogReposHist.findAllByTranNoIsAndMrKeyIsOrderByCreateDateDesc(tranNo_0, mrKey_1, pageable);
		else
			slice = txDataLogRepos.findAllByTranNoIsAndMrKeyIsOrderByCreateDateDesc(tranNo_0, mrKey_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxDataLog holdById(TxDataLogId txDataLogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txDataLogId);
		Optional<TxDataLog> txDataLog = null;
		if (dbName.equals(ContentName.onDay))
			txDataLog = txDataLogReposDay.findByTxDataLogId(txDataLogId);
		else if (dbName.equals(ContentName.onMon))
			txDataLog = txDataLogReposMon.findByTxDataLogId(txDataLogId);
		else if (dbName.equals(ContentName.onHist))
			txDataLog = txDataLogReposHist.findByTxDataLogId(txDataLogId);
		else
			txDataLog = txDataLogRepos.findByTxDataLogId(txDataLogId);
		return txDataLog.isPresent() ? txDataLog.get() : null;
	}

	@Override
	public TxDataLog holdById(TxDataLog txDataLog, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txDataLog.getTxDataLogId());
		Optional<TxDataLog> txDataLogT = null;
		if (dbName.equals(ContentName.onDay))
			txDataLogT = txDataLogReposDay.findByTxDataLogId(txDataLog.getTxDataLogId());
		else if (dbName.equals(ContentName.onMon))
			txDataLogT = txDataLogReposMon.findByTxDataLogId(txDataLog.getTxDataLogId());
		else if (dbName.equals(ContentName.onHist))
			txDataLogT = txDataLogReposHist.findByTxDataLogId(txDataLog.getTxDataLogId());
		else
			txDataLogT = txDataLogRepos.findByTxDataLogId(txDataLog.getTxDataLogId());
		return txDataLogT.isPresent() ? txDataLogT.get() : null;
	}

	@Override
	public TxDataLog insert(TxDataLog txDataLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + txDataLog.getTxDataLogId());
		if (this.findById(txDataLog.getTxDataLogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txDataLog.setCreateEmpNo(empNot);

		if (txDataLog.getLastUpdateEmpNo() == null || txDataLog.getLastUpdateEmpNo().isEmpty())
			txDataLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txDataLogReposDay.saveAndFlush(txDataLog);
		else if (dbName.equals(ContentName.onMon))
			return txDataLogReposMon.saveAndFlush(txDataLog);
		else if (dbName.equals(ContentName.onHist))
			return txDataLogReposHist.saveAndFlush(txDataLog);
		else
			return txDataLogRepos.saveAndFlush(txDataLog);
	}

	@Override
	public TxDataLog update(TxDataLog txDataLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + txDataLog.getTxDataLogId());
		if (!empNot.isEmpty())
			txDataLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txDataLogReposDay.saveAndFlush(txDataLog);
		else if (dbName.equals(ContentName.onMon))
			return txDataLogReposMon.saveAndFlush(txDataLog);
		else if (dbName.equals(ContentName.onHist))
			return txDataLogReposHist.saveAndFlush(txDataLog);
		else
			return txDataLogRepos.saveAndFlush(txDataLog);
	}

	@Override
	public TxDataLog update2(TxDataLog txDataLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + txDataLog.getTxDataLogId());
		if (!empNot.isEmpty())
			txDataLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txDataLogReposDay.saveAndFlush(txDataLog);
		else if (dbName.equals(ContentName.onMon))
			txDataLogReposMon.saveAndFlush(txDataLog);
		else if (dbName.equals(ContentName.onHist))
			txDataLogReposHist.saveAndFlush(txDataLog);
		else
			txDataLogRepos.saveAndFlush(txDataLog);
		return this.findById(txDataLog.getTxDataLogId());
	}

	@Override
	public void delete(TxDataLog txDataLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txDataLog.getTxDataLogId());
		if (dbName.equals(ContentName.onDay)) {
			txDataLogReposDay.delete(txDataLog);
			txDataLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txDataLogReposMon.delete(txDataLog);
			txDataLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txDataLogReposHist.delete(txDataLog);
			txDataLogReposHist.flush();
		} else {
			txDataLogRepos.delete(txDataLog);
			txDataLogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxDataLog> txDataLog, TitaVo... titaVo) throws DBException {
		if (txDataLog == null || txDataLog.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("InsertAll...");
		for (TxDataLog t : txDataLog) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			txDataLog = txDataLogReposDay.saveAll(txDataLog);
			txDataLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txDataLog = txDataLogReposMon.saveAll(txDataLog);
			txDataLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txDataLog = txDataLogReposHist.saveAll(txDataLog);
			txDataLogReposHist.flush();
		} else {
			txDataLog = txDataLogRepos.saveAll(txDataLog);
			txDataLogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxDataLog> txDataLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (txDataLog == null || txDataLog.size() == 0)
			throw new DBException(6);

		for (TxDataLog t : txDataLog)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txDataLog = txDataLogReposDay.saveAll(txDataLog);
			txDataLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txDataLog = txDataLogReposMon.saveAll(txDataLog);
			txDataLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txDataLog = txDataLogReposHist.saveAll(txDataLog);
			txDataLogReposHist.flush();
		} else {
			txDataLog = txDataLogRepos.saveAll(txDataLog);
			txDataLogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxDataLog> txDataLog, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txDataLog == null || txDataLog.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txDataLogReposDay.deleteAll(txDataLog);
			txDataLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txDataLogReposMon.deleteAll(txDataLog);
			txDataLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txDataLogReposHist.deleteAll(txDataLog);
			txDataLogReposHist.flush();
		} else {
			txDataLogRepos.deleteAll(txDataLog);
			txDataLogRepos.flush();
		}
	}

}
