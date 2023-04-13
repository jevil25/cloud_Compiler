package compilers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.cloudbus.cloudsim.DatacenterBroker;

public class CppCompiler extends DatacenterBroker {
	
	public CppCompiler(String name) throws Exception {
		super(name);
	}
	
	public String compile(String code) throws Exception {
	    String sourceFile = "Main.cpp";
	    String executableFile = "Main.exe";

	    // Write the code to a file
	    try (PrintWriter out = new PrintWriter(sourceFile)) {
	        out.println(code);
	    }

	    // Compile the code using g++
	    ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\CodeBlocks\\MinGW\\bin\\g++", "-o", executableFile, sourceFile);
	    pb.redirectErrorStream(true);
	    Process p = pb.start();

	    // Wait for the process to complete
	    int exitCode = p.waitFor();

	    // Read the output and error streams from the process
	    String output = "";
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            output += line + "\n";
	            System.out.println(line);
	        }
	    } catch (IOException e) {
	        // error handling code
	        System.out.println(e);
	    }
	    try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
	        String errorLine;
	        while ((errorLine = errorReader.readLine()) != null) {
	            System.err.println(errorLine);
	        }
	    } catch (IOException e) {
	        // error handling code
	        System.out.println(e);
	    }

	    if (exitCode != 0) {
	        System.err.println("Compilation failed with exit code " + exitCode);
	        return output;
	    }

	    // Run the compiled executable and print its output
	    pb = new ProcessBuilder(executableFile);
	    pb.redirectErrorStream(true);
	    p = pb.start();
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            output += line + "\n";
	            System.out.println(line);
	        }
	    } catch (IOException e) {
	        // error handling code
	        System.out.println(e);
	    }
	    try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
	        String errorLine;
	        while ((errorLine = errorReader.readLine()) != null) {
	            System.err.println(errorLine);
	        }
	    } catch (IOException e) {
	        // error handling code
	        System.out.println(e);
	    }

	    // Delete the source file and executable file
	    new File(sourceFile).delete();
	    new File(executableFile).delete();

	    System.out.println("Compilation and execution successful!");

	    return output;
	}


}
