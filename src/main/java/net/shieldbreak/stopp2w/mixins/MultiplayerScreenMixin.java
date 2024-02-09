package net.shieldbreak.stopp2w.mixins;

import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
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


    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"),method = "init")

    private void addCustomButtons(CallbackInfo ci) {


        Text text = null;

        if(Main.state == Main.State.DISABLED) {
            text = Text.of("Disabled");
        }
        if(Main.state == Main.State.ENABLED) {
            text = Text.of("Enabled");
        }
        if(Main.state == Main.State.UNTESTED) {
            text = Text.of("Untested");
        }

        this.addDrawableChild(ButtonWidget.builder(text, (button) -> {
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
        }).width(120).build());
    }
}