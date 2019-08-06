package perf.ui.junit.agent;


import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Config.LoadPolicy;


@LoadPolicy(Config.LoadType.FIRST)
@Sources({ "classpath:perfui.properties"})
public interface PerfUIConfig extends Config {

    @Key("perfui.host")
    @DefaultValue("localhost")
    String host();

    @Key("perfui.port")
    @DefaultValue("8585")
    String port();

    @Key("perfui.protocol")
    @DefaultValue("http")
    String protocol();
}
