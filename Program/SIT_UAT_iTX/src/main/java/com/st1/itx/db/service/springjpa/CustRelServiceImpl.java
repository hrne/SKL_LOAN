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
import com.st1.itx.db.domain.CustRel;
import com.st1.itx.db.domain.CustRelId;
import com.st1.itx.db.repository.online.CustRelRepository;
import com.st1.itx.db.repository.day.CustRelRepositoryDay;
import com.st1.itx.db.repository.mon.CustRelRepositoryMon;
import com.st1.itx.db.repository.hist.CustRelRepositoryHist;
import com.st1.itx.db.service.CustRelService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("custRelService")
@Repository
public class CustRelServiceImpl implements CustRelService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(CustRelServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CustRelRepository custRelRepos;

	@Autowired
	private CustRelRepositoryDay custRelReposDay;

	@Autowired
	private CustRelRepositoryMon custRelReposMon;

	@Autowired
	private CustRelRepositoryHist custRelReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(custRelRepos);
		org.junit.Assert.assertNotNull(custRelReposDay);
		org.junit.Assert.assertNotNull(custRelReposMon);
		org.junit.Assert.assertNotNull(custRelReposHist);
	}

	@Override
	public CustRel findById(CustRelId custRelId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + custRelId);
		Optional<CustRel> custRel = null;
		if (dbName.equals(ContentName.onDay))
			custRel = custRelReposDay.findById(custRelId);
		else if (dbName.equals(ContentName.onMon))
			custRel = custRelReposMon.findById(custRelId);
		else if (dbName.equals(ContentName.onHist))
			custRel = custRelReposHist.findById(custRelId);
		else
			custRel = custRelRepos.findById(custRelId);
		CustRel obj = custRel.isPresent() ? custRel.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CustRel> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CustRel> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustUKey", "RelUKey"));
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = custRelReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = custRelReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = custRelReposHist.findAll(pageable);
		else
			slice = custRelRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CustRel> custUKeyEq(String custUKey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CustRel> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		logger.info("custUKeyEq " + dbName + " : " + "custUKey_0 : " + custUKey_0);
		if (dbName.equals(ContentName.onDay))
			slice = custRelReposDay.findAllByCustUKeyIs(custUKey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = custRelReposMon.findAllByCustUKeyIs(custUKey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = custRelReposHist.findAllByCustUKeyIs(custUKey_0, pageable);
		else
			slice = custRelRepos.findAllByCustUKeyIs(custUKey_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CustRel> relUKeyEq(String relUKey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CustRel> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		logger.info("relUKeyEq " + dbName + " : " + "relUKey_0 : " + relUKey_0);
		if (dbName.equals(ContentName.onDay))
			slice = custRelReposDay.findAllByRelUKeyIs(relUKey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = custRelReposMon.findAllByRelUKeyIs(relUKey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = custRelReposHist.findAllByRelUKeyIs(relUKey_0, pageable);
		else
			slice = custRelRepos.findAllByRelUKeyIs(relUKey_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CustRel> RelCodeLike(String relCode_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CustRel> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		logger.info("RelCodeLike " + dbName + " : " + "relCode_0 : " + relCode_0);
		if (dbName.equals(ContentName.onDay))
			slice = custRelReposDay.findAllByRelCodeLikeOrderByRelCodeAsc(relCode_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = custRelReposMon.findAllByRelCodeLikeOrderByRelCodeAsc(relCode_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = custRelReposHist.findAllByRelCodeLikeOrderByRelCodeAsc(relCode_0, pageable);
		else
			slice = custRelRepos.findAllByRelCodeLikeOrderByRelCodeAsc(relCode_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CustRel> findRelUKeyEq(String custUKey_0, String enable_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CustRel> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		logger.info("findRelUKeyEq " + dbName + " : " + "custUKey_0 : " + custUKey_0 + " enable_1 : " + enable_1);
		if (dbName.equals(ContentName.onDay))
			slice = custRelReposDay.findAllByCustUKeyIsAndEnableIs(custUKey_0, enable_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = custRelReposMon.findAllByCustUKeyIsAndEnableIs(custUKey_0, enable_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = custRelReposHist.findAllByCustUKeyIsAndEnableIs(custUKey_0, enable_1, pageable);
		else
			slice = custRelRepos.findAllByCustUKeyIsAndEnableIs(custUKey_0, enable_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CustRel holdById(CustRelId custRelId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + custRelId);
		Optional<CustRel> custRel = null;
		if (dbName.equals(ContentName.onDay))
			custRel = custRelReposDay.findByCustRelId(custRelId);
		else if (dbName.equals(ContentName.onMon))
			custRel = custRelReposMon.findByCustRelId(custRelId);
		else if (dbName.equals(ContentName.onHist))
			custRel = custRelReposHist.findByCustRelId(custRelId);
		else
			custRel = custRelRepos.findByCustRelId(custRelId);
		return custRel.isPresent() ? custRel.get() : null;
	}

	@Override
	public CustRel holdById(CustRel custRel, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + custRel.getCustRelId());
		Optional<CustRel> custRelT = null;
		if (dbName.equals(ContentName.onDay))
			custRelT = custRelReposDay.findByCustRelId(custRel.getCustRelId());
		else if (dbName.equals(ContentName.onMon))
			custRelT = custRelReposMon.findByCustRelId(custRel.getCustRelId());
		else if (dbName.equals(ContentName.onHist))
			custRelT = custRelReposHist.findByCustRelId(custRel.getCustRelId());
		else
			custRelT = custRelRepos.findByCustRelId(custRel.getCustRelId());
		return custRelT.isPresent() ? custRelT.get() : null;
	}

	@Override
	public CustRel insert(CustRel custRel, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		logger.info("Insert..." + dbName + " " + custRel.getCustRelId());
		if (this.findById(custRel.getCustRelId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			custRel.setCreateEmpNo(empNot);

		if (custRel.getLastUpdateEmpNo() == null || custRel.getLastUpdateEmpNo().isEmpty())
			custRel.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return custRelReposDay.saveAndFlush(custRel);
		else if (dbName.equals(ContentName.onMon))
			return custRelReposMon.saveAndFlush(custRel);
		else if (dbName.equals(ContentName.onHist))
			return custRelReposHist.saveAndFlush(custRel);
		else
			return custRelRepos.saveAndFlush(custRel);
	}

	@Override
	public CustRel update(CustRel custRel, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + custRel.getCustRelId());
		if (!empNot.isEmpty())
			custRel.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return custRelReposDay.saveAndFlush(custRel);
		else if (dbName.equals(ContentName.onMon))
			return custRelReposMon.saveAndFlush(custRel);
		else if (dbName.equals(ContentName.onHist))
			return custRelReposHist.saveAndFlush(custRel);
		else
			return custRelRepos.saveAndFlush(custRel);
	}

	@Override
	public CustRel update2(CustRel custRel, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + custRel.getCustRelId());
		if (!empNot.isEmpty())
			custRel.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			custRelReposDay.saveAndFlush(custRel);
		else if (dbName.equals(ContentName.onMon))
			custRelReposMon.saveAndFlush(custRel);
		else if (dbName.equals(ContentName.onHist))
			custRelReposHist.saveAndFlush(custRel);
		else
			custRelRepos.saveAndFlush(custRel);
		return this.findById(custRel.getCustRelId());
	}

	@Override
	public void delete(CustRel custRel, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + custRel.getCustRelId());
		if (dbName.equals(ContentName.onDay)) {
			custRelReposDay.delete(custRel);
			custRelReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			custRelReposMon.delete(custRel);
			custRelReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			custRelReposHist.delete(custRel);
			custRelReposHist.flush();
		} else {
			custRelRepos.delete(custRel);
			custRelRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CustRel> custRel, TitaVo... titaVo) throws DBException {
		if (custRel == null || custRel.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		logger.info("InsertAll...");
		for (CustRel t : custRel) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			custRel = custRelReposDay.saveAll(custRel);
			custRelReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			custRel = custRelReposMon.saveAll(custRel);
			custRelReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			custRel = custRelReposHist.saveAll(custRel);
			custRelReposHist.flush();
		} else {
			custRel = custRelRepos.saveAll(custRel);
			custRelRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CustRel> custRel, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (custRel == null || custRel.size() == 0)
			throw new DBException(6);

		for (CustRel t : custRel)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			custRel = custRelReposDay.saveAll(custRel);
			custRelReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			custRel = custRelReposMon.saveAll(custRel);
			custRelReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			custRel = custRelReposHist.saveAll(custRel);
			custRelReposHist.flush();
		} else {
			custRel = custRelRepos.saveAll(custRel);
			custRelRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CustRel> custRel, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (custRel == null || custRel.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			custRelReposDay.deleteAll(custRel);
			custRelReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			custRelReposMon.deleteAll(custRel);
			custRelReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			custRelReposHist.deleteAll(custRel);
			custRelReposHist.flush();
		} else {
			custRelRepos.deleteAll(custRel);
			custRelRepos.flush();
		}
	}

}
