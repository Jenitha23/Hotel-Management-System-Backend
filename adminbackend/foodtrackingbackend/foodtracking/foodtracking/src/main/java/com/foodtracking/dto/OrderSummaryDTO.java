package com.foodtracking.dto;

public class OrderSummaryDTO {
    private long preparing;
    private long ready;
    private long outForDelivery;
    private long delivered;

    public OrderSummaryDTO() {}

    public OrderSummaryDTO(long preparing, long ready, long outForDelivery, long delivered) {
        this.preparing = preparing;
        this.ready = ready;
        this.outForDelivery = outForDelivery;
        this.delivered = delivered;
    }

    // Getters and Setters
    public long getPreparing() { return preparing; }
    public void setPreparing(long preparing) { this.preparing = preparing; }

    public long getReady() { return ready; }
    public void setReady(long ready) { this.ready = ready; }

    public long getOutForDelivery() { return outForDelivery; }
    public void setOutForDelivery(long outForDelivery) { this.outForDelivery = outForDelivery; }

    public long getDelivered() { return delivered; }
    public void setDelivered(long delivered) { this.delivered = delivered; }
}