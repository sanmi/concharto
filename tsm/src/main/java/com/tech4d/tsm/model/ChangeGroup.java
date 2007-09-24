package com.tech4d.tsm.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ForeignKey;

@Entity
public class ChangeGroup extends BaseEntity {
    public List<ChangeItem> changeItems;

    public ChangeGroup() {
        super();
    }

    @OneToMany(cascade = { CascadeType.ALL })
    @ForeignKey(name = "FK_CHANGEGRP_CHGITEM", inverseName = "FK_CHGITEM_CHANGEGRP")
    public List<ChangeItem> getChangeItems() {
        return changeItems;
    }

    public void setChangeItems(List<ChangeItem> changeItems) {
        this.changeItems = changeItems;
    }

}
