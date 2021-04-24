package vip.creatio.clib.basic.cmd.suggestion;

import vip.creatio.clib.basic.chat.LiteralMessage;
import vip.creatio.clib.basic.chat.Message;
import vip.creatio.clib.basic.tools.Wrapper;

public class Suggestion implements Wrapper<com.mojang.brigadier.suggestion.Suggestion> {

    private final com.mojang.brigadier.suggestion.Suggestion suggestion;

    /** Suggestion is immutable so it's fine to copy */
    Suggestion(com.mojang.brigadier.suggestion.Suggestion raw) {
        this.suggestion = new com.mojang.brigadier.suggestion.
                Suggestion(raw.getRange(), raw.getText(), new LiteralMessage(raw.getTooltip().getString()));
    }

    public Suggestion(StringRange range, String text) {
        this(range, text, null);
    }

    public Suggestion(StringRange range, int value) {
        this(range, Integer.toString(value), null);
    }

    public Suggestion(StringRange range, String text, Message tooltip) {
        suggestion = new com.mojang.brigadier.suggestion.Suggestion(range.unwrap(), text, tooltip);
    }

    public Suggestion(StringRange range, int value, Message tooltip) {
        suggestion = new com.mojang.brigadier.suggestion.Suggestion(range.unwrap(), Integer.toString(value), tooltip);
    }

    public StringRange getRange() {
        return new StringRange(suggestion.getRange());
    }

    public String getText() {
        return suggestion.getText();
    }

    public Message getTooltip() {
        return (Message) suggestion.getTooltip();
    }

    public String apply(String input) {
        return suggestion.apply(input);
    }

    public boolean equals(Object o) {
        if (o instanceof Suggestion) {
            return suggestion.equals(((Suggestion) o).unwrap());
        }
        return false;
    }

    public int hashCode() {
        return suggestion.hashCode() * 31;
    }

    public String toString() {
        return suggestion.toString();
    }

    public int compareTo(Suggestion sug) {
        return getText().compareTo(sug.getText());
    }

    public int compareToIgnoreCase(Suggestion sug) {
        return getText().compareToIgnoreCase(sug.getText());
    }

    public Suggestion expand(String command, StringRange range) {
        return new Suggestion(suggestion.expand(command, range.unwrap()));
    }

    @Override
    public com.mojang.brigadier.suggestion.Suggestion unwrap() {
        return suggestion;
    }

    @Override
    public Class<? extends com.mojang.brigadier.suggestion.Suggestion> wrappedClass() {
        return com.mojang.brigadier.suggestion.Suggestion.class;
    }
}
