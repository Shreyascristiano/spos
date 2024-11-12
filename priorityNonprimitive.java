package Pendrive.Scheduling;

import java.util.*;

class PriorityProcess {
    int pid, burstTime, priority, waitingTime, turnaroundTime;

    public PriorityProcess(int pid, int burstTime, int priority) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.priority = priority;
    }
}

public class priorityNonprimitive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        PriorityProcess[] processes = new PriorityProcess[n];

        // Input burst time and priority for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter burst time and priority for process " + (i + 1) + ": ");
            int bt = sc.nextInt();
            int priority = sc.nextInt();
            processes[i] = new PriorityProcess(i + 1, bt, priority);
        }

        // Sort processes by priority (non-preemptive, lower priority value means higher priority)
        Arrays.sort(processes, Comparator.comparingInt(p -> p.priority));

        int totalTime = 0, totalWT = 0, totalTAT = 0;

        // Calculate waiting time and turnaround time for each process
        for (PriorityProcess p : processes) {
            p.waitingTime = totalTime;
            totalTime += p.burstTime;
            p.turnaroundTime = p.waitingTime + p.burstTime;
            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
        }

        // Print Gantt Chart
        System.out.println("Gantt Chart: ");
        for (PriorityProcess p : processes) {
            System.out.print("P" + p.pid + " ");
        }
        System.out.println("\n");

        // Print process details
        System.out.println("Process\tBurst\tPriority\tWaiting\tTurnaround");
        for (PriorityProcess p : processes) {
            System.out.println("P" + p.pid + "\t" + p.burstTime + "\t" + p.priority + "\t\t" + p.waitingTime + "\t" + p.turnaroundTime);
        }

        // Calculate and print average waiting time and average turnaround time
        System.out.println("Average Waiting Time: " + (totalWT / (float) n));
        System.out.println("Average Turnaround Time: " + (totalTAT / (float) n));

        sc.close();
    }
}
