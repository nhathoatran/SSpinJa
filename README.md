# SSpinJa
1. Description
SSpinJa is a tool which aims to verify and analyze the behaviors of the system run under different scheduling policies using model checking techniques. We propose a method using a domain-specific language (DSL) for describing the behaviors of the scheduler. This language provides a high-level support for the succinct specification of different scheduling policies. The necessary artifacts are automatically generated from the corresponding specification of the scheduling policy for subsequent analysis of the behaviors of the system, which are described in a modeling language (i.e. Promela). We implement a search algorithm that uses the information above (realized from the specification of the scheduling policy) to explore the system state space and verify the behaviors of the system.

2. Files description
a) To verify and analyze 
---verification_analysis\grammar.pdf: The language grammar of the DSL, the semantics of the core language is shown in [1]
---verification_analysis\sspinja.jar: The model checker and the analyzer
---verification_analysis\src.7z: source code
---verification_analysis\schedulerDSL.jar: The language converter
---verification_analysis\script.7z: An example of the running script (run on Windows)
---verification_analysis\example_a.7z: Example for analysis
---verification_analysis\example_v.7z: Example for verification

b) To generate tests (testing scheduling policies)
---test_generation\grammar.pdf: Updated grammar of the DSL for the test generation
---test_generation\sspinjaN.jar: Test generator
---test_generation\schedulerDSL.jar: The language converter (updated)
---test_generation\example_gen.7z: Example for test generation

3. Verification and analysis process
a) The input of the tool contains three files, including:
----- A process model which is written in modeling language (this language is based on Promela with some extensions for the scheduling policy);
----- A process description (in DSL) which defines the attributes of the process;
----- A scheduler description (in DSL) which specifies the scheduling policy.

b) Perform the verifying and analyzing task as follows:
----- Convert the process description and the scheduler description into scheduler information;
----- Compile the process model (for analyzing the system behaviors with CTL/RTCTL formula, please use "-a" parameter);
----- Execute the verifier/analyzer.

See the explaination of the results on results.pdf


----------------------------------------
[1] Nhat-Hoa Tran, Yuki Chiba, Toshiaki Aoki:
Model Checking in the Presence of Schedulers Using a Domain-Specific Language for Scheduling Policies. IEICE Transactions 102-D(7): 1280-1295 (2019)
