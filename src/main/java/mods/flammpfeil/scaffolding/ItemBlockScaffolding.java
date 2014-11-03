package mods.flammpfeil.scaffolding;


import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemBlockScaffolding extends ItemBlock
{
    public ItemBlockScaffolding(Block par1)
    {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }


    @Override
    public int getMetadata(int par1) {
    	return par1 == 5? 5 : 0;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
                                      EntityPlayer par3EntityPlayer)
    {
    	if(this.getMetadata(par1ItemStack.getItemDamage())==5){

            if (!par2World.isRemote && par3EntityPlayer.isSneaking()
                    && preReinforced(par1ItemStack, par3EntityPlayer, par2World))
            {
                par2World.updateEntity(par3EntityPlayer);
            }

            return par1ItemStack;
    	}else{
            ItemStack item = getItem(par3EntityPlayer, Items.stick);

            if (!par2World.isRemote && par3EntityPlayer.isSneaking() && item != null
                    && item.stackSize >= 2
                    && preReinforced(par1ItemStack, par3EntityPlayer, par2World))
            {
                par3EntityPlayer.inventory.consumeInventoryItem(item.getItem());
                par3EntityPlayer.inventory.consumeInventoryItem(item.getItem());
                par2World.updateEntity(par3EntityPlayer);
            }

            return par1ItemStack;
    	}
    }

    private MovingObjectPosition getMovingObjectPosition(World par2World, EntityPlayer par3EntityPlayer)
    {
        float f = 1.0F;
        float f1 = par3EntityPlayer.prevRotationPitch + (par3EntityPlayer.rotationPitch - par3EntityPlayer.prevRotationPitch) * f;
        float f2 = par3EntityPlayer.prevRotationYaw + (par3EntityPlayer.rotationYaw - par3EntityPlayer.prevRotationYaw) * f;
        double d0 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)f;
        double d1 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)f + 1.62D - (double)par3EntityPlayer.yOffset;
        double d2 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)f;
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
        return par2World.rayTraceBlocks(vec3, vec31, true);
    }

    public boolean preReinforced(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World)
    {
        MovingObjectPosition targetPos = this
                                         .getMovingObjectPosition(par3World, par2EntityPlayer);

        if (targetPos != null)
        {
            if (targetPos.typeOfHit == MovingObjectType.BLOCK)
            {
                int tagX = targetPos.blockX;
                int tagY = targetPos.blockY;
                int tagZ = targetPos.blockZ;

                if (!par3World.canMineBlock(par2EntityPlayer, tagX, tagY, tagZ))
                {
                    return false;
                }

                if (targetPos.sideHit != 1)
                {
                    return false;
                }

                if (!par2EntityPlayer.canPlayerEdit(tagX, tagY, tagZ,
                                                    targetPos.sideHit, par1ItemStack))
                {
                    return false;
                }

                Block var11 = par3World.getBlock(tagX, tagY, tagZ);

                if (var11 == Blocks.snow
                        || var11 == Blocks.vine
                        || var11 == Blocks.tallgrass
                        || var11 == Blocks.deadbush
                        || (var11.isReplaceable(par3World, tagX, tagY, tagZ) && var11.getMaterial().isSolid()))
                {
                    return false;
                }

                if (!par3World.isAirBlock(tagX, tagY, tagZ) && !par3World.isSideSolid(tagX, tagY, tagZ, ForgeDirection.UP) && par3World.isAirBlock(tagX, tagY + 1, tagZ))
                {
                    tagY += 1;
                    Block var12 = this.field_150939_a;
                    // int var13 =
                    // this.getMetadata(par1ItemStack.getItemDamage());
                    // int var14 =
                    // Block.blocksList[this.getBlockID()].func_85104_a(par3World,
                    // par4, par5, par6, par7, par8, par9, par10, var13);

                    if (placeBlockAt(par1ItemStack, par2EntityPlayer,
                                     par3World, tagX, tagY, tagZ, 1, tagX, tagY, tagZ, 5))
                    {
                        par3World.playSoundEffect(
                            (double)((float) tagX + 0.5F),
                            (double)((float) tagY + 0.5F),
                            (double)((float) tagZ + 0.5F),
                            var12.stepSound.func_150496_b(),
                            (var12.stepSound.getVolume() + 1.0F) / 2.0F,
                            var12.stepSound.getPitch() * 0.8F);
                        --par1ItemStack.stackSize;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public ItemStack getItem(EntityPlayer player, Item item)
    {
        for (int var2 = 0; var2 < player.inventory.mainInventory.length; ++var2)
        {
            if (player.inventory.mainInventory[var2] != null
                    && player.inventory.mainInventory[var2].getItem() == item)
            {
                return player.inventory.mainInventory[var2];
            }
        }

        return null;
    }

    @Override
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack,
                             EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
                             int par6, int par7, float par8, float par9, float par10)
    {
        if (!par3World.isRemote)
        {
            if (!par2EntityPlayer.isSneaking())
            {
                Block block = par3World.getBlock(par4, par5, par6);

                if (block == this.field_150939_a)
                {
                    int offset;

                    for (offset = 1; par3World
                            .getBlock(par4, par5 + offset, par6) == block; offset++)
                        ;

                    par5 += offset - 1;
                    par7 = 1;
                    Block var11 = par3World.getBlock(par4, par5, par6);

                    if (var11 == Blocks.snow)
                    {
                        par7 = 1;
                    }
                    else if (var11 != Blocks.vine
                             && var11 != Blocks.tallgrass
                             && var11 != Blocks.deadbush
                             && (!var11.isReplaceable(par3World, par4, par5, par6)))
                    {
                        if (par7 == 0)
                        {
                            --par5;
                        }

                        if (par7 == 1)
                        {
                            ++par5;
                        }

                        if (par7 == 2)
                        {
                            --par6;
                        }

                        if (par7 == 3)
                        {
                            ++par6;
                        }

                        if (par7 == 4)
                        {
                            --par4;
                        }

                        if (par7 == 5)
                        {
                            ++par4;
                        }
                    }

                    if (par1ItemStack.stackSize == 0)
                    {
                        return false;
                    }
                    else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6,
                             par7, par1ItemStack))
                    {
                        return false;
                    }
                    else if (par5 == 255
                             && this.field_150939_a.getMaterial()
                             .isSolid())
                    {
                        return false;
                    }
                    else if (par3World.canPlaceEntityOnSide(this.field_150939_a, par4, par5, par6, true, par7, par2EntityPlayer, par1ItemStack))
                    {
                        Block var12 = this.field_150939_a;
                        int var13 = this.getMetadata(par1ItemStack.getItemDamage());
                        int var14 = var12
                                    .onBlockPlaced(par3World, par4, par5, par6, par7,
                                                   par8, par9, par10, var13);

                        if (placeBlockAt(par1ItemStack, par2EntityPlayer,
                                         par3World, par4, par5, par6, par7, par8, par9,
                                         par10, var14))
                        {
                            par3World.playSoundEffect(
                                (double)((float) par4 + 0.5F),
                                (double)((float) par5 + 0.5F),
                                (double)((float) par6 + 0.5F),
                                var12.stepSound.func_150496_b(),
                                (var12.stepSound.getVolume() + 1.0F) / 2.0F,
                                var12.stepSound.getPitch() * 0.8F);
                            --par1ItemStack.stackSize;
                        }

                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
            else
            {
            	if(this.getMetadata(par1ItemStack.getItemDamage()) == 5){

                    if (!par3World.isRemote
                            && preReinforced(par1ItemStack, par2EntityPlayer, par3World))
                    {
                        par3World.updateEntity(par2EntityPlayer);
                        return true;
                    }
            	}else{

                    ItemStack item = getItem(par2EntityPlayer, Items.stick);

                    if (!par3World.isRemote
                            && item != null
                            && item.stackSize >= 2
                            && preReinforced(par1ItemStack, par2EntityPlayer, par3World))
                    {
                        par2EntityPlayer.inventory.consumeInventoryItem(item.getItem());
                        par2EntityPlayer.inventory.consumeInventoryItem(item.getItem());
                        par3World.updateEntity(par2EntityPlayer);
                        return true;
                    }
            	}
            }

            return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World,
                                   par4, par5, par6, par7, par8, par9, par10);
        }
        else
        {
            return false;
        }
    }


    @Override
    public boolean func_150936_a(World par1World, int par2, int par3,
                                           int par4, int par5, EntityPlayer par6EntityPlayer,
                                           ItemStack par7ItemStack)
    {
        return true;
    }


    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
                                World world, int x, int y, int z, int side, float hitX, float hitY,
                                float hitZ, int metadata)
    {
        if (metadata < 0)
        {
            return false;
        }

        return super.placeBlockAt(stack, player, world, x, y, z, side, hitX,
                                  hitY, hitZ, metadata);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
    	// TODO 自動生成されたメソッド・スタブ
    	return super.getUnlocalizedName(par1ItemStack) + (par1ItemStack.getItemDamage() == 5 ? ".reinforced" : "");
    }
}
