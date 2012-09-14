package org.jmodelica.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.regex.Pattern;

public class GeneratedFilePatcher {

	private Patch[] patches = new Patch[] {
		new Patch("ASTNode.java", new Change[] {
			new AddLine("    SymbolValueFixer.fix(node);", true, "\\s*public [^ ]* clone().*", "\\s*return .*;")
		})
	};
	
	public static void main(String[] args) throws IOException {
		File dir = new File(args[0]);
		new GeneratedFilePatcher().patchAll(dir);
	}
	
	private void patchAll(File dir) throws IOException {
		for (Patch p : patches)
			p.apply(dir);
	}

	public abstract static class Change {

		private Pattern[] patterns;
		private int cur;
		
		protected Change(String[] patterns) {
			this.patterns = new Pattern[patterns.length];
			for (int i = 0; i < patterns.length; i++)
				this.patterns[i] = Pattern.compile(patterns[i]);
			cur = 0;
		}

		public String processLine(String line, PrintStream out) {
			between(out);
			if (patterns[cur].matcher(line).matches()) {
				cur++;
				if (cur >= patterns.length) {
					line = alter(line, out);
					cur = 0;
				}
			}
			return line;
		}

		public void processEOF(PrintStream out) {
			between(out);
		}

		protected void between(PrintStream out) {}
		protected String alter(String line, PrintStream out) {
			return line;
		}

	}

	public static class AddLine extends Change {

		private String insert;
		private boolean before;
		private boolean found;

		public AddLine(String insert, boolean before, String... patterns) {
			super(addToLast(patterns, insert));
			this.insert = insert;
			this.before = before;
			found = false;
		}

		private static String[] addToLast(String[] patterns, String insert) {
			patterns[patterns.length - 1] = "(" + patterns[patterns.length - 1] + "|" + Pattern.quote(insert) + ")";
			return patterns;
		}

		protected void between(PrintStream out) {
			if (found) {
				out.println(insert);
				found = false;
			}
		}

		protected String alter(String line, PrintStream out) {
			if (!line.equals(insert)) {
				if (before)
					out.println(insert);
				else 
					found = true;
			}
			return line;
		}

	}

	public static class Patch {

		private String fileName;
		private Change[] changes;

		public Patch(String fileName, Change[] changes) {
			this.fileName = fileName;
			this.changes = changes;
		}

		public void apply(File dir) throws IOException {
			File org = new File(dir, fileName);
			File temp = new File(org.getPath() + ".temp");
			BufferedReader in = new BufferedReader(new FileReader(org));
			PrintStream out = new PrintStream(new FileOutputStream(temp));
			apply(in, out);
			in.close();
			out.close();
			org.delete();
			temp.renameTo(org);
			System.out.println("Patched " + org);
		}

		private void apply(BufferedReader in, PrintStream out) throws IOException {
			boolean print = true;
			String line;
			while ((line = in.readLine()) != null) {
				for (Change ch : changes)
					line = ch.processLine(line, out);
				out.println(line);
			}
			for (Change ch : changes)
				ch.processEOF(out);
		}

	}
	
}