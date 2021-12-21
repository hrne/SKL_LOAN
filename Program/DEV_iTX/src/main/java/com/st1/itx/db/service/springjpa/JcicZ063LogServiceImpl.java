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
import com.st1.itx.db.domain.JcicZ063Log;
import com.st1.itx.db.domain.JcicZ063LogId;
import com.st1.itx.db.repository.online.JcicZ063LogRepository;
import com.st1.itx.db.repository.day.JcicZ063LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ063LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ063LogRepositoryHist;
import com.st1.itx.db.service.JcicZ063LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ063LogService")
@Repository
public class JcicZ063LogServiceImpl extends ASpringJpaParm implements JcicZ063LogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ063LogRepository jcicZ063LogRepos;

	@Autowired
	private JcicZ063LogRepositoryDay jcicZ063LogReposDay;

	@Autowired
	private JcicZ063LogRepositoryMon jcicZ063LogReposMon;

	@Autowired
	private JcicZ063LogRepositoryHist jcicZ063LogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ063LogRepos);
		org.junit.Assert.assertNotNull(jcicZ063LogReposDay);
		org.junit.Assert.assertNotNull(jcicZ063LogReposMon);
		org.junit.Assert.assertNotNull(jcicZ063LogReposHist);
	}

	@Override
	public JcicZ063Log findById(JcicZ063LogId jcicZ063LogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ063LogId);
		Optional<JcicZ063Log> jcicZ063Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ063Log = jcicZ063LogReposDay.findById(jcicZ063LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ063Log = jcicZ063LogReposMon.findById(jcicZ063LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ063Log = jcicZ063LogReposHist.findById(jcicZ063LogId);
		else
			jcicZ063Log = jcicZ063LogRepos.findById(jcicZ063LogId);
		JcicZ063Log obj = jcicZ063Log.isPresent() ? jcicZ063Log.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ063Log> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ063Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ063LogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ063LogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ063LogReposHist.findAll(pageable);
		else
			slice = jcicZ063LogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ063Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ063Log> jcicZ063LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ063LogT = jcicZ063LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ063LogT = jcicZ063LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ063LogT = jcicZ063LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else
			jcicZ063LogT = jcicZ063LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

		return jcicZ063LogT.isPresent() ? jcicZ063LogT.get() : null;
	}

	@Override
	public Slice<JcicZ063Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ063Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ063LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ063LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ063LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else
			slice = jcicZ063LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ063Log holdById(JcicZ063LogId jcicZ063LogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ063LogId);
		Optional<JcicZ063Log> jcicZ063Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ063Log = jcicZ063LogReposDay.findByJcicZ063LogId(jcicZ063LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ063Log = jcicZ063LogReposMon.findByJcicZ063LogId(jcicZ063LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ063Log = jcicZ063LogReposHist.findByJcicZ063LogId(jcicZ063LogId);
		else
			jcicZ063Log = jcicZ063LogRepos.findByJcicZ063LogId(jcicZ063LogId);
		return jcicZ063Log.isPresent() ? jcicZ063Log.get() : null;
	}

	@Override
	public JcicZ063Log holdById(JcicZ063Log jcicZ063Log, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ063Log.getJcicZ063LogId());
		Optional<JcicZ063Log> jcicZ063LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ063LogT = jcicZ063LogReposDay.findByJcicZ063LogId(jcicZ063Log.getJcicZ063LogId());
		else if (dbName.equals(ContentName.onMon))
			jcicZ063LogT = jcicZ063LogReposMon.findByJcicZ063LogId(jcicZ063Log.getJcicZ063LogId());
		else if (dbName.equals(ContentName.onHist))
			jcicZ063LogT = jcicZ063LogReposHist.findByJcicZ063LogId(jcicZ063Log.getJcicZ063LogId());
		else
			jcicZ063LogT = jcicZ063LogRepos.findByJcicZ063LogId(jcicZ063Log.getJcicZ063LogId());
		return jcicZ063LogT.isPresent() ? jcicZ063LogT.get() : null;
	}

	@Override
	public JcicZ063Log insert(JcicZ063Log jcicZ063Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ063Log.getJcicZ063LogId());
		if (this.findById(jcicZ063Log.getJcicZ063LogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ063Log.setCreateEmpNo(empNot);

		if (jcicZ063Log.getLastUpdateEmpNo() == null || jcicZ063Log.getLastUpdateEmpNo().isEmpty())
			jcicZ063Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ063LogReposDay.saveAndFlush(jcicZ063Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ063LogReposMon.saveAndFlush(jcicZ063Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ063LogReposHist.saveAndFlush(jcicZ063Log);
		else
			return jcicZ063LogRepos.saveAndFlush(jcicZ063Log);
	}

	@Override
	public JcicZ063Log update(JcicZ063Log jcicZ063Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ063Log.getJcicZ063LogId());
		if (!empNot.isEmpty())
			jcicZ063Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ063LogReposDay.saveAndFlush(jcicZ063Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ063LogReposMon.saveAndFlush(jcicZ063Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ063LogReposHist.saveAndFlush(jcicZ063Log);
		else
			return jcicZ063LogRepos.saveAndFlush(jcicZ063Log);
	}

	@Override
	public JcicZ063Log update2(JcicZ063Log jcicZ063Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ063Log.getJcicZ063LogId());
		if (!empNot.isEmpty())
			jcicZ063Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ063LogReposDay.saveAndFlush(jcicZ063Log);
		else if (dbName.equals(ContentName.onMon))
			jcicZ063LogReposMon.saveAndFlush(jcicZ063Log);
		else if (dbName.equals(ContentName.onHist))
			jcicZ063LogReposHist.saveAndFlush(jcicZ063Log);
		else
			jcicZ063LogRepos.saveAndFlush(jcicZ063Log);
		return this.findById(jcicZ063Log.getJcicZ063LogId());
	}

	@Override
	public void delete(JcicZ063Log jcicZ063Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ063Log.getJcicZ063LogId());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ063LogReposDay.delete(jcicZ063Log);
			jcicZ063LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ063LogReposMon.delete(jcicZ063Log);
			jcicZ063LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ063LogReposHist.delete(jcicZ063Log);
			jcicZ063LogReposHist.flush();
		} else {
			jcicZ063LogRepos.delete(jcicZ063Log);
			jcicZ063LogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ063Log> jcicZ063Log, TitaVo... titaVo) throws DBException {
		if (jcicZ063Log == null || jcicZ063Log.size() == 0)
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
		for (JcicZ063Log t : jcicZ063Log) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ063Log = jcicZ063LogReposDay.saveAll(jcicZ063Log);
			jcicZ063LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ063Log = jcicZ063LogReposMon.saveAll(jcicZ063Log);
			jcicZ063LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ063Log = jcicZ063LogReposHist.saveAll(jcicZ063Log);
			jcicZ063LogReposHist.flush();
		} else {
			jcicZ063Log = jcicZ063LogRepos.saveAll(jcicZ063Log);
			jcicZ063LogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ063Log> jcicZ063Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ063Log == null || jcicZ063Log.size() == 0)
			throw new DBException(6);

		for (JcicZ063Log t : jcicZ063Log)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ063Log = jcicZ063LogReposDay.saveAll(jcicZ063Log);
			jcicZ063LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ063Log = jcicZ063LogReposMon.saveAll(jcicZ063Log);
			jcicZ063LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ063Log = jcicZ063LogReposHist.saveAll(jcicZ063Log);
			jcicZ063LogReposHist.flush();
		} else {
			jcicZ063Log = jcicZ063LogRepos.saveAll(jcicZ063Log);
			jcicZ063LogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ063Log> jcicZ063Log, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ063Log == null || jcicZ063Log.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ063LogReposDay.deleteAll(jcicZ063Log);
			jcicZ063LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ063LogReposMon.deleteAll(jcicZ063Log);
			jcicZ063LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ063LogReposHist.deleteAll(jcicZ063Log);
			jcicZ063LogReposHist.flush();
		} else {
			jcicZ063LogRepos.deleteAll(jcicZ063Log);
			jcicZ063LogRepos.flush();
		}
	}

}
