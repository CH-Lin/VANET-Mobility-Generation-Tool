/*
Copyright (c) 2010 Che-Hung Lin

This file is part of VANET Mobility Generation Tool.

VANET Mobility Generation Tool is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or (at your option)
any later version.

VANET Mobility Generation Tool is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details.

You should have received a copy of the GNU General Public License along with
VANET Mobility Generation Tool. If not, see <https://www.gnu.org/licenses/>.
*/
package vanet.task.cmd;

import java.io.PrintWriter;

public class CommandProcessor {

	public CommandProcessor() {
	}

	public static boolean executeCommand(String command) throws Exception {
		return executeCommand(command, null);
	}

	public static boolean executeCommand(String command, PrintWriter logFile) throws Exception {
		boolean success = true;

		System.out.println("Processing command: " + command + "\n");

		// Prepare buffers for process output and error streams
		StringBuffer err = new StringBuffer();
		try {
			Process proc = Runtime.getRuntime().exec(command);

			StreamReaderThread outThread = new StreamReaderThread(proc.getInputStream(), System.out);

			StreamReaderThreadError errThread = new StreamReaderThreadError(proc.getErrorStream(), err);

			// Start both threads
			outThread.start();
			errThread.start();

			// Wait for process to end
			proc.waitFor();

			// Finish reading what is left in the buffers
			outThread.join();
			errThread.join();

			if (err.toString().trim().length() > 0 && err.toString().trim().indexOf("Error:") > 0) {
				System.out.println("Process error while executing command:\n" + err.toString());
				if (logFile != null)
					logFile.append("Process error while executing command:\n" + err.toString());
				success = false;
			} else {
				System.out.println("Command executed successfully.");
				if (logFile != null)
					logFile.append("Command executed successfully.");
				success = true;
			}
		} catch (Exception e) {
			System.out.println("Error executing " + command);
			if (logFile != null)
				logFile.append("Error executing " + command);
			success = false;

			e.printStackTrace();
			throw e;
		}
		if (logFile != null)
			logFile.flush();
		return success;
	}

}