package Pendrive.Scheduling;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class SJFNProcess {
    int pid, burstTime, arrivalTime, waitingTime, turnaroundTime;

    public SJFNProcess(int pid, int burstTime, int arrivalTime) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
    }
}

public class SJFNonPreemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        SJFNProcess[] processes = new SJFNProcess[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time and burst time for process " + (i + 1) + ": ");
            int at = sc.nextInt();
            int bt = sc.nextInt();
            processes[i] = new SJFNProcess(i + 1, bt, at);
        }

        // Sort processes by arrival time, and then by burst time
        Arrays.sort(processes, new Comparator<SJFNProcess>() {
            @Override
            public int compare(SJFNProcess p1, SJFNProcess p2) {
                if (p1.arrivalTime != p2.arrivalTime) {
                    return Integer.compare(p1.arrivalTime, p2.arrivalTime);
                } else {
                    return Integer.compare(p1.burstTime, p2.burstTime);
                }
            }
        });

        int totalTime = 0, totalWT = 0, totalTAT = 0;

        for (SJFNProcess p : processes) {
            // If the CPU is idle, jump to the arrival time of the process
            if (totalTime < p.arrivalTime) {
                totalTime = p.arrivalTime;
            }

            p.waitingTime = totalTime - p.arrivalTime;
            totalTime += p.burstTime;
            p.turnaroundTime = p.waitingTime + p.burstTime;

            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
        }

        // Print Gantt Chart
        System.out.println("Gantt Chart: ");
        for (SJFNProcess p : processes) {
            System.out.print("P" + p.pid + " ");
        }
        System.out.println("\n");

        // Print process details
        System.out.println("Process\tArrival\tBurst\tWaiting\tTurnaround");
        for (SJFNProcess p : processes) {
            System.out.println("P" + p.pid + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" +
                    p.waitingTime + "\t" + p.turnaroundTime);
        }

        // Print average times
        System.out.println("Average Waiting Time: " + (totalWT / (float) n));
        System.out.println("Average Turnaround Time: " + (totalTAT / (float) n));

        sc.close();
    }
}
