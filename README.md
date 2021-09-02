# **SSpinJa**
## 1. Description
SSpinJa is a tool which aims to verify and analyze the behaviors of the system run under different scheduling policies using model checking techniques. We propose a method using a domain-specific language (DSL) for describing the behaviors of the scheduler. This language provides a high-level support for the succinct specification of different scheduling policies. The necessary artifacts are automatically generated from the corresponding specification of the scheduling policy for subsequent analysis of the behaviors of the system, which are described in a modeling language (i.e. Promela). We implement a search algorithm that uses the information above (realized from the specification of the scheduling policy) to explore the system state space and verify the behaviors of the system.
A short demo video of SSpinJa is available at Youtube: https://youtu.be/fHDgrzzsHA0

## 2. Files
### a) To verify and analyze (command line version)
1. _\verificationanalysis\grammar.pdf_: The language grammar of the DSL, the semantics of the core language is shown in [1]
1. _\verificationanalysis\sspinja.jar_: The model checker and the analyzer
1. _\verificationanalysis\src.7z_: source code
1. _\verificationanalysis\schedulerDSL.jar_: The language converter
1. _\verificationanalysis\script.7z_: An example of the running script (run on Windows)
1. _\verificationanalysis\examplea.7z_: Example for analysis
1. _\verificationanalysis\examplev.7z_: Example for verification



### b) To generate tests (testing scheduling policies) (command line version)
1. _\testgeneration\grammar.pdf_: Updated grammar of the DSL for the test generation
1. _\testgeneration\sspinjaN.jar_: Test generator
1. _\testgeneration\schedulerDSL.jar_: The language converter (updated)
1. _\testgeneration\examplegen.7z_: Example for test generation



## 3. Verification and analysis process (command line version)
### a) The input of the tool contains three files, including:
1. A process model which is written in modeling language (this language is based on Promela with some extensions for the scheduling policy);
1. A process description (in DSL) which defines the attributes of the process;
1. A scheduler description (in DSL) which specifies the scheduling policy.


### b) Perform the verifying and analyzing task as follows:
1. Convert the process description and the scheduler description into scheduler information;
1. Compile the process model (for analyzing the system behaviors with CTL/RTCTL formula, please use "-a" parameter);
1. Execute the verifier/analyzer.

**See the explaination of the results on: results.pdf**

## 4. Deployed plugin for Eclipse IDE and Model checker (Java library) (GUI version - updated!)
1. Install the plugin for Eclipse (download the directory: deployed\EclipsePlugin, then in Eclipse, go to Help > Install New Software. ... browse to the downloaded directory to install it).
1. Download the Java library (jar file) for the model checker (in deployed\ModelChecker).
1. Open Eclipse, create a new Java project then add the library above (step 2).
1. Create a Promela file (for the behaviors of the processes) then click compile (see the demonstration at https://youtu.be/fHDgrzzsHA0).
1. Add the specification of the scheduling policy in the DSL (process description and the scheduling policy).
1. Do the verification (and/or the simulation as needed).

## 5. Source code for the DSL and the model checker (GUI version - updated!)
See the modelChecker and schedulingDSL.parent directories in src directory.

----------------------------------------
[1] Nhat-Hoa Tran, Yuki Chiba, Toshiaki Aoki:
Model Checking in the Presence of Schedulers Using a Domain-Specific Language for Scheduling Policies. IEICE Transactions 102-D(7): 1280-1295 (2019)
