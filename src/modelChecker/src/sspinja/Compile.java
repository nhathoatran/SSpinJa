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

package sspinja;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Compile {	

	public static void main(final String[] args) {
		Set<String> set = new HashSet<String>(Arrays.asList(args)) ;
		boolean isAnalysis = set.remove("-A") || set.remove("-a");	

		if (isAnalysis) {
			try {
				String[] newargs = set.toArray(new String[set.size()]) ;								
				//CompileSchedulerWithCTL.main(newargs);
				CompileSchedulerWithCTLGen.main(newargs);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				CompileScheduler.main(args);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}