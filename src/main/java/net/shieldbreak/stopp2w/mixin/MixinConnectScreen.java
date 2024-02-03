package net.shieldbreak.stopp2w.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.shieldbreak.stopp2w.StopP2W;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Mixin(ConnectScreen.class)
public class MixinConnectScreen extends Screen {
    @Shadow
    Screen parent;

    protected MixinConnectScreen(Text title,Screen parent) {
        super(title);
        this.parent = parent;
    }


    @Inject(method = "connect", at = @At("HEAD"), cancellable = true)
    private void onConnect(String address, int port, CallbackInfo ci) {
        try {
            if (StopP2W.state == StopP2W.State.ENABLED) {
                String endpoint = "https://wireway.ch/api/stopp2w/check/?ip=" + address + "&user=" +MinecraftClient.getInstance().getSession().getUsername();
                URL url = new URL(endpoint);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String response = in.readLine();
                in.close();

                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(response).getAsJsonObject();
                boolean block = json.get("block").getAsBoolean();
                String reason = json.get("reason").getAsString();
                String message = json.get("message").getAsString();

                if (block) {

                    StopP2W.logger().info("Blocking server " + address);
                    MinecraftClient client = MinecraftClient.getInstance();
                    assert this.client != null;
                    this.client.openScreen(new DisconnectedScreen(parent, ScreenTexts.CONNECT_FAILED, Text.of("§c§lServer was blocked by StopP2W\n\n§7This Server is suspected to be Pay To Win.\nIf you think this is an error please send us a message on Discord: https://discord.gg/p2tqycDqHY\n\n§aServer: §7" + address + "\n §aReason: §7" + reason + "\n\n"+message)) {
                    });
                    ci.cancel();
                }
            }
            if (StopP2W.state == StopP2W.State.UNTESTED) {
                String endpoint = "https://wireway.ch/api/stopp2w/check/?ip=" + address+"&untested=true" + "&user=" +MinecraftClient.getInstance().getSession().getUsername();
                URL url = new URL(endpoint);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String response = in.readLine();
                in.close();

                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(response).getAsJsonObject();
                boolean block = json.get("block").getAsBoolean();
                String reason = json.get("reason").getAsString();
                String message = json.get("message").getAsString();

                if (block) {

                    StopP2W.logger().info("Blocking server " + address);
                    MinecraftClient client = MinecraftClient.getInstance();
                    assert this.client != null;
                    this.client.openScreen(new DisconnectedScreen(parent, ScreenTexts.CONNECT_FAILED, Text.of("§c§lServer was blocked by StopP2W\n\n§7This Server is suspected to be Pay To Win.\nIf you think this is an error please send us a message on Discord: https://discord.gg/p2tqycDqHY\n\n§aServer: §7" + address + "\n §aReason: §7" + reason + "\n\n"+message)) {
                    });
                    ci.cancel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}