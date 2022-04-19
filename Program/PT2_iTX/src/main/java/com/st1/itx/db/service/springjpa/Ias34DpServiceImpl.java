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
import com.st1.itx.db.domain.Ias34Dp;
import com.st1.itx.db.domain.Ias34DpId;
import com.st1.itx.db.repository.online.Ias34DpRepository;
import com.st1.itx.db.repository.day.Ias34DpRepositoryDay;
import com.st1.itx.db.repository.mon.Ias34DpRepositoryMon;
import com.st1.itx.db.repository.hist.Ias34DpRepositoryHist;
import com.st1.itx.db.service.Ias34DpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ias34DpService")
@Repository
public class Ias34DpServiceImpl extends ASpringJpaParm implements Ias34DpService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Ias34DpRepository ias34DpRepos;

	@Autowired
	private Ias34DpRepositoryDay ias34DpReposDay;

	@Autowired
	private Ias34DpRepositoryMon ias34DpReposMon;

	@Autowired
	private Ias34DpRepositoryHist ias34DpReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(ias34DpRepos);
		org.junit.Assert.assertNotNull(ias34DpReposDay);
		org.junit.Assert.assertNotNull(ias34DpReposMon);
		org.junit.Assert.assertNotNull(ias34DpReposHist);
	}

	@Override
	public Ias34Dp findById(Ias34DpId ias34DpId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + ias34DpId);
		Optional<Ias34Dp> ias34Dp = null;
		if (dbName.equals(ContentName.onDay))
			ias34Dp = ias34DpReposDay.findById(ias34DpId);
		else if (dbName.equals(ContentName.onMon))
			ias34Dp = ias34DpReposMon.findById(ias34DpId);
		else if (dbName.equals(ContentName.onHist))
			ias34Dp = ias34DpReposHist.findById(ias34DpId);
		else
			ias34Dp = ias34DpRepos.findById(ias34DpId);
		Ias34Dp obj = ias34Dp.isPresent() ? ias34Dp.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<Ias34Dp> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<Ias34Dp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = ias34DpReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = ias34DpReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = ias34DpReposHist.findAll(pageable);
		else
			slice = ias34DpRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Ias34Dp holdById(Ias34DpId ias34DpId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + ias34DpId);
		Optional<Ias34Dp> ias34Dp = null;
		if (dbName.equals(ContentName.onDay))
			ias34Dp = ias34DpReposDay.findByIas34DpId(ias34DpId);
		else if (dbName.equals(ContentName.onMon))
			ias34Dp = ias34DpReposMon.findByIas34DpId(ias34DpId);
		else if (dbName.equals(ContentName.onHist))
			ias34Dp = ias34DpReposHist.findByIas34DpId(ias34DpId);
		else
			ias34Dp = ias34DpRepos.findByIas34DpId(ias34DpId);
		return ias34Dp.isPresent() ? ias34Dp.get() : null;
	}

	@Override
	public Ias34Dp holdById(Ias34Dp ias34Dp, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + ias34Dp.getIas34DpId());
		Optional<Ias34Dp> ias34DpT = null;
		if (dbName.equals(ContentName.onDay))
			ias34DpT = ias34DpReposDay.findByIas34DpId(ias34Dp.getIas34DpId());
		else if (dbName.equals(ContentName.onMon))
			ias34DpT = ias34DpReposMon.findByIas34DpId(ias34Dp.getIas34DpId());
		else if (dbName.equals(ContentName.onHist))
			ias34DpT = ias34DpReposHist.findByIas34DpId(ias34Dp.getIas34DpId());
		else
			ias34DpT = ias34DpRepos.findByIas34DpId(ias34Dp.getIas34DpId());
		return ias34DpT.isPresent() ? ias34DpT.get() : null;
	}

	@Override
	public Ias34Dp insert(Ias34Dp ias34Dp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + ias34Dp.getIas34DpId());
		if (this.findById(ias34Dp.getIas34DpId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			ias34Dp.setCreateEmpNo(empNot);

		if (ias34Dp.getLastUpdateEmpNo() == null || ias34Dp.getLastUpdateEmpNo().isEmpty())
			ias34Dp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return ias34DpReposDay.saveAndFlush(ias34Dp);
		else if (dbName.equals(ContentName.onMon))
			return ias34DpReposMon.saveAndFlush(ias34Dp);
		else if (dbName.equals(ContentName.onHist))
			return ias34DpReposHist.saveAndFlush(ias34Dp);
		else
			return ias34DpRepos.saveAndFlush(ias34Dp);
	}

	@Override
	public Ias34Dp update(Ias34Dp ias34Dp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + ias34Dp.getIas34DpId());
		if (!empNot.isEmpty())
			ias34Dp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return ias34DpReposDay.saveAndFlush(ias34Dp);
		else if (dbName.equals(ContentName.onMon))
			return ias34DpReposMon.saveAndFlush(ias34Dp);
		else if (dbName.equals(ContentName.onHist))
			return ias34DpReposHist.saveAndFlush(ias34Dp);
		else
			return ias34DpRepos.saveAndFlush(ias34Dp);
	}

	@Override
	public Ias34Dp update2(Ias34Dp ias34Dp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + ias34Dp.getIas34DpId());
		if (!empNot.isEmpty())
			ias34Dp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			ias34DpReposDay.saveAndFlush(ias34Dp);
		else if (dbName.equals(ContentName.onMon))
			ias34DpReposMon.saveAndFlush(ias34Dp);
		else if (dbName.equals(ContentName.onHist))
			ias34DpReposHist.saveAndFlush(ias34Dp);
		else
			ias34DpRepos.saveAndFlush(ias34Dp);
		return this.findById(ias34Dp.getIas34DpId());
	}

	@Override
	public void delete(Ias34Dp ias34Dp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + ias34Dp.getIas34DpId());
		if (dbName.equals(ContentName.onDay)) {
			ias34DpReposDay.delete(ias34Dp);
			ias34DpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias34DpReposMon.delete(ias34Dp);
			ias34DpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias34DpReposHist.delete(ias34Dp);
			ias34DpReposHist.flush();
		} else {
			ias34DpRepos.delete(ias34Dp);
			ias34DpRepos.flush();
		}
	}

	@Override
	public void insertAll(List<Ias34Dp> ias34Dp, TitaVo... titaVo) throws DBException {
		if (ias34Dp == null || ias34Dp.size() == 0)
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
		for (Ias34Dp t : ias34Dp) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			ias34Dp = ias34DpReposDay.saveAll(ias34Dp);
			ias34DpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias34Dp = ias34DpReposMon.saveAll(ias34Dp);
			ias34DpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias34Dp = ias34DpReposHist.saveAll(ias34Dp);
			ias34DpReposHist.flush();
		} else {
			ias34Dp = ias34DpRepos.saveAll(ias34Dp);
			ias34DpRepos.flush();
		}
	}

	@Override
	public void updateAll(List<Ias34Dp> ias34Dp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (ias34Dp == null || ias34Dp.size() == 0)
			throw new DBException(6);

		for (Ias34Dp t : ias34Dp)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			ias34Dp = ias34DpReposDay.saveAll(ias34Dp);
			ias34DpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias34Dp = ias34DpReposMon.saveAll(ias34Dp);
			ias34DpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias34Dp = ias34DpReposHist.saveAll(ias34Dp);
			ias34DpReposHist.flush();
		} else {
			ias34Dp = ias34DpRepos.saveAll(ias34Dp);
			ias34DpRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<Ias34Dp> ias34Dp, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (ias34Dp == null || ias34Dp.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			ias34DpReposDay.deleteAll(ias34Dp);
			ias34DpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias34DpReposMon.deleteAll(ias34Dp);
			ias34DpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias34DpReposHist.deleteAll(ias34Dp);
			ias34DpReposHist.flush();
		} else {
			ias34DpRepos.deleteAll(ias34Dp);
			ias34DpRepos.flush();
		}
	}

	@Override
	public void Usp_L7_Ias34Dp_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			ias34DpReposDay.uspL7Ias34dpUpd(TBSDYF, EmpNo, NewAcFg);
		else if (dbName.equals(ContentName.onMon))
			ias34DpReposMon.uspL7Ias34dpUpd(TBSDYF, EmpNo, NewAcFg);
		else if (dbName.equals(ContentName.onHist))
			ias34DpReposHist.uspL7Ias34dpUpd(TBSDYF, EmpNo, NewAcFg);
		else
			ias34DpRepos.uspL7Ias34dpUpd(TBSDYF, EmpNo, NewAcFg);
	}

}
