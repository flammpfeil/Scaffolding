package mods.flammpfeil.scaffolding;


import com.google.common.collect.Lists;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
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

import java.util.List;

@Mod(name = "Scaffolding", modid = "flammpfeil.scaffolding", useMetadata = false, version = "mc1.7.2 r2")
public class Scaffolding
{
    public static BlockScaffolding blockScaffolding;

    public static Configuration mainConfiguration;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {

        mainConfiguration = new Configuration(evt.getSuggestedConfigurationFile());


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

    /**
     * [\] -> [\\]
     * ["] -> [\quot;]
     * 改行 -> [\r;\r;]
     * 全文を""でquotationする
     * 上記のとおり、エスケープされます。直接configを修正するときに覚えておくべき。
     * @param source
     * @return
     */
    static private String escape(String source){
        return String.format("\"%s\"", source.replace("\\","\\\\").replace("\"","\\quot;").replace("\r", "\\r;").replace("\n", "\\n;"));
    }
    static private String unescape(String source){
        return source.replace("\"", "").replace("\\quot;", "\"").replace("\\r;","\r").replace("\\n;","\n").replace("\\\\", "\\");
    }

    static public List<Block> vineLikeBlocks = Lists.newArrayList();
    @EventHandler
    public void postInit(FMLPostInitializationEvent evt){
        try
        {
            mainConfiguration.load();

            String[] strings = new String[]{escape("minecraft:vine"),escape("flammpfeil.scaffolding:scaffolding")};

            Property prop;
            prop = mainConfiguration.get(Configuration.CATEGORY_GENERAL,"VineLikeBlocks", strings);
            strings = prop.getStringList();

            for(String name : strings){
                name = unescape(name);
                Block block = Block.getBlockFromName(name);
                if(block == null) continue;
                if(block == Blocks.air) continue;

                vineLikeBlocks.add(block);
            }

        }
        finally
        {
            mainConfiguration.save();
        }
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
                        if (curBlock == blockScaffolding || (x2 == x && z2 == z))
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
