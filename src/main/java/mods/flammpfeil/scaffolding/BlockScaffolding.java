package mods.flammpfeil.scaffolding;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockScaffolding extends Block implements IFuelHandler
{
    public BlockScaffolding()
    {
        super(Material.cloth);
    }

    @Override
    public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {

        boolean result = true;
        entity.fallDistance = 0;

        result = ScaffoldingProxy.proxy.isLadder(world, x, y, z, entity, result);

        return result;
    }
    @Override
    public MovingObjectPosition collisionRayTrace(World p_149731_1_,
    		int p_149731_2_, int p_149731_3_, int p_149731_4_,
    		Vec3 p_149731_5_, Vec3 p_149731_6_) {

    	if(Scaffolding.nowRender)
    		return null;
    	return super.collisionRayTrace(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_, p_149731_5_, p_149731_6_);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        return pass <= 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isBlockNormalCube()
    {
        return false;
    }

    @Override
    public boolean isNormalCube()
    {
    	//息できる
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
    	//向こうが透けない
        return false;
    }

    @Override
   public boolean isNormalCube(IBlockAccess world, int x, int y, int z)
   {
        //ブロック置ける
       return false;
   }

    @Override
    public boolean renderAsNormalBlock()
    {
    	//1x1ブロックサイズブロックである
        return true;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z,
    		ForgeDirection side) {

        return world.getBlockMetadata(x, y, z) == 5 ? true : (side == ForgeDirection.DOWN) || (side == ForgeDirection.UP);
    }


    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        if (par7Entity != null && par7Entity instanceof EntityPlayer  && par7Entity.motionY > 0 && par7Entity.motionX == 0 && par7Entity.motionZ == 0)
        {
            return;
        }

        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
    }

    protected IIcon side;
    protected IIcon top;

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
    	//super.registerBlockIcons(p_149651_1_);

        this.side = p_149651_1_.registerIcon("flammpfeil.scaffolding:side");
        this.blockIcon = p_149651_1_.registerIcon("flammpfeil.scaffolding:sideR");
        this.top = p_149651_1_.registerIcon("flammpfeil.scaffolding:top");
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
    		int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1,0));

        if (metadata == 5)
        {
            ret.add(new ItemStack(Items.stick, 2));
        }

        return ret;
    }


    @Override
    public void onBlockClicked(World par1World, int par2, int par3, int par4,
                               EntityPlayer par5EntityPlayer)
    {
        int meta = par1World.getBlockMetadata(par2, par3, par4);
        ItemStack sitem = par5EntityPlayer.getCurrentEquippedItem();

        if (meta == 2 && !par1World.isAirBlock(par2, par3 - 1, par4) && sitem != null && sitem.getItem() == Items.stick && sitem.stackSize >= 2)
        {
            par5EntityPlayer.getCurrentEquippedItem().stackSize -= 2;
            par1World.setBlock(par2, par3, par4, this, 5, 3);
        }
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3,
                                    int par4, EntityPlayer par5EntityPlayer, int par6, float par7,
                                    float par8, float par9)
    {
        int meta = par1World.getBlockMetadata(par2, par3, par4);
        ItemStack sitem = par5EntityPlayer.getCurrentEquippedItem();

        if (meta == 2 && !par1World.isAirBlock(par2, par3 - 1, par4) && sitem != null && sitem.getItem() == Items.stick && sitem.stackSize >= 2)
        {
            par5EntityPlayer.getCurrentEquippedItem().stackSize -= 2;
            par1World.setBlock(par2, par3, par4, this, 5, 3);
        }

        // TODO 自動生成されたメソッド・スタブ
        return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer,
                                      par6, par7, par8, par9);
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        if (!par1World.isRemote)
        {
            int meta = par1World.getBlockMetadata(par2, par3, par4);
            boolean isStay = false;

            isStay = par1World.isSideSolid(par2, par3 - 1, par4, ForgeDirection.UP)
                     || (par1World.getBlock(par2 - 1, par3, par4) == this && par1World.getBlockMetadata(par2 - 1, par3, par4) > meta)
                     || (par1World.getBlock(par2 + 1, par3, par4) == this && par1World.getBlockMetadata(par2 + 1, par3, par4) > meta)
                     || (par1World.getBlock(par2, par3, par4 - 1) == this && par1World.getBlockMetadata(par2, par3, par4 - 1) > meta)
                     || (par1World.getBlock(par2, par3, par4 + 1) == this && par1World.getBlockMetadata(par2, par3, par4 + 1) > meta)
                     || (meta == 5 && !par1World.isAirBlock(par2, par3 - 1, par4));
            //par1World.getBlockMaterial(par2, par3-1, par4) == Material.water && par1World.getBlockMetadata(par2, par3-1, par4) == 0

            if (!isStay)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                par1World.setBlock(par2, par3, par4, Blocks.air, 0, 3);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_,
    		int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_,
    		ItemStack p_149689_6_) {
    	super.onBlockPlacedBy(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_,
    			p_149689_5_, p_149689_6_);
    	onNeighborBlockChange(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_,this);
    }

    @Override
    public int onBlockPlaced(World par1World, int par2,
    		int par3, int par4, int p_149660_5_,
    		float p_149660_6_, float p_149660_7_, float p_149660_8_,
    		int par9) {

        int calcMeta = -1;

        if (par9 == 5)
        {
            calcMeta = 5;
        }
        else if (par1World.isSideSolid(par2, par3 - 1, par4, ForgeDirection.UP))
        {
            if (par1World.getBlock(par2, par3 - 1, par4) == this)
            {
                calcMeta = Math.min(2, par1World.getBlockMetadata(par2, par3 - 1, par4));
            }
            else
            {
                calcMeta = 2;
            }
        }
        else
        {
            if (par1World.getBlock(par2 - 1, par3, par4) == this)
            {
                calcMeta = Math.max(par1World.getBlockMetadata(par2 - 1, par3, par4) - 1, calcMeta);
            }

            if (par1World.getBlock(par2 + 1, par3, par4) == this)
            {
                calcMeta = Math.max(par1World.getBlockMetadata(par2 + 1, par3, par4) - 1, calcMeta);
            }

            if (par1World.getBlock(par2, par3, par4 - 1) == this)
            {
                calcMeta = Math.max(par1World.getBlockMetadata(par2, par3, par4 - 1) - 1, calcMeta);
            }

            if (par1World.getBlock(par2, par3, par4 + 1) == this)
            {
                calcMeta = Math.max(par1World.getBlockMetadata(par2, par3, par4 + 1) - 1, calcMeta);
            }
        }

        return calcMeta;
    }

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        if (par1 == 1 || par1 == 0)
        {
            return this.top;
        }
        else if (par2 >= 5)
        {
            return this.blockIcon;
        }

        return this.side;
    }

    @Override
    public int getBurnTime(ItemStack fuel)
    {
        return fuel.getItem() == new ItemStack(this).getItem() ? 100 : 0;
    }

}
