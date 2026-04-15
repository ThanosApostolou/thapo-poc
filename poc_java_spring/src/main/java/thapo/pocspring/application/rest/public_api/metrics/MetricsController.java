package thapo.pocspring.application.rest.public_api.metrics;

import io.micrometer.core.instrument.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import thapo.pocspring.application.rest.public_api.PublicApiController;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Example controller that showcases different Micrometer metric types.
 * <p>
 * Hit these endpoints and then check {@code /pocspring/actuator/prometheus}
 * to see the recorded metrics.
 */
@Slf4j
@RestController
@RequestMapping(value = MetricsController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class MetricsController {
    public static final String PATH = PublicApiController.PATH + "/metrics";
    public static final String PATH_COUNTER = "/counter";
    public static final String PATH_TIMER = "/timer";
    public static final String PATH_GAUGE = "/gauge";
    public static final String PATH_SUMMARY = "/summary";
    public static final String PATH_EVENT = "/event";
    public static final String PATH_ALL = "/all";

    // ── Constants ─────────────────────────────────────────────────────

    private static final String TAG_CONTROLLER = "controller";
    private static final String TAG_CONTROLLER_VALUE = "MetricsController";
    private static final String TAG_EVENT = "event";
    private static final String KEY_METRIC = "metric";
    private static final String KEY_COUNT = "count";
    private static final String METRIC_EVENTS = "metrics_demo.events";

    // ── Metric objects ──────────────────────────────────────────────────

    private final Counter requestCounter;
    private final Timer processTimer;
    private final DistributionSummary orderSummary;
    private final AtomicInteger activeUsers = new AtomicInteger(0);
    private final Map<String, Counter> eventCounters = new ConcurrentHashMap<>();
    private final MeterRegistry registry;

    public MetricsController(MeterRegistry registry) {
        this.registry = registry;

        // Counter – monotonically increasing value
        this.requestCounter = Counter.builder("metrics_demo.requests")
                .description("Total number of demo requests received")
                .tag(TAG_CONTROLLER, TAG_CONTROLLER_VALUE)
                .register(registry);

        // Timer – measures latency / duration of operations
        this.processTimer = Timer.builder("metrics_demo.process.duration")
                .description("Time spent processing a simulated task")
                .tag(TAG_CONTROLLER, TAG_CONTROLLER_VALUE)
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);

        // Gauge – value that can go up and down
        Gauge.builder("metrics_demo.active_users", activeUsers, AtomicInteger::get)
                .description("Current number of simulated active users")
                .tag(TAG_CONTROLLER, TAG_CONTROLLER_VALUE)
                .register(registry);

        // Distribution Summary – distribution of values (e.g. order amounts)
        this.orderSummary = DistributionSummary.builder("metrics_demo.order.amount")
                .description("Distribution of simulated order amounts")
                .baseUnit("USD")
                .tag(TAG_CONTROLLER, TAG_CONTROLLER_VALUE)
                .publishPercentiles(0.5, 0.95)
                .register(registry);
    }

    // ── 1. Counter endpoint ─────────────────────────────────────────────

    /**
     * Each call increments a simple counter.
     * <p>
     * Prometheus metric: {@code metrics_demo_requests_total}
     */
    @GetMapping(PATH_COUNTER)
    public ResponseEntity<Map<String, Object>> counter() {
        requestCounter.increment();
        log.info("Counter incremented to {}", requestCounter.count());
        return ResponseEntity.ok(Map.of(
                KEY_METRIC, "metrics_demo.requests",
                "type", "Counter",
                "currentCount", requestCounter.count()
        ));
    }

    // ── 2. Timer endpoint ───────────────────────────────────────────────

    /**
     * Simulates a task with random latency and records it in a Timer.
     * <p>
     * Prometheus metrics: {@code metrics_demo_process_duration_seconds_*}
     */
    @GetMapping(PATH_TIMER)
    public ResponseEntity<Map<String, Object>> timer() {
        long sleepMs = ThreadLocalRandom.current().nextLong(50, 500);

        processTimer.record(() -> {
            try {
                Thread.sleep(sleepMs);
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
            }
        });

        log.info("Timer recorded {}ms", sleepMs);
        return ResponseEntity.ok(Map.of(
                KEY_METRIC, "metrics_demo.process.duration",
                "type", "Timer",
                "simulatedLatencyMs", sleepMs,
                "totalTimeSeconds", processTimer.totalTime(java.util.concurrent.TimeUnit.SECONDS),
                KEY_COUNT, processTimer.count(),
                "maxSeconds", processTimer.max(java.util.concurrent.TimeUnit.SECONDS)
        ));
    }

    // ── 3. Gauge endpoint ───────────────────────────────────────────────

    /**
     * Simulates users logging in ({@code action=login}) or out ({@code action=logout}).
     * The gauge value goes up and down.
     * <p>
     * Prometheus metric: {@code metrics_demo_active_users}
     *
     * @param action "login" (default) or "logout"
     */
    @GetMapping(PATH_GAUGE)
    public ResponseEntity<Map<String, Object>> gauge(
            @RequestParam(defaultValue = "login") String action
    ) {
        int current;
        if ("logout".equalsIgnoreCase(action)) {
            current = activeUsers.updateAndGet(v -> Math.max(0, v - 1));
        } else {
            current = activeUsers.incrementAndGet();
        }

        log.info("Gauge – active users: {} (action={})", current, action);
        return ResponseEntity.ok(Map.of(
                KEY_METRIC, "metrics_demo.active_users",
                "type", "Gauge",
                "action", action,
                "activeUsers", current
        ));
    }

    // ── 4. Distribution Summary endpoint ────────────────────────────────

    /**
     * Records a random "order amount" in a distribution summary.
     * <p>
     * Prometheus metrics: {@code metrics_demo_order_amount_*}
     *
     * @param amount optional fixed amount; otherwise a random value 5–500 is used
     */
    @GetMapping(PATH_SUMMARY)
    public ResponseEntity<Map<String, Object>> summary(
            @RequestParam(required = false) Double amount
    ) {
        double orderAmount = (amount != null)
                ? amount
                : ThreadLocalRandom.current().nextDouble(5.0, 500.0);

        orderSummary.record(orderAmount);

        log.info("Summary recorded order amount: {}", orderAmount);
        return ResponseEntity.ok(Map.of(
                KEY_METRIC, "metrics_demo.order.amount",
                "type", "DistributionSummary",
                "recordedAmount", orderAmount,
                KEY_COUNT, orderSummary.count(),
                "totalAmount", orderSummary.totalAmount(),
                "mean", orderSummary.mean(),
                "max", orderSummary.max()
        ));
    }

    // ── 5. Dynamic tags / multi-counter endpoint ────────────────────────

    /**
     * Demonstrates dynamic tagging: each unique {@code event} value creates its own
     * counter time-series.
     * <p>
     * Prometheus metric: {@code metrics_demo_events_total{event="…"}}
     *
     * @param event the event name (e.g. "click", "purchase", "signup")
     */
    @GetMapping(PATH_EVENT)
    public ResponseEntity<Map<String, Object>> event(
            @RequestParam(defaultValue = "click") String event
    ) {
        Counter eventCounter = eventCounters.computeIfAbsent(event, e ->
                Counter.builder(METRIC_EVENTS)
                        .description("Counts of custom events by name")
                        .tag(TAG_EVENT, e)
                        .register(registry)
        );
        eventCounter.increment();

        log.info("Event '{}' counter incremented to {}", event, eventCounter.count());
        return ResponseEntity.ok(Map.of(
                KEY_METRIC, METRIC_EVENTS,
                "type", "Counter (dynamic tag)",
                TAG_EVENT, event,
                KEY_COUNT, eventCounter.count()
        ));
    }

    // ── 6. All-in-one demo endpoint ─────────────────────────────────────

    /**
     * Hits every metric type at once and returns a combined overview.
     */
    @GetMapping(PATH_ALL)
    public ResponseEntity<Map<String, Object>> all() {
        // Counter
        requestCounter.increment();

        // Timer
        processTimer.record(Duration.ofMillis(ThreadLocalRandom.current().nextLong(10, 200)));

        // Gauge
        int users = activeUsers.incrementAndGet();

        // Summary
        double orderAmount = ThreadLocalRandom.current().nextDouble(10.0, 300.0);
        orderSummary.record(orderAmount);

        // Dynamic event
        Counter allCounter = eventCounters.computeIfAbsent("all-in-one", e ->
                Counter.builder(METRIC_EVENTS)
                        .description("Counts of custom events by name")
                        .tag(TAG_EVENT, e)
                        .register(registry)
        );
        allCounter.increment();

        log.info("All metrics updated in a single call");
        return ResponseEntity.ok(Map.of(
                "counter", requestCounter.count(),
                "timerCount", processTimer.count(),
                "activeUsers", users,
                "orderSummaryCount", orderSummary.count(),
                "lastOrderAmount", orderAmount,
                "allInOneEventCount", allCounter.count()
        ));
    }
}
