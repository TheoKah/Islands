package com.carrot.islands.channel;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.channel.MutableMessageChannel;

public class IslandMessageChannel implements MutableMessageChannel {
    private Set<MessageReceiver> members;

    public IslandMessageChannel() {
        this(Collections.emptySet());
    }

    public IslandMessageChannel(Collection<MessageReceiver> members) {
        this.members = Collections.newSetFromMap(new WeakHashMap<>());
        this.members.addAll(members);
    }

    @Override
    public Collection<MessageReceiver> getMembers() {
        return Collections.unmodifiableSet(this.members);
    }

    @Override
    public boolean addMember(MessageReceiver member) {
        return this.members.add(member);
    }

    @Override
    public boolean removeMember(MessageReceiver member) {
        return this.members.remove(member);
    }

    @Override
    public void clearMembers() {
        this.members.clear();
    }
}
