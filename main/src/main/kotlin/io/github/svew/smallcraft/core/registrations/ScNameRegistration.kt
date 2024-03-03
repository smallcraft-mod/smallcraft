package io.github.svew.smallcraft.core.registrations

interface ScNameRegistration<T> : IScRegistration<T>
{
    /**
     * English translation of the resource being registered
     * Other language translations should be put into resource files
     */
    val name: String
}