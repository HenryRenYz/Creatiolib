package vip.creatio.clib.basic.chat;

import vip.creatio.clib.basic.tools.Wrapper;
import net.minecraft.server.ChatClickable;

public class ClickEvent implements Wrapper<ChatClickable> {

    private final ClickAction action;
    private final ChatClickable event;

    public ClickEvent(ClickAction action, String value) {
        this.action = action;
        this.event = new ChatClickable(action.unwrap(), value);
    }

    ClickEvent(ChatClickable event) {
        this.action = ClickAction.valueOf(event.a().name());
        this.event = event;
    }

    public String getValue() {
        return event.b();
    }

    public ClickAction getAction() {
        return action;
    }

    @Override
    public String toString() {
        return event.toString();
    }

    @Override
    public int hashCode() {
        return event.hashCode() * 31;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ClickEvent) {
            return event.equals(((ClickEvent) o).event);
        }
        return false;
    }

    @Override
    public ChatClickable unwrap() {
        return event;
    }

    @Override
    public Class<? extends ChatClickable> wrappedClass() {
        return ChatClickable.class;
    }

    public static ClickEvent openUrl(String url) {
        return new ClickEvent(ClickAction.OPEN_URL, url);
    }

    public static ClickEvent openFile(String file) {
        return new ClickEvent(ClickAction.OPEN_FILE, file);
    }

    public static ClickEvent runCmd(String cmd) {
        return new ClickEvent(ClickAction.RUN_COMMAND, cmd);
    }

    public static ClickEvent suggestCmd(String cmd) {
        return new ClickEvent(ClickAction.SUGGEST_COMMAND, cmd);
    }

    public static ClickEvent changePage(int page) {
        return new ClickEvent(ClickAction.CHANGE_PAGE, Integer.toString(page));
    }

    public static ClickEvent copyToClipboard(String content) {
        return new ClickEvent(ClickAction.COPY_TO_CLIPBOARD, content);
    }
}
