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
import com.st1.itx.db.domain.HlEmpLnYg5Pt;
import com.st1.itx.db.domain.HlEmpLnYg5PtId;
import com.st1.itx.db.repository.online.HlEmpLnYg5PtRepository;
import com.st1.itx.db.repository.day.HlEmpLnYg5PtRepositoryDay;
import com.st1.itx.db.repository.mon.HlEmpLnYg5PtRepositoryMon;
import com.st1.itx.db.repository.hist.HlEmpLnYg5PtRepositoryHist;
import com.st1.itx.db.service.HlEmpLnYg5PtService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("hlEmpLnYg5PtService")
@Repository
public class HlEmpLnYg5PtServiceImpl extends ASpringJpaParm implements HlEmpLnYg5PtService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private HlEmpLnYg5PtRepository hlEmpLnYg5PtRepos;

	@Autowired
	private HlEmpLnYg5PtRepositoryDay hlEmpLnYg5PtReposDay;

	@Autowired
	private HlEmpLnYg5PtRepositoryMon hlEmpLnYg5PtReposMon;

	@Autowired
	private HlEmpLnYg5PtRepositoryHist hlEmpLnYg5PtReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(hlEmpLnYg5PtRepos);
		org.junit.Assert.assertNotNull(hlEmpLnYg5PtReposDay);
		org.junit.Assert.assertNotNull(hlEmpLnYg5PtReposMon);
		org.junit.Assert.assertNotNull(hlEmpLnYg5PtReposHist);
	}

	@Override
	public HlEmpLnYg5Pt findById(HlEmpLnYg5PtId hlEmpLnYg5PtId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + hlEmpLnYg5PtId);
		Optional<HlEmpLnYg5Pt> hlEmpLnYg5Pt = null;
		if (dbName.equals(ContentName.onDay))
			hlEmpLnYg5Pt = hlEmpLnYg5PtReposDay.findById(hlEmpLnYg5PtId);
		else if (dbName.equals(ContentName.onMon))
			hlEmpLnYg5Pt = hlEmpLnYg5PtReposMon.findById(hlEmpLnYg5PtId);
		else if (dbName.equals(ContentName.onHist))
			hlEmpLnYg5Pt = hlEmpLnYg5PtReposHist.findById(hlEmpLnYg5PtId);
		else
			hlEmpLnYg5Pt = hlEmpLnYg5PtRepos.findById(hlEmpLnYg5PtId);
		HlEmpLnYg5Pt obj = hlEmpLnYg5Pt.isPresent() ? hlEmpLnYg5Pt.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<HlEmpLnYg5Pt> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<HlEmpLnYg5Pt> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "WorkYM", "AreaUnitNo", "HlEmpNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "WorkYM", "AreaUnitNo", "HlEmpNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = hlEmpLnYg5PtReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = hlEmpLnYg5PtReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = hlEmpLnYg5PtReposHist.findAll(pageable);
		else
			slice = hlEmpLnYg5PtRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<HlEmpLnYg5Pt> FindWorkYM(String workYM_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<HlEmpLnYg5Pt> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("FindWorkYM " + dbName + " : " + "workYM_0 : " + workYM_0);
		if (dbName.equals(ContentName.onDay))
			slice = hlEmpLnYg5PtReposDay.findAllByWorkYMIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(workYM_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = hlEmpLnYg5PtReposMon.findAllByWorkYMIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(workYM_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = hlEmpLnYg5PtReposHist.findAllByWorkYMIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(workYM_0, pageable);
		else
			slice = hlEmpLnYg5PtRepos.findAllByWorkYMIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(workYM_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<HlEmpLnYg5Pt> FindAreaUnitNo(String areaUnitNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<HlEmpLnYg5Pt> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("FindAreaUnitNo " + dbName + " : " + "areaUnitNo_0 : " + areaUnitNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = hlEmpLnYg5PtReposDay.findAllByAreaUnitNoIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(areaUnitNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = hlEmpLnYg5PtReposMon.findAllByAreaUnitNoIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(areaUnitNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = hlEmpLnYg5PtReposHist.findAllByAreaUnitNoIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(areaUnitNo_0, pageable);
		else
			slice = hlEmpLnYg5PtRepos.findAllByAreaUnitNoIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(areaUnitNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<HlEmpLnYg5Pt> FindHlEmpNo(String hlEmpNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<HlEmpLnYg5Pt> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("FindHlEmpNo " + dbName + " : " + "hlEmpNo_0 : " + hlEmpNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = hlEmpLnYg5PtReposDay.findAllByHlEmpNoIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(hlEmpNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = hlEmpLnYg5PtReposMon.findAllByHlEmpNoIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(hlEmpNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = hlEmpLnYg5PtReposHist.findAllByHlEmpNoIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(hlEmpNo_0, pageable);
		else
			slice = hlEmpLnYg5PtRepos.findAllByHlEmpNoIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(hlEmpNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public HlEmpLnYg5Pt holdById(HlEmpLnYg5PtId hlEmpLnYg5PtId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + hlEmpLnYg5PtId);
		Optional<HlEmpLnYg5Pt> hlEmpLnYg5Pt = null;
		if (dbName.equals(ContentName.onDay))
			hlEmpLnYg5Pt = hlEmpLnYg5PtReposDay.findByHlEmpLnYg5PtId(hlEmpLnYg5PtId);
		else if (dbName.equals(ContentName.onMon))
			hlEmpLnYg5Pt = hlEmpLnYg5PtReposMon.findByHlEmpLnYg5PtId(hlEmpLnYg5PtId);
		else if (dbName.equals(ContentName.onHist))
			hlEmpLnYg5Pt = hlEmpLnYg5PtReposHist.findByHlEmpLnYg5PtId(hlEmpLnYg5PtId);
		else
			hlEmpLnYg5Pt = hlEmpLnYg5PtRepos.findByHlEmpLnYg5PtId(hlEmpLnYg5PtId);
		return hlEmpLnYg5Pt.isPresent() ? hlEmpLnYg5Pt.get() : null;
	}

	@Override
	public HlEmpLnYg5Pt holdById(HlEmpLnYg5Pt hlEmpLnYg5Pt, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + hlEmpLnYg5Pt.getHlEmpLnYg5PtId());
		Optional<HlEmpLnYg5Pt> hlEmpLnYg5PtT = null;
		if (dbName.equals(ContentName.onDay))
			hlEmpLnYg5PtT = hlEmpLnYg5PtReposDay.findByHlEmpLnYg5PtId(hlEmpLnYg5Pt.getHlEmpLnYg5PtId());
		else if (dbName.equals(ContentName.onMon))
			hlEmpLnYg5PtT = hlEmpLnYg5PtReposMon.findByHlEmpLnYg5PtId(hlEmpLnYg5Pt.getHlEmpLnYg5PtId());
		else if (dbName.equals(ContentName.onHist))
			hlEmpLnYg5PtT = hlEmpLnYg5PtReposHist.findByHlEmpLnYg5PtId(hlEmpLnYg5Pt.getHlEmpLnYg5PtId());
		else
			hlEmpLnYg5PtT = hlEmpLnYg5PtRepos.findByHlEmpLnYg5PtId(hlEmpLnYg5Pt.getHlEmpLnYg5PtId());
		return hlEmpLnYg5PtT.isPresent() ? hlEmpLnYg5PtT.get() : null;
	}

	@Override
	public HlEmpLnYg5Pt insert(HlEmpLnYg5Pt hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + hlEmpLnYg5Pt.getHlEmpLnYg5PtId());
		if (this.findById(hlEmpLnYg5Pt.getHlEmpLnYg5PtId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			hlEmpLnYg5Pt.setCreateEmpNo(empNot);

		if (hlEmpLnYg5Pt.getLastUpdateEmpNo() == null || hlEmpLnYg5Pt.getLastUpdateEmpNo().isEmpty())
			hlEmpLnYg5Pt.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return hlEmpLnYg5PtReposDay.saveAndFlush(hlEmpLnYg5Pt);
		else if (dbName.equals(ContentName.onMon))
			return hlEmpLnYg5PtReposMon.saveAndFlush(hlEmpLnYg5Pt);
		else if (dbName.equals(ContentName.onHist))
			return hlEmpLnYg5PtReposHist.saveAndFlush(hlEmpLnYg5Pt);
		else
			return hlEmpLnYg5PtRepos.saveAndFlush(hlEmpLnYg5Pt);
	}

	@Override
	public HlEmpLnYg5Pt update(HlEmpLnYg5Pt hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + hlEmpLnYg5Pt.getHlEmpLnYg5PtId());
		if (!empNot.isEmpty())
			hlEmpLnYg5Pt.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return hlEmpLnYg5PtReposDay.saveAndFlush(hlEmpLnYg5Pt);
		else if (dbName.equals(ContentName.onMon))
			return hlEmpLnYg5PtReposMon.saveAndFlush(hlEmpLnYg5Pt);
		else if (dbName.equals(ContentName.onHist))
			return hlEmpLnYg5PtReposHist.saveAndFlush(hlEmpLnYg5Pt);
		else
			return hlEmpLnYg5PtRepos.saveAndFlush(hlEmpLnYg5Pt);
	}

	@Override
	public HlEmpLnYg5Pt update2(HlEmpLnYg5Pt hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + hlEmpLnYg5Pt.getHlEmpLnYg5PtId());
		if (!empNot.isEmpty())
			hlEmpLnYg5Pt.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			hlEmpLnYg5PtReposDay.saveAndFlush(hlEmpLnYg5Pt);
		else if (dbName.equals(ContentName.onMon))
			hlEmpLnYg5PtReposMon.saveAndFlush(hlEmpLnYg5Pt);
		else if (dbName.equals(ContentName.onHist))
			hlEmpLnYg5PtReposHist.saveAndFlush(hlEmpLnYg5Pt);
		else
			hlEmpLnYg5PtRepos.saveAndFlush(hlEmpLnYg5Pt);
		return this.findById(hlEmpLnYg5Pt.getHlEmpLnYg5PtId());
	}

	@Override
	public void delete(HlEmpLnYg5Pt hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + hlEmpLnYg5Pt.getHlEmpLnYg5PtId());
		if (dbName.equals(ContentName.onDay)) {
			hlEmpLnYg5PtReposDay.delete(hlEmpLnYg5Pt);
			hlEmpLnYg5PtReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			hlEmpLnYg5PtReposMon.delete(hlEmpLnYg5Pt);
			hlEmpLnYg5PtReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			hlEmpLnYg5PtReposHist.delete(hlEmpLnYg5Pt);
			hlEmpLnYg5PtReposHist.flush();
		} else {
			hlEmpLnYg5PtRepos.delete(hlEmpLnYg5Pt);
			hlEmpLnYg5PtRepos.flush();
		}
	}

	@Override
	public void insertAll(List<HlEmpLnYg5Pt> hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException {
		if (hlEmpLnYg5Pt == null || hlEmpLnYg5Pt.size() == 0)
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
		for (HlEmpLnYg5Pt t : hlEmpLnYg5Pt) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			hlEmpLnYg5Pt = hlEmpLnYg5PtReposDay.saveAll(hlEmpLnYg5Pt);
			hlEmpLnYg5PtReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			hlEmpLnYg5Pt = hlEmpLnYg5PtReposMon.saveAll(hlEmpLnYg5Pt);
			hlEmpLnYg5PtReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			hlEmpLnYg5Pt = hlEmpLnYg5PtReposHist.saveAll(hlEmpLnYg5Pt);
			hlEmpLnYg5PtReposHist.flush();
		} else {
			hlEmpLnYg5Pt = hlEmpLnYg5PtRepos.saveAll(hlEmpLnYg5Pt);
			hlEmpLnYg5PtRepos.flush();
		}
	}

	@Override
	public void updateAll(List<HlEmpLnYg5Pt> hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (hlEmpLnYg5Pt == null || hlEmpLnYg5Pt.size() == 0)
			throw new DBException(6);

		for (HlEmpLnYg5Pt t : hlEmpLnYg5Pt)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			hlEmpLnYg5Pt = hlEmpLnYg5PtReposDay.saveAll(hlEmpLnYg5Pt);
			hlEmpLnYg5PtReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			hlEmpLnYg5Pt = hlEmpLnYg5PtReposMon.saveAll(hlEmpLnYg5Pt);
			hlEmpLnYg5PtReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			hlEmpLnYg5Pt = hlEmpLnYg5PtReposHist.saveAll(hlEmpLnYg5Pt);
			hlEmpLnYg5PtReposHist.flush();
		} else {
			hlEmpLnYg5Pt = hlEmpLnYg5PtRepos.saveAll(hlEmpLnYg5Pt);
			hlEmpLnYg5PtRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<HlEmpLnYg5Pt> hlEmpLnYg5Pt, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (hlEmpLnYg5Pt == null || hlEmpLnYg5Pt.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			hlEmpLnYg5PtReposDay.deleteAll(hlEmpLnYg5Pt);
			hlEmpLnYg5PtReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			hlEmpLnYg5PtReposMon.deleteAll(hlEmpLnYg5Pt);
			hlEmpLnYg5PtReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			hlEmpLnYg5PtReposHist.deleteAll(hlEmpLnYg5Pt);
			hlEmpLnYg5PtReposHist.flush();
		} else {
			hlEmpLnYg5PtRepos.deleteAll(hlEmpLnYg5Pt);
			hlEmpLnYg5PtRepos.flush();
		}
	}

}
