package mods.flammpfeil.scaffolding;


import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(name = "Scaffolding", modid = "flammpfeil.scaffolding", useMetadata = false, version = "mc1.7.2 r2")
public class Scaffolding
{
    public static BlockScaffolding blockScaffolding;

    //public static Configuration mainConfiguration;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {
    	/*
        mainConfiguration = new Configuration(event.getSuggestedConfigurationFile());

        try
        {
            mainConfiguration.load();
            Property propBlockId;
            propBlockId = mainConfiguration.get("","Scaffolding", blockId);
            blockId = propBlockId.getInt();
        }
        finally
        {
            mainConfiguration.save();
        }
        */

    	blockScaffolding = (BlockScaffolding)new BlockScaffolding().setHardness(0.1F).setStepSound(Block.soundTypeWood).setBlockName("flammpfeil.scaffolding").setBlockTextureName("flammpfeil.scaffolding:sideR").setCreativeTab(CreativeTabs.tabDecorations);

    	blockScaffolding = (BlockScaffolding)GameRegistry.registerBlock(blockScaffolding, ItemBlockScaffolding.class, "scaffolding");

    	Blocks.fire.setFireInfo(blockScaffolding, 5, 20);

        GameRegistry.registerFuelHandler(blockScaffolding);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockScaffolding, 8), "III", "# #", "III",
                               '#', "stickWood",
                               'I', "slabWood"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockScaffolding, 9), blockScaffolding, "logWood"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockScaffolding, 3), blockScaffolding, "plankWood"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockScaffolding, 2), blockScaffolding, "slabWood"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockScaffolding, 2), blockScaffolding, "stickWood"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockScaffolding, 1,5), blockScaffolding, "stickWood", "stickWood"));

        GameRegistry.addRecipe(new ItemStack(Items.stick, 2), "#", "#", '#', new ItemStack(blockScaffolding, 1));

        FMLCommonHandler.instance().bus().register(this);
    }

    public static boolean isLivingOnLadder(Block block, World world, int x, int y, int z, EntityLivingBase entity)
    {
    	AxisAlignedBB bb = entity.boundingBox;
        bb = bb.expand(0.5, 0, 0.5);
        int mX = MathHelper.floor_double(bb.minX);
        int mY = MathHelper.floor_double(bb.minY);
        int mZ = MathHelper.floor_double(bb.minZ);

        for (int y2 = mY; y2 < bb.maxY; y2++)
        {
            for (int x2 = mX; x2 < bb.maxX; x2++)
            {
                for (int z2 = mZ; z2 < bb.maxZ; z2++)
                {
                	Block curBlock = world.getBlock(x2, y2, z2);
                    if (curBlock.isLadder(world, x2, y2, z2, entity))
                    {
                        if (curBlock.renderAsNormalBlock() || (x2 == x && z2 == z))
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    static public boolean nowRender = false;

    @SubscribeEvent()
    public void onTickEventRenderTickEvent(TickEvent.RenderTickEvent event){
    	if(event.phase == Phase.START){
    		nowRender = true;
    	}else{
    		nowRender = false;
    	}
    }
}
