// https://github.com/lemire/Code-used-on-Daniel-Lemire-s-blog/blob/master/2026/02/13/stress_gc.go

package main

import (
    "fmt"
    "time"
    "runtime"
)

type Node struct {
    value int
    next  *Node
}

func createLinkedList(num int) *Node {
    head := &Node{value: 0}
    current := head
    for i := 1; i < num; i++ {
        current.next = &Node{value: i}
        current = current.next
    }
    return head
}

func main() {
    runtime.GOMAXPROCS(1)
    fmt.Println("Starting GC stress test...")
    maxdiff := time.Duration(0)

    objects1 := createLinkedList(50_000_000)
    start := time.Now()
    dummy := 0

    // Create a lot of cyclic garbage
    for batch := 0; batch < 1_000_000; batch++ {
        batchStart := time.Now()
        // compute the delay between batchStart and start
        diff := batchStart.Sub(start)
        if diff > maxdiff {
            maxdiff = diff
        }
        start = batchStart
        objects := createLinkedList(1000)
        dummy += objects.value
    }
    fmt.Printf("Max delay between batch start and overall start: %v\n", maxdiff)
    fmt.Printf("GC stress test completed. Value: %d %d\n", objects1.value, dummy)
}