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

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import spinja.concurrent.model.ConcurrentModel;
import spinja.concurrent.search.PartialOrderReduction;
import spinja.exceptions.ValidationException;
import spinja.options.BooleanOption;
import spinja.options.MultiStringOption;
import spinja.options.NumberOption;
import spinja.options.OptionParser;
import spinja.options.SchedulerOption;
import spinja.promela.model.NeverClaimModel;
import spinja.promela.model.PromelaModel;
import spinja.promela.model.PromelaTransition;
import spinja.search.Algoritm;
import spinja.search.TransitionCalculator;
import spinja.store.AtomicHashTable;
import spinja.store.BitstateHashStore;
import spinja.store.HashCompactStore;
import spinja.store.HashTable;
import spinja.store.ProbingHashTable;
import spinja.store.StateStore;
import spinja.util.Log;
import sspinja.scheduler.promela.model.SchedulerPromelaModel;
import sspinja.scheduler.promela.model.SchedulerSporadicModel;
import sspinja.scheduler.search.SchedulerSimulation;
import sspinja.ui.MainUI;


public class RunSchedulerSimulation extends Thread {
	private StateStore store = null;
	
	private SchedulerSimulation algo = null;
	public static int checkOption = 0 ;
	
	private double startTime;
	
	
	public RunSchedulerSimulation(boolean isScheduler){
		Config.isSchedulerSearch = isScheduler;
	}
	
	public RunSchedulerSimulation() {
		// TODO Auto-generated constructor stub
	}

	private final MultiStringOption impl = new MultiStringOption(
		'D',
		"use a specific implementation option",
		new String[] {
				"BFS", "NOREDUCE", "BITSTATE", "HC", "ARRAY", "GTRAIL", "GUSER", "GRANDOM"
		},
		new String[] {
				"uses a Breadth First Search algorithm",
				"disables the partial order reduction algoritm",
				"uses bitstate hashing for storing states\n" +
				 	"(this method does not guarantee a complete search)",
				"uses hash compaction for storing states \n" +
				    "(this method does not guarantee a complete search)",
				"uses a hash table that uses array lists \n" +
				    "instead of probing",
				"uses the trail-file to guide the search", 
				"uses user input to guide the search",
				"uses a randomizer to guide the search",
		});

	private final BooleanOption checkAccept = new BooleanOption('a',
		"checks for acceptance cycles");

	private final BooleanOption ignoreAssert = new BooleanOption('A',
		"assert statements will be ignored");

	private final BooleanOption errorExceedDepth = new BooleanOption('b',
		"exceeding the depth limit is considered an error");

	private final NumberOption nrErrors = new NumberOption('c',
		"stops the verification after N errors \n" +
			"(when set to 0 it does not stop for any error)", 1,
		0, Integer.MAX_VALUE);

	private final NumberOption concurrent = new NumberOption('C',
		"starts several concurrent threads (*experimental*)", 1, 1, 1024);

	private final BooleanOption ignoreInvalidEnd = new BooleanOption('E',
		"ignore invalid end states (deadlocks)");

	private final NumberOption nrBits = new NumberOption('k',
		"sets N bits per state when using bitstate hashing", 3, 1, 1024);

	private final NumberOption maxDepth = new NumberOption('m', 
		"sets the maximum search depth",
		100000, 1, Integer.MAX_VALUE);

	public static int testDepth = -1 ;
	
	private final BooleanOption ignoreNever = new BooleanOption('N',
		"ignores any never claim (if present)");

	private final BooleanOption onlyVersion = new BooleanOption('v', 
		"prints the version number and exits");

	private final NumberOption hashEntries = new NumberOption('w',
		"sets the number of entries in the hash table to 2^N", 21, 3, 30);

	public final SchedulerOption scheduleropt = new SchedulerOption('S', "scheduler option");
	
	private byte done = 0;

	private TransitionCalculator<ConcurrentModel<PromelaTransition>, 
									PromelaTransition> nextTransition = null;

	public boolean getIgnoreAssert() {
		return ignoreAssert.isSet();
	}

	public boolean getIgnoreNever() {
		return ignoreNever.isSet();
	}

	public boolean getIgnoreInvalidEnd() {
		return ignoreInvalidEnd.isSet();
	}

	public void parseArguments(final String[] args, final String name) {
		final String  shortd  = 
			"SSpinJa Model Checker version " + Version.VERSION + " (" + Version.DATE + ")\n" +
			"with SpinJa 0.9.3 (C) University of Twente, Formal Methods and Tools group";
		final String  longd   = 
			"SpinJa Model Checker is a (generated) Java program which verifies\n" +
			"whether the original Promela model (+ never claim) was correct." ;
		final OptionParser p = 
			new OptionParser("java spinja." + name + "Model", 
			                  shortd, longd, false);

		p.addOption(impl);
		p.addOption(checkAccept);
		p.addOption(ignoreAssert);
		p.addOption(errorExceedDepth);
		p.addOption(concurrent);
		p.addOption(nrErrors);
		p.addOption(ignoreInvalidEnd);
		p.addOption(nrBits);
		p.addOption(maxDepth);
		p.addOption(ignoreNever);
		p.addOption(onlyVersion);
		p.addOption(hashEntries);
		p.addOption(scheduleropt);
		p.parse(args);
	}

	private boolean realMem() {
		final Runtime r = Runtime.getRuntime();
		r.runFinalization();
		for (int i = 0; i < 16; i++) {
			r.gc();
		}
		final double memory = (r.totalMemory() - r.freeMemory()) / (1024.0 * 1024.0);
		System.out.printf("%7g real memory usage (Mbyte)\n", memory);
		Log.out.printf("%7g real memory usage (Mbyte)\n", memory);
		return true;
	}

	//@Override
	public void run() {
		if (done == 0) {
			System.out.println("Warning: Search not completed!");
			Log.out.println("Warning: Search not completed!");
		} else if (done == -1) {
			return;
		}
		
		algo.printSummary();
		algo.printSummary(Log.out);

		double memory = algo.getBytes() / (1024.0 * 1024.0);
		System.out.printf("%7g memory usage (Mbyte)\n", memory);
		Log.out.printf("%7g memory usage (Mbyte)\n", memory);
		
		System.out.println();
		Log.out.println();
			
		final double seconds = (System.currentTimeMillis() - startTime) / 1e3;
		System.out.printf("sspinja: elapsed time %.2f seconds\n", seconds);
		Log.out.printf("sspinja: elapsed time %.2f seconds\n", seconds);

		System.out.printf("sspinja: rate %8d states/second\n", (int) (algo.getNrStates() / seconds));
		Log.out.printf("sspinja: rate %8d states/second\n", (int) (algo.getNrStates() / seconds));

		realMem();
		Log.flushOutputTest();
	}

	public SchedulerSimulation search(final Class<? extends PromelaModel> clazz, MainUI mainUI) {
		if (onlyVersion.isSet()) {
			done = -1;
			return null;
		}

		ConcurrentModel<PromelaTransition> model = null;

		try {
			model = clazz.getConstructor().newInstance();
		} catch (final IllegalArgumentException e) {
			System.err.println("The process model should have at least a contructor with one boolean as a parameter.");
			e.printStackTrace();
			return null;
		} catch (final SecurityException e) {
			System.err.println("The process model should have at least an accesable contructor with one boolean as a parameter.");
			e.printStackTrace();
			return null;
		} catch (final InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (final InvocationTargetException e) {
			System.err.println(e.getCause().getMessage());
			return null;
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}

		if (model instanceof SchedulerPromelaModel) {
			Config.isSchedulerSearch = true ;
			try {
				model = SchedulerSporadicModel.createSporadic((SchedulerPromelaModel) model);
				if (model instanceof SchedulerSporadicModel) {
				} else {
					SchedulerPromelaModel.panmodel = (SchedulerPanModel) model ;					
				}
			} catch (final ValidationException e) {
				System.out.println("Warning: Could not create sporadic model: " + e.getMessage());
			}
		}
		
	
		boolean guided = impl.isSet("GUSER") || impl.isSet("GTRAIL") || impl.isSet("GRANDOM");
		boolean reduced = false;
		
		if (reduced) {
			System.out.println("        + Partial Order Reduction");
			nextTransition = new PartialOrderReduction<ConcurrentModel<PromelaTransition>, PromelaTransition>();
		} else {
			nextTransition = new TransitionCalculator<ConcurrentModel<PromelaTransition>, PromelaTransition>();
		}
		System.out.println();
		System.out.println("Full statespace search for:");
		System.out.print("        never claim             ");
		System.out.println(model instanceof NeverClaimModel ? "+" : "-");
		System.out.print("        assertion violations    ");
		System.out.println(getIgnoreAssert() ? "-" : "+");
		System.out.print("        cycle checks            ");
		System.out.println(checkAccept.isSet() ? "+ Acceptance cycles" : "-");
		System.out.print("        invalid end states      ");
		System.out.println(ignoreInvalidEnd.isSet() ? "-" : "+");
		System.out.println();
	
		int maxDepth = testDepth <= 0 ? this.maxDepth.getValue() : testDepth; //set the depth for testcases
						
		if (!guided) {
			store = new AtomicHashTable(hashEntries.getValue()); //test for heuristic search
		}else {
			if (concurrent.getValue() > 1) {
				store = new AtomicHashTable(hashEntries.getValue());
			} else if (impl.isSet("BITSTATE")) {
				store = new BitstateHashStore(hashEntries.getValue(), nrBits.getValue());
			} else if (impl.isSet("HC")) {
				store = new HashCompactStore(hashEntries.getValue(), nrBits.getValue());
			} else if (impl.isSet("ARRAY")) {
				store = new HashTable(hashEntries.getValue());
			} else {
				store = new ProbingHashTable(hashEntries.getValue());
			}
		}
		
		
		algo = new SchedulerSimulation<ConcurrentModel<PromelaTransition>, PromelaTransition>(
				model, store, maxDepth
				, errorExceedDepth.isSet(), !ignoreInvalidEnd.isSet(),
				nrErrors.getValue(), nextTransition);

		System.out.println("Max depth:" + maxDepth);
		Log.out.println("Max depth:" + maxDepth);
		startTime = System.currentTimeMillis();

		return algo;
	}
	
	public void execute(MainUI mainUI) {
		try {
			algo.execute(); 
			done = 1;
		} catch (final OutOfMemoryError er) {
			algo.freeMemory();
			System.out.println("sspinja error: Maximum memory reached. Please make more memory available for the virtual machine.");
			Log.out.println("sspinja error: Maximum memory reached. Please make more memory available for the virtual machine.");
		} finally {
			if (concurrent.getValue() > 1) {
				AtomicHashTable table = (AtomicHashTable) store;
				table.shutdown();
				try {
					table.awaitTermination(5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	}
	
	public void setDepth(int depth){
		/*
		 * depth for test case
		 * depth == -1 -> non using test
		 * depth > 0 -> test depth
		 */
		testDepth = depth  ;
	}
}
