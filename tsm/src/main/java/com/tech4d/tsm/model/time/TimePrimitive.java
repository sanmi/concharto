package com.tech4d.tsm.model.time;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.tech4d.tsm.model.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "timePrimativeType", discriminatorType = DiscriminatorType.INTEGER)
public abstract class TimePrimitive extends BaseEntity {

}
