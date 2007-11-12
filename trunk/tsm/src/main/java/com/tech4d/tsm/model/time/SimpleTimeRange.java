package com.tech4d.tsm.model.time;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public interface SimpleTimeRange {

    @Temporal(TemporalType.TIMESTAMP)
    public VariablePrecisionDate getBegin();

    public void setBegin(VariablePrecisionDate begin);

    @Temporal(TemporalType.TIMESTAMP)
    public VariablePrecisionDate getEnd();

    public void setEnd(VariablePrecisionDate end);

}