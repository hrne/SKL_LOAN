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
import com.st1.itx.db.domain.RelsCompany;
import com.st1.itx.db.domain.RelsCompanyId;
import com.st1.itx.db.repository.online.RelsCompanyRepository;
import com.st1.itx.db.repository.day.RelsCompanyRepositoryDay;
import com.st1.itx.db.repository.mon.RelsCompanyRepositoryMon;
import com.st1.itx.db.repository.hist.RelsCompanyRepositoryHist;
import com.st1.itx.db.service.RelsCompanyService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("relsCompanyService")
@Repository
public class RelsCompanyServiceImpl implements RelsCompanyService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(RelsCompanyServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private RelsCompanyRepository relsCompanyRepos;

	@Autowired
	private RelsCompanyRepositoryDay relsCompanyReposDay;

	@Autowired
	private RelsCompanyRepositoryMon relsCompanyReposMon;

	@Autowired
	private RelsCompanyRepositoryHist relsCompanyReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(relsCompanyRepos);
		org.junit.Assert.assertNotNull(relsCompanyReposDay);
		org.junit.Assert.assertNotNull(relsCompanyReposMon);
		org.junit.Assert.assertNotNull(relsCompanyReposHist);
	}

	@Override
	public RelsCompany findById(RelsCompanyId relsCompanyId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + relsCompanyId);
		Optional<RelsCompany> relsCompany = null;
		if (dbName.equals(ContentName.onDay))
			relsCompany = relsCompanyReposDay.findById(relsCompanyId);
		else if (dbName.equals(ContentName.onMon))
			relsCompany = relsCompanyReposMon.findById(relsCompanyId);
		else if (dbName.equals(ContentName.onHist))
			relsCompany = relsCompanyReposHist.findById(relsCompanyId);
		else
			relsCompany = relsCompanyRepos.findById(relsCompanyId);
		RelsCompany obj = relsCompany.isPresent() ? relsCompany.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<RelsCompany> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RelsCompany> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "RelsUKey", "CompanyId"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = relsCompanyReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = relsCompanyReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = relsCompanyReposHist.findAll(pageable);
		else
			slice = relsCompanyRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<RelsCompany> RelsUKeyEq(String relsUKey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RelsCompany> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("RelsUKeyEq " + dbName + " : " + "relsUKey_0 : " + relsUKey_0);
		if (dbName.equals(ContentName.onDay))
			slice = relsCompanyReposDay.findAllByRelsUKeyIs(relsUKey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = relsCompanyReposMon.findAllByRelsUKeyIs(relsUKey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = relsCompanyReposHist.findAllByRelsUKeyIs(relsUKey_0, pageable);
		else
			slice = relsCompanyRepos.findAllByRelsUKeyIs(relsUKey_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<RelsCompany> findCompanyIdEq(String companyId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RelsCompany> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findCompanyIdEq " + dbName + " : " + "companyId_0 : " + companyId_0);
		if (dbName.equals(ContentName.onDay))
			slice = relsCompanyReposDay.findAllByCompanyIdIs(companyId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = relsCompanyReposMon.findAllByCompanyIdIs(companyId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = relsCompanyReposHist.findAllByCompanyIdIs(companyId_0, pageable);
		else
			slice = relsCompanyRepos.findAllByCompanyIdIs(companyId_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<RelsCompany> findCompanyNameEq(String companyName_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RelsCompany> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findCompanyNameEq " + dbName + " : " + "companyName_0 : " + companyName_0);
		if (dbName.equals(ContentName.onDay))
			slice = relsCompanyReposDay.findAllByCompanyNameIs(companyName_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = relsCompanyReposMon.findAllByCompanyNameIs(companyName_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = relsCompanyReposHist.findAllByCompanyNameIs(companyName_0, pageable);
		else
			slice = relsCompanyRepos.findAllByCompanyNameIs(companyName_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public RelsCompany holdById(RelsCompanyId relsCompanyId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + relsCompanyId);
		Optional<RelsCompany> relsCompany = null;
		if (dbName.equals(ContentName.onDay))
			relsCompany = relsCompanyReposDay.findByRelsCompanyId(relsCompanyId);
		else if (dbName.equals(ContentName.onMon))
			relsCompany = relsCompanyReposMon.findByRelsCompanyId(relsCompanyId);
		else if (dbName.equals(ContentName.onHist))
			relsCompany = relsCompanyReposHist.findByRelsCompanyId(relsCompanyId);
		else
			relsCompany = relsCompanyRepos.findByRelsCompanyId(relsCompanyId);
		return relsCompany.isPresent() ? relsCompany.get() : null;
	}

	@Override
	public RelsCompany holdById(RelsCompany relsCompany, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + relsCompany.getRelsCompanyId());
		Optional<RelsCompany> relsCompanyT = null;
		if (dbName.equals(ContentName.onDay))
			relsCompanyT = relsCompanyReposDay.findByRelsCompanyId(relsCompany.getRelsCompanyId());
		else if (dbName.equals(ContentName.onMon))
			relsCompanyT = relsCompanyReposMon.findByRelsCompanyId(relsCompany.getRelsCompanyId());
		else if (dbName.equals(ContentName.onHist))
			relsCompanyT = relsCompanyReposHist.findByRelsCompanyId(relsCompany.getRelsCompanyId());
		else
			relsCompanyT = relsCompanyRepos.findByRelsCompanyId(relsCompany.getRelsCompanyId());
		return relsCompanyT.isPresent() ? relsCompanyT.get() : null;
	}

	@Override
	public RelsCompany insert(RelsCompany relsCompany, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + relsCompany.getRelsCompanyId());
		if (this.findById(relsCompany.getRelsCompanyId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			relsCompany.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return relsCompanyReposDay.saveAndFlush(relsCompany);
		else if (dbName.equals(ContentName.onMon))
			return relsCompanyReposMon.saveAndFlush(relsCompany);
		else if (dbName.equals(ContentName.onHist))
			return relsCompanyReposHist.saveAndFlush(relsCompany);
		else
			return relsCompanyRepos.saveAndFlush(relsCompany);
	}

	@Override
	public RelsCompany update(RelsCompany relsCompany, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + relsCompany.getRelsCompanyId());
		if (!empNot.isEmpty())
			relsCompany.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return relsCompanyReposDay.saveAndFlush(relsCompany);
		else if (dbName.equals(ContentName.onMon))
			return relsCompanyReposMon.saveAndFlush(relsCompany);
		else if (dbName.equals(ContentName.onHist))
			return relsCompanyReposHist.saveAndFlush(relsCompany);
		else
			return relsCompanyRepos.saveAndFlush(relsCompany);
	}

	@Override
	public RelsCompany update2(RelsCompany relsCompany, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + relsCompany.getRelsCompanyId());
		if (!empNot.isEmpty())
			relsCompany.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			relsCompanyReposDay.saveAndFlush(relsCompany);
		else if (dbName.equals(ContentName.onMon))
			relsCompanyReposMon.saveAndFlush(relsCompany);
		else if (dbName.equals(ContentName.onHist))
			relsCompanyReposHist.saveAndFlush(relsCompany);
		else
			relsCompanyRepos.saveAndFlush(relsCompany);
		return this.findById(relsCompany.getRelsCompanyId());
	}

	@Override
	public void delete(RelsCompany relsCompany, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + relsCompany.getRelsCompanyId());
		if (dbName.equals(ContentName.onDay)) {
			relsCompanyReposDay.delete(relsCompany);
			relsCompanyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			relsCompanyReposMon.delete(relsCompany);
			relsCompanyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			relsCompanyReposHist.delete(relsCompany);
			relsCompanyReposHist.flush();
		} else {
			relsCompanyRepos.delete(relsCompany);
			relsCompanyRepos.flush();
		}
	}

	@Override
	public void insertAll(List<RelsCompany> relsCompany, TitaVo... titaVo) throws DBException {
		if (relsCompany == null || relsCompany.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (RelsCompany t : relsCompany)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			relsCompany = relsCompanyReposDay.saveAll(relsCompany);
			relsCompanyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			relsCompany = relsCompanyReposMon.saveAll(relsCompany);
			relsCompanyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			relsCompany = relsCompanyReposHist.saveAll(relsCompany);
			relsCompanyReposHist.flush();
		} else {
			relsCompany = relsCompanyRepos.saveAll(relsCompany);
			relsCompanyRepos.flush();
		}
	}

	@Override
	public void updateAll(List<RelsCompany> relsCompany, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (relsCompany == null || relsCompany.size() == 0)
			throw new DBException(6);

		for (RelsCompany t : relsCompany)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			relsCompany = relsCompanyReposDay.saveAll(relsCompany);
			relsCompanyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			relsCompany = relsCompanyReposMon.saveAll(relsCompany);
			relsCompanyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			relsCompany = relsCompanyReposHist.saveAll(relsCompany);
			relsCompanyReposHist.flush();
		} else {
			relsCompany = relsCompanyRepos.saveAll(relsCompany);
			relsCompanyRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<RelsCompany> relsCompany, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (relsCompany == null || relsCompany.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			relsCompanyReposDay.deleteAll(relsCompany);
			relsCompanyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			relsCompanyReposMon.deleteAll(relsCompany);
			relsCompanyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			relsCompanyReposHist.deleteAll(relsCompany);
			relsCompanyReposHist.flush();
		} else {
			relsCompanyRepos.deleteAll(relsCompany);
			relsCompanyRepos.flush();
		}
	}

}
