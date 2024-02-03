package net.shieldbreak.stopp2w.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.shieldbreak.stopp2w.StopP2W;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {

    @Shadow
    @Final
    private Screen parent;
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

        if(StopP2W.state == StopP2W.State.DISABLED) {
            icon = DISABLED_TEXTURE;
        }
        if(StopP2W.state == StopP2W.State.ENABLED) {
            icon = ENABLED_TEXTURE;
        }
        if(StopP2W.state == StopP2W.State.UNTESTED) {
            icon = UNLISTED_TEXTURE;
        }


        this.addButton(new TexturedButtonWidget(this.width / 2 - 95 + 255,this.height-47, 32, 32, 0, 0, 32, icon, 32, 64, (button) -> {
            if(StopP2W.state == StopP2W.State.DISABLED) {
                StopP2W.state = StopP2W.State.ENABLED;
                this.client.openScreen(this.parent);
                this.client.openScreen(new MultiplayerScreen(this));
                return;
            }
            if(StopP2W.state == StopP2W.State.ENABLED) {
                StopP2W.state = StopP2W.State.UNTESTED;
                this.client.openScreen(this.parent);
                this.client.openScreen(new MultiplayerScreen(this));
                return;
            }
            if(StopP2W.state == StopP2W.State.UNTESTED) {
                StopP2W.state = StopP2W.State.DISABLED;
                this.client.openScreen(this.parent);
                this.client.openScreen(new MultiplayerScreen(this));
                return;
            }
        }));
    }
}