package perf.ui.junit.agent.config;


import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Config.LoadPolicy;


@LoadPolicy(Config.LoadType.FIRST)
@Sources({"classpath:perfui.properties"})
public interface PerfUIConfig extends Config {

    @Key("perfui.host")
    default String host(){
        return System.getProperty("perfui.host","localhost");
    }

    @Key("perfui.port")
    default String port(){
        return System.getProperty("perfui.port","5000");
    }

    @Key("perfui.protocol")
    default String protocol(){
        return System.getProperty("perfui.protocol","http");
    }

    @Key("result.folder")
    default String folder(){
        return System.getProperty("result.folder","PerfUiResult");
    }
}
