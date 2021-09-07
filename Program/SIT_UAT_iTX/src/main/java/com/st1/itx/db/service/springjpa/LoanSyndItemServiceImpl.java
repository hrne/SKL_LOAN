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
import com.st1.itx.db.domain.LoanSyndItem;
import com.st1.itx.db.domain.LoanSyndItemId;
import com.st1.itx.db.repository.online.LoanSyndItemRepository;
import com.st1.itx.db.repository.day.LoanSyndItemRepositoryDay;
import com.st1.itx.db.repository.mon.LoanSyndItemRepositoryMon;
import com.st1.itx.db.repository.hist.LoanSyndItemRepositoryHist;
import com.st1.itx.db.service.LoanSyndItemService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanSyndItemService")
@Repository
public class LoanSyndItemServiceImpl extends ASpringJpaParm implements LoanSyndItemService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanSyndItemRepository loanSyndItemRepos;

	@Autowired
	private LoanSyndItemRepositoryDay loanSyndItemReposDay;

	@Autowired
	private LoanSyndItemRepositoryMon loanSyndItemReposMon;

	@Autowired
	private LoanSyndItemRepositoryHist loanSyndItemReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanSyndItemRepos);
		org.junit.Assert.assertNotNull(loanSyndItemReposDay);
		org.junit.Assert.assertNotNull(loanSyndItemReposMon);
		org.junit.Assert.assertNotNull(loanSyndItemReposHist);
	}

	@Override
	public LoanSyndItem findById(LoanSyndItemId loanSyndItemId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + loanSyndItemId);
		Optional<LoanSyndItem> loanSyndItem = null;
		if (dbName.equals(ContentName.onDay))
			loanSyndItem = loanSyndItemReposDay.findById(loanSyndItemId);
		else if (dbName.equals(ContentName.onMon))
			loanSyndItem = loanSyndItemReposMon.findById(loanSyndItemId);
		else if (dbName.equals(ContentName.onHist))
			loanSyndItem = loanSyndItemReposHist.findById(loanSyndItemId);
		else
			loanSyndItem = loanSyndItemRepos.findById(loanSyndItemId);
		LoanSyndItem obj = loanSyndItem.isPresent() ? loanSyndItem.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<LoanSyndItem> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanSyndItem> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "SyndNo", "Item"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = loanSyndItemReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanSyndItemReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanSyndItemReposHist.findAll(pageable);
		else
			slice = loanSyndItemRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanSyndItem> findSyndNo(int custNo_0, int syndNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanSyndItem> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findSyndNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " syndNo_1 : " + syndNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = loanSyndItemReposDay.findAllByCustNoIsAndSyndNoIsOrderByItemAsc(custNo_0, syndNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanSyndItemReposMon.findAllByCustNoIsAndSyndNoIsOrderByItemAsc(custNo_0, syndNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanSyndItemReposHist.findAllByCustNoIsAndSyndNoIsOrderByItemAsc(custNo_0, syndNo_1, pageable);
		else
			slice = loanSyndItemRepos.findAllByCustNoIsAndSyndNoIsOrderByItemAsc(custNo_0, syndNo_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public LoanSyndItem holdById(LoanSyndItemId loanSyndItemId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanSyndItemId);
		Optional<LoanSyndItem> loanSyndItem = null;
		if (dbName.equals(ContentName.onDay))
			loanSyndItem = loanSyndItemReposDay.findByLoanSyndItemId(loanSyndItemId);
		else if (dbName.equals(ContentName.onMon))
			loanSyndItem = loanSyndItemReposMon.findByLoanSyndItemId(loanSyndItemId);
		else if (dbName.equals(ContentName.onHist))
			loanSyndItem = loanSyndItemReposHist.findByLoanSyndItemId(loanSyndItemId);
		else
			loanSyndItem = loanSyndItemRepos.findByLoanSyndItemId(loanSyndItemId);
		return loanSyndItem.isPresent() ? loanSyndItem.get() : null;
	}

	@Override
	public LoanSyndItem holdById(LoanSyndItem loanSyndItem, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanSyndItem.getLoanSyndItemId());
		Optional<LoanSyndItem> loanSyndItemT = null;
		if (dbName.equals(ContentName.onDay))
			loanSyndItemT = loanSyndItemReposDay.findByLoanSyndItemId(loanSyndItem.getLoanSyndItemId());
		else if (dbName.equals(ContentName.onMon))
			loanSyndItemT = loanSyndItemReposMon.findByLoanSyndItemId(loanSyndItem.getLoanSyndItemId());
		else if (dbName.equals(ContentName.onHist))
			loanSyndItemT = loanSyndItemReposHist.findByLoanSyndItemId(loanSyndItem.getLoanSyndItemId());
		else
			loanSyndItemT = loanSyndItemRepos.findByLoanSyndItemId(loanSyndItem.getLoanSyndItemId());
		return loanSyndItemT.isPresent() ? loanSyndItemT.get() : null;
	}

	@Override
	public LoanSyndItem insert(LoanSyndItem loanSyndItem, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + loanSyndItem.getLoanSyndItemId());
		if (this.findById(loanSyndItem.getLoanSyndItemId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			loanSyndItem.setCreateEmpNo(empNot);

		if (loanSyndItem.getLastUpdateEmpNo() == null || loanSyndItem.getLastUpdateEmpNo().isEmpty())
			loanSyndItem.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanSyndItemReposDay.saveAndFlush(loanSyndItem);
		else if (dbName.equals(ContentName.onMon))
			return loanSyndItemReposMon.saveAndFlush(loanSyndItem);
		else if (dbName.equals(ContentName.onHist))
			return loanSyndItemReposHist.saveAndFlush(loanSyndItem);
		else
			return loanSyndItemRepos.saveAndFlush(loanSyndItem);
	}

	@Override
	public LoanSyndItem update(LoanSyndItem loanSyndItem, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + loanSyndItem.getLoanSyndItemId());
		if (!empNot.isEmpty())
			loanSyndItem.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanSyndItemReposDay.saveAndFlush(loanSyndItem);
		else if (dbName.equals(ContentName.onMon))
			return loanSyndItemReposMon.saveAndFlush(loanSyndItem);
		else if (dbName.equals(ContentName.onHist))
			return loanSyndItemReposHist.saveAndFlush(loanSyndItem);
		else
			return loanSyndItemRepos.saveAndFlush(loanSyndItem);
	}

	@Override
	public LoanSyndItem update2(LoanSyndItem loanSyndItem, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + loanSyndItem.getLoanSyndItemId());
		if (!empNot.isEmpty())
			loanSyndItem.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			loanSyndItemReposDay.saveAndFlush(loanSyndItem);
		else if (dbName.equals(ContentName.onMon))
			loanSyndItemReposMon.saveAndFlush(loanSyndItem);
		else if (dbName.equals(ContentName.onHist))
			loanSyndItemReposHist.saveAndFlush(loanSyndItem);
		else
			loanSyndItemRepos.saveAndFlush(loanSyndItem);
		return this.findById(loanSyndItem.getLoanSyndItemId());
	}

	@Override
	public void delete(LoanSyndItem loanSyndItem, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + loanSyndItem.getLoanSyndItemId());
		if (dbName.equals(ContentName.onDay)) {
			loanSyndItemReposDay.delete(loanSyndItem);
			loanSyndItemReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanSyndItemReposMon.delete(loanSyndItem);
			loanSyndItemReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanSyndItemReposHist.delete(loanSyndItem);
			loanSyndItemReposHist.flush();
		} else {
			loanSyndItemRepos.delete(loanSyndItem);
			loanSyndItemRepos.flush();
		}
	}

	@Override
	public void insertAll(List<LoanSyndItem> loanSyndItem, TitaVo... titaVo) throws DBException {
		if (loanSyndItem == null || loanSyndItem.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (LoanSyndItem t : loanSyndItem) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			loanSyndItem = loanSyndItemReposDay.saveAll(loanSyndItem);
			loanSyndItemReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanSyndItem = loanSyndItemReposMon.saveAll(loanSyndItem);
			loanSyndItemReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanSyndItem = loanSyndItemReposHist.saveAll(loanSyndItem);
			loanSyndItemReposHist.flush();
		} else {
			loanSyndItem = loanSyndItemRepos.saveAll(loanSyndItem);
			loanSyndItemRepos.flush();
		}
	}

	@Override
	public void updateAll(List<LoanSyndItem> loanSyndItem, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (loanSyndItem == null || loanSyndItem.size() == 0)
			throw new DBException(6);

		for (LoanSyndItem t : loanSyndItem)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			loanSyndItem = loanSyndItemReposDay.saveAll(loanSyndItem);
			loanSyndItemReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanSyndItem = loanSyndItemReposMon.saveAll(loanSyndItem);
			loanSyndItemReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanSyndItem = loanSyndItemReposHist.saveAll(loanSyndItem);
			loanSyndItemReposHist.flush();
		} else {
			loanSyndItem = loanSyndItemRepos.saveAll(loanSyndItem);
			loanSyndItemRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<LoanSyndItem> loanSyndItem, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (loanSyndItem == null || loanSyndItem.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			loanSyndItemReposDay.deleteAll(loanSyndItem);
			loanSyndItemReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanSyndItemReposMon.deleteAll(loanSyndItem);
			loanSyndItemReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanSyndItemReposHist.deleteAll(loanSyndItem);
			loanSyndItemReposHist.flush();
		} else {
			loanSyndItemRepos.deleteAll(loanSyndItem);
			loanSyndItemRepos.flush();
		}
	}

}
