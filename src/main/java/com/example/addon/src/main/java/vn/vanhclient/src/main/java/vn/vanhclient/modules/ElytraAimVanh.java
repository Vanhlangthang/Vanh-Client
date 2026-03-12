package vn.vanhclient.modules;

import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ElytraAimVanh extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder().name("Tầm quét").defaultValue(100).min(10).sliderMax(200).build());
    private final Setting<Double> predict = sgGeneral.add(new DoubleSetting.Builder().name("Dự đoán (Prediction)").defaultValue(0.15).min(0).sliderMax(1).build());
    private final Setting<Boolean> tracers = sgGeneral.add(new BoolSetting.Builder().name("Hiện đường kẻ (Tracers)").defaultValue(true).build());

    private PlayerEntity target;

    public ElytraAimVanh() {
        super(Categories.Combat, "vanh-elytra-pro", "Hệ thống Aim Macro mạnh nhất cho Vanh Client 1.21.4");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!mc.player.isFallFlying()) { target = null; return; }

        target = null;
        double closestDist = range.get();

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || !player.isAlive()) continue;
            double dist = mc.player.distanceTo(player);
            if (dist < closestDist) {
                closestDist = dist;
                target = player;
            }
        }

        if (target != null) {
            Vec3d velocity = new Vec3d(target.getX() - target.prevX, target.getY() - target.prevY, target.getZ() - target.prevZ);
            Vec3d targetPos = target.getPos().add(0, target.getEyeHeight(target.getPose()), 0).add(velocity.multiply(predict.get() * 10));
            Rotations.rotate(Rotations.getYaw(targetPos), Rotations.getPitch(targetPos), 100, null);
        }
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (target != null && tracers.get()) {
            event.renderer.line(mc.player.getEyePos().x, mc.player.getEyePos().y, mc.player.getEyePos().z, 
                target.getX(), target.getY() + target.getEyeHeight(target.getPose()), target.getZ(), Color.RED);
        }
    }
}
