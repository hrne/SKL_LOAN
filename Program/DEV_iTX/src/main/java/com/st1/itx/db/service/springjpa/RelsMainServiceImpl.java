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
import com.st1.itx.db.domain.RelsMain;
import com.st1.itx.db.repository.online.RelsMainRepository;
import com.st1.itx.db.repository.day.RelsMainRepositoryDay;
import com.st1.itx.db.repository.mon.RelsMainRepositoryMon;
import com.st1.itx.db.repository.hist.RelsMainRepositoryHist;
import com.st1.itx.db.service.RelsMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("relsMainService")
@Repository
public class RelsMainServiceImpl extends ASpringJpaParm implements RelsMainService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private RelsMainRepository relsMainRepos;

	@Autowired
	private RelsMainRepositoryDay relsMainReposDay;

	@Autowired
	private RelsMainRepositoryMon relsMainReposMon;

	@Autowired
	private RelsMainRepositoryHist relsMainReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(relsMainRepos);
		org.junit.Assert.assertNotNull(relsMainReposDay);
		org.junit.Assert.assertNotNull(relsMainReposMon);
		org.junit.Assert.assertNotNull(relsMainReposHist);
	}

	@Override
	public RelsMain findById(String relsUKey, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + relsUKey);
		Optional<RelsMain> relsMain = null;
		if (dbName.equals(ContentName.onDay))
			relsMain = relsMainReposDay.findById(relsUKey);
		else if (dbName.equals(ContentName.onMon))
			relsMain = relsMainReposMon.findById(relsUKey);
		else if (dbName.equals(ContentName.onHist))
			relsMain = relsMainReposHist.findById(relsUKey);
		else
			relsMain = relsMainRepos.findById(relsUKey);
		RelsMain obj = relsMain.isPresent() ? relsMain.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<RelsMain> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RelsMain> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "RelsUKey"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = relsMainReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = relsMainReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = relsMainReposHist.findAll(pageable);
		else
			slice = relsMainRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public RelsMain RelsIdFirst(String relsId_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("RelsIdFirst " + dbName + " : " + "relsId_0 : " + relsId_0);
		Optional<RelsMain> relsMainT = null;
		if (dbName.equals(ContentName.onDay))
			relsMainT = relsMainReposDay.findTopByRelsIdIs(relsId_0);
		else if (dbName.equals(ContentName.onMon))
			relsMainT = relsMainReposMon.findTopByRelsIdIs(relsId_0);
		else if (dbName.equals(ContentName.onHist))
			relsMainT = relsMainReposHist.findTopByRelsIdIs(relsId_0);
		else
			relsMainT = relsMainRepos.findTopByRelsIdIs(relsId_0);
		return relsMainT.isPresent() ? relsMainT.get() : null;
	}

	@Override
	public Slice<RelsMain> RelsPerson(int relsType_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RelsMain> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("RelsPerson " + dbName + " : " + "relsType_0 : " + relsType_0);
		if (dbName.equals(ContentName.onDay))
			slice = relsMainReposDay.findAllByRelsTypeIs(relsType_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = relsMainReposMon.findAllByRelsTypeIs(relsType_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = relsMainReposHist.findAllByRelsTypeIs(relsType_0, pageable);
		else
			slice = relsMainRepos.findAllByRelsTypeIs(relsType_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public RelsMain RelsNameFirst(String relsName_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("RelsNameFirst " + dbName + " : " + "relsName_0 : " + relsName_0);
		Optional<RelsMain> relsMainT = null;
		if (dbName.equals(ContentName.onDay))
			relsMainT = relsMainReposDay.findTopByRelsNameIs(relsName_0);
		else if (dbName.equals(ContentName.onMon))
			relsMainT = relsMainReposMon.findTopByRelsNameIs(relsName_0);
		else if (dbName.equals(ContentName.onHist))
			relsMainT = relsMainReposHist.findTopByRelsNameIs(relsName_0);
		else
			relsMainT = relsMainRepos.findTopByRelsNameIs(relsName_0);
		return relsMainT.isPresent() ? relsMainT.get() : null;
	}

	@Override
	public Slice<RelsMain> RelsNameEq(String relsName_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RelsMain> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("RelsNameEq " + dbName + " : " + "relsName_0 : " + relsName_0);
		if (dbName.equals(ContentName.onDay))
			slice = relsMainReposDay.findAllByRelsNameIs(relsName_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = relsMainReposMon.findAllByRelsNameIs(relsName_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = relsMainReposHist.findAllByRelsNameIs(relsName_0, pageable);
		else
			slice = relsMainRepos.findAllByRelsNameIs(relsName_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<RelsMain> RelsIdEq(String relsId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<RelsMain> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("RelsIdEq " + dbName + " : " + "relsId_0 : " + relsId_0);
		if (dbName.equals(ContentName.onDay))
			slice = relsMainReposDay.findAllByRelsIdIs(relsId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = relsMainReposMon.findAllByRelsIdIs(relsId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = relsMainReposHist.findAllByRelsIdIs(relsId_0, pageable);
		else
			slice = relsMainRepos.findAllByRelsIdIs(relsId_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public RelsMain holdById(String relsUKey, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + relsUKey);
		Optional<RelsMain> relsMain = null;
		if (dbName.equals(ContentName.onDay))
			relsMain = relsMainReposDay.findByRelsUKey(relsUKey);
		else if (dbName.equals(ContentName.onMon))
			relsMain = relsMainReposMon.findByRelsUKey(relsUKey);
		else if (dbName.equals(ContentName.onHist))
			relsMain = relsMainReposHist.findByRelsUKey(relsUKey);
		else
			relsMain = relsMainRepos.findByRelsUKey(relsUKey);
		return relsMain.isPresent() ? relsMain.get() : null;
	}

	@Override
	public RelsMain holdById(RelsMain relsMain, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + relsMain.getRelsUKey());
		Optional<RelsMain> relsMainT = null;
		if (dbName.equals(ContentName.onDay))
			relsMainT = relsMainReposDay.findByRelsUKey(relsMain.getRelsUKey());
		else if (dbName.equals(ContentName.onMon))
			relsMainT = relsMainReposMon.findByRelsUKey(relsMain.getRelsUKey());
		else if (dbName.equals(ContentName.onHist))
			relsMainT = relsMainReposHist.findByRelsUKey(relsMain.getRelsUKey());
		else
			relsMainT = relsMainRepos.findByRelsUKey(relsMain.getRelsUKey());
		return relsMainT.isPresent() ? relsMainT.get() : null;
	}

	@Override
	public RelsMain insert(RelsMain relsMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + relsMain.getRelsUKey());
		if (this.findById(relsMain.getRelsUKey()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			relsMain.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return relsMainReposDay.saveAndFlush(relsMain);
		else if (dbName.equals(ContentName.onMon))
			return relsMainReposMon.saveAndFlush(relsMain);
		else if (dbName.equals(ContentName.onHist))
			return relsMainReposHist.saveAndFlush(relsMain);
		else
			return relsMainRepos.saveAndFlush(relsMain);
	}

	@Override
	public RelsMain update(RelsMain relsMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + relsMain.getRelsUKey());
		if (!empNot.isEmpty())
			relsMain.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return relsMainReposDay.saveAndFlush(relsMain);
		else if (dbName.equals(ContentName.onMon))
			return relsMainReposMon.saveAndFlush(relsMain);
		else if (dbName.equals(ContentName.onHist))
			return relsMainReposHist.saveAndFlush(relsMain);
		else
			return relsMainRepos.saveAndFlush(relsMain);
	}

	@Override
	public RelsMain update2(RelsMain relsMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + relsMain.getRelsUKey());
		if (!empNot.isEmpty())
			relsMain.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			relsMainReposDay.saveAndFlush(relsMain);
		else if (dbName.equals(ContentName.onMon))
			relsMainReposMon.saveAndFlush(relsMain);
		else if (dbName.equals(ContentName.onHist))
			relsMainReposHist.saveAndFlush(relsMain);
		else
			relsMainRepos.saveAndFlush(relsMain);
		return this.findById(relsMain.getRelsUKey());
	}

	@Override
	public void delete(RelsMain relsMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + relsMain.getRelsUKey());
		if (dbName.equals(ContentName.onDay)) {
			relsMainReposDay.delete(relsMain);
			relsMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			relsMainReposMon.delete(relsMain);
			relsMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			relsMainReposHist.delete(relsMain);
			relsMainReposHist.flush();
		} else {
			relsMainRepos.delete(relsMain);
			relsMainRepos.flush();
		}
	}

	@Override
	public void insertAll(List<RelsMain> relsMain, TitaVo... titaVo) throws DBException {
		if (relsMain == null || relsMain.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (RelsMain t : relsMain)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			relsMain = relsMainReposDay.saveAll(relsMain);
			relsMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			relsMain = relsMainReposMon.saveAll(relsMain);
			relsMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			relsMain = relsMainReposHist.saveAll(relsMain);
			relsMainReposHist.flush();
		} else {
			relsMain = relsMainRepos.saveAll(relsMain);
			relsMainRepos.flush();
		}
	}

	@Override
	public void updateAll(List<RelsMain> relsMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (relsMain == null || relsMain.size() == 0)
			throw new DBException(6);

		for (RelsMain t : relsMain)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			relsMain = relsMainReposDay.saveAll(relsMain);
			relsMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			relsMain = relsMainReposMon.saveAll(relsMain);
			relsMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			relsMain = relsMainReposHist.saveAll(relsMain);
			relsMainReposHist.flush();
		} else {
			relsMain = relsMainRepos.saveAll(relsMain);
			relsMainRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<RelsMain> relsMain, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (relsMain == null || relsMain.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			relsMainReposDay.deleteAll(relsMain);
			relsMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			relsMainReposMon.deleteAll(relsMain);
			relsMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			relsMainReposHist.deleteAll(relsMain);
			relsMainReposHist.flush();
		} else {
			relsMainRepos.deleteAll(relsMain);
			relsMainRepos.flush();
		}
	}

}
