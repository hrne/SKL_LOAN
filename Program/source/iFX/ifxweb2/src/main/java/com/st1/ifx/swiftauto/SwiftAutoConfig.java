package com.st1.ifx.swiftauto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SwiftAutoConfig {

	@Value("${swift.import.inbox}")
	String notifySwiftFloder;

	@Value("${swift.bicquery.brno}")
	String bicQueryBrno;

	@Value("${swift.bicquery.tlrno}")
	String bicQueryTlrno;

	@Value("${swift.import.copyto}")
	String copytoFolder;

	@Value("${swift.import.eoifolder}")
	String copytoEoiFolder;

	@Value("${swift.bicquery.type.1}")
	String bicQueryType1;

	@Value("${swift.bicquery.type.2}")
	String bicQueryType2;

	@Value("${swift.bicquery.type.3}")
	String bicQueryType3;

	@Value("${swift.import.upload}")
	String uploadSwiftFolder;

	public String getuploadSwiftFolder() {
		return uploadSwiftFolder;
	}
}
