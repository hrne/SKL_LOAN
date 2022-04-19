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
import com.st1.itx.db.domain.JcicZ442Log;
import com.st1.itx.db.domain.JcicZ442LogId;
import com.st1.itx.db.repository.online.JcicZ442LogRepository;
import com.st1.itx.db.repository.day.JcicZ442LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ442LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ442LogRepositoryHist;
import com.st1.itx.db.service.JcicZ442LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ442LogService")
@Repository
public class JcicZ442LogServiceImpl extends ASpringJpaParm implements JcicZ442LogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ442LogRepository jcicZ442LogRepos;

	@Autowired
	private JcicZ442LogRepositoryDay jcicZ442LogReposDay;

	@Autowired
	private JcicZ442LogRepositoryMon jcicZ442LogReposMon;

	@Autowired
	private JcicZ442LogRepositoryHist jcicZ442LogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ442LogRepos);
		org.junit.Assert.assertNotNull(jcicZ442LogReposDay);
		org.junit.Assert.assertNotNull(jcicZ442LogReposMon);
		org.junit.Assert.assertNotNull(jcicZ442LogReposHist);
	}

	@Override
	public JcicZ442Log findById(JcicZ442LogId jcicZ442LogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ442LogId);
		Optional<JcicZ442Log> jcicZ442Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ442Log = jcicZ442LogReposDay.findById(jcicZ442LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ442Log = jcicZ442LogReposMon.findById(jcicZ442LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ442Log = jcicZ442LogReposHist.findById(jcicZ442LogId);
		else
			jcicZ442Log = jcicZ442LogRepos.findById(jcicZ442LogId);
		JcicZ442Log obj = jcicZ442Log.isPresent() ? jcicZ442Log.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ442Log> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ442Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ442LogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ442LogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ442LogReposHist.findAll(pageable);
		else
			slice = jcicZ442LogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ442Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ442Log> jcicZ442LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ442LogT = jcicZ442LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ442LogT = jcicZ442LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ442LogT = jcicZ442LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else
			jcicZ442LogT = jcicZ442LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

		return jcicZ442LogT.isPresent() ? jcicZ442LogT.get() : null;
	}

	@Override
	public Slice<JcicZ442Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ442Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ442LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ442LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ442LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else
			slice = jcicZ442LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ442Log holdById(JcicZ442LogId jcicZ442LogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ442LogId);
		Optional<JcicZ442Log> jcicZ442Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ442Log = jcicZ442LogReposDay.findByJcicZ442LogId(jcicZ442LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ442Log = jcicZ442LogReposMon.findByJcicZ442LogId(jcicZ442LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ442Log = jcicZ442LogReposHist.findByJcicZ442LogId(jcicZ442LogId);
		else
			jcicZ442Log = jcicZ442LogRepos.findByJcicZ442LogId(jcicZ442LogId);
		return jcicZ442Log.isPresent() ? jcicZ442Log.get() : null;
	}

	@Override
	public JcicZ442Log holdById(JcicZ442Log jcicZ442Log, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ442Log.getJcicZ442LogId());
		Optional<JcicZ442Log> jcicZ442LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ442LogT = jcicZ442LogReposDay.findByJcicZ442LogId(jcicZ442Log.getJcicZ442LogId());
		else if (dbName.equals(ContentName.onMon))
			jcicZ442LogT = jcicZ442LogReposMon.findByJcicZ442LogId(jcicZ442Log.getJcicZ442LogId());
		else if (dbName.equals(ContentName.onHist))
			jcicZ442LogT = jcicZ442LogReposHist.findByJcicZ442LogId(jcicZ442Log.getJcicZ442LogId());
		else
			jcicZ442LogT = jcicZ442LogRepos.findByJcicZ442LogId(jcicZ442Log.getJcicZ442LogId());
		return jcicZ442LogT.isPresent() ? jcicZ442LogT.get() : null;
	}

	@Override
	public JcicZ442Log insert(JcicZ442Log jcicZ442Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ442Log.getJcicZ442LogId());
		if (this.findById(jcicZ442Log.getJcicZ442LogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ442Log.setCreateEmpNo(empNot);

		if (jcicZ442Log.getLastUpdateEmpNo() == null || jcicZ442Log.getLastUpdateEmpNo().isEmpty())
			jcicZ442Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ442LogReposDay.saveAndFlush(jcicZ442Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ442LogReposMon.saveAndFlush(jcicZ442Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ442LogReposHist.saveAndFlush(jcicZ442Log);
		else
			return jcicZ442LogRepos.saveAndFlush(jcicZ442Log);
	}

	@Override
	public JcicZ442Log update(JcicZ442Log jcicZ442Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ442Log.getJcicZ442LogId());
		if (!empNot.isEmpty())
			jcicZ442Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ442LogReposDay.saveAndFlush(jcicZ442Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ442LogReposMon.saveAndFlush(jcicZ442Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ442LogReposHist.saveAndFlush(jcicZ442Log);
		else
			return jcicZ442LogRepos.saveAndFlush(jcicZ442Log);
	}

	@Override
	public JcicZ442Log update2(JcicZ442Log jcicZ442Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ442Log.getJcicZ442LogId());
		if (!empNot.isEmpty())
			jcicZ442Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ442LogReposDay.saveAndFlush(jcicZ442Log);
		else if (dbName.equals(ContentName.onMon))
			jcicZ442LogReposMon.saveAndFlush(jcicZ442Log);
		else if (dbName.equals(ContentName.onHist))
			jcicZ442LogReposHist.saveAndFlush(jcicZ442Log);
		else
			jcicZ442LogRepos.saveAndFlush(jcicZ442Log);
		return this.findById(jcicZ442Log.getJcicZ442LogId());
	}

	@Override
	public void delete(JcicZ442Log jcicZ442Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ442Log.getJcicZ442LogId());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ442LogReposDay.delete(jcicZ442Log);
			jcicZ442LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ442LogReposMon.delete(jcicZ442Log);
			jcicZ442LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ442LogReposHist.delete(jcicZ442Log);
			jcicZ442LogReposHist.flush();
		} else {
			jcicZ442LogRepos.delete(jcicZ442Log);
			jcicZ442LogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ442Log> jcicZ442Log, TitaVo... titaVo) throws DBException {
		if (jcicZ442Log == null || jcicZ442Log.size() == 0)
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
		for (JcicZ442Log t : jcicZ442Log) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ442Log = jcicZ442LogReposDay.saveAll(jcicZ442Log);
			jcicZ442LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ442Log = jcicZ442LogReposMon.saveAll(jcicZ442Log);
			jcicZ442LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ442Log = jcicZ442LogReposHist.saveAll(jcicZ442Log);
			jcicZ442LogReposHist.flush();
		} else {
			jcicZ442Log = jcicZ442LogRepos.saveAll(jcicZ442Log);
			jcicZ442LogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ442Log> jcicZ442Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ442Log == null || jcicZ442Log.size() == 0)
			throw new DBException(6);

		for (JcicZ442Log t : jcicZ442Log)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ442Log = jcicZ442LogReposDay.saveAll(jcicZ442Log);
			jcicZ442LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ442Log = jcicZ442LogReposMon.saveAll(jcicZ442Log);
			jcicZ442LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ442Log = jcicZ442LogReposHist.saveAll(jcicZ442Log);
			jcicZ442LogReposHist.flush();
		} else {
			jcicZ442Log = jcicZ442LogRepos.saveAll(jcicZ442Log);
			jcicZ442LogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ442Log> jcicZ442Log, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ442Log == null || jcicZ442Log.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ442LogReposDay.deleteAll(jcicZ442Log);
			jcicZ442LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ442LogReposMon.deleteAll(jcicZ442Log);
			jcicZ442LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ442LogReposHist.deleteAll(jcicZ442Log);
			jcicZ442LogReposHist.flush();
		} else {
			jcicZ442LogRepos.deleteAll(jcicZ442Log);
			jcicZ442LogRepos.flush();
		}
	}

}
