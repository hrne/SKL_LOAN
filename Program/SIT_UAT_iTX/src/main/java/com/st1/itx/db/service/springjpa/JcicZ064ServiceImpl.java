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
import com.st1.itx.db.domain.JcicZ064;
import com.st1.itx.db.domain.JcicZ064Id;
import com.st1.itx.db.repository.online.JcicZ064Repository;
import com.st1.itx.db.repository.day.JcicZ064RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ064RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ064RepositoryHist;
import com.st1.itx.db.service.JcicZ064Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ064Service")
@Repository
public class JcicZ064ServiceImpl implements JcicZ064Service, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(JcicZ064ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ064Repository jcicZ064Repos;

	@Autowired
	private JcicZ064RepositoryDay jcicZ064ReposDay;

	@Autowired
	private JcicZ064RepositoryMon jcicZ064ReposMon;

	@Autowired
	private JcicZ064RepositoryHist jcicZ064ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ064Repos);
		org.junit.Assert.assertNotNull(jcicZ064ReposDay);
		org.junit.Assert.assertNotNull(jcicZ064ReposMon);
		org.junit.Assert.assertNotNull(jcicZ064ReposHist);
	}

	@Override
	public JcicZ064 findById(JcicZ064Id jcicZ064Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + jcicZ064Id);
		Optional<JcicZ064> jcicZ064 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ064 = jcicZ064ReposDay.findById(jcicZ064Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ064 = jcicZ064ReposMon.findById(jcicZ064Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ064 = jcicZ064ReposHist.findById(jcicZ064Id);
		else
			jcicZ064 = jcicZ064Repos.findById(jcicZ064Id);
		JcicZ064 obj = jcicZ064.isPresent() ? jcicZ064.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ064> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ064> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ064ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ064ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ064ReposHist.findAll(pageable);
		else
			slice = jcicZ064Repos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ064> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ064> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ064ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ064ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ064ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else
			slice = jcicZ064Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ064> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ064> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ064ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ064ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ064ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else
			slice = jcicZ064Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ064> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ064> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " + applyDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ064ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ064ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ064ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else
			slice = jcicZ064Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ064 holdById(JcicZ064Id jcicZ064Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + jcicZ064Id);
		Optional<JcicZ064> jcicZ064 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ064 = jcicZ064ReposDay.findByJcicZ064Id(jcicZ064Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ064 = jcicZ064ReposMon.findByJcicZ064Id(jcicZ064Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ064 = jcicZ064ReposHist.findByJcicZ064Id(jcicZ064Id);
		else
			jcicZ064 = jcicZ064Repos.findByJcicZ064Id(jcicZ064Id);
		return jcicZ064.isPresent() ? jcicZ064.get() : null;
	}

	@Override
	public JcicZ064 holdById(JcicZ064 jcicZ064, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + jcicZ064.getJcicZ064Id());
		Optional<JcicZ064> jcicZ064T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ064T = jcicZ064ReposDay.findByJcicZ064Id(jcicZ064.getJcicZ064Id());
		else if (dbName.equals(ContentName.onMon))
			jcicZ064T = jcicZ064ReposMon.findByJcicZ064Id(jcicZ064.getJcicZ064Id());
		else if (dbName.equals(ContentName.onHist))
			jcicZ064T = jcicZ064ReposHist.findByJcicZ064Id(jcicZ064.getJcicZ064Id());
		else
			jcicZ064T = jcicZ064Repos.findByJcicZ064Id(jcicZ064.getJcicZ064Id());
		return jcicZ064T.isPresent() ? jcicZ064T.get() : null;
	}

	@Override
	public JcicZ064 insert(JcicZ064 jcicZ064, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + jcicZ064.getJcicZ064Id());
		if (this.findById(jcicZ064.getJcicZ064Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ064.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ064ReposDay.saveAndFlush(jcicZ064);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ064ReposMon.saveAndFlush(jcicZ064);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ064ReposHist.saveAndFlush(jcicZ064);
		else
			return jcicZ064Repos.saveAndFlush(jcicZ064);
	}

	@Override
	public JcicZ064 update(JcicZ064 jcicZ064, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + jcicZ064.getJcicZ064Id());
		if (!empNot.isEmpty())
			jcicZ064.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ064ReposDay.saveAndFlush(jcicZ064);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ064ReposMon.saveAndFlush(jcicZ064);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ064ReposHist.saveAndFlush(jcicZ064);
		else
			return jcicZ064Repos.saveAndFlush(jcicZ064);
	}

	@Override
	public JcicZ064 update2(JcicZ064 jcicZ064, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + jcicZ064.getJcicZ064Id());
		if (!empNot.isEmpty())
			jcicZ064.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ064ReposDay.saveAndFlush(jcicZ064);
		else if (dbName.equals(ContentName.onMon))
			jcicZ064ReposMon.saveAndFlush(jcicZ064);
		else if (dbName.equals(ContentName.onHist))
			jcicZ064ReposHist.saveAndFlush(jcicZ064);
		else
			jcicZ064Repos.saveAndFlush(jcicZ064);
		return this.findById(jcicZ064.getJcicZ064Id());
	}

	@Override
	public void delete(JcicZ064 jcicZ064, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + jcicZ064.getJcicZ064Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ064ReposDay.delete(jcicZ064);
			jcicZ064ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ064ReposMon.delete(jcicZ064);
			jcicZ064ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ064ReposHist.delete(jcicZ064);
			jcicZ064ReposHist.flush();
		} else {
			jcicZ064Repos.delete(jcicZ064);
			jcicZ064Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ064> jcicZ064, TitaVo... titaVo) throws DBException {
		if (jcicZ064 == null || jcicZ064.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (JcicZ064 t : jcicZ064)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ064 = jcicZ064ReposDay.saveAll(jcicZ064);
			jcicZ064ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ064 = jcicZ064ReposMon.saveAll(jcicZ064);
			jcicZ064ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ064 = jcicZ064ReposHist.saveAll(jcicZ064);
			jcicZ064ReposHist.flush();
		} else {
			jcicZ064 = jcicZ064Repos.saveAll(jcicZ064);
			jcicZ064Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ064> jcicZ064, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (jcicZ064 == null || jcicZ064.size() == 0)
			throw new DBException(6);

		for (JcicZ064 t : jcicZ064)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ064 = jcicZ064ReposDay.saveAll(jcicZ064);
			jcicZ064ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ064 = jcicZ064ReposMon.saveAll(jcicZ064);
			jcicZ064ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ064 = jcicZ064ReposHist.saveAll(jcicZ064);
			jcicZ064ReposHist.flush();
		} else {
			jcicZ064 = jcicZ064Repos.saveAll(jcicZ064);
			jcicZ064Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ064> jcicZ064, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ064 == null || jcicZ064.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ064ReposDay.deleteAll(jcicZ064);
			jcicZ064ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ064ReposMon.deleteAll(jcicZ064);
			jcicZ064ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ064ReposHist.deleteAll(jcicZ064);
			jcicZ064ReposHist.flush();
		} else {
			jcicZ064Repos.deleteAll(jcicZ064);
			jcicZ064Repos.flush();
		}
	}

}
