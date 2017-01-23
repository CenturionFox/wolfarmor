package com.attributestudios.wolfarmor.client;

import com.attributestudios.wolfarmor.client.renderer.entity.layer.LayerWolfArmor;
import com.attributestudios.wolfarmor.client.renderer.entity.layer.LayerWolfBackpack;
import com.attributestudios.wolfarmor.common.CommonProxy;
import com.attributestudios.wolfarmor.item.ItemWolfArmor;
import com.attributestudios.wolfarmor.item.WolfArmorItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Loads client-specific mod data
 */
@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    //region Public / Protected Methods

    /**
     * Registers entity renderers for this mod.
     */
    @Override
    protected void registerEntityRenderingHandlers() {    	
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        RenderWolf renderWolf = (RenderWolf)renderManager.entityRenderMap.get(EntityWolf.class);
        
        renderWolf.addLayer(new LayerWolfArmor(renderWolf));
        renderWolf.addLayer(new LayerWolfBackpack(renderWolf));
        
        if (Loader.isModLoaded("SophisticatedWolves")) {
        	RenderLiving<? extends EntityWolf> sRender = null;
			try {
				Class clazz = Class.forName("sophisticated_wolves.entity.EntitySophisticatedWolf");
				sRender = (RenderLiving<? extends EntityWolf>)renderManager.entityRenderMap.get(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        	if (sRender != null) {
        		 sRender.addLayer(new LayerWolfArmor(sRender));
        		 sRender.addLayer(new LayerWolfBackpack(sRender));
        	}
        	
        }
    }

    /**
     * Registers item renderers for this mod.
     * @param initializationEvent the initialization event
     */
    @Override
    protected void registerItemRenderers(@Nonnull FMLPreInitializationEvent initializationEvent) {
        WolfArmorItems.registerItemModel(WolfArmorItems.LEATHER_WOLF_ARMOR, 0);
        WolfArmorItems.registerItemModel(WolfArmorItems.CHAINMAIL_WOLF_ARMOR, 0);
        WolfArmorItems.registerItemModel(WolfArmorItems.IRON_WOLF_ARMOR, 0);
        WolfArmorItems.registerItemModel(WolfArmorItems.GOLDEN_WOLF_ARMOR, 0);
        WolfArmorItems.registerItemModel(WolfArmorItems.DIAMOND_WOLF_ARMOR, 0);
    }

    @Override
    protected void registerItemColorHandlers(@Nonnull FMLInitializationEvent initializationEvent) {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(@Nonnull ItemStack stack, int tintIndex) {
                return tintIndex > 0 ? -1 : ((ItemWolfArmor)stack.getItem()).getColor(stack);
            }
        }, WolfArmorItems.LEATHER_WOLF_ARMOR);
    }

    //endregion Public / Protected Methods
}
