package vip.creatio.clib.basic.cmd.suggestion;

import vip.creatio.clib.basic.tools.Wrapper;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Suggestions implements Wrapper<com.mojang.brigadier.suggestion.Suggestions> {

    private static final Suggestions EMPTY = new Suggestions(StringRange.at(0), new ArrayList<>());
    
    private final com.mojang.brigadier.suggestion.Suggestions suggestions;
    
    public Suggestions(com.mojang.brigadier.suggestion.Suggestions raw) {
        this.suggestions = raw;
    }

    public Suggestions(StringRange range, Collection<Suggestion> suggestions) {
        this(new com.mojang.brigadier.suggestion.
                Suggestions(range.unwrap(), suggestions.stream().map(Suggestion::unwrap).collect(Collectors.toList())));
    }

    public StringRange getRange() {
        return new StringRange(suggestions.getRange());
    }

    public List<Suggestion> getList() {
        return suggestions.getList().stream().map(Suggestion::new).collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return suggestions.isEmpty();
    }

    public boolean equals(Object o) {
        if (o instanceof Suggestions) {
            return suggestions.equals(((Suggestions) o).unwrap());
        }
        return false;
    }

    public int hashCode() {
        return suggestions.hashCode() * 31;
    }

    public String toString() {
        return suggestions.toString();
    }

    public static CompletableFuture<Suggestions> empty() {
        return CompletableFuture.completedFuture(EMPTY);
    }

    public static Suggestions merge(String command, Collection<Suggestions> input) {
        return new Suggestions(com.mojang.brigadier.suggestion.Suggestions.
                merge(command, input.stream().map(Suggestions::unwrap).collect(Collectors.toList())));
    }

    public static Suggestions create(String command, Collection<Suggestion> suggestions) {
        return new Suggestions(com.mojang.brigadier.suggestion.Suggestions.
                create(command, suggestions.stream().map(Suggestion::unwrap).collect(Collectors.toList())));
    }
    
    @Override
    public com.mojang.brigadier.suggestion.Suggestions unwrap() {
        return suggestions;
    }

    @Override
    public Class<? extends com.mojang.brigadier.suggestion.Suggestions> wrappedClass() {
        return com.mojang.brigadier.suggestion.Suggestions.class;
    }
}
