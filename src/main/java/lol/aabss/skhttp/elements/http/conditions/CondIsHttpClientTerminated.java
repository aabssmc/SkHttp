package lol.aabss.skhttp.elements.http.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpClient;

@Name("Is Terminated")
@Description({
        "Returns true if the http client has been closed/shutdown.",
        "Required Java 21+"
})
@Examples({
        "if {_client} is not terminated:",
        "\tclose {_client}"
})
@Since("1.5")
public class CondIsHttpClientTerminated extends PropertyCondition<HttpClient> {

    static {
        if (Skript.methodExists(HttpClient.class, "isTerminated")) {
            register(CondIsHttpClientTerminated.class, "terminated", "httpclients");
        }
    }

    @Override
    public boolean check(HttpClient httpClient) {
        return httpClient.isTerminated();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "terminated";
    }
}
