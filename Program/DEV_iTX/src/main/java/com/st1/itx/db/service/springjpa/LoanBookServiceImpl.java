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
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.domain.LoanBookId;
import com.st1.itx.db.repository.online.LoanBookRepository;
import com.st1.itx.db.repository.day.LoanBookRepositoryDay;
import com.st1.itx.db.repository.mon.LoanBookRepositoryMon;
import com.st1.itx.db.repository.hist.LoanBookRepositoryHist;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanBookService")
@Repository
public class LoanBookServiceImpl extends ASpringJpaParm implements LoanBookService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanBookRepository loanBookRepos;

  @Autowired
  private LoanBookRepositoryDay loanBookReposDay;

  @Autowired
  private LoanBookRepositoryMon loanBookReposMon;

  @Autowired
  private LoanBookRepositoryHist loanBookReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanBookRepos);
    org.junit.Assert.assertNotNull(loanBookReposDay);
    org.junit.Assert.assertNotNull(loanBookReposMon);
    org.junit.Assert.assertNotNull(loanBookReposHist);
  }

  @Override
  public LoanBook findById(LoanBookId loanBookId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanBookId);
    Optional<LoanBook> loanBook = null;
    if (dbName.equals(ContentName.onDay))
      loanBook = loanBookReposDay.findById(loanBookId);
    else if (dbName.equals(ContentName.onMon))
      loanBook = loanBookReposMon.findById(loanBookId);
    else if (dbName.equals(ContentName.onHist))
      loanBook = loanBookReposHist.findById(loanBookId);
    else 
      loanBook = loanBookRepos.findById(loanBookId);
    LoanBook obj = loanBook.isPresent() ? loanBook.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanBook> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBook> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "BormNo", "BookDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "BormNo", "BookDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanBookReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBookReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBookReposHist.findAll(pageable);
    else 
      slice = loanBookRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBook> bookDateRange(int bookDate_0, int bookDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBook> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("bookDateRange " + dbName + " : " + "bookDate_0 : " + bookDate_0 + " bookDate_1 : " +  bookDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = loanBookReposDay.findAllByBookDateGreaterThanEqualAndBookDateLessThanEqual(bookDate_0, bookDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBookReposMon.findAllByBookDateGreaterThanEqualAndBookDateLessThanEqual(bookDate_0, bookDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBookReposHist.findAllByBookDateGreaterThanEqualAndBookDateLessThanEqual(bookDate_0, bookDate_1, pageable);
    else 
      slice = loanBookRepos.findAllByBookDateGreaterThanEqualAndBookDateLessThanEqual(bookDate_0, bookDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanBook bookBormNoFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("bookBormNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2);
    Optional<LoanBook> loanBookT = null;
    if (dbName.equals(ContentName.onDay))
      loanBookT = loanBookReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByBookDateAsc(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onMon))
      loanBookT = loanBookReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByBookDateAsc(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onHist))
      loanBookT = loanBookReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByBookDateAsc(custNo_0, facmNo_1, bormNo_2);
    else 
      loanBookT = loanBookRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByBookDateAsc(custNo_0, facmNo_1, bormNo_2);

    return loanBookT.isPresent() ? loanBookT.get() : null;
  }

  @Override
  public Slice<LoanBook> bookBormNoRange(int custNo_0, int facmNo_1, int bormNo_2, int bookDate_3, int bookDate_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBook> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("bookBormNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " bookDate_3 : " +  bookDate_3 + " bookDate_4 : " +  bookDate_4);
    if (dbName.equals(ContentName.onDay))
      slice = loanBookReposDay.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBookDateGreaterThanEqualAndBookDateLessThanEqualOrderByBookDateAsc(custNo_0, facmNo_1, bormNo_2, bookDate_3, bookDate_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBookReposMon.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBookDateGreaterThanEqualAndBookDateLessThanEqualOrderByBookDateAsc(custNo_0, facmNo_1, bormNo_2, bookDate_3, bookDate_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBookReposHist.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBookDateGreaterThanEqualAndBookDateLessThanEqualOrderByBookDateAsc(custNo_0, facmNo_1, bormNo_2, bookDate_3, bookDate_4, pageable);
    else 
      slice = loanBookRepos.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBookDateGreaterThanEqualAndBookDateLessThanEqualOrderByBookDateAsc(custNo_0, facmNo_1, bormNo_2, bookDate_3, bookDate_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBook> bookCustNoRange(int custNo_0, int custNo_1, int facmNo_2, int facmNo_3, int bormNo_4, int bormNo_5, int bookDate_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBook> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("bookCustNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2 + " facmNo_3 : " +  facmNo_3 + " bormNo_4 : " +  bormNo_4 + " bormNo_5 : " +  bormNo_5 + " bookDate_6 : " +  bookDate_6);
    if (dbName.equals(ContentName.onDay))
      slice = loanBookReposDay.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndBookDateGreaterThanEqualOrderByBookDateAsc(custNo_0, custNo_1, facmNo_2, facmNo_3, bormNo_4, bormNo_5, bookDate_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBookReposMon.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndBookDateGreaterThanEqualOrderByBookDateAsc(custNo_0, custNo_1, facmNo_2, facmNo_3, bormNo_4, bormNo_5, bookDate_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBookReposHist.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndBookDateGreaterThanEqualOrderByBookDateAsc(custNo_0, custNo_1, facmNo_2, facmNo_3, bormNo_4, bormNo_5, bookDate_6, pageable);
    else 
      slice = loanBookRepos.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndBookDateGreaterThanEqualOrderByBookDateAsc(custNo_0, custNo_1, facmNo_2, facmNo_3, bormNo_4, bormNo_5, bookDate_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanBook facmNoLastBookDateFirst(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("facmNoLastBookDateFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2 + " bormNo_3 : " +  bormNo_3 + " bormNo_4 : " +  bormNo_4);
    Optional<LoanBook> loanBookT = null;
    if (dbName.equals(ContentName.onDay))
      loanBookT = loanBookReposDay.findTopByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByBookDateDesc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4);
    else if (dbName.equals(ContentName.onMon))
      loanBookT = loanBookReposMon.findTopByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByBookDateDesc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4);
    else if (dbName.equals(ContentName.onHist))
      loanBookT = loanBookReposHist.findTopByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByBookDateDesc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4);
    else 
      loanBookT = loanBookRepos.findTopByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByBookDateDesc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4);

    return loanBookT.isPresent() ? loanBookT.get() : null;
  }

  @Override
  public LoanBook holdById(LoanBookId loanBookId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanBookId);
    Optional<LoanBook> loanBook = null;
    if (dbName.equals(ContentName.onDay))
      loanBook = loanBookReposDay.findByLoanBookId(loanBookId);
    else if (dbName.equals(ContentName.onMon))
      loanBook = loanBookReposMon.findByLoanBookId(loanBookId);
    else if (dbName.equals(ContentName.onHist))
      loanBook = loanBookReposHist.findByLoanBookId(loanBookId);
    else 
      loanBook = loanBookRepos.findByLoanBookId(loanBookId);
    return loanBook.isPresent() ? loanBook.get() : null;
  }

  @Override
  public LoanBook holdById(LoanBook loanBook, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanBook.getLoanBookId());
    Optional<LoanBook> loanBookT = null;
    if (dbName.equals(ContentName.onDay))
      loanBookT = loanBookReposDay.findByLoanBookId(loanBook.getLoanBookId());
    else if (dbName.equals(ContentName.onMon))
      loanBookT = loanBookReposMon.findByLoanBookId(loanBook.getLoanBookId());
    else if (dbName.equals(ContentName.onHist))
      loanBookT = loanBookReposHist.findByLoanBookId(loanBook.getLoanBookId());
    else 
      loanBookT = loanBookRepos.findByLoanBookId(loanBook.getLoanBookId());
    return loanBookT.isPresent() ? loanBookT.get() : null;
  }

  @Override
  public LoanBook insert(LoanBook loanBook, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + loanBook.getLoanBookId());
    if (this.findById(loanBook.getLoanBookId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanBook.setCreateEmpNo(empNot);

    if(loanBook.getLastUpdateEmpNo() == null || loanBook.getLastUpdateEmpNo().isEmpty())
      loanBook.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanBookReposDay.saveAndFlush(loanBook);	
    else if (dbName.equals(ContentName.onMon))
      return loanBookReposMon.saveAndFlush(loanBook);
    else if (dbName.equals(ContentName.onHist))
      return loanBookReposHist.saveAndFlush(loanBook);
    else 
    return loanBookRepos.saveAndFlush(loanBook);
  }

  @Override
  public LoanBook update(LoanBook loanBook, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + loanBook.getLoanBookId());
    if (!empNot.isEmpty())
      loanBook.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanBookReposDay.saveAndFlush(loanBook);	
    else if (dbName.equals(ContentName.onMon))
      return loanBookReposMon.saveAndFlush(loanBook);
    else if (dbName.equals(ContentName.onHist))
      return loanBookReposHist.saveAndFlush(loanBook);
    else 
    return loanBookRepos.saveAndFlush(loanBook);
  }

  @Override
  public LoanBook update2(LoanBook loanBook, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + loanBook.getLoanBookId());
    if (!empNot.isEmpty())
      loanBook.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanBookReposDay.saveAndFlush(loanBook);	
    else if (dbName.equals(ContentName.onMon))
      loanBookReposMon.saveAndFlush(loanBook);
    else if (dbName.equals(ContentName.onHist))
        loanBookReposHist.saveAndFlush(loanBook);
    else 
      loanBookRepos.saveAndFlush(loanBook);	
    return this.findById(loanBook.getLoanBookId());
  }

  @Override
  public void delete(LoanBook loanBook, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanBook.getLoanBookId());
    if (dbName.equals(ContentName.onDay)) {
      loanBookReposDay.delete(loanBook);	
      loanBookReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanBookReposMon.delete(loanBook);	
      loanBookReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanBookReposHist.delete(loanBook);
      loanBookReposHist.flush();
    }
    else {
      loanBookRepos.delete(loanBook);
      loanBookRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanBook> loanBook, TitaVo... titaVo) throws DBException {
    if (loanBook == null || loanBook.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (LoanBook t : loanBook){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanBook = loanBookReposDay.saveAll(loanBook);	
      loanBookReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanBook = loanBookReposMon.saveAll(loanBook);	
      loanBookReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanBook = loanBookReposHist.saveAll(loanBook);
      loanBookReposHist.flush();
    }
    else {
      loanBook = loanBookRepos.saveAll(loanBook);
      loanBookRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanBook> loanBook, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (loanBook == null || loanBook.size() == 0)
      throw new DBException(6);

    for (LoanBook t : loanBook) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanBook = loanBookReposDay.saveAll(loanBook);	
      loanBookReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanBook = loanBookReposMon.saveAll(loanBook);	
      loanBookReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanBook = loanBookReposHist.saveAll(loanBook);
      loanBookReposHist.flush();
    }
    else {
      loanBook = loanBookRepos.saveAll(loanBook);
      loanBookRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanBook> loanBook, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanBook == null || loanBook.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanBookReposDay.deleteAll(loanBook);	
      loanBookReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanBookReposMon.deleteAll(loanBook);	
      loanBookReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanBookReposHist.deleteAll(loanBook);
      loanBookReposHist.flush();
    }
    else {
      loanBookRepos.deleteAll(loanBook);
      loanBookRepos.flush();
    }
  }

}
