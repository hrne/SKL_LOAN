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
import com.st1.itx.db.domain.CdLandOffice;
import com.st1.itx.db.domain.CdLandOfficeId;
import com.st1.itx.db.repository.online.CdLandOfficeRepository;
import com.st1.itx.db.repository.day.CdLandOfficeRepositoryDay;
import com.st1.itx.db.repository.mon.CdLandOfficeRepositoryMon;
import com.st1.itx.db.repository.hist.CdLandOfficeRepositoryHist;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdLandOfficeService")
@Repository
public class CdLandOfficeServiceImpl extends ASpringJpaParm implements CdLandOfficeService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdLandOfficeRepository cdLandOfficeRepos;

	@Autowired
	private CdLandOfficeRepositoryDay cdLandOfficeReposDay;

	@Autowired
	private CdLandOfficeRepositoryMon cdLandOfficeReposMon;

	@Autowired
	private CdLandOfficeRepositoryHist cdLandOfficeReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdLandOfficeRepos);
		org.junit.Assert.assertNotNull(cdLandOfficeReposDay);
		org.junit.Assert.assertNotNull(cdLandOfficeReposMon);
		org.junit.Assert.assertNotNull(cdLandOfficeReposHist);
	}

	@Override
	public CdLandOffice findById(CdLandOfficeId cdLandOfficeId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + cdLandOfficeId);
		Optional<CdLandOffice> cdLandOffice = null;
		if (dbName.equals(ContentName.onDay))
			cdLandOffice = cdLandOfficeReposDay.findById(cdLandOfficeId);
		else if (dbName.equals(ContentName.onMon))
			cdLandOffice = cdLandOfficeReposMon.findById(cdLandOfficeId);
		else if (dbName.equals(ContentName.onHist))
			cdLandOffice = cdLandOfficeReposHist.findById(cdLandOfficeId);
		else
			cdLandOffice = cdLandOfficeRepos.findById(cdLandOfficeId);
		CdLandOffice obj = cdLandOffice.isPresent() ? cdLandOffice.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdLandOffice> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdLandOffice> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LandOfficeCode", "RecWord"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LandOfficeCode", "RecWord"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdLandOfficeReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdLandOfficeReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdLandOfficeReposHist.findAll(pageable);
		else
			slice = cdLandOfficeRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdLandOffice> findLandOfficeCode(String landOfficeCode_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdLandOffice> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findLandOfficeCode " + dbName + " : " + "landOfficeCode_0 : " + landOfficeCode_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdLandOfficeReposDay.findAllByLandOfficeCodeIsOrderByRecWordAsc(landOfficeCode_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdLandOfficeReposMon.findAllByLandOfficeCodeIsOrderByRecWordAsc(landOfficeCode_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdLandOfficeReposHist.findAllByLandOfficeCodeIsOrderByRecWordAsc(landOfficeCode_0, pageable);
		else
			slice = cdLandOfficeRepos.findAllByLandOfficeCodeIsOrderByRecWordAsc(landOfficeCode_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdLandOffice findRecWordFirst(String landOfficeCode_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findRecWordFirst " + dbName + " : " + "landOfficeCode_0 : " + landOfficeCode_0);
		Optional<CdLandOffice> cdLandOfficeT = null;
		if (dbName.equals(ContentName.onDay))
			cdLandOfficeT = cdLandOfficeReposDay.findTopByLandOfficeCodeIsOrderByRecWordDesc(landOfficeCode_0);
		else if (dbName.equals(ContentName.onMon))
			cdLandOfficeT = cdLandOfficeReposMon.findTopByLandOfficeCodeIsOrderByRecWordDesc(landOfficeCode_0);
		else if (dbName.equals(ContentName.onHist))
			cdLandOfficeT = cdLandOfficeReposHist.findTopByLandOfficeCodeIsOrderByRecWordDesc(landOfficeCode_0);
		else
			cdLandOfficeT = cdLandOfficeRepos.findTopByLandOfficeCodeIsOrderByRecWordDesc(landOfficeCode_0);

		return cdLandOfficeT.isPresent() ? cdLandOfficeT.get() : null;
	}

	@Override
	public CdLandOffice holdById(CdLandOfficeId cdLandOfficeId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdLandOfficeId);
		Optional<CdLandOffice> cdLandOffice = null;
		if (dbName.equals(ContentName.onDay))
			cdLandOffice = cdLandOfficeReposDay.findByCdLandOfficeId(cdLandOfficeId);
		else if (dbName.equals(ContentName.onMon))
			cdLandOffice = cdLandOfficeReposMon.findByCdLandOfficeId(cdLandOfficeId);
		else if (dbName.equals(ContentName.onHist))
			cdLandOffice = cdLandOfficeReposHist.findByCdLandOfficeId(cdLandOfficeId);
		else
			cdLandOffice = cdLandOfficeRepos.findByCdLandOfficeId(cdLandOfficeId);
		return cdLandOffice.isPresent() ? cdLandOffice.get() : null;
	}

	@Override
	public CdLandOffice holdById(CdLandOffice cdLandOffice, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdLandOffice.getCdLandOfficeId());
		Optional<CdLandOffice> cdLandOfficeT = null;
		if (dbName.equals(ContentName.onDay))
			cdLandOfficeT = cdLandOfficeReposDay.findByCdLandOfficeId(cdLandOffice.getCdLandOfficeId());
		else if (dbName.equals(ContentName.onMon))
			cdLandOfficeT = cdLandOfficeReposMon.findByCdLandOfficeId(cdLandOffice.getCdLandOfficeId());
		else if (dbName.equals(ContentName.onHist))
			cdLandOfficeT = cdLandOfficeReposHist.findByCdLandOfficeId(cdLandOffice.getCdLandOfficeId());
		else
			cdLandOfficeT = cdLandOfficeRepos.findByCdLandOfficeId(cdLandOffice.getCdLandOfficeId());
		return cdLandOfficeT.isPresent() ? cdLandOfficeT.get() : null;
	}

	@Override
	public CdLandOffice insert(CdLandOffice cdLandOffice, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdLandOffice.getCdLandOfficeId());
		if (this.findById(cdLandOffice.getCdLandOfficeId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdLandOffice.setCreateEmpNo(empNot);

		if (cdLandOffice.getLastUpdateEmpNo() == null || cdLandOffice.getLastUpdateEmpNo().isEmpty())
			cdLandOffice.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdLandOfficeReposDay.saveAndFlush(cdLandOffice);
		else if (dbName.equals(ContentName.onMon))
			return cdLandOfficeReposMon.saveAndFlush(cdLandOffice);
		else if (dbName.equals(ContentName.onHist))
			return cdLandOfficeReposHist.saveAndFlush(cdLandOffice);
		else
			return cdLandOfficeRepos.saveAndFlush(cdLandOffice);
	}

	@Override
	public CdLandOffice update(CdLandOffice cdLandOffice, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdLandOffice.getCdLandOfficeId());
		if (!empNot.isEmpty())
			cdLandOffice.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdLandOfficeReposDay.saveAndFlush(cdLandOffice);
		else if (dbName.equals(ContentName.onMon))
			return cdLandOfficeReposMon.saveAndFlush(cdLandOffice);
		else if (dbName.equals(ContentName.onHist))
			return cdLandOfficeReposHist.saveAndFlush(cdLandOffice);
		else
			return cdLandOfficeRepos.saveAndFlush(cdLandOffice);
	}

	@Override
	public CdLandOffice update2(CdLandOffice cdLandOffice, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdLandOffice.getCdLandOfficeId());
		if (!empNot.isEmpty())
			cdLandOffice.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdLandOfficeReposDay.saveAndFlush(cdLandOffice);
		else if (dbName.equals(ContentName.onMon))
			cdLandOfficeReposMon.saveAndFlush(cdLandOffice);
		else if (dbName.equals(ContentName.onHist))
			cdLandOfficeReposHist.saveAndFlush(cdLandOffice);
		else
			cdLandOfficeRepos.saveAndFlush(cdLandOffice);
		return this.findById(cdLandOffice.getCdLandOfficeId());
	}

	@Override
	public void delete(CdLandOffice cdLandOffice, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdLandOffice.getCdLandOfficeId());
		if (dbName.equals(ContentName.onDay)) {
			cdLandOfficeReposDay.delete(cdLandOffice);
			cdLandOfficeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdLandOfficeReposMon.delete(cdLandOffice);
			cdLandOfficeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdLandOfficeReposHist.delete(cdLandOffice);
			cdLandOfficeReposHist.flush();
		} else {
			cdLandOfficeRepos.delete(cdLandOffice);
			cdLandOfficeRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdLandOffice> cdLandOffice, TitaVo... titaVo) throws DBException {
		if (cdLandOffice == null || cdLandOffice.size() == 0)
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
		for (CdLandOffice t : cdLandOffice) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdLandOffice = cdLandOfficeReposDay.saveAll(cdLandOffice);
			cdLandOfficeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdLandOffice = cdLandOfficeReposMon.saveAll(cdLandOffice);
			cdLandOfficeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdLandOffice = cdLandOfficeReposHist.saveAll(cdLandOffice);
			cdLandOfficeReposHist.flush();
		} else {
			cdLandOffice = cdLandOfficeRepos.saveAll(cdLandOffice);
			cdLandOfficeRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdLandOffice> cdLandOffice, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdLandOffice == null || cdLandOffice.size() == 0)
			throw new DBException(6);

		for (CdLandOffice t : cdLandOffice)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdLandOffice = cdLandOfficeReposDay.saveAll(cdLandOffice);
			cdLandOfficeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdLandOffice = cdLandOfficeReposMon.saveAll(cdLandOffice);
			cdLandOfficeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdLandOffice = cdLandOfficeReposHist.saveAll(cdLandOffice);
			cdLandOfficeReposHist.flush();
		} else {
			cdLandOffice = cdLandOfficeRepos.saveAll(cdLandOffice);
			cdLandOfficeRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdLandOffice> cdLandOffice, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdLandOffice == null || cdLandOffice.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdLandOfficeReposDay.deleteAll(cdLandOffice);
			cdLandOfficeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdLandOfficeReposMon.deleteAll(cdLandOffice);
			cdLandOfficeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdLandOfficeReposHist.deleteAll(cdLandOffice);
			cdLandOfficeReposHist.flush();
		} else {
			cdLandOfficeRepos.deleteAll(cdLandOffice);
			cdLandOfficeRepos.flush();
		}
	}

}
