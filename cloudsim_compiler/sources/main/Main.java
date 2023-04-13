package main;

import org.cloudbus.cloudsim.*;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import compilers.Java;
import compilers.CppCompiler;

import java.util.*;

public class Main {

    private static List<Map<String, String>> results = new ArrayList<>();
    
    private static List<Vm> createVM(int userId, int numberOfVm) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<Vm> list = new LinkedList<>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 1000;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        for(int i = 0; i< numberOfVm; i++){
            Vm vm = new Vm(i, userId, mips+(i*10), pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
            list.add(vm);
        }

        return list;
    }

    private static List<Cloudlet> createCloudlet(int userId, int numberOfCloudlet){
        // Creates a container to store Cloudlets
        LinkedList<Cloudlet> list = new LinkedList<>();

        //cloudlet parameters
        long length = 1000;
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        for(int i = 0; i < numberOfCloudlet; i++){
            Cloudlet cloudlet = new Cloudlet(i, (length + 2L * i * 10), pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            cloudlet.setUserId(userId);
            list.add(cloudlet);
        }

        return list;
    }

    public static void main(String[] args) {
        Log.printLine();
        Log.printLine("===================================== Load Balancer ==================================");
        Log.printLine("Title:        Serverless Compilation" +
                "\nAuthors:      Aaron Jevil Nazareth" +
                "\n              Aaron Francis Dsouza" );
        try {
            Calendar calendar = Calendar.getInstance();

            Scanner scanner = new Scanner(System.in);

            Log.printLine();
            Log.printLine("First step: Initialize the CloudSim package.");
            Log.printLine("Enter number of users:");
            int numUsers = scanner.nextInt();
            for (int i = 1; i <= numUsers ; i++) { 
            Log.printLine();
            Log.printLine("Second step: Create Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation.");
            Log.printLine("Enter number of datacenters:");
            int numberOfDatacenters = scanner.nextInt();

            Log.printLine();
            Log.printLine("Third step: Create Broker");

            Log.printLine("Enter number of vms:");
            int numberOfVm = scanner.nextInt();

            Log.printLine("Enter number of cloudlet");
            int numberOfCloudlet = scanner.nextInt();

            CloudSim.init(numUsers, calendar, true);


                for (int j = 0; j < numberOfDatacenters; j++) {
                    createDatacenter("Datacenter_" + j);
                }
                Java broker = new Java("JavaBroker");
                CppCompiler cbroker = new CppCompiler("CppBroker");
                assert broker != null;
                int brokerId = broker.getId();
                String brokerName = broker.getName();
                
                List<Vm> vmList = createVM(brokerId, numberOfVm);
                List<Cloudlet> cloudletList = createCloudlet(brokerId, numberOfCloudlet);
                
                broker.submitVmList(vmList);
                broker.submitCloudletList(cloudletList);

                Log.printLine("Broker: " + brokerName);
                Log.printLine("Create VMs");

                

                Log.printLine();
                Log.printLine("Create Cloudlets");

                Log.printLine("Sending them to broker...");

                Log.printLine();
                Log.printLine("Starts the simulation");

                CloudSim.startSimulation();
                    
                    Log.printLine("Enter number 1)Java 2)C++ for user "+(i+1));
                    int com = scanner.nextInt();   

                    try {
                        switch (com) {
                            case 1:
                               broker.compile("import java.util.Scanner;\r\n"
                                		+ "\r\n"
                                		+ "public class Main {\r\n"
                                		+ "    public static void main(String[] args) {\r\n"
                                		+ "        Scanner input = new Scanner(System.in);\r\n"
                                		+ "        System.out.print(\"Enter an integer: \");\r\n"
                                		+ "        int num = input.nextInt();\r\n"
                                		+ "        int result = num * 2;\r\n"
                                		+ "        System.out.println(\"Result: \" + result);\r\n"
                                		+ "    }\r\n"
                                		+ "}"); 
                                
                                break;
                            case 2:
                            	cbroker.compile("#include <iostream>\r\n"
                            			+ "using namespace std;\r\n"
                            			+ "int main() {\r\n"
                            			+ "    int n = 11;\r\n"
                            			+ "    cout << n << endl;\r\n"
                            			+ "    return 0;\r\n"
                            			+ "}");
                            	break;
                            default:
                                Log.printLine("Please, select from [1-"+(numUsers-1)+"] only:");
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    

                Log.printLine();
                Log.printLine("Results when simulation is over");

                List<Cloudlet> cloudletReceivedList = broker.getCloudletReceivedList();

                CloudSim.stopSimulation();

                printResult(cloudletReceivedList, brokerName);

                Log.printLine();
                Log.printLine("Simulation Complete");
            

            String leftAlignFormat = "| %-39s | %-15s | %-15s |%n";

            System.out.format("+-----------------------------------------+-----------------+-----------------+%n");
            System.out.format("| Broker                                  | Total CPU Time  | Average CPU Time|%n");
            System.out.format("+-----------------------------------------+-----------------+-----------------+%n");
            for (Map<String, String> result: results) {
                System.out.format(leftAlignFormat, result.get("broker"), result.get("total_cpu_time"), result.get("average_cpu_time"));
            }
            System.out.format("+-----------------------------------------+-----------------+-----------------+%n");
            }
            scanner.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
    }

    private static Datacenter createDatacenter(String name){

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more
        //    Machines
        List<Host> hostList = new ArrayList<>();

        // 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
        //    create a list to store these PEs before creating
        //    a Machine.
        List<Pe> peList1 = new ArrayList<>();

        int mips = 10000;

        // 3. Create PEs and add these into the list.
        //for a quad-core machine, a list of 4 PEs is required:
        peList1.add(new Pe(0, new PeProvisionerSimple(mips + 500))); // need to store Pe id and MIPS Rating
        peList1.add(new Pe(1, new PeProvisionerSimple(mips + 1000)));
        peList1.add(new Pe(2, new PeProvisionerSimple(mips + 1500)));
        peList1.add(new Pe(3, new PeProvisionerSimple(mips + 700)));

        //Another list, for a dual-core machine
        List<Pe> peList2 = new ArrayList<>();

        peList2.add(new Pe(0, new PeProvisionerSimple(mips + 700)));
        peList2.add(new Pe(1, new PeProvisionerSimple(mips + 900)));

        //4. Create Hosts with its id and list of PEs and add them to the list of machines
        int hostId=0;
        int ram = 1002048; //host memory (MB)
        long storage = 1000000; //host storage
        int bw = 10000;

        hostList.add(
                new Host(
                        hostId,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList1,
                        new VmSchedulerTimeShared(peList1)
                )
        ); // This is our first machine

        hostId++;

        hostList.add(
                new Host(
                        hostId,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList2,
                        new VmSchedulerTimeShared(peList2)
                )
        ); // Second machine

        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;		// the cost of using memory in this resource
        double costPerStorage = 0.1;	// the cost of using storage in this resource
        double costPerBw = 0.1;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<>();	//we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


        // 6. Finally, we need to create a PowerDatacenter String.
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    /**
     * Prints the Cloudlet Strings
     * @param list  list of Cloudlets
     */
    private static void printResult(List<Cloudlet> list, String broker) {

        Log.printLine();
        Log.printLine();
        Log.printLine("========================================== OUTPUT ==========================================");
        Log.printLine("Broker: " + broker);
        
        double time = 0;

        for (Cloudlet value : list) {
            if (value.getCloudletStatus() == Cloudlet.SUCCESS) {
                time += value.getActualCPUTime();
            }
        }

        double avgTime = time/list.toArray().length;
        Log.printLine("Total CPU Time: " + time);
        Log.printLine("Average CPU Time: " + avgTime);

        Map<String, String> result = new HashMap<>();
        result.put("broker", broker);
        result.put("total_cpu_time", String.format("%.5f", time));
        result.put("average_cpu_time", String.format("%.5f", avgTime));

        results.add(result);
    }

}