package org.jmodelica.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;

import javax.naming.OperationNotSupportedException;

/**
 * \brief Base class for logging messages from the tree.
 * 
 * This implementation discards all messages.
 */
public class ModelicaLogger {
    /**
     * \brief Log <code>message</code> on log level <code>level</code>.
     * 
     * This implementation discards all messages.
     * 
     * Override this method to provide log facilities.
     */
    public void log(Level level, String message) {}
    
    /**
     * \brief Get the current log level.
     * 
     * This implementation returns null.
     * 
     * Override this method to provide log facilities.
     */
    public Level getLevel() { return null; }
    
    /**
     * \brief Set the current log level.
     * 
     * This implementation does nothing.
     * 
     * Override this method to provide log facilities.
     */
    public void setLevel(Level lv) { }

    public void debug(String message)   { log(Level.FINE,    message); }
    public void info(String message)    { log(Level.INFO,    message); }
    public void warning(String message) { log(Level.WARNING, message); }
    public void error(String message)   { log(Level.SEVERE,  message); }
    
    /**
     * Log stack trace of exception on debug log level.
     */
    public void stackTrace(Throwable e) {
		StringWriter str = new StringWriter();
		PrintWriter print = new PrintWriter(str);
		e.printStackTrace(print);
		debug(str.toString());
    }
    
    /**
     * Creates an output stream that writes to the log.
     * 
     * Note that while this class tries to log entire lines separately, it
     * only handles the line break representations "\n", "\r" and "\r\n", 
     * and assumes that the character encoding used encodes both '\n' and '\r' 
     * like ASCII & UTF-8 does.
     * 
     * @param level  the log level to write to
     */
    public OutputStream logStream(Level level) {
    	return new LogOutputStream(level);
    }
    
    public OutputStream debugStream()   { return logStream(Level.FINE);    }
    public OutputStream infoStream()    { return logStream(Level.INFO);    }
    public OutputStream warningStream() { return logStream(Level.WARNING); }
    public OutputStream errorStream()   { return logStream(Level.SEVERE);  }
    
    private class LogOutputStream extends OutputStream {
    	
    	private Level level;
    	private byte[] buf;
    	private int n;
    	private boolean lastR;
    	
    	private static final byte R = (byte) '\r';
    	private static final byte N = (byte) '\n';

		public LogOutputStream(Level level) {
			this.level = level;
			buf = new byte[2048];
		}

		public void write(int b) throws IOException {
			buf[n++] = (byte) b;
			if (lastR || b == N)
				logBuffer();
			lastR = (b == R);
		}

		public void write(byte[] b, int off, int len) throws IOException {
			while (n + len > buf.length) {
				int part = buf.length - n;
				System.arraycopy(b, off, buf, n, part);
				n = buf.length;
				logBuffer();
				off += part;
				len -= part;
			}
			System.arraycopy(b, off, buf, n, len);
			n += len;
			logBuffer();
		}

		public void close() throws IOException {
			log(level, new String(buf, 0, n));
		}
		
		private void logBuffer() {
			int start = 0;
			int nlpos = 0;
			
			while (nlpos >= 0) {
				nlpos = -1;
				for (int i = start; nlpos < 0 && i < n; i++)
					if (buf[i] == N || buf[i] == R)
						nlpos = i;
				if (nlpos == n - 1 && buf[nlpos] == R) {
					lastR = true;
					nlpos = -1;
				} else if (nlpos > 0) {
					log(level, new String(buf, start, nlpos - start));
					start = nlpos + 1;
					if (start < n && buf[start] == N && buf[start - 1] == R)
						start++;
				} else if (start == 0 && n == buf.length) {
					log(level, new String(buf, 0, n));
					start = n;
				}
			}
			
			n -= start;
			if (n > 0 && start > 0)
				System.arraycopy(buf, start, buf, 0, n);
		}
    	
    }
}