package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterCharacteristics;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.network.datacenter.AggregateSwitch;
import org.cloudbus.cloudsim.network.datacenter.NetworkConstants;
import org.cloudbus.cloudsim.network.datacenter.NetworkDatacenter;
import org.cloudbus.cloudsim.network.datacenter.NetworkDatacenterBroker;
import org.cloudbus.cloudsim.network.datacenter.NetworkLink;
import org.cloudbus.cloudsim.network.datacenter.NetworkOperatingSystem;
import org.cloudbus.cloudsim.network.datacenter.RootSwitch;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;

public class UserVM extends Vm {
    public UserVM(int id, int userId, double mips, int pesNumber, int ram, long bw, long size, String vmm, CloudletScheduler cloudletScheduler) {
        super(id, userId, mips, pesNumber, ram, bw, size, vmm, cloudletScheduler);
    }
    
    public String compile(String code) {
        // Compile the code and return the output
        // You can use any Java compiler library or tool to compile the code
        // For example:
        String output = "";
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StringWriter writer = new StringWriter();
            PrintWriter out = new PrintWriter(writer);
            JavaFileObject file = new JavaSourceFromString("UserClass", code);
            Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
            CompilationTask task = compiler.getTask(out, null, diagnostics, null, null, compilationUnits);
            boolean success = task.call();
            if (success) {
                output = "Compilation successful";
            } else {
                output = "Compilation failed\n";
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    output += diagnostic.getMessage(null) + "\n";
                }
            }
        } catch (Exception e) {
            output = "Compilation error: " + e.getMessage();
        }
        return output;
    }
    
    public String execute(String code) {
        // Execute the compiled code and return the output
        // You can use any Java runtime library or tool to execute the code
        // For example:
        String output = "";
        try {
            Class<?> clazz = Class.forName("UserClass");
            Method method = clazz.getMethod("main", String[].class);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outStream));
            method.invoke(null, (Object) null);
            output = new String(outStream.toByteArray());
        } catch (Exception e) {
            output = "Execution error: " + e.getMessage();
        }
        return output;
    }
}