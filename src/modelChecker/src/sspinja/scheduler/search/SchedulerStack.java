// Copyright 2010, University of Twente, Formal Methods and Tools group
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package sspinja.scheduler.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import spinja.exceptions.SpinJaException;
import spinja.model.Transition;
import spinja.search.SearchableStack;
import spinja.store.hash.HashAlgorithm;
//import spinja.util.Gen;
import sspinja.scheduling.SchedulerObject;

public class SchedulerStack implements SearchableStack  {
	private final HashAlgorithm hash;

	public static int top;
	private final int size;

	public final Transition[] lastTransition;
	public final int [] lastschopt ;
	public final int [] lastcore ;
		
	private final byte[][] encoded;
	public final int[] identifiers;	
	private final int[] hashTable;
	private long bytes;
	
	public final byte[][] runningSetEncoded ;
	public int[] runningSetIdentifiers ;
	private final int[] runningSetHashTable;
	private long runningSetBytes ;
	
	
	private final int hashMask;	
	
	
	public int[] runInstance ; //running process id (instance)
	public int[] runid ; //running process id
	public static byte[] pcount ; //determines the instance of processes
	
	
	
//	public byte[][] pcnt ;
//	public GenerateCode[] code ;
//	public GenCode[] code ;
	
	
	
	
	public int getLastSchOption() {
		if (top < 0) return 0 ;
		return lastschopt[top];
	}
	public int getLastCore() {
//		if (top < 0) return 0 ;
		return lastcore[top];
	}
	public SchedulerStack(int size) {
		top = -1;
		this.size = size;
		lastTransition = new Transition[size];
		
		lastschopt = new int[size] ;
		lastcore = new int[size] ;
		
		encoded = new byte[size][];
		identifiers = new int[size];
		
		runningSetEncoded = new byte[size][] ;
		runningSetIdentifiers = new int[size];

		runid = new int[size] ;
		runInstance = new int[size] ;
		pcount = new byte[size] ;		
//		pcnt = new byte[size][128] ;
//		code = new GenCode[size] ;
//		code = new GenerateCode[size] ;
		
		int hashSize = 0;
		while (size > 0) {
			size >>= 1;
			hashSize++;
		}
		hashSize = 1 << hashSize; 
		
		hashTable = new int[hashSize];		
		for(int i= 0; i < hashSize; i++) {
			hashTable[i] = Integer.MIN_VALUE;
		}
		
		
		runningSetHashTable = new int[hashSize];
		for(int i= 0; i < hashSize; i++) {
			runningSetHashTable[i] = Integer.MIN_VALUE;
		}
		
		hashMask = hashSize - 1;

		//initial size
		bytes = 128 + 12 * size + 4 * hashSize;	
		runningSetBytes = 128 + 12 * size + 4 * hashSize;
		
		hash = HashAlgorithm.getDefaultAlgorithm();
	}

	public void clearStack() {
		while (top >= 0) {
			pop();
		}
	}
	
	public int getTopIdentifer() {
		if (top >=0)
			return identifiers[top] ;
		else
			return -1;
	}
	
	public boolean containsState(final byte[] state) {
		for (int i = top; i >= 0; i--) {
			if (Arrays.equals(encoded[i], state)) {
				return true;
			}
		}
		return false;		
	}

	public boolean containsState(final byte[] state, int identifier) {
		int index = identifier & hashMask;
		int incr = identifier | 1;
		int value = 0;
		while((value = hashTable[index]) >= 0) {
			if(Arrays.equals(encoded[value], state)) {
				return true;
			}
			index = (index + incr) & hashMask;
		}
		return false;
	}

	public long getBytes() {
		double memory = bytes / (1024.0 * 1024.0);
		System.out.printf("%7g process memory usage (Mbyte)\n", memory);		
		
		double memory1 = runningSetBytes / (1024.0 * 1024.0);
		System.out.printf("%7g running set memory usage (Mbyte)\n", memory1);
		
		return bytes + runningSetBytes;
	}

	public Transition getLastTransition() {
		if (top < 0) return null ;
		return lastTransition[top];
	}

	public int getSize() {
		return top + 1;
	}
	
	public boolean isFull() {
		return top == size ;
	}
	
	public void setTop(byte[] bytes,int identifier) {
		encoded[top] = bytes;
		identifiers[top] = identifier ;
		lastTransition[top] = null ;
	}
	
	public byte[] getTop() {
		return encoded[top];
	}
	
	public int top() {
		return top ;
	}
	public byte[] getRunningSetTop(){
		return runningSetEncoded[top] ;
	}

	public byte[] popRunningSet() {
		if (top == -1) {			
			throw new IndexOutOfBoundsException();
		}

		bytes -= 19 + encoded[top].length >> 3 << 3;
		byte[] res = encoded[top];
		encoded[top] = null;
						
		int index = runningSetIdentifiers[top] & hashMask;
		int incr = runningSetIdentifiers[top] | 1;
		while(hashTable[index] != top) {
			index = (index + incr) & hashMask;
		}
		hashTable[index] = Integer.MIN_VALUE;			
		if (runningSetEncoded[top] != null) {
			removeLastRunningSet();			
		}
		
		top--;		
		return res;
	}
	
	public int getCore(final int depth){
		return lastcore[depth];
	}
	public Transition getTransition(final int depth) {
		return lastTransition[depth];
	}

	public byte[] getState(final int depth) {
		return encoded[depth];
	}

	public byte[] pop() {
		if (top == -1) {			
			throw new IndexOutOfBoundsException();
		}

		bytes -= 19 + encoded[top].length >> 3 << 3;
		byte[] res = encoded[top];
		encoded[top] = null;
		
		lastTransition[top] = null;
				
		int index = identifiers[top] & hashMask;
		int incr = identifiers[top] | 1;
		while(hashTable[index] != top) {
			index = (index + incr) & hashMask;
		}
		hashTable[index] = Integer.MIN_VALUE;			
		if (runningSetEncoded[top] != null) {
			if (runningSetIdentifiers[top] >= 0) {
				removeLastRunningSet();			
			}
		}
		
		top--;		
		return res;
	}
	
	public boolean pushRunningSet(final byte[] runningSet, int runningSetIdentifier) {
		if (top == size) {
			return false;
		}		
		
		this.runningSetEncoded[top] = runningSet;
		this.runningSetIdentifiers[top] = runningSetIdentifier;
		if (runningSetIdentifier > 0){	
			runningSetBytes += 19 + runningSet.length >> 3 << 3;
			
			int index = runningSetIdentifier & hashMask;
			int incr = runningSetIdentifier | 1;
			while(runningSetHashTable[index] >= 0) {
				index = (index + incr) & hashMask;
			}
			runningSetHashTable[index] = top;
		} 		
			
		return true;
	}
		
	public void removeLastRunningSet(){		
		runningSetBytes -= 19 + runningSetEncoded[top].length >> 3 << 3;
		runningSetEncoded[top] = null;
						
		int runningSetIndex = runningSetIdentifiers[top] & hashMask;
		int runningSetIncr = runningSetIdentifiers[top] | 1;
		while(runningSetHashTable[runningSetIndex] != top) {
			runningSetIndex = (runningSetIndex + runningSetIncr) & hashMask;
		}
		runningSetHashTable[runningSetIndex] = Integer.MIN_VALUE;
	}
		
	public ArrayList<Integer> getCyclicID(int start_duplicateID) {
		ArrayList<Integer> resultList = new ArrayList<Integer>() ;
		boolean startCopy = false ;
		for (int i = 0; i<= top; i ++) {			
			if (identifiers[i] == start_duplicateID) {
				startCopy = true ;				
			}
				
			if (startCopy)				 
				resultList.add(identifiers[i]);
		}				
		return resultList ;
	}
	
	public void resetLastTransition(){
		lastTransition[top] = null ;
	}
	
	public void replaceLastState(byte[] state, int identifier) {
		pop() ;
		push(state, identifier) ;
	}
	
	public void print(){
		System.out.println("------- ------------------------ --");			
		if (top >=0) {			
			for (int i = top; i >= 0; i--) {
				//System.out.printf("Pid/runIns/Count: %2d/%2d/%2d opt: %2d %s %12d %s %8d", runid[i], runInstance[i], pcount[i], lastschopt[i], "System state:", identifiers[i], "RunningSet state:", runningSetIdentifiers[i]);
//				System.out.printf("Pid/runIns/Count: %2d/%2d/%2d opt: %2d", runid[i], runInstance[i], pcount[i], lastschopt[i]);
//				System.out.println("System state:" + identifiers[i] + ", RunningSet state: " + runningSetIdentifiers[i]);
//				if (lastTransition[i] != null)
//					System.out.println(i + ") core: " + lastcore[i] + ", sch: " + lastschopt[i] + " - SysState:" + identifiers[i]  + " - " + lastTransition[i].toString() +  " - RunSet:" + runningSetIdentifiers[i]);	
				if (lastTransition[i] != null)	
					System.out.println(i + ") - SysState:" + identifiers[i] + " - " + lastTransition[i].toString() );	
				else
					System.out.println(i + ") - SysState:" + identifiers[i] + " - null");
//				else
//					System.out.println(i + ") core: " + lastcore[i] + ", sch: " + lastschopt[i] + " - SysState:" + identifiers[i] + " - <none> - RunSet:" + runningSetIdentifiers[i]);
					
//				if (runid[i] >= 0)
//					System.out.printf("%3d %3d %s \n",runid[i], pcnt[i][runid[i]], code[i]);
//				else
//					System.out.printf(" - <no process> - <no code> \n" );
				
			}
			
			System.out.println("Top = " + top + ", size = " + getSize());
		}
		else
			System.out.println("Empty");
		System.out.println("------- ------------------------ --");
	}

		
//	public String exportTestCase() { 
//		if (top==0) 
//			return "";
//		String result = "" ;
//		HashMap<String, String> processCode = new HashMap<String,String>() ;
//		String processName = "";
//		ArrayList<String> listProcess = new ArrayList<String>() ;	
//		int redundantstep = 0 ;
//		for (int i = 0 ; i < top ; i++) {
//			if (this.runid[i] < 0) continue ;
//			if (this.pcnt[i][this.runid[i]] == 0) {
//				redundantstep ++ ;
//				continue ;
//			}
//		}
//		for (int i = 0 ; i < top ; i++) {
//			if (this.runid[i] < 0) continue ;
//			if (this.pcnt[i][this.runid[i]] == 0) {			
//				continue ;
//			}
//			//processName = this.pid[i] + "" + this.pcnt[i][this.pid[i]];
//			processName = "" + this.pcnt[i][this.runid[i]];
//			if (!listProcess.contains(processName)) 
//				listProcess.add(processName);
//			String addcode = this.code[i].code.replace("getTotalStep()", (top + 1 - redundantstep) + "") ;
//			if (processCode.containsKey(processName)) {
//				String code = processCode.get(processName) +  addcode;
//				processCode.put(processName, code);
//			} else {
//				processCode.put(processName, addcode);
//			}
//		}
//		
//		
//		result += "/* auto gen */ \n";
//		ArrayList<String> processList = new ArrayList<String>() ;
//		int id = 0 ;
//		while (this.pcnt[top][id] > 0) {
//			//processList.add(id + "" + this.pcnt[top][id]) ;
//			processList.add("" + this.pcnt[top][id]) ;
//			id++ ;
//		}		
//		for (String procName : processList) {
//			if (processCode.keySet().contains(procName)) { 
//				result += "void *process_" + procName + "(); \n" ;
//			} else {
//				result += "void *process_" + procName + "(){}\n" ;
////				result += "\t pthread_mutex_lock(&lock) ; \n" ;
////				result += "\t printf(\"process_" + procName + " run & end \\n\"); \n" ;
////				result += "\t thcnt-- ; \n" ;
////				result += "\t pthread_mutex_unlock(&lock) ; \n" ;
////				result += "}\n" ;				
//			}
//			result += "pthread_t thread_" + procName + "; \n" ;
//		}
//		//Ignore steps done by no process
//		for (String procName : processCode.keySet()) {
//			if (!processList.contains(procName) && !procName.equals("00")) { 
//				result += "void *process_" + procName + "(); \n" ;
//				result += "pthread_t thread_" + procName + "; \n" ;
//			} 
//		}
//		result += "\n" ;
//
////		result += "void joint_thread(){ \n" ;
////		for (String procName : processList) {
////			result += "\t pthread_join(thread_process_" + procName + ", NULL); \n" ;
////		}
////		for (String procName : processCode.keySet()) {
////			if (!processList.contains(procName)) { 
////				result += "\t pthread_join(thread_process_" + procName + ", NULL); \n" ;
////			}
////		}
////		result += "}\n\n" ;
//		
//		int thcnt = processList.size();		
//		for (String procName : processCode.keySet()) {
//			if (!processList.contains(procName)) { 
//				thcnt++ ;
//			}
//		}		
//		
//		result += "void init_thread(){ \n" ;
//		//result += "\t thcnt = " + thcnt + "; \n" ;
//		for (String procName : SchedulerObject.initprocesslist) {
//			result += "\t create_thread(" + "thread_" + procName +", (void*) process_" + procName + "); \n" ;
//		}
//		result += "}\n\n" ;
//				
//		
//		result += "/* thread code */ \n";
//		for (String procName : processCode.keySet()) {
//			result += "void *process_" + procName + "() { \n" ;
//			//result += "\t pthread_mutex_lock(&lockstart) ; \n" ;			
//			//result += "\t pthread_mutex_unlock(&lockstart); \n\n" ;
//			//result += "\t printf(\"thread " + procName + " run \\n\") ; \n" ;
//			result += processCode.get(procName) ;
//			
////			result += "\t pthread_mutex_lock(&lock);\n" ;
////			result += "\t thcnt-- ; \n" ;
////			result += "\t pthread_mutex_unlock(&lock);\n" ;
//			result += "}\n\n" ;
//		}
//		return result ; 
//	}	
	
	
//	public String exportTestCase2() {   
//		if (top==0) 
//			return "";
//		String actionList = "" ;
//		//String processList = "" ;
//		
//		//int cnt = 0 ;
//		//String actlist = "" ; 
//		for (int i = 0 ; i <= top ; i++) {
//			if (this.runid[i] < 0) continue ;
////			if (this.pcnt[i][this.pid[i]] == 0) {
////				continue ;						
////			} else 
//			//cnt ++ ;
//			if (this.code[i].code.isEmpty()) {
//				//actionList += "SKIP,";
//				//actlist += "0 " ;
//			} else {
//				actionList +=  this.code[i].code;
////				switch (this.code[i].code.trim()) {
////					case "NEW" :
////						actlist += "1 "; break ;
////					case "END" :
////						actlist += "2 "; break ;					
////				}
//			}
//			//processList += this.pcnt[i][this.pid[i]] + ";";
//		}
//			
//		return actionList; // + "FINISH"; // + cnt + "\n" + actlist + "3"; // + processList ;
//	}
	

	
	
	
	
	
	
	
	
	
	
	
	public ArrayList<Transition> exportTrans() {
		ArrayList<Transition> trans = new ArrayList<Transition>() ;
		for (int i = 0; i <= top ; i++) {		
			trans.add(lastTransition[i]) ;
		}

		return trans ;
	}

	
	//---> move to SchedulerCTLGenSearch
	//	public void pushPcount(int pcnt) {
//		pcount[top] = (byte) pcnt ;		
//	}
//	
//	public static byte getPmax() {
//		if (top > 0)
//			return pcount[top-1] ;
//		else
//			return 0 ;
//	}

	
	//	public void pushID(int pid, int runinstance, byte[] pcnt) {
//		this.runid[top] = pid ;	
//		this.runInstance[top] = runinstance ;
//		this.pcnt[top] = pcnt ;
//	}
//	public void pushID(int pid, byte[] pcnt) {
//		this.runid[top] = pid ;	
//		this.pcnt[top] = pcnt ;
//	}
//	public GenCode getGenCode() {
//		return this.code[top] ;
//	}
//	public void updateGenCode(String code) {
//		this.code[top].code = code ;
//	}
//	public GenCode pushGenCode(String addcode, String part) {
//		if (addcode == null) return null;
//		if (this.code[top] == null) this.code[top] = new GenCode() ;
//		String code = addcode.replace("getStep()", top+1+"") ;
//		switch (part) {
//			case "clock": 
//				this.code[top].setClock(code); break ;
//			case "select_process": 
//				this.code[top].setSelect(code); break;		
//			case "new_process" : 
//				this.code[top].setNew(code); break;
//			case "preTake" : 
//				this.code[top].setPre(code); break;
//			case "postTake" : 
//				this.code[top].setPost(code); break;
//			case "Take" :
//				this.code[top].setTake(code); break;
//		}	
//		Gen.out.clear();
//		return this.code[top] ;
//	}
//	public void clearGenCode() {
//		if (top >= 0)
//			if (this.code[top] != null)
//				this.code[top].clearCode();
//	}

	
	
	public void takeTransition(final Transition transition) throws SpinJaException {
		if (transition != null) {
			lastTransition[top] = transition;
			transition.take();
		}
	}
	
	public void pushSchOpt(int opt){		
		this.lastschopt[top] = opt ;
	}
	public void pushSchTrans(Transition trans){
		this.lastTransition[top] = trans ;
	}
	public void pushCore(int core){
		this.lastcore[top] = core ;
	}
	public boolean push(byte[] state, int identifier) {
		if (top == size - 1) {
			return false;
		}
		top++;
		this.lastschopt[top] = 0 ;//-1 ;
		this.lastcore[top] = 0 ; //-1 ;
		
		this.encoded[top] = state;
		this.identifiers[top] = identifier;
		bytes += 19 + state.length >> 3 << 3;
				
		int index = identifier & hashMask;
		int incr = identifier | 1;
		while(hashTable[index] >= 0) {
			index = (index + incr) & hashMask;
		}
		hashTable[index] = top;
		return true;
	}	
}
