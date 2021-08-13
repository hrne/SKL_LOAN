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
import com.st1.itx.db.domain.JcicZ069;
import com.st1.itx.db.domain.JcicZ069Id;
import com.st1.itx.db.repository.online.JcicZ069Repository;
import com.st1.itx.db.repository.day.JcicZ069RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ069RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ069RepositoryHist;
import com.st1.itx.db.service.JcicZ069Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ069Service")
@Repository
public class JcicZ069ServiceImpl implements JcicZ069Service, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(JcicZ069ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ069Repository jcicZ069Repos;

	@Autowired
	private JcicZ069RepositoryDay jcicZ069ReposDay;

	@Autowired
	private JcicZ069RepositoryMon jcicZ069ReposMon;

	@Autowired
	private JcicZ069RepositoryHist jcicZ069ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ069Repos);
		org.junit.Assert.assertNotNull(jcicZ069ReposDay);
		org.junit.Assert.assertNotNull(jcicZ069ReposMon);
		org.junit.Assert.assertNotNull(jcicZ069ReposHist);
	}

	@Override
	public JcicZ069 findById(JcicZ069Id jcicZ069Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + jcicZ069Id);
		Optional<JcicZ069> jcicZ069 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ069 = jcicZ069ReposDay.findById(jcicZ069Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ069 = jcicZ069ReposMon.findById(jcicZ069Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ069 = jcicZ069ReposHist.findById(jcicZ069Id);
		else
			jcicZ069 = jcicZ069Repos.findById(jcicZ069Id);
		JcicZ069 obj = jcicZ069.isPresent() ? jcicZ069.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ069> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ069> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "BankId"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ069ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ069ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ069ReposHist.findAll(pageable);
		else
			slice = jcicZ069Repos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ069> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ069> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ069ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ069ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ069ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else
			slice = jcicZ069Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ069> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ069> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ069ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ069ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ069ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else
			slice = jcicZ069Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ069> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ069> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " + applyDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ069ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ069ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ069ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else
			slice = jcicZ069Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ069 holdById(JcicZ069Id jcicZ069Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + jcicZ069Id);
		Optional<JcicZ069> jcicZ069 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ069 = jcicZ069ReposDay.findByJcicZ069Id(jcicZ069Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ069 = jcicZ069ReposMon.findByJcicZ069Id(jcicZ069Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ069 = jcicZ069ReposHist.findByJcicZ069Id(jcicZ069Id);
		else
			jcicZ069 = jcicZ069Repos.findByJcicZ069Id(jcicZ069Id);
		return jcicZ069.isPresent() ? jcicZ069.get() : null;
	}

	@Override
	public JcicZ069 holdById(JcicZ069 jcicZ069, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + jcicZ069.getJcicZ069Id());
		Optional<JcicZ069> jcicZ069T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ069T = jcicZ069ReposDay.findByJcicZ069Id(jcicZ069.getJcicZ069Id());
		else if (dbName.equals(ContentName.onMon))
			jcicZ069T = jcicZ069ReposMon.findByJcicZ069Id(jcicZ069.getJcicZ069Id());
		else if (dbName.equals(ContentName.onHist))
			jcicZ069T = jcicZ069ReposHist.findByJcicZ069Id(jcicZ069.getJcicZ069Id());
		else
			jcicZ069T = jcicZ069Repos.findByJcicZ069Id(jcicZ069.getJcicZ069Id());
		return jcicZ069T.isPresent() ? jcicZ069T.get() : null;
	}

	@Override
	public JcicZ069 insert(JcicZ069 jcicZ069, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + jcicZ069.getJcicZ069Id());
		if (this.findById(jcicZ069.getJcicZ069Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ069.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ069ReposDay.saveAndFlush(jcicZ069);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ069ReposMon.saveAndFlush(jcicZ069);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ069ReposHist.saveAndFlush(jcicZ069);
		else
			return jcicZ069Repos.saveAndFlush(jcicZ069);
	}

	@Override
	public JcicZ069 update(JcicZ069 jcicZ069, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + jcicZ069.getJcicZ069Id());
		if (!empNot.isEmpty())
			jcicZ069.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ069ReposDay.saveAndFlush(jcicZ069);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ069ReposMon.saveAndFlush(jcicZ069);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ069ReposHist.saveAndFlush(jcicZ069);
		else
			return jcicZ069Repos.saveAndFlush(jcicZ069);
	}

	@Override
	public JcicZ069 update2(JcicZ069 jcicZ069, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + jcicZ069.getJcicZ069Id());
		if (!empNot.isEmpty())
			jcicZ069.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ069ReposDay.saveAndFlush(jcicZ069);
		else if (dbName.equals(ContentName.onMon))
			jcicZ069ReposMon.saveAndFlush(jcicZ069);
		else if (dbName.equals(ContentName.onHist))
			jcicZ069ReposHist.saveAndFlush(jcicZ069);
		else
			jcicZ069Repos.saveAndFlush(jcicZ069);
		return this.findById(jcicZ069.getJcicZ069Id());
	}

	@Override
	public void delete(JcicZ069 jcicZ069, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + jcicZ069.getJcicZ069Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ069ReposDay.delete(jcicZ069);
			jcicZ069ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ069ReposMon.delete(jcicZ069);
			jcicZ069ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ069ReposHist.delete(jcicZ069);
			jcicZ069ReposHist.flush();
		} else {
			jcicZ069Repos.delete(jcicZ069);
			jcicZ069Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ069> jcicZ069, TitaVo... titaVo) throws DBException {
		if (jcicZ069 == null || jcicZ069.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (JcicZ069 t : jcicZ069)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ069 = jcicZ069ReposDay.saveAll(jcicZ069);
			jcicZ069ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ069 = jcicZ069ReposMon.saveAll(jcicZ069);
			jcicZ069ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ069 = jcicZ069ReposHist.saveAll(jcicZ069);
			jcicZ069ReposHist.flush();
		} else {
			jcicZ069 = jcicZ069Repos.saveAll(jcicZ069);
			jcicZ069Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ069> jcicZ069, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (jcicZ069 == null || jcicZ069.size() == 0)
			throw new DBException(6);

		for (JcicZ069 t : jcicZ069)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ069 = jcicZ069ReposDay.saveAll(jcicZ069);
			jcicZ069ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ069 = jcicZ069ReposMon.saveAll(jcicZ069);
			jcicZ069ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ069 = jcicZ069ReposHist.saveAll(jcicZ069);
			jcicZ069ReposHist.flush();
		} else {
			jcicZ069 = jcicZ069Repos.saveAll(jcicZ069);
			jcicZ069Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ069> jcicZ069, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ069 == null || jcicZ069.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ069ReposDay.deleteAll(jcicZ069);
			jcicZ069ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ069ReposMon.deleteAll(jcicZ069);
			jcicZ069ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ069ReposHist.deleteAll(jcicZ069);
			jcicZ069ReposHist.flush();
		} else {
			jcicZ069Repos.deleteAll(jcicZ069);
			jcicZ069Repos.flush();
		}
	}

}
