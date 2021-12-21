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
import com.st1.itx.db.domain.CdCl;
import com.st1.itx.db.domain.CdClId;
import com.st1.itx.db.repository.online.CdClRepository;
import com.st1.itx.db.repository.day.CdClRepositoryDay;
import com.st1.itx.db.repository.mon.CdClRepositoryMon;
import com.st1.itx.db.repository.hist.CdClRepositoryHist;
import com.st1.itx.db.service.CdClService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdClService")
@Repository
public class CdClServiceImpl extends ASpringJpaParm implements CdClService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdClRepository cdClRepos;

	@Autowired
	private CdClRepositoryDay cdClReposDay;

	@Autowired
	private CdClRepositoryMon cdClReposMon;

	@Autowired
	private CdClRepositoryHist cdClReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdClRepos);
		org.junit.Assert.assertNotNull(cdClReposDay);
		org.junit.Assert.assertNotNull(cdClReposMon);
		org.junit.Assert.assertNotNull(cdClReposHist);
	}

	@Override
	public CdCl findById(CdClId cdClId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + cdClId);
		Optional<CdCl> cdCl = null;
		if (dbName.equals(ContentName.onDay))
			cdCl = cdClReposDay.findById(cdClId);
		else if (dbName.equals(ContentName.onMon))
			cdCl = cdClReposMon.findById(cdClId);
		else if (dbName.equals(ContentName.onHist))
			cdCl = cdClReposHist.findById(cdClId);
		else
			cdCl = cdClRepos.findById(cdClId);
		CdCl obj = cdCl.isPresent() ? cdCl.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdCl> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdCl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdClReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdClReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdClReposHist.findAll(pageable);
		else
			slice = cdClRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdCl> clCode1Eq(int clCode1_0, int clCode1_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdCl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("clCode1Eq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode1_1 : " + clCode1_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdClReposDay.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualOrderByClCode1AscClCode2Asc(clCode1_0, clCode1_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdClReposMon.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualOrderByClCode1AscClCode2Asc(clCode1_0, clCode1_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdClReposHist.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualOrderByClCode1AscClCode2Asc(clCode1_0, clCode1_1, pageable);
		else
			slice = cdClRepos.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualOrderByClCode1AscClCode2Asc(clCode1_0, clCode1_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdCl> clTypeJCICEq(String clTypeJCIC_0, String clTypeJCIC_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdCl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("clTypeJCICEq " + dbName + " : " + "clTypeJCIC_0 : " + clTypeJCIC_0 + " clTypeJCIC_1 : " + clTypeJCIC_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdClReposDay.findAllByClTypeJCICGreaterThanEqualAndClTypeJCICLessThanEqual(clTypeJCIC_0, clTypeJCIC_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdClReposMon.findAllByClTypeJCICGreaterThanEqualAndClTypeJCICLessThanEqual(clTypeJCIC_0, clTypeJCIC_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdClReposHist.findAllByClTypeJCICGreaterThanEqualAndClTypeJCICLessThanEqual(clTypeJCIC_0, clTypeJCIC_1, pageable);
		else
			slice = cdClRepos.findAllByClTypeJCICGreaterThanEqualAndClTypeJCICLessThanEqual(clTypeJCIC_0, clTypeJCIC_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdCl> clCode1Range(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdCl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("clCode1Range " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode1_1 : " + clCode1_1 + " clCode2_2 : " + clCode2_2 + " clCode2_3 : " + clCode2_3);
		if (dbName.equals(ContentName.onDay))
			slice = cdClReposDay.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualOrderByClCode1AscClCode2Asc(clCode1_0, clCode1_1, clCode2_2,
					clCode2_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdClReposMon.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualOrderByClCode1AscClCode2Asc(clCode1_0, clCode1_1, clCode2_2,
					clCode2_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdClReposHist.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualOrderByClCode1AscClCode2Asc(clCode1_0, clCode1_1, clCode2_2,
					clCode2_3, pageable);
		else
			slice = cdClRepos.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualOrderByClCode1AscClCode2Asc(clCode1_0, clCode1_1, clCode2_2,
					clCode2_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdCl holdById(CdClId cdClId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdClId);
		Optional<CdCl> cdCl = null;
		if (dbName.equals(ContentName.onDay))
			cdCl = cdClReposDay.findByCdClId(cdClId);
		else if (dbName.equals(ContentName.onMon))
			cdCl = cdClReposMon.findByCdClId(cdClId);
		else if (dbName.equals(ContentName.onHist))
			cdCl = cdClReposHist.findByCdClId(cdClId);
		else
			cdCl = cdClRepos.findByCdClId(cdClId);
		return cdCl.isPresent() ? cdCl.get() : null;
	}

	@Override
	public CdCl holdById(CdCl cdCl, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdCl.getCdClId());
		Optional<CdCl> cdClT = null;
		if (dbName.equals(ContentName.onDay))
			cdClT = cdClReposDay.findByCdClId(cdCl.getCdClId());
		else if (dbName.equals(ContentName.onMon))
			cdClT = cdClReposMon.findByCdClId(cdCl.getCdClId());
		else if (dbName.equals(ContentName.onHist))
			cdClT = cdClReposHist.findByCdClId(cdCl.getCdClId());
		else
			cdClT = cdClRepos.findByCdClId(cdCl.getCdClId());
		return cdClT.isPresent() ? cdClT.get() : null;
	}

	@Override
	public CdCl insert(CdCl cdCl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + cdCl.getCdClId());
		if (this.findById(cdCl.getCdClId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdCl.setCreateEmpNo(empNot);

		if (cdCl.getLastUpdateEmpNo() == null || cdCl.getLastUpdateEmpNo().isEmpty())
			cdCl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdClReposDay.saveAndFlush(cdCl);
		else if (dbName.equals(ContentName.onMon))
			return cdClReposMon.saveAndFlush(cdCl);
		else if (dbName.equals(ContentName.onHist))
			return cdClReposHist.saveAndFlush(cdCl);
		else
			return cdClRepos.saveAndFlush(cdCl);
	}

	@Override
	public CdCl update(CdCl cdCl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + cdCl.getCdClId());
		if (!empNot.isEmpty())
			cdCl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdClReposDay.saveAndFlush(cdCl);
		else if (dbName.equals(ContentName.onMon))
			return cdClReposMon.saveAndFlush(cdCl);
		else if (dbName.equals(ContentName.onHist))
			return cdClReposHist.saveAndFlush(cdCl);
		else
			return cdClRepos.saveAndFlush(cdCl);
	}

	@Override
	public CdCl update2(CdCl cdCl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + cdCl.getCdClId());
		if (!empNot.isEmpty())
			cdCl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdClReposDay.saveAndFlush(cdCl);
		else if (dbName.equals(ContentName.onMon))
			cdClReposMon.saveAndFlush(cdCl);
		else if (dbName.equals(ContentName.onHist))
			cdClReposHist.saveAndFlush(cdCl);
		else
			cdClRepos.saveAndFlush(cdCl);
		return this.findById(cdCl.getCdClId());
	}

	@Override
	public void delete(CdCl cdCl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdCl.getCdClId());
		if (dbName.equals(ContentName.onDay)) {
			cdClReposDay.delete(cdCl);
			cdClReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdClReposMon.delete(cdCl);
			cdClReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdClReposHist.delete(cdCl);
			cdClReposHist.flush();
		} else {
			cdClRepos.delete(cdCl);
			cdClRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdCl> cdCl, TitaVo... titaVo) throws DBException {
		if (cdCl == null || cdCl.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (CdCl t : cdCl) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdCl = cdClReposDay.saveAll(cdCl);
			cdClReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdCl = cdClReposMon.saveAll(cdCl);
			cdClReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdCl = cdClReposHist.saveAll(cdCl);
			cdClReposHist.flush();
		} else {
			cdCl = cdClRepos.saveAll(cdCl);
			cdClRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdCl> cdCl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (cdCl == null || cdCl.size() == 0)
			throw new DBException(6);

		for (CdCl t : cdCl)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdCl = cdClReposDay.saveAll(cdCl);
			cdClReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdCl = cdClReposMon.saveAll(cdCl);
			cdClReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdCl = cdClReposHist.saveAll(cdCl);
			cdClReposHist.flush();
		} else {
			cdCl = cdClRepos.saveAll(cdCl);
			cdClRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdCl> cdCl, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdCl == null || cdCl.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdClReposDay.deleteAll(cdCl);
			cdClReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdClReposMon.deleteAll(cdCl);
			cdClReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdClReposHist.deleteAll(cdCl);
			cdClReposHist.flush();
		} else {
			cdClRepos.deleteAll(cdCl);
			cdClRepos.flush();
		}
	}

}
