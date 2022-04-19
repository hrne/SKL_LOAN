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
import com.st1.itx.db.domain.JcicZ575Log;
import com.st1.itx.db.domain.JcicZ575LogId;
import com.st1.itx.db.repository.online.JcicZ575LogRepository;
import com.st1.itx.db.repository.day.JcicZ575LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ575LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ575LogRepositoryHist;
import com.st1.itx.db.service.JcicZ575LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ575LogService")
@Repository
public class JcicZ575LogServiceImpl extends ASpringJpaParm implements JcicZ575LogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ575LogRepository jcicZ575LogRepos;

	@Autowired
	private JcicZ575LogRepositoryDay jcicZ575LogReposDay;

	@Autowired
	private JcicZ575LogRepositoryMon jcicZ575LogReposMon;

	@Autowired
	private JcicZ575LogRepositoryHist jcicZ575LogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ575LogRepos);
		org.junit.Assert.assertNotNull(jcicZ575LogReposDay);
		org.junit.Assert.assertNotNull(jcicZ575LogReposMon);
		org.junit.Assert.assertNotNull(jcicZ575LogReposHist);
	}

	@Override
	public JcicZ575Log findById(JcicZ575LogId jcicZ575LogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ575LogId);
		Optional<JcicZ575Log> jcicZ575Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ575Log = jcicZ575LogReposDay.findById(jcicZ575LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ575Log = jcicZ575LogReposMon.findById(jcicZ575LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ575Log = jcicZ575LogReposHist.findById(jcicZ575LogId);
		else
			jcicZ575Log = jcicZ575LogRepos.findById(jcicZ575LogId);
		JcicZ575Log obj = jcicZ575Log.isPresent() ? jcicZ575Log.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ575Log> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ575Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ575LogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ575LogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ575LogReposHist.findAll(pageable);
		else
			slice = jcicZ575LogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ575Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ575Log> jcicZ575LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ575LogT = jcicZ575LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ575LogT = jcicZ575LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ575LogT = jcicZ575LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else
			jcicZ575LogT = jcicZ575LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

		return jcicZ575LogT.isPresent() ? jcicZ575LogT.get() : null;
	}

	@Override
	public Slice<JcicZ575Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ575Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ575LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ575LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ575LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else
			slice = jcicZ575LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ575Log holdById(JcicZ575LogId jcicZ575LogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ575LogId);
		Optional<JcicZ575Log> jcicZ575Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ575Log = jcicZ575LogReposDay.findByJcicZ575LogId(jcicZ575LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ575Log = jcicZ575LogReposMon.findByJcicZ575LogId(jcicZ575LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ575Log = jcicZ575LogReposHist.findByJcicZ575LogId(jcicZ575LogId);
		else
			jcicZ575Log = jcicZ575LogRepos.findByJcicZ575LogId(jcicZ575LogId);
		return jcicZ575Log.isPresent() ? jcicZ575Log.get() : null;
	}

	@Override
	public JcicZ575Log holdById(JcicZ575Log jcicZ575Log, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ575Log.getJcicZ575LogId());
		Optional<JcicZ575Log> jcicZ575LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ575LogT = jcicZ575LogReposDay.findByJcicZ575LogId(jcicZ575Log.getJcicZ575LogId());
		else if (dbName.equals(ContentName.onMon))
			jcicZ575LogT = jcicZ575LogReposMon.findByJcicZ575LogId(jcicZ575Log.getJcicZ575LogId());
		else if (dbName.equals(ContentName.onHist))
			jcicZ575LogT = jcicZ575LogReposHist.findByJcicZ575LogId(jcicZ575Log.getJcicZ575LogId());
		else
			jcicZ575LogT = jcicZ575LogRepos.findByJcicZ575LogId(jcicZ575Log.getJcicZ575LogId());
		return jcicZ575LogT.isPresent() ? jcicZ575LogT.get() : null;
	}

	@Override
	public JcicZ575Log insert(JcicZ575Log jcicZ575Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ575Log.getJcicZ575LogId());
		if (this.findById(jcicZ575Log.getJcicZ575LogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ575Log.setCreateEmpNo(empNot);

		if (jcicZ575Log.getLastUpdateEmpNo() == null || jcicZ575Log.getLastUpdateEmpNo().isEmpty())
			jcicZ575Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ575LogReposDay.saveAndFlush(jcicZ575Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ575LogReposMon.saveAndFlush(jcicZ575Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ575LogReposHist.saveAndFlush(jcicZ575Log);
		else
			return jcicZ575LogRepos.saveAndFlush(jcicZ575Log);
	}

	@Override
	public JcicZ575Log update(JcicZ575Log jcicZ575Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ575Log.getJcicZ575LogId());
		if (!empNot.isEmpty())
			jcicZ575Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ575LogReposDay.saveAndFlush(jcicZ575Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ575LogReposMon.saveAndFlush(jcicZ575Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ575LogReposHist.saveAndFlush(jcicZ575Log);
		else
			return jcicZ575LogRepos.saveAndFlush(jcicZ575Log);
	}

	@Override
	public JcicZ575Log update2(JcicZ575Log jcicZ575Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ575Log.getJcicZ575LogId());
		if (!empNot.isEmpty())
			jcicZ575Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ575LogReposDay.saveAndFlush(jcicZ575Log);
		else if (dbName.equals(ContentName.onMon))
			jcicZ575LogReposMon.saveAndFlush(jcicZ575Log);
		else if (dbName.equals(ContentName.onHist))
			jcicZ575LogReposHist.saveAndFlush(jcicZ575Log);
		else
			jcicZ575LogRepos.saveAndFlush(jcicZ575Log);
		return this.findById(jcicZ575Log.getJcicZ575LogId());
	}

	@Override
	public void delete(JcicZ575Log jcicZ575Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ575Log.getJcicZ575LogId());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ575LogReposDay.delete(jcicZ575Log);
			jcicZ575LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ575LogReposMon.delete(jcicZ575Log);
			jcicZ575LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ575LogReposHist.delete(jcicZ575Log);
			jcicZ575LogReposHist.flush();
		} else {
			jcicZ575LogRepos.delete(jcicZ575Log);
			jcicZ575LogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ575Log> jcicZ575Log, TitaVo... titaVo) throws DBException {
		if (jcicZ575Log == null || jcicZ575Log.size() == 0)
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
		for (JcicZ575Log t : jcicZ575Log) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ575Log = jcicZ575LogReposDay.saveAll(jcicZ575Log);
			jcicZ575LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ575Log = jcicZ575LogReposMon.saveAll(jcicZ575Log);
			jcicZ575LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ575Log = jcicZ575LogReposHist.saveAll(jcicZ575Log);
			jcicZ575LogReposHist.flush();
		} else {
			jcicZ575Log = jcicZ575LogRepos.saveAll(jcicZ575Log);
			jcicZ575LogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ575Log> jcicZ575Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ575Log == null || jcicZ575Log.size() == 0)
			throw new DBException(6);

		for (JcicZ575Log t : jcicZ575Log)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ575Log = jcicZ575LogReposDay.saveAll(jcicZ575Log);
			jcicZ575LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ575Log = jcicZ575LogReposMon.saveAll(jcicZ575Log);
			jcicZ575LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ575Log = jcicZ575LogReposHist.saveAll(jcicZ575Log);
			jcicZ575LogReposHist.flush();
		} else {
			jcicZ575Log = jcicZ575LogRepos.saveAll(jcicZ575Log);
			jcicZ575LogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ575Log> jcicZ575Log, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ575Log == null || jcicZ575Log.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ575LogReposDay.deleteAll(jcicZ575Log);
			jcicZ575LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ575LogReposMon.deleteAll(jcicZ575Log);
			jcicZ575LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ575LogReposHist.deleteAll(jcicZ575Log);
			jcicZ575LogReposHist.flush();
		} else {
			jcicZ575LogRepos.deleteAll(jcicZ575Log);
			jcicZ575LogRepos.flush();
		}
	}

}
