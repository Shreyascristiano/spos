package Pendrive.Scheduling;

import java.util.Scanner;

public class RoundRobinNonPreemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] burstTime = new int[n];
        int[] remainingTime = new int[n];
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];

        // Input the time quantum
        System.out.print("Enter time quantum: ");
        int quantum = sc.nextInt();

        // Input burst times for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            burstTime[i] = sc.nextInt();
            remainingTime[i] = burstTime[i]; // Initially, remaining time is equal to burst time
        }

        int time = 0; // The current time (time passed since the start)
        int completed = 0; // To keep track of number of completed processes
        String ganttChart = "";

        // Simulating the Round-Robin scheduling
        while (completed < n) {
            boolean processExecuted = false;

            for (int i = 0; i < n; i++) {
                // Only process if there is remaining time for the process
                if (remainingTime[i] > 0) {
                    // If remaining time is greater than quantum, execute for quantum time
                    if (remainingTime[i] > quantum) {
                        time += quantum;
                        remainingTime[i] -= quantum;
                        ganttChart += "P" + (i + 1) + " ";
                    } else {
                        // If remaining time is less than or equal to quantum, process finishes
                        time += remainingTime[i];
                        waitingTime[i] = time - burstTime[i]; // Waiting time calculation
                        turnaroundTime[i] = time; // Turnaround time calculation
                        remainingTime[i] = 0; // Process is completed
                        ganttChart += "P" + (i + 1) + " ";
                        completed++; // Increment completed process count
                    }
                    processExecuted = true;
                }
            }

            // If no process is executed in a round, idle time is added
            if (!processExecuted) {
                ganttChart += "idle ";
                time++; // Increment time for idle
            }
        }

        // Print the Gantt chart
        System.out.println("Gantt Chart: " + ganttChart);

        // Calculate and print the average waiting time and turnaround time
        int totalWT = 0, totalTAT = 0;
        System.out.println("Process\tBurst\tWaiting\tTurnaround");
        for (int i = 0; i < n; i++) {
            totalWT += waitingTime[i];
            totalTAT += turnaroundTime[i];
            System.out.println("P" + (i + 1) + "\t" + burstTime[i] + "\t" + waitingTime[i] + "\t" +
                    turnaroundTime[i]);
        }

        // Calculate and print average waiting time and turnaround time
        System.out.println("Average Waiting Time: " + (totalWT / (float) n));
        System.out.println("Average Turnaround Time: " + (totalTAT / (float) n));

        sc.close();
    }
}
