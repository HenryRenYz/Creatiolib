package com.henryrenyz.clib.MultiblockAPI.MachineAPI;

import com.henryrenyz.clib.MultiblockAPI.MultiblockStructure;
import org.bukkit.event.Event;

import java.util.Map;

public abstract class Machine{

    public abstract void Enable();

    public abstract void Tick();

    public abstract void Disable();

    public abstract void onDestroy(Event event);

    public abstract String getNameSpace();

    public abstract Map<String, Object> serialize();

    public abstract void deserialize(Map<String, Object> map);

    public abstract MultiblockStructure getHookedStructure();

}
