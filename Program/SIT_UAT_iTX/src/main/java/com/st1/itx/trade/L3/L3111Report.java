package com.st1.itx.trade.L3;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.util.common.MakeReport;

@Component("l3111Report")
@Scope("prototype")

public class L3111Report extends MakeReport {
	@Override
	public void printTitle() {
//		this.print(-8, 2, "NO");
//		this.print(-8, 11, "DESC");
//		this.print(-9, 2, "===");
//		this.print(-9, 11, "============================================================");
		this.print(-8, 1, "┌────────────────────┬──────┬─────────┐");
		this.print(-9, 1, "│　　　　　　　　　　　　　　　　　　　　│　件　　數　│　貸　款　金　額　│");
	}

}
