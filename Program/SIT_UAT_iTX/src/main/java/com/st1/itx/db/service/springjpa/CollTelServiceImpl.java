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
import com.st1.itx.db.domain.CollTel;
import com.st1.itx.db.domain.CollTelId;
import com.st1.itx.db.repository.online.CollTelRepository;
import com.st1.itx.db.repository.day.CollTelRepositoryDay;
import com.st1.itx.db.repository.mon.CollTelRepositoryMon;
import com.st1.itx.db.repository.hist.CollTelRepositoryHist;
import com.st1.itx.db.service.CollTelService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("collTelService")
@Repository
public class CollTelServiceImpl extends ASpringJpaParm implements CollTelService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CollTelRepository collTelRepos;

	@Autowired
	private CollTelRepositoryDay collTelReposDay;

	@Autowired
	private CollTelRepositoryMon collTelReposMon;

	@Autowired
	private CollTelRepositoryHist collTelReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(collTelRepos);
		org.junit.Assert.assertNotNull(collTelReposDay);
		org.junit.Assert.assertNotNull(collTelReposMon);
		org.junit.Assert.assertNotNull(collTelReposHist);
	}

	@Override
	public CollTel findById(CollTelId collTelId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + collTelId);
		Optional<CollTel> collTel = null;
		if (dbName.equals(ContentName.onDay))
			collTel = collTelReposDay.findById(collTelId);
		else if (dbName.equals(ContentName.onMon))
			collTel = collTelReposMon.findById(collTelId);
		else if (dbName.equals(ContentName.onHist))
			collTel = collTelReposHist.findById(collTelId);
		else
			collTel = collTelRepos.findById(collTelId);
		CollTel obj = collTel.isPresent() ? collTel.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CollTel> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CollTel> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CaseCode", "CustNo", "FacmNo", "AcDate", "TitaTlrNo", "TitaTxtNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CaseCode", "CustNo", "FacmNo", "AcDate", "TitaTlrNo", "TitaTxtNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = collTelReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = collTelReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = collTelReposHist.findAll(pageable);
		else
			slice = collTelRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CollTel> telTimeBetween(int telDate_0, int telDate_1, String caseCode_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CollTel> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info(
				"telTimeBetween " + dbName + " : " + "telDate_0 : " + telDate_0 + " telDate_1 : " + telDate_1 + " caseCode_2 : " + caseCode_2 + " custNo_3 : " + custNo_3 + " facmNo_4 : " + facmNo_4);
		if (dbName.equals(ContentName.onDay))
			slice = collTelReposDay.findAllByTelDateGreaterThanEqualAndTelDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByTelDateDesc(telDate_0, telDate_1, caseCode_2, custNo_3, facmNo_4,
					pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = collTelReposMon.findAllByTelDateGreaterThanEqualAndTelDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByTelDateDesc(telDate_0, telDate_1, caseCode_2, custNo_3, facmNo_4,
					pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = collTelReposHist.findAllByTelDateGreaterThanEqualAndTelDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByTelDateDesc(telDate_0, telDate_1, caseCode_2, custNo_3, facmNo_4,
					pageable);
		else
			slice = collTelRepos.findAllByTelDateGreaterThanEqualAndTelDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByTelDateDesc(telDate_0, telDate_1, caseCode_2, custNo_3, facmNo_4,
					pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CollTel> findSameCust(String caseCode_0, int custNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CollTel> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findSameCust " + dbName + " : " + "caseCode_0 : " + caseCode_0 + " custNo_1 : " + custNo_1 + " facmNo_2 : " + facmNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = collTelReposDay.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByTelDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = collTelReposMon.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByTelDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = collTelReposHist.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByTelDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);
		else
			slice = collTelRepos.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByTelDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CollTel> withoutFacmNo(int telDate_0, int telDate_1, String caseCode_2, int custNo_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CollTel> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("withoutFacmNo " + dbName + " : " + "telDate_0 : " + telDate_0 + " telDate_1 : " + telDate_1 + " caseCode_2 : " + caseCode_2 + " custNo_3 : " + custNo_3);
		if (dbName.equals(ContentName.onDay))
			slice = collTelReposDay.findAllByTelDateGreaterThanEqualAndTelDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByTelDateDesc(telDate_0, telDate_1, caseCode_2, custNo_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = collTelReposMon.findAllByTelDateGreaterThanEqualAndTelDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByTelDateDesc(telDate_0, telDate_1, caseCode_2, custNo_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = collTelReposHist.findAllByTelDateGreaterThanEqualAndTelDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByTelDateDesc(telDate_0, telDate_1, caseCode_2, custNo_3, pageable);
		else
			slice = collTelRepos.findAllByTelDateGreaterThanEqualAndTelDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByTelDateDesc(telDate_0, telDate_1, caseCode_2, custNo_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CollTel> withoutFacmNoAll(String caseCode_0, int custNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CollTel> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("withoutFacmNoAll " + dbName + " : " + "caseCode_0 : " + caseCode_0 + " custNo_1 : " + custNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = collTelReposDay.findAllByCaseCodeIsAndCustNoIsOrderByTelDateDesc(caseCode_0, custNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = collTelReposMon.findAllByCaseCodeIsAndCustNoIsOrderByTelDateDesc(caseCode_0, custNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = collTelReposHist.findAllByCaseCodeIsAndCustNoIsOrderByTelDateDesc(caseCode_0, custNo_1, pageable);
		else
			slice = collTelRepos.findAllByCaseCodeIsAndCustNoIsOrderByTelDateDesc(caseCode_0, custNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CollTel holdById(CollTelId collTelId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + collTelId);
		Optional<CollTel> collTel = null;
		if (dbName.equals(ContentName.onDay))
			collTel = collTelReposDay.findByCollTelId(collTelId);
		else if (dbName.equals(ContentName.onMon))
			collTel = collTelReposMon.findByCollTelId(collTelId);
		else if (dbName.equals(ContentName.onHist))
			collTel = collTelReposHist.findByCollTelId(collTelId);
		else
			collTel = collTelRepos.findByCollTelId(collTelId);
		return collTel.isPresent() ? collTel.get() : null;
	}

	@Override
	public CollTel holdById(CollTel collTel, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + collTel.getCollTelId());
		Optional<CollTel> collTelT = null;
		if (dbName.equals(ContentName.onDay))
			collTelT = collTelReposDay.findByCollTelId(collTel.getCollTelId());
		else if (dbName.equals(ContentName.onMon))
			collTelT = collTelReposMon.findByCollTelId(collTel.getCollTelId());
		else if (dbName.equals(ContentName.onHist))
			collTelT = collTelReposHist.findByCollTelId(collTel.getCollTelId());
		else
			collTelT = collTelRepos.findByCollTelId(collTel.getCollTelId());
		return collTelT.isPresent() ? collTelT.get() : null;
	}

	@Override
	public CollTel insert(CollTel collTel, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + collTel.getCollTelId());
		if (this.findById(collTel.getCollTelId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			collTel.setCreateEmpNo(empNot);

		if (collTel.getLastUpdateEmpNo() == null || collTel.getLastUpdateEmpNo().isEmpty())
			collTel.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return collTelReposDay.saveAndFlush(collTel);
		else if (dbName.equals(ContentName.onMon))
			return collTelReposMon.saveAndFlush(collTel);
		else if (dbName.equals(ContentName.onHist))
			return collTelReposHist.saveAndFlush(collTel);
		else
			return collTelRepos.saveAndFlush(collTel);
	}

	@Override
	public CollTel update(CollTel collTel, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + collTel.getCollTelId());
		if (!empNot.isEmpty())
			collTel.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return collTelReposDay.saveAndFlush(collTel);
		else if (dbName.equals(ContentName.onMon))
			return collTelReposMon.saveAndFlush(collTel);
		else if (dbName.equals(ContentName.onHist))
			return collTelReposHist.saveAndFlush(collTel);
		else
			return collTelRepos.saveAndFlush(collTel);
	}

	@Override
	public CollTel update2(CollTel collTel, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + collTel.getCollTelId());
		if (!empNot.isEmpty())
			collTel.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			collTelReposDay.saveAndFlush(collTel);
		else if (dbName.equals(ContentName.onMon))
			collTelReposMon.saveAndFlush(collTel);
		else if (dbName.equals(ContentName.onHist))
			collTelReposHist.saveAndFlush(collTel);
		else
			collTelRepos.saveAndFlush(collTel);
		return this.findById(collTel.getCollTelId());
	}

	@Override
	public void delete(CollTel collTel, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + collTel.getCollTelId());
		if (dbName.equals(ContentName.onDay)) {
			collTelReposDay.delete(collTel);
			collTelReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			collTelReposMon.delete(collTel);
			collTelReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			collTelReposHist.delete(collTel);
			collTelReposHist.flush();
		} else {
			collTelRepos.delete(collTel);
			collTelRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CollTel> collTel, TitaVo... titaVo) throws DBException {
		if (collTel == null || collTel.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (CollTel t : collTel) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			collTel = collTelReposDay.saveAll(collTel);
			collTelReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			collTel = collTelReposMon.saveAll(collTel);
			collTelReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			collTel = collTelReposHist.saveAll(collTel);
			collTelReposHist.flush();
		} else {
			collTel = collTelRepos.saveAll(collTel);
			collTelRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CollTel> collTel, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (collTel == null || collTel.size() == 0)
			throw new DBException(6);

		for (CollTel t : collTel)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			collTel = collTelReposDay.saveAll(collTel);
			collTelReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			collTel = collTelReposMon.saveAll(collTel);
			collTelReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			collTel = collTelReposHist.saveAll(collTel);
			collTelReposHist.flush();
		} else {
			collTel = collTelRepos.saveAll(collTel);
			collTelRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CollTel> collTel, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (collTel == null || collTel.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			collTelReposDay.deleteAll(collTel);
			collTelReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			collTelReposMon.deleteAll(collTel);
			collTelReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			collTelReposHist.deleteAll(collTel);
			collTelReposHist.flush();
		} else {
			collTelRepos.deleteAll(collTel);
			collTelRepos.flush();
		}
	}

}
