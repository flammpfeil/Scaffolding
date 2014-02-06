package mods.flammpfeil.scaffolding.asm;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import mods.flammpfeil.scaffolding.asm.CorePlugin;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;
public class ModContainer extends DummyModContainer
{
    public ModContainer()
    {
        super(new ModMetadata());
        getMetadata();
    }

    @Override
    public List<ArtifactVersion> getDependencies()
    {
        return super.getDependencies();
    }

    @Override
    public ModMetadata getMetadata()
    {
        ModMetadata meta = super.getMetadata();
        meta.modId       = "flammpfeil.scaffolding.core";
        meta.name        = "ScaffoldingCore";
        meta.version     = "1.7.2 r1";
        meta.authorList  = Arrays.asList("Ferne");
        meta.description = "";
        meta.url         = "";
        meta.credits     = "Furia";
        return meta;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

    @Override
    public File getSource()
    {
        return CorePlugin.location;
    }
}
