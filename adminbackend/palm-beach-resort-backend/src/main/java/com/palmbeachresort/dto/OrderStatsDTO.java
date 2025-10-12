package com.palmbeachresort.dto;

public class OrderStatsDTO {
    private long total;
    private long ordered;
    private long preparing;
    private long done;
    private long delivering;
    private long delivered;
    private long urgent;

    // Constructors
    public OrderStatsDTO() {}

    public OrderStatsDTO(long total, long ordered, long preparing, long done,
                         long delivering, long delivered, long urgent) {
        this.total = total;
        this.ordered = ordered;
        this.preparing = preparing;
        this.done = done;
        this.delivering = delivering;
        this.delivered = delivered;
        this.urgent = urgent;
    }

    // Getters and Setters
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public long getOrdered() { return ordered; }
    public void setOrdered(long ordered) { this.ordered = ordered; }

    public long getPreparing() { return preparing; }
    public void setPreparing(long preparing) { this.preparing = preparing; }

    public long getDone() { return done; }
    public void setDone(long done) { this.done = done; }

    public long getDelivering() { return delivering; }
    public void setDelivering(long delivering) { this.delivering = delivering; }

    public long getDelivered() { return delivered; }
    public void setDelivered(long delivered) { this.delivered = delivered; }

    public long getUrgent() { return urgent; }
    public void setUrgent(long urgent) { this.urgent = urgent; }
}