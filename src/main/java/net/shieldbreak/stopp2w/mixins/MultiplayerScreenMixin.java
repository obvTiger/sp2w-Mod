package net.shieldbreak.stopp2w.mixins;

import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.shieldbreak.stopp2w.Main;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {

    @Shadow
    @Final
    private Screen parent;
    private static final Identifier ENABLED_TEXTURE = new Identifier("stopp2w","textures/gui/enabled.png");
    private static final Identifier ENABLED_TEXTURE_FOCUSED = new Identifier("stopp2w","textures/gui/enabled_focused.png");
    private static final Identifier DISABLED_TEXTURE = new Identifier("stopp2w","textures/gui/disabled.png");
    private static final Identifier DISABLED_TEXTURE_FOCUSED = new Identifier("stopp2w","textures/gui/disabled_focused.png");
    private static final Identifier UNLISTED_TEXTURE = new Identifier("stopp2w","textures/gui/neutral.png");
    private static final Identifier UNLISTED_TEXTURE_FOCUSED = new Identifier("stopp2w","textures/gui/neutral_focused.png");


    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"),method = "init")

    private void addCustomButtons(CallbackInfo ci) {


        ButtonTextures icon = null;

        if(Main.state == Main.State.DISABLED) {
            icon = new ButtonTextures(DISABLED_TEXTURE,DISABLED_TEXTURE_FOCUSED);
        }
        if(Main.state == Main.State.ENABLED) {
            icon = new ButtonTextures(ENABLED_TEXTURE,ENABLED_TEXTURE_FOCUSED);
        }
        if(Main.state == Main.State.UNTESTED) {
            icon = new ButtonTextures(UNLISTED_TEXTURE,UNLISTED_TEXTURE_FOCUSED);
        }

        this.addDrawableChild(new TexturedButtonWidget(this.width / 2 - 95 + 255,this.height-47, 32, 32, icon, (button) -> {
            if(Main.state == Main.State.DISABLED) {
                Main.state = Main.State.ENABLED;
                this.client.setScreen(this.parent);
                this.client.setScreen(new MultiplayerScreen(this));
                return;
            }
            if(Main.state == Main.State.ENABLED) {
                Main.state = Main.State.UNTESTED;
                this.client.setScreen(this.parent);
                this.client.setScreen(new MultiplayerScreen(this));
                return;
            }
            if(Main.state == Main.State.UNTESTED) {
                Main.state = Main.State.DISABLED;
                this.client.setScreen(this.parent);
                this.client.setScreen(new MultiplayerScreen(this));
                return;
            }
        }));
    }
}