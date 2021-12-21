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
import com.st1.itx.db.domain.CdPerformance;
import com.st1.itx.db.domain.CdPerformanceId;
import com.st1.itx.db.repository.online.CdPerformanceRepository;
import com.st1.itx.db.repository.day.CdPerformanceRepositoryDay;
import com.st1.itx.db.repository.mon.CdPerformanceRepositoryMon;
import com.st1.itx.db.repository.hist.CdPerformanceRepositoryHist;
import com.st1.itx.db.service.CdPerformanceService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdPerformanceService")
@Repository
public class CdPerformanceServiceImpl extends ASpringJpaParm implements CdPerformanceService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdPerformanceRepository cdPerformanceRepos;

	@Autowired
	private CdPerformanceRepositoryDay cdPerformanceReposDay;

	@Autowired
	private CdPerformanceRepositoryMon cdPerformanceReposMon;

	@Autowired
	private CdPerformanceRepositoryHist cdPerformanceReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdPerformanceRepos);
		org.junit.Assert.assertNotNull(cdPerformanceReposDay);
		org.junit.Assert.assertNotNull(cdPerformanceReposMon);
		org.junit.Assert.assertNotNull(cdPerformanceReposHist);
	}

	@Override
	public CdPerformance findById(CdPerformanceId cdPerformanceId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + cdPerformanceId);
		Optional<CdPerformance> cdPerformance = null;
		if (dbName.equals(ContentName.onDay))
			cdPerformance = cdPerformanceReposDay.findById(cdPerformanceId);
		else if (dbName.equals(ContentName.onMon))
			cdPerformance = cdPerformanceReposMon.findById(cdPerformanceId);
		else if (dbName.equals(ContentName.onHist))
			cdPerformance = cdPerformanceReposHist.findById(cdPerformanceId);
		else
			cdPerformance = cdPerformanceRepos.findById(cdPerformanceId);
		CdPerformance obj = cdPerformance.isPresent() ? cdPerformance.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdPerformance> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdPerformance> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "WorkMonth", "PieceCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "WorkMonth", "PieceCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdPerformanceReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdPerformanceReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdPerformanceReposHist.findAll(pageable);
		else
			slice = cdPerformanceRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdPerformance> findPieceCode(int workMonth_0, String pieceCode_1, String pieceCode_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdPerformance> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findPieceCode " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " pieceCode_1 : " + pieceCode_1 + " pieceCode_2 : " + pieceCode_2);
		if (dbName.equals(ContentName.onDay))
			slice = cdPerformanceReposDay.findAllByWorkMonthIsAndPieceCodeGreaterThanEqualAndPieceCodeLessThanEqualOrderByPieceCodeAsc(workMonth_0, pieceCode_1, pieceCode_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdPerformanceReposMon.findAllByWorkMonthIsAndPieceCodeGreaterThanEqualAndPieceCodeLessThanEqualOrderByPieceCodeAsc(workMonth_0, pieceCode_1, pieceCode_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdPerformanceReposHist.findAllByWorkMonthIsAndPieceCodeGreaterThanEqualAndPieceCodeLessThanEqualOrderByPieceCodeAsc(workMonth_0, pieceCode_1, pieceCode_2, pageable);
		else
			slice = cdPerformanceRepos.findAllByWorkMonthIsAndPieceCodeGreaterThanEqualAndPieceCodeLessThanEqualOrderByPieceCodeAsc(workMonth_0, pieceCode_1, pieceCode_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdPerformance> findWorkMonth(int workMonth_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdPerformance> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findWorkMonth " + dbName + " : " + "workMonth_0 : " + workMonth_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdPerformanceReposDay.findAllByWorkMonthIs(workMonth_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdPerformanceReposMon.findAllByWorkMonthIs(workMonth_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdPerformanceReposHist.findAllByWorkMonthIs(workMonth_0, pageable);
		else
			slice = cdPerformanceRepos.findAllByWorkMonthIs(workMonth_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdPerformance findWorkMonthFirst(int workMonth_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findWorkMonthFirst " + dbName + " : " + "workMonth_0 : " + workMonth_0);
		Optional<CdPerformance> cdPerformanceT = null;
		if (dbName.equals(ContentName.onDay))
			cdPerformanceT = cdPerformanceReposDay.findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(workMonth_0);
		else if (dbName.equals(ContentName.onMon))
			cdPerformanceT = cdPerformanceReposMon.findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(workMonth_0);
		else if (dbName.equals(ContentName.onHist))
			cdPerformanceT = cdPerformanceReposHist.findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(workMonth_0);
		else
			cdPerformanceT = cdPerformanceRepos.findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(workMonth_0);

		return cdPerformanceT.isPresent() ? cdPerformanceT.get() : null;
	}

	@Override
	public CdPerformance holdById(CdPerformanceId cdPerformanceId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdPerformanceId);
		Optional<CdPerformance> cdPerformance = null;
		if (dbName.equals(ContentName.onDay))
			cdPerformance = cdPerformanceReposDay.findByCdPerformanceId(cdPerformanceId);
		else if (dbName.equals(ContentName.onMon))
			cdPerformance = cdPerformanceReposMon.findByCdPerformanceId(cdPerformanceId);
		else if (dbName.equals(ContentName.onHist))
			cdPerformance = cdPerformanceReposHist.findByCdPerformanceId(cdPerformanceId);
		else
			cdPerformance = cdPerformanceRepos.findByCdPerformanceId(cdPerformanceId);
		return cdPerformance.isPresent() ? cdPerformance.get() : null;
	}

	@Override
	public CdPerformance holdById(CdPerformance cdPerformance, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdPerformance.getCdPerformanceId());
		Optional<CdPerformance> cdPerformanceT = null;
		if (dbName.equals(ContentName.onDay))
			cdPerformanceT = cdPerformanceReposDay.findByCdPerformanceId(cdPerformance.getCdPerformanceId());
		else if (dbName.equals(ContentName.onMon))
			cdPerformanceT = cdPerformanceReposMon.findByCdPerformanceId(cdPerformance.getCdPerformanceId());
		else if (dbName.equals(ContentName.onHist))
			cdPerformanceT = cdPerformanceReposHist.findByCdPerformanceId(cdPerformance.getCdPerformanceId());
		else
			cdPerformanceT = cdPerformanceRepos.findByCdPerformanceId(cdPerformance.getCdPerformanceId());
		return cdPerformanceT.isPresent() ? cdPerformanceT.get() : null;
	}

	@Override
	public CdPerformance insert(CdPerformance cdPerformance, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdPerformance.getCdPerformanceId());
		if (this.findById(cdPerformance.getCdPerformanceId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdPerformance.setCreateEmpNo(empNot);

		if (cdPerformance.getLastUpdateEmpNo() == null || cdPerformance.getLastUpdateEmpNo().isEmpty())
			cdPerformance.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdPerformanceReposDay.saveAndFlush(cdPerformance);
		else if (dbName.equals(ContentName.onMon))
			return cdPerformanceReposMon.saveAndFlush(cdPerformance);
		else if (dbName.equals(ContentName.onHist))
			return cdPerformanceReposHist.saveAndFlush(cdPerformance);
		else
			return cdPerformanceRepos.saveAndFlush(cdPerformance);
	}

	@Override
	public CdPerformance update(CdPerformance cdPerformance, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdPerformance.getCdPerformanceId());
		if (!empNot.isEmpty())
			cdPerformance.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdPerformanceReposDay.saveAndFlush(cdPerformance);
		else if (dbName.equals(ContentName.onMon))
			return cdPerformanceReposMon.saveAndFlush(cdPerformance);
		else if (dbName.equals(ContentName.onHist))
			return cdPerformanceReposHist.saveAndFlush(cdPerformance);
		else
			return cdPerformanceRepos.saveAndFlush(cdPerformance);
	}

	@Override
	public CdPerformance update2(CdPerformance cdPerformance, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdPerformance.getCdPerformanceId());
		if (!empNot.isEmpty())
			cdPerformance.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdPerformanceReposDay.saveAndFlush(cdPerformance);
		else if (dbName.equals(ContentName.onMon))
			cdPerformanceReposMon.saveAndFlush(cdPerformance);
		else if (dbName.equals(ContentName.onHist))
			cdPerformanceReposHist.saveAndFlush(cdPerformance);
		else
			cdPerformanceRepos.saveAndFlush(cdPerformance);
		return this.findById(cdPerformance.getCdPerformanceId());
	}

	@Override
	public void delete(CdPerformance cdPerformance, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdPerformance.getCdPerformanceId());
		if (dbName.equals(ContentName.onDay)) {
			cdPerformanceReposDay.delete(cdPerformance);
			cdPerformanceReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdPerformanceReposMon.delete(cdPerformance);
			cdPerformanceReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdPerformanceReposHist.delete(cdPerformance);
			cdPerformanceReposHist.flush();
		} else {
			cdPerformanceRepos.delete(cdPerformance);
			cdPerformanceRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdPerformance> cdPerformance, TitaVo... titaVo) throws DBException {
		if (cdPerformance == null || cdPerformance.size() == 0)
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
		for (CdPerformance t : cdPerformance) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdPerformance = cdPerformanceReposDay.saveAll(cdPerformance);
			cdPerformanceReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdPerformance = cdPerformanceReposMon.saveAll(cdPerformance);
			cdPerformanceReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdPerformance = cdPerformanceReposHist.saveAll(cdPerformance);
			cdPerformanceReposHist.flush();
		} else {
			cdPerformance = cdPerformanceRepos.saveAll(cdPerformance);
			cdPerformanceRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdPerformance> cdPerformance, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdPerformance == null || cdPerformance.size() == 0)
			throw new DBException(6);

		for (CdPerformance t : cdPerformance)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdPerformance = cdPerformanceReposDay.saveAll(cdPerformance);
			cdPerformanceReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdPerformance = cdPerformanceReposMon.saveAll(cdPerformance);
			cdPerformanceReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdPerformance = cdPerformanceReposHist.saveAll(cdPerformance);
			cdPerformanceReposHist.flush();
		} else {
			cdPerformance = cdPerformanceRepos.saveAll(cdPerformance);
			cdPerformanceRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdPerformance> cdPerformance, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdPerformance == null || cdPerformance.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdPerformanceReposDay.deleteAll(cdPerformance);
			cdPerformanceReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdPerformanceReposMon.deleteAll(cdPerformance);
			cdPerformanceReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdPerformanceReposHist.deleteAll(cdPerformance);
			cdPerformanceReposHist.flush();
		} else {
			cdPerformanceRepos.deleteAll(cdPerformance);
			cdPerformanceRepos.flush();
		}
	}

}
