package mods.flammpfeil.scaffolding;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.common.SidedProxy;

public class ScaffoldingProxy
{
    @SidedProxy(clientSide = "mods.flammpfeil.scaffolding.ScaffoldingProxyClient", serverSide = "mods.flammpfeil.scaffolding.ScaffoldingProxy")
    public static ScaffoldingProxy proxy;

    public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity, boolean def)
    {
        return def;
    }
}
