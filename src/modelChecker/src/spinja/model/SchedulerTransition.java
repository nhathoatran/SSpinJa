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

package spinja.model;

import spinja.exceptions.ValidationException;

public class SchedulerTransition extends Transition{
	int transCode = 0 ;
	public SchedulerTransition(int trans){
		transCode = trans ;
	}
	public String toString() {
		switch (transCode) {
			case 0: return "Clock" ;
			case 1: return "Select process";
			case 2: return "Switch process" ;
			case 3: return "Switch core" ;
			case 4: return "Select core" ;
		}		
		return "" ;
	}
	public int getId() {return 0;}
	public void take() throws ValidationException{}
	public void undo(){}
	public String getText(){
		return toString();
	}
}
