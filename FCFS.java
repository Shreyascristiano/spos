package Pendrive.Scheduling;

import java.util.*;


class Process {
    int pid, burstTime, arrivalTime, waitingTime, turnaroundTime;

    // Constructor for the Process class
    public Process(int pid, int burstTime, int arrivalTime) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
    }
}

public class FCFS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        Process[] processes = new Process[n];

        // Input arrival and burst time for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time and burst time for process " + (i + 1) + ": ");
            int at = sc.nextInt();
            int bt = sc.nextInt();
            processes[i] = new Process(i + 1, bt, at); // Creating new Process object for each process
        }

        // Sort processes based on arrival time (ascending order)
        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        // Calculate waiting time and turnaround time
        int totalTime = 0, totalWT = 0, totalTAT = 0;

        for (Process p : processes) {
            // Waiting time for the process
            p.waitingTime = totalTime - p.arrivalTime;

            // Update total time (time when this process finishes)
            totalTime += p.burstTime;

            // Turnaround time is waiting time + burst time
            p.turnaroundTime = p.waitingTime + p.burstTime;

            // Update total waiting time and total turnaround time
            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
        }

        // Print Gantt Chart (just process IDs in order)
        System.out.println("Gantt Chart: ");
        for (Process p : processes) {
            System.out.print("P" + p.pid + " ");
        }
        System.out.println();

        // Print process details: Arrival Time, Burst Time, Waiting Time, Turnaround Time
        System.out.println("Process\tArrival\tBurst\tWaiting\tTurnaround");
        for (Process p : processes) {
            System.out.println("P" + p.pid + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" +
                    p.waitingTime + "\t" + p.turnaroundTime);
        }

        // Calculate and print the average waiting time and average turnaround time
        System.out.println("Average Waiting Time: " + (totalWT / (float) n));
        System.out.println("Average Turnaround Time: " + (totalTAT / (float) n));

        sc.close();
    }
}
