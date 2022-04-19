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
import com.st1.itx.db.domain.MonthlyLM028;
import com.st1.itx.db.domain.MonthlyLM028Id;
import com.st1.itx.db.repository.online.MonthlyLM028Repository;
import com.st1.itx.db.repository.day.MonthlyLM028RepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM028RepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM028RepositoryHist;
import com.st1.itx.db.service.MonthlyLM028Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM028Service")
@Repository
public class MonthlyLM028ServiceImpl extends ASpringJpaParm implements MonthlyLM028Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MonthlyLM028Repository monthlyLM028Repos;

	@Autowired
	private MonthlyLM028RepositoryDay monthlyLM028ReposDay;

	@Autowired
	private MonthlyLM028RepositoryMon monthlyLM028ReposMon;

	@Autowired
	private MonthlyLM028RepositoryHist monthlyLM028ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(monthlyLM028Repos);
		org.junit.Assert.assertNotNull(monthlyLM028ReposDay);
		org.junit.Assert.assertNotNull(monthlyLM028ReposMon);
		org.junit.Assert.assertNotNull(monthlyLM028ReposHist);
	}

	@Override
	public MonthlyLM028 findById(MonthlyLM028Id monthlyLM028Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + monthlyLM028Id);
		Optional<MonthlyLM028> monthlyLM028 = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM028 = monthlyLM028ReposDay.findById(monthlyLM028Id);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM028 = monthlyLM028ReposMon.findById(monthlyLM028Id);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM028 = monthlyLM028ReposHist.findById(monthlyLM028Id);
		else
			monthlyLM028 = monthlyLM028Repos.findById(monthlyLM028Id);
		MonthlyLM028 obj = monthlyLM028.isPresent() ? monthlyLM028.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MonthlyLM028> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyLM028> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataMonth", "CustNo", "FacmNo", "BormNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataMonth", "CustNo", "FacmNo", "BormNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyLM028ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyLM028ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyLM028ReposHist.findAll(pageable);
		else
			slice = monthlyLM028Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<MonthlyLM028> findByMonth(int dataMonth_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyLM028> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByMonth " + dbName + " : " + "dataMonth_0 : " + dataMonth_0);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyLM028ReposDay.findAllByDataMonthIsOrderByDataMonthAscCustNoAscFacmNoAscBormNoAsc(dataMonth_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyLM028ReposMon.findAllByDataMonthIsOrderByDataMonthAscCustNoAscFacmNoAscBormNoAsc(dataMonth_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyLM028ReposHist.findAllByDataMonthIsOrderByDataMonthAscCustNoAscFacmNoAscBormNoAsc(dataMonth_0, pageable);
		else
			slice = monthlyLM028Repos.findAllByDataMonthIsOrderByDataMonthAscCustNoAscFacmNoAscBormNoAsc(dataMonth_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MonthlyLM028 holdById(MonthlyLM028Id monthlyLM028Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + monthlyLM028Id);
		Optional<MonthlyLM028> monthlyLM028 = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM028 = monthlyLM028ReposDay.findByMonthlyLM028Id(monthlyLM028Id);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM028 = monthlyLM028ReposMon.findByMonthlyLM028Id(monthlyLM028Id);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM028 = monthlyLM028ReposHist.findByMonthlyLM028Id(monthlyLM028Id);
		else
			monthlyLM028 = monthlyLM028Repos.findByMonthlyLM028Id(monthlyLM028Id);
		return monthlyLM028.isPresent() ? monthlyLM028.get() : null;
	}

	@Override
	public MonthlyLM028 holdById(MonthlyLM028 monthlyLM028, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + monthlyLM028.getMonthlyLM028Id());
		Optional<MonthlyLM028> monthlyLM028T = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM028T = monthlyLM028ReposDay.findByMonthlyLM028Id(monthlyLM028.getMonthlyLM028Id());
		else if (dbName.equals(ContentName.onMon))
			monthlyLM028T = monthlyLM028ReposMon.findByMonthlyLM028Id(monthlyLM028.getMonthlyLM028Id());
		else if (dbName.equals(ContentName.onHist))
			monthlyLM028T = monthlyLM028ReposHist.findByMonthlyLM028Id(monthlyLM028.getMonthlyLM028Id());
		else
			monthlyLM028T = monthlyLM028Repos.findByMonthlyLM028Id(monthlyLM028.getMonthlyLM028Id());
		return monthlyLM028T.isPresent() ? monthlyLM028T.get() : null;
	}

	@Override
	public MonthlyLM028 insert(MonthlyLM028 monthlyLM028, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + monthlyLM028.getMonthlyLM028Id());
		if (this.findById(monthlyLM028.getMonthlyLM028Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			monthlyLM028.setCreateEmpNo(empNot);

		if (monthlyLM028.getLastUpdateEmpNo() == null || monthlyLM028.getLastUpdateEmpNo().isEmpty())
			monthlyLM028.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM028ReposDay.saveAndFlush(monthlyLM028);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM028ReposMon.saveAndFlush(monthlyLM028);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM028ReposHist.saveAndFlush(monthlyLM028);
		else
			return monthlyLM028Repos.saveAndFlush(monthlyLM028);
	}

	@Override
	public MonthlyLM028 update(MonthlyLM028 monthlyLM028, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + monthlyLM028.getMonthlyLM028Id());
		if (!empNot.isEmpty())
			monthlyLM028.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM028ReposDay.saveAndFlush(monthlyLM028);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM028ReposMon.saveAndFlush(monthlyLM028);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM028ReposHist.saveAndFlush(monthlyLM028);
		else
			return monthlyLM028Repos.saveAndFlush(monthlyLM028);
	}

	@Override
	public MonthlyLM028 update2(MonthlyLM028 monthlyLM028, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + monthlyLM028.getMonthlyLM028Id());
		if (!empNot.isEmpty())
			monthlyLM028.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			monthlyLM028ReposDay.saveAndFlush(monthlyLM028);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM028ReposMon.saveAndFlush(monthlyLM028);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM028ReposHist.saveAndFlush(monthlyLM028);
		else
			monthlyLM028Repos.saveAndFlush(monthlyLM028);
		return this.findById(monthlyLM028.getMonthlyLM028Id());
	}

	@Override
	public void delete(MonthlyLM028 monthlyLM028, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + monthlyLM028.getMonthlyLM028Id());
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM028ReposDay.delete(monthlyLM028);
			monthlyLM028ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM028ReposMon.delete(monthlyLM028);
			monthlyLM028ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM028ReposHist.delete(monthlyLM028);
			monthlyLM028ReposHist.flush();
		} else {
			monthlyLM028Repos.delete(monthlyLM028);
			monthlyLM028Repos.flush();
		}
	}

	@Override
	public void insertAll(List<MonthlyLM028> monthlyLM028, TitaVo... titaVo) throws DBException {
		if (monthlyLM028 == null || monthlyLM028.size() == 0)
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
		for (MonthlyLM028 t : monthlyLM028) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM028 = monthlyLM028ReposDay.saveAll(monthlyLM028);
			monthlyLM028ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM028 = monthlyLM028ReposMon.saveAll(monthlyLM028);
			monthlyLM028ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM028 = monthlyLM028ReposHist.saveAll(monthlyLM028);
			monthlyLM028ReposHist.flush();
		} else {
			monthlyLM028 = monthlyLM028Repos.saveAll(monthlyLM028);
			monthlyLM028Repos.flush();
		}
	}

	@Override
	public void updateAll(List<MonthlyLM028> monthlyLM028, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (monthlyLM028 == null || monthlyLM028.size() == 0)
			throw new DBException(6);

		for (MonthlyLM028 t : monthlyLM028)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM028 = monthlyLM028ReposDay.saveAll(monthlyLM028);
			monthlyLM028ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM028 = monthlyLM028ReposMon.saveAll(monthlyLM028);
			monthlyLM028ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM028 = monthlyLM028ReposHist.saveAll(monthlyLM028);
			monthlyLM028ReposHist.flush();
		} else {
			monthlyLM028 = monthlyLM028Repos.saveAll(monthlyLM028);
			monthlyLM028Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<MonthlyLM028> monthlyLM028, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (monthlyLM028 == null || monthlyLM028.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM028ReposDay.deleteAll(monthlyLM028);
			monthlyLM028ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM028ReposMon.deleteAll(monthlyLM028);
			monthlyLM028ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM028ReposHist.deleteAll(monthlyLM028);
			monthlyLM028ReposHist.flush();
		} else {
			monthlyLM028Repos.deleteAll(monthlyLM028);
			monthlyLM028Repos.flush();
		}
	}

	@Override
	public void Usp_L9_MonthlyLM028_Upd(int TBSDYF, String empNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			monthlyLM028ReposDay.uspL9Monthlylm028Upd(TBSDYF, empNo);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM028ReposMon.uspL9Monthlylm028Upd(TBSDYF, empNo);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM028ReposHist.uspL9Monthlylm028Upd(TBSDYF, empNo);
		else
			monthlyLM028Repos.uspL9Monthlylm028Upd(TBSDYF, empNo);
	}

}
