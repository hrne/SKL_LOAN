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
import com.st1.itx.db.domain.CdLoanNotYet;
import com.st1.itx.db.repository.online.CdLoanNotYetRepository;
import com.st1.itx.db.repository.day.CdLoanNotYetRepositoryDay;
import com.st1.itx.db.repository.mon.CdLoanNotYetRepositoryMon;
import com.st1.itx.db.repository.hist.CdLoanNotYetRepositoryHist;
import com.st1.itx.db.service.CdLoanNotYetService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdLoanNotYetService")
@Repository
public class CdLoanNotYetServiceImpl extends ASpringJpaParm implements CdLoanNotYetService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdLoanNotYetRepository cdLoanNotYetRepos;

	@Autowired
	private CdLoanNotYetRepositoryDay cdLoanNotYetReposDay;

	@Autowired
	private CdLoanNotYetRepositoryMon cdLoanNotYetReposMon;

	@Autowired
	private CdLoanNotYetRepositoryHist cdLoanNotYetReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdLoanNotYetRepos);
		org.junit.Assert.assertNotNull(cdLoanNotYetReposDay);
		org.junit.Assert.assertNotNull(cdLoanNotYetReposMon);
		org.junit.Assert.assertNotNull(cdLoanNotYetReposHist);
	}

	@Override
	public CdLoanNotYet findById(String notYetCode, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + notYetCode);
		Optional<CdLoanNotYet> cdLoanNotYet = null;
		if (dbName.equals(ContentName.onDay))
			cdLoanNotYet = cdLoanNotYetReposDay.findById(notYetCode);
		else if (dbName.equals(ContentName.onMon))
			cdLoanNotYet = cdLoanNotYetReposMon.findById(notYetCode);
		else if (dbName.equals(ContentName.onHist))
			cdLoanNotYet = cdLoanNotYetReposHist.findById(notYetCode);
		else
			cdLoanNotYet = cdLoanNotYetRepos.findById(notYetCode);
		CdLoanNotYet obj = cdLoanNotYet.isPresent() ? cdLoanNotYet.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdLoanNotYet> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdLoanNotYet> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "NotYetCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "NotYetCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdLoanNotYetReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdLoanNotYetReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdLoanNotYetReposHist.findAll(pageable);
		else
			slice = cdLoanNotYetRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdLoanNotYet> codeLike(String notYetCode_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdLoanNotYet> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("codeLike " + dbName + " : " + "notYetCode_0 : " + notYetCode_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdLoanNotYetReposDay.findAllByNotYetCodeLikeOrderByNotYetCodeAsc(notYetCode_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdLoanNotYetReposMon.findAllByNotYetCodeLikeOrderByNotYetCodeAsc(notYetCode_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdLoanNotYetReposHist.findAllByNotYetCodeLikeOrderByNotYetCodeAsc(notYetCode_0, pageable);
		else
			slice = cdLoanNotYetRepos.findAllByNotYetCodeLikeOrderByNotYetCodeAsc(notYetCode_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdLoanNotYet holdById(String notYetCode, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + notYetCode);
		Optional<CdLoanNotYet> cdLoanNotYet = null;
		if (dbName.equals(ContentName.onDay))
			cdLoanNotYet = cdLoanNotYetReposDay.findByNotYetCode(notYetCode);
		else if (dbName.equals(ContentName.onMon))
			cdLoanNotYet = cdLoanNotYetReposMon.findByNotYetCode(notYetCode);
		else if (dbName.equals(ContentName.onHist))
			cdLoanNotYet = cdLoanNotYetReposHist.findByNotYetCode(notYetCode);
		else
			cdLoanNotYet = cdLoanNotYetRepos.findByNotYetCode(notYetCode);
		return cdLoanNotYet.isPresent() ? cdLoanNotYet.get() : null;
	}

	@Override
	public CdLoanNotYet holdById(CdLoanNotYet cdLoanNotYet, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdLoanNotYet.getNotYetCode());
		Optional<CdLoanNotYet> cdLoanNotYetT = null;
		if (dbName.equals(ContentName.onDay))
			cdLoanNotYetT = cdLoanNotYetReposDay.findByNotYetCode(cdLoanNotYet.getNotYetCode());
		else if (dbName.equals(ContentName.onMon))
			cdLoanNotYetT = cdLoanNotYetReposMon.findByNotYetCode(cdLoanNotYet.getNotYetCode());
		else if (dbName.equals(ContentName.onHist))
			cdLoanNotYetT = cdLoanNotYetReposHist.findByNotYetCode(cdLoanNotYet.getNotYetCode());
		else
			cdLoanNotYetT = cdLoanNotYetRepos.findByNotYetCode(cdLoanNotYet.getNotYetCode());
		return cdLoanNotYetT.isPresent() ? cdLoanNotYetT.get() : null;
	}

	@Override
	public CdLoanNotYet insert(CdLoanNotYet cdLoanNotYet, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdLoanNotYet.getNotYetCode());
		if (this.findById(cdLoanNotYet.getNotYetCode()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdLoanNotYet.setCreateEmpNo(empNot);

		if (cdLoanNotYet.getLastUpdateEmpNo() == null || cdLoanNotYet.getLastUpdateEmpNo().isEmpty())
			cdLoanNotYet.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdLoanNotYetReposDay.saveAndFlush(cdLoanNotYet);
		else if (dbName.equals(ContentName.onMon))
			return cdLoanNotYetReposMon.saveAndFlush(cdLoanNotYet);
		else if (dbName.equals(ContentName.onHist))
			return cdLoanNotYetReposHist.saveAndFlush(cdLoanNotYet);
		else
			return cdLoanNotYetRepos.saveAndFlush(cdLoanNotYet);
	}

	@Override
	public CdLoanNotYet update(CdLoanNotYet cdLoanNotYet, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdLoanNotYet.getNotYetCode());
		if (!empNot.isEmpty())
			cdLoanNotYet.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdLoanNotYetReposDay.saveAndFlush(cdLoanNotYet);
		else if (dbName.equals(ContentName.onMon))
			return cdLoanNotYetReposMon.saveAndFlush(cdLoanNotYet);
		else if (dbName.equals(ContentName.onHist))
			return cdLoanNotYetReposHist.saveAndFlush(cdLoanNotYet);
		else
			return cdLoanNotYetRepos.saveAndFlush(cdLoanNotYet);
	}

	@Override
	public CdLoanNotYet update2(CdLoanNotYet cdLoanNotYet, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdLoanNotYet.getNotYetCode());
		if (!empNot.isEmpty())
			cdLoanNotYet.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdLoanNotYetReposDay.saveAndFlush(cdLoanNotYet);
		else if (dbName.equals(ContentName.onMon))
			cdLoanNotYetReposMon.saveAndFlush(cdLoanNotYet);
		else if (dbName.equals(ContentName.onHist))
			cdLoanNotYetReposHist.saveAndFlush(cdLoanNotYet);
		else
			cdLoanNotYetRepos.saveAndFlush(cdLoanNotYet);
		return this.findById(cdLoanNotYet.getNotYetCode());
	}

	@Override
	public void delete(CdLoanNotYet cdLoanNotYet, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdLoanNotYet.getNotYetCode());
		if (dbName.equals(ContentName.onDay)) {
			cdLoanNotYetReposDay.delete(cdLoanNotYet);
			cdLoanNotYetReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdLoanNotYetReposMon.delete(cdLoanNotYet);
			cdLoanNotYetReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdLoanNotYetReposHist.delete(cdLoanNotYet);
			cdLoanNotYetReposHist.flush();
		} else {
			cdLoanNotYetRepos.delete(cdLoanNotYet);
			cdLoanNotYetRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdLoanNotYet> cdLoanNotYet, TitaVo... titaVo) throws DBException {
		if (cdLoanNotYet == null || cdLoanNotYet.size() == 0)
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
		for (CdLoanNotYet t : cdLoanNotYet) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdLoanNotYet = cdLoanNotYetReposDay.saveAll(cdLoanNotYet);
			cdLoanNotYetReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdLoanNotYet = cdLoanNotYetReposMon.saveAll(cdLoanNotYet);
			cdLoanNotYetReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdLoanNotYet = cdLoanNotYetReposHist.saveAll(cdLoanNotYet);
			cdLoanNotYetReposHist.flush();
		} else {
			cdLoanNotYet = cdLoanNotYetRepos.saveAll(cdLoanNotYet);
			cdLoanNotYetRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdLoanNotYet> cdLoanNotYet, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdLoanNotYet == null || cdLoanNotYet.size() == 0)
			throw new DBException(6);

		for (CdLoanNotYet t : cdLoanNotYet)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdLoanNotYet = cdLoanNotYetReposDay.saveAll(cdLoanNotYet);
			cdLoanNotYetReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdLoanNotYet = cdLoanNotYetReposMon.saveAll(cdLoanNotYet);
			cdLoanNotYetReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdLoanNotYet = cdLoanNotYetReposHist.saveAll(cdLoanNotYet);
			cdLoanNotYetReposHist.flush();
		} else {
			cdLoanNotYet = cdLoanNotYetRepos.saveAll(cdLoanNotYet);
			cdLoanNotYetRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdLoanNotYet> cdLoanNotYet, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdLoanNotYet == null || cdLoanNotYet.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdLoanNotYetReposDay.deleteAll(cdLoanNotYet);
			cdLoanNotYetReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdLoanNotYetReposMon.deleteAll(cdLoanNotYet);
			cdLoanNotYetReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdLoanNotYetReposHist.deleteAll(cdLoanNotYet);
			cdLoanNotYetReposHist.flush();
		} else {
			cdLoanNotYetRepos.deleteAll(cdLoanNotYet);
			cdLoanNotYetRepos.flush();
		}
	}

}
