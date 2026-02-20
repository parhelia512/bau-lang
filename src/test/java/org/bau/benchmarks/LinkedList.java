// https://lemire.me/blog/2026/02/15/how-bad-can-python-stop-the-world-pauses-get/

package org.bau.benchmarks;

public class LinkedList {

    static class Node {
        int value;
        Node next;
        Node(int value) {
            this.value = value;
            this.next = null;
        }
        void addNext(Node node) {
            this.next = node;
        }
    }

    public static Node createLinkedList(int limit) {
        // create a linked list of length 'limit'
        Node head = new Node(0);
        Node current = head;
        for (int i = 1; i < limit; i++) {
            Node newNode = new Node(i);
            current.addNext(newNode);
            current = newNode;
        }
        return head;
    }

    public static void main(String[] args) {
        System.out.println("Starting loading linked list...");
        Node x = createLinkedList(50_000_000);
        System.out.println("Initial batch completed, starting stress test...");
        long start = System.nanoTime();
        long worstCase = 0;
        for (int i = 0; i < 1_000_000; i++) {
            long batchStart = System.nanoTime();
            long elapsedSinceStart = batchStart - start;
            if (elapsedSinceStart > worstCase) {
                worstCase = elapsedSinceStart;
            }
            start = batchStart;
            createLinkedList(1000);
        }
        System.out.printf("Max delay between batch start and overall start: %d nanos%n", worstCase);
        System.out.println("dummy value: " + x.toString().length());
    }

}
