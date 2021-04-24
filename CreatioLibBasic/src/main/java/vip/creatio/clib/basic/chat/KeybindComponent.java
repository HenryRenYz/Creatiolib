package vip.creatio.clib.basic.chat;

import net.minecraft.server.ChatComponentKeybind;

public class KeybindComponent extends Component {

    KeybindComponent(ChatComponentKeybind component) {
        super(component);
    }

    public KeybindComponent(String key) {
        super(new ChatComponentKeybind(key));
    }

    public String getName() {
        return ((ChatComponentKeybind) component).i();
    }

    @Override
    public KeybindComponent plainCopy() {
        return new KeybindComponent(((ChatComponentKeybind) component).g());
    }

    @Override
    public ChatComponentKeybind unwrap() {
        return (ChatComponentKeybind) component;
    }
}
