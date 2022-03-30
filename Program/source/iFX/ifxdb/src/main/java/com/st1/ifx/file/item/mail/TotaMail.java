package com.st1.ifx.file.item.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "subJect", "enter1", "add1", "comm1", "add2", "comm2", "add3", "comm3", "add4", "comm4", "add5", "enter2",
		"attachf", "enter3", "data" })
public class TotaMail implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(TotaMail.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 6882707079461380323L;

	@Cobol("X,100")
	String subJect;

	@Cobol("X,2")
	String enter1;

	@Cobol("X,60")
	String add1;

	@Cobol("X,1")
	String comm1;

	@Cobol("X,60")
	String add2;

	@Cobol("X,1")
	String comm2;

	@Cobol("X,60")
	String add3;

	@Cobol("X,1")
	String comm3;

	@Cobol("X,60")
	String add4;

	@Cobol("X,1")
	String comm4;

	@Cobol("X,60")
	String add5;

	@Cobol("X,2")
	String enter2;

	@Cobol("X,200")
	String attachf;

	@Cobol("X,2")
	String enter3;

	@Cobol("X,2200")
	String data;

	public String getSubJect() {
		return subJect;
	}

	public void setSubJect(String subJect) {
		this.subJect = subJect;
	}

	public List<String> getAdds() {
		String[] adds = { add1, add2, add3, add4, add5 };
		List<String> addressString = new ArrayList<String>();
		for (int i = 0; i < adds.length; i++) {
			// toAddress string去空白
			if (adds[i] != null && !adds[i].trim().isEmpty())
				addressString.add(adds[i].trim());
		}
		return addressString;
	}

	public void setAdd1(String add1) {
		this.add1 = add1;
	}

	public void setAdd2(String add2) {
		this.add2 = add2;
	}

	public void setAdd3(String add3) {
		this.add3 = add3;
	}

	public void setAdd4(String add4) {
		this.add4 = add4;
	}

	public void setAdd5(String add5) {
		this.add5 = add5;
	}

	public String getAdd1() {
		return add1;
	}

	public String getAdd2() {
		return add2;
	}

	public String getAdd3() {
		return add3;
	}

	public String getAdd4() {
		return add4;
	}

	public String getAdd5() {
		return add5;
	}

	public String[] getAttachfs() {
		String[] atths = new String[] { attachf };
		logger.info("attachf:" + attachf);
		if (attachf != null && !attachf.trim().isEmpty()) {
			atths[0] = attachf.trim();
			logger.info("atths[0]:" + atths[0]);
			return atths;
		}
		logger.info("return null!");
		return null;
	}

	public String getAttachf() {
		return attachf;
	}

	public void setAttachf(String attachf) {
		this.attachf = attachf;
	}

	public String getData() {
		return data.trim();
	}

	public void setData(String data) {
		this.data = data.trim();
	}

}
