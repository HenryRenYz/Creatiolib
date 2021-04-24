package vip.creatio.clib.basic.chat;

import net.minecraft.server.ChatMessage;

public class TranslatableComponent extends Component {

    TranslatableComponent(ChatMessage msg) {
        super(msg);
    }

    public TranslatableComponent(String key) {
        super(new ChatMessage(key));
    }

    public TranslatableComponent(String key, Object... args) {
        super(new ChatMessage(key, args));
    }

    public String getKey() {
        return ((ChatMessage) component).getKey();
    }

    public Object[] getArgs() {
        return ((ChatMessage) component).getArgs();
    }

    @Override
    public TranslatableComponent plainCopy() {
        return new TranslatableComponent(((ChatMessage) component).g());
    }

    @Override
    public ChatMessage unwrap() {
        return (ChatMessage) component;
    }
}
