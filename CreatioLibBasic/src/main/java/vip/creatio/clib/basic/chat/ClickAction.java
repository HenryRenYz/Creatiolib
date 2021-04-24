package vip.creatio.clib.basic.chat;

import vip.creatio.clib.basic.tools.Wrapper;
import net.minecraft.server.ChatClickable;

public enum ClickAction implements Wrapper<ChatClickable.EnumClickAction> {
    OPEN_URL,
    OPEN_FILE,
    RUN_COMMAND,
    SUGGEST_COMMAND,
    CHANGE_PAGE,
    COPY_TO_CLIPBOARD;

    private final ChatClickable.EnumClickAction action;

    ClickAction() {
        this.action = ChatClickable.EnumClickAction.valueOf(name());
    }

    public boolean isAllowedFromServer() {
        return action.a();
    }

    public String getName() {
        return action.b();
    }

    @Override
    public ChatClickable.EnumClickAction unwrap() {
        return action;
    }

    @Override
    public Class<? extends ChatClickable.EnumClickAction> wrappedClass() {
        return ChatClickable.EnumClickAction.class;
    }


}
