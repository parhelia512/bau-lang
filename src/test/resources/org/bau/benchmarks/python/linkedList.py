# https://lemire.me/blog/2026/02/15/how-bad-can-python-stop-the-world-pauses-get/

import time

class Node:
    def __init__(self, value):
        self.value = value
        self.next = None
    def add_next(self, node):
        self.next = node

def create_linked_list(limit):
    """ create a linked list of length 'limit' """
    head = Node(0)
    current = head
    for i in range(1, limit):
        new_node = Node(i)
        current.add_next(new_node)
        current = new_node
    return head

def main():
    print("Starting loading linked list...")
    x = create_linked_list(50_000_000)
    print("Initial batch completed, starting stress test...")
    start = time.time()
    worse_case = 0.0
    for i in range(1_000_000):
        batch_start = time.time()
        if batch_start - start > worse_case:
            worse_case = batch_start - start
        start = batch_start
        create_linked_list(1000)
    print(f"Max delay between batch start and overall start: {worse_case * 1000:.4f} milliseconds", flush=True)

main()