package com.app.wte.ui;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

public class Category implements Serializable {

    private int id = -1;

    private int version;

    @NotNull
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass().equals(Category.class)) {
            Category category = (Category) obj;
            return category.getId() == getId() && category.version == version
                    && Objects.equals(category.getName(), getName());
        }
        return false;
    }
}
