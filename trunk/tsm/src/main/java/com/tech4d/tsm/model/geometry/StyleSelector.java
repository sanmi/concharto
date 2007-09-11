package com.tech4d.tsm.model.geometry;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.tech4d.tsm.model.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "styleSelectorType", discriminatorType = DiscriminatorType.STRING)
public class StyleSelector extends BaseEntity {

}
