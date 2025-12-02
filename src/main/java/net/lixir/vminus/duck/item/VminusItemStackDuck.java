package net.lixir.vminus.duck.item;


public interface VminusItemStackDuck {
    default boolean vminus$isBanned() {
        return false;
    }
}
