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
import com.st1.itx.db.domain.DailyLoanBal;
import com.st1.itx.db.domain.DailyLoanBalId;
import com.st1.itx.db.repository.online.DailyLoanBalRepository;
import com.st1.itx.db.repository.day.DailyLoanBalRepositoryDay;
import com.st1.itx.db.repository.mon.DailyLoanBalRepositoryMon;
import com.st1.itx.db.repository.hist.DailyLoanBalRepositoryHist;
import com.st1.itx.db.service.DailyLoanBalService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("dailyLoanBalService")
@Repository
public class DailyLoanBalServiceImpl extends ASpringJpaParm implements DailyLoanBalService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private DailyLoanBalRepository dailyLoanBalRepos;

	@Autowired
	private DailyLoanBalRepositoryDay dailyLoanBalReposDay;

	@Autowired
	private DailyLoanBalRepositoryMon dailyLoanBalReposMon;

	@Autowired
	private DailyLoanBalRepositoryHist dailyLoanBalReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(dailyLoanBalRepos);
		org.junit.Assert.assertNotNull(dailyLoanBalReposDay);
		org.junit.Assert.assertNotNull(dailyLoanBalReposMon);
		org.junit.Assert.assertNotNull(dailyLoanBalReposHist);
	}

	@Override
	public DailyLoanBal findById(DailyLoanBalId dailyLoanBalId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + dailyLoanBalId);
		Optional<DailyLoanBal> dailyLoanBal = null;
		if (dbName.equals(ContentName.onDay))
			dailyLoanBal = dailyLoanBalReposDay.findById(dailyLoanBalId);
		else if (dbName.equals(ContentName.onMon))
			dailyLoanBal = dailyLoanBalReposMon.findById(dailyLoanBalId);
		else if (dbName.equals(ContentName.onHist))
			dailyLoanBal = dailyLoanBalReposHist.findById(dailyLoanBalId);
		else
			dailyLoanBal = dailyLoanBalRepos.findById(dailyLoanBalId);
		DailyLoanBal obj = dailyLoanBal.isPresent() ? dailyLoanBal.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<DailyLoanBal> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<DailyLoanBal> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataDate", "CustNo", "FacmNo", "BormNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataDate", "CustNo", "FacmNo", "BormNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = dailyLoanBalReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = dailyLoanBalReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = dailyLoanBalReposHist.findAll(pageable);
		else
			slice = dailyLoanBalRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public DailyLoanBal dataDateFirst(int custNo_0, int facmNo_1, int bormNo_2, int dataDate_3, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("dataDateFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " bormNo_2 : " + bormNo_2 + " dataDate_3 : " + dataDate_3);
		Optional<DailyLoanBal> dailyLoanBalT = null;
		if (dbName.equals(ContentName.onDay))
			dailyLoanBalT = dailyLoanBalReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndDataDateLessThanEqualOrderByDataDateDesc(custNo_0, facmNo_1, bormNo_2, dataDate_3);
		else if (dbName.equals(ContentName.onMon))
			dailyLoanBalT = dailyLoanBalReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndDataDateLessThanEqualOrderByDataDateDesc(custNo_0, facmNo_1, bormNo_2, dataDate_3);
		else if (dbName.equals(ContentName.onHist))
			dailyLoanBalT = dailyLoanBalReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndDataDateLessThanEqualOrderByDataDateDesc(custNo_0, facmNo_1, bormNo_2, dataDate_3);
		else
			dailyLoanBalT = dailyLoanBalRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndDataDateLessThanEqualOrderByDataDateDesc(custNo_0, facmNo_1, bormNo_2, dataDate_3);

		return dailyLoanBalT.isPresent() ? dailyLoanBalT.get() : null;
	}

	@Override
	public DailyLoanBal holdById(DailyLoanBalId dailyLoanBalId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + dailyLoanBalId);
		Optional<DailyLoanBal> dailyLoanBal = null;
		if (dbName.equals(ContentName.onDay))
			dailyLoanBal = dailyLoanBalReposDay.findByDailyLoanBalId(dailyLoanBalId);
		else if (dbName.equals(ContentName.onMon))
			dailyLoanBal = dailyLoanBalReposMon.findByDailyLoanBalId(dailyLoanBalId);
		else if (dbName.equals(ContentName.onHist))
			dailyLoanBal = dailyLoanBalReposHist.findByDailyLoanBalId(dailyLoanBalId);
		else
			dailyLoanBal = dailyLoanBalRepos.findByDailyLoanBalId(dailyLoanBalId);
		return dailyLoanBal.isPresent() ? dailyLoanBal.get() : null;
	}

	@Override
	public DailyLoanBal holdById(DailyLoanBal dailyLoanBal, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + dailyLoanBal.getDailyLoanBalId());
		Optional<DailyLoanBal> dailyLoanBalT = null;
		if (dbName.equals(ContentName.onDay))
			dailyLoanBalT = dailyLoanBalReposDay.findByDailyLoanBalId(dailyLoanBal.getDailyLoanBalId());
		else if (dbName.equals(ContentName.onMon))
			dailyLoanBalT = dailyLoanBalReposMon.findByDailyLoanBalId(dailyLoanBal.getDailyLoanBalId());
		else if (dbName.equals(ContentName.onHist))
			dailyLoanBalT = dailyLoanBalReposHist.findByDailyLoanBalId(dailyLoanBal.getDailyLoanBalId());
		else
			dailyLoanBalT = dailyLoanBalRepos.findByDailyLoanBalId(dailyLoanBal.getDailyLoanBalId());
		return dailyLoanBalT.isPresent() ? dailyLoanBalT.get() : null;
	}

	@Override
	public DailyLoanBal insert(DailyLoanBal dailyLoanBal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + dailyLoanBal.getDailyLoanBalId());
		if (this.findById(dailyLoanBal.getDailyLoanBalId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			dailyLoanBal.setCreateEmpNo(empNot);

		if (dailyLoanBal.getLastUpdateEmpNo() == null || dailyLoanBal.getLastUpdateEmpNo().isEmpty())
			dailyLoanBal.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return dailyLoanBalReposDay.saveAndFlush(dailyLoanBal);
		else if (dbName.equals(ContentName.onMon))
			return dailyLoanBalReposMon.saveAndFlush(dailyLoanBal);
		else if (dbName.equals(ContentName.onHist))
			return dailyLoanBalReposHist.saveAndFlush(dailyLoanBal);
		else
			return dailyLoanBalRepos.saveAndFlush(dailyLoanBal);
	}

	@Override
	public DailyLoanBal update(DailyLoanBal dailyLoanBal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + dailyLoanBal.getDailyLoanBalId());
		if (!empNot.isEmpty())
			dailyLoanBal.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return dailyLoanBalReposDay.saveAndFlush(dailyLoanBal);
		else if (dbName.equals(ContentName.onMon))
			return dailyLoanBalReposMon.saveAndFlush(dailyLoanBal);
		else if (dbName.equals(ContentName.onHist))
			return dailyLoanBalReposHist.saveAndFlush(dailyLoanBal);
		else
			return dailyLoanBalRepos.saveAndFlush(dailyLoanBal);
	}

	@Override
	public DailyLoanBal update2(DailyLoanBal dailyLoanBal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + dailyLoanBal.getDailyLoanBalId());
		if (!empNot.isEmpty())
			dailyLoanBal.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			dailyLoanBalReposDay.saveAndFlush(dailyLoanBal);
		else if (dbName.equals(ContentName.onMon))
			dailyLoanBalReposMon.saveAndFlush(dailyLoanBal);
		else if (dbName.equals(ContentName.onHist))
			dailyLoanBalReposHist.saveAndFlush(dailyLoanBal);
		else
			dailyLoanBalRepos.saveAndFlush(dailyLoanBal);
		return this.findById(dailyLoanBal.getDailyLoanBalId());
	}

	@Override
	public void delete(DailyLoanBal dailyLoanBal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + dailyLoanBal.getDailyLoanBalId());
		if (dbName.equals(ContentName.onDay)) {
			dailyLoanBalReposDay.delete(dailyLoanBal);
			dailyLoanBalReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			dailyLoanBalReposMon.delete(dailyLoanBal);
			dailyLoanBalReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			dailyLoanBalReposHist.delete(dailyLoanBal);
			dailyLoanBalReposHist.flush();
		} else {
			dailyLoanBalRepos.delete(dailyLoanBal);
			dailyLoanBalRepos.flush();
		}
	}

	@Override
	public void insertAll(List<DailyLoanBal> dailyLoanBal, TitaVo... titaVo) throws DBException {
		if (dailyLoanBal == null || dailyLoanBal.size() == 0)
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
		for (DailyLoanBal t : dailyLoanBal) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			dailyLoanBal = dailyLoanBalReposDay.saveAll(dailyLoanBal);
			dailyLoanBalReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			dailyLoanBal = dailyLoanBalReposMon.saveAll(dailyLoanBal);
			dailyLoanBalReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			dailyLoanBal = dailyLoanBalReposHist.saveAll(dailyLoanBal);
			dailyLoanBalReposHist.flush();
		} else {
			dailyLoanBal = dailyLoanBalRepos.saveAll(dailyLoanBal);
			dailyLoanBalRepos.flush();
		}
	}

	@Override
	public void updateAll(List<DailyLoanBal> dailyLoanBal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (dailyLoanBal == null || dailyLoanBal.size() == 0)
			throw new DBException(6);

		for (DailyLoanBal t : dailyLoanBal)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			dailyLoanBal = dailyLoanBalReposDay.saveAll(dailyLoanBal);
			dailyLoanBalReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			dailyLoanBal = dailyLoanBalReposMon.saveAll(dailyLoanBal);
			dailyLoanBalReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			dailyLoanBal = dailyLoanBalReposHist.saveAll(dailyLoanBal);
			dailyLoanBalReposHist.flush();
		} else {
			dailyLoanBal = dailyLoanBalRepos.saveAll(dailyLoanBal);
			dailyLoanBalRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<DailyLoanBal> dailyLoanBal, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dailyLoanBal == null || dailyLoanBal.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			dailyLoanBalReposDay.deleteAll(dailyLoanBal);
			dailyLoanBalReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			dailyLoanBalReposMon.deleteAll(dailyLoanBal);
			dailyLoanBalReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			dailyLoanBalReposHist.deleteAll(dailyLoanBal);
			dailyLoanBalReposHist.flush();
		} else {
			dailyLoanBalRepos.deleteAll(dailyLoanBal);
			dailyLoanBalRepos.flush();
		}
	}

	@Override
	public void Usp_L9_DailyLoanBal_Upd(int tbsdyf, String empNo, int mfbsdyf, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			dailyLoanBalReposDay.uspL9DailyloanbalUpd(tbsdyf, empNo, mfbsdyf);
		else if (dbName.equals(ContentName.onMon))
			dailyLoanBalReposMon.uspL9DailyloanbalUpd(tbsdyf, empNo, mfbsdyf);
		else if (dbName.equals(ContentName.onHist))
			dailyLoanBalReposHist.uspL9DailyloanbalUpd(tbsdyf, empNo, mfbsdyf);
		else
			dailyLoanBalRepos.uspL9DailyloanbalUpd(tbsdyf, empNo, mfbsdyf);
	}

}
