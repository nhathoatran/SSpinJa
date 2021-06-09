package scheduling.generator;

import org.eclipse.xtext.xbase.lib.Inline;

public class ArrayLiterals2 {
	@Inline("new int[$1][$2]")
	public static int[][] intArray(int outerSize, int innerSize) {
		throw new UnsupportedOperationException();
	}
}
