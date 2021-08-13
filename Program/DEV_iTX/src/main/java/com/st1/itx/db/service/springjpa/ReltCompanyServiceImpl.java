package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.domain.ReltCompany;
import com.st1.itx.db.domain.ReltCompanyId;
import com.st1.itx.db.repository.online.ReltCompanyRepository;
import com.st1.itx.db.repository.day.ReltCompanyRepositoryDay;
import com.st1.itx.db.repository.mon.ReltCompanyRepositoryMon;
import com.st1.itx.db.repository.hist.ReltCompanyRepositoryHist;
import com.st1.itx.db.service.ReltCompanyService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("reltCompanyService")
@Repository
public class ReltCompanyServiceImpl implements ReltCompanyService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(ReltCompanyServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private ReltCompanyRepository reltCompanyRepos;

	@Autowired
	private ReltCompanyRepositoryDay reltCompanyReposDay;

	@Autowired
	private ReltCompanyRepositoryMon reltCompanyReposMon;

	@Autowired
	private ReltCompanyRepositoryHist reltCompanyReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(reltCompanyRepos);
		org.junit.Assert.assertNotNull(reltCompanyReposDay);
		org.junit.Assert.assertNotNull(reltCompanyReposMon);
		org.junit.Assert.assertNotNull(reltCompanyReposHist);
	}

	@Override
	public ReltCompany findById(ReltCompanyId reltCompanyId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + reltCompanyId);
		Optional<ReltCompany> reltCompany = null;
		if (dbName.equals(ContentName.onDay))
			reltCompany = reltCompanyReposDay.findById(reltCompanyId);
		else if (dbName.equals(ContentName.onMon))
			reltCompany = reltCompanyReposMon.findById(reltCompanyId);
		else if (dbName.equals(ContentName.onHist))
			reltCompany = reltCompanyReposHist.findById(reltCompanyId);
		else
			reltCompany = reltCompanyRepos.findById(reltCompanyId);
		ReltCompany obj = reltCompany.isPresent() ? reltCompany.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<ReltCompany> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ReltCompany> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ReltUKey", "CompanyId"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = reltCompanyReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = reltCompanyReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = reltCompanyReposHist.findAll(pageable);
		else
			slice = reltCompanyRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ReltCompany> ReltUKeyEq(String reltUKey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ReltCompany> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("ReltUKeyEq " + dbName + " : " + "reltUKey_0 : " + reltUKey_0);
		if (dbName.equals(ContentName.onDay))
			slice = reltCompanyReposDay.findAllByReltUKeyIs(reltUKey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = reltCompanyReposMon.findAllByReltUKeyIs(reltUKey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = reltCompanyReposHist.findAllByReltUKeyIs(reltUKey_0, pageable);
		else
			slice = reltCompanyRepos.findAllByReltUKeyIs(reltUKey_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ReltCompany holdById(ReltCompanyId reltCompanyId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + reltCompanyId);
		Optional<ReltCompany> reltCompany = null;
		if (dbName.equals(ContentName.onDay))
			reltCompany = reltCompanyReposDay.findByReltCompanyId(reltCompanyId);
		else if (dbName.equals(ContentName.onMon))
			reltCompany = reltCompanyReposMon.findByReltCompanyId(reltCompanyId);
		else if (dbName.equals(ContentName.onHist))
			reltCompany = reltCompanyReposHist.findByReltCompanyId(reltCompanyId);
		else
			reltCompany = reltCompanyRepos.findByReltCompanyId(reltCompanyId);
		return reltCompany.isPresent() ? reltCompany.get() : null;
	}

	@Override
	public ReltCompany holdById(ReltCompany reltCompany, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + reltCompany.getReltCompanyId());
		Optional<ReltCompany> reltCompanyT = null;
		if (dbName.equals(ContentName.onDay))
			reltCompanyT = reltCompanyReposDay.findByReltCompanyId(reltCompany.getReltCompanyId());
		else if (dbName.equals(ContentName.onMon))
			reltCompanyT = reltCompanyReposMon.findByReltCompanyId(reltCompany.getReltCompanyId());
		else if (dbName.equals(ContentName.onHist))
			reltCompanyT = reltCompanyReposHist.findByReltCompanyId(reltCompany.getReltCompanyId());
		else
			reltCompanyT = reltCompanyRepos.findByReltCompanyId(reltCompany.getReltCompanyId());
		return reltCompanyT.isPresent() ? reltCompanyT.get() : null;
	}

	@Override
	public ReltCompany insert(ReltCompany reltCompany, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + reltCompany.getReltCompanyId());
		if (this.findById(reltCompany.getReltCompanyId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			reltCompany.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return reltCompanyReposDay.saveAndFlush(reltCompany);
		else if (dbName.equals(ContentName.onMon))
			return reltCompanyReposMon.saveAndFlush(reltCompany);
		else if (dbName.equals(ContentName.onHist))
			return reltCompanyReposHist.saveAndFlush(reltCompany);
		else
			return reltCompanyRepos.saveAndFlush(reltCompany);
	}

	@Override
	public ReltCompany update(ReltCompany reltCompany, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + reltCompany.getReltCompanyId());
		if (!empNot.isEmpty())
			reltCompany.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return reltCompanyReposDay.saveAndFlush(reltCompany);
		else if (dbName.equals(ContentName.onMon))
			return reltCompanyReposMon.saveAndFlush(reltCompany);
		else if (dbName.equals(ContentName.onHist))
			return reltCompanyReposHist.saveAndFlush(reltCompany);
		else
			return reltCompanyRepos.saveAndFlush(reltCompany);
	}

	@Override
	public ReltCompany update2(ReltCompany reltCompany, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + reltCompany.getReltCompanyId());
		if (!empNot.isEmpty())
			reltCompany.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			reltCompanyReposDay.saveAndFlush(reltCompany);
		else if (dbName.equals(ContentName.onMon))
			reltCompanyReposMon.saveAndFlush(reltCompany);
		else if (dbName.equals(ContentName.onHist))
			reltCompanyReposHist.saveAndFlush(reltCompany);
		else
			reltCompanyRepos.saveAndFlush(reltCompany);
		return this.findById(reltCompany.getReltCompanyId());
	}

	@Override
	public void delete(ReltCompany reltCompany, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + reltCompany.getReltCompanyId());
		if (dbName.equals(ContentName.onDay)) {
			reltCompanyReposDay.delete(reltCompany);
			reltCompanyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			reltCompanyReposMon.delete(reltCompany);
			reltCompanyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			reltCompanyReposHist.delete(reltCompany);
			reltCompanyReposHist.flush();
		} else {
			reltCompanyRepos.delete(reltCompany);
			reltCompanyRepos.flush();
		}
	}

	@Override
	public void insertAll(List<ReltCompany> reltCompany, TitaVo... titaVo) throws DBException {
		if (reltCompany == null || reltCompany.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (ReltCompany t : reltCompany)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			reltCompany = reltCompanyReposDay.saveAll(reltCompany);
			reltCompanyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			reltCompany = reltCompanyReposMon.saveAll(reltCompany);
			reltCompanyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			reltCompany = reltCompanyReposHist.saveAll(reltCompany);
			reltCompanyReposHist.flush();
		} else {
			reltCompany = reltCompanyRepos.saveAll(reltCompany);
			reltCompanyRepos.flush();
		}
	}

	@Override
	public void updateAll(List<ReltCompany> reltCompany, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (reltCompany == null || reltCompany.size() == 0)
			throw new DBException(6);

		for (ReltCompany t : reltCompany)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			reltCompany = reltCompanyReposDay.saveAll(reltCompany);
			reltCompanyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			reltCompany = reltCompanyReposMon.saveAll(reltCompany);
			reltCompanyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			reltCompany = reltCompanyReposHist.saveAll(reltCompany);
			reltCompanyReposHist.flush();
		} else {
			reltCompany = reltCompanyRepos.saveAll(reltCompany);
			reltCompanyRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<ReltCompany> reltCompany, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (reltCompany == null || reltCompany.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			reltCompanyReposDay.deleteAll(reltCompany);
			reltCompanyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			reltCompanyReposMon.deleteAll(reltCompany);
			reltCompanyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			reltCompanyReposHist.deleteAll(reltCompany);
			reltCompanyReposHist.flush();
		} else {
			reltCompanyRepos.deleteAll(reltCompany);
			reltCompanyRepos.flush();
		}
	}

}
