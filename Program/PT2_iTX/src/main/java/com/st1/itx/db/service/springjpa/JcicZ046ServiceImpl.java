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
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ046Id;
import com.st1.itx.db.repository.online.JcicZ046Repository;
import com.st1.itx.db.repository.day.JcicZ046RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ046RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ046RepositoryHist;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ046Service")
@Repository
public class JcicZ046ServiceImpl extends ASpringJpaParm implements JcicZ046Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ046Repository jcicZ046Repos;

	@Autowired
	private JcicZ046RepositoryDay jcicZ046ReposDay;

	@Autowired
	private JcicZ046RepositoryMon jcicZ046ReposMon;

	@Autowired
	private JcicZ046RepositoryHist jcicZ046ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ046Repos);
		org.junit.Assert.assertNotNull(jcicZ046ReposDay);
		org.junit.Assert.assertNotNull(jcicZ046ReposMon);
		org.junit.Assert.assertNotNull(jcicZ046ReposHist);
	}

	@Override
	public JcicZ046 findById(JcicZ046Id jcicZ046Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ046Id);
		Optional<JcicZ046> jcicZ046 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ046 = jcicZ046ReposDay.findById(jcicZ046Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ046 = jcicZ046ReposMon.findById(jcicZ046Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ046 = jcicZ046ReposHist.findById(jcicZ046Id);
		else
			jcicZ046 = jcicZ046Repos.findById(jcicZ046Id);
		JcicZ046 obj = jcicZ046.isPresent() ? jcicZ046.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ046> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ046> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "CloseDate"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "CloseDate"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ046ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ046ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ046ReposHist.findAll(pageable);
		else
			slice = jcicZ046Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ046> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ046> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ046ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDescCloseDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ046ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDescCloseDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ046ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDescCloseDateDesc(custId_0, pageable);
		else
			slice = jcicZ046Repos.findAllByCustIdIsOrderByCustIdAscRcDateDescCloseDateDesc(custId_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ046> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ046> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("rcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ046ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDescCloseDateDesc(rcDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ046ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDescCloseDateDesc(rcDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ046ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDescCloseDateDesc(rcDate_0, pageable);
		else
			slice = jcicZ046Repos.findAllByRcDateIsOrderByCustIdAscRcDateDescCloseDateDesc(rcDate_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ046> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ046> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " + rcDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ046ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescCloseDateDesc(custId_0, rcDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ046ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescCloseDateDesc(custId_0, rcDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ046ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescCloseDateDesc(custId_0, rcDate_1, pageable);
		else
			slice = jcicZ046Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescCloseDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ046> hadZ046(String custId_0, int rcDate_1, String submitKey_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ046> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("hadZ046 " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " + rcDate_1 + " submitKey_2 : " + submitKey_2);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ046ReposDay.findAllByCustIdIsAndRcDateIsAndSubmitKeyIsOrderByCustIdAscRcDateDescCloseDateDesc(custId_0, rcDate_1, submitKey_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ046ReposMon.findAllByCustIdIsAndRcDateIsAndSubmitKeyIsOrderByCustIdAscRcDateDescCloseDateDesc(custId_0, rcDate_1, submitKey_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ046ReposHist.findAllByCustIdIsAndRcDateIsAndSubmitKeyIsOrderByCustIdAscRcDateDescCloseDateDesc(custId_0, rcDate_1, submitKey_2, pageable);
		else
			slice = jcicZ046Repos.findAllByCustIdIsAndRcDateIsAndSubmitKeyIsOrderByCustIdAscRcDateDescCloseDateDesc(custId_0, rcDate_1, submitKey_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ046> otherEq(String submitKey_0, String custId_1, int rcDate_2, int closeDate_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ046> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " + custId_1 + " rcDate_2 : " + rcDate_2 + " closeDate_3 : " + closeDate_3);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ046ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndCloseDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, closeDate_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ046ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndCloseDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, closeDate_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ046ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndCloseDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, closeDate_3, pageable);
		else
			slice = jcicZ046Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndCloseDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, closeDate_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ046 ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ046> jcicZ046T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ046T = jcicZ046ReposDay.findTopByUkeyIs(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ046T = jcicZ046ReposMon.findTopByUkeyIs(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ046T = jcicZ046ReposHist.findTopByUkeyIs(ukey_0);
		else
			jcicZ046T = jcicZ046Repos.findTopByUkeyIs(ukey_0);

		return jcicZ046T.isPresent() ? jcicZ046T.get() : null;
	}

	@Override
	public JcicZ046 otherFirst(String submitKey_0, String custId_1, int rcDate_2, int closeDate_3, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " + custId_1 + " rcDate_2 : " + rcDate_2 + " closeDate_3 : " + closeDate_3);
		Optional<JcicZ046> jcicZ046T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ046T = jcicZ046ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndCloseDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, closeDate_3);
		else if (dbName.equals(ContentName.onMon))
			jcicZ046T = jcicZ046ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndCloseDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, closeDate_3);
		else if (dbName.equals(ContentName.onHist))
			jcicZ046T = jcicZ046ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndCloseDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, closeDate_3);
		else
			jcicZ046T = jcicZ046Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndCloseDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, closeDate_3);

		return jcicZ046T.isPresent() ? jcicZ046T.get() : null;
	}

	@Override
	public JcicZ046 custIdFirst(String custId_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("custIdFirst " + dbName + " : " + "custId_0 : " + custId_0);
		Optional<JcicZ046> jcicZ046T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ046T = jcicZ046ReposDay.findTopByCustIdIsOrderByRcDateDesc(custId_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ046T = jcicZ046ReposMon.findTopByCustIdIsOrderByRcDateDesc(custId_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ046T = jcicZ046ReposHist.findTopByCustIdIsOrderByRcDateDesc(custId_0);
		else
			jcicZ046T = jcicZ046Repos.findTopByCustIdIsOrderByRcDateDesc(custId_0);

		return jcicZ046T.isPresent() ? jcicZ046T.get() : null;
	}

	@Override
	public JcicZ046 holdById(JcicZ046Id jcicZ046Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ046Id);
		Optional<JcicZ046> jcicZ046 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ046 = jcicZ046ReposDay.findByJcicZ046Id(jcicZ046Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ046 = jcicZ046ReposMon.findByJcicZ046Id(jcicZ046Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ046 = jcicZ046ReposHist.findByJcicZ046Id(jcicZ046Id);
		else
			jcicZ046 = jcicZ046Repos.findByJcicZ046Id(jcicZ046Id);
		return jcicZ046.isPresent() ? jcicZ046.get() : null;
	}

	@Override
	public JcicZ046 holdById(JcicZ046 jcicZ046, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ046.getJcicZ046Id());
		Optional<JcicZ046> jcicZ046T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ046T = jcicZ046ReposDay.findByJcicZ046Id(jcicZ046.getJcicZ046Id());
		else if (dbName.equals(ContentName.onMon))
			jcicZ046T = jcicZ046ReposMon.findByJcicZ046Id(jcicZ046.getJcicZ046Id());
		else if (dbName.equals(ContentName.onHist))
			jcicZ046T = jcicZ046ReposHist.findByJcicZ046Id(jcicZ046.getJcicZ046Id());
		else
			jcicZ046T = jcicZ046Repos.findByJcicZ046Id(jcicZ046.getJcicZ046Id());
		return jcicZ046T.isPresent() ? jcicZ046T.get() : null;
	}

	@Override
	public JcicZ046 insert(JcicZ046 jcicZ046, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ046.getJcicZ046Id());
		if (this.findById(jcicZ046.getJcicZ046Id(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ046.setCreateEmpNo(empNot);

		if (jcicZ046.getLastUpdateEmpNo() == null || jcicZ046.getLastUpdateEmpNo().isEmpty())
			jcicZ046.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ046ReposDay.saveAndFlush(jcicZ046);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ046ReposMon.saveAndFlush(jcicZ046);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ046ReposHist.saveAndFlush(jcicZ046);
		else
			return jcicZ046Repos.saveAndFlush(jcicZ046);
	}

	@Override
	public JcicZ046 update(JcicZ046 jcicZ046, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ046.getJcicZ046Id());
		if (!empNot.isEmpty())
			jcicZ046.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ046ReposDay.saveAndFlush(jcicZ046);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ046ReposMon.saveAndFlush(jcicZ046);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ046ReposHist.saveAndFlush(jcicZ046);
		else
			return jcicZ046Repos.saveAndFlush(jcicZ046);
	}

	@Override
	public JcicZ046 update2(JcicZ046 jcicZ046, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ046.getJcicZ046Id());
		if (!empNot.isEmpty())
			jcicZ046.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ046ReposDay.saveAndFlush(jcicZ046);
		else if (dbName.equals(ContentName.onMon))
			jcicZ046ReposMon.saveAndFlush(jcicZ046);
		else if (dbName.equals(ContentName.onHist))
			jcicZ046ReposHist.saveAndFlush(jcicZ046);
		else
			jcicZ046Repos.saveAndFlush(jcicZ046);
		return this.findById(jcicZ046.getJcicZ046Id());
	}

	@Override
	public void delete(JcicZ046 jcicZ046, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ046.getJcicZ046Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ046ReposDay.delete(jcicZ046);
			jcicZ046ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ046ReposMon.delete(jcicZ046);
			jcicZ046ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ046ReposHist.delete(jcicZ046);
			jcicZ046ReposHist.flush();
		} else {
			jcicZ046Repos.delete(jcicZ046);
			jcicZ046Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ046> jcicZ046, TitaVo... titaVo) throws DBException {
		if (jcicZ046 == null || jcicZ046.size() == 0)
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
		for (JcicZ046 t : jcicZ046) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ046 = jcicZ046ReposDay.saveAll(jcicZ046);
			jcicZ046ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ046 = jcicZ046ReposMon.saveAll(jcicZ046);
			jcicZ046ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ046 = jcicZ046ReposHist.saveAll(jcicZ046);
			jcicZ046ReposHist.flush();
		} else {
			jcicZ046 = jcicZ046Repos.saveAll(jcicZ046);
			jcicZ046Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ046> jcicZ046, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ046 == null || jcicZ046.size() == 0)
			throw new DBException(6);

		for (JcicZ046 t : jcicZ046)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ046 = jcicZ046ReposDay.saveAll(jcicZ046);
			jcicZ046ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ046 = jcicZ046ReposMon.saveAll(jcicZ046);
			jcicZ046ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ046 = jcicZ046ReposHist.saveAll(jcicZ046);
			jcicZ046ReposHist.flush();
		} else {
			jcicZ046 = jcicZ046Repos.saveAll(jcicZ046);
			jcicZ046Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ046> jcicZ046, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ046 == null || jcicZ046.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ046ReposDay.deleteAll(jcicZ046);
			jcicZ046ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ046ReposMon.deleteAll(jcicZ046);
			jcicZ046ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ046ReposHist.deleteAll(jcicZ046);
			jcicZ046ReposHist.flush();
		} else {
			jcicZ046Repos.deleteAll(jcicZ046);
			jcicZ046Repos.flush();
		}
	}

}
