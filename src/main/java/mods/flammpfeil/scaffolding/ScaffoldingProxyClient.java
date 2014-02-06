package mods.flammpfeil.scaffolding;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class ScaffoldingProxyClient extends ScaffoldingProxy
{
    @Override
    public boolean isLadder(IBlockAccess world, int x, int y, int z,
                            EntityLivingBase entity, boolean def)
    {
        boolean result = def;

        if (entity instanceof EntityPlayerSP)
        {
            EntityPlayerSP sp = (EntityPlayerSP)entity;

            if (sp.movementInput.jump)
            {
                if (sp.movementInput.sneak)
                {
                    AxisAlignedBB bb = entity.boundingBox;
                    int mX = MathHelper.floor_double(bb.minX);
                    int mY = MathHelper.floor_double(bb.minY);
                    int mZ = MathHelper.floor_double(bb.minZ);

                    if (world.isAirBlock(mX, mY, mZ))
                    {
                        result = false;
                    }
                }
                else
                {
                    if (sp.motionY > 0)
                    {
                        sp.motionY = 0.41999998688697815D;

                        if (sp.isPotionActive(Potion.moveSpeed))
                        {
                            sp.motionY += (double)((float)(sp.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) * 0.1F);
                        }
                    }
                }
            }
            else
            {
                if (entity.isSneaking() && sp.isSwingInProgress)
                {
                    entity.onGround = true;
                }
            }
        }

        return result;
    }
}
