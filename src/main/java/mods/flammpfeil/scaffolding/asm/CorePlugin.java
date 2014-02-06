package mods.flammpfeil.scaffolding.asm;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions(value = {"mods.flammpfeil.scaffolding.asm"})
public class CorePlugin implements IFMLLoadingPlugin, IFMLCallHook
{
    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]
               {
                   "mods.flammpfeil.scaffolding.asm.Transformer"
               };
    }

    @Override
    public String getModContainerClass()
    {
        return "mods.flammpfeil.scaffolding.asm.ModContainer";
    }

    @Override
    public String getSetupClass()
    {
        return "mods.flammpfeil.scaffolding.asm.CorePlugin";
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
        if (data.containsKey("coremodLocation"))
        {
            location = (File) data.get("coremodLocation");
        }
    }

    @Override
    public Void call() throws Exception
    {
        return null;
    }

    public static File location;

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
