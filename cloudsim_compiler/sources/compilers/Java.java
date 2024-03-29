package compilers;

import org.cloudbus.cloudsim.DatacenterBroker;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Scanner;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class Java extends DatacenterBroker {

    public Java(String name) throws Exception {
        super(name);
    }

    public void compile(String code) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, MalformedURLException {
        // Use the Java Compiler API to compile the input code into bytecode
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        JavaFileObject sourceFile = new DynamicJavaSourceCodeObject("Main", code);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(sourceFile);
        compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits).call();
        try {
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Use reflection to load and execute the compiled bytecode
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("").toURI().toURL() });
        Class<?> loadedClass = null;
        try {
            loadedClass = classLoader.loadClass("Main");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Method mainMethod = loadedClass.getDeclaredMethod("main", String[].class);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Output:");
            mainMethod.invoke(null, new Object[] { new String[] {  } });
            scanner.close();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
