package vip.creatio.clib.basic.chat;

import net.minecraft.server.ChatComponentSelector;

public class SelectorComponent extends Component {

    SelectorComponent(ChatComponentSelector raw) {
        super(raw);
    }

    public SelectorComponent(String selector) {
        super(new ChatComponentSelector(selector));
    }

    public String getPattern() {
        return ((ChatComponentSelector) component).h();
    }

    public String getContents() {
        return unwrap().getText();
    }

    @Override
    public SelectorComponent plainCopy() {
        return new SelectorComponent(((ChatComponentSelector) component).g());
    }

    @Override
    public ChatComponentSelector unwrap() {
        return (ChatComponentSelector) component;
    }
}
