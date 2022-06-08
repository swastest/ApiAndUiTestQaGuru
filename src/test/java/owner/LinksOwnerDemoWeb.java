package owner;

import org.aeonbits.owner.Config;
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:properties/links.properties"
})
public interface LinksOwnerDemoWeb extends Config {
    String uiURL();
    String apiURI();
}
