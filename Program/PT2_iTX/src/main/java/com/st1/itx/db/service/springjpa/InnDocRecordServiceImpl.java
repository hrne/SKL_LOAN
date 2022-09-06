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
import com.st1.itx.db.domain.InnDocRecord;
import com.st1.itx.db.domain.InnDocRecordId;
import com.st1.itx.db.repository.online.InnDocRecordRepository;
import com.st1.itx.db.repository.day.InnDocRecordRepositoryDay;
import com.st1.itx.db.repository.mon.InnDocRecordRepositoryMon;
import com.st1.itx.db.repository.hist.InnDocRecordRepositoryHist;
import com.st1.itx.db.service.InnDocRecordService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("innDocRecordService")
@Repository
public class InnDocRecordServiceImpl extends ASpringJpaParm implements InnDocRecordService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private InnDocRecordRepository innDocRecordRepos;

	@Autowired
	private InnDocRecordRepositoryDay innDocRecordReposDay;

	@Autowired
	private InnDocRecordRepositoryMon innDocRecordReposMon;

	@Autowired
	private InnDocRecordRepositoryHist innDocRecordReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(innDocRecordRepos);
		org.junit.Assert.assertNotNull(innDocRecordReposDay);
		org.junit.Assert.assertNotNull(innDocRecordReposMon);
		org.junit.Assert.assertNotNull(innDocRecordReposHist);
	}

	@Override
	public InnDocRecord findById(InnDocRecordId innDocRecordId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + innDocRecordId);
		Optional<InnDocRecord> innDocRecord = null;
		if (dbName.equals(ContentName.onDay))
			innDocRecord = innDocRecordReposDay.findById(innDocRecordId);
		else if (dbName.equals(ContentName.onMon))
			innDocRecord = innDocRecordReposMon.findById(innDocRecordId);
		else if (dbName.equals(ContentName.onHist))
			innDocRecord = innDocRecordReposHist.findById(innDocRecordId);
		else
			innDocRecord = innDocRecordRepos.findById(innDocRecordId);
		InnDocRecord obj = innDocRecord.isPresent() ? innDocRecord.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<InnDocRecord> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InnDocRecord> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "ApplSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "ApplSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = innDocRecordReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = innDocRecordReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = innDocRecordReposHist.findAll(pageable);
		else
			slice = innDocRecordRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<InnDocRecord> findL5903ARg(int custNo_0, int applDate_1, int applDate_2, String usageCode_3, String applCode_4, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InnDocRecord> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findL5903ARg " + dbName + " : " + "custNo_0 : " + custNo_0 + " applDate_1 : " + applDate_1 + " applDate_2 : " + applDate_2 + " usageCode_3 : " + usageCode_3 + " applCode_4 : "
				+ applCode_4);
		if (dbName.equals(ContentName.onDay))
			slice = innDocRecordReposDay.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIsAndApplCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0,
					applDate_1, applDate_2, usageCode_3, applCode_4, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = innDocRecordReposMon.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIsAndApplCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0,
					applDate_1, applDate_2, usageCode_3, applCode_4, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = innDocRecordReposHist.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIsAndApplCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0,
					applDate_1, applDate_2, usageCode_3, applCode_4, pageable);
		else
			slice = innDocRecordRepos.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIsAndApplCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0,
					applDate_1, applDate_2, usageCode_3, applCode_4, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<InnDocRecord> findL5903BRg(int custNo_0, int applDate_1, int applDate_2, String applCode_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InnDocRecord> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findL5903BRg " + dbName + " : " + "custNo_0 : " + custNo_0 + " applDate_1 : " + applDate_1 + " applDate_2 : " + applDate_2 + " applCode_3 : " + applCode_3);
		if (dbName.equals(ContentName.onDay))
			slice = innDocRecordReposDay.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndApplCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0, applDate_1,
					applDate_2, applCode_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = innDocRecordReposMon.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndApplCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0, applDate_1,
					applDate_2, applCode_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = innDocRecordReposHist.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndApplCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0, applDate_1,
					applDate_2, applCode_3, pageable);
		else
			slice = innDocRecordRepos.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndApplCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0, applDate_1,
					applDate_2, applCode_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<InnDocRecord> findL5903CRg(int custNo_0, int applDate_1, int applDate_2, String usageCode_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InnDocRecord> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findL5903CRg " + dbName + " : " + "custNo_0 : " + custNo_0 + " applDate_1 : " + applDate_1 + " applDate_2 : " + applDate_2 + " usageCode_3 : " + usageCode_3);
		if (dbName.equals(ContentName.onDay))
			slice = innDocRecordReposDay.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0, applDate_1,
					applDate_2, usageCode_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = innDocRecordReposMon.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0, applDate_1,
					applDate_2, usageCode_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = innDocRecordReposHist.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0, applDate_1,
					applDate_2, usageCode_3, pageable);
		else
			slice = innDocRecordRepos.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0, applDate_1,
					applDate_2, usageCode_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<InnDocRecord> findL5903DRg(int custNo_0, int applDate_1, int applDate_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InnDocRecord> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findL5903DRg " + dbName + " : " + "custNo_0 : " + custNo_0 + " applDate_1 : " + applDate_1 + " applDate_2 : " + applDate_2);
		if (dbName.equals(ContentName.onDay))
			slice = innDocRecordReposDay.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0, applDate_1, applDate_2,
					pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = innDocRecordReposMon.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0, applDate_1, applDate_2,
					pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = innDocRecordReposHist.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0, applDate_1, applDate_2,
					pageable);
		else
			slice = innDocRecordRepos.findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(custNo_0, applDate_1, applDate_2,
					pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<InnDocRecord> findL5104ARg(int applDate_0, int applDate_1, int returnDate_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InnDocRecord> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findL5104ARg " + dbName + " : " + "applDate_0 : " + applDate_0 + " applDate_1 : " + applDate_1 + " returnDate_2 : " + returnDate_2);
		if (dbName.equals(ContentName.onDay))
			slice = innDocRecordReposDay.findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualAndReturnDateIs(applDate_0, applDate_1, returnDate_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = innDocRecordReposMon.findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualAndReturnDateIs(applDate_0, applDate_1, returnDate_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = innDocRecordReposHist.findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualAndReturnDateIs(applDate_0, applDate_1, returnDate_2, pageable);
		else
			slice = innDocRecordRepos.findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualAndReturnDateIs(applDate_0, applDate_1, returnDate_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<InnDocRecord> findL5104BRg(int applDate_0, int applDate_1, String usageCode_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InnDocRecord> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findL5104BRg " + dbName + " : " + "applDate_0 : " + applDate_0 + " applDate_1 : " + applDate_1 + " usageCode_2 : " + usageCode_2);
		if (dbName.equals(ContentName.onDay))
			slice = innDocRecordReposDay.findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIs(applDate_0, applDate_1, usageCode_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = innDocRecordReposMon.findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIs(applDate_0, applDate_1, usageCode_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = innDocRecordReposHist.findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIs(applDate_0, applDate_1, usageCode_2, pageable);
		else
			slice = innDocRecordRepos.findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIs(applDate_0, applDate_1, usageCode_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public InnDocRecord holdById(InnDocRecordId innDocRecordId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + innDocRecordId);
		Optional<InnDocRecord> innDocRecord = null;
		if (dbName.equals(ContentName.onDay))
			innDocRecord = innDocRecordReposDay.findByInnDocRecordId(innDocRecordId);
		else if (dbName.equals(ContentName.onMon))
			innDocRecord = innDocRecordReposMon.findByInnDocRecordId(innDocRecordId);
		else if (dbName.equals(ContentName.onHist))
			innDocRecord = innDocRecordReposHist.findByInnDocRecordId(innDocRecordId);
		else
			innDocRecord = innDocRecordRepos.findByInnDocRecordId(innDocRecordId);
		return innDocRecord.isPresent() ? innDocRecord.get() : null;
	}

	@Override
	public InnDocRecord holdById(InnDocRecord innDocRecord, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + innDocRecord.getInnDocRecordId());
		Optional<InnDocRecord> innDocRecordT = null;
		if (dbName.equals(ContentName.onDay))
			innDocRecordT = innDocRecordReposDay.findByInnDocRecordId(innDocRecord.getInnDocRecordId());
		else if (dbName.equals(ContentName.onMon))
			innDocRecordT = innDocRecordReposMon.findByInnDocRecordId(innDocRecord.getInnDocRecordId());
		else if (dbName.equals(ContentName.onHist))
			innDocRecordT = innDocRecordReposHist.findByInnDocRecordId(innDocRecord.getInnDocRecordId());
		else
			innDocRecordT = innDocRecordRepos.findByInnDocRecordId(innDocRecord.getInnDocRecordId());
		return innDocRecordT.isPresent() ? innDocRecordT.get() : null;
	}

	@Override
	public InnDocRecord insert(InnDocRecord innDocRecord, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + innDocRecord.getInnDocRecordId());
		if (this.findById(innDocRecord.getInnDocRecordId(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			innDocRecord.setCreateEmpNo(empNot);

		if (innDocRecord.getLastUpdateEmpNo() == null || innDocRecord.getLastUpdateEmpNo().isEmpty())
			innDocRecord.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return innDocRecordReposDay.saveAndFlush(innDocRecord);
		else if (dbName.equals(ContentName.onMon))
			return innDocRecordReposMon.saveAndFlush(innDocRecord);
		else if (dbName.equals(ContentName.onHist))
			return innDocRecordReposHist.saveAndFlush(innDocRecord);
		else
			return innDocRecordRepos.saveAndFlush(innDocRecord);
	}

	@Override
	public InnDocRecord update(InnDocRecord innDocRecord, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + innDocRecord.getInnDocRecordId());
		if (!empNot.isEmpty())
			innDocRecord.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return innDocRecordReposDay.saveAndFlush(innDocRecord);
		else if (dbName.equals(ContentName.onMon))
			return innDocRecordReposMon.saveAndFlush(innDocRecord);
		else if (dbName.equals(ContentName.onHist))
			return innDocRecordReposHist.saveAndFlush(innDocRecord);
		else
			return innDocRecordRepos.saveAndFlush(innDocRecord);
	}

	@Override
	public InnDocRecord update2(InnDocRecord innDocRecord, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + innDocRecord.getInnDocRecordId());
		if (!empNot.isEmpty())
			innDocRecord.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			innDocRecordReposDay.saveAndFlush(innDocRecord);
		else if (dbName.equals(ContentName.onMon))
			innDocRecordReposMon.saveAndFlush(innDocRecord);
		else if (dbName.equals(ContentName.onHist))
			innDocRecordReposHist.saveAndFlush(innDocRecord);
		else
			innDocRecordRepos.saveAndFlush(innDocRecord);
		return this.findById(innDocRecord.getInnDocRecordId());
	}

	@Override
	public void delete(InnDocRecord innDocRecord, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + innDocRecord.getInnDocRecordId());
		if (dbName.equals(ContentName.onDay)) {
			innDocRecordReposDay.delete(innDocRecord);
			innDocRecordReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			innDocRecordReposMon.delete(innDocRecord);
			innDocRecordReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			innDocRecordReposHist.delete(innDocRecord);
			innDocRecordReposHist.flush();
		} else {
			innDocRecordRepos.delete(innDocRecord);
			innDocRecordRepos.flush();
		}
	}

	@Override
	public void insertAll(List<InnDocRecord> innDocRecord, TitaVo... titaVo) throws DBException {
		if (innDocRecord == null || innDocRecord.size() == 0)
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
		for (InnDocRecord t : innDocRecord) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			innDocRecord = innDocRecordReposDay.saveAll(innDocRecord);
			innDocRecordReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			innDocRecord = innDocRecordReposMon.saveAll(innDocRecord);
			innDocRecordReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			innDocRecord = innDocRecordReposHist.saveAll(innDocRecord);
			innDocRecordReposHist.flush();
		} else {
			innDocRecord = innDocRecordRepos.saveAll(innDocRecord);
			innDocRecordRepos.flush();
		}
	}

	@Override
	public void updateAll(List<InnDocRecord> innDocRecord, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (innDocRecord == null || innDocRecord.size() == 0)
			throw new DBException(6);

		for (InnDocRecord t : innDocRecord)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			innDocRecord = innDocRecordReposDay.saveAll(innDocRecord);
			innDocRecordReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			innDocRecord = innDocRecordReposMon.saveAll(innDocRecord);
			innDocRecordReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			innDocRecord = innDocRecordReposHist.saveAll(innDocRecord);
			innDocRecordReposHist.flush();
		} else {
			innDocRecord = innDocRecordRepos.saveAll(innDocRecord);
			innDocRecordRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<InnDocRecord> innDocRecord, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (innDocRecord == null || innDocRecord.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			innDocRecordReposDay.deleteAll(innDocRecord);
			innDocRecordReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			innDocRecordReposMon.deleteAll(innDocRecord);
			innDocRecordReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			innDocRecordReposHist.deleteAll(innDocRecord);
			innDocRecordReposHist.flush();
		} else {
			innDocRecordRepos.deleteAll(innDocRecord);
			innDocRecordRepos.flush();
		}
	}

}
