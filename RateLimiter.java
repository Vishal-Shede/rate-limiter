/**
 * A simple Token Bucket Rate Limiter implementation.
 * Controls how many requests are allowed within a given time
 * by maintaining a bucket of tokens. Each request consumes one token.
 * Tokens are refilled at a fixed rate up to a maximum capacity.
 */
public class RateLimiter {

    // Maximum number of tokens the bucket can hold (burst capacity).
    private final int capacity;

    // Number of tokens added to the bucket every second (refill rate).
    private final double refillPerSecond;

    // Current number of tokens available in the bucket.
    private double tokens;

    // The last time (in nanoseconds) when the bucket was refilled.
    private long lastRefillNanos;

    /**
     * Creates a new RateLimiter.
     * 
     * @param capacity        maximum number of tokens in the bucket
     * @param refillPerSecond number of tokens added per second
     */
    public RateLimiter(int capacity, double refillPerSecond) {
        this.capacity = capacity;
        this.refillPerSecond = refillPerSecond;
        this.tokens = capacity;                   // bucket starts full
        this.lastRefillNanos = System.nanoTime(); // start time
    }

    /**
     * Tries to allow a request.
     * 
     * @return true if the request is allowed (token consumed),
     *         false if the bucket is empty (request rejected).
     */
    public boolean allow() {
        refill();
        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

  
    //Refills the bucket based on elapsed time since last refill.   
    private void refill() {
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastRefillNanos) / 1_000_000_000.0;
        tokens = Math.min(capacity, tokens + elapsedSeconds * refillPerSecond);
        lastRefillNanos = now;
    }
}


class Main {
    public static void main(String[] args) throws Exception {
        // Capacity = 3 tokens, refill = 1 token/sec
        RateLimiter rl = new RateLimiter(3, 1.0);

        // Burst of 5 requests
        for (int i = 1; i <= 5; i++) {
            System.out.println("call " + i + " -> " + rl.allow());
        }

        // Wait to let one token refill
        Thread.sleep(1100);
        System.out.println("after 1.1s -> " + rl.allow());
    }
}
