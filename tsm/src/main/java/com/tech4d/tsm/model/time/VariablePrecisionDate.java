package com.tech4d.tsm.model.time;

import java.util.Date;

import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

@Embeddable
public class VariablePrecisionDate {
    public static final int PRECISION_SECOND = 0;
    public static final int PRECISION_MINUTE = 1;
    public static final int PRECISION_HOUR = 2;
    public static final int PRECISION_DAY = 3;
    public static final int PRECISION_MONTH = 4;
    public static final int PRECISION_YEAR = 5;

	Integer precision = null;
	Date date;

	public VariablePrecisionDate() {
		super();
	}

	public VariablePrecisionDate(Date date) {
		this.date = date;
	}
	
	public VariablePrecisionDate(Date date, Integer precision) {
		this.date = date;
		this.precision = precision;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

    @Type(type = "com.tech4d.tsm.model.time.UtcDateTimeType")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
