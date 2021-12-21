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
import com.st1.itx.db.domain.JcicZ044Log;
import com.st1.itx.db.domain.JcicZ044LogId;
import com.st1.itx.db.repository.online.JcicZ044LogRepository;
import com.st1.itx.db.repository.day.JcicZ044LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ044LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ044LogRepositoryHist;
import com.st1.itx.db.service.JcicZ044LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ044LogService")
@Repository
public class JcicZ044LogServiceImpl extends ASpringJpaParm implements JcicZ044LogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ044LogRepository jcicZ044LogRepos;

	@Autowired
	private JcicZ044LogRepositoryDay jcicZ044LogReposDay;

	@Autowired
	private JcicZ044LogRepositoryMon jcicZ044LogReposMon;

	@Autowired
	private JcicZ044LogRepositoryHist jcicZ044LogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ044LogRepos);
		org.junit.Assert.assertNotNull(jcicZ044LogReposDay);
		org.junit.Assert.assertNotNull(jcicZ044LogReposMon);
		org.junit.Assert.assertNotNull(jcicZ044LogReposHist);
	}

	@Override
	public JcicZ044Log findById(JcicZ044LogId jcicZ044LogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ044LogId);
		Optional<JcicZ044Log> jcicZ044Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ044Log = jcicZ044LogReposDay.findById(jcicZ044LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ044Log = jcicZ044LogReposMon.findById(jcicZ044LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ044Log = jcicZ044LogReposHist.findById(jcicZ044LogId);
		else
			jcicZ044Log = jcicZ044LogRepos.findById(jcicZ044LogId);
		JcicZ044Log obj = jcicZ044Log.isPresent() ? jcicZ044Log.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ044Log> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ044Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ044LogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ044LogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ044LogReposHist.findAll(pageable);
		else
			slice = jcicZ044LogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ044Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ044Log> jcicZ044LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ044LogT = jcicZ044LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ044LogT = jcicZ044LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ044LogT = jcicZ044LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else
			jcicZ044LogT = jcicZ044LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

		return jcicZ044LogT.isPresent() ? jcicZ044LogT.get() : null;
	}

	@Override
	public Slice<JcicZ044Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ044Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ044LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ044LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ044LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else
			slice = jcicZ044LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ044Log holdById(JcicZ044LogId jcicZ044LogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ044LogId);
		Optional<JcicZ044Log> jcicZ044Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ044Log = jcicZ044LogReposDay.findByJcicZ044LogId(jcicZ044LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ044Log = jcicZ044LogReposMon.findByJcicZ044LogId(jcicZ044LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ044Log = jcicZ044LogReposHist.findByJcicZ044LogId(jcicZ044LogId);
		else
			jcicZ044Log = jcicZ044LogRepos.findByJcicZ044LogId(jcicZ044LogId);
		return jcicZ044Log.isPresent() ? jcicZ044Log.get() : null;
	}

	@Override
	public JcicZ044Log holdById(JcicZ044Log jcicZ044Log, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ044Log.getJcicZ044LogId());
		Optional<JcicZ044Log> jcicZ044LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ044LogT = jcicZ044LogReposDay.findByJcicZ044LogId(jcicZ044Log.getJcicZ044LogId());
		else if (dbName.equals(ContentName.onMon))
			jcicZ044LogT = jcicZ044LogReposMon.findByJcicZ044LogId(jcicZ044Log.getJcicZ044LogId());
		else if (dbName.equals(ContentName.onHist))
			jcicZ044LogT = jcicZ044LogReposHist.findByJcicZ044LogId(jcicZ044Log.getJcicZ044LogId());
		else
			jcicZ044LogT = jcicZ044LogRepos.findByJcicZ044LogId(jcicZ044Log.getJcicZ044LogId());
		return jcicZ044LogT.isPresent() ? jcicZ044LogT.get() : null;
	}

	@Override
	public JcicZ044Log insert(JcicZ044Log jcicZ044Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ044Log.getJcicZ044LogId());
		if (this.findById(jcicZ044Log.getJcicZ044LogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ044Log.setCreateEmpNo(empNot);

		if (jcicZ044Log.getLastUpdateEmpNo() == null || jcicZ044Log.getLastUpdateEmpNo().isEmpty())
			jcicZ044Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ044LogReposDay.saveAndFlush(jcicZ044Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ044LogReposMon.saveAndFlush(jcicZ044Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ044LogReposHist.saveAndFlush(jcicZ044Log);
		else
			return jcicZ044LogRepos.saveAndFlush(jcicZ044Log);
	}

	@Override
	public JcicZ044Log update(JcicZ044Log jcicZ044Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ044Log.getJcicZ044LogId());
		if (!empNot.isEmpty())
			jcicZ044Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ044LogReposDay.saveAndFlush(jcicZ044Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ044LogReposMon.saveAndFlush(jcicZ044Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ044LogReposHist.saveAndFlush(jcicZ044Log);
		else
			return jcicZ044LogRepos.saveAndFlush(jcicZ044Log);
	}

	@Override
	public JcicZ044Log update2(JcicZ044Log jcicZ044Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ044Log.getJcicZ044LogId());
		if (!empNot.isEmpty())
			jcicZ044Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ044LogReposDay.saveAndFlush(jcicZ044Log);
		else if (dbName.equals(ContentName.onMon))
			jcicZ044LogReposMon.saveAndFlush(jcicZ044Log);
		else if (dbName.equals(ContentName.onHist))
			jcicZ044LogReposHist.saveAndFlush(jcicZ044Log);
		else
			jcicZ044LogRepos.saveAndFlush(jcicZ044Log);
		return this.findById(jcicZ044Log.getJcicZ044LogId());
	}

	@Override
	public void delete(JcicZ044Log jcicZ044Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ044Log.getJcicZ044LogId());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ044LogReposDay.delete(jcicZ044Log);
			jcicZ044LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ044LogReposMon.delete(jcicZ044Log);
			jcicZ044LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ044LogReposHist.delete(jcicZ044Log);
			jcicZ044LogReposHist.flush();
		} else {
			jcicZ044LogRepos.delete(jcicZ044Log);
			jcicZ044LogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ044Log> jcicZ044Log, TitaVo... titaVo) throws DBException {
		if (jcicZ044Log == null || jcicZ044Log.size() == 0)
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
		for (JcicZ044Log t : jcicZ044Log) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ044Log = jcicZ044LogReposDay.saveAll(jcicZ044Log);
			jcicZ044LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ044Log = jcicZ044LogReposMon.saveAll(jcicZ044Log);
			jcicZ044LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ044Log = jcicZ044LogReposHist.saveAll(jcicZ044Log);
			jcicZ044LogReposHist.flush();
		} else {
			jcicZ044Log = jcicZ044LogRepos.saveAll(jcicZ044Log);
			jcicZ044LogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ044Log> jcicZ044Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ044Log == null || jcicZ044Log.size() == 0)
			throw new DBException(6);

		for (JcicZ044Log t : jcicZ044Log)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ044Log = jcicZ044LogReposDay.saveAll(jcicZ044Log);
			jcicZ044LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ044Log = jcicZ044LogReposMon.saveAll(jcicZ044Log);
			jcicZ044LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ044Log = jcicZ044LogReposHist.saveAll(jcicZ044Log);
			jcicZ044LogReposHist.flush();
		} else {
			jcicZ044Log = jcicZ044LogRepos.saveAll(jcicZ044Log);
			jcicZ044LogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ044Log> jcicZ044Log, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ044Log == null || jcicZ044Log.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ044LogReposDay.deleteAll(jcicZ044Log);
			jcicZ044LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ044LogReposMon.deleteAll(jcicZ044Log);
			jcicZ044LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ044LogReposHist.deleteAll(jcicZ044Log);
			jcicZ044LogReposHist.flush();
		} else {
			jcicZ044LogRepos.deleteAll(jcicZ044Log);
			jcicZ044LogRepos.flush();
		}
	}

}
