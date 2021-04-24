package vip.creatio.clib.basic.chat;

import net.minecraft.server.ChatComponentText;

public class TextComponent extends Component {
    public static final Component EMPTY = new TextComponent("");

    TextComponent(ChatComponentText text) {
        super(text);
    }

    public TextComponent(String text) {
        super(new ChatComponentText(text));
    }

    public String getContents() {
        return ((ChatComponentText) component).h();
    }

    @Override
    public TextComponent plainCopy() {
        return new TextComponent(((ChatComponentText) component).g());
    }

    @Override
    public ChatComponentText unwrap() {
        return (ChatComponentText) component;
    }
}
