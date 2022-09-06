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
import com.st1.itx.db.domain.JcicZ574;
import com.st1.itx.db.domain.JcicZ574Id;
import com.st1.itx.db.repository.online.JcicZ574Repository;
import com.st1.itx.db.repository.day.JcicZ574RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ574RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ574RepositoryHist;
import com.st1.itx.db.service.JcicZ574Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ574Service")
@Repository
public class JcicZ574ServiceImpl extends ASpringJpaParm implements JcicZ574Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ574Repository jcicZ574Repos;

	@Autowired
	private JcicZ574RepositoryDay jcicZ574ReposDay;

	@Autowired
	private JcicZ574RepositoryMon jcicZ574ReposMon;

	@Autowired
	private JcicZ574RepositoryHist jcicZ574ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ574Repos);
		org.junit.Assert.assertNotNull(jcicZ574ReposDay);
		org.junit.Assert.assertNotNull(jcicZ574ReposMon);
		org.junit.Assert.assertNotNull(jcicZ574ReposHist);
	}

	@Override
	public JcicZ574 findById(JcicZ574Id jcicZ574Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ574Id);
		Optional<JcicZ574> jcicZ574 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ574 = jcicZ574ReposDay.findById(jcicZ574Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ574 = jcicZ574ReposMon.findById(jcicZ574Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ574 = jcicZ574ReposHist.findById(jcicZ574Id);
		else
			jcicZ574 = jcicZ574Repos.findById(jcicZ574Id);
		JcicZ574 obj = jcicZ574.isPresent() ? jcicZ574.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ574> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ574> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustId", "SubmitKey", "ApplyDate"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustId", "SubmitKey", "ApplyDate"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ574ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ574ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ574ReposHist.findAll(pageable);
		else
			slice = jcicZ574Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ574> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ574> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ574ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ574ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ574ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else
			slice = jcicZ574Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ574> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ574> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("rcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ574ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ574ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ574ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else
			slice = jcicZ574Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ574> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ574> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " + applyDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ574ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ574ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ574ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else
			slice = jcicZ574Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ574 ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ574> jcicZ574T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ574T = jcicZ574ReposDay.findTopByUkeyIs(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ574T = jcicZ574ReposMon.findTopByUkeyIs(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ574T = jcicZ574ReposHist.findTopByUkeyIs(ukey_0);
		else
			jcicZ574T = jcicZ574Repos.findTopByUkeyIs(ukey_0);

		return jcicZ574T.isPresent() ? jcicZ574T.get() : null;
	}

	@Override
	public Slice<JcicZ574> otherEq(String custId_0, int applyDate_1, String submitKey_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ574> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("otherEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " + applyDate_1 + " submitKey_2 : " + submitKey_2);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ574ReposDay.findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ574ReposMon.findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ574ReposHist.findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, pageable);
		else
			slice = jcicZ574Repos.findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ574 otherFirst(String custId_0, int applyDate_1, String submitKey_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("otherFirst " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " + applyDate_1 + " submitKey_2 : " + submitKey_2);
		Optional<JcicZ574> jcicZ574T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ574T = jcicZ574ReposDay.findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2);
		else if (dbName.equals(ContentName.onMon))
			jcicZ574T = jcicZ574ReposMon.findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2);
		else if (dbName.equals(ContentName.onHist))
			jcicZ574T = jcicZ574ReposHist.findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2);
		else
			jcicZ574T = jcicZ574Repos.findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2);

		return jcicZ574T.isPresent() ? jcicZ574T.get() : null;
	}

	@Override
	public JcicZ574 holdById(JcicZ574Id jcicZ574Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ574Id);
		Optional<JcicZ574> jcicZ574 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ574 = jcicZ574ReposDay.findByJcicZ574Id(jcicZ574Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ574 = jcicZ574ReposMon.findByJcicZ574Id(jcicZ574Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ574 = jcicZ574ReposHist.findByJcicZ574Id(jcicZ574Id);
		else
			jcicZ574 = jcicZ574Repos.findByJcicZ574Id(jcicZ574Id);
		return jcicZ574.isPresent() ? jcicZ574.get() : null;
	}

	@Override
	public JcicZ574 holdById(JcicZ574 jcicZ574, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ574.getJcicZ574Id());
		Optional<JcicZ574> jcicZ574T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ574T = jcicZ574ReposDay.findByJcicZ574Id(jcicZ574.getJcicZ574Id());
		else if (dbName.equals(ContentName.onMon))
			jcicZ574T = jcicZ574ReposMon.findByJcicZ574Id(jcicZ574.getJcicZ574Id());
		else if (dbName.equals(ContentName.onHist))
			jcicZ574T = jcicZ574ReposHist.findByJcicZ574Id(jcicZ574.getJcicZ574Id());
		else
			jcicZ574T = jcicZ574Repos.findByJcicZ574Id(jcicZ574.getJcicZ574Id());
		return jcicZ574T.isPresent() ? jcicZ574T.get() : null;
	}

	@Override
	public JcicZ574 insert(JcicZ574 jcicZ574, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ574.getJcicZ574Id());
		if (this.findById(jcicZ574.getJcicZ574Id(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ574.setCreateEmpNo(empNot);

		if (jcicZ574.getLastUpdateEmpNo() == null || jcicZ574.getLastUpdateEmpNo().isEmpty())
			jcicZ574.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ574ReposDay.saveAndFlush(jcicZ574);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ574ReposMon.saveAndFlush(jcicZ574);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ574ReposHist.saveAndFlush(jcicZ574);
		else
			return jcicZ574Repos.saveAndFlush(jcicZ574);
	}

	@Override
	public JcicZ574 update(JcicZ574 jcicZ574, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ574.getJcicZ574Id());
		if (!empNot.isEmpty())
			jcicZ574.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ574ReposDay.saveAndFlush(jcicZ574);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ574ReposMon.saveAndFlush(jcicZ574);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ574ReposHist.saveAndFlush(jcicZ574);
		else
			return jcicZ574Repos.saveAndFlush(jcicZ574);
	}

	@Override
	public JcicZ574 update2(JcicZ574 jcicZ574, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ574.getJcicZ574Id());
		if (!empNot.isEmpty())
			jcicZ574.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ574ReposDay.saveAndFlush(jcicZ574);
		else if (dbName.equals(ContentName.onMon))
			jcicZ574ReposMon.saveAndFlush(jcicZ574);
		else if (dbName.equals(ContentName.onHist))
			jcicZ574ReposHist.saveAndFlush(jcicZ574);
		else
			jcicZ574Repos.saveAndFlush(jcicZ574);
		return this.findById(jcicZ574.getJcicZ574Id());
	}

	@Override
	public void delete(JcicZ574 jcicZ574, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ574.getJcicZ574Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ574ReposDay.delete(jcicZ574);
			jcicZ574ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ574ReposMon.delete(jcicZ574);
			jcicZ574ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ574ReposHist.delete(jcicZ574);
			jcicZ574ReposHist.flush();
		} else {
			jcicZ574Repos.delete(jcicZ574);
			jcicZ574Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ574> jcicZ574, TitaVo... titaVo) throws DBException {
		if (jcicZ574 == null || jcicZ574.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("InsertAll...");
		for (JcicZ574 t : jcicZ574) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ574 = jcicZ574ReposDay.saveAll(jcicZ574);
			jcicZ574ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ574 = jcicZ574ReposMon.saveAll(jcicZ574);
			jcicZ574ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ574 = jcicZ574ReposHist.saveAll(jcicZ574);
			jcicZ574ReposHist.flush();
		} else {
			jcicZ574 = jcicZ574Repos.saveAll(jcicZ574);
			jcicZ574Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ574> jcicZ574, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ574 == null || jcicZ574.size() == 0)
			throw new DBException(6);

		for (JcicZ574 t : jcicZ574)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ574 = jcicZ574ReposDay.saveAll(jcicZ574);
			jcicZ574ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ574 = jcicZ574ReposMon.saveAll(jcicZ574);
			jcicZ574ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ574 = jcicZ574ReposHist.saveAll(jcicZ574);
			jcicZ574ReposHist.flush();
		} else {
			jcicZ574 = jcicZ574Repos.saveAll(jcicZ574);
			jcicZ574Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ574> jcicZ574, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ574 == null || jcicZ574.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ574ReposDay.deleteAll(jcicZ574);
			jcicZ574ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ574ReposMon.deleteAll(jcicZ574);
			jcicZ574ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ574ReposHist.deleteAll(jcicZ574);
			jcicZ574ReposHist.flush();
		} else {
			jcicZ574Repos.deleteAll(jcicZ574);
			jcicZ574Repos.flush();
		}
	}

}
