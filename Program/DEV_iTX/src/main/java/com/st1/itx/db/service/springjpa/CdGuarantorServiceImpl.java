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
import com.st1.itx.db.domain.CdGuarantor;
import com.st1.itx.db.repository.online.CdGuarantorRepository;
import com.st1.itx.db.repository.day.CdGuarantorRepositoryDay;
import com.st1.itx.db.repository.mon.CdGuarantorRepositoryMon;
import com.st1.itx.db.repository.hist.CdGuarantorRepositoryHist;
import com.st1.itx.db.service.CdGuarantorService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdGuarantorService")
@Repository
public class CdGuarantorServiceImpl extends ASpringJpaParm implements CdGuarantorService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdGuarantorRepository cdGuarantorRepos;

	@Autowired
	private CdGuarantorRepositoryDay cdGuarantorReposDay;

	@Autowired
	private CdGuarantorRepositoryMon cdGuarantorReposMon;

	@Autowired
	private CdGuarantorRepositoryHist cdGuarantorReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdGuarantorRepos);
		org.junit.Assert.assertNotNull(cdGuarantorReposDay);
		org.junit.Assert.assertNotNull(cdGuarantorReposMon);
		org.junit.Assert.assertNotNull(cdGuarantorReposHist);
	}

	@Override
	public CdGuarantor findById(String guaRelCode, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + guaRelCode);
		Optional<CdGuarantor> cdGuarantor = null;
		if (dbName.equals(ContentName.onDay))
			cdGuarantor = cdGuarantorReposDay.findById(guaRelCode);
		else if (dbName.equals(ContentName.onMon))
			cdGuarantor = cdGuarantorReposMon.findById(guaRelCode);
		else if (dbName.equals(ContentName.onHist))
			cdGuarantor = cdGuarantorReposHist.findById(guaRelCode);
		else
			cdGuarantor = cdGuarantorRepos.findById(guaRelCode);
		CdGuarantor obj = cdGuarantor.isPresent() ? cdGuarantor.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdGuarantor> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdGuarantor> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "GuaRelCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "GuaRelCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdGuarantorReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdGuarantorReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdGuarantorReposHist.findAll(pageable);
		else
			slice = cdGuarantorRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdGuarantor guaRelJcicFirst(String guaRelJcic_0, String guaRelJcic_1, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("guaRelJcicFirst " + dbName + " : " + "guaRelJcic_0 : " + guaRelJcic_0 + " guaRelJcic_1 : " + guaRelJcic_1);
		Optional<CdGuarantor> cdGuarantorT = null;
		if (dbName.equals(ContentName.onDay))
			cdGuarantorT = cdGuarantorReposDay.findTopByGuaRelJcicGreaterThanEqualAndGuaRelJcicLessThanEqual(guaRelJcic_0, guaRelJcic_1);
		else if (dbName.equals(ContentName.onMon))
			cdGuarantorT = cdGuarantorReposMon.findTopByGuaRelJcicGreaterThanEqualAndGuaRelJcicLessThanEqual(guaRelJcic_0, guaRelJcic_1);
		else if (dbName.equals(ContentName.onHist))
			cdGuarantorT = cdGuarantorReposHist.findTopByGuaRelJcicGreaterThanEqualAndGuaRelJcicLessThanEqual(guaRelJcic_0, guaRelJcic_1);
		else
			cdGuarantorT = cdGuarantorRepos.findTopByGuaRelJcicGreaterThanEqualAndGuaRelJcicLessThanEqual(guaRelJcic_0, guaRelJcic_1);

		return cdGuarantorT.isPresent() ? cdGuarantorT.get() : null;
	}

	@Override
	public Slice<CdGuarantor> findGuaRelCode(String guaRelCode_0, String guaRelCode_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdGuarantor> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findGuaRelCode " + dbName + " : " + "guaRelCode_0 : " + guaRelCode_0 + " guaRelCode_1 : " + guaRelCode_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdGuarantorReposDay.findAllByGuaRelCodeGreaterThanEqualAndGuaRelCodeLessThanEqualOrderByGuaRelCodeAsc(guaRelCode_0, guaRelCode_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdGuarantorReposMon.findAllByGuaRelCodeGreaterThanEqualAndGuaRelCodeLessThanEqualOrderByGuaRelCodeAsc(guaRelCode_0, guaRelCode_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdGuarantorReposHist.findAllByGuaRelCodeGreaterThanEqualAndGuaRelCodeLessThanEqualOrderByGuaRelCodeAsc(guaRelCode_0, guaRelCode_1, pageable);
		else
			slice = cdGuarantorRepos.findAllByGuaRelCodeGreaterThanEqualAndGuaRelCodeLessThanEqualOrderByGuaRelCodeAsc(guaRelCode_0, guaRelCode_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdGuarantor holdById(String guaRelCode, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + guaRelCode);
		Optional<CdGuarantor> cdGuarantor = null;
		if (dbName.equals(ContentName.onDay))
			cdGuarantor = cdGuarantorReposDay.findByGuaRelCode(guaRelCode);
		else if (dbName.equals(ContentName.onMon))
			cdGuarantor = cdGuarantorReposMon.findByGuaRelCode(guaRelCode);
		else if (dbName.equals(ContentName.onHist))
			cdGuarantor = cdGuarantorReposHist.findByGuaRelCode(guaRelCode);
		else
			cdGuarantor = cdGuarantorRepos.findByGuaRelCode(guaRelCode);
		return cdGuarantor.isPresent() ? cdGuarantor.get() : null;
	}

	@Override
	public CdGuarantor holdById(CdGuarantor cdGuarantor, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdGuarantor.getGuaRelCode());
		Optional<CdGuarantor> cdGuarantorT = null;
		if (dbName.equals(ContentName.onDay))
			cdGuarantorT = cdGuarantorReposDay.findByGuaRelCode(cdGuarantor.getGuaRelCode());
		else if (dbName.equals(ContentName.onMon))
			cdGuarantorT = cdGuarantorReposMon.findByGuaRelCode(cdGuarantor.getGuaRelCode());
		else if (dbName.equals(ContentName.onHist))
			cdGuarantorT = cdGuarantorReposHist.findByGuaRelCode(cdGuarantor.getGuaRelCode());
		else
			cdGuarantorT = cdGuarantorRepos.findByGuaRelCode(cdGuarantor.getGuaRelCode());
		return cdGuarantorT.isPresent() ? cdGuarantorT.get() : null;
	}

	@Override
	public CdGuarantor insert(CdGuarantor cdGuarantor, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + cdGuarantor.getGuaRelCode());
		if (this.findById(cdGuarantor.getGuaRelCode()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdGuarantor.setCreateEmpNo(empNot);

		if (cdGuarantor.getLastUpdateEmpNo() == null || cdGuarantor.getLastUpdateEmpNo().isEmpty())
			cdGuarantor.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdGuarantorReposDay.saveAndFlush(cdGuarantor);
		else if (dbName.equals(ContentName.onMon))
			return cdGuarantorReposMon.saveAndFlush(cdGuarantor);
		else if (dbName.equals(ContentName.onHist))
			return cdGuarantorReposHist.saveAndFlush(cdGuarantor);
		else
			return cdGuarantorRepos.saveAndFlush(cdGuarantor);
	}

	@Override
	public CdGuarantor update(CdGuarantor cdGuarantor, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + cdGuarantor.getGuaRelCode());
		if (!empNot.isEmpty())
			cdGuarantor.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdGuarantorReposDay.saveAndFlush(cdGuarantor);
		else if (dbName.equals(ContentName.onMon))
			return cdGuarantorReposMon.saveAndFlush(cdGuarantor);
		else if (dbName.equals(ContentName.onHist))
			return cdGuarantorReposHist.saveAndFlush(cdGuarantor);
		else
			return cdGuarantorRepos.saveAndFlush(cdGuarantor);
	}

	@Override
	public CdGuarantor update2(CdGuarantor cdGuarantor, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + cdGuarantor.getGuaRelCode());
		if (!empNot.isEmpty())
			cdGuarantor.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdGuarantorReposDay.saveAndFlush(cdGuarantor);
		else if (dbName.equals(ContentName.onMon))
			cdGuarantorReposMon.saveAndFlush(cdGuarantor);
		else if (dbName.equals(ContentName.onHist))
			cdGuarantorReposHist.saveAndFlush(cdGuarantor);
		else
			cdGuarantorRepos.saveAndFlush(cdGuarantor);
		return this.findById(cdGuarantor.getGuaRelCode());
	}

	@Override
	public void delete(CdGuarantor cdGuarantor, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdGuarantor.getGuaRelCode());
		if (dbName.equals(ContentName.onDay)) {
			cdGuarantorReposDay.delete(cdGuarantor);
			cdGuarantorReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdGuarantorReposMon.delete(cdGuarantor);
			cdGuarantorReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdGuarantorReposHist.delete(cdGuarantor);
			cdGuarantorReposHist.flush();
		} else {
			cdGuarantorRepos.delete(cdGuarantor);
			cdGuarantorRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdGuarantor> cdGuarantor, TitaVo... titaVo) throws DBException {
		if (cdGuarantor == null || cdGuarantor.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (CdGuarantor t : cdGuarantor) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdGuarantor = cdGuarantorReposDay.saveAll(cdGuarantor);
			cdGuarantorReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdGuarantor = cdGuarantorReposMon.saveAll(cdGuarantor);
			cdGuarantorReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdGuarantor = cdGuarantorReposHist.saveAll(cdGuarantor);
			cdGuarantorReposHist.flush();
		} else {
			cdGuarantor = cdGuarantorRepos.saveAll(cdGuarantor);
			cdGuarantorRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdGuarantor> cdGuarantor, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (cdGuarantor == null || cdGuarantor.size() == 0)
			throw new DBException(6);

		for (CdGuarantor t : cdGuarantor)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdGuarantor = cdGuarantorReposDay.saveAll(cdGuarantor);
			cdGuarantorReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdGuarantor = cdGuarantorReposMon.saveAll(cdGuarantor);
			cdGuarantorReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdGuarantor = cdGuarantorReposHist.saveAll(cdGuarantor);
			cdGuarantorReposHist.flush();
		} else {
			cdGuarantor = cdGuarantorRepos.saveAll(cdGuarantor);
			cdGuarantorRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdGuarantor> cdGuarantor, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdGuarantor == null || cdGuarantor.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdGuarantorReposDay.deleteAll(cdGuarantor);
			cdGuarantorReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdGuarantorReposMon.deleteAll(cdGuarantor);
			cdGuarantorReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdGuarantorReposHist.deleteAll(cdGuarantor);
			cdGuarantorReposHist.flush();
		} else {
			cdGuarantorRepos.deleteAll(cdGuarantor);
			cdGuarantorRepos.flush();
		}
	}

}
