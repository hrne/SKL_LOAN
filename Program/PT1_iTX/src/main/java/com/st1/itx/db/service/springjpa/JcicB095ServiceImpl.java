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
import com.st1.itx.db.domain.JcicB095;
import com.st1.itx.db.domain.JcicB095Id;
import com.st1.itx.db.repository.online.JcicB095Repository;
import com.st1.itx.db.repository.day.JcicB095RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB095RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB095RepositoryHist;
import com.st1.itx.db.service.JcicB095Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB095Service")
@Repository
public class JcicB095ServiceImpl extends ASpringJpaParm implements JcicB095Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicB095Repository jcicB095Repos;

	@Autowired
	private JcicB095RepositoryDay jcicB095ReposDay;

	@Autowired
	private JcicB095RepositoryMon jcicB095ReposMon;

	@Autowired
	private JcicB095RepositoryHist jcicB095ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicB095Repos);
		org.junit.Assert.assertNotNull(jcicB095ReposDay);
		org.junit.Assert.assertNotNull(jcicB095ReposMon);
		org.junit.Assert.assertNotNull(jcicB095ReposHist);
	}

	@Override
	public JcicB095 findById(JcicB095Id jcicB095Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicB095Id);
		Optional<JcicB095> jcicB095 = null;
		if (dbName.equals(ContentName.onDay))
			jcicB095 = jcicB095ReposDay.findById(jcicB095Id);
		else if (dbName.equals(ContentName.onMon))
			jcicB095 = jcicB095ReposMon.findById(jcicB095Id);
		else if (dbName.equals(ContentName.onHist))
			jcicB095 = jcicB095ReposHist.findById(jcicB095Id);
		else
			jcicB095 = jcicB095Repos.findById(jcicB095Id);
		JcicB095 obj = jcicB095.isPresent() ? jcicB095.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicB095> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicB095> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "ClActNo", "OwnerId", "CityJCICCode", "AreaJCICCode", "IrCode", "BdNo1", "BdNo2"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "ClActNo", "OwnerId", "CityJCICCode", "AreaJCICCode", "IrCode", "BdNo1", "BdNo2"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicB095ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicB095ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicB095ReposHist.findAll(pageable);
		else
			slice = jcicB095Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicB095 holdById(JcicB095Id jcicB095Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicB095Id);
		Optional<JcicB095> jcicB095 = null;
		if (dbName.equals(ContentName.onDay))
			jcicB095 = jcicB095ReposDay.findByJcicB095Id(jcicB095Id);
		else if (dbName.equals(ContentName.onMon))
			jcicB095 = jcicB095ReposMon.findByJcicB095Id(jcicB095Id);
		else if (dbName.equals(ContentName.onHist))
			jcicB095 = jcicB095ReposHist.findByJcicB095Id(jcicB095Id);
		else
			jcicB095 = jcicB095Repos.findByJcicB095Id(jcicB095Id);
		return jcicB095.isPresent() ? jcicB095.get() : null;
	}

	@Override
	public JcicB095 holdById(JcicB095 jcicB095, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicB095.getJcicB095Id());
		Optional<JcicB095> jcicB095T = null;
		if (dbName.equals(ContentName.onDay))
			jcicB095T = jcicB095ReposDay.findByJcicB095Id(jcicB095.getJcicB095Id());
		else if (dbName.equals(ContentName.onMon))
			jcicB095T = jcicB095ReposMon.findByJcicB095Id(jcicB095.getJcicB095Id());
		else if (dbName.equals(ContentName.onHist))
			jcicB095T = jcicB095ReposHist.findByJcicB095Id(jcicB095.getJcicB095Id());
		else
			jcicB095T = jcicB095Repos.findByJcicB095Id(jcicB095.getJcicB095Id());
		return jcicB095T.isPresent() ? jcicB095T.get() : null;
	}

	@Override
	public JcicB095 insert(JcicB095 jcicB095, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicB095.getJcicB095Id());
		if (this.findById(jcicB095.getJcicB095Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicB095.setCreateEmpNo(empNot);

		if (jcicB095.getLastUpdateEmpNo() == null || jcicB095.getLastUpdateEmpNo().isEmpty())
			jcicB095.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicB095ReposDay.saveAndFlush(jcicB095);
		else if (dbName.equals(ContentName.onMon))
			return jcicB095ReposMon.saveAndFlush(jcicB095);
		else if (dbName.equals(ContentName.onHist))
			return jcicB095ReposHist.saveAndFlush(jcicB095);
		else
			return jcicB095Repos.saveAndFlush(jcicB095);
	}

	@Override
	public JcicB095 update(JcicB095 jcicB095, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicB095.getJcicB095Id());
		if (!empNot.isEmpty())
			jcicB095.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicB095ReposDay.saveAndFlush(jcicB095);
		else if (dbName.equals(ContentName.onMon))
			return jcicB095ReposMon.saveAndFlush(jcicB095);
		else if (dbName.equals(ContentName.onHist))
			return jcicB095ReposHist.saveAndFlush(jcicB095);
		else
			return jcicB095Repos.saveAndFlush(jcicB095);
	}

	@Override
	public JcicB095 update2(JcicB095 jcicB095, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicB095.getJcicB095Id());
		if (!empNot.isEmpty())
			jcicB095.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicB095ReposDay.saveAndFlush(jcicB095);
		else if (dbName.equals(ContentName.onMon))
			jcicB095ReposMon.saveAndFlush(jcicB095);
		else if (dbName.equals(ContentName.onHist))
			jcicB095ReposHist.saveAndFlush(jcicB095);
		else
			jcicB095Repos.saveAndFlush(jcicB095);
		return this.findById(jcicB095.getJcicB095Id());
	}

	@Override
	public void delete(JcicB095 jcicB095, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicB095.getJcicB095Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicB095ReposDay.delete(jcicB095);
			jcicB095ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB095ReposMon.delete(jcicB095);
			jcicB095ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB095ReposHist.delete(jcicB095);
			jcicB095ReposHist.flush();
		} else {
			jcicB095Repos.delete(jcicB095);
			jcicB095Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicB095> jcicB095, TitaVo... titaVo) throws DBException {
		if (jcicB095 == null || jcicB095.size() == 0)
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
		for (JcicB095 t : jcicB095) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicB095 = jcicB095ReposDay.saveAll(jcicB095);
			jcicB095ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB095 = jcicB095ReposMon.saveAll(jcicB095);
			jcicB095ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB095 = jcicB095ReposHist.saveAll(jcicB095);
			jcicB095ReposHist.flush();
		} else {
			jcicB095 = jcicB095Repos.saveAll(jcicB095);
			jcicB095Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicB095> jcicB095, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicB095 == null || jcicB095.size() == 0)
			throw new DBException(6);

		for (JcicB095 t : jcicB095)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicB095 = jcicB095ReposDay.saveAll(jcicB095);
			jcicB095ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB095 = jcicB095ReposMon.saveAll(jcicB095);
			jcicB095ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB095 = jcicB095ReposHist.saveAll(jcicB095);
			jcicB095ReposHist.flush();
		} else {
			jcicB095 = jcicB095Repos.saveAll(jcicB095);
			jcicB095Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicB095> jcicB095, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicB095 == null || jcicB095.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicB095ReposDay.deleteAll(jcicB095);
			jcicB095ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB095ReposMon.deleteAll(jcicB095);
			jcicB095ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB095ReposHist.deleteAll(jcicB095);
			jcicB095ReposHist.flush();
		} else {
			jcicB095Repos.deleteAll(jcicB095);
			jcicB095Repos.flush();
		}
	}

	@Override
	public void Usp_L8_JcicB095_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			jcicB095ReposDay.uspL8Jcicb095Upd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onMon))
			jcicB095ReposMon.uspL8Jcicb095Upd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onHist))
			jcicB095ReposHist.uspL8Jcicb095Upd(TBSDYF, EmpNo);
		else
			jcicB095Repos.uspL8Jcicb095Upd(TBSDYF, EmpNo);
	}

}
