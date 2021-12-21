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
import com.st1.itx.db.domain.JcicZ447Log;
import com.st1.itx.db.domain.JcicZ447LogId;
import com.st1.itx.db.repository.online.JcicZ447LogRepository;
import com.st1.itx.db.repository.day.JcicZ447LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ447LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ447LogRepositoryHist;
import com.st1.itx.db.service.JcicZ447LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ447LogService")
@Repository
public class JcicZ447LogServiceImpl extends ASpringJpaParm implements JcicZ447LogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ447LogRepository jcicZ447LogRepos;

	@Autowired
	private JcicZ447LogRepositoryDay jcicZ447LogReposDay;

	@Autowired
	private JcicZ447LogRepositoryMon jcicZ447LogReposMon;

	@Autowired
	private JcicZ447LogRepositoryHist jcicZ447LogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ447LogRepos);
		org.junit.Assert.assertNotNull(jcicZ447LogReposDay);
		org.junit.Assert.assertNotNull(jcicZ447LogReposMon);
		org.junit.Assert.assertNotNull(jcicZ447LogReposHist);
	}

	@Override
	public JcicZ447Log findById(JcicZ447LogId jcicZ447LogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ447LogId);
		Optional<JcicZ447Log> jcicZ447Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ447Log = jcicZ447LogReposDay.findById(jcicZ447LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ447Log = jcicZ447LogReposMon.findById(jcicZ447LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ447Log = jcicZ447LogReposHist.findById(jcicZ447LogId);
		else
			jcicZ447Log = jcicZ447LogRepos.findById(jcicZ447LogId);
		JcicZ447Log obj = jcicZ447Log.isPresent() ? jcicZ447Log.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ447Log> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ447Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ447LogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ447LogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ447LogReposHist.findAll(pageable);
		else
			slice = jcicZ447LogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ447Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ447Log> jcicZ447LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ447LogT = jcicZ447LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ447LogT = jcicZ447LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ447LogT = jcicZ447LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else
			jcicZ447LogT = jcicZ447LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

		return jcicZ447LogT.isPresent() ? jcicZ447LogT.get() : null;
	}

	@Override
	public Slice<JcicZ447Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ447Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ447LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ447LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ447LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else
			slice = jcicZ447LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ447Log holdById(JcicZ447LogId jcicZ447LogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ447LogId);
		Optional<JcicZ447Log> jcicZ447Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ447Log = jcicZ447LogReposDay.findByJcicZ447LogId(jcicZ447LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ447Log = jcicZ447LogReposMon.findByJcicZ447LogId(jcicZ447LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ447Log = jcicZ447LogReposHist.findByJcicZ447LogId(jcicZ447LogId);
		else
			jcicZ447Log = jcicZ447LogRepos.findByJcicZ447LogId(jcicZ447LogId);
		return jcicZ447Log.isPresent() ? jcicZ447Log.get() : null;
	}

	@Override
	public JcicZ447Log holdById(JcicZ447Log jcicZ447Log, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ447Log.getJcicZ447LogId());
		Optional<JcicZ447Log> jcicZ447LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ447LogT = jcicZ447LogReposDay.findByJcicZ447LogId(jcicZ447Log.getJcicZ447LogId());
		else if (dbName.equals(ContentName.onMon))
			jcicZ447LogT = jcicZ447LogReposMon.findByJcicZ447LogId(jcicZ447Log.getJcicZ447LogId());
		else if (dbName.equals(ContentName.onHist))
			jcicZ447LogT = jcicZ447LogReposHist.findByJcicZ447LogId(jcicZ447Log.getJcicZ447LogId());
		else
			jcicZ447LogT = jcicZ447LogRepos.findByJcicZ447LogId(jcicZ447Log.getJcicZ447LogId());
		return jcicZ447LogT.isPresent() ? jcicZ447LogT.get() : null;
	}

	@Override
	public JcicZ447Log insert(JcicZ447Log jcicZ447Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ447Log.getJcicZ447LogId());
		if (this.findById(jcicZ447Log.getJcicZ447LogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ447Log.setCreateEmpNo(empNot);

		if (jcicZ447Log.getLastUpdateEmpNo() == null || jcicZ447Log.getLastUpdateEmpNo().isEmpty())
			jcicZ447Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ447LogReposDay.saveAndFlush(jcicZ447Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ447LogReposMon.saveAndFlush(jcicZ447Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ447LogReposHist.saveAndFlush(jcicZ447Log);
		else
			return jcicZ447LogRepos.saveAndFlush(jcicZ447Log);
	}

	@Override
	public JcicZ447Log update(JcicZ447Log jcicZ447Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ447Log.getJcicZ447LogId());
		if (!empNot.isEmpty())
			jcicZ447Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ447LogReposDay.saveAndFlush(jcicZ447Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ447LogReposMon.saveAndFlush(jcicZ447Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ447LogReposHist.saveAndFlush(jcicZ447Log);
		else
			return jcicZ447LogRepos.saveAndFlush(jcicZ447Log);
	}

	@Override
	public JcicZ447Log update2(JcicZ447Log jcicZ447Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ447Log.getJcicZ447LogId());
		if (!empNot.isEmpty())
			jcicZ447Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ447LogReposDay.saveAndFlush(jcicZ447Log);
		else if (dbName.equals(ContentName.onMon))
			jcicZ447LogReposMon.saveAndFlush(jcicZ447Log);
		else if (dbName.equals(ContentName.onHist))
			jcicZ447LogReposHist.saveAndFlush(jcicZ447Log);
		else
			jcicZ447LogRepos.saveAndFlush(jcicZ447Log);
		return this.findById(jcicZ447Log.getJcicZ447LogId());
	}

	@Override
	public void delete(JcicZ447Log jcicZ447Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ447Log.getJcicZ447LogId());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ447LogReposDay.delete(jcicZ447Log);
			jcicZ447LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ447LogReposMon.delete(jcicZ447Log);
			jcicZ447LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ447LogReposHist.delete(jcicZ447Log);
			jcicZ447LogReposHist.flush();
		} else {
			jcicZ447LogRepos.delete(jcicZ447Log);
			jcicZ447LogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ447Log> jcicZ447Log, TitaVo... titaVo) throws DBException {
		if (jcicZ447Log == null || jcicZ447Log.size() == 0)
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
		for (JcicZ447Log t : jcicZ447Log) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ447Log = jcicZ447LogReposDay.saveAll(jcicZ447Log);
			jcicZ447LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ447Log = jcicZ447LogReposMon.saveAll(jcicZ447Log);
			jcicZ447LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ447Log = jcicZ447LogReposHist.saveAll(jcicZ447Log);
			jcicZ447LogReposHist.flush();
		} else {
			jcicZ447Log = jcicZ447LogRepos.saveAll(jcicZ447Log);
			jcicZ447LogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ447Log> jcicZ447Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ447Log == null || jcicZ447Log.size() == 0)
			throw new DBException(6);

		for (JcicZ447Log t : jcicZ447Log)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ447Log = jcicZ447LogReposDay.saveAll(jcicZ447Log);
			jcicZ447LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ447Log = jcicZ447LogReposMon.saveAll(jcicZ447Log);
			jcicZ447LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ447Log = jcicZ447LogReposHist.saveAll(jcicZ447Log);
			jcicZ447LogReposHist.flush();
		} else {
			jcicZ447Log = jcicZ447LogRepos.saveAll(jcicZ447Log);
			jcicZ447LogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ447Log> jcicZ447Log, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ447Log == null || jcicZ447Log.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ447LogReposDay.deleteAll(jcicZ447Log);
			jcicZ447LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ447LogReposMon.deleteAll(jcicZ447Log);
			jcicZ447LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ447LogReposHist.deleteAll(jcicZ447Log);
			jcicZ447LogReposHist.flush();
		} else {
			jcicZ447LogRepos.deleteAll(jcicZ447Log);
			jcicZ447LogRepos.flush();
		}
	}

}
