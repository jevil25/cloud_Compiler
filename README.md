# Cloud Compiler

This is a java cloud sim based program. It is used to compile and run the code. It has features like compile and run the code. It gives proper error messages and gives proper ouput. It suports multiple programming languages. It can also take live user input

It works similar to online compilers used in various web pages.

# Code Explanation:
1.Package and Imports:
The code imports required classes from the org.cloudbus.cloudsim package for cloud simulation. Additionally, it imports specific classes from custom packages compilers.Java and compilers.CppCompiler.

2.Class Declaration and Variables:
The Main class is declared with a few private static variables: results, a list of maps to store simulation results, and two helper methods (createVM and createCloudlet) to create VMs and Cloudlets.

3.createVM method:
The createVM method creates a list of virtual machines (VMs) based on the given parameters. It takes two arguments: userId and numberOfVm. For each VM, it sets parameters like image size, RAM, MIPS (processing power), bandwidth, number of processing elements (CPU cores), VMM name, and a Cloudlet scheduler.

4.createCloudlet method:
The createCloudlet method creates a list of cloudlets. A cloudlet represents a task that needs to be executed on a VM. The method takes two arguments: userId and numberOfCloudlet. For each cloudlet, it sets parameters like task length (in instructions), file size, output size, number of processing elements (PEs), and utilization models (models representing CPU, memory, and bandwidth utilization).

5.main method:
The main method is the entry point of the program. It performs the following steps:

Initializes the CloudSim package and displays introductory information about the simulation.

Asks the user to input the number of users (numUsers) who will participate in the simulation.

Enters a loop to run the simulation for each user.

Asks the user to input the number of datacenters (numberOfDatacenters).

Creates datacenters with specified hosts and processing elements using the createDatacenter method.

Creates a broker object (Java or CppCompiler) based on the user's choice.

Sets up VMs and Cloudlets using the createVM and createCloudlet methods, respectively.

Submits the VMs and Cloudlets to the broker for processing.

Before starting the simulation, asks the user to choose between Java and C++ for each user and then tries to compile and execute the respective code.

Starts the simulation using CloudSim.startSimulation().

After the simulation, collects and prints the results of the Cloudlets' execution, including total CPU time and average CPU time.

Stores the results in the results list for each user.

Once all users' simulations are completed, the program prints the total and average CPU times for each user in a tabular format.

6.createDatacenter method:
The createDatacenter method creates datacenters. It takes a name for the datacenter as an argument.

Creates a list of Host objects, where each Host represents a physical machine that can host one or more VMs.

Defines two types of processing element (Pe) lists: one for quad-core machines and the other for dual-core machines.

Creates Host objects with their respective attributes such as RAM, storage, and bandwidth, along with the created Pe lists.

Defines DatacenterCharacteristics for the datacenter, including architecture, operating system, VMM, and the list of hosts.

Creates a Datacenter object using the defined characteristics, a simple VM allocation policy, and an empty list of storage elements.

7.printResult method:
The printResult method takes a list of Cloudlet objects and a broker name as arguments.

Calculates the total CPU time and average CPU time for each user's cloudlets by summing up their actual CPU times.

Prints the broker name and the calculated total and average CPU times.

Stores the results in a map and adds the map to the results list.
