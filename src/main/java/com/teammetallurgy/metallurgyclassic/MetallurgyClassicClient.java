package com.teammetallurgy.metallurgyclassic;

import com.teammetallurgy.metallurgyclassic.client.render.entities.CustomizableTntRenderer;
import com.teammetallurgy.metallurgyclassic.debug.OreScannerKeybind;
import com.teammetallurgy.metallurgyclassic.machines.chest.MetalChestComponent;
import com.teammetallurgy.metallurgyclassic.machines.crusher.CrusherComponent;
import com.teammetallurgy.metallurgyclassic.machines.furnace.MetalFurnaceComponent;
import com.teammetallurgy.metallurgyclassic.materials.MetallurgyArmorMaterial;
import com.teammetallurgy.metallurgyclassic.mixin.ArmorFeatureRendererAccessor;
import com.teammetallurgy.metallurgyclassic.network.CustomizableTntSpawnPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.teammetallurgy.metallurgyclassic.MetallurgyClassic.id;

@Environment(EnvType.CLIENT)
public class MetallurgyClassicClient implements ClientModInitializer {

    public static void preRegisterArmorTextures(Identifier name, boolean hasOverlay) {
        for (int layer = 1; layer <= 2; layer++) {
            preRegisterArmorTexture(name, layer, null);
            if (hasOverlay) {
                preRegisterArmorTexture(name, layer, "overlay");
            }
        }
    }

    public static void preRegisterArmorTexture(Identifier name, int layer, @Nullable String extra) {
        String key = "textures/models/armor/" + name.getPath() + "_layer_" + layer + (extra == null ? "" : "_" + extra) + ".png";
        Identifier value = new Identifier(name.getNamespace(), "textures/models/armor/" + name.getPath() + "_layer_" + layer + (extra == null ? "" : "_" + extra) + ".png");
        ArmorFeatureRendererAccessor.getArmorTextureCache().put(key, value);
    }

    @Override
    public void onInitializeClient() {
        MetalRegistry.instance().setOreRenderLayers();
        EntityRendererRegistry.INSTANCE.register(MetallurgyClassic.TNT_ENTITY_TYPE, CustomizableTntRenderer::new);
        ClientSidePacketRegistry.INSTANCE.register(CustomizableTntSpawnPacket.ID, CustomizableTntSpawnPacket::onPacket);

        MetalFurnaceComponent.onInitializerClient();
        MetalChestComponent.onInitializerClient();
        CrusherComponent.onInitializerClient();

        OreScannerKeybind.setup();

        MetalRegistry.instance().fixArmorTextures();
    }
}
