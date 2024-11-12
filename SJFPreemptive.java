package Pendrive.Scheduling;

import java.util.Scanner;

public class SJFPreemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input the number of processes
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();
        int[] bt = new int[n]; // burst time of each process
        int[] at = new int[n]; // arrival time of each process
        int[] rt = new int[n]; // remaining time of each process (for preemption)
        int[] wt = new int[n]; // waiting time for each process
        int[] tat = new int[n]; // turnaround time for each process
        boolean[] completed = new boolean[n]; // keeps track of completed processes

        // Input burst time and arrival time of each process
        System.out.println("Enter Arrival Time and Burst Time of the processes:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + (i + 1) + ": ");
            at[i] = sc.nextInt();
            bt[i] = sc.nextInt();
            rt[i] = bt[i]; // remaining time initially equals burst time
        }

        int completedProcesses = 0, currentTime = 0, shortest = 0;
        boolean found;
        StringBuilder ganttChart = new StringBuilder();

        // While there are still incomplete processes
        while (completedProcesses < n) {
            found = false;

            // Check for the process with the shortest remaining time that has arrived
            for (int i = 0; i < n; i++) {
                if (!completed[i] && at[i] <= currentTime && (found == false || rt[i] < rt[shortest])) {
                    shortest = i;
                    found = true;
                }
            }

            if (found) {
                // Execute the selected process
                rt[shortest]--;
                ganttChart.append("P").append(shortest + 1).append(" ");

                currentTime++;

                // If the process is completed
                if (rt[shortest] == 0) {
                    completed[shortest] = true;
                    completedProcesses++;
                    tat[shortest] = currentTime - at[shortest]; // Turnaround time
                    wt[shortest] = tat[shortest] - bt[shortest]; // Waiting time
                }
            } else {
                // No process is ready, increment the time and mark as idle
                currentTime++;
                ganttChart.append("idle ");
            }
        }

        // Output the Gantt Chart
        System.out.println("Gantt Chart: " + ganttChart);

        // Calculate average waiting time and turnaround time
        float avgWT = 0, avgTAT = 0;
        System.out.println("Process\tArrival\tBurst\tWaiting\tTurnaround");

        // Print process details
        for (int i = 0; i < n; i++) {
            avgWT += wt[i];
            avgTAT += tat[i];
            System.out.println("P" + (i + 1) + "\t" + at[i] + "\t" + bt[i] + "\t" + wt[i] + "\t" + tat[i]);
        }

        // Calculate averages
        avgWT /= n;
        avgTAT /= n;

        // Output the averages
        System.out.println("Average Waiting Time: " + avgWT);
        System.out.println("Average Turnaround Time: " + avgTAT);

        sc.close();
    }
}
