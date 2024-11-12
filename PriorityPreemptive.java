package Pendrive.Scheduling;

import java.util.Scanner;

public class PriorityPreemptive {
    static class Process {
        int id, burst, priority, arrival, waitingTime, turnaroundTime;
        int remainingBurst;

        public Process(int id, int burst, int priority, int arrival) {
            this.id = id;
            this.burst = burst;
            this.priority = priority;
            this.arrival = arrival;
            this.remainingBurst = burst;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        Process[] processes = new Process[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time, burst time, and priority for process " + (i + 1) + ": ");
            int at = sc.nextInt();
            int bt = sc.nextInt();
            int priority = sc.nextInt();

            processes[i] = new Process(i + 1, bt, priority, at);
        }

        int completed = 0, time = 0;
        StringBuilder ganttChart = new StringBuilder();

        while (completed < n) {
            int idx = -1;
            int highestPriority = Integer.MAX_VALUE;

            // Find process with the highest priority (preemptive)
            for (int i = 0; i < n; i++) {
                if (processes[i].arrival <= time && processes[i].remainingBurst > 0 && processes[i].priority < highestPriority) {
                    highestPriority = processes[i].priority;
                    idx = i;
                }
            }

            if (idx != -1) {
                // Execute the process
                processes[idx].remainingBurst--;
                ganttChart.append("P").append(processes[idx].id).append(" ");

                // If the process is completed
                if (processes[idx].remainingBurst == 0) {
                    completed++;
                    processes[idx].turnaroundTime = time + 1 - processes[idx].arrival;
                    processes[idx].waitingTime = processes[idx].turnaroundTime - processes[idx].burst;
                }
            } else {
                // If no process is ready, add "idle" to the Gantt chart
                ganttChart.append("idle ");
            }

            // Move time forward
            time++;
        }

        // Print Gantt Chart
        System.out.println("Gantt Chart: " + ganttChart.toString());

        // Calculate and print average waiting time and turnaround time
        int totalWT = 0, totalTAT = 0;
        System.out.println("Process\tArrival\tBurst\tPriority\tWaiting\tTurnaround");
        for (Process p : processes) {
            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
            System.out.println("P" + p.id + "\t" + p.arrival + "\t" + p.burst + "\t" + p.priority + "\t\t" + p.waitingTime + "\t" + p.turnaroundTime);
        }

        System.out.println("Average Waiting Time: " + (totalWT / (float) n));
        System.out.println("Average Turnaround Time: " + (totalTAT / (float) n));

        sc.close();
    }
}
