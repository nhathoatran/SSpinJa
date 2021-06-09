package spinja.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;


public class DummyStdOut extends PrintStream{

		public DummyStdOut()  {
			super(new ByteArrayOutputStream());
			// TODO Auto-generated constructor stub
			
		}

		@Override
		public PrintStream append(char c) {
			return this;
		}

		@Override
		public PrintStream append(CharSequence csq, int start, int end) {
			return this;
		}

		@Override
		public PrintStream append(CharSequence csq) {
			return this;
		}

		@Override
		public boolean checkError() {
			return false;
		}

		@Override
		protected void clearError() {
			
		}

		@Override
		public void close() {
		
		}

		@Override
		public void flush() {
		
		}

		@Override
		public PrintStream format(Locale l, String format, Object... args) {
			return this;
		}

		@Override
		public PrintStream format(String format, Object... args) {
			return this;
		}

		@Override
		public void print(boolean b) {
			
		}

		@Override
		public void print(char c) {
		}

		@Override
		public void print(char[] s) {
		}

		@Override
		public void print(double d) {
		}

		@Override
		public void print(float f) {
		}

		@Override
		public void print(int i) {
		}

		@Override
		public void print(long l) {
		}

		@Override
		public void print(Object obj) {
		}

		@Override
		public void print(String s) {
		}

		@Override
		public PrintStream printf(Locale l, String format, Object... args) {
			return this;	}

		@Override
		public PrintStream printf(String format, Object... args) {
			return this;	}

		@Override
		public void println() {
		}

		@Override
		public void println(boolean x) {
		}

		@Override
		public void println(char x) {
		}

		@Override
		public void println(char[] x) {
		}

		@Override
		public void println(double x) {
		}

		@Override
		public void println(float x) {
		}

		@Override
		public void println(int x) {
		}

		@Override
		public void println(long x) {
		}

		@Override
		public void println(Object x) {
		}

		@Override
		public void println(String x) {
		}

		@Override
		protected void setError() {
		}

		@Override
		public void write(byte[] buf, int off, int len) {
		}

		@Override
		public void write(int b) {
		}
		
		private static final DummyStdOut  inst= new DummyStdOut();
		private static final PrintStream sysout = System.out;
		public static void enableStdOut(){
			System.setOut(sysout);
		}
		public static void disableStdOut(){
			System.setOut(inst);
		}
	}