package com.tech4d.tsm.model;

import javax.persistence.Embeddable;

@Embeddable
public class Votes {
    Integer positive;

    Integer negative;

    public Votes() {
        super();
    }

    public Votes(Integer positive, Integer negative) {
        super();
        this.positive = positive;
        this.negative = negative;
    }

    public Integer getNegative() {
        return negative;
    }

    public void setNegative(Integer negative) {
        this.negative = negative;
    }

    public Integer getPositive() {
        return positive;
    }

    public void setPositive(Integer positive) {
        this.positive = positive;
    }

}
