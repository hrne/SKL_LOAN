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
import com.st1.itx.db.domain.CdSyndFee;
import com.st1.itx.db.repository.online.CdSyndFeeRepository;
import com.st1.itx.db.repository.day.CdSyndFeeRepositoryDay;
import com.st1.itx.db.repository.mon.CdSyndFeeRepositoryMon;
import com.st1.itx.db.repository.hist.CdSyndFeeRepositoryHist;
import com.st1.itx.db.service.CdSyndFeeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdSyndFeeService")
@Repository
public class CdSyndFeeServiceImpl extends ASpringJpaParm implements CdSyndFeeService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdSyndFeeRepository cdSyndFeeRepos;

	@Autowired
	private CdSyndFeeRepositoryDay cdSyndFeeReposDay;

	@Autowired
	private CdSyndFeeRepositoryMon cdSyndFeeReposMon;

	@Autowired
	private CdSyndFeeRepositoryHist cdSyndFeeReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdSyndFeeRepos);
		org.junit.Assert.assertNotNull(cdSyndFeeReposDay);
		org.junit.Assert.assertNotNull(cdSyndFeeReposMon);
		org.junit.Assert.assertNotNull(cdSyndFeeReposHist);
	}

	@Override
	public CdSyndFee findById(String syndFeeCode, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + syndFeeCode);
		Optional<CdSyndFee> cdSyndFee = null;
		if (dbName.equals(ContentName.onDay))
			cdSyndFee = cdSyndFeeReposDay.findById(syndFeeCode);
		else if (dbName.equals(ContentName.onMon))
			cdSyndFee = cdSyndFeeReposMon.findById(syndFeeCode);
		else if (dbName.equals(ContentName.onHist))
			cdSyndFee = cdSyndFeeReposHist.findById(syndFeeCode);
		else
			cdSyndFee = cdSyndFeeRepos.findById(syndFeeCode);
		CdSyndFee obj = cdSyndFee.isPresent() ? cdSyndFee.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdSyndFee> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdSyndFee> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SyndFeeCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SyndFeeCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdSyndFeeReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdSyndFeeReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdSyndFeeReposHist.findAll(pageable);
		else
			slice = cdSyndFeeRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdSyndFee acctCodeFirst(String acctCode_0, String acctCode_1, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("acctCodeFirst " + dbName + " : " + "acctCode_0 : " + acctCode_0 + " acctCode_1 : " + acctCode_1);
		Optional<CdSyndFee> cdSyndFeeT = null;
		if (dbName.equals(ContentName.onDay))
			cdSyndFeeT = cdSyndFeeReposDay.findTopByAcctCodeGreaterThanEqualAndAcctCodeLessThanEqual(acctCode_0, acctCode_1);
		else if (dbName.equals(ContentName.onMon))
			cdSyndFeeT = cdSyndFeeReposMon.findTopByAcctCodeGreaterThanEqualAndAcctCodeLessThanEqual(acctCode_0, acctCode_1);
		else if (dbName.equals(ContentName.onHist))
			cdSyndFeeT = cdSyndFeeReposHist.findTopByAcctCodeGreaterThanEqualAndAcctCodeLessThanEqual(acctCode_0, acctCode_1);
		else
			cdSyndFeeT = cdSyndFeeRepos.findTopByAcctCodeGreaterThanEqualAndAcctCodeLessThanEqual(acctCode_0, acctCode_1);

		return cdSyndFeeT.isPresent() ? cdSyndFeeT.get() : null;
	}

	@Override
	public Slice<CdSyndFee> findSyndFeeCode(String syndFeeCode_0, String syndFeeCode_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdSyndFee> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findSyndFeeCode " + dbName + " : " + "syndFeeCode_0 : " + syndFeeCode_0 + " syndFeeCode_1 : " + syndFeeCode_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdSyndFeeReposDay.findAllBySyndFeeCodeGreaterThanEqualAndSyndFeeCodeLessThanEqualOrderBySyndFeeCodeAsc(syndFeeCode_0, syndFeeCode_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdSyndFeeReposMon.findAllBySyndFeeCodeGreaterThanEqualAndSyndFeeCodeLessThanEqualOrderBySyndFeeCodeAsc(syndFeeCode_0, syndFeeCode_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdSyndFeeReposHist.findAllBySyndFeeCodeGreaterThanEqualAndSyndFeeCodeLessThanEqualOrderBySyndFeeCodeAsc(syndFeeCode_0, syndFeeCode_1, pageable);
		else
			slice = cdSyndFeeRepos.findAllBySyndFeeCodeGreaterThanEqualAndSyndFeeCodeLessThanEqualOrderBySyndFeeCodeAsc(syndFeeCode_0, syndFeeCode_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdSyndFee holdById(String syndFeeCode, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + syndFeeCode);
		Optional<CdSyndFee> cdSyndFee = null;
		if (dbName.equals(ContentName.onDay))
			cdSyndFee = cdSyndFeeReposDay.findBySyndFeeCode(syndFeeCode);
		else if (dbName.equals(ContentName.onMon))
			cdSyndFee = cdSyndFeeReposMon.findBySyndFeeCode(syndFeeCode);
		else if (dbName.equals(ContentName.onHist))
			cdSyndFee = cdSyndFeeReposHist.findBySyndFeeCode(syndFeeCode);
		else
			cdSyndFee = cdSyndFeeRepos.findBySyndFeeCode(syndFeeCode);
		return cdSyndFee.isPresent() ? cdSyndFee.get() : null;
	}

	@Override
	public CdSyndFee holdById(CdSyndFee cdSyndFee, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdSyndFee.getSyndFeeCode());
		Optional<CdSyndFee> cdSyndFeeT = null;
		if (dbName.equals(ContentName.onDay))
			cdSyndFeeT = cdSyndFeeReposDay.findBySyndFeeCode(cdSyndFee.getSyndFeeCode());
		else if (dbName.equals(ContentName.onMon))
			cdSyndFeeT = cdSyndFeeReposMon.findBySyndFeeCode(cdSyndFee.getSyndFeeCode());
		else if (dbName.equals(ContentName.onHist))
			cdSyndFeeT = cdSyndFeeReposHist.findBySyndFeeCode(cdSyndFee.getSyndFeeCode());
		else
			cdSyndFeeT = cdSyndFeeRepos.findBySyndFeeCode(cdSyndFee.getSyndFeeCode());
		return cdSyndFeeT.isPresent() ? cdSyndFeeT.get() : null;
	}

	@Override
	public CdSyndFee insert(CdSyndFee cdSyndFee, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdSyndFee.getSyndFeeCode());
		if (this.findById(cdSyndFee.getSyndFeeCode()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdSyndFee.setCreateEmpNo(empNot);

		if (cdSyndFee.getLastUpdateEmpNo() == null || cdSyndFee.getLastUpdateEmpNo().isEmpty())
			cdSyndFee.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdSyndFeeReposDay.saveAndFlush(cdSyndFee);
		else if (dbName.equals(ContentName.onMon))
			return cdSyndFeeReposMon.saveAndFlush(cdSyndFee);
		else if (dbName.equals(ContentName.onHist))
			return cdSyndFeeReposHist.saveAndFlush(cdSyndFee);
		else
			return cdSyndFeeRepos.saveAndFlush(cdSyndFee);
	}

	@Override
	public CdSyndFee update(CdSyndFee cdSyndFee, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdSyndFee.getSyndFeeCode());
		if (!empNot.isEmpty())
			cdSyndFee.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdSyndFeeReposDay.saveAndFlush(cdSyndFee);
		else if (dbName.equals(ContentName.onMon))
			return cdSyndFeeReposMon.saveAndFlush(cdSyndFee);
		else if (dbName.equals(ContentName.onHist))
			return cdSyndFeeReposHist.saveAndFlush(cdSyndFee);
		else
			return cdSyndFeeRepos.saveAndFlush(cdSyndFee);
	}

	@Override
	public CdSyndFee update2(CdSyndFee cdSyndFee, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdSyndFee.getSyndFeeCode());
		if (!empNot.isEmpty())
			cdSyndFee.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdSyndFeeReposDay.saveAndFlush(cdSyndFee);
		else if (dbName.equals(ContentName.onMon))
			cdSyndFeeReposMon.saveAndFlush(cdSyndFee);
		else if (dbName.equals(ContentName.onHist))
			cdSyndFeeReposHist.saveAndFlush(cdSyndFee);
		else
			cdSyndFeeRepos.saveAndFlush(cdSyndFee);
		return this.findById(cdSyndFee.getSyndFeeCode());
	}

	@Override
	public void delete(CdSyndFee cdSyndFee, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdSyndFee.getSyndFeeCode());
		if (dbName.equals(ContentName.onDay)) {
			cdSyndFeeReposDay.delete(cdSyndFee);
			cdSyndFeeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdSyndFeeReposMon.delete(cdSyndFee);
			cdSyndFeeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdSyndFeeReposHist.delete(cdSyndFee);
			cdSyndFeeReposHist.flush();
		} else {
			cdSyndFeeRepos.delete(cdSyndFee);
			cdSyndFeeRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdSyndFee> cdSyndFee, TitaVo... titaVo) throws DBException {
		if (cdSyndFee == null || cdSyndFee.size() == 0)
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
		for (CdSyndFee t : cdSyndFee) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdSyndFee = cdSyndFeeReposDay.saveAll(cdSyndFee);
			cdSyndFeeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdSyndFee = cdSyndFeeReposMon.saveAll(cdSyndFee);
			cdSyndFeeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdSyndFee = cdSyndFeeReposHist.saveAll(cdSyndFee);
			cdSyndFeeReposHist.flush();
		} else {
			cdSyndFee = cdSyndFeeRepos.saveAll(cdSyndFee);
			cdSyndFeeRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdSyndFee> cdSyndFee, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdSyndFee == null || cdSyndFee.size() == 0)
			throw new DBException(6);

		for (CdSyndFee t : cdSyndFee)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdSyndFee = cdSyndFeeReposDay.saveAll(cdSyndFee);
			cdSyndFeeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdSyndFee = cdSyndFeeReposMon.saveAll(cdSyndFee);
			cdSyndFeeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdSyndFee = cdSyndFeeReposHist.saveAll(cdSyndFee);
			cdSyndFeeReposHist.flush();
		} else {
			cdSyndFee = cdSyndFeeRepos.saveAll(cdSyndFee);
			cdSyndFeeRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdSyndFee> cdSyndFee, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdSyndFee == null || cdSyndFee.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdSyndFeeReposDay.deleteAll(cdSyndFee);
			cdSyndFeeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdSyndFeeReposMon.deleteAll(cdSyndFee);
			cdSyndFeeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdSyndFeeReposHist.deleteAll(cdSyndFee);
			cdSyndFeeReposHist.flush();
		} else {
			cdSyndFeeRepos.deleteAll(cdSyndFee);
			cdSyndFeeRepos.flush();
		}
	}

}
