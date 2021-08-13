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
import com.st1.itx.db.domain.RelsFamily;
import com.st1.itx.db.domain.RelsFamilyId;
import com.st1.itx.db.repository.online.RelsFamilyRepository;
import com.st1.itx.db.repository.day.RelsFamilyRepositoryDay;
import com.st1.itx.db.repository.mon.RelsFamilyRepositoryMon;
import com.st1.itx.db.repository.hist.RelsFamilyRepositoryHist;
import com.st1.itx.db.service.RelsFamilyService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("relsFamilyService")
@Repository
public class RelsFamilyServiceImpl implements RelsFamilyService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(RelsFamilyServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private RelsFamilyRepository relsFamilyRepos;

	@Autowired
	private RelsFamilyRepositoryDay relsFamilyReposDay;

	@Autowired
	private RelsFamilyRepositoryMon relsFamilyReposMon;

	@Autowired
	private RelsFamilyRepositoryHist relsFamilyReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(relsFamilyRepos);
		org.junit.Assert.assertNotNull(relsFamilyReposDay);
		org.junit.Assert.assertNotNull(relsFamilyReposMon);
		org.junit.Assert.assertNotNull(relsFamilyReposHist);
	}

	@Override
	public RelsFamily findById(RelsFamilyId relsFamilyId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + relsFamilyId);
		Optional<RelsFamily> relsFamily = null;
		if (dbName.equals(ContentName.onDay))
			relsFamily = relsFamilyReposDay.findById(relsFamilyId);
		else if (dbName.equals(ContentName.onMon))
			relsFamily = relsFamilyReposMon.findById(relsFamilyId);
		else if (dbName.equals(ContentName.onHist))
			relsFamily = relsFamilyReposHist.findById(relsFamilyId);
		else
			relsFamily = relsFamilyRepos.findById(relsFamilyId);
		RelsFamily obj = relsFamily.isPresent() ? relsFamily.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<RelsFamily> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RelsFamily> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "RelsUKey", "RelsSeq"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = relsFamilyReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = relsFamilyReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = relsFamilyReposHist.findAll(pageable);
		else
			slice = relsFamilyRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<RelsFamily> RelsUKeyEq(String relsUKey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RelsFamily> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("RelsUKeyEq " + dbName + " : " + "relsUKey_0 : " + relsUKey_0);
		if (dbName.equals(ContentName.onDay))
			slice = relsFamilyReposDay.findAllByRelsUKeyIsOrderByRelsSeqAsc(relsUKey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = relsFamilyReposMon.findAllByRelsUKeyIsOrderByRelsSeqAsc(relsUKey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = relsFamilyReposHist.findAllByRelsUKeyIsOrderByRelsSeqAsc(relsUKey_0, pageable);
		else
			slice = relsFamilyRepos.findAllByRelsUKeyIsOrderByRelsSeqAsc(relsUKey_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public RelsFamily maxRelsSeqFirst(String relsUKey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("maxRelsSeqFirst " + dbName + " : " + "relsUKey_0 : " + relsUKey_0);
		Optional<RelsFamily> relsFamilyT = null;
		if (dbName.equals(ContentName.onDay))
			relsFamilyT = relsFamilyReposDay.findTopByRelsUKeyIsOrderByRelsSeqDesc(relsUKey_0);
		else if (dbName.equals(ContentName.onMon))
			relsFamilyT = relsFamilyReposMon.findTopByRelsUKeyIsOrderByRelsSeqDesc(relsUKey_0);
		else if (dbName.equals(ContentName.onHist))
			relsFamilyT = relsFamilyReposHist.findTopByRelsUKeyIsOrderByRelsSeqDesc(relsUKey_0);
		else
			relsFamilyT = relsFamilyRepos.findTopByRelsUKeyIsOrderByRelsSeqDesc(relsUKey_0);
		return relsFamilyT.isPresent() ? relsFamilyT.get() : null;
	}

	@Override
	public Slice<RelsFamily> findFamilyIdEq(String familyId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RelsFamily> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findFamilyIdEq " + dbName + " : " + "familyId_0 : " + familyId_0);
		if (dbName.equals(ContentName.onDay))
			slice = relsFamilyReposDay.findAllByFamilyIdIs(familyId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = relsFamilyReposMon.findAllByFamilyIdIs(familyId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = relsFamilyReposHist.findAllByFamilyIdIs(familyId_0, pageable);
		else
			slice = relsFamilyRepos.findAllByFamilyIdIs(familyId_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<RelsFamily> findFamilyNameEq(String familyName_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RelsFamily> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findFamilyNameEq " + dbName + " : " + "familyName_0 : " + familyName_0);
		if (dbName.equals(ContentName.onDay))
			slice = relsFamilyReposDay.findAllByFamilyNameIs(familyName_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = relsFamilyReposMon.findAllByFamilyNameIs(familyName_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = relsFamilyReposHist.findAllByFamilyNameIs(familyName_0, pageable);
		else
			slice = relsFamilyRepos.findAllByFamilyNameIs(familyName_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public RelsFamily holdById(RelsFamilyId relsFamilyId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + relsFamilyId);
		Optional<RelsFamily> relsFamily = null;
		if (dbName.equals(ContentName.onDay))
			relsFamily = relsFamilyReposDay.findByRelsFamilyId(relsFamilyId);
		else if (dbName.equals(ContentName.onMon))
			relsFamily = relsFamilyReposMon.findByRelsFamilyId(relsFamilyId);
		else if (dbName.equals(ContentName.onHist))
			relsFamily = relsFamilyReposHist.findByRelsFamilyId(relsFamilyId);
		else
			relsFamily = relsFamilyRepos.findByRelsFamilyId(relsFamilyId);
		return relsFamily.isPresent() ? relsFamily.get() : null;
	}

	@Override
	public RelsFamily holdById(RelsFamily relsFamily, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + relsFamily.getRelsFamilyId());
		Optional<RelsFamily> relsFamilyT = null;
		if (dbName.equals(ContentName.onDay))
			relsFamilyT = relsFamilyReposDay.findByRelsFamilyId(relsFamily.getRelsFamilyId());
		else if (dbName.equals(ContentName.onMon))
			relsFamilyT = relsFamilyReposMon.findByRelsFamilyId(relsFamily.getRelsFamilyId());
		else if (dbName.equals(ContentName.onHist))
			relsFamilyT = relsFamilyReposHist.findByRelsFamilyId(relsFamily.getRelsFamilyId());
		else
			relsFamilyT = relsFamilyRepos.findByRelsFamilyId(relsFamily.getRelsFamilyId());
		return relsFamilyT.isPresent() ? relsFamilyT.get() : null;
	}

	@Override
	public RelsFamily insert(RelsFamily relsFamily, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + relsFamily.getRelsFamilyId());
		if (this.findById(relsFamily.getRelsFamilyId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			relsFamily.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return relsFamilyReposDay.saveAndFlush(relsFamily);
		else if (dbName.equals(ContentName.onMon))
			return relsFamilyReposMon.saveAndFlush(relsFamily);
		else if (dbName.equals(ContentName.onHist))
			return relsFamilyReposHist.saveAndFlush(relsFamily);
		else
			return relsFamilyRepos.saveAndFlush(relsFamily);
	}

	@Override
	public RelsFamily update(RelsFamily relsFamily, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + relsFamily.getRelsFamilyId());
		if (!empNot.isEmpty())
			relsFamily.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return relsFamilyReposDay.saveAndFlush(relsFamily);
		else if (dbName.equals(ContentName.onMon))
			return relsFamilyReposMon.saveAndFlush(relsFamily);
		else if (dbName.equals(ContentName.onHist))
			return relsFamilyReposHist.saveAndFlush(relsFamily);
		else
			return relsFamilyRepos.saveAndFlush(relsFamily);
	}

	@Override
	public RelsFamily update2(RelsFamily relsFamily, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + relsFamily.getRelsFamilyId());
		if (!empNot.isEmpty())
			relsFamily.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			relsFamilyReposDay.saveAndFlush(relsFamily);
		else if (dbName.equals(ContentName.onMon))
			relsFamilyReposMon.saveAndFlush(relsFamily);
		else if (dbName.equals(ContentName.onHist))
			relsFamilyReposHist.saveAndFlush(relsFamily);
		else
			relsFamilyRepos.saveAndFlush(relsFamily);
		return this.findById(relsFamily.getRelsFamilyId());
	}

	@Override
	public void delete(RelsFamily relsFamily, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + relsFamily.getRelsFamilyId());
		if (dbName.equals(ContentName.onDay)) {
			relsFamilyReposDay.delete(relsFamily);
			relsFamilyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			relsFamilyReposMon.delete(relsFamily);
			relsFamilyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			relsFamilyReposHist.delete(relsFamily);
			relsFamilyReposHist.flush();
		} else {
			relsFamilyRepos.delete(relsFamily);
			relsFamilyRepos.flush();
		}
	}

	@Override
	public void insertAll(List<RelsFamily> relsFamily, TitaVo... titaVo) throws DBException {
		if (relsFamily == null || relsFamily.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (RelsFamily t : relsFamily)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			relsFamily = relsFamilyReposDay.saveAll(relsFamily);
			relsFamilyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			relsFamily = relsFamilyReposMon.saveAll(relsFamily);
			relsFamilyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			relsFamily = relsFamilyReposHist.saveAll(relsFamily);
			relsFamilyReposHist.flush();
		} else {
			relsFamily = relsFamilyRepos.saveAll(relsFamily);
			relsFamilyRepos.flush();
		}
	}

	@Override
	public void updateAll(List<RelsFamily> relsFamily, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (relsFamily == null || relsFamily.size() == 0)
			throw new DBException(6);

		for (RelsFamily t : relsFamily)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			relsFamily = relsFamilyReposDay.saveAll(relsFamily);
			relsFamilyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			relsFamily = relsFamilyReposMon.saveAll(relsFamily);
			relsFamilyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			relsFamily = relsFamilyReposHist.saveAll(relsFamily);
			relsFamilyReposHist.flush();
		} else {
			relsFamily = relsFamilyRepos.saveAll(relsFamily);
			relsFamilyRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<RelsFamily> relsFamily, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (relsFamily == null || relsFamily.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			relsFamilyReposDay.deleteAll(relsFamily);
			relsFamilyReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			relsFamilyReposMon.deleteAll(relsFamily);
			relsFamilyReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			relsFamilyReposHist.deleteAll(relsFamily);
			relsFamilyReposHist.flush();
		} else {
			relsFamilyRepos.deleteAll(relsFamily);
			relsFamilyRepos.flush();
		}
	}

}
