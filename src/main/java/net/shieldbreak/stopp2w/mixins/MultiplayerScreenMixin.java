package net.shieldbreak.stopp2w.mixins;


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

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {

    @Shadow @Final private Screen parent;
    private static final Identifier ENABLED_TEXTURE = new Identifier("stopp2w:gui/enabled.png");
    private static final Identifier DISABLED_TEXTURE = new Identifier("stopp2w:gui/disabled.png");
    private static final Identifier UNLISTED_TEXTURE = new Identifier("stopp2w:gui/neutral.png");


    private Identifier currentIcon = null;


    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"),method = "init")

    private void addCustomButtons(CallbackInfo ci) {

        Identifier icon = null;

        if(Main.state == Main.State.DISABLED) {
            icon = DISABLED_TEXTURE;
        }
        if(Main.state == Main.State.ENABLED) {
            icon = ENABLED_TEXTURE;
        }
        if(Main.state == Main.State.UNTESTED) {
            icon = UNLISTED_TEXTURE;
        }


        this.addDrawableChild(new TexturedButtonWidget(this.width / 2 - 95 + 255,this.height-47, 32, 32, 0, 0, 32, icon, 32, 64, (button) -> {
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