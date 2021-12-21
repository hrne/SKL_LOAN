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
import com.st1.itx.db.domain.JcicZ444;
import com.st1.itx.db.domain.JcicZ444Id;
import com.st1.itx.db.repository.online.JcicZ444Repository;
import com.st1.itx.db.repository.day.JcicZ444RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ444RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ444RepositoryHist;
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ444Service")
@Repository
public class JcicZ444ServiceImpl extends ASpringJpaParm implements JcicZ444Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ444Repository jcicZ444Repos;

	@Autowired
	private JcicZ444RepositoryDay jcicZ444ReposDay;

	@Autowired
	private JcicZ444RepositoryMon jcicZ444ReposMon;

	@Autowired
	private JcicZ444RepositoryHist jcicZ444ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ444Repos);
		org.junit.Assert.assertNotNull(jcicZ444ReposDay);
		org.junit.Assert.assertNotNull(jcicZ444ReposMon);
		org.junit.Assert.assertNotNull(jcicZ444ReposHist);
	}

	@Override
	public JcicZ444 findById(JcicZ444Id jcicZ444Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ444Id);
		Optional<JcicZ444> jcicZ444 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ444 = jcicZ444ReposDay.findById(jcicZ444Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ444 = jcicZ444ReposMon.findById(jcicZ444Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ444 = jcicZ444ReposHist.findById(jcicZ444Id);
		else
			jcicZ444 = jcicZ444Repos.findById(jcicZ444Id);
		JcicZ444 obj = jcicZ444.isPresent() ? jcicZ444.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ444> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ444> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ444ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ444ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ444ReposHist.findAll(pageable);
		else
			slice = jcicZ444Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ444> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ444> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ444ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ444ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ444ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else
			slice = jcicZ444Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ444> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ444> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("rcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ444ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ444ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ444ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else
			slice = jcicZ444Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ444> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ444> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " + applyDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ444ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ444ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ444ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else
			slice = jcicZ444Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ444> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ444> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " + custId_1 + " applyDate_2 : " + applyDate_2 + " courtCode_3 : " + courtCode_3);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ444ReposDay.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ444ReposMon.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ444ReposHist.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, pageable);
		else
			slice = jcicZ444Repos.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ444 ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ444> jcicZ444T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ444T = jcicZ444ReposDay.findTopByUkeyIs(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ444T = jcicZ444ReposMon.findTopByUkeyIs(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ444T = jcicZ444ReposHist.findTopByUkeyIs(ukey_0);
		else
			jcicZ444T = jcicZ444Repos.findTopByUkeyIs(ukey_0);

		return jcicZ444T.isPresent() ? jcicZ444T.get() : null;
	}

	@Override
	public JcicZ444 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " + custId_1 + " applyDate_2 : " + applyDate_2 + " courtCode_3 : " + courtCode_3);
		Optional<JcicZ444> jcicZ444T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ444T = jcicZ444ReposDay.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3);
		else if (dbName.equals(ContentName.onMon))
			jcicZ444T = jcicZ444ReposMon.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3);
		else if (dbName.equals(ContentName.onHist))
			jcicZ444T = jcicZ444ReposHist.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3);
		else
			jcicZ444T = jcicZ444Repos.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3);

		return jcicZ444T.isPresent() ? jcicZ444T.get() : null;
	}

	@Override
	public JcicZ444 holdById(JcicZ444Id jcicZ444Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ444Id);
		Optional<JcicZ444> jcicZ444 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ444 = jcicZ444ReposDay.findByJcicZ444Id(jcicZ444Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ444 = jcicZ444ReposMon.findByJcicZ444Id(jcicZ444Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ444 = jcicZ444ReposHist.findByJcicZ444Id(jcicZ444Id);
		else
			jcicZ444 = jcicZ444Repos.findByJcicZ444Id(jcicZ444Id);
		return jcicZ444.isPresent() ? jcicZ444.get() : null;
	}

	@Override
	public JcicZ444 holdById(JcicZ444 jcicZ444, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ444.getJcicZ444Id());
		Optional<JcicZ444> jcicZ444T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ444T = jcicZ444ReposDay.findByJcicZ444Id(jcicZ444.getJcicZ444Id());
		else if (dbName.equals(ContentName.onMon))
			jcicZ444T = jcicZ444ReposMon.findByJcicZ444Id(jcicZ444.getJcicZ444Id());
		else if (dbName.equals(ContentName.onHist))
			jcicZ444T = jcicZ444ReposHist.findByJcicZ444Id(jcicZ444.getJcicZ444Id());
		else
			jcicZ444T = jcicZ444Repos.findByJcicZ444Id(jcicZ444.getJcicZ444Id());
		return jcicZ444T.isPresent() ? jcicZ444T.get() : null;
	}

	@Override
	public JcicZ444 insert(JcicZ444 jcicZ444, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ444.getJcicZ444Id());
		if (this.findById(jcicZ444.getJcicZ444Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ444.setCreateEmpNo(empNot);

		if (jcicZ444.getLastUpdateEmpNo() == null || jcicZ444.getLastUpdateEmpNo().isEmpty())
			jcicZ444.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ444ReposDay.saveAndFlush(jcicZ444);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ444ReposMon.saveAndFlush(jcicZ444);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ444ReposHist.saveAndFlush(jcicZ444);
		else
			return jcicZ444Repos.saveAndFlush(jcicZ444);
	}

	@Override
	public JcicZ444 update(JcicZ444 jcicZ444, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ444.getJcicZ444Id());
		if (!empNot.isEmpty())
			jcicZ444.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ444ReposDay.saveAndFlush(jcicZ444);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ444ReposMon.saveAndFlush(jcicZ444);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ444ReposHist.saveAndFlush(jcicZ444);
		else
			return jcicZ444Repos.saveAndFlush(jcicZ444);
	}

	@Override
	public JcicZ444 update2(JcicZ444 jcicZ444, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ444.getJcicZ444Id());
		if (!empNot.isEmpty())
			jcicZ444.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ444ReposDay.saveAndFlush(jcicZ444);
		else if (dbName.equals(ContentName.onMon))
			jcicZ444ReposMon.saveAndFlush(jcicZ444);
		else if (dbName.equals(ContentName.onHist))
			jcicZ444ReposHist.saveAndFlush(jcicZ444);
		else
			jcicZ444Repos.saveAndFlush(jcicZ444);
		return this.findById(jcicZ444.getJcicZ444Id());
	}

	@Override
	public void delete(JcicZ444 jcicZ444, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ444.getJcicZ444Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ444ReposDay.delete(jcicZ444);
			jcicZ444ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ444ReposMon.delete(jcicZ444);
			jcicZ444ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ444ReposHist.delete(jcicZ444);
			jcicZ444ReposHist.flush();
		} else {
			jcicZ444Repos.delete(jcicZ444);
			jcicZ444Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ444> jcicZ444, TitaVo... titaVo) throws DBException {
		if (jcicZ444 == null || jcicZ444.size() == 0)
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
		for (JcicZ444 t : jcicZ444) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ444 = jcicZ444ReposDay.saveAll(jcicZ444);
			jcicZ444ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ444 = jcicZ444ReposMon.saveAll(jcicZ444);
			jcicZ444ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ444 = jcicZ444ReposHist.saveAll(jcicZ444);
			jcicZ444ReposHist.flush();
		} else {
			jcicZ444 = jcicZ444Repos.saveAll(jcicZ444);
			jcicZ444Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ444> jcicZ444, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ444 == null || jcicZ444.size() == 0)
			throw new DBException(6);

		for (JcicZ444 t : jcicZ444)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ444 = jcicZ444ReposDay.saveAll(jcicZ444);
			jcicZ444ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ444 = jcicZ444ReposMon.saveAll(jcicZ444);
			jcicZ444ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ444 = jcicZ444ReposHist.saveAll(jcicZ444);
			jcicZ444ReposHist.flush();
		} else {
			jcicZ444 = jcicZ444Repos.saveAll(jcicZ444);
			jcicZ444Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ444> jcicZ444, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ444 == null || jcicZ444.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ444ReposDay.deleteAll(jcicZ444);
			jcicZ444ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ444ReposMon.deleteAll(jcicZ444);
			jcicZ444ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ444ReposHist.deleteAll(jcicZ444);
			jcicZ444ReposHist.flush();
		} else {
			jcicZ444Repos.deleteAll(jcicZ444);
			jcicZ444Repos.flush();
		}
	}

}
