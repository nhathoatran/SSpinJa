package sspinja;

import java.util.ArrayList;

import sspinja.scheduler.promela.model.SchedulerPromelaModel;

public class GenerateCode {
	public int pid = -1;
	public int execid = -1;
	public int branchid = -1;
	public int pinstance;
	public int stateid ;
	public ArrayList<Byte> pcnt ;
	public Code gencode;	
	
	public ArrayList<Byte> getPcnt() {
		return pcnt ;
	}
	public GenerateCode() {
		pid = -1 ;
		stateid = -1 ;
		pcnt = new ArrayList<Byte>() ;
		gencode = new Code() ;
	}
	public void reset() {
		pid = -1 ;
		stateid = -1 ;
		gencode.clearCode();
	}
	public void reset(ArrayList<Byte> pcount) {
		pid = -1 ;
		stateid = -1 ;
		gencode.clearCode();
		pcnt.clear();
		for (byte c : pcount)
			pcnt.add(c) ;
	}
	public void clearCode() {
		gencode.clearCode();
	}
	public GenerateCode clone() {
		GenerateCode code = new GenerateCode();
		code.pid = pid;	
		code.execid = execid ;
		code.pinstance = pinstance;
		code.stateid = stateid ;
		code.gencode = gencode.clone() ;
		code.pcnt.addAll(pcnt);
		return code ;
	}
}

