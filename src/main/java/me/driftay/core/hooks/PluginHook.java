package me.driftay.core.hooks;

public interface PluginHook<T> {

    T setup();

    String getName();


}
