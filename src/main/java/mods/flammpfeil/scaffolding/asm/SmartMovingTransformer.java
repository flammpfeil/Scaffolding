package mods.flammpfeil.scaffolding.asm;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.LocalVariablesSorter;


/**
 * Created by Furia on 14/11/03.
 */
public class SmartMovingTransformer implements IClassTransformer, Opcodes {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        try
        {
            final String targetClassName = "net.smart.moving.Orientation";
            if (targetClassName.equals(transformedName)) {
                System.out.println("start transform scaffolding > SmartMoving.Orientation");
                ClassReader classReader = new ClassReader(basicClass);
                ClassWriter classWriter = new ClassWriter(1);
                classReader.accept(new OrientationVisitor(targetClassName,classWriter), 8);
                return classWriter.toByteArray();
            }
        }
        catch (Exception e)
        {
            FMLLog.log("ScaffoldingCore", Level.ERROR,e,"smartmoving transform feild.");
        }

        return basicClass;
    }

    private class OrientationVisitor extends ClassVisitor {
        String owner;
        public OrientationVisitor(String owner, ClassVisitor cv) {
            super(Opcodes.ASM4,cv);
            this.owner = owner;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            String deobfName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc);
            String deobfDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
            String key = deobfName + deobfDesc;

            if("isOnVine(I)Z".equals(key)){
                mv = new MethodVisitorIsOnVine(access,deobfDesc,mv);
            }else if("isOnVineFront(I)Z".equals(key)){
                mv = new MethodVisitorIsOnVineFront(access,deobfDesc,mv);
            }else if("isVine(Lnet/minecraft/block/Block;)Z".equals(key)){
                mv = new MethodVisitorIsVine(access,deobfDesc,mv);
            }

            return mv;
        }

        private class MethodVisitorIsOnVine extends MethodVisitor{
            public MethodVisitorIsOnVine(int access, String desc, MethodVisitor mv) {
                super(Opcodes.ASM4, mv);
            }

            @Override
            public void visitCode() {
                super.visitCode();

                mv.visitVarInsn(ILOAD, 1);
                mv.visitFieldInsn(GETSTATIC, "net/smart/moving/Orientation", "world", "Lnet/minecraft/world/World;");
                mv.visitFieldInsn(GETSTATIC, "net/smart/moving/Orientation", "base_i", "I");
                mv.visitFieldInsn(GETSTATIC, "net/smart/moving/Orientation", "local_offset", "I");
                mv.visitFieldInsn(GETSTATIC, "net/smart/moving/Orientation", "base_k", "I");
                mv.visitMethodInsn(INVOKESTATIC, "mods/flammpfeil/scaffolding/asm/SmartMovingHandler", "isOnVine", "(ILnet/minecraft/world/World;III)Z");
                mv.visitInsn(IRETURN);
            }


            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
            }

            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                super.visitMaxs(maxStack + 5, maxLocals);
            }
        }
        private class MethodVisitorIsOnVineFront extends MethodVisitor{
            public MethodVisitorIsOnVineFront(int access, String desc, MethodVisitor mv) {
                super(Opcodes.ASM4, mv);
            }

            @Override
            public void visitCode() {
                super.visitCode();

                mv.visitInsn(ICONST_1);
                mv.visitInsn(IRETURN);

            }

            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {

            }
        }
        private class MethodVisitorIsVine extends MethodVisitor{
            public MethodVisitorIsVine(int access, String desc, MethodVisitor mv) {
                super(Opcodes.ASM4, mv);
            }

            @Override
            public void visitCode() {
                super.visitCode();

                mv.visitVarInsn(ILOAD, 0);
                mv.visitMethodInsn(INVOKESTATIC, "mods/flammpfeil/scaffolding/asm/SmartMovingHandler", "isVine", "(Lnet/minecraft/block/Block;)Z");
                mv.visitInsn(IRETURN);

            }

            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {

            }
        }
    }
}
