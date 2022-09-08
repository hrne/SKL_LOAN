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
import com.st1.itx.db.domain.MlaundryRecord;
import com.st1.itx.db.repository.online.MlaundryRecordRepository;
import com.st1.itx.db.repository.day.MlaundryRecordRepositoryDay;
import com.st1.itx.db.repository.mon.MlaundryRecordRepositoryMon;
import com.st1.itx.db.repository.hist.MlaundryRecordRepositoryHist;
import com.st1.itx.db.service.MlaundryRecordService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("mlaundryRecordService")
@Repository
public class MlaundryRecordServiceImpl extends ASpringJpaParm implements MlaundryRecordService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MlaundryRecordRepository mlaundryRecordRepos;

	@Autowired
	private MlaundryRecordRepositoryDay mlaundryRecordReposDay;

	@Autowired
	private MlaundryRecordRepositoryMon mlaundryRecordReposMon;

	@Autowired
	private MlaundryRecordRepositoryHist mlaundryRecordReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(mlaundryRecordRepos);
		org.junit.Assert.assertNotNull(mlaundryRecordReposDay);
		org.junit.Assert.assertNotNull(mlaundryRecordReposMon);
		org.junit.Assert.assertNotNull(mlaundryRecordReposHist);
	}

	@Override
	public MlaundryRecord findById(Long logNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + logNo);
		Optional<MlaundryRecord> mlaundryRecord = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryRecord = mlaundryRecordReposDay.findById(logNo);
		else if (dbName.equals(ContentName.onMon))
			mlaundryRecord = mlaundryRecordReposMon.findById(logNo);
		else if (dbName.equals(ContentName.onHist))
			mlaundryRecord = mlaundryRecordReposHist.findById(logNo);
		else
			mlaundryRecord = mlaundryRecordRepos.findById(logNo);
		MlaundryRecord obj = mlaundryRecord.isPresent() ? mlaundryRecord.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MlaundryRecord> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MlaundryRecord> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = mlaundryRecordReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = mlaundryRecordReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = mlaundryRecordReposHist.findAll(pageable);
		else
			slice = mlaundryRecordRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<MlaundryRecord> findRecordDate(int recordDate_0, int recordDate_1, int actualRepayDate_2, int actualRepayDate_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MlaundryRecord> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findRecordDate " + dbName + " : " + "recordDate_0 : " + recordDate_0 + " recordDate_1 : " + recordDate_1 + " actualRepayDate_2 : " + actualRepayDate_2 + " actualRepayDate_3 : "
				+ actualRepayDate_3);
		if (dbName.equals(ContentName.onDay))
			slice = mlaundryRecordReposDay
					.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(recordDate_0,
							recordDate_1, actualRepayDate_2, actualRepayDate_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = mlaundryRecordReposMon
					.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(recordDate_0,
							recordDate_1, actualRepayDate_2, actualRepayDate_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = mlaundryRecordReposHist
					.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(recordDate_0,
							recordDate_1, actualRepayDate_2, actualRepayDate_3, pageable);
		else
			slice = mlaundryRecordRepos
					.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(recordDate_0,
							recordDate_1, actualRepayDate_2, actualRepayDate_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<MlaundryRecord> findRecordD(int recordDate_0, int recordDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MlaundryRecord> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findRecordD " + dbName + " : " + "recordDate_0 : " + recordDate_0 + " recordDate_1 : " + recordDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = mlaundryRecordReposDay.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(recordDate_0, recordDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = mlaundryRecordReposMon.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(recordDate_0, recordDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = mlaundryRecordReposHist.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(recordDate_0, recordDate_1, pageable);
		else
			slice = mlaundryRecordRepos.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(recordDate_0, recordDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<MlaundryRecord> findRepayD(int actualRepayDate_0, int actualRepayDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MlaundryRecord> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findRepayD " + dbName + " : " + "actualRepayDate_0 : " + actualRepayDate_0 + " actualRepayDate_1 : " + actualRepayDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = mlaundryRecordReposDay.findAllByActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(actualRepayDate_0, actualRepayDate_1,
					pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = mlaundryRecordReposMon.findAllByActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(actualRepayDate_0, actualRepayDate_1,
					pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = mlaundryRecordReposHist.findAllByActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(actualRepayDate_0, actualRepayDate_1,
					pageable);
		else
			slice = mlaundryRecordRepos.findAllByActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(actualRepayDate_0, actualRepayDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<MlaundryRecord> findCustNoEq(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int repayDate_5, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MlaundryRecord> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCustNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " facmNo_2 : " + facmNo_2 + " bormNo_3 : " + bormNo_3 + " bormNo_4 : " + bormNo_4
				+ " repayDate_5 : " + repayDate_5);
		if (dbName.equals(ContentName.onDay))
			slice = mlaundryRecordReposDay
					.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndRepayDateGreaterThanEqualOrderByRepayDateAsc(custNo_0, facmNo_1,
							facmNo_2, bormNo_3, bormNo_4, repayDate_5, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = mlaundryRecordReposMon
					.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndRepayDateGreaterThanEqualOrderByRepayDateAsc(custNo_0, facmNo_1,
							facmNo_2, bormNo_3, bormNo_4, repayDate_5, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = mlaundryRecordReposHist
					.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndRepayDateGreaterThanEqualOrderByRepayDateAsc(custNo_0, facmNo_1,
							facmNo_2, bormNo_3, bormNo_4, repayDate_5, pageable);
		else
			slice = mlaundryRecordRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndRepayDateGreaterThanEqualOrderByRepayDateAsc(
					custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, repayDate_5, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MlaundryRecord findCustNoFirst(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int repayDate_5, int repayDate_6, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findCustNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " facmNo_2 : " + facmNo_2 + " bormNo_3 : " + bormNo_3 + " bormNo_4 : " + bormNo_4
				+ " repayDate_5 : " + repayDate_5 + " repayDate_6 : " + repayDate_6);
		Optional<MlaundryRecord> mlaundryRecordT = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryRecordT = mlaundryRecordReposDay
					.findTopByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndRepayDateGreaterThanEqualAndRepayDateLessThanEqualOrderByLogNoDesc(
							custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, repayDate_5, repayDate_6);
		else if (dbName.equals(ContentName.onMon))
			mlaundryRecordT = mlaundryRecordReposMon
					.findTopByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndRepayDateGreaterThanEqualAndRepayDateLessThanEqualOrderByLogNoDesc(
							custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, repayDate_5, repayDate_6);
		else if (dbName.equals(ContentName.onHist))
			mlaundryRecordT = mlaundryRecordReposHist
					.findTopByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndRepayDateGreaterThanEqualAndRepayDateLessThanEqualOrderByLogNoDesc(
							custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, repayDate_5, repayDate_6);
		else
			mlaundryRecordT = mlaundryRecordRepos
					.findTopByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndRepayDateGreaterThanEqualAndRepayDateLessThanEqualOrderByLogNoDesc(
							custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, repayDate_5, repayDate_6);

		return mlaundryRecordT.isPresent() ? mlaundryRecordT.get() : null;
	}

	@Override
	public MlaundryRecord maxLogNoFirst(int custNo_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("maxLogNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0);
		Optional<MlaundryRecord> mlaundryRecordT = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryRecordT = mlaundryRecordReposDay.findTopByCustNoGreaterThanOrderByLogNoDesc(custNo_0);
		else if (dbName.equals(ContentName.onMon))
			mlaundryRecordT = mlaundryRecordReposMon.findTopByCustNoGreaterThanOrderByLogNoDesc(custNo_0);
		else if (dbName.equals(ContentName.onHist))
			mlaundryRecordT = mlaundryRecordReposHist.findTopByCustNoGreaterThanOrderByLogNoDesc(custNo_0);
		else
			mlaundryRecordT = mlaundryRecordRepos.findTopByCustNoGreaterThanOrderByLogNoDesc(custNo_0);

		return mlaundryRecordT.isPresent() ? mlaundryRecordT.get() : null;
	}

	@Override
	public MlaundryRecord findCustNoAndRecordDateFirst(int custNo_0, int recordDate_1, int recordDate_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findCustNoAndRecordDateFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " recordDate_1 : " + recordDate_1 + " recordDate_2 : " + recordDate_2);
		Optional<MlaundryRecord> mlaundryRecordT = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryRecordT = mlaundryRecordReposDay.findTopByCustNoIsAndRecordDateGreaterThanEqualAndRecordDateLessThanEqualOrderByRecordDateAsc(custNo_0, recordDate_1, recordDate_2);
		else if (dbName.equals(ContentName.onMon))
			mlaundryRecordT = mlaundryRecordReposMon.findTopByCustNoIsAndRecordDateGreaterThanEqualAndRecordDateLessThanEqualOrderByRecordDateAsc(custNo_0, recordDate_1, recordDate_2);
		else if (dbName.equals(ContentName.onHist))
			mlaundryRecordT = mlaundryRecordReposHist.findTopByCustNoIsAndRecordDateGreaterThanEqualAndRecordDateLessThanEqualOrderByRecordDateAsc(custNo_0, recordDate_1, recordDate_2);
		else
			mlaundryRecordT = mlaundryRecordRepos.findTopByCustNoIsAndRecordDateGreaterThanEqualAndRecordDateLessThanEqualOrderByRecordDateAsc(custNo_0, recordDate_1, recordDate_2);

		return mlaundryRecordT.isPresent() ? mlaundryRecordT.get() : null;
	}

	@Override
	public MlaundryRecord findCustNoAndActualRepayDateFirst(int custNo_0, int actualRepayDate_1, int actualRepayDate_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findCustNoAndActualRepayDateFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " actualRepayDate_1 : " + actualRepayDate_1 + " actualRepayDate_2 : " + actualRepayDate_2);
		Optional<MlaundryRecord> mlaundryRecordT = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryRecordT = mlaundryRecordReposDay.findTopByCustNoIsAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByActualRepayDateDescLogNoDesc(custNo_0, actualRepayDate_1,
					actualRepayDate_2);
		else if (dbName.equals(ContentName.onMon))
			mlaundryRecordT = mlaundryRecordReposMon.findTopByCustNoIsAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByActualRepayDateDescLogNoDesc(custNo_0, actualRepayDate_1,
					actualRepayDate_2);
		else if (dbName.equals(ContentName.onHist))
			mlaundryRecordT = mlaundryRecordReposHist.findTopByCustNoIsAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByActualRepayDateDescLogNoDesc(custNo_0, actualRepayDate_1,
					actualRepayDate_2);
		else
			mlaundryRecordT = mlaundryRecordRepos.findTopByCustNoIsAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByActualRepayDateDescLogNoDesc(custNo_0, actualRepayDate_1,
					actualRepayDate_2);

		return mlaundryRecordT.isPresent() ? mlaundryRecordT.get() : null;
	}

	@Override
	public MlaundryRecord holdById(Long logNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + logNo);
		Optional<MlaundryRecord> mlaundryRecord = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryRecord = mlaundryRecordReposDay.findByLogNo(logNo);
		else if (dbName.equals(ContentName.onMon))
			mlaundryRecord = mlaundryRecordReposMon.findByLogNo(logNo);
		else if (dbName.equals(ContentName.onHist))
			mlaundryRecord = mlaundryRecordReposHist.findByLogNo(logNo);
		else
			mlaundryRecord = mlaundryRecordRepos.findByLogNo(logNo);
		return mlaundryRecord.isPresent() ? mlaundryRecord.get() : null;
	}

	@Override
	public MlaundryRecord holdById(MlaundryRecord mlaundryRecord, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + mlaundryRecord.getLogNo());
		Optional<MlaundryRecord> mlaundryRecordT = null;
		if (dbName.equals(ContentName.onDay))
			mlaundryRecordT = mlaundryRecordReposDay.findByLogNo(mlaundryRecord.getLogNo());
		else if (dbName.equals(ContentName.onMon))
			mlaundryRecordT = mlaundryRecordReposMon.findByLogNo(mlaundryRecord.getLogNo());
		else if (dbName.equals(ContentName.onHist))
			mlaundryRecordT = mlaundryRecordReposHist.findByLogNo(mlaundryRecord.getLogNo());
		else
			mlaundryRecordT = mlaundryRecordRepos.findByLogNo(mlaundryRecord.getLogNo());
		return mlaundryRecordT.isPresent() ? mlaundryRecordT.get() : null;
	}

	@Override
	public MlaundryRecord insert(MlaundryRecord mlaundryRecord, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + mlaundryRecord.getLogNo());
		if (this.findById(mlaundryRecord.getLogNo(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			mlaundryRecord.setCreateEmpNo(empNot);

		if (mlaundryRecord.getLastUpdateEmpNo() == null || mlaundryRecord.getLastUpdateEmpNo().isEmpty())
			mlaundryRecord.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return mlaundryRecordReposDay.saveAndFlush(mlaundryRecord);
		else if (dbName.equals(ContentName.onMon))
			return mlaundryRecordReposMon.saveAndFlush(mlaundryRecord);
		else if (dbName.equals(ContentName.onHist))
			return mlaundryRecordReposHist.saveAndFlush(mlaundryRecord);
		else
			return mlaundryRecordRepos.saveAndFlush(mlaundryRecord);
	}

	@Override
	public MlaundryRecord update(MlaundryRecord mlaundryRecord, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + mlaundryRecord.getLogNo());
		if (!empNot.isEmpty())
			mlaundryRecord.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return mlaundryRecordReposDay.saveAndFlush(mlaundryRecord);
		else if (dbName.equals(ContentName.onMon))
			return mlaundryRecordReposMon.saveAndFlush(mlaundryRecord);
		else if (dbName.equals(ContentName.onHist))
			return mlaundryRecordReposHist.saveAndFlush(mlaundryRecord);
		else
			return mlaundryRecordRepos.saveAndFlush(mlaundryRecord);
	}

	@Override
	public MlaundryRecord update2(MlaundryRecord mlaundryRecord, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + mlaundryRecord.getLogNo());
		if (!empNot.isEmpty())
			mlaundryRecord.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			mlaundryRecordReposDay.saveAndFlush(mlaundryRecord);
		else if (dbName.equals(ContentName.onMon))
			mlaundryRecordReposMon.saveAndFlush(mlaundryRecord);
		else if (dbName.equals(ContentName.onHist))
			mlaundryRecordReposHist.saveAndFlush(mlaundryRecord);
		else
			mlaundryRecordRepos.saveAndFlush(mlaundryRecord);
		return this.findById(mlaundryRecord.getLogNo());
	}

	@Override
	public void delete(MlaundryRecord mlaundryRecord, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + mlaundryRecord.getLogNo());
		if (dbName.equals(ContentName.onDay)) {
			mlaundryRecordReposDay.delete(mlaundryRecord);
			mlaundryRecordReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryRecordReposMon.delete(mlaundryRecord);
			mlaundryRecordReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryRecordReposHist.delete(mlaundryRecord);
			mlaundryRecordReposHist.flush();
		} else {
			mlaundryRecordRepos.delete(mlaundryRecord);
			mlaundryRecordRepos.flush();
		}
	}

	@Override
	public void insertAll(List<MlaundryRecord> mlaundryRecord, TitaVo... titaVo) throws DBException {
		if (mlaundryRecord == null || mlaundryRecord.size() == 0)
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
		for (MlaundryRecord t : mlaundryRecord) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			mlaundryRecord = mlaundryRecordReposDay.saveAll(mlaundryRecord);
			mlaundryRecordReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryRecord = mlaundryRecordReposMon.saveAll(mlaundryRecord);
			mlaundryRecordReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryRecord = mlaundryRecordReposHist.saveAll(mlaundryRecord);
			mlaundryRecordReposHist.flush();
		} else {
			mlaundryRecord = mlaundryRecordRepos.saveAll(mlaundryRecord);
			mlaundryRecordRepos.flush();
		}
	}

	@Override
	public void updateAll(List<MlaundryRecord> mlaundryRecord, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (mlaundryRecord == null || mlaundryRecord.size() == 0)
			throw new DBException(6);

		for (MlaundryRecord t : mlaundryRecord)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			mlaundryRecord = mlaundryRecordReposDay.saveAll(mlaundryRecord);
			mlaundryRecordReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryRecord = mlaundryRecordReposMon.saveAll(mlaundryRecord);
			mlaundryRecordReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryRecord = mlaundryRecordReposHist.saveAll(mlaundryRecord);
			mlaundryRecordReposHist.flush();
		} else {
			mlaundryRecord = mlaundryRecordRepos.saveAll(mlaundryRecord);
			mlaundryRecordRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<MlaundryRecord> mlaundryRecord, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (mlaundryRecord == null || mlaundryRecord.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			mlaundryRecordReposDay.deleteAll(mlaundryRecord);
			mlaundryRecordReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			mlaundryRecordReposMon.deleteAll(mlaundryRecord);
			mlaundryRecordReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			mlaundryRecordReposHist.deleteAll(mlaundryRecord);
			mlaundryRecordReposHist.flush();
		} else {
			mlaundryRecordRepos.deleteAll(mlaundryRecord);
			mlaundryRecordRepos.flush();
		}
	}

}
