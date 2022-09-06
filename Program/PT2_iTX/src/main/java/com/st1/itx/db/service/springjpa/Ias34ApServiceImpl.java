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
import com.st1.itx.db.domain.Ias34Ap;
import com.st1.itx.db.domain.Ias34ApId;
import com.st1.itx.db.repository.online.Ias34ApRepository;
import com.st1.itx.db.repository.day.Ias34ApRepositoryDay;
import com.st1.itx.db.repository.mon.Ias34ApRepositoryMon;
import com.st1.itx.db.repository.hist.Ias34ApRepositoryHist;
import com.st1.itx.db.service.Ias34ApService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ias34ApService")
@Repository
public class Ias34ApServiceImpl extends ASpringJpaParm implements Ias34ApService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Ias34ApRepository ias34ApRepos;

	@Autowired
	private Ias34ApRepositoryDay ias34ApReposDay;

	@Autowired
	private Ias34ApRepositoryMon ias34ApReposMon;

	@Autowired
	private Ias34ApRepositoryHist ias34ApReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(ias34ApRepos);
		org.junit.Assert.assertNotNull(ias34ApReposDay);
		org.junit.Assert.assertNotNull(ias34ApReposMon);
		org.junit.Assert.assertNotNull(ias34ApReposHist);
	}

	@Override
	public Ias34Ap findById(Ias34ApId ias34ApId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + ias34ApId);
		Optional<Ias34Ap> ias34Ap = null;
		if (dbName.equals(ContentName.onDay))
			ias34Ap = ias34ApReposDay.findById(ias34ApId);
		else if (dbName.equals(ContentName.onMon))
			ias34Ap = ias34ApReposMon.findById(ias34ApId);
		else if (dbName.equals(ContentName.onHist))
			ias34Ap = ias34ApReposHist.findById(ias34ApId);
		else
			ias34Ap = ias34ApRepos.findById(ias34ApId);
		Ias34Ap obj = ias34Ap.isPresent() ? ias34Ap.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<Ias34Ap> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<Ias34Ap> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = ias34ApReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = ias34ApReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = ias34ApReposHist.findAll(pageable);
		else
			slice = ias34ApRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<Ias34Ap> dataEq(int custNo_0, int facmNo_1, int dataYM_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<Ias34Ap> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("dataEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " dataYM_2 : " + dataYM_2);
		if (dbName.equals(ContentName.onDay))
			slice = ias34ApReposDay.findAllByCustNoIsAndFacmNoIsAndDataYMIs(custNo_0, facmNo_1, dataYM_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = ias34ApReposMon.findAllByCustNoIsAndFacmNoIsAndDataYMIs(custNo_0, facmNo_1, dataYM_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = ias34ApReposHist.findAllByCustNoIsAndFacmNoIsAndDataYMIs(custNo_0, facmNo_1, dataYM_2, pageable);
		else
			slice = ias34ApRepos.findAllByCustNoIsAndFacmNoIsAndDataYMIs(custNo_0, facmNo_1, dataYM_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Ias34Ap holdById(Ias34ApId ias34ApId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + ias34ApId);
		Optional<Ias34Ap> ias34Ap = null;
		if (dbName.equals(ContentName.onDay))
			ias34Ap = ias34ApReposDay.findByIas34ApId(ias34ApId);
		else if (dbName.equals(ContentName.onMon))
			ias34Ap = ias34ApReposMon.findByIas34ApId(ias34ApId);
		else if (dbName.equals(ContentName.onHist))
			ias34Ap = ias34ApReposHist.findByIas34ApId(ias34ApId);
		else
			ias34Ap = ias34ApRepos.findByIas34ApId(ias34ApId);
		return ias34Ap.isPresent() ? ias34Ap.get() : null;
	}

	@Override
	public Ias34Ap holdById(Ias34Ap ias34Ap, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + ias34Ap.getIas34ApId());
		Optional<Ias34Ap> ias34ApT = null;
		if (dbName.equals(ContentName.onDay))
			ias34ApT = ias34ApReposDay.findByIas34ApId(ias34Ap.getIas34ApId());
		else if (dbName.equals(ContentName.onMon))
			ias34ApT = ias34ApReposMon.findByIas34ApId(ias34Ap.getIas34ApId());
		else if (dbName.equals(ContentName.onHist))
			ias34ApT = ias34ApReposHist.findByIas34ApId(ias34Ap.getIas34ApId());
		else
			ias34ApT = ias34ApRepos.findByIas34ApId(ias34Ap.getIas34ApId());
		return ias34ApT.isPresent() ? ias34ApT.get() : null;
	}

	@Override
	public Ias34Ap insert(Ias34Ap ias34Ap, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + ias34Ap.getIas34ApId());
		if (this.findById(ias34Ap.getIas34ApId(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			ias34Ap.setCreateEmpNo(empNot);

		if (ias34Ap.getLastUpdateEmpNo() == null || ias34Ap.getLastUpdateEmpNo().isEmpty())
			ias34Ap.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return ias34ApReposDay.saveAndFlush(ias34Ap);
		else if (dbName.equals(ContentName.onMon))
			return ias34ApReposMon.saveAndFlush(ias34Ap);
		else if (dbName.equals(ContentName.onHist))
			return ias34ApReposHist.saveAndFlush(ias34Ap);
		else
			return ias34ApRepos.saveAndFlush(ias34Ap);
	}

	@Override
	public Ias34Ap update(Ias34Ap ias34Ap, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + ias34Ap.getIas34ApId());
		if (!empNot.isEmpty())
			ias34Ap.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return ias34ApReposDay.saveAndFlush(ias34Ap);
		else if (dbName.equals(ContentName.onMon))
			return ias34ApReposMon.saveAndFlush(ias34Ap);
		else if (dbName.equals(ContentName.onHist))
			return ias34ApReposHist.saveAndFlush(ias34Ap);
		else
			return ias34ApRepos.saveAndFlush(ias34Ap);
	}

	@Override
	public Ias34Ap update2(Ias34Ap ias34Ap, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + ias34Ap.getIas34ApId());
		if (!empNot.isEmpty())
			ias34Ap.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			ias34ApReposDay.saveAndFlush(ias34Ap);
		else if (dbName.equals(ContentName.onMon))
			ias34ApReposMon.saveAndFlush(ias34Ap);
		else if (dbName.equals(ContentName.onHist))
			ias34ApReposHist.saveAndFlush(ias34Ap);
		else
			ias34ApRepos.saveAndFlush(ias34Ap);
		return this.findById(ias34Ap.getIas34ApId());
	}

	@Override
	public void delete(Ias34Ap ias34Ap, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + ias34Ap.getIas34ApId());
		if (dbName.equals(ContentName.onDay)) {
			ias34ApReposDay.delete(ias34Ap);
			ias34ApReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias34ApReposMon.delete(ias34Ap);
			ias34ApReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias34ApReposHist.delete(ias34Ap);
			ias34ApReposHist.flush();
		} else {
			ias34ApRepos.delete(ias34Ap);
			ias34ApRepos.flush();
		}
	}

	@Override
	public void insertAll(List<Ias34Ap> ias34Ap, TitaVo... titaVo) throws DBException {
		if (ias34Ap == null || ias34Ap.size() == 0)
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
		for (Ias34Ap t : ias34Ap) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			ias34Ap = ias34ApReposDay.saveAll(ias34Ap);
			ias34ApReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias34Ap = ias34ApReposMon.saveAll(ias34Ap);
			ias34ApReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias34Ap = ias34ApReposHist.saveAll(ias34Ap);
			ias34ApReposHist.flush();
		} else {
			ias34Ap = ias34ApRepos.saveAll(ias34Ap);
			ias34ApRepos.flush();
		}
	}

	@Override
	public void updateAll(List<Ias34Ap> ias34Ap, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (ias34Ap == null || ias34Ap.size() == 0)
			throw new DBException(6);

		for (Ias34Ap t : ias34Ap)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			ias34Ap = ias34ApReposDay.saveAll(ias34Ap);
			ias34ApReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias34Ap = ias34ApReposMon.saveAll(ias34Ap);
			ias34ApReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias34Ap = ias34ApReposHist.saveAll(ias34Ap);
			ias34ApReposHist.flush();
		} else {
			ias34Ap = ias34ApRepos.saveAll(ias34Ap);
			ias34ApRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<Ias34Ap> ias34Ap, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (ias34Ap == null || ias34Ap.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			ias34ApReposDay.deleteAll(ias34Ap);
			ias34ApReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias34ApReposMon.deleteAll(ias34Ap);
			ias34ApReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias34ApReposHist.deleteAll(ias34Ap);
			ias34ApReposHist.flush();
		} else {
			ias34ApRepos.deleteAll(ias34Ap);
			ias34ApRepos.flush();
		}
	}

	@Override
	public void Usp_L7_Ias34Ap_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			ias34ApReposDay.uspL7Ias34apUpd(TBSDYF, EmpNo, NewAcFg);
		else if (dbName.equals(ContentName.onMon))
			ias34ApReposMon.uspL7Ias34apUpd(TBSDYF, EmpNo, NewAcFg);
		else if (dbName.equals(ContentName.onHist))
			ias34ApReposHist.uspL7Ias34apUpd(TBSDYF, EmpNo, NewAcFg);
		else
			ias34ApRepos.uspL7Ias34apUpd(TBSDYF, EmpNo, NewAcFg);
	}

}
