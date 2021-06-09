package sspinja.scheduler.search;

import java.util.ArrayList;

import sspinja.scheduling.SchedulerObject;

public class InstanceStack {
	private byte pCount = 0;
	private ArrayList<Byte> pInstance = new ArrayList<Byte>() ;
	private int top = -1 ;
	
	private ArrayList<ArrayList<Byte>> pInstanceList = new ArrayList<ArrayList<Byte>>() ;
	private ArrayList<Byte> pCountList = new ArrayList<Byte>() ;
	
	
	public void init() {
		while (SchedulerObject.processInScheduler[pCount])  {
			pInstance.add(pCount++);
		}
		top = -1;
		push() ;
	}
	
	public void reset() {
		while (top > 0) {
			pInstanceList.remove(top);
			pCountList.remove(top--);
		}	
		pCount = pCountList.get(top);
		pInstance = (ArrayList<Byte>) pInstanceList.get(top).clone();
	}
	public void push() {		
		pInstanceList.add((ArrayList<Byte>) pInstance.clone());
		pCountList.add(pCount);
		top++ ;
	}
	
//	public void update() {
//		pInstanceList.set(top,(ArrayList<Byte>) pInstance.clone());
//		pCountList.set(top,pCount);
//	}
	
	public void updateNewID(int processID) {
		if (processID < 0) return ;		
		while (pInstance.size() < processID + 1) pInstance.add((byte) -1) ;				
		pInstance.set(processID, (byte) (pCount++));
//		push();
	}
	
	public void updateEndID(int processID) {
		if (processID < 0) return ;
		pInstance.set(processID, (byte) (-1));
//		push();
	}
	
	public int getInstance(int processID) {
		if (processID > pInstance.size())
			return -1;
		return pInstance.get(processID) ;
	}
	public ArrayList<Byte> pop() {
		if (top >= 0){
			pCountList.remove(top);
			return pInstanceList.remove(top--) ;						
		}
		return null ;
	}	
	
	public void print() {
		for (int i= top; i >= 0 ; i--) {
			System.out.println(i + ") " + pInstanceList.get(i) );
		}
	}
	public void undo() {
		pCount = pCountList.get(top);
		pInstance = (ArrayList<Byte>) pInstanceList.get(top).clone();
	}
	public boolean doneInstance() {
		if (top < 0) {
			return false ;
		} else {
			pCountList.remove(top);
			pInstanceList.remove(top--);
			if (top < 0) return false ;
			undo() ;
			return true ;
		}
	}
}

