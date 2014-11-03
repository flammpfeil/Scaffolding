package mods.flammpfeil.scaffolding.asm;

import mods.flammpfeil.scaffolding.Scaffolding;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

/**
 * Created by Furia on 14/11/03.
 */
public class SmartMovingHandler {

    /*
    static public int xx;
    static public World ww;

    public boolean isOnVine(int offset){
        return isOnVine(offset,ww,xx,xx,xx);
    }
    */
    static public boolean isOnVine(int offset,World w, int x,int y, int z){
        Block block = w.getBlock(x,y+offset,z);
        return Scaffolding.isLivingOnLadder(block,w,x,y+offset,z,Minecraft.getMinecraft().thePlayer);
    }

    static public boolean isVine(Block block){
        return block == Blocks.vine || Scaffolding.vineLikeBlocks.contains(block);
    }
}
