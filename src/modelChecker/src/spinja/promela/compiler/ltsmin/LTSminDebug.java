package spinja.promela.compiler.ltsmin;

public class LTSminDebug {
	static final String TAB = "   ";
	
	public static enum MessageKind {
		DEBUG,
		NORMAL,
		ERROR,
		FATAL,
		WARNING
	}

	public int say_indent = 0;
	private boolean verbose;

	public LTSminDebug(boolean verbose) {
		this.verbose = verbose;
	}

	public void say(String s) {
		say(MessageKind.NORMAL, s);
	}

	public void say(MessageKind k, String s) {
		switch (k) {
		case DEBUG:
			if (!verbose) break;
		case NORMAL:
			for(int n=say_indent; n > 0; n--)
				System.out.print(TAB);
			System.out.println(s);
			break;
		case WARNING:
		case ERROR:
			for(int n=say_indent; n > 0; n--)
				System.err.print(TAB);
			System.err.println(s);
			break;
		case FATAL:
			for(int n=say_indent; n > 0; n--)
				System.err.print(TAB);
			System.err.println(s);
			System.exit(-100);
		default: throw new AssertionError("Unimplemented debug message kind: "+ k);
		}
	}
}