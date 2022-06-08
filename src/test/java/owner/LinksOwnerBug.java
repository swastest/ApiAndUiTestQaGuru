package owner;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:properties/linksBug.properties"
})
public interface LinksOwnerBug extends Config {
    String uiURL();
    String apiURI();
}
