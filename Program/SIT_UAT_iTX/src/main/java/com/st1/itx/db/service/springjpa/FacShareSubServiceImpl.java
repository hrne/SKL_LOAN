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
import com.st1.itx.db.domain.FacShareSub;
import com.st1.itx.db.domain.FacShareSubId;
import com.st1.itx.db.repository.online.FacShareSubRepository;
import com.st1.itx.db.repository.day.FacShareSubRepositoryDay;
import com.st1.itx.db.repository.mon.FacShareSubRepositoryMon;
import com.st1.itx.db.repository.hist.FacShareSubRepositoryHist;
import com.st1.itx.db.service.FacShareSubService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facShareSubService")
@Repository
public class FacShareSubServiceImpl implements FacShareSubService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(FacShareSubServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private FacShareSubRepository facShareSubRepos;

	@Autowired
	private FacShareSubRepositoryDay facShareSubReposDay;

	@Autowired
	private FacShareSubRepositoryMon facShareSubReposMon;

	@Autowired
	private FacShareSubRepositoryHist facShareSubReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(facShareSubRepos);
		org.junit.Assert.assertNotNull(facShareSubReposDay);
		org.junit.Assert.assertNotNull(facShareSubReposMon);
		org.junit.Assert.assertNotNull(facShareSubReposHist);
	}

	@Override
	public FacShareSub findById(FacShareSubId facShareSubId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + facShareSubId);
		Optional<FacShareSub> facShareSub = null;
		if (dbName.equals(ContentName.onDay))
			facShareSub = facShareSubReposDay.findById(facShareSubId);
		else if (dbName.equals(ContentName.onMon))
			facShareSub = facShareSubReposMon.findById(facShareSubId);
		else if (dbName.equals(ContentName.onHist))
			facShareSub = facShareSubReposHist.findById(facShareSubId);
		else
			facShareSub = facShareSubRepos.findById(facShareSubId);
		FacShareSub obj = facShareSub.isPresent() ? facShareSub.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<FacShareSub> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacShareSub> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ShareCustNo", "ShareFacmNo"));
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = facShareSubReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facShareSubReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facShareSubReposHist.findAll(pageable);
		else
			slice = facShareSubRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacShareSub> findMainIdEq(int creditSysNo_0, int mainCustNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacShareSub> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		logger.info("findMainIdEq " + dbName + " : " + "creditSysNo_0 : " + creditSysNo_0 + " mainCustNo_1 : " + mainCustNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = facShareSubReposDay.findAllByCreditSysNoIsAndMainCustNoIsOrderByShareSeqAscShareCustNoAscShareFacmNoAsc(creditSysNo_0, mainCustNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facShareSubReposMon.findAllByCreditSysNoIsAndMainCustNoIsOrderByShareSeqAscShareCustNoAscShareFacmNoAsc(creditSysNo_0, mainCustNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facShareSubReposHist.findAllByCreditSysNoIsAndMainCustNoIsOrderByShareSeqAscShareCustNoAscShareFacmNoAsc(creditSysNo_0, mainCustNo_1, pageable);
		else
			slice = facShareSubRepos.findAllByCreditSysNoIsAndMainCustNoIsOrderByShareSeqAscShareCustNoAscShareFacmNoAsc(creditSysNo_0, mainCustNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacShareSub> findCustNoEq(int shareCustNo_0, int shareFacmNo_1, int shareFacmNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacShareSub> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		logger.info("findCustNoEq " + dbName + " : " + "shareCustNo_0 : " + shareCustNo_0 + " shareFacmNo_1 : " + shareFacmNo_1 + " shareFacmNo_2 : " + shareFacmNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = facShareSubReposDay
					.findAllByShareCustNoIsAndShareFacmNoGreaterThanEqualAndShareFacmNoLessThanEqualOrderByCreditSysNoAscMainCustNoAscMainFacmNoAscShareSeqAscShareCustNoAscShareFacmNoAsc(
							shareCustNo_0, shareFacmNo_1, shareFacmNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facShareSubReposMon
					.findAllByShareCustNoIsAndShareFacmNoGreaterThanEqualAndShareFacmNoLessThanEqualOrderByCreditSysNoAscMainCustNoAscMainFacmNoAscShareSeqAscShareCustNoAscShareFacmNoAsc(
							shareCustNo_0, shareFacmNo_1, shareFacmNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facShareSubReposHist
					.findAllByShareCustNoIsAndShareFacmNoGreaterThanEqualAndShareFacmNoLessThanEqualOrderByCreditSysNoAscMainCustNoAscMainFacmNoAscShareSeqAscShareCustNoAscShareFacmNoAsc(
							shareCustNo_0, shareFacmNo_1, shareFacmNo_2, pageable);
		else
			slice = facShareSubRepos
					.findAllByShareCustNoIsAndShareFacmNoGreaterThanEqualAndShareFacmNoLessThanEqualOrderByCreditSysNoAscMainCustNoAscMainFacmNoAscShareSeqAscShareCustNoAscShareFacmNoAsc(
							shareCustNo_0, shareFacmNo_1, shareFacmNo_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacShareSub> findMainFacmNoEq(int mainCustNo_0, int mainFacmNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacShareSub> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		logger.info("findMainFacmNoEq " + dbName + " : " + "mainCustNo_0 : " + mainCustNo_0 + " mainFacmNo_1 : " + mainFacmNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = facShareSubReposDay.findAllByMainCustNoIsAndMainFacmNoIsOrderByShareSeqAscShareCustNoAscShareFacmNoAsc(mainCustNo_0, mainFacmNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facShareSubReposMon.findAllByMainCustNoIsAndMainFacmNoIsOrderByShareSeqAscShareCustNoAscShareFacmNoAsc(mainCustNo_0, mainFacmNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facShareSubReposHist.findAllByMainCustNoIsAndMainFacmNoIsOrderByShareSeqAscShareCustNoAscShareFacmNoAsc(mainCustNo_0, mainFacmNo_1, pageable);
		else
			slice = facShareSubRepos.findAllByMainCustNoIsAndMainFacmNoIsOrderByShareSeqAscShareCustNoAscShareFacmNoAsc(mainCustNo_0, mainFacmNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FacShareSub holdById(FacShareSubId facShareSubId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + facShareSubId);
		Optional<FacShareSub> facShareSub = null;
		if (dbName.equals(ContentName.onDay))
			facShareSub = facShareSubReposDay.findByFacShareSubId(facShareSubId);
		else if (dbName.equals(ContentName.onMon))
			facShareSub = facShareSubReposMon.findByFacShareSubId(facShareSubId);
		else if (dbName.equals(ContentName.onHist))
			facShareSub = facShareSubReposHist.findByFacShareSubId(facShareSubId);
		else
			facShareSub = facShareSubRepos.findByFacShareSubId(facShareSubId);
		return facShareSub.isPresent() ? facShareSub.get() : null;
	}

	@Override
	public FacShareSub holdById(FacShareSub facShareSub, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + facShareSub.getFacShareSubId());
		Optional<FacShareSub> facShareSubT = null;
		if (dbName.equals(ContentName.onDay))
			facShareSubT = facShareSubReposDay.findByFacShareSubId(facShareSub.getFacShareSubId());
		else if (dbName.equals(ContentName.onMon))
			facShareSubT = facShareSubReposMon.findByFacShareSubId(facShareSub.getFacShareSubId());
		else if (dbName.equals(ContentName.onHist))
			facShareSubT = facShareSubReposHist.findByFacShareSubId(facShareSub.getFacShareSubId());
		else
			facShareSubT = facShareSubRepos.findByFacShareSubId(facShareSub.getFacShareSubId());
		return facShareSubT.isPresent() ? facShareSubT.get() : null;
	}

	@Override
	public FacShareSub insert(FacShareSub facShareSub, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		logger.info("Insert..." + dbName + " " + facShareSub.getFacShareSubId());
		if (this.findById(facShareSub.getFacShareSubId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			facShareSub.setCreateEmpNo(empNot);

		if (facShareSub.getLastUpdateEmpNo() == null || facShareSub.getLastUpdateEmpNo().isEmpty())
			facShareSub.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facShareSubReposDay.saveAndFlush(facShareSub);
		else if (dbName.equals(ContentName.onMon))
			return facShareSubReposMon.saveAndFlush(facShareSub);
		else if (dbName.equals(ContentName.onHist))
			return facShareSubReposHist.saveAndFlush(facShareSub);
		else
			return facShareSubRepos.saveAndFlush(facShareSub);
	}

	@Override
	public FacShareSub update(FacShareSub facShareSub, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + facShareSub.getFacShareSubId());
		if (!empNot.isEmpty())
			facShareSub.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facShareSubReposDay.saveAndFlush(facShareSub);
		else if (dbName.equals(ContentName.onMon))
			return facShareSubReposMon.saveAndFlush(facShareSub);
		else if (dbName.equals(ContentName.onHist))
			return facShareSubReposHist.saveAndFlush(facShareSub);
		else
			return facShareSubRepos.saveAndFlush(facShareSub);
	}

	@Override
	public FacShareSub update2(FacShareSub facShareSub, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + facShareSub.getFacShareSubId());
		if (!empNot.isEmpty())
			facShareSub.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			facShareSubReposDay.saveAndFlush(facShareSub);
		else if (dbName.equals(ContentName.onMon))
			facShareSubReposMon.saveAndFlush(facShareSub);
		else if (dbName.equals(ContentName.onHist))
			facShareSubReposHist.saveAndFlush(facShareSub);
		else
			facShareSubRepos.saveAndFlush(facShareSub);
		return this.findById(facShareSub.getFacShareSubId());
	}

	@Override
	public void delete(FacShareSub facShareSub, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + facShareSub.getFacShareSubId());
		if (dbName.equals(ContentName.onDay)) {
			facShareSubReposDay.delete(facShareSub);
			facShareSubReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facShareSubReposMon.delete(facShareSub);
			facShareSubReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facShareSubReposHist.delete(facShareSub);
			facShareSubReposHist.flush();
		} else {
			facShareSubRepos.delete(facShareSub);
			facShareSubRepos.flush();
		}
	}

	@Override
	public void insertAll(List<FacShareSub> facShareSub, TitaVo... titaVo) throws DBException {
		if (facShareSub == null || facShareSub.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		logger.info("InsertAll...");
		for (FacShareSub t : facShareSub) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			facShareSub = facShareSubReposDay.saveAll(facShareSub);
			facShareSubReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facShareSub = facShareSubReposMon.saveAll(facShareSub);
			facShareSubReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facShareSub = facShareSubReposHist.saveAll(facShareSub);
			facShareSubReposHist.flush();
		} else {
			facShareSub = facShareSubRepos.saveAll(facShareSub);
			facShareSubRepos.flush();
		}
	}

	@Override
	public void updateAll(List<FacShareSub> facShareSub, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (facShareSub == null || facShareSub.size() == 0)
			throw new DBException(6);

		for (FacShareSub t : facShareSub)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			facShareSub = facShareSubReposDay.saveAll(facShareSub);
			facShareSubReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facShareSub = facShareSubReposMon.saveAll(facShareSub);
			facShareSubReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facShareSub = facShareSubReposHist.saveAll(facShareSub);
			facShareSubReposHist.flush();
		} else {
			facShareSub = facShareSubRepos.saveAll(facShareSub);
			facShareSubRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<FacShareSub> facShareSub, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (facShareSub == null || facShareSub.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			facShareSubReposDay.deleteAll(facShareSub);
			facShareSubReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facShareSubReposMon.deleteAll(facShareSub);
			facShareSubReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facShareSubReposHist.deleteAll(facShareSub);
			facShareSubReposHist.flush();
		} else {
			facShareSubRepos.deleteAll(facShareSub);
			facShareSubRepos.flush();
		}
	}

}
