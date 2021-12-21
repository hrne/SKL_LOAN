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
import com.st1.itx.db.domain.JcicB080;
import com.st1.itx.db.domain.JcicB080Id;
import com.st1.itx.db.repository.online.JcicB080Repository;
import com.st1.itx.db.repository.day.JcicB080RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB080RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB080RepositoryHist;
import com.st1.itx.db.service.JcicB080Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB080Service")
@Repository
public class JcicB080ServiceImpl extends ASpringJpaParm implements JcicB080Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicB080Repository jcicB080Repos;

	@Autowired
	private JcicB080RepositoryDay jcicB080ReposDay;

	@Autowired
	private JcicB080RepositoryMon jcicB080ReposMon;

	@Autowired
	private JcicB080RepositoryHist jcicB080ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicB080Repos);
		org.junit.Assert.assertNotNull(jcicB080ReposDay);
		org.junit.Assert.assertNotNull(jcicB080ReposMon);
		org.junit.Assert.assertNotNull(jcicB080ReposHist);
	}

	@Override
	public JcicB080 findById(JcicB080Id jcicB080Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicB080Id);
		Optional<JcicB080> jcicB080 = null;
		if (dbName.equals(ContentName.onDay))
			jcicB080 = jcicB080ReposDay.findById(jcicB080Id);
		else if (dbName.equals(ContentName.onMon))
			jcicB080 = jcicB080ReposMon.findById(jcicB080Id);
		else if (dbName.equals(ContentName.onHist))
			jcicB080 = jcicB080ReposHist.findById(jcicB080Id);
		else
			jcicB080 = jcicB080Repos.findById(jcicB080Id);
		JcicB080 obj = jcicB080.isPresent() ? jcicB080.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicB080> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicB080> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "BankItem", "FacmNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "BankItem", "FacmNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicB080ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicB080ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicB080ReposHist.findAll(pageable);
		else
			slice = jcicB080Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicB080 holdById(JcicB080Id jcicB080Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicB080Id);
		Optional<JcicB080> jcicB080 = null;
		if (dbName.equals(ContentName.onDay))
			jcicB080 = jcicB080ReposDay.findByJcicB080Id(jcicB080Id);
		else if (dbName.equals(ContentName.onMon))
			jcicB080 = jcicB080ReposMon.findByJcicB080Id(jcicB080Id);
		else if (dbName.equals(ContentName.onHist))
			jcicB080 = jcicB080ReposHist.findByJcicB080Id(jcicB080Id);
		else
			jcicB080 = jcicB080Repos.findByJcicB080Id(jcicB080Id);
		return jcicB080.isPresent() ? jcicB080.get() : null;
	}

	@Override
	public JcicB080 holdById(JcicB080 jcicB080, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicB080.getJcicB080Id());
		Optional<JcicB080> jcicB080T = null;
		if (dbName.equals(ContentName.onDay))
			jcicB080T = jcicB080ReposDay.findByJcicB080Id(jcicB080.getJcicB080Id());
		else if (dbName.equals(ContentName.onMon))
			jcicB080T = jcicB080ReposMon.findByJcicB080Id(jcicB080.getJcicB080Id());
		else if (dbName.equals(ContentName.onHist))
			jcicB080T = jcicB080ReposHist.findByJcicB080Id(jcicB080.getJcicB080Id());
		else
			jcicB080T = jcicB080Repos.findByJcicB080Id(jcicB080.getJcicB080Id());
		return jcicB080T.isPresent() ? jcicB080T.get() : null;
	}

	@Override
	public JcicB080 insert(JcicB080 jcicB080, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicB080.getJcicB080Id());
		if (this.findById(jcicB080.getJcicB080Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicB080.setCreateEmpNo(empNot);

		if (jcicB080.getLastUpdateEmpNo() == null || jcicB080.getLastUpdateEmpNo().isEmpty())
			jcicB080.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicB080ReposDay.saveAndFlush(jcicB080);
		else if (dbName.equals(ContentName.onMon))
			return jcicB080ReposMon.saveAndFlush(jcicB080);
		else if (dbName.equals(ContentName.onHist))
			return jcicB080ReposHist.saveAndFlush(jcicB080);
		else
			return jcicB080Repos.saveAndFlush(jcicB080);
	}

	@Override
	public JcicB080 update(JcicB080 jcicB080, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicB080.getJcicB080Id());
		if (!empNot.isEmpty())
			jcicB080.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicB080ReposDay.saveAndFlush(jcicB080);
		else if (dbName.equals(ContentName.onMon))
			return jcicB080ReposMon.saveAndFlush(jcicB080);
		else if (dbName.equals(ContentName.onHist))
			return jcicB080ReposHist.saveAndFlush(jcicB080);
		else
			return jcicB080Repos.saveAndFlush(jcicB080);
	}

	@Override
	public JcicB080 update2(JcicB080 jcicB080, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicB080.getJcicB080Id());
		if (!empNot.isEmpty())
			jcicB080.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicB080ReposDay.saveAndFlush(jcicB080);
		else if (dbName.equals(ContentName.onMon))
			jcicB080ReposMon.saveAndFlush(jcicB080);
		else if (dbName.equals(ContentName.onHist))
			jcicB080ReposHist.saveAndFlush(jcicB080);
		else
			jcicB080Repos.saveAndFlush(jcicB080);
		return this.findById(jcicB080.getJcicB080Id());
	}

	@Override
	public void delete(JcicB080 jcicB080, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicB080.getJcicB080Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicB080ReposDay.delete(jcicB080);
			jcicB080ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB080ReposMon.delete(jcicB080);
			jcicB080ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB080ReposHist.delete(jcicB080);
			jcicB080ReposHist.flush();
		} else {
			jcicB080Repos.delete(jcicB080);
			jcicB080Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicB080> jcicB080, TitaVo... titaVo) throws DBException {
		if (jcicB080 == null || jcicB080.size() == 0)
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
		for (JcicB080 t : jcicB080) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicB080 = jcicB080ReposDay.saveAll(jcicB080);
			jcicB080ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB080 = jcicB080ReposMon.saveAll(jcicB080);
			jcicB080ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB080 = jcicB080ReposHist.saveAll(jcicB080);
			jcicB080ReposHist.flush();
		} else {
			jcicB080 = jcicB080Repos.saveAll(jcicB080);
			jcicB080Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicB080> jcicB080, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicB080 == null || jcicB080.size() == 0)
			throw new DBException(6);

		for (JcicB080 t : jcicB080)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicB080 = jcicB080ReposDay.saveAll(jcicB080);
			jcicB080ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB080 = jcicB080ReposMon.saveAll(jcicB080);
			jcicB080ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB080 = jcicB080ReposHist.saveAll(jcicB080);
			jcicB080ReposHist.flush();
		} else {
			jcicB080 = jcicB080Repos.saveAll(jcicB080);
			jcicB080Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicB080> jcicB080, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicB080 == null || jcicB080.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicB080ReposDay.deleteAll(jcicB080);
			jcicB080ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB080ReposMon.deleteAll(jcicB080);
			jcicB080ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB080ReposHist.deleteAll(jcicB080);
			jcicB080ReposHist.flush();
		} else {
			jcicB080Repos.deleteAll(jcicB080);
			jcicB080Repos.flush();
		}
	}

	@Override
	public void Usp_L8_JcicB080_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			jcicB080ReposDay.uspL8Jcicb080Upd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onMon))
			jcicB080ReposMon.uspL8Jcicb080Upd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onHist))
			jcicB080ReposHist.uspL8Jcicb080Upd(TBSDYF, EmpNo);
		else
			jcicB080Repos.uspL8Jcicb080Upd(TBSDYF, EmpNo);
	}

}
