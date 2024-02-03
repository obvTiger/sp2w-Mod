package net.shieldbreak.stopp2w.mixins;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;

import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.shieldbreak.stopp2w.Main;
import org.spongepowered.asm.mixin.Final;
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
public class MixinConnectScreen {
    @Final
    @Shadow
    Screen parent;


    @Inject(method = "connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;)V", at = @At("HEAD"), cancellable = true)
    private void onConnect(MinecraftClient client, ServerAddress address, ServerInfo info, CallbackInfo ci) {
        try {
            if (Main.state == Main.State.ENABLED) {
                String endpoint = "https://wireway.ch/api/stopp2w/check/?ip=" + address.getAddress() + "&user=" +MinecraftClient.getInstance().getSession().getUsername();
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

                    Main.logger().info("Blocking server " + address.getAddress());
                    client.setScreen(new DisconnectedScreen(this.parent, ScreenTexts.CONNECT_FAILED, Text.of("§c§lServer was blocked by StopP2W\n\n§7This Server is suspected to be Pay To Win.\nIf you think this is an error please send us a message on Discord: https://discord.gg/p2tqycDqHY\n\n§aServer: §7" + address.getAddress() + "\n §aReason: §7" + reason + "\n\n"+message)) {
                    });
                    ci.cancel();

                }
            }
            if (Main.state == Main.State.UNTESTED) {
                String endpoint = "https://wireway.ch/api/stopp2w/check/?ip=" + address.getAddress()+"&untested=true" + "&user=" +MinecraftClient.getInstance().getSession().getUsername();
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

                    Main.logger().info("Blocking server " + address.getAddress());
                    client.setScreen(new DisconnectedScreen(this.parent, ScreenTexts.CONNECT_FAILED, Text.of("§c§lServer was blocked by StopP2W\n\n§7This Server is suspected to be Pay To Win.\nIf you think this is an error please send us a message on Discord: https://discord.gg/p2tqycDqHY\n\n§aServer: §7" + address.getAddress() + "\n §aReason: §7" + reason + "\n\n"+message)) {
                    });
                    ci.cancel();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
