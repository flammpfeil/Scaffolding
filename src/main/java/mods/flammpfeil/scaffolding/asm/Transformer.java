package mods.flammpfeil.scaffolding.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class Transformer implements IClassTransformer , Opcodes
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        try
        {
            final String targetClassName = "net.minecraftforge.common.ForgeHooks";
            if (targetClassName.equals(transformedName)) {
            	System.out.println("start transform scaffolding > ForgeHooks");
                ClassReader classReader = new ClassReader(bytes);
                ClassWriter classWriter = new ClassWriter(1);
                classReader.accept(new forgeHooksVisitor(name,classWriter), 8);
                return classWriter.toByteArray();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("failed : scaffolding loading", e);
        }

        return bytes;
    }

    class forgeHooksVisitor extends ClassVisitor
    {
    	String owner;
    	public forgeHooksVisitor(String owner ,ClassVisitor cv)
    	{
    		super(Opcodes.ASM4,cv);
    		this.owner = owner;
    	}

    	static final String targetMethodName = "isLivingOnLadder";
    	static final String targetMethoddesc = "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLivingBase;)Z";

    	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
    	{

    		if (targetMethodName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))
                && targetMethoddesc.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc)))
    		{
    			MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

    			//mv.visitCode();

    			mv.visitVarInsn(ALOAD, 0);
    			mv.visitVarInsn(ALOAD, 1);
    			mv.visitVarInsn(ILOAD, 2);
    			mv.visitVarInsn(ILOAD, 3);
    			mv.visitVarInsn(ILOAD, 4);
    			mv.visitVarInsn(ALOAD, 5);
    			mv.visitMethodInsn(INVOKESTATIC, "mods/flammpfeil/scaffolding/Scaffolding", "isLivingOnLadder", "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLivingBase;)Z");
    			mv.visitInsn(IRETURN);

    			return mv;
    		}
    		return super.visitMethod(access, name, desc, signature, exceptions);
    	}
    }

    /*
    private byte[] replaceClass(String name, String transformedName, byte[] bytes) throws IOException
    {
        ZipFile zf = null;
        InputStream zi = null;

        try
        {
            zf = new ZipFile(CorePlugin.location);
            // 差し替え後のファイルです。coremodのjar内のパスを指定します。
            ZipEntry ze = zf.getEntry("ForgeHooks.class");

            if (ze != null)
            {
                zi = zf.getInputStream(ze);
                byte[] modded = new byte[(int) ze.getSize()];
                zi.read(modded);
                String targetMethodName = "isLivingOnLadder";
                String targetMethoddesc = "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLivingBase;)Z";
                // ASMで、bytesに格納されたクラスファイルを解析します。
                ClassNode cnode = new ClassNode();
                ClassReader reader = new ClassReader(bytes);
                reader.accept(cnode, 0);
                // 対象のメソッドを検索取得します。
                MethodNode mnode = null;

                for (MethodNode curMnode : (List<MethodNode>) cnode.methods)
                {
                    if (targetMethodName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, curMnode.name, curMnode.desc))
                            && targetMethoddesc.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(curMnode.desc)))
                    {
                        mnode = curMnode;
                        break;
                    }
                }

                // ASMで、bytesに格納されたクラスファイルを解析します。
                ClassNode cnodeM = new ClassNode();
                ClassReader readerM = new ClassReader(modded);
                readerM.accept(cnodeM, 0);
                // 対象のメソッドを検索取得します。
                MethodNode mnodeM = null;

                for (MethodNode curMnode : (List<MethodNode>) cnodeM.methods)
                {
                    if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc))
                    {
                        mnodeM = curMnode;
                        break;
                    }
                }

                if (mnode != null && mnodeM != null)
                {
                    InsnList overrideList = new InsnList();
                    mnode.desc = targetMethoddesc;
                    mnode.instructions = mnodeM.instructions;
                    // 改変したクラスファイルをバイト列に書き出します
                    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                    cnode.accept(cw);
                    bytes = cw.toByteArray();
                }
            }

            return bytes;
        }
        finally
        {
            if (zi != null)
            {
                zi.close();
            }

            if (zf != null)
            {
                zf.close();
            }
        }
    }*/
}
