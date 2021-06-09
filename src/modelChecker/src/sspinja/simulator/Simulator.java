package sspinja.simulator;
import sspinja.ui.MainUI;
import sspinja.verifier.Verifier;

public class Simulator {
	public static void main(String[] args) {
		Verifier.isVerified = true;
		MainUI.main(args);
	}
}	
