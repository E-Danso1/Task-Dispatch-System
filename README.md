# ConcurQueue – High-Performance Job Dispatcher System

#Overview

ConcurQueue is a Java 21 + Spring Boot based multithreaded job dispatch system that simulates a real-world backend task processing engine using producer-consumer architecture, priority queues, and thread pools.

#  Features
- Multithreaded producers generating tasks
- Priority-based task scheduling
- Worker thread pool using ExecutorService
- Retry mechanism (max 3 retries)
- Graceful shutdown handling
- Real-time monitoring system
- Task status tracking using ConcurrentHashMap
- JSON export of system state
- Race condition demonstration and fix (AtomicInteger)

# Architecture
- Producers → generate tasks
- PriorityBlockingQueue → task buffer
- Worker Pool → processes tasks concurrently
- Status Tracker → tracks task lifecycle
- Retry Handler → handles failures
- Monitor → system observability
- JSON Exporter → audit logs

# Tech Stack
- Java 21
- Spring Boot
- ExecutorService (Thread Pool)
- PriorityBlockingQueue
- ConcurrentHashMap
- AtomicInteger
- SLF4J Logging
- Jackson (JSON Export)

# Key Concepts Demonstrated
- Producer-Consumer Pattern
- Thread Safety
- Race Conditions & Fixes
- Deadlock Avoidance
- Retry Mechanism
- Graceful Shutdown
- Concurrency Control

## Prerequisites

Before running ConcurQueue, make sure you have:

| Tool | Version | Check Command |
|------|---------|---------------|
| Java JDK | 21 or higher | `java -version` |
| Maven | 3.8 or higher | `mvn -version` |
| Git | Any recent | `git --version` |

> **Note:** An IDE like IntelliJ IDEA or VS Code with the Java Extension Pack
> is strongly recommended for navigating and understanding the code.

## Concurrency Concepts Demonstrated
### 1. PriorityBlockingQueue
The shared data structure between all producers and workers.
- **Blocking**: workers wait (don't spin) when queue is empty
- **Priority**: highest priority tasks always dequeued first
- **Thread-safe**: multiple producers/workers access it concurrently

### 2. ExecutorService Fixed Thread Pool
Manages the 4 worker threads efficiently:
- Threads created once, reused for all tasks (no per-task thread creation overhead)
- Idle threads wait for work without consuming CPU
- `awaitTermination()` ensures clean shutdown

### 3. ConcurrentHashMap
Tracks every task's status without locks:
- Read and write from any thread simultaneously
- Internal bucket-level locking (far more concurrent than `synchronized HashMap`)
- `getOrDefault()` is atomic — no null check race conditions

### 4. AtomicInteger / AtomicBoolean
Lock-free thread-safe primitives:
- `AtomicInteger.incrementAndGet()` uses CPU CAS instruction
- No lock acquisition = faster than `synchronized` for simple counters
- `AtomicBoolean` for shutdown flags visible across all threads instantly

### 5. ScheduledExecutorService
Precise fixed-rate scheduling for the monitor:
- `scheduleAtFixedRate()` fires exactly every N seconds
- More accurate than `while(true) { work(); sleep(N); }` loops
- Separate daemon threads = monitor never blocks the main system

### 6. Synchronized + wait() / notifyAll()
Used for the bounded queue capacity check:
- Producers `wait()` when queue is full (releases lock so workers can still fetch)
- Workers call `notifyAll()` after fetching (wakes blocked producers)
- Classic producer-consumer synchronization pattern

### 7. Shutdown Hook
Registered via `Runtime.getRuntime().addShutdownHook()`:
- Fires on Ctrl+C, kill signal, or System.exit()
- Ensures clean shutdown regardless of how JVM stops
- Ordered shutdown prevents task loss

## Race Condition Demo

This is one of the most important learning points in the project.

### What is a Race Condition?

When two threads read-modify-write the same variable without synchronization,
they can overwrite each other's changes:
Thread-1: reads unsafeCount = 5
Thread-2: reads unsafeCount = 5 ← same stale value!
Thread-1: writes unsafeCount = 6
Thread-2: writes unsafeCount = 6 ← Thread-1's increment is LOST!

Expected: 7 Actual: 6 ← 1 update lost

## Design Decisions

### Why PriorityBlockingQueue over LinkedBlockingQueue?
`LinkedBlockingQueue` is FIFO — first submitted is first processed.
`PriorityBlockingQueue` processes by urgency, which matches real-world
systems where payment jobs shouldn't wait behind cleanup jobs.

### Why immutable Task objects?
Once created, a Task never changes. This eliminates an entire class
of concurrency bugs — if no thread can modify an object, no thread
can corrupt it. The `incrementRetry()` method returns a NEW task
copy instead of modifying the original.

### Why ApplicationRunner over @PostConstruct?
`@PostConstruct` runs during bean initialization — some beans might
not be fully wired yet. `ApplicationRunner.run()` fires after the
entire Spring context is loaded, guaranteeing all dependencies are ready.

### Why ScheduledExecutorService for the monitor?
A manual `while(true) { log(); sleep(5000); }` loop drifts over time —
if logging takes 200ms, the next log fires at 5200ms, then 5400ms, etc.
`scheduleAtFixedRate()` fires at exactly 5000ms intervals regardless
of how long the task takes.

### Why a shutdown hook instead of just a finally block?
A `finally` block only runs when `run()` exits normally.
A shutdown hook fires on Ctrl+C, `kill`, or any other JVM termination.
This guarantees clean shutdown in all scenarios.

## Author

**[Ernest Danso Opoku]**
Software Developer — NovaTech Solutions Lab
Java 21 | Spring Boot 3.3 | Multithreaded Systems