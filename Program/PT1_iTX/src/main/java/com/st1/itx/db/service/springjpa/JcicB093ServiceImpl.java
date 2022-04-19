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
import com.st1.itx.db.domain.JcicB093;
import com.st1.itx.db.domain.JcicB093Id;
import com.st1.itx.db.repository.online.JcicB093Repository;
import com.st1.itx.db.repository.day.JcicB093RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB093RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB093RepositoryHist;
import com.st1.itx.db.service.JcicB093Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB093Service")
@Repository
public class JcicB093ServiceImpl extends ASpringJpaParm implements JcicB093Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicB093Repository jcicB093Repos;

	@Autowired
	private JcicB093RepositoryDay jcicB093ReposDay;

	@Autowired
	private JcicB093RepositoryMon jcicB093ReposMon;

	@Autowired
	private JcicB093RepositoryHist jcicB093ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicB093Repos);
		org.junit.Assert.assertNotNull(jcicB093ReposDay);
		org.junit.Assert.assertNotNull(jcicB093ReposMon);
		org.junit.Assert.assertNotNull(jcicB093ReposHist);
	}

	@Override
	public JcicB093 findById(JcicB093Id jcicB093Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicB093Id);
		Optional<JcicB093> jcicB093 = null;
		if (dbName.equals(ContentName.onDay))
			jcicB093 = jcicB093ReposDay.findById(jcicB093Id);
		else if (dbName.equals(ContentName.onMon))
			jcicB093 = jcicB093ReposMon.findById(jcicB093Id);
		else if (dbName.equals(ContentName.onHist))
			jcicB093 = jcicB093ReposHist.findById(jcicB093Id);
		else
			jcicB093 = jcicB093Repos.findById(jcicB093Id);
		JcicB093 obj = jcicB093.isPresent() ? jcicB093.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicB093> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicB093> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "ClActNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "ClActNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicB093ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicB093ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicB093ReposHist.findAll(pageable);
		else
			slice = jcicB093Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicB093 holdById(JcicB093Id jcicB093Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicB093Id);
		Optional<JcicB093> jcicB093 = null;
		if (dbName.equals(ContentName.onDay))
			jcicB093 = jcicB093ReposDay.findByJcicB093Id(jcicB093Id);
		else if (dbName.equals(ContentName.onMon))
			jcicB093 = jcicB093ReposMon.findByJcicB093Id(jcicB093Id);
		else if (dbName.equals(ContentName.onHist))
			jcicB093 = jcicB093ReposHist.findByJcicB093Id(jcicB093Id);
		else
			jcicB093 = jcicB093Repos.findByJcicB093Id(jcicB093Id);
		return jcicB093.isPresent() ? jcicB093.get() : null;
	}

	@Override
	public JcicB093 holdById(JcicB093 jcicB093, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicB093.getJcicB093Id());
		Optional<JcicB093> jcicB093T = null;
		if (dbName.equals(ContentName.onDay))
			jcicB093T = jcicB093ReposDay.findByJcicB093Id(jcicB093.getJcicB093Id());
		else if (dbName.equals(ContentName.onMon))
			jcicB093T = jcicB093ReposMon.findByJcicB093Id(jcicB093.getJcicB093Id());
		else if (dbName.equals(ContentName.onHist))
			jcicB093T = jcicB093ReposHist.findByJcicB093Id(jcicB093.getJcicB093Id());
		else
			jcicB093T = jcicB093Repos.findByJcicB093Id(jcicB093.getJcicB093Id());
		return jcicB093T.isPresent() ? jcicB093T.get() : null;
	}

	@Override
	public JcicB093 insert(JcicB093 jcicB093, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicB093.getJcicB093Id());
		if (this.findById(jcicB093.getJcicB093Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicB093.setCreateEmpNo(empNot);

		if (jcicB093.getLastUpdateEmpNo() == null || jcicB093.getLastUpdateEmpNo().isEmpty())
			jcicB093.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicB093ReposDay.saveAndFlush(jcicB093);
		else if (dbName.equals(ContentName.onMon))
			return jcicB093ReposMon.saveAndFlush(jcicB093);
		else if (dbName.equals(ContentName.onHist))
			return jcicB093ReposHist.saveAndFlush(jcicB093);
		else
			return jcicB093Repos.saveAndFlush(jcicB093);
	}

	@Override
	public JcicB093 update(JcicB093 jcicB093, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicB093.getJcicB093Id());
		if (!empNot.isEmpty())
			jcicB093.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicB093ReposDay.saveAndFlush(jcicB093);
		else if (dbName.equals(ContentName.onMon))
			return jcicB093ReposMon.saveAndFlush(jcicB093);
		else if (dbName.equals(ContentName.onHist))
			return jcicB093ReposHist.saveAndFlush(jcicB093);
		else
			return jcicB093Repos.saveAndFlush(jcicB093);
	}

	@Override
	public JcicB093 update2(JcicB093 jcicB093, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicB093.getJcicB093Id());
		if (!empNot.isEmpty())
			jcicB093.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicB093ReposDay.saveAndFlush(jcicB093);
		else if (dbName.equals(ContentName.onMon))
			jcicB093ReposMon.saveAndFlush(jcicB093);
		else if (dbName.equals(ContentName.onHist))
			jcicB093ReposHist.saveAndFlush(jcicB093);
		else
			jcicB093Repos.saveAndFlush(jcicB093);
		return this.findById(jcicB093.getJcicB093Id());
	}

	@Override
	public void delete(JcicB093 jcicB093, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicB093.getJcicB093Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicB093ReposDay.delete(jcicB093);
			jcicB093ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB093ReposMon.delete(jcicB093);
			jcicB093ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB093ReposHist.delete(jcicB093);
			jcicB093ReposHist.flush();
		} else {
			jcicB093Repos.delete(jcicB093);
			jcicB093Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicB093> jcicB093, TitaVo... titaVo) throws DBException {
		if (jcicB093 == null || jcicB093.size() == 0)
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
		for (JcicB093 t : jcicB093) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicB093 = jcicB093ReposDay.saveAll(jcicB093);
			jcicB093ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB093 = jcicB093ReposMon.saveAll(jcicB093);
			jcicB093ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB093 = jcicB093ReposHist.saveAll(jcicB093);
			jcicB093ReposHist.flush();
		} else {
			jcicB093 = jcicB093Repos.saveAll(jcicB093);
			jcicB093Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicB093> jcicB093, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicB093 == null || jcicB093.size() == 0)
			throw new DBException(6);

		for (JcicB093 t : jcicB093)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicB093 = jcicB093ReposDay.saveAll(jcicB093);
			jcicB093ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB093 = jcicB093ReposMon.saveAll(jcicB093);
			jcicB093ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB093 = jcicB093ReposHist.saveAll(jcicB093);
			jcicB093ReposHist.flush();
		} else {
			jcicB093 = jcicB093Repos.saveAll(jcicB093);
			jcicB093Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicB093> jcicB093, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicB093 == null || jcicB093.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicB093ReposDay.deleteAll(jcicB093);
			jcicB093ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB093ReposMon.deleteAll(jcicB093);
			jcicB093ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB093ReposHist.deleteAll(jcicB093);
			jcicB093ReposHist.flush();
		} else {
			jcicB093Repos.deleteAll(jcicB093);
			jcicB093Repos.flush();
		}
	}

	@Override
	public void Usp_L8_JcicB093_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			jcicB093ReposDay.uspL8Jcicb093Upd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onMon))
			jcicB093ReposMon.uspL8Jcicb093Upd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onHist))
			jcicB093ReposHist.uspL8Jcicb093Upd(TBSDYF, EmpNo);
		else
			jcicB093Repos.uspL8Jcicb093Upd(TBSDYF, EmpNo);
	}

}
