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

package spinja.promela.compiler;



/**
 * 
 * @author Alfons Laarman
 */
public class ProcInstance extends Proctype {
	
	private int instance;

	public ProcInstance(Proctype p, int instance, int id) {
		super(p.getSpecification(), id, p.getNrActive(), p.getName());
		this.instance = instance;
	}

	
	public String getName() {
		if (-1 == instance) return super.getName(); 
		return super.getName() +"_"+ instance;
	}

	public String getTypeName() {
		return super.getName();
	}

	public boolean equals(Object o) {
		if (o == null || !(o instanceof ProcInstance))
			return false;
		ProcInstance p = (ProcInstance)o;
		return p.getName().equals(name) && instance == p.instance;
	}

    /**
     * @return the instance
     */
    public int getInstance() {
        return instance;
    }

    /**
     * @param instance the instance to set
     */
    public void setInstance(int instance) {
        this.instance = instance;
    }
}
