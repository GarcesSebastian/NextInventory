package com.sebxstt.nextinventory.instances;

import com.sebxstt.nextinventory.NextInventory;

import java.util.ArrayList;
import java.util.UUID;

import static com.sebxstt.nextinventory.InventoryHelper.pagination;

public class NextPage {
    private UUID id;
    private int index;

    private Integer maxStack;
    private ArrayList<UUID> stack = new ArrayList<>();

    public NextPage (int maxStack) {
        this.id = UUID.randomUUID();
        this.maxStack = maxStack;
    }

    public NextPage clone() {
        NextPage cloned = new NextPage(this.maxStack);
        cloned.setIndex(this.index);
        cloned.setStack(new ArrayList<>(this.stack));
        return cloned;
    }

    public NextPage insert(NextItem instance) throws IllegalStateException {
        if (stack.size() > maxStack) {
            throw new IllegalStateException("This Page is Full");
        }

        if (instance.getCurrentPage() != null) {
            NextPage beforePage = pagination(instance.getCurrentPage(), instance.getParent());
            if (beforePage == null) throw new IllegalStateException("[NextPage] Not Page Found " + instance.getCurrentPage());
            beforePage.remove(instance);
        }

        this.stack.add(instance.getId());
        instance.setCurrentPage(this.index);
        return this;
    }

    public NextPage remove(NextItem instance) {
        this.stack.remove(instance.getId());
        instance.setCurrentPage(null);

        return this;
    }

    public NextPage index(int index) {
        this.index = index;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<UUID> getStack() {
        return stack;
    }

    public Integer getMaxStack() {
        return maxStack;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setStack(ArrayList<UUID> stack) {
        this.stack = stack;
    }
}
