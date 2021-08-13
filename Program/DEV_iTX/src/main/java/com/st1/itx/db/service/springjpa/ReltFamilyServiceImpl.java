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
import com.st1.itx.db.domain.ReltFamily;
import com.st1.itx.db.domain.ReltFamilyId;
import com.st1.itx.db.repository.online.ReltFamilyRepository;
import com.st1.itx.db.repository.day.ReltFamilyRepositoryDay;
import com.st1.itx.db.repository.mon.ReltFamilyRepositoryMon;
import com.st1.itx.db.repository.hist.ReltFamilyRepositoryHist;
import com.st1.itx.db.service.ReltFamilyService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("reltFamilyService")
@Repository
public class ReltFamilyServiceImpl implements ReltFamilyService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(ReltFamilyServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private ReltFamilyRepository reltFamilyRepos;

	@Autowired
	private ReltFamilyRepositoryDay reltFamilyReposDay;

	@Autowired
	private ReltFamilyRepositoryMon reltFamilyReposMon;

	@Autowired
	private ReltFamilyRepositoryHist reltFamilyReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(reltFamilyRepos);
		org.junit.Assert.assertNotNull(reltFamilyReposDay);
		org.junit.Assert.assertNotNull(reltFamilyReposMon);
		org.junit.Assert.assertNotNull(reltFamilyReposHist);
	}

	@Override
	public ReltFamily findById(ReltFamilyId reltFamilyId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + reltFamilyId);
		Optional<ReltFamily> reltFamily = null;
		if (dbName.equals(ContentName.onDay))
			reltFamily = reltFamilyReposDay.findById(reltFamilyId);
		else if (dbName.equals(ContentName.onMon))
			reltFamily = reltFamilyReposMon.findById(reltFamilyId);
		else if (dbName.equals(ContentName.onHist))
			reltFamily = reltFamilyReposHist.findById(reltFamilyId);
		else
			reltFamily = reltFamilyRepos.findById(reltFamilyId);
		ReltFamily obj = reltFamily.isPresent() ? reltFamily.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<ReltFamily> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ReltFamily> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ReltUKey", "ReltSeq"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = reltFamilyReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = reltFamilyReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = reltFamilyReposHist.findAll(pageable);
		else
			slice = reltFamilyRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ReltFamily> ReltUKeyEq(String reltUKey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ReltFamily> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("ReltUKeyEq " + dbName + " : " + "reltUKey_0 : " + reltUKey_0);
		if (dbName.equals(ContentName.onDay))
			slice = reltFamilyReposDay.findAllByReltUKeyIsOrderByReltSeqAsc(reltUKey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = reltFamilyReposMon.findAllByReltUKeyIsOrderByReltSeqAsc(reltUKey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = reltFamilyReposHist.findAllByReltUKeyIsOrderByReltSeqAsc(reltUKey_0, pageable);
		else
			slice = reltFamilyRepos.findAllByReltUKeyIsOrderByReltSeqAsc(reltUKey_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ReltFamily maxReltSeqFirst(String reltUKey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("maxReltSeqFirst " + dbName + " : " + "reltUKey_0 : " + reltUKey_0);
		Optional<ReltFamily> reltFamilyT = null;
		if (dbName.equals(ContentName.onDay))
			reltFamilyT = reltFamilyReposDay.findTopByReltUKeyIsOrderByReltSeqDesc(reltUKey_0);
		else if (dbName.equals(ContentName.onMon))
			reltFamilyT = reltFamilyReposMon.findTopByReltUKeyIsOrderByReltSeqDesc(reltUKey_0);
		else if (dbName.equals(ContentName.onHist))
			reltFamilyT = reltFamilyReposHist.findTopByReltUKeyIsOrderByReltSeqDesc(reltUKey_0);
		else
			reltFamilyT = reltFamilyRepos.findTopByReltUKeyIsOrderByReltSeqDesc(reltUKey_0);
		return reltFamilyT.isPresent() ? reltFamilyT.get() : null;
	}

	@Override
	public ReltFamily holdById(ReltFamilyId reltFamilyId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + reltFamilyId);
		Optional<ReltFamily> reltFamily = null;
		if (dbName.equals(ContentName.onDay))
			reltFamily = reltFamilyReposDay.findByReltFamilyId(reltFamilyId);
		else if (dbName.equals(ContentName.onMon))
			reltFamily = reltFamilyReposMon.findByReltFamilyId(reltFamilyId);
		else if (dbName.equals(ContentName.onHist))
			reltFamily = reltFamilyReposHist.findByReltFamilyId(reltFamilyId);
		else
			reltFamily = reltFamilyRepos.findByReltFamilyId(reltFamilyId);
		return reltFamily.isPresent() ? reltFamily.get() : null;
	}

	@Override
	public ReltFamily holdById(ReltFamily reltFamily, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + reltFamily.getReltFamilyId());
		Optional<ReltFamily> reltFamilyT = null;
		if (dbName.equals(ContentName.onDay))
			reltFamilyT = reltFamilyReposDay.findByReltFamilyId(reltFamily.getReltFamilyId());
		else if (dbName.equals(ContentName.onMon))
			reltFamilyT = reltFamilyReposMon.findByReltFamilyId(reltFamily.getReltFamilyId());
		else if (dbName.equals(ContentName.onHist))
			reltFamilyT = reltFamilyReposHist.findByReltFamilyId(reltFamily.getReltFamilyId());
		else
			reltFamilyT = reltFamilyRepos.findByReltFamilyId(reltFamily.getReltFamilyId());
		return reltFamilyT.isPresent() ? reltFamilyT.get() : null;
	}

	@Override
	public ReltFamily insert(ReltFamily reltFamily, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + reltFamily.getReltFamilyId());
		if (this.findById(reltFamily.getReltFamilyId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			reltFamily.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return reltFamilyReposDay.saveAndFlush(reltFamily);
		else if (dbName.equals(ContentName.onMon))
			return reltFamilyReposMon.saveAndFlush(reltFamily);
		else if (dbName.equals(ContentName.onHist))
			return reltFamilyReposHist.saveAndFlush(reltFamily);
		else
			return reltFamilyRepos.saveAndFlush(reltFamily);
	}

	@Override
	public ReltFamily update(ReltFamily reltFamily, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + reltFamily.getReltFamilyId());
		if (!empNot.isEmpty())
			reltFamily.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return reltFamilyReposDay.saveAndFlush(reltFamily);
		else if (dbName.equals(ContentName.onMon))
			return reltFamilyReposMon.saveAndFlush(reltFamily);
		else if (dbName.equals(ContentName.onHist))
			return reltFamilyReposHist.saveAndFlush(reltFamily);
		else
			return reltFamilyRepos.saveAndFlush(reltFamily);
	}

	@Override
	public ReltFamily update2(ReltFamily reltFamily, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + reltFamily.getReltFamilyId());
		if (!empNot.isEmpty())
			reltFamily.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			reltFamilyReposDay.saveAndFlush(reltFamily);
		else if (dbName.equals(ContentName.onMon))
			reltFamilyReposMon.saveAndFlush(reltFamily);
		else if (dbName.equals(ContentName.onHist))
			reltFamilyReposHist.saveAndFlush(reltFamily);
		else
			reltFamilyRepos.saveAndFlush(reltFamily);
		return this.findById(reltFamily.getReltFamilyId());
	}

	@Override
	public void delete(ReltFamily reltFamily, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + reltFamily.getReltFamilyId());
		if (dbName.equals(ContentName.onDay)) {
			reltFamilyReposDay.delete(reltFamily);
			reltFamilyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			reltFamilyReposMon.delete(reltFamily);
			reltFamilyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			reltFamilyReposHist.delete(reltFamily);
			reltFamilyReposHist.flush();
		} else {
			reltFamilyRepos.delete(reltFamily);
			reltFamilyRepos.flush();
		}
	}

	@Override
	public void insertAll(List<ReltFamily> reltFamily, TitaVo... titaVo) throws DBException {
		if (reltFamily == null || reltFamily.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (ReltFamily t : reltFamily)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			reltFamily = reltFamilyReposDay.saveAll(reltFamily);
			reltFamilyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			reltFamily = reltFamilyReposMon.saveAll(reltFamily);
			reltFamilyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			reltFamily = reltFamilyReposHist.saveAll(reltFamily);
			reltFamilyReposHist.flush();
		} else {
			reltFamily = reltFamilyRepos.saveAll(reltFamily);
			reltFamilyRepos.flush();
		}
	}

	@Override
	public void updateAll(List<ReltFamily> reltFamily, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (reltFamily == null || reltFamily.size() == 0)
			throw new DBException(6);

		for (ReltFamily t : reltFamily)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			reltFamily = reltFamilyReposDay.saveAll(reltFamily);
			reltFamilyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			reltFamily = reltFamilyReposMon.saveAll(reltFamily);
			reltFamilyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			reltFamily = reltFamilyReposHist.saveAll(reltFamily);
			reltFamilyReposHist.flush();
		} else {
			reltFamily = reltFamilyRepos.saveAll(reltFamily);
			reltFamilyRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<ReltFamily> reltFamily, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (reltFamily == null || reltFamily.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			reltFamilyReposDay.deleteAll(reltFamily);
			reltFamilyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			reltFamilyReposMon.deleteAll(reltFamily);
			reltFamilyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			reltFamilyReposHist.deleteAll(reltFamily);
			reltFamilyReposHist.flush();
		} else {
			reltFamilyRepos.deleteAll(reltFamily);
			reltFamilyRepos.flush();
		}
	}

}
