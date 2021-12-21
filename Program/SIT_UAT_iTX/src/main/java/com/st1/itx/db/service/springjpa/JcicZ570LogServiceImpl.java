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
import com.st1.itx.db.domain.JcicZ570Log;
import com.st1.itx.db.domain.JcicZ570LogId;
import com.st1.itx.db.repository.online.JcicZ570LogRepository;
import com.st1.itx.db.repository.day.JcicZ570LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ570LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ570LogRepositoryHist;
import com.st1.itx.db.service.JcicZ570LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ570LogService")
@Repository
public class JcicZ570LogServiceImpl extends ASpringJpaParm implements JcicZ570LogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ570LogRepository jcicZ570LogRepos;

	@Autowired
	private JcicZ570LogRepositoryDay jcicZ570LogReposDay;

	@Autowired
	private JcicZ570LogRepositoryMon jcicZ570LogReposMon;

	@Autowired
	private JcicZ570LogRepositoryHist jcicZ570LogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ570LogRepos);
		org.junit.Assert.assertNotNull(jcicZ570LogReposDay);
		org.junit.Assert.assertNotNull(jcicZ570LogReposMon);
		org.junit.Assert.assertNotNull(jcicZ570LogReposHist);
	}

	@Override
	public JcicZ570Log findById(JcicZ570LogId jcicZ570LogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ570LogId);
		Optional<JcicZ570Log> jcicZ570Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ570Log = jcicZ570LogReposDay.findById(jcicZ570LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ570Log = jcicZ570LogReposMon.findById(jcicZ570LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ570Log = jcicZ570LogReposHist.findById(jcicZ570LogId);
		else
			jcicZ570Log = jcicZ570LogRepos.findById(jcicZ570LogId);
		JcicZ570Log obj = jcicZ570Log.isPresent() ? jcicZ570Log.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ570Log> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ570Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ570LogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ570LogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ570LogReposHist.findAll(pageable);
		else
			slice = jcicZ570LogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ570Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ570Log> jcicZ570LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ570LogT = jcicZ570LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ570LogT = jcicZ570LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ570LogT = jcicZ570LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else
			jcicZ570LogT = jcicZ570LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

		return jcicZ570LogT.isPresent() ? jcicZ570LogT.get() : null;
	}

	@Override
	public Slice<JcicZ570Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ570Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ570LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ570LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ570LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else
			slice = jcicZ570LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ570Log holdById(JcicZ570LogId jcicZ570LogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ570LogId);
		Optional<JcicZ570Log> jcicZ570Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ570Log = jcicZ570LogReposDay.findByJcicZ570LogId(jcicZ570LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ570Log = jcicZ570LogReposMon.findByJcicZ570LogId(jcicZ570LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ570Log = jcicZ570LogReposHist.findByJcicZ570LogId(jcicZ570LogId);
		else
			jcicZ570Log = jcicZ570LogRepos.findByJcicZ570LogId(jcicZ570LogId);
		return jcicZ570Log.isPresent() ? jcicZ570Log.get() : null;
	}

	@Override
	public JcicZ570Log holdById(JcicZ570Log jcicZ570Log, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ570Log.getJcicZ570LogId());
		Optional<JcicZ570Log> jcicZ570LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ570LogT = jcicZ570LogReposDay.findByJcicZ570LogId(jcicZ570Log.getJcicZ570LogId());
		else if (dbName.equals(ContentName.onMon))
			jcicZ570LogT = jcicZ570LogReposMon.findByJcicZ570LogId(jcicZ570Log.getJcicZ570LogId());
		else if (dbName.equals(ContentName.onHist))
			jcicZ570LogT = jcicZ570LogReposHist.findByJcicZ570LogId(jcicZ570Log.getJcicZ570LogId());
		else
			jcicZ570LogT = jcicZ570LogRepos.findByJcicZ570LogId(jcicZ570Log.getJcicZ570LogId());
		return jcicZ570LogT.isPresent() ? jcicZ570LogT.get() : null;
	}

	@Override
	public JcicZ570Log insert(JcicZ570Log jcicZ570Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ570Log.getJcicZ570LogId());
		if (this.findById(jcicZ570Log.getJcicZ570LogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ570Log.setCreateEmpNo(empNot);

		if (jcicZ570Log.getLastUpdateEmpNo() == null || jcicZ570Log.getLastUpdateEmpNo().isEmpty())
			jcicZ570Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ570LogReposDay.saveAndFlush(jcicZ570Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ570LogReposMon.saveAndFlush(jcicZ570Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ570LogReposHist.saveAndFlush(jcicZ570Log);
		else
			return jcicZ570LogRepos.saveAndFlush(jcicZ570Log);
	}

	@Override
	public JcicZ570Log update(JcicZ570Log jcicZ570Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ570Log.getJcicZ570LogId());
		if (!empNot.isEmpty())
			jcicZ570Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ570LogReposDay.saveAndFlush(jcicZ570Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ570LogReposMon.saveAndFlush(jcicZ570Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ570LogReposHist.saveAndFlush(jcicZ570Log);
		else
			return jcicZ570LogRepos.saveAndFlush(jcicZ570Log);
	}

	@Override
	public JcicZ570Log update2(JcicZ570Log jcicZ570Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ570Log.getJcicZ570LogId());
		if (!empNot.isEmpty())
			jcicZ570Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ570LogReposDay.saveAndFlush(jcicZ570Log);
		else if (dbName.equals(ContentName.onMon))
			jcicZ570LogReposMon.saveAndFlush(jcicZ570Log);
		else if (dbName.equals(ContentName.onHist))
			jcicZ570LogReposHist.saveAndFlush(jcicZ570Log);
		else
			jcicZ570LogRepos.saveAndFlush(jcicZ570Log);
		return this.findById(jcicZ570Log.getJcicZ570LogId());
	}

	@Override
	public void delete(JcicZ570Log jcicZ570Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ570Log.getJcicZ570LogId());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ570LogReposDay.delete(jcicZ570Log);
			jcicZ570LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ570LogReposMon.delete(jcicZ570Log);
			jcicZ570LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ570LogReposHist.delete(jcicZ570Log);
			jcicZ570LogReposHist.flush();
		} else {
			jcicZ570LogRepos.delete(jcicZ570Log);
			jcicZ570LogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ570Log> jcicZ570Log, TitaVo... titaVo) throws DBException {
		if (jcicZ570Log == null || jcicZ570Log.size() == 0)
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
		for (JcicZ570Log t : jcicZ570Log) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ570Log = jcicZ570LogReposDay.saveAll(jcicZ570Log);
			jcicZ570LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ570Log = jcicZ570LogReposMon.saveAll(jcicZ570Log);
			jcicZ570LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ570Log = jcicZ570LogReposHist.saveAll(jcicZ570Log);
			jcicZ570LogReposHist.flush();
		} else {
			jcicZ570Log = jcicZ570LogRepos.saveAll(jcicZ570Log);
			jcicZ570LogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ570Log> jcicZ570Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ570Log == null || jcicZ570Log.size() == 0)
			throw new DBException(6);

		for (JcicZ570Log t : jcicZ570Log)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ570Log = jcicZ570LogReposDay.saveAll(jcicZ570Log);
			jcicZ570LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ570Log = jcicZ570LogReposMon.saveAll(jcicZ570Log);
			jcicZ570LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ570Log = jcicZ570LogReposHist.saveAll(jcicZ570Log);
			jcicZ570LogReposHist.flush();
		} else {
			jcicZ570Log = jcicZ570LogRepos.saveAll(jcicZ570Log);
			jcicZ570LogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ570Log> jcicZ570Log, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ570Log == null || jcicZ570Log.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ570LogReposDay.deleteAll(jcicZ570Log);
			jcicZ570LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ570LogReposMon.deleteAll(jcicZ570Log);
			jcicZ570LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ570LogReposHist.deleteAll(jcicZ570Log);
			jcicZ570LogReposHist.flush();
		} else {
			jcicZ570LogRepos.deleteAll(jcicZ570Log);
			jcicZ570LogRepos.flush();
		}
	}

}
