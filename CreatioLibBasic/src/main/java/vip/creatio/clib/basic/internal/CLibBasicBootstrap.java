package vip.creatio.clib.basic.internal;

import vip.creatio.clib.basic.tools.MsgManager;
import vip.creatio.clib.basic.tools.loader.BasicPluginBoostrap;
import vip.creatio.clib.basic.tools.loader.NmsClassLoader;
import vip.creatio.clib.basic.tools.loader.PluginInterface;

import java.util.List;

public class CLibBasicBootstrap extends BasicPluginBoostrap {

    static PluginInterface clibBasic;

    public CLibBasicBootstrap() {
        super(new NmsClassLoader(CLibBasicBootstrap.class), "vip.creatio.clib.basic.CLibBasic");
        if (clibBasic != null) throw new IllegalCallerException("Bootstrap constructor cannot be called twice!");
        clibBasic = delegate;
    }

    @Override
    protected void initNmsLoader() {
        loader.addIncludePath("vip.creatio.clib.basic");
        loader.addIncludePath("vip.creatio.accessor");
        loader.addGlobalPath("vip.creatio.accessor.global");
    }

}
