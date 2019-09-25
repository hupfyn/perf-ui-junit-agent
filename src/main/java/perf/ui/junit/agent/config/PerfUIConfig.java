package perf.ui.junit.agent.config;


import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Config.LoadPolicy;


@LoadPolicy(Config.LoadType.FIRST)
@Sources({"classpath:perfui.properties"})
public interface PerfUIConfig extends Config {

    @Key("perfui.webhook")
    @DefaultValue("http://localhost:5000/perfui/analyze")
    String webHook();

    @Key("result.folder")
    @DefaultValue("PerfUiResult")
    String folder();

    @Key("video.framerate")
    @DefaultValue("1")
    String frameRate();

    @Key("video.display")
    String videoDisplay();

    @Key("perfui.loadtimeout")
    @DefaultValue("1")
    int loadTimeOut();
}
