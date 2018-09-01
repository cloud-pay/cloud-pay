package com.cloud.pay.common.contants;

public enum BohaiMessageEleEnumTmp {
	SCSP("SCSPReq","SCSPRes"),SCTQ("SCTQReq","SCTQRes"),SCUP("SCUPReq","SCUPRes"),SCHP("SCHPReq","SCSPRes");
	
	String req;
	
	String rsp;
	
	private BohaiMessageEleEnumTmp(String req,String rsp) {
		this.req = req;
		this.rsp = rsp;
	}
	
	public static String getRsp(String req) {
		for(BohaiMessageEleEnumTmp tmp:values()) {
			if(req.equals(tmp.getReq())) {
				return tmp.getRsp();
			}
		}
		return null;
	}

	public String getReq() {
		return req;
	}

	public void setReq(String req) {
		this.req = req;
	}

	public String getRsp() {
		return rsp;
	}

	public void setRsp(String rsp) {
		this.rsp = rsp;
	}
	
	
}
