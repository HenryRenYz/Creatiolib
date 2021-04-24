package vip.creatio.clib.basic.chat;

import net.minecraft.server.ChatComponentScore;

public class ScoreComponent extends Component {

    ScoreComponent(ChatComponentScore raw) {
        super(raw);
    }

    public ScoreComponent(String selector, String objective) {
        super(new ChatComponentScore(selector, objective));
    }

    public String getName() {
        return ((ChatComponentScore) component).h();
    }

    public String getObjective() {
        return ((ChatComponentScore) component).j();
    }

    @Override
    public ScoreComponent plainCopy() {
        return new ScoreComponent(((ChatComponentScore) component).g());
    }

    @Override
    public ChatComponentScore unwrap() {
        return (ChatComponentScore) component;
    }
}
