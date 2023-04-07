package compilers;

import org.cloudbus.cloudsim.DatacenterBroker;
import java.io.*;
import java.util.Arrays;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class Java  extends DatacenterBroker{

	public Java(String name) throws Exception {
		super(name);
	}
		
	public String compile(String code) {
	    // Set up in-memory file manager
	    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	    StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
	    JavaFileObject fileObject = new InMemoryJavaFileObject("Main", code);
	    Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(fileObject);

	    // Set up compiler task and output stream
	    StringWriter writer = new StringWriter();
	    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
	    CompilationTask task = compiler.getTask(writer, fileManager, diagnostics, null, null, compilationUnits);

	    // Execute compilation and capture output
	    boolean success = task.call();
	    String output = writer.toString();

	    // Clean up file manager and writer
	    try {
	        fileManager.close();
	        writer.close();
	    } catch (Exception e) {
	        // Handle exception
	    }

	    // Return compilation output
	    if (success) {
	        return "Compilation succeeded.\n" + output;
	    } else {
	        return "Compilation failed.\n" + output;
	    }
	}
    
}
