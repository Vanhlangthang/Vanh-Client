package vn.vanhclient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;
import vn.vanhclient.modules.ElytraAimVanh;
public class VanhClient extends MeteorAddon {
    @Override
    public void onInitialize() {
        Modules.get().add(new ElytraAimVanh());
    }
    @Override public String getPackage() { return "vn.vanhclient"; }
}
